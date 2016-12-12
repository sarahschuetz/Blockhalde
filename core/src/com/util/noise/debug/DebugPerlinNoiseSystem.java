package com.util.noise.debug;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.terrain.world.WorldManagementSystem;

public class DebugPerlinNoiseSystem extends EntitySystem {

	private Stage stage;
	private NoiseImg noise;
	
	@Override
	public void addedToEngine(Engine engine) {
		Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
		this.stage = new Stage(viewport);
		
		this.noise = new NoiseImg(engine.getSystem(WorldManagementSystem.class).getTerrainGenerator().getPerlinNoise());
		this.stage.addActor(this.noise);
	}

	@Override
	public void update(float deltaTime) {
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		stage.draw();
	}
	
	public void toggleDebugView() {
		noise.setVisible(!noise.isVisible());
	}
	
	public void incrementNoiseY() {
		noise.incrementY();
	}
	
	public void decrementNoiseY() {
		noise.decrementY();
	}

}
