package com.blockhalde.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Item {
	
	public enum Category{
		block,
		ingredient,
		tool,
		armor
	}
	
	public int id;
	public String name = "";
	public String category = "";
	public String hasWear = "";
	public String image = "";
	public int maxStackSize = 0;
	
	private Category cat = null;
	private int currentStackSize = 0;
	private int inventoryPos;
	
	
	public Item(){
	}

	
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


	public Texture getImage() {
		return new Texture(Gdx.files.internal(image));
	}

	@Override
	public String toString() {
		return "Item [name=" + name + ", category=" + cat + ", stackSize=" + currentStackSize
				+ ", maxStackSize=" + maxStackSize + "]";
	}
}
