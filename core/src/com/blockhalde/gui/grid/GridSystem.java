package com.blockhalde.gui.grid;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.blockhalde.gui.Item;
import com.blockhalde.gui.RendererGUI;

public class GridSystem extends EntitySystem {
	
	private final int GRID_COUNT = 12;
	private final int BOTTOM_GRID_HEIGHT = 1;
	private final int FONT_GRID_HEIGHT = 1;
	private final int MENU_TITLE_HEIGHT = 1;
	private final int INVENTORY_GRID_HEIGHT = 9;
	
	private int width;
	private int height;
	
	private int gridSize;
	
	private Stage stage;
	
	GridInstance[][] bottomGrid = new GridInstance [INVENTORY_GRID_HEIGHT][GRID_COUNT];
	GridInstance[][] invengtoryGrid = new GridInstance [BOTTOM_GRID_HEIGHT][GRID_COUNT];
	
	int selectedItem = 0;
	
	Actor bottomMenu;
	public Actor inventoryMenu;
	Actor bottomMenuBackground;
	Actor inventroyMenuBackground;
	
	HeadingTextActor titleActor;
	HeadingTextActor descriptionActor;
	
	public boolean inventoryIsVisible = false;
	
	protected HashMap<String, String> itemsForBottomGrid;
	
	public GridSystem() {
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		if (height < width) {
			gridSize = height/GRID_COUNT;
		} else {
			gridSize = width/GRID_COUNT;
		}
		
		this.stage = RendererGUI.instance().getStage();
		
		setHelperMaterials();
		
		drawItemDescription(1, "Item description");
		drawTitle(11, "INVENTORY");
		drawBottomGridBackground(0);
		drawBottomGrid(0);
		drawInventoryGridBackground(2);
		drawInventoryGrid(2);
		
	}
	
	private void drawBottomGridBackground(int startOffset) {
		int widthBackground = gridSize * GRID_COUNT;
		int heightBackground = gridSize * BOTTOM_GRID_HEIGHT;
		int startX = width/2 - widthBackground/2;
		int startY = startOffset * gridSize;
		bottomMenuBackground = new BackgroundActor(startX, startY, widthBackground, heightBackground, new Color(0.0f, 0.0f, 0.0f, 0.5f));
		stage.addActor(bottomMenuBackground);
	}
	
	private void drawInventoryGridBackground(int startOffset) {
		int widthBackground = gridSize * GRID_COUNT;
		int heightBackground = gridSize * INVENTORY_GRID_HEIGHT;
		int startX = width/2 - widthBackground/2;
		int startY = startOffset * gridSize;
		inventroyMenuBackground = new BackgroundActor(startX, startY, widthBackground, heightBackground, new Color(0.0f, 0.0f, 0.0f, 0.5f));
		inventroyMenuBackground.setVisible(false);
		stage.addActor(inventroyMenuBackground);
	}
	
	private void drawBottomGrid(int startOffset) {
		
		bottomGrid = new GridInstance [BOTTOM_GRID_HEIGHT][GRID_COUNT];
		
		for (int i = 0; i < BOTTOM_GRID_HEIGHT; i++) {
			for (int j = 0; j < GRID_COUNT; j++) {
				int x = width/2 - gridSize * GRID_COUNT/2 + gridSize * j;
				int y = startOffset;	
				bottomGrid[i][j] = new GridInstance(null, new Color(1.0f, 1.0f, 1.0f, 0.5f), new Vector2(x, y), 2, ShapeType.Filled, false);
			}
		}
		
		int i = 0;
		
		for (Map.Entry<String, String> entry : itemsForBottomGrid.entrySet()) {
		    Item item = new Item();
		    item.name = entry.getKey();
		    item.image = entry.getValue();
		    setGridItem(item, i, 0);
		    i++;
		}
		
		setSelectedItemBottomGrid (0);
		
		bottomMenu = new GridActor(gridSize, bottomGrid);
		stage.addActor(bottomMenu);
		
	}
	
	private void drawItemDescription(int startOffset, String text) {
		descriptionActor = new HeadingTextActor(text, gridSize, startOffset);
		stage.addActor(descriptionActor);
	}
	
