package com.blockhalde.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.blockhalde.gui.RendererGUI;

public class PlayerVirtualController implements VirtualController {
	private Camera camera;
	private int startX = 0;
	private int startY = 0;
	private Vector3 tmpV1 = new Vector3();
	private Vector3 target = new Vector3();
	private float rotateAngle = 360f;
	
	private double movementX = 0;
	private double movementY = 0;
	private boolean isJumping = false;
	private boolean isDigging = false;
	
	public PlayerVirtualController(Camera camera) {
		this.camera = camera;
	}

	public double getMovementX() {
		return movementX;
	}

	public double getMovementY() {
		return movementY;
	}

	public boolean isJumping() {
		return isJumping;
	}

	public boolean isDigging() {
		return isDigging;
	}
	
	@Override
	public void keyDown(int keycode) {
		if (keycode == Keybindings.FORWARD)           movementX += 1;
		else if (keycode == Keybindings.LEFT)         movementY += 1;
		else if (keycode == Keybindings.BACKWARD)     movementX += -1;
		else if (keycode == Keybindings.RIGHT)        movementY += -1;
		else if (keycode == Keybindings.JUMP)         isJumping = true;
		else if (keycode == Keybindings.INV_TOGGLE)   RendererGUI.instance().toggleMenu();
		else if (keycode == Keybindings.INV_FORWARD)  RendererGUI.instance().scrollItems(1);
		else if (keycode == Keybindings.INV_BACKWARD) RendererGUI.instance().scrollItems(-1);
	}
	
	@Override
	public void keyUp(int keycode) {
		if (keycode == Keybindings.FORWARD)       movementX -= 1;
		else if (keycode == Keybindings.LEFT)     movementY -= 1;
		else if (keycode == Keybindings.BACKWARD) movementX -= -1;
		else if (keycode == Keybindings.RIGHT)    movementY -= -1;
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
		final float deltaX = (float)(startX - screenX) / Gdx.graphics.getWidth();
		final float deltaY = (float)(startY - screenY) / Gdx.graphics.getHeight();
		startX = screenX;
		startY = screenY;
		process(deltaX, deltaY);
	}
	
	private void process(float deltaX, float deltaY) {
		tmpV1.set(camera.direction).crs(camera.up).y = 0f;
		camera.rotateAround(target, tmpV1.nor(), deltaY * rotateAngle);
		camera.rotateAround(target, Vector3.Y, deltaX * -rotateAngle);
		camera.update();
	}

	@Override
	public void scrolled(int amount) {
		RendererGUI.instance().scrollItems(amount);
	}
}
