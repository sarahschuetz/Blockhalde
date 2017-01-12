package com.blockhalde.gui.grid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class HeadingTextActor extends Actor {
	
	protected ShapeRenderer shapeRenderer;
	protected GlyphLayout layout;
	protected BitmapFont font;
	protected String text = "";
	int yPosition = 0;
	int gridSize = 0;
	
	public HeadingTextActor(String text, int gridSize, int yPosition) {
		shapeRenderer = new ShapeRenderer();
		
		font = new BitmapFont();
        font.setColor(Color.WHITE);
        
        layout = new GlyphLayout();
        
        this.text = text;
        this.yPosition = yPosition;
        this.gridSize = gridSize;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		layout.setText(font, text);
		font.draw(batch, layout, Gdx.graphics.getWidth()/2 - layout.width/2, yPosition * gridSize + gridSize/2 + layout.height/2);
	} 
	
	public void setText(String text) {
		this.text = text;
	}

}