	private void drawInventoryGrid(int startOffset) {
		
		invengtoryGrid = new GridInstance [INVENTORY_GRID_HEIGHT][GRID_COUNT];
		
		for (int i = 0; i < INVENTORY_GRID_HEIGHT; i++) {
			for (int j = 0; j < GRID_COUNT; j++) {
				int x = width/2 - gridSize * GRID_COUNT/2 + gridSize * j;
				int y = (i + startOffset) * gridSize;	
				
				if (i == 0 && j == 0) {
					invengtoryGrid[i][j] = new GridInstance(null, new Color(1.0f, 1.0f, 1.0f, 0.5f), new Vector2(x, y), 2, ShapeType.Filled, true);
				} else {
					invengtoryGrid[i][j] = new GridInstance(null, new Color(1.0f, 1.0f, 1.0f, 0.5f), new Vector2(x, y), 2, ShapeType.Filled, false);
				}	
			}
		}
		
		inventoryMenu = new GridActor(gridSize, invengtoryGrid);
		inventoryMenu.setVisible(false);
		stage.addActor(inventoryMenu);
		
	}
	
	private void drawTitle(int startOffset, String text) {
		titleActor = new HeadingTextActor(text, gridSize, startOffset);
		titleActor.setVisible(false);
		stage.addActor(titleActor);
	}
	
	private void setHelperMaterials() {
		
		itemsForBottomGrid = new HashMap<String, String>();
		itemsForBottomGrid.put("Imaginary Stone", "textures/gui_textures/stone.png");
		itemsForBottomGrid.put("Imaginary Wood", "textures/gui_textures/wood.png");
		itemsForBottomGrid.put("Imaginary Cobblestone", "textures/gui_textures/cobblestone.png");
		itemsForBottomGrid.put("Imaginary Glass", "textures/gui_textures/glass.png");
		itemsForBottomGrid.put("Imaginary Diamond", "textures/gui_textures/diamond.png");
		itemsForBottomGrid.put("Imaginary Coal", "textures/gui_textures/coal.png");
		itemsForBottomGrid.put("Imaginary Bricks", "textures/gui_textures/bricks.png");
		
	}
	
	public void setGridItem(Item item, int x, int y) {
		if (x < GRID_COUNT && y < INVENTORY_GRID_HEIGHT) {
			bottomGrid[y][x].item = item;
		}
	}
 	
	public void setSelectedItemBottomGrid (int x) {
		bottomGrid[0][selectedItem].selected = false;
		
		selectedItem = x;
		
		bottomGrid[0][x].selected = true;
		
		if (bottomGrid[0][selectedItem].item != null) {
			descriptionActor.setText(bottomGrid[0][selectedItem].item.name);
		} else {
			descriptionActor.setText("Empty");
		}
		
		
	}
	
	private void increaseSelectedItem() {
		bottomGrid[0][selectedItem].selected = false;
		
		selectedItem += 1;
		if (selectedItem >= GRID_COUNT) {
			selectedItem = 0;
		}
		
		if (bottomGrid[0][selectedItem].item != null) {
			descriptionActor.setText(bottomGrid[0][selectedItem].item.name);
		} else {
			descriptionActor.setText("Empty");
		}
		
		bottomGrid[0][selectedItem].selected = true;
		
	}
	
	private void decreaseSelectedItem() {
		bottomGrid[0][selectedItem].selected = false;
		
		selectedItem -= 1;
		if (selectedItem < 0) {
			selectedItem = GRID_COUNT - 1;
		}
		
		if (bottomGrid[0][selectedItem].item != null) {
			descriptionActor.setText(bottomGrid[0][selectedItem].item.name);
		} else {
			descriptionActor.setText("Empty");
		}
		
		bottomGrid[0][selectedItem].selected = true;
	}
	
	public void scrollItems(int value) {
		if (value > 0) {
			for (int i = 0; i < value; i++) {
				increaseSelectedItem();
			}
		} else {
			for (int i = 0; i > value; i--) {
				decreaseSelectedItem();
			}
		}
	}
	
	public void toggleMenu() { 
		inventoryIsVisible = !inventoryMenu.isVisible();
		inventoryMenu.setVisible(!inventoryMenu.isVisible());
		inventroyMenuBackground.setVisible(!inventroyMenuBackground.isVisible());
		descriptionActor.setVisible(!descriptionActor.isVisible());
		titleActor.setVisible(!titleActor.isVisible());
	}
	
	public boolean inventoryIsVisible() {
		return inventoryIsVisible;
	}
	
}
