package com.vaadin.tutorial.addressbook.backend;

import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * A simple DTO for the address book example.
 *
 * Serializable and cloneable Java Object that are typically persisted
 * in the database and can also be easily converted to different formats like JSON.
 */
// Backend DTO class. This is just a typical Java backend implementation
// class and nothing Vaadin specific.
public class Organisationseinheit implements Serializable, Cloneable {

    private Long id;

    private String Name = "";
    private String Klasse = "";
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    @Override
    public Organisationseinheit clone() throws CloneNotSupportedException {
        try {
            return (Organisationseinheit) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getKlasse() {
		return Klasse;
	}

	public void setKlasse(String klasse) {
		Klasse = klasse;
	}

	@Override
	public String toString() {
		return "Organisationseinheit [id=" + id + ", Name=" + Name
				+ ", Klasse=" + Klasse + "]";
	}


}
