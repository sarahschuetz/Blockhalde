package com.blockhalde.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
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

	private int movementFwrd = 0;
	private int movementSide = 0;
	private Vector3 movementVectorFwrd = new Vector3();
	private Vector3 movementVectorSide = new Vector3();

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
			if (keycode == keybindings.getKey("FORWARD"))       movementFwrd += 1;
			else if (keycode == keybindings.getKey("LEFT"))     movementSide += 1;
			else if (keycode == keybindings.getKey("BACKWARD")) movementFwrd -= 1;
			else if (keycode == keybindings.getKey("RIGHT"))    movementSide -= 1;
		}
	}

	@Override
	public void keyUp(int keycode) {
		if (active) {
			if (keycode == keybindings.getKey("FORWARD"))       movementFwrd -= 1;
			else if (keycode == keybindings.getKey("LEFT"))     movementSide -= 1;
			else if (keycode == keybindings.getKey("BACKWARD")) movementFwrd += 1;
			else if (keycode == keybindings.getKey("RIGHT"))    movementSide += 1;
		}
	}

	@Override
	public void update(float deltaTime) {
		if (!PauseListener.isPaused() && active) {
			ImmutableArray<Entity> query = inputSystem.getEngine().getEntitiesFor(Family.all(PlayerDataComponent.class, CameraComponent.class).get());
			if (query.size() != 0) {
				Entity player = query.first();
				Camera camera = player.getComponent(CameraComponent.class).getCamera();
				Vector3 position = player.getComponent(PositionComponent.class).getPosition();
				position.add(prepareMovementVectorFwrd(camera));
				position.add(prepareMovementVectorSide(camera));
				camera.position.set(position);
				camera.update();
			}
		}
	}

	private Vector3 prepareMovementVectorFwrd(Camera camera) {
		float balancedMovementFwrd = (float) movementFwrd / (movementSide != 0 ? 2 : 1);
		return movementVectorFwrd.set(camera.direction.x * balancedMovementFwrd, camera.direction.y * balancedMovementFwrd, camera.direction.z * balancedMovementFwrd);
	}

	private Vector3 prepareMovementVectorSide(Camera camera) {
		float balancedMovementSide = (float) movementSide / (movementFwrd != 0 ? 2 : 1);
		return movementVectorSide.set(camera.direction).rotate(camera.up, 90f).set(movementVectorSide.x * balancedMovementSide, movementVectorSide.y * balancedMovementSide, movementVectorSide.z * balancedMovementSide);
	}
}
