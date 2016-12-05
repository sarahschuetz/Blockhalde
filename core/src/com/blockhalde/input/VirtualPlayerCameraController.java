package com.blockhalde.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;

/**
 * An implementation of the {@link VirtualController} interface that controls the player camera.
 * @author shaendro
 */
public class VirtualPlayerCameraController implements VirtualController {
	private static final float ROTATION_SPEED = 0.5f;
	private static final float MAX_ROTATION = 90f;

	private InputSystem inputSystem;
	private boolean active = true;
	private Camera camera;
	private Vector3 startDirection = new Vector3(0, 0, -1);
	private Vector3 startUp = new Vector3(0, 1, 0);

	private int width = Gdx.graphics.getWidth();
	private int height = Gdx.graphics.getHeight();
	private int centerX = width/2;
	private int centerY = height/2;

	private float rotationX = 0;
	private float rotationY = 0;

	/**
	 * Creates a {@link VirtualPlayerCameraController} and attaches the given camera to it.
	 * @param camera A {@link Camera} for the {@link VirtualPlayerCameraController} to move around
	 */
	public VirtualPlayerCameraController(InputSystem inputSystem, Camera camera) {
		this.inputSystem = inputSystem;
		this.camera = camera;
		PauseListener.init();
	}

	@Override
	public void keyDown(int keycode) {
	}

	@Override
	public void keyUp(int keycode) {
	}

	@Override
	public void touchDown(int screenX, int screenY, int button) {
	}

	@Override
	public void touchUp(int screenX, int screenY, int button) {
	}

	@Override
	public void mouseMoved(int screenX, int screenY) {
		if (!PauseListener.isPaused() && active) {
			float deltaX = -Gdx.input.getDeltaX() * ROTATION_SPEED;
			float deltaY = -Gdx.input.getDeltaY() * ROTATION_SPEED;
			rotationX += deltaX;
			rotationY += deltaY;
			
			//Windows-only version that does not rely on CursorCatched
			//rotationX += (float)(centerX - screenX) / width * ROTATION_SPEED;
			//rotationY += (float)(centerY - screenY) / height * ROTATION_SPEED;
			//Gdx.input.setCursorPosition(centerX, centerY);
			
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

	@Override
	public void scrolled(int amount) {
	}

	@Override
	public void update(float deltaTime) {
		if (!PauseListener.isPaused() && active) {
		}
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		this.centerX = width/2;
		this.centerY = height/2;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
}
