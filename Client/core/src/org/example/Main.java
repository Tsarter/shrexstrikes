package org.example;

import java.io.IOException;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;


public class Main {
    public static void main(String[] args) throws IOException {
        //new MyClient();
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("My Game");
        config.setWindowedMode(640, 480);
        new Lwjgl3Application(new testMyClient(), config);
    }
}
