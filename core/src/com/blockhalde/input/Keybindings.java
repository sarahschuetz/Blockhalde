package com.blockhalde.input;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

/**
 * A glorified {@link Map} that reads keybindings from a .properties file 
 * and offers them via a get-method.
 * @author shaendro
 */
public class Keybindings {
	private Map<String, Integer> keyMap = new HashMap<>();

	/**
	 * Creates a keybindings object that automatically reads 
	 * keybindings from the specified file.
	 * @param file Internal path to the file
	 */
	public Keybindings(String file) {
		StringReader stringReader = new StringReader(Gdx.files.internal(file).readString());
		try {
			Properties prop = new Properties();
			prop.load(stringReader);
			
			for (Entry<Object, Object> entry : prop.entrySet()) {
				int keycode = Keys.valueOf((String)entry.getValue());
				if (keycode == -1) Gdx.app.error("KEYBINDINGS", "The keybinding \"" + entry.getKey() + " = " + entry.getValue() + "\" was not recognized.");
				else keyMap.put((String)entry.getKey(), keycode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stringReader.close();
		}
	}

	/**
	 * @param command The name of the keybinding
	 * @return The keycode for the specified command
	 * or -1 if the command is not recognized.
	 */
	public int getKey(String command) {
		Integer value = keyMap.get(command);
		return value != null ? value : -1;
	}
}
