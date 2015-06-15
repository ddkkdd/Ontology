package model;

import java.util.List;

public class Abteilung {
	private String name;
	private List<String> mitarbeiter;
	private String leiter;
	
	
	
	public Abteilung(String name, List<String> mitarbeiter, String leiter) {
		super();
		this.name = name;
		this.mitarbeiter = mitarbeiter;
		this.leiter = leiter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getMitarbeiter() {
		return mitarbeiter;
	}
	public void setMitarbeiter(List<String> mitarbeiter) {
		this.mitarbeiter = mitarbeiter;
	}
	public String getLeiter() {
		return leiter;
	}
	public void setLeiter(String leiter) {
		this.leiter = leiter;
	}
}
