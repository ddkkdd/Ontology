package com.vaadin.tutorial.addressbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.tutorial.addressbook.backend.Contact;
import com.vaadin.tutorial.addressbook.backend.ContactService;
import com.vaadin.tutorial.addressbook.backend.Individual;
import com.vaadin.tutorial.addressbook.backend.Mitarbeiter;
import com.vaadin.tutorial.addressbook.backend.OWLConcept;
import com.vaadin.tutorial.addressbook.backend.SemanticService;
import com.vaadin.ui.*;
import com.vaadin.ui.Tree.CollapseEvent;
import com.vaadin.ui.Tree.ExpandEvent;

import javax.servlet.annotation.WebServlet;

import org.semanticweb.owlapi.model.OWLIndividual;

/* User Interface written in Java.
 *
 * Define the user interface shown on the Vaadin generated web page by extending the UI class.
 * By default, a new UI instance is automatically created when the page is loaded. To reuse
 * the same instance, add @PreserveOnRefresh.
 */
@Title("Semantic Organization")
@Theme("valo")
public class AddressbookUI extends UI {

    TextField filter = new TextField();
    Grid contactList = new Grid();
    Button newContact = new Button("New contact");

    // ContactForm is an example of a custom component class
    ContactForm contactForm = new ContactForm();
    
    MyTree tree = new MyTree();
    List<String> sparteList = new LinkedList<String>();
    Map<String, String> bereichList = new HashMap<String, String>();
    
    
    // ContactService is a in-memory mock DAO that mimics
    // a real-world datasource. Typically implemented for
    // example as EJB or Spring Data based service.
    //ContactService service = ContactService.createDemoService();
    SemanticService semService = SemanticService.createDemoService();

    @Override
    protected void init(VaadinRequest request) {
        configureComponents();
        buildLayout();
        
       
    }


    private void configureComponents() {
        newContact.addClickListener(e -> contactForm.edit(new Mitarbeiter()));

        filter.setInputPrompt("Filter contacts...");
        filter.addTextChangeListener(e -> refreshContacts(e.getText()));

        contactList.setContainerDataSource(new BeanItemContainer<>(Mitarbeiter.class));
//        contactList.setColumnOrder("firstName", "lastName", "email");
//        contactList.removeColumn("id");
//        contactList.removeColumn("birthDate");
//        contactList.removeColumn("phone");
        contactList.setSelectionMode(Grid.SelectionMode.SINGLE);
        contactList.addSelectionListener(e
                -> contactForm.edit((Mitarbeiter) contactList.getSelectedRow()));
        refreshContacts();
        
        for (Individual it : semService.getIndividualByClass("<http://www.semanticweb.org/semanticOrg#Sparte>")){
			String[] temp = it.getIndividualName().split("#");
        	sparteList.add((String) temp[1].subSequence(0, temp[1].length()-1));
		}
		
       	
        for (Individual it : semService.getIndividualByClass("<http://www.semanticweb.org/semanticOrg#Bereich>")){
			//System.out.println(it.getIndividualName());
        	for (OWLConcept concept: it.getObjectProperties()){
						
				bereichList.put(it.getIndividualName(), concept.getValue());
				
				System.out.println(it.getIndividualName()+"\n");
				System.out.println(concept.getValue()+"\n");
			}
		}
    }

    private void buildLayout() {
    	List<String> list = new LinkedList<String>();
        
    	tree.addElements("", sparteList);
    	
        //expand Tree
        for (Object itemId: tree.getItemIds())
            tree.expandItem(itemId);
    	        
        HorizontalLayout actions = new HorizontalLayout(filter, newContact);
        actions.setWidth("100%");
        filter.setWidth("100%");
        actions.setExpandRatio(filter, 1);
        
        HorizontalLayout secondRow = new HorizontalLayout(tree, contactList, contactForm);
        
        VerticalLayout vert = new VerticalLayout(actions, secondRow);
        
        HorizontalLayout hor = new HorizontalLayout(vert);
       
        setContent(hor);
    }

    void refreshContacts() {
        refreshContacts(filter.getValue());
    }

    private void refreshContacts(String stringFilter) {
        contactList.setContainerDataSource(new BeanItemContainer<>(
                Mitarbeiter.class, semService.findAllMA(stringFilter)));
        contactForm.setVisible(false);
    }

    @WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = AddressbookUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
