package com.vaadin.tutorial.addressbook;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Tree;

public class MyTree extends Tree{
	private Tree tree;

	public MyTree(){
		tree = new Tree();
		
		this.setSelectable(true);
        this.setMultiSelect(false);
        this.setNullSelectionAllowed(false);
        this.addItemClickListener(new ItemClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void itemClick(ItemClickEvent event) {
                System.out.print(event.getItemId().toString());
            }
        });
        
	}
	
	public void addElements(String parent, List<String> elements){
		for(int i=0; i<elements.size(); i++){
			String temp = elements.get(i);
			if (parent==""){
				this.addItem(temp);
			}else{
				//if (!this.containsId(parent)){
					this.addItem(parent);
				//}
				
				//if (!this.containsId(temp)){
	    			this.addItem(temp);
	    			this.setParent(temp, parent);
				//}
			}
		}
    }
}
