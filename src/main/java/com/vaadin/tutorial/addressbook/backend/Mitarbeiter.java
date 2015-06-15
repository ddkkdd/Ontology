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
public class Mitarbeiter implements Serializable, Cloneable {

    private Long id;

    private String Name = "";
    private String Beschreibung= "";
    private String Gehalt = "";
    private int Erfahrungsjahre = 0;
    private Date Geburtsdatum;
    private String Eintrittsdatum;

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    @Override
    public Mitarbeiter clone() throws CloneNotSupportedException {
        try {
            return (Mitarbeiter) BeanUtils.cloneBean(this);
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

	public String getGehalt() {
		return Gehalt;
	}

	public void setGehalt(String gehalt) {
		Gehalt = gehalt;
	}

	public int getErfahrungsjahre() {
		return Erfahrungsjahre;
	}

	public void setErfahrungsjahre(int erfahrungsjahre) {
		Erfahrungsjahre = erfahrungsjahre;
	}

	public Date getGeburtsdatum() {
		return Geburtsdatum;
	}

	public void setGeburtsdatum(Date geburtsdatum) {
		Geburtsdatum = geburtsdatum;
	}

	@Override
	public String toString() {
		return "Mitarbeiter [id=" + id + ", Name=" + Name + ", Gehalt="
				+ Gehalt + ", Erfahrungsjahre=" + Erfahrungsjahre
				+ ", Geburtsdatum=" + Geburtsdatum + "]";
	}

	public String getEintrittsdatum() {
		return Eintrittsdatum;
	}

	public void setEintrittsdatum(String eintrittsdatum) {
		Eintrittsdatum = eintrittsdatum;
	}

	public String getBeschreibung() {
		return Beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		Beschreibung = beschreibung;
	}

}
