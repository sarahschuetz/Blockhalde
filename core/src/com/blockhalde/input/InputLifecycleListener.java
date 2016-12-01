package com.blockhalde.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;

public class InputLifecycleListener implements LifecycleListener {
	private boolean paused = false;
	
	public boolean isPaused() {
		return paused;
	}

	public InputLifecycleListener(PlayerVirtualController pvc) {
		Gdx.app.addLifecycleListener(this);
	}
	
	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		paused = false;
	}

	@Override
	public void dispose() {
		Gdx.app.removeLifecycleListener(this);
	}
}
