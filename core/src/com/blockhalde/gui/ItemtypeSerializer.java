package com.blockhalde.gui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

public class ItemtypeSerializer {

	public static List<Item> loadJsonAndItems(){   
		 
	        FileHandle handle = Gdx.files.internal("json/itemtypes.json");
	        Json json = new Json();
	        ArrayList<Item> itemData = new ArrayList<>();
	     
	        
	    /*    for (JsonValue v : list) {
	        	itemData.add(json.readValue(Item.class, v));
	        	System.out.println(v.toString());
	        }
 */
			return itemData; 
	   }   
	
}

