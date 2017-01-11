package com.blockhalde.gui.grid;

import java.nio.ByteBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ScreenUtils;
import com.blockhalde.gui.RendererGUI;

public class BlurActor extends Actor {
	
	Texture blurredBackground;
	/**
	 * Is set to true once blurredBackground contains a meaningful value.
	 */
	private boolean bgInitialized = false;
	
	public BlurActor() {
		Pixmap orig = getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Pixmap blurred = BlurUtils.blur(orig, 4, 2, true);
		blurredBackground = new Texture(blurred);
		blurred.dispose();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(bgInitialized) {
			batch.draw(blurredBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, true);
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (RendererGUI.instance().menusActive == 1 && visible) {
    		Pixmap orig = getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
			Pixmap blurred = BlurUtils.blur(orig, 4, 2, false);
			blurredBackground = new Texture(blurred);
			blurred.dispose();
			
			super.setVisible(visible);
			
			if(visible) {
				bgInitialized = true;
			}
    	}
		
		if (RendererGUI.instance().menusActive == 0 && !visible) {
			super.setVisible(visible);
		}

	}
	

	private static Pixmap getScreenshot(int x, int y, int w, int h, boolean yDown){
		
        final Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);

        if (yDown) {
            ByteBuffer pixels = pixmap.getPixels();
            int numBytes = w * h * 4;
            byte[] lines = new byte[numBytes];
            int numBytesPerLine = w * 4;
            for (int i = 0; i < h; i++) {
                pixels.position((h - i - 1) * numBytesPerLine);
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
            }
            pixels.clear();
            pixels.put(lines);
        }

        return pixmap;
    }

}


