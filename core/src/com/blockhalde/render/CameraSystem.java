package com.blockhalde.render;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;

public class CameraSystem extends EntitySystem {
	
	private PerspectiveCamera cam;

	@Override
	public void addedToEngine(Engine engine) {
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(-34f, 210f, 62f);
		cam.position.set(-34f, 210f, 60f);
		cam.near = 0.001f;
		cam.far = 300f;
		cam.update();
	}
	
	public void resize(int width, int height){
		cam.viewportWidth =  width;
		cam.viewportHeight = height;
		cam.update();
	}

	public PerspectiveCamera getCam() {
		return cam;
	}

}
