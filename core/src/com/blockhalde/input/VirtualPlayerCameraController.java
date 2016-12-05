package com.blockhalde.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

/**
 * An implementation of the {@link VirtualController} interface that controls the player camera.
 * @author shaendro
 */
public class VirtualPlayerCameraController extends VirtualAbstractController {
	private static final float ROTATION_SPEED = 0.5f;
	private static final float MAX_ROTATION = 90f;

	private Camera camera;
	private Vector3 startDirection;
	private Vector3 startUp;

	private float rotationX = 0;
	private float rotationY = 0;

	/**
	 * Creates a {@link VirtualPlayerCameraController} and attaches the given camera to it.
	 * @param inputSystem The {@link InputSystem} the controller belongs to
	 * @param camera A {@link Camera} for the {@link VirtualPlayerCameraController} to move around
	 */
	public VirtualPlayerCameraController(InputSystem inputSystem, Camera camera) {
		super(inputSystem);
		this.camera = camera;
		this.startDirection = new Vector3(camera.direction);
		this.startUp = new Vector3(camera.up);
		PauseListener.init();
	}

	@Override
	public void touchDragged(int screenX, int screenY) {
		mouseMoved(screenX, screenY);
	}

	@Override
	public void mouseMoved(int screenX, int screenY) {
		if (!PauseListener.isPaused() && active) {
			float deltaX = -Gdx.input.getDeltaX() * ROTATION_SPEED;
			float deltaY = -Gdx.input.getDeltaY() * ROTATION_SPEED;
			rotationX += deltaX;
			rotationY += deltaY;
			
			rotationX = rotationX % 360;
			if (rotationY > MAX_ROTATION) rotationY = MAX_ROTATION;
			else if (rotationY < -MAX_ROTATION) rotationY = -MAX_ROTATION;

			camera.direction.set(startDirection);
			camera.up.set(startUp);
			camera.rotate(Vector3.Y, rotationX);
			camera.rotate(camera.direction.cpy().crs(camera.up), rotationY);

			camera.update();
		}
	}
}
