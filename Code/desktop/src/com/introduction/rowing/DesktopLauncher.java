package com.introduction.rowing;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Rowing");
		config.setWindowedMode(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		new Lwjgl3Application(new MyRowing(), config);
	}
}