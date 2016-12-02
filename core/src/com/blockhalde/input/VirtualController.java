package com.blockhalde.input;

/**
 * Defines the methods a controller has to implement 
 * in order to be attached to the {@link PhysicalInputProcessor}
 * @author shaendro
 */
public interface VirtualController {
	/**
	 * Is called by the {@link PhysicalInputProcessor} when a key is pressed.
	 * @param keycode The keycode of the pressed key
	 */
	public void keyDown(int keycode);
	/**
	 * Is called by the {@link PhysicalInputProcessor} when a key is released.
	 * @param keycode The keycode of the released key
	 */
	public void keyUp(int keycode);
	/**
	 * Is called by the {@link PhysicalInputProcessor} when a mouse button is released.
	 * @param screenX The x coordinate of the click
	 * @param screenY The y coordinate of the click
	 * @param button The keycode of the pressed mouse button
	 */
	public void touchDown(int screenX, int screenY, int button);
	/**
	 * Is called by the {@link PhysicalInputProcessor} when a mouse button is released.
	 * @param screenX The x coordinate of the release
	 * @param screenY The y coordinate of the release
	 * @param button The keycode of the released mouse button
	 */
	public void touchUp(int screenX, int screenY, int button);
	/**
	 * Is called by the {@link PhysicalInputProcessor} when the mouse moves.
	 * @param screenX The new x coordinate of the mouse
	 * @param screenY The new y coordinate of the mouse
	 */
	public void mouseMoved(int screenX, int screenY);
	/**
	 * Is called by the {@link PhysicalInputProcessor} when the scroll wheel is used.
	 * @param amount The amount of ticks the wheel was moved
	 */
	public void scrolled(int amount);
	
	/**
	 * Is called by the engine each update cycle.
	 * @param deltaTime The time between this and the last update
	 */
	public void update(float deltaTime);
	/**
	 * Is called by the application on resize.
	 * @param width The new width of the window
	 * @param height The new height of the window
	 */
	public void resize(int width, int height);
}
