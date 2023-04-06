package org.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import org.example.screens.LoadingScreen;
import org.example.screens.MenuScreen;
import org.example.screens.ShrexScreen;

import java.io.IOException;

public class MyGame extends Game {
    private MenuScreen menuScreen;
    private ShrexScreen shrexScreen;
    private LoadingScreen loadingScreen;
    private AssetManager assetManager;
    private boolean initialized = false;
    @Override
    public void create() {
        assetManager = new AssetManager();
        menuScreen = new MenuScreen(this);
        loadingScreen = new LoadingScreen(this);
        try {
            shrexScreen = new ShrexScreen(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                // Asynchronously load the map
                assetManager.load("assets/Shrek_Body.png", Texture.class);
                assetManager.load("assets/Shrek_Head_Legs.png", Texture.class);
                assetManager.load("assets/characters/Fiona/fiona.obj", Model.class);
                assetManager.load("assets/Shrek.obj", Model.class);
                assetManager.load("assets/characters/Shrek/Shrek.obj", Model.class);
                assetManager.load("assets/maps/City/MediEvalCity.g3db", Model.class);
                setScreen(menuScreen);
            }
        });
    }

    public void showMenuScreen() {
        setScreen(menuScreen);
    }
    public void showShrexScreen() {
        if (assetManager.update()) {
            // All assets have been loaded, show the shrexScreen
            shrexScreen.create();
            setScreen(shrexScreen);
        } else {
            // Assets are still loading, show a loading screen or progress bar
            setScreen(loadingScreen);
        }
    }

    // Other methods as needed

    // ...
    @Override
    public void render() {
        super.render();
    }
    public AssetManager getAssetManager() {
        return assetManager;
    }
}

