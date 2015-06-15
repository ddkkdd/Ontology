package com.vaadin.tutorial.addressbook.backend;

import java.io.Serializable;

import org.apache.commons.beanutils.BeanUtils;

public class OWLConcept implements Serializable, Cloneable {
	public OWLConcept(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	private String name = "";
	private String value = "";



	@Override
	public OWLConcept clone() throws CloneNotSupportedException {
		try {
			//return (OWLConcept) BeanUtils.cloneBean(this);
			return this;
		} catch (Exception ex) {
			throw new CloneNotSupportedException();
		}
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getValue() {
		return value;
	}



	public void setValue(String value) {
		this.value = value;
	}



}
