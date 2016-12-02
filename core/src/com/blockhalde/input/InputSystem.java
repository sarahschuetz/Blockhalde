package com.blockhalde.input;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.blockhalde.render.CameraSystem;

public class InputSystem extends EntitySystem {
	
	private InputProcessor inputProcessor;
	private VirtualController cameraController;

	@Override
	public void addedToEngine(Engine engine) {
		CameraSystem c = engine.getSystem(CameraSystem.class);
		cameraController = new PlayerVirtualController(c.getCam());
		inputProcessor = new PhysicalInputProcessor(cameraController);
		Gdx.input.setInputProcessor(inputProcessor);
		
		Pixmap pm = new Pixmap(16, 16, Format.RGBA8888); 
		Pixmap.setBlending(Pixmap.Blending.None); 
		pm.setColor(0f,0f,0f,0f);
		pm.fillRectangle(0, 0, 16, 16);
		Gdx.input.setCursorImage(pm, 0, 0);
		pm.dispose();
		
		//CursorCatched would allow the cursor to move over an unfocused window without disappearing.
		//But it seems to cause rendering errors on some systems...
		//Gdx.input.setCursorCatched(true);
	}

	@Override
	public void update(float deltaTime) {
		cameraController.update(deltaTime);
	}

	public void resize(int width, int height) {
		cameraController.resize(width, height);
	}
}
