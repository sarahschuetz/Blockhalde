package com.blockhalde.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;

public class PauseListener implements LifecycleListener {
	private static PauseListener instance;
	private boolean paused = false;

	public static void init() {
		if (instance == null) {
			instance = new PauseListener();
			Gdx.app.addLifecycleListener(instance);
		}
	}

	public static boolean isPaused() {
		return instance.paused;
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
