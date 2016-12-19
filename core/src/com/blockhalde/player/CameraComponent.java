package com.blockhalde.player;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

/**
 * An extension of the {@link Component} class which contains a {@link Player}-attached {@link Camera}.
 * @author shaendro
 */
public class CameraComponent extends Component {
	private Camera camera;
	private Vector3 startDirection;
	private Vector3 startUp;
	
	/**
	 * Creates the {@link CameraComponent} with the given {@link Camera}.
	 * @param camera The {@link Camera} the {@link CameraComponent} is based on
	 */
	public CameraComponent(Camera camera) {
		this.camera = camera;
		startDirection = new Vector3(camera.direction);
		startUp = new Vector3(camera.up);
	}

	/**
	 * @return The attached {@link Camera}
	 */
	public Camera getCamera() {
		return camera;
	}
	
	/**
	 * Resets the direction- and up-vectors of the {@link Camera} to their starting values.
	 */
	public void resetCamera() {
		camera.direction.set(startDirection);
		camera.up.set(startUp);
	}
}
