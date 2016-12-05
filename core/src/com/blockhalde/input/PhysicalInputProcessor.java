package com.blockhalde.input;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.InputProcessor;

/**
 * The general {@link InputProcessor} for the application.
 * Relays the commands put in by the user to a list of {@link VirtualController}s
 * @author shaendro
 */
public class PhysicalInputProcessor implements InputProcessor {
	private List<VirtualController> controllers = new ArrayList<VirtualController>();
	
	/**
	 * Creates the {@link PhysicalInputProcessor} with the array of {@link VirtualController}s.
	 * Enables very simple creation of the object via PhysicalInputProcessor(vc1, vc2).
	 * @param controllers An array of {@link VirtualController}s to be added to the {@link PhysicalInputProcessor}
	 */
	public PhysicalInputProcessor(VirtualController... controllers) {
		for (VirtualController virtualController : controllers) {
			this.controllers.add(virtualController);
		}
	}
	
	/**
	 * Alternative creation of the {@link PhysicalInputProcessor} with a list of {@link VirtualController}s.
	 * @param controllers A {@link List} of {@link VirtualController}s
	 */
	public PhysicalInputProcessor(List<VirtualController> controllers) {
		this.controllers = controllers;
	}

	/**
	 * @return The stored {@link List} of {@link VirtualController}s
	 */
	public List<VirtualController> getControllers() {
		return controllers;
	}

	@Override
	public boolean keyDown(int keycode) {
		for (VirtualController virtualController : controllers) {
			virtualController.keyDown(keycode);
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		for (VirtualController virtualController : controllers) {
			virtualController.keyUp(keycode);
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		for (VirtualController virtualController : controllers) {
			virtualController.touchDown(screenX, screenY, button);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		for (VirtualController virtualController : controllers) {
			virtualController.touchUp(screenX, screenY, button);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		for (VirtualController virtualController : controllers) {
			virtualController.mouseMoved(screenX, screenY);
		}
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		for (VirtualController virtualController : controllers) {
			virtualController.scrolled(amount);
		}
		return false;
	}
}
