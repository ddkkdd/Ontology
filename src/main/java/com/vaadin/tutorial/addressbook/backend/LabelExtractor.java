package com.vaadin.tutorial.addressbook.backend;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.util.OWLObjectVisitorExAdapter;

class LabelExtractor extends OWLObjectVisitorExAdapter<String> implements
		OWLAnnotationObjectVisitorEx<String> {
	@Override
	public String visit(OWLAnnotation annotation) {
		if (annotation.getProperty().isLabel()) {
			OWLLiteral c = (OWLLiteral) annotation.getValue();
			return c.getLiteral();
		}
		return null;
	}
}