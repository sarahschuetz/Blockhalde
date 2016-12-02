package com.blockhalde.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.blockhalde.gui.RendererGUI;

public class PlayerVirtualController implements VirtualController {
	private static final float ROTATION_SPEED = 180f;
	private static final float MAX_ROTATION = 90;

	private Camera camera;
	private Vector3 startDirection = new Vector3(0, 0, -1);
	private Vector3 startUp = new Vector3(0, 1, 0);

	private int width = Gdx.graphics.getWidth();
	private int height = Gdx.graphics.getHeight();
	private int centerX = width/2;
	private int centerY = height/2;

	private float rotationX = 0;
	private float rotationY = 0;

	private float movementFwd = 0;
	private float movementSide = 0;
	private boolean isJumping = false;
	private boolean isDigging = false;

	public PlayerVirtualController(Camera camera) {
		this.camera = camera;
		PauseListener.init();
	}

	public float getMovementFwd() {
		return movementFwd;
	}

	public float getMovementSide() {
		return movementSide;
	}

	public boolean isJumping() {
		return isJumping;
	}

	public boolean isDigging() {
		return isDigging;
	}

	@Override
	public void keyDown(int keycode) {
		if (keycode == Keybindings.FORWARD)           movementFwd += 1;
		else if (keycode == Keybindings.LEFT)         movementSide += 1;
		else if (keycode == Keybindings.BACKWARD)     movementFwd += -1;
		else if (keycode == Keybindings.RIGHT)        movementSide += -1;
		else if (keycode == Keybindings.JUMP)         isJumping = true;
		else if (keycode == Keybindings.INV_TOGGLE)   RendererGUI.instance().toggleMenu();
		else if (keycode == Keybindings.INV_FORWARD)  RendererGUI.instance().scrollItems(1);
		else if (keycode == Keybindings.INV_BACKWARD) RendererGUI.instance().scrollItems(-1);
		else if (keycode == Keybindings.QUIT)		  Gdx.app.exit();
	}

	@Override
	public void keyUp(int keycode) {
		if (keycode == Keybindings.FORWARD)       movementFwd -= 1;
		else if (keycode == Keybindings.LEFT)     movementSide -= 1;
		else if (keycode == Keybindings.BACKWARD) movementFwd -= -1;
		else if (keycode == Keybindings.RIGHT)    movementSide -= -1;
	}

	@Override
	public void touchDown(int screenX, int screenY, int button) {
		if (button == Buttons.LEFT) isDigging = true;
	}

	@Override
	public void touchUp(int screenX, int screenY, int button) {
		if (button == Buttons.LEFT) isDigging = false;
	}

	@Override
	public void mouseMoved(int screenX, int screenY) {
		if (!PauseListener.isPaused()) {
			rotationX += (float)(centerX - screenX) / width * ROTATION_SPEED;
			rotationY += (float)(centerY - screenY) / height * ROTATION_SPEED;

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
		RendererGUI.instance().scrollItems(amount);
	}

	@Override
	public void update(float deltaTime) {
		if (!PauseListener.isPaused()) {
			Gdx.input.setCursorPosition(centerX, centerY);
			camera.translate(camera.direction.x * movementFwd, camera.direction.y * movementFwd, camera.direction.z * movementFwd);
			Vector3 side = camera.direction.cpy().rotate(camera.up, 90f);
			camera.translate(side.x * movementSide, 0, side.z * movementSide);
			camera.update();
		}
	}

	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		this.centerX = width/2;
		this.centerY = height/2;
	}
}
