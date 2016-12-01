package com.blockhalde.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.blockhalde.gui.RendererGUI;

public class PlayerVirtualController implements VirtualController {
	private static final float ROTATION_SPEED = 180f;

	private Camera camera;

	private int centerX = Gdx.graphics.getWidth()/2;
	private int centerY = Gdx.graphics.getHeight()/2;

	private float movementFwd = 0;
	private float movementSide = 0;
	private boolean isJumping = false;
	private boolean isDigging = false;

	public PlayerVirtualController(Camera camera) {
		this.camera = camera;
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
		final int width = Gdx.graphics.getWidth();
		final int height = Gdx.graphics.getHeight();
		resize(width, height);

		final float deltaX = (float)(centerX - screenX) / width;
		final float deltaY = (float)(centerY - screenY) / height;
		Gdx.input.setCursorPosition(centerX, centerY);

		camera.rotate(Vector3.Y, deltaX * ROTATION_SPEED);
		if ((camera.direction.y > -0.965 && deltaY < 0) || (camera.direction.y < 0.965 && deltaY > 0))
			camera.rotate(camera.direction.cpy().crs(Vector3.Y), deltaY * ROTATION_SPEED);
		
		camera.update();
	}

	@Override
	public void scrolled(int amount) {
		RendererGUI.instance().scrollItems(amount);
	}

	@Override
	public void update() {
		camera.translate(camera.direction.x * movementFwd, camera.direction.y * movementFwd, camera.direction.z * movementFwd);
		Vector3 side = camera.direction.cpy().rotate(90f, 0, 1, 0);
		camera.translate(side.x * movementSide, 0, side.z * movementSide);
		camera.update();
	}

	private void resize(int width, int height) {
		centerX = width / 2;
		centerY = height / 2;
	}
}
