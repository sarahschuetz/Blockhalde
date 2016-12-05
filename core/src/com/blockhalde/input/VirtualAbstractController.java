package com.blockhalde.input;

/**
 * An abstract implementation of the {@link VirtualController} interface that serves as a base for {@link VirtualController}s.
 * @author shaendro
 */
public abstract class VirtualAbstractController implements VirtualController {
	protected InputSystem inputSystem;
	protected boolean active = true;

	/**
	 * Creates a {@link VirtualAbstractController}.
	 * @param inputSystem The {@link InputSystem} the controller belongs to
	 */
	public VirtualAbstractController(InputSystem inputSystem) {
		this.inputSystem = inputSystem;
	}

	@Override
	public void keyDown(int keycode) {
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
	public void touchDragged(int screenX, int screenY) {
	}

	@Override
	public void mouseMoved(int screenX, int screenY) {
	}

	@Override
	public void scrolled(int amount) {
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
