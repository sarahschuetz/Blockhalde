package com.blockhalde.gui;

import com.badlogic.gdx.graphics.Texture;

public class Item implements UIItem {
	
	public enum Category{
		block,
		ingredient,
		tool,
		armor
	}
	
	private String name = "";
	private Texture img = null;
	private Category category = null;
	private int stackSize = 0;
	private int maxStackSize = 0;
	
	public Item(){
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setImg(Texture img) {
		this.img = img;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setStackSize(int stackSize) {
		this.stackSize = stackSize;
	}

	public void setMaxStackSize(int maxStackSize) {
		this.maxStackSize = maxStackSize;
	}

	public String getName() {
		return name;
	}

	public Texture getImg() {
		return img;
	}

	public int getStackSize() {
		return stackSize;
	}

	public int getMaxStackSize() {
		return maxStackSize;
	}

	@Override
	public Category getCategory() {
		return category;
	}

	@Override
	public int[] getInventoryPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "Item [name=" + name + ", img=" + img + ", category=" + category + ", stackSize=" + stackSize
				+ ", maxStackSize=" + maxStackSize + "]";
	}
}
