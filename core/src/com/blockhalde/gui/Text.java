package com.blockhalde.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Text extends Actor{
    BitmapFont font;
    String text;
    
    public Text(String text){
    	this.text = text;
        font = new BitmapFont();
        font.setColor(0.9f,0.9f,0.9f,1);
        this.setPosition(5, Gdx.graphics.getHeight()-5);
    }
    
    public Text(){
    	this("");
    }
    
    public void setText(String text){
    	this.text = text;
    }
    
	@Override
	public void draw(Batch batch, float parentAlpha) {
		font.draw(batch, text, this.getX(), this.getY());
		super.draw(batch, parentAlpha);
	}
}
