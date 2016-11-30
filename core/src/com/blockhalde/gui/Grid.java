package com.blockhalde.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Grid extends Actor {

	enum Side {
		TOP, BOTTOM, LEFT, RIGHT, CENTERX, CENTERY
	}

	protected String[][] items;

	protected ShapeRenderer shapeRenderer;

	protected int rows;
	protected int columns;
	protected int gridSize = 4;
	protected int offsetX = 0;
	protected int offsetY = 0;

	protected int gapSize = 6;
	
	private final int MAX_ITEM_COUNT = 10;
	protected int width = 0;

	public Grid(int columns, int rows) {
		
		if (columns > 10 || rows > 10) {
			throw new IllegalArgumentException("Grid cannot exceed 10 rows or columns");
		}

		shapeRenderer = new ShapeRenderer();
		this.items = new String[columns][rows];
		this.rows = rows;
		this.columns = columns;
		
		this.width = Gdx.graphics.getWidth();

		if (Gdx.graphics.getHeight() < Gdx.graphics.getWidth()) {
			this.gridSize = Gdx.graphics.getHeight() / MAX_ITEM_COUNT;
		} else {
			this.gridSize = Gdx.graphics.getWidth() / MAX_ITEM_COUNT;
		}
		
		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				items[i][j] = "Empty";
			}
		}

	}

	public Grid(int itemCount) {
		
		if (itemCount > 100) {
			throw new IllegalArgumentException("Grid cannot exceed 100 items");
		}

		shapeRenderer = new ShapeRenderer();
		this.items = new String[columns][rows];

		if (itemCount <= 10) {
			this.columns = 10;
		} else {
			this.columns = 10;
			this.rows = itemCount / 10;
		}

		if (Gdx.graphics.getHeight() < Gdx.graphics.getWidth()) {
			this.gridSize = Gdx.graphics.getHeight() / MAX_ITEM_COUNT;
		} else {
			this.gridSize = Gdx.graphics.getWidth() / MAX_ITEM_COUNT;
		}
		
		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				items[i][j] = "Empty";
			}
		}

	}

	public Grid setCoordinates(int x, int y) {
		offsetX = x;
		offsetY = y;
		return this;
	}
	
	public Grid addCoordinates(int x, int y) {
		offsetX += x;
		offsetY += y;
		return this;
	}

	public Grid stickToSide(Side... side) {

		for (Side s : side) {
			if (s == Side.TOP) {
				offsetY = Gdx.graphics.getHeight() - gridSize * rows;
			} else if (s == Side.RIGHT) {
				offsetX = Gdx.graphics.getWidth() - gridSize * columns;
			} else if (s == Side.BOTTOM) {
				offsetY = 0;
			} else if (s == Side.LEFT) {
				offsetX = 0;
			} else if (s == Side.CENTERX) {
				offsetX = Gdx.graphics.getWidth() / 2 - columns * gridSize / 2;
			} else if (s == Side.CENTERY) {
				offsetY = Gdx.graphics.getHeight() / 2 - rows * gridSize / 2;
			}
		}

		return this;
	}

	public void setItem(int row, int column, String item) {
		this.items[row][column] = item;
	}

	public String getItem(int row, int column) {
		return this.items[row][column];
	}

	public String[] getAllItems() {
		String[] itemArray = new String[rows * columns];
		int counter = 0;

		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				itemArray[counter] = items[i][j];
			}
		}

		return itemArray;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);

		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				shapeRenderer.rect(offsetX + (gridSize * i + gapSize / 2) , offsetY + (gridSize * j + gapSize / 2),
						gridSize - gapSize, gridSize - gapSize);
			}
		}

		shapeRenderer.end();
		batch.begin();
	}

}
