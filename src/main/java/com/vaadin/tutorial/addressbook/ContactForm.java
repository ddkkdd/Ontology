package com.vaadin.tutorial.addressbook;

import java.util.LinkedList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.tutorial.addressbook.backend.Contact;
import com.vaadin.tutorial.addressbook.backend.Mitarbeiter;
import com.vaadin.tutorial.addressbook.backend.SemanticService;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Tree;


/* Create custom UI Components.
 *
 * Create your own Vaadin components by inheritance and composition.
 * This is a form component inherited from VerticalLayout. Use
 * Use BeanFieldGroup to bind data fields from DTO to UI fields.
 * Similarly named field by naming convention or customized
 * with @PropertyId annotation.
 */
public class ContactForm extends FormLayout {

    Button save = new Button("Save", this::save);
    Button cancel = new Button("Cancel", this::cancel);
    Button addRow = new Button("Zeile hinzufügen",this::addRow);
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField phone = new TextField("Phone");
    TextField email = new TextField("Email");
    DateField birthDate = new DateField("Birth date");
    ComboBox box = new ComboBox("Wohnort");
    Tree tree = new Tree();

    SemanticService semService = SemanticService.createDemoService();
    
    Mitarbeiter mitarbeiter;

    // Easily bind forms to beans and manage validation and buffering
    BeanFieldGroup<Mitarbeiter> formFieldBindings;

    
    class EmplyoeeRow extends CustomComponent {
        public EmplyoeeRow(String property) {
            // A layout structure used for composition
            Panel panel = new Panel("neues Property");
            HorizontalLayout panelContent = new HorizontalLayout(); 
            
            panel.setContent(new VerticalLayout());
            
            
            // Compose from multiple components
            ComboBox select = new ComboBox("Beziehung");
            try {
				select.addItems(SemanticService.getObjectProperties());
			} catch (UnsupportedOperationException e) {
				e.printStackTrace();
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			}
            addComponent(select);
            Label label = new Label(property);
            label.setSizeUndefined(); // Shrink
            

            // Set the size as undefined at all levels
            panel.getContent().setSizeUndefined();
            panel.setSizeUndefined();
            setSizeUndefined();

            // The composition root MUST be set
            setCompositionRoot(panel);
        }
    }

    public ContactForm() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        /* Highlight primary actions.
         *
         * With Vaadin built-in styles you can highlight the primary save button
         * and give it a keyboard shortcut for a better UX.
         */
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);
        
        List<String> list = new LinkedList<String>();
        
    	list.add("Amstetten");
    	list.add("Linz");
    	list.add("Wien");
    	
    	for (int i=0; i<list.size();i++){
    		box.addItem(list.get(i));
    	}
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(save, cancel, addRow);
        actions.setSpacing(true);
        
		addComponents(tree, actions, firstName, lastName, phone, email, birthDate, box);
		addComponent(new EmplyoeeRow("nachricht"));
    }

    /* Use any JVM language.
     *
     * Vaadin supports all languages supported by Java Virtual Machine 1.6+.
     * This allows you to program user interface in Java 8, Scala, Groovy or any other
     * language you choose.
     * The new languages give you very powerful tools for organizing your code
     * as you choose. For example, you can implement the listener methods in your
     * compositions or in separate controller classes and receive
     * to various Vaadin component events, like button clicks. Or keep it simple
     * and compact with Lambda expressions.
     */
    public void save(Button.ClickEvent event) {
        try {
            // Commit the fields from UI to DAO
            formFieldBindings.commit();

            // Save DAO to backend with direct synchronous service API
            getUI().semService.saveMA(mitarbeiter);

            String msg = String.format("Saved '%s %s'.",
                    mitarbeiter.getName(),
                    mitarbeiter.getBeschreibung());
            Notification.show(msg,Type.TRAY_NOTIFICATION);
            getUI().refreshContacts();
        } catch (FieldGroup.CommitException e) {
            // Validation exceptions could be shown here
        }
    }

    public void cancel(Button.ClickEvent event) {
        // Place to call business logic.
        Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
        getUI().contactList.select(null);
        getUI().contactForm.setVisible(false);
    }

    void edit(Mitarbeiter mitarbeiter) {
        this.mitarbeiter = mitarbeiter;
        if(mitarbeiter != null) {
            // Bind the properties of the contact POJO to fiels in this form
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(mitarbeiter, this);
            firstName.focus();
        }
        setVisible(mitarbeiter != null);
    }
    
    public void addRow(Button.ClickEvent event) {
		
    }
    

    @Override
    public AddressbookUI getUI() {
        return (AddressbookUI) super.getUI();
    }


}
