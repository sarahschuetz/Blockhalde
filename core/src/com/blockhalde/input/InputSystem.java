package com.blockhalde.input;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.blockhalde.render.CameraSystem;

/**
 * An {@link EntitySystem} that handles user input.
 * @author shaendro
 */
public class InputSystem extends EntitySystem {
	private PhysicalInputProcessor inputProcessor;
	private VirtualController gameController;
	private VirtualController cameraController;
	private VirtualController movementController;
	private boolean cursorVisible = true;

	@Override
	public void addedToEngine(Engine engine) {
		CameraSystem cameraSystem = engine.getSystem(CameraSystem.class);
		gameController = new VirtualGameController(this);
		cameraController = new VirtualPlayerCameraController(this, cameraSystem.getCam());
		movementController = new VirtualPlayerMovementController(this, cameraSystem.getCam());
		inputProcessor = new PhysicalInputProcessor(gameController, cameraController, movementController);
		Gdx.input.setInputProcessor(inputProcessor);
		toggleCursor();
		Gdx.input.setCursorCatched(true);
	}

	@Override
	public void update(float deltaTime) {
		for (VirtualController controller : inputProcessor.getControllers()) {
			controller.update(deltaTime);
		}
	}

	/**
	 * Is called by the application on resize.
	 * Relays the command to the {@link VirtualController}.
	 * @param width The new width of the window
	 * @param height The new height of the window
	 */
	public void resize(int width, int height) {
		for (VirtualController controller : inputProcessor.getControllers()) {
			controller.resize(width, height);
		}
	}
	
	public void toggleCursor() {
		if (cursorVisible) {
			Pixmap pm = new Pixmap(16, 16, Format.RGBA8888); 
			Pixmap.setBlending(Pixmap.Blending.None); 
			pm.setColor(0f,0f,0f,0f);
			pm.fillRectangle(0, 0, 16, 16);
			Gdx.input.setCursorImage(pm, 0, 0);
			pm.dispose();
			
			//CursorCatched would allow the cursor to move over an unfocused window without disappearing.
			//But it seems to cause rendering errors on some systems...
			//Gdx.input.setCursorCatched(true);
		} else {
			Gdx.input.setCursorImage(null, 0, 0);
		}
	}
}
