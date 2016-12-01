package com.blockhalde.input;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.render.CameraSystem;

public class InputSystem extends EntitySystem {
	
	private InputProcessor inputProcessor;
	private VirtualController cameraController;

	@Override
	public void addedToEngine(Engine engine) {
		CameraSystem c = engine.getSystem(CameraSystem.class);
		cameraController = new PlayerVirtualController(c.getCam());
		inputProcessor = new PhysicalInputProcessor(cameraController);
		Gdx.input.setInputProcessor(inputProcessor);
//		Gdx.input.setCursorCatched(true);
	}

	@Override
	public void update(float deltaTime) {
		cameraController.update(deltaTime);
	}

}
