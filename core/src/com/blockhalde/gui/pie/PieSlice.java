package com.blockhalde.gui.pie;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class PieSlice {
	private String name;
	private boolean isActive;
	private Label label;
	private Command cmd;
	static LabelStyle labelStyle;
	
	static{
		labelStyle = new LabelStyle(new BitmapFont(), new Color(0.9f,0.9f,0.9f,1.0f));
	}
	
	public PieSlice(String name){
		this.name = name;
		isActive = false;
		label = new Label(name, labelStyle);
	}
	
	public PieSlice setName(String name){
		this.name = name;
		label.setText(name);
		return this;
	}
	
	public PieSlice setCommand(Command command) {
		cmd = command;
		return this;
	}
	
	public String getName(){
		return name;
	}
	
	protected Label getLabel(){
		return label;
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

	protected void execute(){
		if(cmd != null) cmd.execute();
	}
}
