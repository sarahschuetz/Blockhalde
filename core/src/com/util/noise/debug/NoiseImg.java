package com.util.noise.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.terrain.chunk.Chunk;
import com.terrain.generators.PerlinTerrainGenerator;
import com.util.noise.PerlinNoise3D;

public class NoiseImg extends Actor {
	
	private double width = Gdx.graphics.getWidth();
	private double height = Gdx.graphics.getHeight();
	private int y = 0;
	
	private PerlinNoise3D perlin;
	
	private Texture textureScreen;
	private Texture textureChunk;
	
	private double smoothness;
	private int octaves;
	private double persistence;
	private double frequency;
	
	public NoiseImg(PerlinTerrainGenerator generator) {
		setVisible(false);
		perlin = generator.getPerlinNoise();
		this.octaves = generator.getOctaves();
		this.persistence = generator.getPersistence();
		this.frequency = generator.getFrequency();
		this.smoothness = generator.getSmoothness();
		
		generateNoiseTextures();
	}
	
	private void generateNoiseTextures() {
		
		Pixmap pixmapScreen = new Pixmap(1260, 700, Format.RGBA8888);	
		
		for(int x = 0; x < width; x++) {
			for(int z = 0; z < height; z++) {
//				float perlinValue = (float) perlin.calcPerlinAt(x / smoothness, y, z / smoothness, octaves, persistence);
				
				double heightMap = perlin.calcPerlinAt(x / smoothness, z / smoothness, y, 2, 1); // generate Heightmap from 2d perlin noise 
				double heightMap2 = perlin.calcPerlinAt(x / smoothness, z / smoothness, y, 3, 1); // generate Heightmap from 2d perlin noise 
				double heightMap3 = perlin.calcPerlinAt(x / smoothness, z / smoothness, y, 1, 1); // generate Heightmap from 2d perlin noise 
				
				heightMap = (heightMap + heightMap2) * 0.5;
				heightMap = (heightMap + heightMap3) * 0.5;
				
				pixmapScreen.setColor((float) heightMap, (float) heightMap, (float) heightMap, 1f);
				pixmapScreen.drawPixel(x, z);
			}
		}
		
//		Pixmap pixmapChunk = new Pixmap(16, 16, Format.RGBA8888);	
//		
//		for(int x = 0; x < Chunk.X_MAX; x++) {
//			for(int z2 = 0; z2 < Chunk.Z_MAX; z2++) {
//				
//				float perlinValue = (float) perlin.calcPerlinAt(
//						x  / (double) Chunk.X_MAX,
//						(y % Chunk.Y_MAX),
//						z2 / (double) Chunk.Z_MAX, octaves, persistence);
//				
//				pixmapChunk.setColor(perlinValue, perlinValue, perlinValue, 1f);
//				pixmapChunk.drawPixel(x, z2);
//			}
//		}
		
		textureScreen = new Texture(pixmapScreen);
		pixmapScreen.dispose();
		
//		textureChunk = new Texture(pixmapChunk);
//		pixmapChunk.dispose();
	}
	
	public void incrementY() {
		y++;
		generateNoiseTextures();
	}
	
	public void decrementY() {
		y--;
		generateNoiseTextures();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.disableBlending();
	    batch.draw(textureScreen, 10, 10);
//	    batch.draw(textureChunk, 10, 10, 160, 160);
	}
}
