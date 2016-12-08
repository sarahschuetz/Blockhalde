package com.blockhalde.gui.pie;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ArrowActor extends Actor {
	
	private Vector2 vector;
	static ShapeRenderer shapeRenderer;
	
	/**
	 * Draws an arrow onto the stage - direction and length is determined by setVector
	 */
	public ArrowActor(){
		vector = new Vector2(1,0);
		shapeRenderer = new ShapeRenderer();
	}
	
	public void setVector(Vector2 v){
		vector.set(v);
	}
	
	public Vector2 getVector(){
		return vector;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1, 1, 1, 1);
		//shapeRenderer.line(this.getX(), this.getY(), this.getX() + vector.x, this.getY() + vector.y);
		shapeRenderer.circle(this.getX() + vector.x, this.getY() + vector.y, 5);
        shapeRenderer.end();
		batch.begin();
	}
	
}
