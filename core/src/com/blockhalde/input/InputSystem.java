package com.blockhalde.input;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;

/**
 * An {@link EntitySystem} that handles user input.
 * @author shaendro
 */
public class InputSystem extends EntitySystem {
	private Engine engine;
	private PhysicalInputProcessor inputProcessor;
	private VirtualController gameController;
	private VirtualController cameraController;
	private VirtualController movementController;

	@Override
	public void addedToEngine(Engine engine) {
		this.engine = engine;
		gameController = new VirtualGameController(this);
		cameraController = new VirtualPlayerCameraController(this);
		movementController = new VirtualPlayerMovementController(this);
		inputProcessor = new PhysicalInputProcessor(gameController, cameraController, movementController);
		Gdx.input.setInputProcessor(inputProcessor);
		Gdx.input.setCursorCatched(true);
	}

	@Override
	public void update(float deltaTime) {
		for (VirtualController controller : inputProcessor.getControllers()) {
			controller.update(deltaTime);
		}
	}

	/**
	 * Is called by the application on resize.
	 * Relays the command to the {@link VirtualController}.
	 * @param width The new width of the window
	 * @param height The new height of the window
	 */
	public void resize(int width, int height) {
		for (VirtualController controller : inputProcessor.getControllers()) {
			controller.resize(width, height);
		}
	}

	/**
	 * Is mostly called by {@link VirtualController}s which need access to the {@link Engine}.
	 * @return The {@link Engine} the {@link InputSystem} is attached to
	 */
	public Engine getEngine() {
		return engine;
	}
	
	/**
	 * Is mostly called by {@link VirtualController}s which need access to other {@link VirtualController}s.
	 * @return The desired {@link VirtualController} if it is registered, null otherwise
	 */
	public <T extends VirtualController> VirtualController getController(Class<T> controllerClass) {
		for (VirtualController controller : inputProcessor.getControllers()) {
			if (controller.getClass().equals(controllerClass)) return controller;
		}
		return null;
	}
}
