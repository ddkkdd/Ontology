package ontology;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import model.Abteilung;

public class FirmaServiceImpl implements FirmaService{

	private static OWLReasoner reasoner;
	private static FirmaServiceImpl instance;
	private OWLOntology ontology;
	private OWLDataFactory dataFactory;
	private PrefixManager prefixManager;
	private OWLOntologyManager manager;
	private IRI ontoIri; 
	
	@Override
	public List<String> getAllEmployees() throws Exception
	{
		List<String> mitarbeiterList = new LinkedList<String>();
		//throw new Exception("Fehler getAllAbteilungsMitarbeiter!");
		
		if (reasoner != null) {
			for (OWLClass clazz : ontology.getClassesInSignature()) {
				if (clazz.getIRI().getFragment().equals("Mitarbeiter")) {
					NodeSet<OWLClass> mitarbeiter = reasoner
							.getSubClasses(clazz, true);
					for (Node<OWLClass> node : mitarbeiter) {
						printInstancesOfCls(reasoner, node.getRepresentativeElement());
						
						Set<OWLIndividual> indiv = node.getRepresentativeElement().getIndividuals(
										ontology);
						
						for (OWLIndividual owlNamedIndividual : indiv) {
							System.out.println("\t"
									+ owlNamedIndividual.asOWLNamedIndividual()
											.getIRI().getFragment());
							
							mitarbeiterList.add(owlNamedIndividual
									.asOWLNamedIndividual().getIRI()
									.getFragment());
						}
					}
				}
			}
		} else
			throw new Exception(
					"No reasoner started! Please call start reasoner first!");
				
		return mitarbeiterList;
	}
	
	public List<String> getAllIndividuals() throws Exception{
		List<String> vorgesetzter = new LinkedList<String>();
		
		for (OWLIndividual ind : ontology.getIndividualsInSignature()){
			vorgesetzter.add(ind.asOWLNamedIndividual().getIRI().getFragment());
		}
		
		return vorgesetzter;
	}
	
	
	public static void printInstancesOfCls(OWLReasoner reasoner, OWLClass cls) {
		Iterator<Node<OWLNamedIndividual>> iIt = reasoner.getInstances(cls,
				false).iterator();
		System.out.print("-- Instances of Class " + cls.getIRI().getFragment()
				+ ": ");
		while (iIt.hasNext()) {
			OWLNamedIndividual indiv = iIt.next().getRepresentativeElement();
			System.out.print(indiv.getIRI().getFragment());
			if (iIt.hasNext()) {
				System.out.print(", ");
			}
		}
		System.out.println();
	}
	
	public static List<String> getInstancesStringsOfCls(OWLClass cls) {
		List<String> instances = new LinkedList<String>();
		NodeSet<OWLNamedIndividual> instancesOfClass = reasoner.getInstances(
				cls, false);
		for (Node<OWLNamedIndividual> indis : instancesOfClass) {
			OWLNamedIndividual indiv = indis.getRepresentativeElement();
			instances.add(indiv.getIRI().getFragment());
		}
		return instances;
	}

	public static List<OWLNamedIndividual> getInstancesOfCls(OWLClass cls) {
		List<OWLNamedIndividual> instances = new LinkedList<OWLNamedIndividual>();
		Iterator<Node<OWLNamedIndividual>> iIt = reasoner.getInstances(cls,
				false).iterator();
		while (iIt.hasNext()) {
			OWLNamedIndividual indiv = iIt.next().getRepresentativeElement();
			instances.add(indiv);
		}
		return instances;
	}

	public static FirmaServiceImpl getInstance() {
		if (instance == null) {
			instance = new FirmaServiceImpl();
			instance.startReasoner();
		}
		return instance;
	}
	
	@Override
	public void startReasoner() {
		ontoIri = IRI.create("http://www.firma.com/ontologies/Mini2_OWL.owl");
		manager = OWLManager.createOWLOntologyManager();
		dataFactory = OWLManager.getOWLDataFactory();
		prefixManager = new DefaultPrefixManager(
				"http://www.firma.com/ontologies/Mini2_OWL.owl");

		ontology = null;
		try {
			ontology = manager.loadOntologyFromOntologyDocument(new File(
					"/Users/Daniel/Documents/workspace/Firma/src/ontology/Mini2_OWL.owl"));
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

		// Uebergabe der Ontologie an den HermiT Reasoner
		ReasonerFactory factory = new ReasonerFactory();
		Configuration c = new Configuration();
		reasoner = factory.createReasoner(ontology, c);
		reasoner.isConsistent();
	}
}
