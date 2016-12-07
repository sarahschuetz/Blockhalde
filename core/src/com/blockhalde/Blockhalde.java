package com.blockhalde;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.blockhalde.gui.RendererGUI;
import com.blockhalde.input.InputSystem;
import com.blockhalde.player.Player;
import com.blockhalde.render.CameraSystem;
import com.blockhalde.render.RenderSystem;
import com.terrain.world.WorldManagementSystem;
import com.util.noise.debug.DebugPerlinNoiseSystem;

public class Blockhalde extends ApplicationAdapter {
	
	private Engine engine;
	private CameraSystem cameraSystem;

	@Override
	public void create() {
		engine = new Engine();
		
		cameraSystem = new CameraSystem();
		engine.addSystem(cameraSystem);
		engine.addSystem(new InputSystem());
		engine.addSystem(new WorldManagementSystem());
		engine.addSystem(new RenderSystem());
		engine.addSystem(new DebugPerlinNoiseSystem());
		
		engine.addEntity(new Player(cameraSystem.getCam()));
	}
	
	@Override
	public void resize(int width, int height){
		cameraSystem.resize(width, height);
		RendererGUI.instance().resize(width, height);
		engine.getSystem(InputSystem.class).resize(width, height);
	}

	@Override
	public void render() {
		engine.update(Gdx.graphics.getRawDeltaTime());
		RendererGUI.instance().setDebugText("fps " + Gdx.graphics.getFramesPerSecond() + 
				"\ncam pos " + cameraSystem.getCam().position.toString() + 
				"\nM: toggle menu, Q + E: iterate items" +
				"\nN: toggle Perlin Noise Debug View");
		RendererGUI.instance().render();
	}
}