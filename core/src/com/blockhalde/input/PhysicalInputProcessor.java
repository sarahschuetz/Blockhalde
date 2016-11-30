package com.blockhalde.input;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.InputProcessor;

public class PhysicalInputProcessor implements InputProcessor {
	private List<VirtualController> controllers = new ArrayList<VirtualController>();
	
	public PhysicalInputProcessor(VirtualController... controllers) {
		for (VirtualController virtualController : controllers) {
			this.controllers.add(virtualController);
		}
	}
	
	public PhysicalInputProcessor(List<VirtualController> controllers) {
		this.controllers = controllers;
	}

	public List<VirtualController> getControllers() {
		return controllers;
	}

	@Override
	public boolean keyDown(int keycode) {
		for (VirtualController virtualController : controllers) {
			virtualController.keyDown(keycode);
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		for (VirtualController virtualController : controllers) {
			virtualController.keyUp(keycode);
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		for (VirtualController virtualController : controllers) {
			virtualController.touchDown(screenX, screenY, button);
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		for (VirtualController virtualController : controllers) {
			virtualController.touchUp(screenX, screenY, button);
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		for (VirtualController virtualController : controllers) {
			virtualController.mouseMoved(screenX, screenY);
		}
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		for (VirtualController virtualController : controllers) {
			virtualController.scrolled(amount);
		}
		return true;
	}
}
