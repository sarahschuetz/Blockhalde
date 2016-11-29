package com.blockhalde.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class BottomGrid extends Grid {
	
	private ShapeRenderer shapeRenderer;
	private int selectedItem = 0;
	
	GlyphLayout layout;
	BitmapFont font;
	
	public static BottomGrid instance;
	
	public static BottomGrid getInstance () {
	    if (BottomGrid.instance == null) {
	    	BottomGrid.instance = new BottomGrid ();
	    }
	    return BottomGrid.instance;
	  }
	
	private BottomGrid() {
		
		super (10,1);
		this.stickToSide(Side.BOTTOM, Side.CENTERX);
		this.setVisible(false);
		shapeRenderer = new ShapeRenderer();
		gapSize = 10;
		
		font = new BitmapFont();
        font.setColor(Color.WHITE);
        layout = new GlyphLayout();
        
        setHelperMaterials();
        
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		processHelperInput();
		
		layout.setText(font, items[selectedItem][0]);
		font.draw(batch, items[selectedItem][0], width/2 - layout.width/2, offsetY + gridSize + layout.height + gapSize);
		
		batch.end();
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(gridSize * selectedItem + offsetX, offsetY, gridSize, gridSize);
		shapeRenderer.end();
		batch.begin();
		
		super.draw(batch, parentAlpha);
		
		
	}
	
	public void setSelectedItem (int number) {
		selectedItem = number;
	}
	
	public void increaseSelectedItem() {
		if (selectedItem == 9) {
			selectedItem = 0;
		} else {
			selectedItem += 1;
		}
	}
	
	public void decreaseSelectedItem() {
		if (selectedItem == 0) {
			selectedItem = 9;
		} else {
			selectedItem -= 1;
		}
	}
	
	private void setHelperMaterials() {
		items[0][0] = "Imaginary Stone";
		items[1][0] = "Imaginary Wood";
		items[2][0] = "Imaginary Cobblestone";
		items[3][0] = "Imaginary Water";
		items[4][0] = "Imaginary Lava";
		items[5][0] = "Imaginary Glass";
		items[6][0] = "Imaginary Shovel";
		items[7][0] = "Imaginary Seeds";
	}
	
	private void processHelperInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
			increaseSelectedItem();
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			decreaseSelectedItem();
		}
	}

}
