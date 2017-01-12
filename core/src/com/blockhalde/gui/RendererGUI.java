package com.blockhalde.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.blockhalde.gui.pie.PieSlice;

public class RendererGUI {
	private static RendererGUI instance;
	
	Stage stage;
	Label debugLabel ;
	LabelStyle labelStyle;
	BitmapFont debugFont;
	
	public static RendererGUI instance(){
		if(instance == null){
			instance = new RendererGUI();
		}
		return instance;
	}

	RendererGUI(){
		Viewport viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
		stage = new Stage(viewport);
		
		// setting up debug text
		debugFont = new BitmapFont();
		labelStyle = new LabelStyle(debugFont, new Color(0.9f,0.9f,0.9f,1.0f));
		debugLabel = new Label("", labelStyle);
		
		stage.addActor(debugLabel);
		
	}
	
	public Stage getStage(){
		return stage;
	}

	public void setDebugText(String text){
		debugLabel.setPosition(5, Gdx.graphics.getHeight() - debugLabel.getPrefHeight()/2 - 5, Align.topRight);
		
		debugLabel.setText(text);
	}
	
	public void addDebugText(String text){
		debugLabel.setText(debugLabel.getText().append(text));
		debugLabel.setPosition(10, Gdx.graphics.getHeight() - debugLabel.getPrefHeight()/2 - 10, Align.topRight);
	}

	public void resize (int width, int height) {
		// Passing true when updating the viewport changes camera pos making 0,0 the bottom left corner.
		stage.getViewport().update(width, height, true);
		debugLabel.setPosition(5, Gdx.graphics.getHeight() - debugLabel.getPrefHeight()/2 - 5, Align.topRight);
		PieSlice.loadFonts();
	}

	public void render() {
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		stage.draw();
		debugLabel.setText("");
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
	}
	
}
