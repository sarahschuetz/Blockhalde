package com.util.noise.debug;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.blockhalde.render.CameraSystem;
import com.terrain.world.WorldManagementSystem;
import com.util.noise.PerlinNoise3D;

public class DebugPerlinNoiseSystem extends EntitySystem {

	private Stage stage;
	private NoiseImg noise = new NoiseImg();
	
	@Override
	public void addedToEngine(Engine engine) {
		Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
		this.stage = new Stage(viewport);
		this.stage.addActor(noise);
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
	
	public void incrementNoiseZ() {
		noise.incrementZ();
	}
	
	public void decrementNoiseZ() {
		noise.decrementZ();
	}

}
