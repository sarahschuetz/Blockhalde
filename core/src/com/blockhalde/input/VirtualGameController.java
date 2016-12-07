package com.blockhalde.input;

import com.badlogic.gdx.Gdx;
import com.blockhalde.gui.RendererGUI;
import com.util.noise.debug.DebugPerlinNoiseSystem;

/**
 * An implementation of the {@link VirtualController} interface that controls the game interaction.
 * @author shaendro
 */
public class VirtualGameController extends VirtualAbstractController {
	private Keybindings keybindings = new Keybindings("util/keybindings.properties");

	/**
	 * Creates a {@link VirtualGameController}.
	 * @param inputSystem The {@link InputSystem} the controller belongs to
	 */
	public VirtualGameController(InputSystem inputSystem) {
		super(inputSystem);
	}

	@Override
	public void keyDown(int keycode) {
		if (active) {
			if (keycode == keybindings.getKey("INV_TOGGLE"))        RendererGUI.instance().toggleMenu();
			else if (keycode == keybindings.getKey("INV_FORWARD"))  RendererGUI.instance().scrollItems(1);
			else if (keycode == keybindings.getKey("INV_BACKWARD")) RendererGUI.instance().scrollItems(-1);
			else if (keycode == keybindings.getKey("NOISE_DEBUG"))  inputSystem.getEngine().getSystem(DebugPerlinNoiseSystem.class).toggleDebugView();
			else if (keycode == keybindings.getKey("NOISE_DEBUG_Y_UP"))  inputSystem.getEngine().getSystem(DebugPerlinNoiseSystem.class).incrementNoiseY();
			else if (keycode == keybindings.getKey("NOISE_DEBUG_Y_DOWN"))  inputSystem.getEngine().getSystem(DebugPerlinNoiseSystem.class).decrementNoiseY();
			else if (keycode == keybindings.getKey("QUIT"))		    Gdx.app.exit();
		}
	}

	@Override
	public void scrolled(int amount) {
		if (active) {
			RendererGUI.instance().scrollItems(amount);
		}
	}
}
