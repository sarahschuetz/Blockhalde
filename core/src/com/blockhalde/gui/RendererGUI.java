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
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
<<<<<<< HEAD
import com.blockhalde.gui.grid.BlurActor;
import com.blockhalde.gui.grid.BottomGrid;
import com.blockhalde.gui.grid.InventoryGrid;
=======
import com.blockhalde.gui.pie.PieSlice;
>>>>>>> e7328e50e11fa369b5a3f12bfb615a8d8226e0a3

public class RendererGUI {
	private static RendererGUI instance;
	
	public int menusActive = 0;

	Stage stage;
	Label debugLabel ;
	LabelStyle labelStyle;
	BottomGrid bottomGrid;
	InventoryGrid inventoryGrid;
	BitmapFont debugFont;
	BlurActor blurActor;
	
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
		
		// setting up bottom grid
		bottomGrid = BottomGrid.getInstance();
		
		// setting up inventory grid
		inventoryGrid = InventoryGrid.getInstance();
		
		blurActor = new BlurActor();
		
		stage.addActor(blurActor);
		stage.addActor(debugLabel);
		stage.addActor(bottomGrid);
		stage.addActor(inventoryGrid);
		
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
	}

	public void toggleMenu() {
		inventoryGrid.setVisible(!inventoryGrid.isVisible());
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
	
	public void setBlurActive() {
		menusActive++;
		blurActor.setVisible(true);
	}
	
	public void setBlurInactive() {
		menusActive--;
		blurActor.setVisible(false);
	}
}
