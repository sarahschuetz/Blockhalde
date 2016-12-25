package com.blockhalde.gui.grid;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.blockhalde.gui.RendererGUI;

public class InventoryGrid extends Grid {
	
	boolean menu = false;
	
	public static InventoryGrid instance;
	
	protected HashMap<String, Texture> itemsForBottomGrid;
		
	private int selectedItem = 0;
	private int selectedItemRow = 0;
	private int selectedItemColumn = 0;
	
	private final int BORDER_SIZE = 3;
	private final int BOTTOM_GRID_OFFSET = 0;
	
	GlyphLayout layout;
	BitmapFont font;
	
	Texture blurredBackground;
	
	public static InventoryGrid getInstance () {
	    if (InventoryGrid.instance == null) {
	    	InventoryGrid.instance = new InventoryGrid ();
	    }
	    return InventoryGrid.instance;
	  }
	
	private InventoryGrid() {
		
		super (7);
		
		stickToSide(Side.CENTERY, Side.CENTERX).addCoordinates(BORDER_SIZE, BORDER_SIZE);
				
		font = new BitmapFont();
        font.setColor(Color.WHITE);
        layout = new GlyphLayout();
        
        setHelperMaterials();
        setVisible(false);
        
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		drawGrid(batch);
	}
	
	public void setSelectedItem (int number) {
		selectedItem = number;
	}
	
	public void increaseSelectedItem() {
		if (selectedItem == rows * columns - 1) {
			selectedItem = 0;
		} else {
			selectedItem += 1;
		}
		
		selectedItemRow = (int) selectedItem / 10;
		selectedItemColumn = selectedItem - 10 * selectedItemRow;
		
	}
	
	public void decreaseSelectedItem() {
		if (selectedItem == 0) {
			selectedItem = rows * columns - 1;
		} else {
			selectedItem -= 1;
		}
				
		selectedItemRow = (int) selectedItem / 10;
		selectedItemColumn = selectedItem - 10 * selectedItemRow;
	}
	
	private void drawGrid(Batch batch) {
						
		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				if (i != selectedItemColumn || j != selectedItemRow) {
					//batch.draw(off, offsetX + gridSize * i , offsetY + BOTTOM_GRID_OFFSET + gridSize * (rows - j - 1), gridSize, gridSize);
				
					if (itemsForBottomGrid.containsKey((items[i][j]))) {
						batch.draw(itemsForBottomGrid.get(items[i][j]), offsetX + gridSize * i + 13, offsetY + BOTTOM_GRID_OFFSET + gridSize * (rows - j - 1) + 13, gridSize - 26, gridSize - 26);
					}
				}
			}
		}
		
		//batch.draw(on, gridSize * selectedItemColumn + offsetX - BORDER_SIZE, gridSize * (rows - selectedItemRow - 1) + offsetY - BORDER_SIZE + BOTTOM_GRID_OFFSET, gridSize + BORDER_SIZE * 2, gridSize + BORDER_SIZE * 2);
		
		if (itemsForBottomGrid.containsKey((items[selectedItemColumn][selectedItemRow]))) {
			batch.draw(itemsForBottomGrid.get(items[selectedItemColumn][selectedItemRow]), offsetX + gridSize * selectedItemColumn + 8, offsetY + BOTTOM_GRID_OFFSET + gridSize * (rows - selectedItemRow - 1) + 8, gridSize - 16, gridSize - 16);
		}
	}
	
	private void setHelperMaterials() {

		itemsForBottomGrid = new HashMap<String, Texture>();
		itemsForBottomGrid.put("Imaginary Stone", new Texture(Gdx.files.internal("textures/gui_textures/stone.png")));
		itemsForBottomGrid.put("Imaginary Wood", new Texture(Gdx.files.internal("textures/gui_textures/wood.png")));
		itemsForBottomGrid.put("Imaginary Cobblestone", new Texture(Gdx.files.internal("textures/gui_textures/cobblestone.png")));
		itemsForBottomGrid.put("Imaginary Glass", new Texture(Gdx.files.internal("textures/gui_textures/glass.png")));
		itemsForBottomGrid.put("Imaginary Diamond", new Texture(Gdx.files.internal("textures/gui_textures/diamond.png")));
		itemsForBottomGrid.put("Imaginary Coal", new Texture(Gdx.files.internal("textures/gui_textures/coal.png")));
		itemsForBottomGrid.put("Imaginary Bricks", new Texture(Gdx.files.internal("textures/gui_textures/bricks.png")));
		
		items[0][0] = "Imaginary Stone";
		items[1][0] = "Imaginary Wood";
		items[2][0] = "Imaginary Cobblestone";
		items[3][0] = "Imaginary Glass";
		items[4][0] = "Imaginary Diamond";
		items[5][0] = "Imaginary Coal";
		items[6][0] = "Imaginary Bricks";	
					
	}
	
	public int getTotalHeight() {
		return (int) (offsetY + gridSize * rows + layout.height + BORDER_SIZE + gapSize + BOTTOM_GRID_OFFSET);
	}
	
	public int getTotalWidth() {
		return (int) gridSize * columns;
	}
	
	@Override
	public void setVisible(boolean visible) {
		
		/*if (visible) {
			RendererGUI.instance().menusActive += 1;
		} else {
			RendererGUI.instance().menusActive -= 1;
		}*/
		
		super.setVisible(visible);
	}
	
}
