package com.blockhalde.gui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.Map;

public class InventoryManager {
	
	private List<Item> items;
	
	private FileHandle inventory;
	
	private static InventoryManager theInstance = null;
	
	private InventoryManager(){
		this.items = new ArrayList<Item>();
		this.inventory =  Gdx.files.internal("json/inventory.json");
	}
	
	public static InventoryManager getInstance(){
		 if(theInstance == null) {
	         theInstance = new InventoryManager();
	      }
	      return theInstance;
	}
	
	public void addItem(Item item){
		this.items.add(item);		
	}
	
	public Item getItem(int id){
		return this.items.get(id);
	}
	
	public Item getItem(String id){
		for (Item item : this.items) {
			if (item.id == id) {
				return item;
			}
		}
		return null;
	}
	
	public void updateItem(Item item){
		
	}
	
	public void deleteItem(Item item){
			this.items.remove(item);	
	}
	
	public void deleteItem(String id){
		for (Item item : this.items) {
			if (item.id == id) {
				deleteItem(item);
			}
		}
	}
	
	// file handling
	public void saveInventory(){
		ItemtypeSerializer.saveJsonItems(this.items);
	}
	
	public void loadInventory(){
		this.items = ItemtypeSerializer.loadInventoryJsonItems(this.inventory);
	}
	
	public List<Item> getInventory(){
		if(this.items.isEmpty()){
			loadInventory();
		}
		return this.items;
	}
}
