package com.blockhalde.gui.grid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GridActor extends Actor {
	
	private final int ITEM_OFFSET = 5;
	
	protected ShapeRenderer shapeRenderer;
	private int gridSize;
	private GridInstance[][] grids;
	
	protected String text = "";
	protected GlyphLayout layout;
	protected BitmapFont font;
	
	public GridActor(int gridSize, GridInstance[][] grids) {
		shapeRenderer = new ShapeRenderer();
		
		font = new BitmapFont();
        font.setColor(Color.WHITE);
        
        layout = new GlyphLayout();
		
		this.gridSize = gridSize;
		this.grids = grids;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		
		drawGridActorBackground();
		
		batch.begin();
		
		drawGridActorItem(batch);
		
		drawGridActorNumbers(batch);
		
	}

	private void drawGridActorItem(Batch batch) {
		for (GridInstance[] gridRow : grids) {
			for (GridInstance singleGrid : gridRow) {
				if (singleGrid.item != null) {
					batch.draw(singleGrid.item.getImage(), singleGrid.position.x + singleGrid.gapSize + ITEM_OFFSET, singleGrid.position.y + singleGrid.gapSize + ITEM_OFFSET, (float)gridSize - singleGrid.gapSize * 2 - ITEM_OFFSET * 2, (float)gridSize - singleGrid.gapSize * 2 - ITEM_OFFSET * 2);
				}	
			}
		}
	} 
	
	private void drawGridActorNumbers(Batch batch) {
		for (GridInstance[] gridRow : grids) {
			for (GridInstance singleGrid : gridRow) {
				if (singleGrid.item != null) {
					layout.setText(font, singleGrid.item.size + "");
					font.draw(batch, layout, singleGrid.position.x + gridSize/2 - layout.width/2, singleGrid.position.y + gridSize/2);
				}	
			}
		}
	}
	
	private void drawGridActorBackground() {
		for (GridInstance[] gridRow : grids) {
			for (GridInstance singleGrid : gridRow) {
				shapeRenderer.begin(singleGrid.shapeType);
				
				if (singleGrid.selected) {
					shapeRenderer.setColor(singleGrid.color.r, singleGrid.color.g, singleGrid.color.b, singleGrid.color.a + 0.3f);
				} else {
					shapeRenderer.setColor(singleGrid.color);
				}
				
				shapeRenderer.rect(singleGrid.position.x + singleGrid.gapSize, singleGrid.position.y + singleGrid.gapSize, (float)gridSize - singleGrid.gapSize * 2, (float)gridSize - singleGrid.gapSize * 2);
				shapeRenderer.end();
				
			}
		}
	}
	

}
