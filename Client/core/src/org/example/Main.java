package org.example;

import java.io.IOException;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;


public class Main {
    public static void main(String[] args) throws IOException {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("ShrexStrikes");
        config.setWindowedMode(1000, 900);
        new Lwjgl3Application(new MyGame(), config);
    }
}
