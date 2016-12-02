package com.blockhalde.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;

public class PauseListener implements LifecycleListener {
	private static PauseListener instance;
	private boolean paused = false;

	/**
	 * Binds the listener to the current application. 
	 * Must be called for the class to be functional.
	 */
	public static void init() {
		if (instance == null) {
			instance = new PauseListener();
			Gdx.app.addLifecycleListener(instance);
		}
	}

	/**
	 * Returns true if the application is currently paused.
	 * Returns false if the application is currently not paused 
	 * or if the listener is not initialized.
	 */
	public static boolean isPaused() {
		return (instance != null && instance.paused);
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
