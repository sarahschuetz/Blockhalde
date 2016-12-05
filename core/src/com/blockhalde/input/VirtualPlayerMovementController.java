package com.blockhalde.input;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

/**
 * An implementation of the {@link VirtualController} interface that controls the player movement.
 * @author shaendro
 */
public class VirtualPlayerMovementController implements VirtualController {
	private InputSystem inputSystem;
	private boolean active = true;
	private Camera camera;
	private Keybindings keybindings = new Keybindings("util/keybindings.properties");

	private float movementFwd = 0;
	private float movementSide = 0;

	/**
	 * Creates a {@link VirtualPlayerMovementController} and attaches the given camera to it.
	 * @param camera A {@link Camera} for the {@link VirtualPlayerMovementController} to move around
	 */
	public VirtualPlayerMovementController(InputSystem inputSystem, Camera camera) {
		this.inputSystem = inputSystem;
		this.camera = camera;
		PauseListener.init();
	}

	@Override
	public void keyDown(int keycode) {
		if (active) {
		if (keycode == keybindings.getKey("FORWARD"))       movementFwd += 1;
		else if (keycode == keybindings.getKey("LEFT"))     movementSide += 1;
		else if (keycode == keybindings.getKey("BACKWARD")) movementFwd += -1;
		else if (keycode == keybindings.getKey("RIGHT"))    movementSide += -1;
		}
	}

	@Override
	public void keyUp(int keycode) {
		if (active) {
		if (keycode == keybindings.getKey("FORWARD"))       movementFwd -= 1;
		else if (keycode == keybindings.getKey("LEFT"))     movementSide -= 1;
		else if (keycode == keybindings.getKey("BACKWARD")) movementFwd -= -1;
		else if (keycode == keybindings.getKey("RIGHT"))    movementSide -= -1;
		}
	}

	@Override
	public void touchDown(int screenX, int screenY, int button) {
	}

	@Override
	public void touchUp(int screenX, int screenY, int button) {
	}

	@Override
	public void mouseMoved(int screenX, int screenY) {
	}

	@Override
	public void scrolled(int amount) {
	}

	@Override
	public void update(float deltaTime) {
		if (!PauseListener.isPaused() && active) {
			camera.translate(camera.direction.x * movementFwd, camera.direction.y * movementFwd, camera.direction.z * movementFwd);
			Vector3 side = camera.direction.cpy().rotate(camera.up, 90f);
			camera.translate(side.x * movementSide, 0, side.z * movementSide);
			camera.update();
		}
	}

	@Override
	public void resize(int width, int height) {
	}
	
	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
}
