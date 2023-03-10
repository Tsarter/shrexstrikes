package org.example;

import com.badlogic.gdx.Game;
import org.example.screens.MenuScreen;
import org.example.screens.ShrexScreen;

public class MyGame extends Game {
    private MenuScreen menuScreen;
    private ShrexScreen shrexScreen;

    @Override
    public void create() {
        menuScreen = new MenuScreen(this);
        shrexScreen = new ShrexScreen(this);

        setScreen(menuScreen);
    }

    public void showMenuScreen() {
        setScreen(menuScreen);
    }
    public void showShrexScreen() {
        setScreen(shrexScreen);
    }

    // Other methods as needed

    // ...
}

