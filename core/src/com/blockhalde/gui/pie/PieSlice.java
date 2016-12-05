package com.blockhalde.gui.pie;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class PieSlice extends Label {
	private boolean isActive;
	private Command cmd;
	static LabelStyle labelStyle;
	static ShapeRenderer shapeRenderer;
	static final float RADIUS = 16.0f;
	
	static{
		labelStyle = new LabelStyle(new BitmapFont(), new Color(0.9f,0.9f,0.9f,1.0f));
		shapeRenderer = new ShapeRenderer();
	}
	
	public PieSlice(String name){
		super(name, labelStyle);
		isActive = false;
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
		shapeRenderer.begin();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.circle(this.getX(), this.getY(), RADIUS);
        shapeRenderer.end();
		super.draw(batch, parentAlpha);
	}

	protected void execute(){
		if(cmd != null) cmd.execute();
	}
}
