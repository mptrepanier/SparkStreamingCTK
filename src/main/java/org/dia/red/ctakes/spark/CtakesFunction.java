/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dia.red.ctakes.spark;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

//import org.apache.ctakes.typesystem.type.textsem.AnatomicalSiteMention;
//import org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention;
//import org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation;
//import org.apache.ctakes.typesystem.type.textsem.SignSymptomMention;
//import org.apache.ctakes.typesystem.type.textsem.ProcedureMention;
import org.apache.ctakes.typesystem.type.refsem.OntologyConcept;
import org.apache.ctakes.typesystem.type.textsem.*;


import org.apache.uima.UIMAException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.util.XMLSerializer;
import org.apache.spark.api.java.function.Function;

import it.cnr.iac.CTAKESClinicalPipelineFactory;
import org.json.*;

/**
 * @author Selina Chu, Michael Starch, and Giuseppe Totaro
 *
 */


public class CtakesFunction implements Function<String, String> {

	transient JCas jcas = null;
	transient AnalysisEngineDescription aed = null;

	private void setup() throws UIMAException {
		System.setProperty("ctakes.umlsuser", "balahota1");
		System.setProperty("ctakes.umlspw", "Ru$h@ccessUM!S");
		this.jcas = JCasFactory.createJCas();
		this.aed = CTAKESClinicalPipelineFactory.getDefaultPipeline();

	}
	
	private void readObject(ObjectInputStream in) {
		try {
			in.defaultReadObject();
			this.setup();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UIMAException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String call(String paragraph) throws Exception {

		this.jcas.setDocumentText(paragraph);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SimplePipeline.runPipeline(this.jcas, this.aed);
		FSIndex index = this.jcas.getAnnotationIndex(IdentifiedAnnotation.type);
		Iterator iter = index.iterator();


		JSONArray annotationsArray = new JSONArray();
		JSONObject allAnnotations = new JSONObject();

		ArrayList<String> types = new ArrayList<String>();

		types.add("org.apache.ctakes.typesystem.type.textsem.SignSymptomMention");
		types.add("org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention");
		types.add("org.apache.ctakes.typesystem.type.textsem.AnatomicalSiteMention");
		types.add("org.apache.ctakes.typesystem.type.textsem.ProcedureMention");
		types.add("import org.apache.ctakes.typesystem.type.textsem.MedicationMention");

		String type;
		String[] splitType;
		FSArray snomedArray;
		ArrayList<String> snomedStringArray = new ArrayList<String>();

		while (iter.hasNext()){
			IdentifiedAnnotation annotation = (IdentifiedAnnotation)iter.next();
			type = annotation.getType().toString();
			if (types.contains(type)){
				JSONObject annotations  = new JSONObject();

				splitType = type.split("[.]");
				annotations.put("id", annotation.getId());
				annotations.put("subject", annotation.getSubject());
				annotations.put("type", splitType[splitType.length - 1]);
				annotations.put("text", annotation.getCoveredText());
				annotations.put("polarity", annotation.getPolarity());
				annotations.put("confidence", annotation.getConfidence());

				snomedArray = annotation.getOntologyConceptArr();
				for (int i = 0; i < snomedArray.size(); i++){
					snomedStringArray.add(((OntologyConcept)snomedArray.get(i)).getCode());
				}
				annotations.put("snomed_codes", snomedStringArray);
				snomedStringArray.clear();
				annotationsArray.put(annotations);
			}

		}

		allAnnotations.put("Annotations", annotationsArray);
		this.jcas.reset();
		return allAnnotations.toString();
	}

}
