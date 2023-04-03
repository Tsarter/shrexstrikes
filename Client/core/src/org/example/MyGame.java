package org.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import org.example.screens.MenuScreen;
import org.example.screens.ShrexScreen;

import java.io.IOException;

public class MyGame extends Game {
    private MenuScreen menuScreen;
    private ShrexScreen shrexScreen;

    @Override
    public void create() {
        menuScreen = new MenuScreen(this);
        try {
            shrexScreen = new ShrexScreen(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setScreen(menuScreen);


    }

    public void showMenuScreen() {
        setScreen(menuScreen);
    }
    public void showShrexScreen() {
        shrexScreen.create();
        setScreen(shrexScreen);
    }

    // Other methods as needed

    // ...
}

