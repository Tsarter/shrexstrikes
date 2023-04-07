package org.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.esotericsoftware.kryonet.Client;
import org.example.screens.LoadingScreen;
import org.example.screens.LobbyScreen;
import org.example.screens.MenuScreen;
import org.example.screens.ShrexScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyGame extends Game {
    public enum GameType {
        ONEvONE,
        TESTING,
        TWOvTWO,
        ZOMBIES,

    }
    public GameType gameType;
    public enum GameState {
        MENU,
        LOADING,
        LOBBY,
        GAME
    }
    public GameState gameState;
    private GameClient gameClient;
    private MenuScreen menuScreen;
    public ShrexScreen shrexScreen;
    private LoadingScreen loadingScreen;
    public LobbyScreen lobbyScreen;
    private AssetManager assetManager;
    private Player[] playersList;
    private Player player;
    private Client client;
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public Player[] getPlayersList() {
        return playersList;
    }
    public void setPlayersList(Player[] playersList) {
        this.playersList = playersList;
    }
    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public AssetManager getAssetManager() {
        return assetManager;
    }

    @Override
    public void create() {
        assetManager = new AssetManager();
        menuScreen = new MenuScreen(this);
        loadingScreen = new LoadingScreen(this);
        lobbyScreen = new LobbyScreen(this);
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
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    // All assets have been loaded, show the shrexScreen
                    if (gameState != GameState.GAME) {
                        shrexScreen.create();
                        setScreen(shrexScreen);
                        gameState = GameState.GAME;
                    }
                }
            });
        } else {
            // Assets are still loading, show a loading screen or progress bar
            setScreen(loadingScreen);
        }
    }

    public void showLobbyScreen() {
        gameClient = new GameClient(this, "localhost", 8080, 8081);
        setScreen(lobbyScreen);
        gameState = GameState.LOBBY;

    }
    public void leaveLobby() {
        client.close();
        showMenuScreen();
    }
    public Player[] getPlayers() {
        return playersList;
    }
    @Override
    public void render() {
        super.render();
    }
}

