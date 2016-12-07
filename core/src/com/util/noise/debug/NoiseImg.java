package com.util.noise.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.util.noise.PerlinNoise3D;

public class NoiseImg  extends Actor {
	
	private double width = Gdx.graphics.getWidth();
	private double height = Gdx.graphics.getHeight();
	private int z = 0;
	
	private final PerlinNoise3D perlin = new PerlinNoise3D();
	
	private Texture texture;
	
	public NoiseImg() {
		setVisible(false);
	}
	
	private void generateNoiseTexture() {
		Pixmap pixmap = new Pixmap(1260, 700, Format.RGBA8888);	
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				float perlinValue = (float) perlin.calcPerlinAt(x / width, y / height, z, 6, 12);
				pixmap.setColor(perlinValue, perlinValue, perlinValue, 1f);
				pixmap.drawPixel(x, y);
			}
		}
		
		texture = new Texture(pixmap);
		pixmap.dispose();
	}
	
	public void incrementZ() {
		z++;
		
	}
	
	public void decrementZ() {
		z--;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		
		generateNoiseTexture();
		
		batch.disableBlending();
	    batch.draw(texture, 10, 10);
	}
}
