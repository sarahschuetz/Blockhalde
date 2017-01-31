package com.blockhalde.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Item {
	
	// enum for categories
	public enum Category{
		block,
		ingredient,
		tool,
		armor
	}
	
	// properties for loading and saving item
	public int id;
	public String name = "";
	public String category = "";
	public String hasWear = "";
	public String image = "";
	public int maxStackSize = 0;
	public int size = 1;
	
	// properties that get changed
	private Category cat = null;
	private int currentStackSize = 0;
	private int inventoryPos;
	
	// constructor must be without parameters for json loading
	public Item(){
	}

	// turn category from json into enum value
	public Category getCat() {
		return Category.valueOf(Category.class, category);
	}
	
	public void setStackSize(int stackSize) {
		this.currentStackSize = stackSize;
	}
	
	public int getStackSize() {
		return currentStackSize;
	}

	public int getInventoryPos() {
		return inventoryPos;
	}


	public void setInventoryPos(int inventoryPos) {
		this.inventoryPos = inventoryPos;
	}

	// load the texture 
	public Texture getImage() {
		return new Texture(Gdx.files.internal("textures/" + image));
	}

	@Override
	public String toString() {
		return "Item [name=" + name + ", category=" + cat + ", stackSize=" + currentStackSize
				+ ", maxStackSize=" + maxStackSize + "]";
	}
}
