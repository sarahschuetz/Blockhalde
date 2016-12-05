package com.blockhalde.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.blockhalde.player.CameraComponent;
import com.blockhalde.player.PlayerDataComponent;
import com.util.PauseListener;

/**
 * An implementation of the {@link VirtualController} interface that controls the player camera.
 * @author shaendro
 */
public class VirtualPlayerCameraController extends VirtualAbstractController {
	private static final float ROTATION_SPEED = 0.5f;
	private static final float MAX_ROTATION = 90f;

	private float rotationX = 0;
	private float rotationY = 0;

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
			Entity player = inputSystem.getEngine().getEntitiesFor(Family.all(PlayerDataComponent.class).get()).first();
			if (player != null) {
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
			}
		}
	}
}
