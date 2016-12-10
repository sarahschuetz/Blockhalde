package com.blockhalde.gui;

import java.util.List;

import com.badlogic.gdx.maps.Map;

public class InventoryManager {
	
	private Map items;
	private List allItems;
	
	private static InventoryManager theInstance = null;
	
	private InventoryManager(){
		
	}
	
	public static InventoryManager getInstance(){
		 if(theInstance == null) {
	         theInstance = new InventoryManager();
	      }
	      return theInstance;
	}

	
	public boolean addItem(UIItem item){
		return false;		
	}
	
	public boolean deleteItem(UIItem item){
		return false;		
	}
	
	// file handling
	public void saveInventory(){
		
	}
	
	public void loadInventory(){
		
	}
}
