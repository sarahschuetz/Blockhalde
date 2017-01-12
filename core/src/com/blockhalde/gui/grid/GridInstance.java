package com.blockhalde.gui.grid;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.blockhalde.gui.Item;

public class GridInstance {
	
	public Color color = new Color(1.0f, 1.0f, 1.0f, 0.5f);
	public Vector2 position = new Vector2(0f, 0f);
	public int gapSize = 2;
	public ShapeType shapeType = ShapeType.Filled;
	public boolean selected = false;
	public Item item;
	
	public GridInstance(Item item, Color color, Vector2 position, int gapSize, ShapeType shapeType, boolean selected) {
		this.item = item;
		this.color = color;
		this.position = position;
		this.gapSize = gapSize;
		this.shapeType = shapeType;
		this.selected = selected;
	}

}
