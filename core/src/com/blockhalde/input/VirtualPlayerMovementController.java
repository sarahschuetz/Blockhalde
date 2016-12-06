package com.blockhalde.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.blockhalde.player.CameraComponent;
import com.blockhalde.player.PlayerDataComponent;
import com.blockhalde.player.PositionComponent;
import com.util.PauseListener;

/**
 * An implementation of the {@link VirtualController} interface that controls the player movement.
 * @author shaendro
 */
public class VirtualPlayerMovementController extends VirtualAbstractController {
	private Keybindings keybindings = new Keybindings("util/keybindings.properties");

	private int movementFwd = 0;
	private int movementSide = 0;

	/**
	 * Creates a {@link VirtualPlayerMovementController}.
	 * @param inputSystem The {@link InputSystem} the controller belongs to
	 */
	public VirtualPlayerMovementController(InputSystem inputSystem) {
		super(inputSystem);
		PauseListener.init();
	}

	@Override
	public void keyDown(int keycode) {
		if (active) {
			if (keycode == keybindings.getKey("FORWARD"))       movementFwd  += 1;
			else if (keycode == keybindings.getKey("LEFT"))     movementSide += 1;
			else if (keycode == keybindings.getKey("BACKWARD")) movementFwd  += -1;
			else if (keycode == keybindings.getKey("RIGHT"))    movementSide += -1;
		}
	}

	@Override
	public void keyUp(int keycode) {
		if (active) {
			if (keycode == keybindings.getKey("FORWARD"))       movementFwd  -= 1;
			else if (keycode == keybindings.getKey("LEFT"))     movementSide -= 1;
			else if (keycode == keybindings.getKey("BACKWARD")) movementFwd  -= -1;
			else if (keycode == keybindings.getKey("RIGHT"))    movementSide -= -1;
		}
	}

	@Override
	public void update(float deltaTime) {
		if (!PauseListener.isPaused() && active) {
			Entity player = inputSystem.getEngine().getEntitiesFor(Family.all(PlayerDataComponent.class).get()).first();
			if (player != null) {
				Camera camera = player.getComponent(CameraComponent.class).getCamera();
				Vector3 side = camera.direction.cpy().rotate(camera.up, 90f);
				Vector3 position = player.getComponent(PositionComponent.class).getPosition();
				position.add(camera.direction.x * movementFwd, camera.direction.y * movementFwd, camera.direction.z * movementFwd);
				position.add(side.x * movementSide, side.y * movementSide, side.z * movementSide);
				camera.position.set(position);
				camera.update();
			}
		}
	}
}
