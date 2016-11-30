package com.blockhalde.input;

public interface VirtualController {
	public void keyDown(int keycode);
	public void keyUp(int keycode);
	public void touchDown(int screenX, int screenY, int button);
	public void touchUp(int screenX, int screenY, int button);
	public void mouseMoved(int screenX, int screenY); 
	public void scrolled(int amount);
}
