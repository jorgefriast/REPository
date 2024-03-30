package com.introduction.rowing;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.introduction.rowing.MyRowing;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Rowing");
		config.setWindowedMode(1920, 1080);
		new Lwjgl3Application(new MyRowing(), config);
	}
}
