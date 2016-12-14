package com.blockhalde.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.blockhalde.player.CameraComponent;
import com.blockhalde.player.Player;
import com.blockhalde.player.PlayerDataComponent;
import com.terrain.block.BlockType;
import com.terrain.world.WorldManagementSystem;
import com.util.PauseListener;

/**
 * An implementation of the {@link VirtualController} interface that controls the player camera.
 * @author shaendro
 */
public class VirtualPlayerCameraController extends VirtualAbstractController {
	private static final float ROTATION_SPEED = 0.5f;
	private static final float MAX_ROTATION = 90f;
	private static final float RAYCAST_RANGE = 2.9f;
	private static final float RAYCAST_STEPS = 100;
	private static final float RAYCAST_STEPSIZE = RAYCAST_RANGE / RAYCAST_STEPS;

	private float rotationX = 0;
	private float rotationY = 0;
	
	private Vector3 blockPosition = null;
	
	/**
	 * Creates a {@link VirtualPlayerCameraController}.
	 * @param inputSystem The {@link InputSystem} the controller belongs to
	 */
	public VirtualPlayerCameraController(InputSystem inputSystem) {
		super(inputSystem);
		PauseListener.init();
	}

	@Override
	public void touchDragged(int screenX, int screenY) {
		mouseMoved(screenX, screenY);
	}

	@Override
	public void mouseMoved(int screenX, int screenY) {
		if (!PauseListener.isPaused() && active) {
			ImmutableArray<Entity> query = inputSystem.getEngine().getEntitiesFor(Family.all(PlayerDataComponent.class, CameraComponent.class).get());
			if (query.size() != 0) {
				Entity player = query.first();
				rotationX += -Gdx.input.getDeltaX() * ROTATION_SPEED;
				rotationY += -Gdx.input.getDeltaY() * ROTATION_SPEED;

				rotationX = rotationX % 360;
				if (rotationY > MAX_ROTATION) rotationY = MAX_ROTATION;
				else if (rotationY < -MAX_ROTATION) rotationY = -MAX_ROTATION;

				player.getComponent(CameraComponent.class).resetCamera();
				Camera camera = player.getComponent(CameraComponent.class).getCamera();
				camera.rotate(Vector3.Y, rotationX);
				camera.rotate(camera.direction.cpy().crs(camera.up), rotationY);
				camera.update();
				updateSelectedBlock();
			}
		}
	}

	/**
	 * Updates the field blockPosition according to the {@link Camera} of the {@link Player}.
	 */
	private void updateSelectedBlock() {
		ImmutableArray<Entity> query = inputSystem.getEngine().getEntitiesFor(Family.all(PlayerDataComponent.class, CameraComponent.class).get());
		if (query.size() != 0) {
			Entity player = query.first();
			Camera camera = player.getComponent(CameraComponent.class).getCamera();
			blockPosition = camera.position.cpy();
			WorldManagementSystem wms = inputSystem.getEngine().getSystem(WorldManagementSystem.class);
			for (int i = 0; i < RAYCAST_STEPS; i++) {
				blockPosition.add(camera.direction.x * RAYCAST_STEPSIZE, camera.direction.y * RAYCAST_STEPSIZE, camera.direction.z * RAYCAST_STEPSIZE);
				if (wms.getBlockType((int) blockPosition.x, (int) blockPosition.y, (int) blockPosition.z) != BlockType.AIR.getBlockId()) {
					blockPosition.set((int) blockPosition.x, (int) blockPosition.y, (int) blockPosition.z);
					return;
				}
			}
			blockPosition = null;
		}
	}

	/**
	 * Can be used to acquire the position of the selected block.
	 * @return A {@link Vector3} representing the position or null if there is no selected block
	 */
	public Vector3 getBlockPosition() {
		return blockPosition;
	}
}
