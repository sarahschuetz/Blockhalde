package com.blockhalde.gui;

import java.awt.Event;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.terrain.block.BlockType;

public class InventoryManager {
	
	ItemListener lst;
	
	private List<Item> items;
	private List<Item> itemtypes;
	
	private FileHandle inventory;
	
	private static InventoryManager theInstance = null;
	
	private InventoryManager(){
		this.items = new ArrayList<Item>();
		this.inventory =  Gdx.files.internal("json/inventory.json");
		this.itemtypes = ItemtypeSerializer.loadJsonItemsTypes();
	}
	
	public static InventoryManager getInstance(){
		 if(theInstance == null) {
	         theInstance = new InventoryManager();
	      }
	      return theInstance;
	}
	
	public void addItem(BlockType block){
		Item item = null;
		
		for (Item itemtype : this.itemtypes) {
			if (itemtype.id == block.getBlockId()) {
				item = itemtype;
			}
		}
		
		System.out.println(block.getBlockId());
		
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
	public void consumeItem(int id){
		Item consumed = this.items.get(id);
		consumed.setStackSize(consumed.getStackSize()-1);
		
		if(consumed.getStackSize() <= 0){
			this.items.remove(consumed);
		}
	}
	
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
	
	public void setListener (ItemListener lst) {
        this.lst = lst;
    }

}
