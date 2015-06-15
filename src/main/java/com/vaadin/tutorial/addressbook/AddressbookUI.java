package com.vaadin.tutorial.addressbook;

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
import com.vaadin.tutorial.addressbook.backend.SemanticService;
import com.vaadin.ui.*;
import com.vaadin.ui.Tree.CollapseEvent;
import com.vaadin.ui.Tree.ExpandEvent;

import javax.servlet.annotation.WebServlet;

/* User Interface written in Java.
 *
 * Define the user interface shown on the Vaadin generated web page by extending the UI class.
 * By default, a new UI instance is automatically created when the page is loaded. To reuse
 * the same instance, add @PreserveOnRefresh.
 */
@Title("Semantic Organization")
@Theme("valo")
public class AddressbookUI extends UI {







	/* Hundreds of widgets.
	 * Vaadin's user interface components are just Java objects that encapsulate
	 * and handle cross-browser support and client-server communication. The
	 * default Vaadin components are in the com.vaadin.ui package and there
	 * are over 500 more in vaadin.com/directory.
     */
    TextField filter = new TextField();
    Grid contactList = new Grid();
    Button newContact = new Button("New contact");

    // ContactForm is an example of a custom component class
    ContactForm contactForm = new ContactForm();
    
    MyTree tree = new MyTree();
    
    // ContactService is a in-memory mock DAO that mimics
    // a real-world datasource. Typically implemented for
    // example as EJB or Spring Data based service.
    //ContactService service = ContactService.createDemoService();
    SemanticService semService = SemanticService.createDemoService();


    /* The "Main method".
     *
     * This is the entry point method executed to initialize and configure
     * the visible user interface. Executed on every browser reload because
     * a new instance is created for each web page loaded.
     */
    @Override
    protected void init(VaadinRequest request) {
        configureComponents();
        buildLayout();
        
       
    }


    private void configureComponents() {
         /* Synchronous event handling.
         *
         * Receive user interaction events on the server-side. This allows you
         * to synchronously handle those events. Vaadin automatically sends
         * only the needed changes to the web page without loading a new page.
         */
    	
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
        
        System.out.println("WEB SERVER TEST 1111");
        for (Individual it : semService.getIndividualByClass("<http://www.semanticweb.org/semanticOrg#Organisationseinheit>")){
			System.out.println(it.toString());
		}
		
        
    }

    /* Robust layouts.
     *
     * Layouts are components that contain other components.
     * HorizontalLayout contains TextField and Button. It is wrapped
     * with a Grid into VerticalLayout for the left side of the screen.
     * Allow user to resize the components with a SplitPanel.
     *
     * In addition to programmatically building layout in Java,
     * you may also choose to setup layout declaratively
     * with Vaadin Designer, CSS and HTML.
     */
    private void buildLayout() {
    	List<String> list = new LinkedList<String>();
        
    	list.add("Amstetten");
    	list.add("Linz");
    	list.add("Wien");
    	
    	tree.addElements("Firma", list);
    	
    	List<String> list2 = new LinkedList<String>();
        
    	list2.add("Metallbau");
    	list2.add("Holzbau");
  
    	tree.addElements("Linz",list2);
    	
    	tree.addElements("Firma2",list);
        tree.addItem("Firma3");	
    	
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

    /* Choose the design patterns you like.
     *
     * It is good practice to have separate data access methods that
     * handle the back-end access and/or the user interface updates.
     * You can further split your code into classes to easier maintenance.
     * With Vaadin you can follow MVC, MVP or any other design pattern
     * you choose.
     */
    void refreshContacts() {
        refreshContacts(filter.getValue());
    }

    private void refreshContacts(String stringFilter) {
        contactList.setContainerDataSource(new BeanItemContainer<>(
                Mitarbeiter.class, semService.findAllMA(stringFilter)));
        contactForm.setVisible(false);
    }

    


    /*  Deployed as a Servlet or Portlet.
     *
     *  You can specify additional servlet parameters like the URI and UI
     *  class name and turn on production mode when you have finished developing the application.
     */
    @WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = AddressbookUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
    
    
   

}
