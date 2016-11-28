package com.blockhalde.input;

import com.badlogic.gdx.InputProcessor;

public class PlayerInputProcessor implements InputProcessor {
	@Override
	public boolean keyDown(int keycode) {
		//TODO: Report input to engine, let it decide which VirtualController to call
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		//TODO: Report input to engine, let it decide which VirtualController to call
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		//TODO: Report input to engine, let it decide which VirtualController to call
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		//TODO: Report input to engine, let it decide which VirtualController to call
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//TODO: Report input to engine, let it decide which VirtualController to call
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//TODO: Report input to engine, let it decide which VirtualController to call
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		//TODO: Report input to engine, let it decide which VirtualController to call
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		//TODO: Report input to engine, let it decide which VirtualController to call
		return true;
	}
}
