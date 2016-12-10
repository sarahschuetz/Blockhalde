package com.blockhalde.player;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.blockhalde.input.InputSystem;

public class Player extends Entity {
	/**
	 * Creates a {@link Player} and attaches the given {@link Camera} to it.
	 * This {@link Entity} will be controlled by the {@link InputSystem}.
	 * @param camera The camera to attach to the {@link Player}
	 */
	public Player(Camera camera) {
		add(new PlayerDataComponent());
		add(new CameraComponent(camera));
		add(new PositionComponent(camera.position));
		add(new DebugComponent());
	}
	
	/**
	 * Creates a {@link Player} without attaching a {@link Camera} to it.
	 * It will not be controlled by the {@link InputSystem}.
	 * @param position The position of the {@link Entity}
	 */
	public Player(Vector3 position) {
		add(new PlayerDataComponent());
		add(new PositionComponent(position));
	}
}
