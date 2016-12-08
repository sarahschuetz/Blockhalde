package com.blockhalde.gui.pie;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;

public class PieSlice extends Actor {
	private Vector2 originalPos;
	private boolean isActive;
	private Command cmd;
	private Label label;
	private int alignment;
	static LabelStyle labelStyle;
	static ShapeRenderer shapeRenderer;
	static final float DOTRADIUS = 13.0f;
	
	static{
		labelStyle = new LabelStyle(new BitmapFont(), new Color(0.9f,0.9f,0.9f,1.0f));
		shapeRenderer = new ShapeRenderer();
	}
	
	public PieSlice(String name){
		label = new Label(name, labelStyle);
		originalPos = new Vector2(this.getX(),this.getY());
		isActive = false;
		
		this.setVisible(false);
		//this.setAlignment(Align.bottomRight, Align.bottomRight); // doesnt do a shit
		//System.out.println(this.setAlignment(labelAlign, lineAlign););
		//this.setWidth(50);
		//this.setBounds(getX(), getY(), width, height);
		//this.validate();
	}
	
	public void setAlignment(int align){
		this.alignment = align;
		setPosition(this.getX(),this.getY());
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
	public void setPosition(float x, float y) {
		super.setPosition(x, y);

		if ((alignment & Align.left) != 0){
			x -= label.getWidth() + DOTRADIUS*2;
			y -= label.getHeight() * 0.5f;
		}else if ((alignment & Align.right) != 0){
			x += DOTRADIUS*2;
			y -= label.getHeight() * 0.5f;
		}else{
			x -= label.getWidth() * 0.5f;
		}
		
		if ((alignment & Align.top) != 0){
			y += DOTRADIUS*2;
		}else if ((alignment & Align.bottom) != 0){
			y -= DOTRADIUS*2 + label.getHeight();
		}else{
			//x -= label.getWidth() * 0.5f;
		}
		
		label.setPosition(x, y);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		label.draw(batch, parentAlpha);
		
		batch.end();
		shapeRenderer.begin(ShapeType.Filled);
		if(isActive){
			shapeRenderer.setColor(1, 1, 0, 1);
		}else{
			shapeRenderer.setColor(1, 1, 1, 1);
		}
		shapeRenderer.circle(this.getX(), this.getY(), DOTRADIUS*this.getScaleX());
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
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		label.setVisible(false);
	}

	protected void execute(){
		if(cmd != null) cmd.execute();
	}
}
