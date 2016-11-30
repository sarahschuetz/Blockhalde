package com.render;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.blockhalde.input.PlayerVirtualController;
import com.blockhalde.input.VirtualController;

public class CameraSystem extends EntitySystem {
	
	public PerspectiveCamera cam;
	private VirtualController camVC;

	@Override
	public void addedToEngine(Engine engine) {
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0f, 0f, 50f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();
		camVC = new PlayerVirtualController(cam);
	}

	@Override
	public void update(float deltaTime) {
		camVC.update();
	}

}
