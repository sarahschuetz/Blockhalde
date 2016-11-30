package com.blockhalde.gui;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class BottomGrid extends Grid {
	
	public static BottomGrid instance;
	
	protected HashMap<String, Texture> itemsForBottomGrid;
		
	private int selectedItem = 0;
	private int selectedItemRow = 0;
	private int selectedItemColumn = 0;
	
	private final int BORDER_SIZE = 3;
	private final int BOTTOM_GRID_OFFSET = 0;
	private final static int MENU_ROWS = 1;
	
	GlyphLayout layout;
	BitmapFont font;
	
	private Texture on;
	private Texture off;
	
	public static BottomGrid getInstance () {
	    if (BottomGrid.instance == null) {
	    	BottomGrid.instance = new BottomGrid ();
	    }
	    return BottomGrid.instance;
	  }
	
	private BottomGrid() {
		
		super (10,MENU_ROWS);
		
		stickToSide(Side.BOTTOM, Side.CENTERX).addCoordinates(BORDER_SIZE, BORDER_SIZE);
		setVisible(false);
				
		font = new BitmapFont();
        font.setColor(Color.WHITE);
        layout = new GlyphLayout();
        
        setHelperMaterials();
        
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		processHelperInput();
		
		layout.setText(font, items[selectedItemColumn][selectedItemRow]);
		font.draw(batch, items[selectedItemColumn][selectedItemRow], width/2 - layout.width/2, offsetY + gridSize * rows + layout.height + BORDER_SIZE + gapSize + BOTTOM_GRID_OFFSET);

		batch.end();
		
		drawItemName(batch);
		
		batch.begin();
		
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
					batch.draw(off, offsetX + gridSize * i , offsetY + BOTTOM_GRID_OFFSET + gridSize * (rows - j - 1), gridSize, gridSize);
				
					if (itemsForBottomGrid.containsKey((items[i][j]))) {
						batch.draw(itemsForBottomGrid.get(items[i][j]), offsetX + gridSize * i + 13, offsetY + BOTTOM_GRID_OFFSET + gridSize * (rows - j - 1) + 13, gridSize - 26, gridSize - 26);
					}
				}
			}
		}
		
		batch.draw(on, gridSize * selectedItemColumn + offsetX - BORDER_SIZE, gridSize * (rows - selectedItemRow - 1) + offsetY - BORDER_SIZE + BOTTOM_GRID_OFFSET, gridSize + BORDER_SIZE * 2, gridSize + BORDER_SIZE * 2);
		
		if (itemsForBottomGrid.containsKey((items[selectedItemColumn][selectedItemRow]))) {
			batch.draw(itemsForBottomGrid.get(items[selectedItemColumn][selectedItemRow]), offsetX + gridSize * selectedItemColumn + 8, offsetY + BOTTOM_GRID_OFFSET + gridSize * (rows - selectedItemRow - 1) + 8, gridSize - 16, gridSize - 16);
		}
	}
	
	private void drawItemName(Batch batch) {
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		
		shapeRenderer.rect(offsetX - BORDER_SIZE, offsetY - BORDER_SIZE + BOTTOM_GRID_OFFSET, gridSize * columns + BORDER_SIZE * 2, BORDER_SIZE);
		shapeRenderer.rect(offsetX - BORDER_SIZE, offsetY + gridSize * rows + BOTTOM_GRID_OFFSET, gridSize * columns + BORDER_SIZE * 2, BORDER_SIZE);
		shapeRenderer.rect(offsetX - BORDER_SIZE, offsetY - BORDER_SIZE + BOTTOM_GRID_OFFSET, BORDER_SIZE, gridSize * rows + BORDER_SIZE * 2);
		shapeRenderer.rect(offsetX + gridSize * columns, offsetY - BORDER_SIZE + BOTTOM_GRID_OFFSET, BORDER_SIZE, gridSize * rows + BORDER_SIZE * 2);

		shapeRenderer.end();
	}
	
	private void setHelperMaterials() {
		
		on = new Texture(Gdx.files.internal("textures/gui_textures/on.png"));
        off = new Texture(Gdx.files.internal("textures/gui_textures/off.png"));
		
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
	
	private void processHelperInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
			increaseSelectedItem();
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			decreaseSelectedItem();
		}
	}

}
