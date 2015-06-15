package com.vaadin.tutorial.addressbook.backend;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.collections.map.LinkedMap;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.CustomField;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Separate Java service class. Backend implementation for the address book
 * application, with "detached entities" simulating real world DAO. Typically
 * these something that the Java EE or Spring backend services provide.
 */
// Backend service class. This is just a typical Java backend implementation
// class and nothing Vaadin specific.
public class SemanticService {

	public static String DEFAULT_NAMESPACE = "http://www.semanticweb.org/semanticOrg";

	static List<Individual> lnames;

	private static SemanticService instance;

	public static void main(String[] args) throws OWLException, IOException {
		createDemoService();
		
		for (Individual it : instance.getIndividualByClass("<http://www.semanticweb.org/semanticOrg#Organisationseinheit>")){
			System.out.println(it.toString());
		}
		
	}

	public static SemanticService createDemoService() {
		if (instance == null) {
			try {
				instance = new SemanticService();
				loadOntology();
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			}
		}

		return instance;
	}

	private HashMap<Long, Individual> individuals = new HashMap<>();
	private HashMap<Long, Mitarbeiter> mitarbeiter = new HashMap<>();
	private long nextId = 0;

	public synchronized List<Individual> findAll(String stringFilter) {
		System.out.println("PEHE TEST");
		ArrayList<Individual> arrayList = new ArrayList<Individual>();
		for (Individual i : individuals.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter
						.isEmpty())
						|| i.toString().toLowerCase()
								.contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(i.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(SemanticService.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<Individual>() {

			@Override
			public int compare(Individual o1, Individual o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}

	public synchronized long count() {
		return individuals.size();
	}

	public synchronized void delete(Individual value) {
		individuals.remove(value.getId());
	}

	public synchronized void save(Individual entry) {
		if (entry.getId() == null) {
			entry.setId(nextId++);
		}
		try {
			// entry = (Individual) BeanUtils.cloneBean(entry);

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		individuals.put(entry.getId(), entry);
		System.out.println(individuals.size());
	}

	public static void loadOntology() throws OWLOntologyCreationException {
		// System.out.println(VaadinService.getCurrent().getBaseDirectory().toString());

		OWLOntologyManager m = OWLManager.createOWLOntologyManager();

		// String file = "/Mini2_OWL.owl";
		String file = "C:\\Users\\Peter\\Dropbox\\SemTech SS15\\Miniprojekt 2\\Mini2_OWL.owl";
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
		System.out.println("Anzahl: " + individuals.size());
		long j = 0;
		boolean isMA = false;

		for (OWLNamedIndividual ind : individuals) {

			List<OWLConcept> dpm = new LinkedList<OWLConcept>();
			List<OWLConcept> opm = new LinkedList<OWLConcept>();

			j++;
			System.out.println(ind);

			Map<OWLDataPropertyExpression, Set<OWLLiteral>> dataProperties = ind
					.getDataPropertyValues(o);
			for (Entry<OWLDataPropertyExpression, Set<OWLLiteral>> d : dataProperties
					.entrySet()) {
				if (d.getKey() != null) {
					for (OWLLiteral s : d.getValue()) {

						dpm.add(new OWLConcept(d.getKey().toString(), s
								.getLiteral()));
						// System.out.println(d.getKey().toString()+"  ---  "+s.getLiteral());
					}
				}
			}
			Map<OWLObjectPropertyExpression, Set<OWLIndividual>> objectProperties = ind
					.getObjectPropertyValues(o);
			for (Entry<OWLObjectPropertyExpression, Set<OWLIndividual>> d : objectProperties
					.entrySet()) {
				if (d.getKey() != null) {
					for (OWLIndividual s : d.getValue()) {
						opm.add(new OWLConcept(d.getKey().toString(), s
								.toString()));
						// System.out.println(d.getKey().toString()+"  ---  "+s.toString());

					}
				}
			}

			isMA = false;

			List<String> classes = new LinkedList<String>();

			NodeSet<OWLClass> owlclasses = reasoner.getTypes(ind, false);
			for (Node<OWLClass> s : owlclasses) {
				for (OWLClass c : s) {
					classes.add(c.toString());

					System.out.println("NAVAX");
					System.out.println(c.toString());
					System.out
							.println("<http://www.semanticweb.org/semanticOrg#Mitarbeiter>");
					isMA = isMA
							| c.toString()
									.equals("<http://www.semanticweb.org/semanticOrg#Mitarbeiter>");

				}
			}

			Individual i = new Individual(j, ind.toString(), dpm, opm, classes);

			if (isMA) {
				System.out.println(i.createMitarbeiter().toString());
				instance.saveMA(i.createMitarbeiter());
			}

			instance.save(i);

		}

	}

	public synchronized List<Mitarbeiter> findAllMA(String stringFilter) {
		System.out.println("PEHE TEST");
		ArrayList<Mitarbeiter> arrayList = new ArrayList<Mitarbeiter>();
		for (Mitarbeiter i : mitarbeiter.values()) {
			System.out.println(i);
			try {
				boolean passesFilter = (stringFilter == null || stringFilter
						.isEmpty())
						|| i.toString().toLowerCase()
								.contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(i.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(SemanticService.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<Mitarbeiter>() {

			@Override
			public int compare(Mitarbeiter o1, Mitarbeiter o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}

	public synchronized long countMA() {
		return individuals.size();
	}

	public synchronized void deleteMA(Mitarbeiter value) {
		individuals.remove(value.getId());
	}

	public synchronized void saveMA(Mitarbeiter entry) {
		if (entry.getId() == null) {
			entry.setId(nextId++);
		}
		try {
			entry = (Mitarbeiter) BeanUtils.cloneBean(entry);

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		mitarbeiter.put(entry.getId(), entry);
		System.out.println("MITARBEITER hinzugefügt ->" + mitarbeiter.size());
	}

	public List<Individual> getIndividualByClass(String classname) {
		// private HashMap<Long, Individual> individuals = new HashMap<>();
		List<Individual> classIndividuals = new ArrayList<>();
		for (Entry<Long, Individual> s : individuals.entrySet()) {
			if (s.getValue().isClassMember(classname)) {
				classIndividuals.add(s.getValue());
			}

		}

		return classIndividuals;

	}

}
