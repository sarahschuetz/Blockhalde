package com.blockhalde.input;

import com.badlogic.gdx.Gdx;
import com.blockhalde.gui.RendererGUI;

/**
 * An implementation of the {@link VirtualController} interface that controls the game.
 * @author shaendro
 */
public class VirtualGameController implements VirtualController {
	private InputSystem inputSystem;
	private boolean active = true;
	private Keybindings keybindings = new Keybindings("util/keybindings.properties");

	/**
	 * Creates a {@link VirtualGameController} and attaches the given camera to it.
	 * @param camera A {@link Camera} for the {@link VirtualGameController} to move around
	 */
	public VirtualGameController(InputSystem inputSystem) {
		this.inputSystem = inputSystem;
	}

	@Override
	public void keyDown(int keycode) {
		if (active) {
			if (keycode == keybindings.getKey("INV_TOGGLE"))        RendererGUI.instance().toggleMenu();
			else if (keycode == keybindings.getKey("INV_FORWARD"))  RendererGUI.instance().scrollItems(1);
			else if (keycode == keybindings.getKey("INV_BACKWARD")) RendererGUI.instance().scrollItems(-1);
			else if (keycode == keybindings.getKey("QUIT"))		    Gdx.app.exit();
		}
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
	}

	@Override
	public void scrolled(int amount) {
		if (active) {
			RendererGUI.instance().scrollItems(amount);
		}
	}

	@Override
	public void update(float deltaTime) {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
}
