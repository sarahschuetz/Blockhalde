package com.blockhalde.input;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class Keybindings {
	private Map<String, Integer> keyMap = new HashMap<>();

	/**
	 * Creates a keybindings object that automatically reads 
	 * keybindings from the specified file.
	 */
	public Keybindings(String file) {
		StringReader stringReader = new StringReader(Gdx.files.internal(file).readString());
		try {
			Properties prop = new Properties();
			prop.load(stringReader);
			
			for (Entry<Object, Object> entry : prop.entrySet()) {
				keyMap.put((String)entry.getKey(), Keys.valueOf((String)entry.getValue()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stringReader.close();
		}
	}

	/**
	 * Returns the keycode for the specified command. 
	 * Returns -1 if the command is not recognized.
	 */
	public int getKey(String command) {
		Integer value = keyMap.get(command);
		return value != null ? value : -1;
	}
}
