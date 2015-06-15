package test;

import model.Abteilung;
import ontology.FirmaServiceImpl;

public class testOntology {

	public static void main(String[] args) throws Exception{
		FirmaServiceImpl service = null;
		
		try {
			service = FirmaServiceImpl.getInstance();
		} catch (Exception e) {
			
		}
		
		for (String str : service.getAllEmployees()){
			System.out.print(str+"\n");
		}
		
		System.out.println("\n########################\n");
		
		for (String str : service.getAllIndividuals()){
			System.out.print(str+"\n");
		}
		
	}
}
