package com.blockhalde.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.blockhalde.gui.RendererGUI;
import com.blockhalde.gui.grid.GridSystem;
import com.blockhalde.gui.pie.Command;
import com.blockhalde.gui.pie.PieMenuSystem;
import com.blockhalde.player.CameraComponent;
import com.blockhalde.player.DebugComponent;
import com.blockhalde.player.PlayerDataComponent;
import com.util.noise.debug.DebugPerlinNoiseSystem;

/**
 * An implementation of the {@link VirtualController} interface that controls the game interaction.
 * @author shaendro
 */
public class VirtualGameController extends VirtualAbstractController {
	private boolean active = true;
	private Keybindings keybindings = new Keybindings("util/keybindings.properties");

	/**
	 * Creates a {@link VirtualGameController}.
	 * @param inputSystem The {@link InputSystem} the controller belongs to
	 */
	public VirtualGameController(InputSystem inputSystem) {
		super(inputSystem);
		
		// add menu point to toggle gravity
		PieMenuSystem pms = inputSystem.getEngine().getSystem(PieMenuSystem.class);
		if(pms != null){
			pms.addPieSlice("Gravity", new Command(){
				public void execute() {
					toggleFlying();
				}
			});
		}
	}

	@Override
	public void keyDown(int keycode) {
		if (active) {
			if (keycode == keybindings.getKey("INV_TOGGLE"))              inputSystem.getEngine().getSystem(GridSystem.class).toggleMenu();
			else if (keycode == keybindings.getKey("INV_FORWARD"))        inputSystem.getEngine().getSystem(GridSystem.class).scrollItems(1);
			else if (keycode == keybindings.getKey("INV_BACKWARD"))       inputSystem.getEngine().getSystem(GridSystem.class).scrollItems(-1);
			else if (keycode == keybindings.getKey("QUIT"))		          Gdx.app.exit();
			else if (keycode == keybindings.getKey("TOGGLE_FLYING"))      toggleFlying();
			else if (keycode == keybindings.getKey("NOISE_DEBUG"))        inputSystem.getEngine().getSystem(DebugPerlinNoiseSystem.class).toggleDebugView();
			else if (keycode == keybindings.getKey("NOISE_DEBUG_Y_UP"))   inputSystem.getEngine().getSystem(DebugPerlinNoiseSystem.class).incrementNoiseY();
			else if (keycode == keybindings.getKey("NOISE_DEBUG_Y_DOWN")) inputSystem.getEngine().getSystem(DebugPerlinNoiseSystem.class).decrementNoiseY();
			else if (keycode == keybindings.getKey("QUIT"))		          Gdx.app.exit();
		}
	}


	@Override
	public void scrolled(int amount) {
		inputSystem.getEngine().getSystem(GridSystem.class).scrollItems(amount);
	}

	private void toggleFlying() {
		ImmutableArray<Entity> query = inputSystem.getEngine().getEntitiesFor(Family.all(PlayerDataComponent.class, CameraComponent.class).get());
		if (query.size() != 0) {
			Entity player = query.first();
			DebugComponent debugComponent = player.getComponent(DebugComponent.class);
			debugComponent.setFlying(!debugComponent.isFlying());
		}
	}
	
	@Override
	public void touchDown(int screenX, int screenY, int button) {
		if(button == 1){
			if (!inputSystem.getEngine().getSystem(GridSystem.class).inventoryIsVisible) {
				PieMenuSystem pms = inputSystem.getEngine().getSystem(PieMenuSystem.class);
				if(pms != null){
					pms.setActive(true);
					VirtualController vpcc = inputSystem.getController(VirtualPlayerCameraController.class);
					if (vpcc != null) vpcc.setActive(false);
				}
			}
		}
	}

	@Override
	public void touchUp(int screenX, int screenY, int button) {
		if(button == 1){
			if (inputSystem.getEngine().getSystem(GridSystem.class).inventoryIsVisible) {
				inputSystem.getEngine().getSystem(GridSystem.class).toggleMenu();
			} else {
				PieMenuSystem pms = inputSystem.getEngine().getSystem(PieMenuSystem.class);
				if(pms != null){
					pms.setActive(false);
					VirtualController vpcc = inputSystem.getController(VirtualPlayerCameraController.class);
					if (vpcc != null) vpcc.setActive(true);
				}
			}
		}
	}

	@Override
	public void touchDragged(int screenX, int screenY) {
		PieMenuSystem pms = inputSystem.getEngine().getSystem(PieMenuSystem.class);
		if(pms != null){
			pms.touchDragged(screenX, screenY);
		}
	}
}