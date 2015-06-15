package com.vaadin.tutorial.addressbook.backend;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

//#!java
public class OWLInfo extends CustomField<OWLConcept> {

	@Override
	protected Component initContent() {
		return null;
	}

	@Override
	public Class<OWLConcept> getType() {
		return OWLConcept.class;
	}
}
