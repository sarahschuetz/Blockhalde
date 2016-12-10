package com.blockhalde.player;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

public class CameraComponent extends Component {
	private Camera camera;
	private Vector3 startDirection;
	private Vector3 startUp;
	
	public CameraComponent(Camera camera) {
		this.camera = camera;
		startDirection = new Vector3(camera.direction);
		startUp = new Vector3(camera.up);
	}

	public Camera getCamera() {
		return camera;
	}
	
	public void resetCamera() {
		camera.direction.set(startDirection);
		camera.up.set(startUp);
	}
}
