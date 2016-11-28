package com.blockhalde.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.blockhalde.Blockhalde;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 600;
		config.addIcon("util/icon_128.png", FileType.Internal);
		config.addIcon("util/icon_32.png", FileType.Internal);
		new LwjglApplication(new Blockhalde(), config);
	}
}
