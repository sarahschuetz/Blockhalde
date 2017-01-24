package com.blockhalde.gui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class ItemtypeSerializer {
	
	// method for saving the inventory 
	public static void saveJsonInventoryItems(List<Item> inventory){
			Json json = new Json();
			FileHandle file = Gdx.files.local("json/inventory.json");
			
			file.writeString("{ \n", false);
			file.writeString("inventory: [ \n", true);
			
			for (Item item : inventory) {
				String it = json.prettyPrint(item);			
				file.writeString(it +",", true);
			}
			
			file.writeString("\n]\n", true);
			file.writeString("}", true);
	}

	// method for loading all itemtypes -> all existing items in the game
	public static List<Item> loadJsonItemsTypes(){   		 
	        FileHandle handle = Gdx.files.internal("json/itemtypes.json");
	        Json json = new Json();	        
	        ItemWrapper dataTest = json.fromJson(ItemWrapper.class, handle);
	        
	        ArrayList<Item> itemData = new ArrayList<>();
	        
	        for (Item it : dataTest.itemtypes) {
	        	itemData.add(it);
			}
 
			return itemData; 
	} 
	
	// method for loading the inventory from the inventory json file
	public static List<Item> loadInventoryJsonItems(FileHandle inventory){
		Json json = new Json();
		return json.fromJson(ItemWrapper.class, inventory).inventory;
	}
	
	
}

