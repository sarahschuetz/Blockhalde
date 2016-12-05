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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.blockhalde.gui.pie.PieMenuSystem;

public class RendererGUI {
	private static RendererGUI instance;

	Stage stage;
	Label debugLabel ;
	LabelStyle labelStyle;
	BottomGrid bottomGrid;
	BitmapFont debugFont;
	PieMenuSystem pieMenu;

	public static RendererGUI instance(){
		if(instance == null){
			instance = new RendererGUI();
		}
		return instance;
	}

	RendererGUI(){
		Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
		stage = new Stage(viewport);
		
		// setting up debug text
		debugFont = new BitmapFont();
		labelStyle = new LabelStyle(debugFont, new Color(0.9f,0.9f,0.9f,1.0f));
		debugLabel = new Label("", labelStyle);
		
		// setting up bottom grid
		bottomGrid = BottomGrid.getInstance();
		
		stage.addActor(debugLabel);
		stage.addActor(bottomGrid);
	}
	
	public Stage getStage(){
		return stage;
	}

	public void setDebugText(String text){
		debugLabel.setText(text);
		debugLabel.setPosition(5, Gdx.graphics.getHeight() - debugLabel.getPrefHeight()/2 - 5, Align.topLeft);
	}

	public void resize (int width, int height) {
		// Passing true when updating the viewport changes camera pos making 0,0 the bottom left corner.
		stage.getViewport().update(width, height, true);
	}

	public void render() {
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		stage.draw();
	}

	public void toggleMenu() {
		bottomGrid.setVisible(!bottomGrid.isVisible());
	}
	
	public void scrollItems(int value) {
		if (value > 0) {
			for (int i = 0; i < value; i++) {
				bottomGrid.increaseSelectedItem();
			}
		} else {
			for (int i = 0; i > value; i--) {
				bottomGrid.decreaseSelectedItem();
			}
		}
	}
}
