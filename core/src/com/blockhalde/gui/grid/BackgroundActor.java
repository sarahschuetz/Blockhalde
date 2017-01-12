package com.blockhalde.gui.grid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BackgroundActor extends Actor {
	
	protected ShapeRenderer shapeRenderer;
	protected int width = 0;
	protected int height = 0;
	protected int startX = 0;
	protected int startY = 0;
	protected Color background;
	
	public BackgroundActor(int startX, int startY, int width, int height, Color background) {
		shapeRenderer = new ShapeRenderer();
		this.width = width;
		this.height = height;
		this.startX = startX;
		this.startY = startY;
		this.background = background;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(background);
		shapeRenderer.rect(startX, startY, width, height);
		shapeRenderer.end();
		
		batch.begin();
	} 

}
