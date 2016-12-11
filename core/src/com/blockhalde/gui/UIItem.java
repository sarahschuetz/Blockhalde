package com.blockhalde.gui;

import com.blockhalde.gui.Item.Category;

public interface UIItem {
	
	public String getName();
	
	public Category getCategory();
	
	public int[] getInventoryPosition();
	
	public int getStackSize();
		
}
