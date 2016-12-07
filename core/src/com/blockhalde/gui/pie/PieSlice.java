package com.blockhalde.gui.pie;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class PieSlice extends Label {
	private Vector2 originalPos;
	private boolean isActive;
	private Command cmd;
	static LabelStyle labelStyle;
	static ShapeRenderer shapeRenderer;
	static final float DOTRADIUS = 13.0f;
	
	static{
		labelStyle = new LabelStyle(new BitmapFont(), new Color(0.9f,0.9f,0.9f,1.0f));
		shapeRenderer = new ShapeRenderer();
	}
	
	public PieSlice(String name){
		super(name, labelStyle);
		originalPos = new Vector2(this.getX(),this.getY());
		isActive = false;
		this.setAlignment(Align.center);
		this.setEllipsis(true);
		this.setVisible(false);
	}
	
	public PieSlice setCommand(Command command) {
		cmd = command;
		return this;
	}
	
	protected boolean isActive(){
		return isActive;
	}
	
	protected PieSlice toggleActive(){
		isActive = !isActive;
		return this;
	}
	
	protected PieSlice setActive(boolean active){
		isActive = active;
		return this;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.end();
		shapeRenderer.begin(ShapeType.Filled);
		if(isActive){
			shapeRenderer.setColor(1, 1, 0, 1);
		}else{
			shapeRenderer.setColor(1, 1, 1, 1);
		}
		shapeRenderer.circle(this.getX(), this.getY(), DOTRADIUS);
        shapeRenderer.end();
		batch.begin();
	}
	
	public void setOriginalPos(Vector2 v){
		originalPos.x = v.x;
		originalPos.y = v.y;
	}
	
	public Vector2 getOriginalPos(){
		return originalPos;
	}

	protected void execute(){
		if(cmd != null) cmd.execute();
	}
}
