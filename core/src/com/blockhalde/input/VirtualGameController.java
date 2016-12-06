package com.blockhalde.input;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.blockhalde.gui.RendererGUI;
import com.blockhalde.gui.pie.PieMenuSystem;

/**
 * An implementation of the {@link VirtualController} interface that controls the game interaction.
 * @author shaendro
 */
public class VirtualGameController extends VirtualAbstractController {
public class VirtualGameController implements VirtualController {
	private InputSystem inputSystem;
	private Engine engine;
	private boolean active = true;
	private Keybindings keybindings = new Keybindings("util/keybindings.properties");

	/**
	 * Creates a {@link VirtualGameController}.
	 * @param inputSystem The {@link InputSystem} the controller belongs to
	 */
	public VirtualGameController(InputSystem inputSystem, Engine engine) {
		this.inputSystem = inputSystem;
		this.engine = engine;
	public VirtualGameController(InputSystem inputSystem) {
		super(inputSystem);
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
			PieMenuSystem pms = engine.getSystem(PieMenuSystem.class);
			if(pms != null){
				pms.setActive(true);
			}
			
	}

	@Override
	public void touchUp(int screenX, int screenY, int button) {
		PieMenuSystem pms = engine.getSystem(PieMenuSystem.class);
		if(pms != null){
			pms.setActive(false);
		}
	}

	@Override
	public void mouseMoved(int screenX, int screenY) {
		PieMenuSystem pms = engine.getSystem(PieMenuSystem.class);
		if(pms != null){
			pms.mouseMoved(screenX, screenY);
		}
	}

	@Override
	public void scrolled(int amount) {
		if (active) {
			RendererGUI.instance().scrollItems(amount);
		}
	}
}
