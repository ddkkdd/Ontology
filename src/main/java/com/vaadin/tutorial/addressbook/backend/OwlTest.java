/* This is based on ... 
 * 
 * 
 * 
 * 
 * 
 * file is part of the OWL API.
 * The contents of this file are subject to the LGPL License, Version 3.0.
 * Copyright 2014, The University of Manchester
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0 in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. */
package com.vaadin.tutorial.addressbook.backend;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.OWLObjectVisitorExAdapter;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;

public class OwlTest {

	public static void printHeading(String str) {
		System.out
				.println("************************************************\n* "
						+ str
						+ "\n************************************************");
	}

	public static void printEnd() {
		System.out
				.println("------------------------------------------------\n\n");
	}

	/*
	 * Vorbereitung:
	 * 
	 * Download des HermiT-Reasoners von http://hermit-reasoner.com/
	 * org.semanticweb.HermiT.jar in das Java-Projekt einbinden
	 */
	public static void main(String[] args) throws OWLException, IOException {
		loadOntology();
	}	
		
	public static HashMap<Long, Individual> loadOntology() throws OWLOntologyCreationException{
		OWLOntologyManager m = OWLManager.createOWLOntologyManager();
		
		String file = "C:\\temp\\Mini2_OWL.owl";
		OWLOntology o = m.loadOntologyFromOntologyDocument(new File(file));

		OWLReasoner reasoner = new Reasoner(o);
		System.out.println("Reasoner-Name: " + reasoner.getReasonerName());
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

		

		OWLDataFactory factory = m.getOWLDataFactory();
		OWLClass Thing = factory.getOWLClass(IRI
				.create("http://www.w3.org/2002/07/owl#Thing"));
		Set<OWLNamedIndividual> individuals = reasoner.getInstances(Thing,
				false).getFlattened();
		
		
		System.out.println("hallo");
		System.out.println("Anzahl: "+individuals.size());
		for (OWLNamedIndividual ind : individuals) {
			
			System.out.println(ind.toString());
			Map<OWLDataPropertyExpression, Set<OWLLiteral>> dataProperties = ind.getDataPropertyValues(o);
			Map<OWLObjectPropertyExpression, Set<OWLIndividual>> objectProperties = ind.getObjectPropertyValues(o);
			NodeSet<OWLClass> classes = reasoner.getTypes(ind, true);
			
			
			System.out.println(classes);
			
			for (Node<OWLClass> n : classes){
			
				System.out.println(("PEHE WAS HERE->" +n.toString()));
			}

		}
		return null;
	}

	private static LabelExtractor le = new LabelExtractor();

	private static String labelFor(OWLEntity clazz, OWLOntology o) {
		Set<OWLAnnotation> annotations = clazz.getAnnotations(o);
		for (OWLAnnotation anno : annotations) {
			String result = anno.accept(le);
			if (result != null) {
				return result;
			}
		}
		return clazz.getIRI().toString();
	}

}