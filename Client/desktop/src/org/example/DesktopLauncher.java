package org.example;


import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.example.Shrexgame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher  {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 8);
		config.setTitle("Shrex");
		config.setWindowIcon(Files.FileType.Classpath,"shrekimage.png");
		config.setWindowedMode(1000, 900);
		new Lwjgl3Application(new MyGame(), config);
	}
}
