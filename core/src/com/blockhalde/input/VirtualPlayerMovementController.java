package com.blockhalde.input;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

/**
 * An implementation of the {@link VirtualController} interface that controls the player movement.
 * @author shaendro
 */
public class VirtualPlayerMovementController extends VirtualAbstractController {
	private Camera camera;
	private Keybindings keybindings = new Keybindings("util/keybindings.properties");

	private float movementFwd = 0;
	private float movementSide = 0;

	/**
	 * Creates a {@link VirtualPlayerMovementController} and attaches the given camera to it.
	 * @param inputSystem The {@link InputSystem} the controller belongs to
	 * @param camera A {@link Camera} for the {@link VirtualPlayerMovementController} to move around
	 */
	public VirtualPlayerMovementController(InputSystem inputSystem, Camera camera) {
		super(inputSystem);
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
	public void update(float deltaTime) {
		if (!PauseListener.isPaused() && active) {
			camera.translate(camera.direction.x * movementFwd, camera.direction.y * movementFwd, camera.direction.z * movementFwd);
			Vector3 side = camera.direction.cpy().rotate(camera.up, 90f);
			camera.translate(side.x * movementSide, 0, side.z * movementSide);
			camera.update();
		}
	}
}
