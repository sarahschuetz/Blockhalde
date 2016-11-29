package com.blockhalde.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class RendererGUI {
	private static RendererGUI instance;
	
	Stage stage;
	Text debugText;
	BottomGrid bottomGrid;
	
	public static RendererGUI instance(){
		if(instance == null){
			instance = new RendererGUI();
		}
		return instance;
	}
	
	RendererGUI(){
		Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
		stage = new Stage(viewport);
		debugText = new Text();
		bottomGrid = BottomGrid.getInstance();
		stage.addActor(debugText);
		stage.addActor(bottomGrid);
	}
	
    public void setDebugText(String text){
        debugText.setText(text);
    }
	
	public void resize (int width, int height) {
		// Passing true when updating the viewport changes camera pos making 0,0 the bottom left corner.
	    stage.getViewport().update(width, height, true);
	}

	public void render() {
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
	    stage.draw();
	    
	    toggleMenu();
	}
	
	private void toggleMenu() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
			if (bottomGrid.isVisible()) {
				bottomGrid.setVisible(false);
			} else {
				bottomGrid.setVisible(true);
			}
			
		}
	}
}
