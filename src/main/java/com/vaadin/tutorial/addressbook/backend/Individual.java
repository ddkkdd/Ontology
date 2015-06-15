package com.vaadin.tutorial.addressbook.backend;

import org.apache.commons.beanutils.BeanUtils;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.reasoner.NodeSet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple DTO for the address book example.
 *
 * Serializable and cloneable Java Object that are typically persisted in the
 * database and can also be easily converted to different formats like JSON.
 */
// Backend DTO class. This is just a typical Java backend implementation
// class and nothing Vaadin specific.
public class Individual implements Serializable, Cloneable {

	private Long id;
	private String individualName;
	private List<OWLConcept> dataProperties;
	private List<OWLConcept> objectProperties;
	private List<String> classes;

	public Individual(Long j, String individualName, List<OWLConcept> dpm,
			List<OWLConcept> opm, List<String> classes2) {

		this.id = j;
		this.individualName = individualName;
		this.dataProperties = dpm;
		this.objectProperties = opm;
		this.classes = classes2;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Individual clone() throws CloneNotSupportedException {
		try {
			// return (Individual) BeanUtils.cloneBean(this);
			return this;
		} catch (Exception ex) {
			throw new CloneNotSupportedException();
		}
	}

	public List<OWLConcept> getDataProperties() {
		return dataProperties;
	}

	public void setDataProperties(List<OWLConcept> dataProperties) {
		this.dataProperties = dataProperties;
	}

	public List<OWLConcept> getObjectProperties() {
		return objectProperties;
	}

	public void setObjectProperties(List<OWLConcept> objectProperties) {
		this.objectProperties = objectProperties;
	}

	public List<String> getClasses() {
		return classes;
	}

	public void setClasses(List<String> classes) {
		this.classes = classes;
	}

	public Mitarbeiter createMitarbeiter() {

		Mitarbeiter m = new Mitarbeiter();

		m.setName(individualName.substring(individualName.lastIndexOf("#")));

		m.setEintrittsdatum(getPropertyValue("<http://www.semanticweb.org/semanticOrg#Erfahrungsjahre>"));
		m.setBeschreibung(getPropertyValue("<http://www.semanticweb.org/semanticOrg#Beschreibung>"));
		m.setGehalt(getPropertyValue("<http://www.semanticweb.org/semanticOrg#Gehalt>"));
		m.setId(id);
		return m;
	}

	public boolean isClassMember(String classname) {

		for (String s : classes) {
			if (s.equals(classname))
				return true;
		}

		return false;
	}

	public String getPropertyValue(String property) {

		for (OWLConcept it : objectProperties) {
			if (it.getName().equals(property)) {
				return it.getValue();
			}
		}

		for (OWLConcept it : dataProperties) {
			if (it.getName().equals(property)) {
				return it.getValue();
			}
		}

		return "";
	}

	@Override
	public String toString() {
		return "Individual [id=" + id + ", individualName=" + individualName
				+ ", dataProperties=" + dataProperties + ", objectProperties="
				+ objectProperties + ", classes=" + classes + "]";
	}

	public List<String> getIndividualByProperty(String pname) {
		List<String> al = new ArrayList<String>();
		
		for ( OWLConcept it : objectProperties){
			if (it.getName().equals(pname)){
				al.add(it.getValue());
			}
		}
		return al;
		
	}

	public String getIndividualName() {
		return individualName;
	}

	public void setIndividualName(String individualName) {
		this.individualName = individualName;
	}

}
