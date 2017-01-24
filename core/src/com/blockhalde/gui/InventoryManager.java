package com.blockhalde.gui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.terrain.block.BlockType;

public class InventoryManager {
	
	ItemListener lst;
	
	// lists for the actual items & all existing items
	private List<Item> items;
	private List<Item> itemtypes;
	
	// file where inventory is saved
	private FileHandle inventory;
	
	// singelton instance
	private static InventoryManager theInstance = null;
	
	private InventoryManager(){
		this.items = new ArrayList<Item>();
		this.inventory =  Gdx.files.internal("json/inventory.json");
		this.itemtypes = ItemtypeSerializer.loadJsonItemsTypes();
	}
	
	// get singelton instance
	public static InventoryManager getInstance(){
		 if(theInstance == null) {
	         theInstance = new InventoryManager();
	      }
	      return theInstance;
	}
	
	// adds item by blocktype to inventory
	public void addItem(BlockType block){
		Item item = null;
		
		for (Item itemtype : this.itemtypes) {
			if (itemtype.id == block.getBlockId()) {
				item = itemtype;
			}
		}
		
		// if the item is null (not found in itemtypes), it must not get saved
		if(item != null){ 
			this.items.add(item);	
		}
		
		if (lst != null) {
			lst.itemAdded(this.items);
		}
		
	}
	

	public Item getItem(int id){
		for (Item item : this.items) {
			if (item.id == id) {
				return item;
			}
		}
		return null;
	}
	
	/* important for gui people, should be called when item is choosen in gui */
	// consumes an item by id
	public void consumeItem(int id){
		Item consumed = this.items.get(id);
		consumed.setStackSize(consumed.getStackSize()-1);
		
		if(consumed.getStackSize() <= 0){
			this.items.remove(consumed);
		}
	}
	
	// consumes an item by blocktype
	public void consumeItem(BlockType item){
		Item consumed = this.items.get(item.getBlockId());
		consumed.setStackSize(consumed.getStackSize()-1);
		
		if(consumed.getStackSize() <= 0){
			this.items.remove(consumed);
		}
	}
	
	public BlockType deleteItem(Item item){
			this.items.remove(item);
			return null;	
	}
	
	public BlockType deleteItem(int id){
		for (Item item : this.items) {
			if (item.id == id) {
				deleteItem(item);
			}
		}
		return null;
	}
	
	// file handling - for loading and saving the inventory
	
	public void saveInventory(){
		ItemtypeSerializer.saveJsonInventoryItems(this.items);
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
	
	// listener for gui inventory 
	public void setListener (ItemListener lst) {
        this.lst = lst;
    }

}
