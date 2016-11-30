package com.blockhalde;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.blockhalde.gui.RendererGUI;
import com.blockhalde.input.InputSystem;
import com.render.CameraSystem;
import com.render.RenderSystem;
import com.terrain.world.World;

public class Blockhalde extends ApplicationAdapter {
	
	private World world;
	private Engine engine;
	private CameraSystem cameraSystem;

	@Override
	public void create() {
		engine = new Engine();
		world = new World();
		
		cameraSystem = new CameraSystem();
		engine.addSystem(cameraSystem);
		engine.addSystem(new RenderSystem(world));
		engine.addSystem(new InputSystem());
	}
	
	@Override
	public void resize(int width, int height){
		cameraSystem.resize(width, height);
		RendererGUI.instance().resize(width, height);
	}

	@Override
	public void render() {
		RendererGUI.instance().setDebugText("fps " + Gdx.graphics.getFramesPerSecond() + 
				"\ncam pos " + cameraSystem.getCam().position.toString() + 
				"\nM: toggle menu, Q + E: iterate items");
		RendererGUI.instance().render();

		engine.update(Gdx.graphics.getRawDeltaTime());
	}
}