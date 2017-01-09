package com.blockhalde.gui;

import com.badlogic.gdx.graphics.Texture;
import com.blockhalde.gui.Item.Category;

// NOT NECESSARY NEEDED??
public interface UIItem {
	
	public Texture getImageTexture();
	
	public Category getCategory();
	
	public int[] getInventoryPosition();
	
	public int getCurrentStackSize();
		
}
