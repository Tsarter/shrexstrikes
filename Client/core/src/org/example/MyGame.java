package org.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.esotericsoftware.kryonet.Client;
import org.example.messages.GameMode;
import org.example.messages.GameStateChange;
import org.example.screens.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyGame extends Game {

    public GameMode.GameModes gameMode;

    public GameStateChange.GameStates gameState;
    private GameClient gameClient;
    // Screens
    private MenuScreen menuScreen;
    public ShrexScreen shrexScreen;
    private LoadingScreen loadingScreen;
    public LobbyScreen lobbyScreen;
    public DeathScreen deathScreen;
    public PauseOverlay pauseOverlay; // also a screen, just called Overlay
    private AssetManager assetManager;
    private GamePreferences gamePreferences;
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
        gamePreferences = new GamePreferences();
        assetManager = new AssetManager();
        menuScreen = new MenuScreen(this);
        loadingScreen = new LoadingScreen(this);
        lobbyScreen = new LobbyScreen(this);
        deathScreen = new DeathScreen(this);
        pauseOverlay = new PauseOverlay(this);
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
                assetManager.load("assets/skyboxes/skyboxBasicBlue.png", Texture.class);
                setScreen(menuScreen);
            }
        });
    }

    public void showMenuScreen() {
        client.close();
        gameClient = null;
        shrexScreen.enemiesToHide.addAll(shrexScreen.enemies.values());
        setScreen(menuScreen);
    }
    public void showShrexScreen() {
        if (gameClient == null) {
            // If the gameClient is null, create it
            gameClient = initGameClient();
        }
        // Some thing with libgdx that I don't understand, everything needs to be on the main thread or smt.
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    if (assetManager.update()) {
                        // All assets have been loaded, show the shrexScreen
                        if (gameState != GameStateChange.GameStates.IN_GAME) {
                            if (shrexScreen.isCreated() == false) {
                                // To avoid 2x creation of the shrexScreen
                                shrexScreen.create();
                            }
                            setScreen(shrexScreen);
                            if (gameState == GameStateChange.GameStates.IN_PAUSE_MENU) {
                                // If the game was paused, unpause it
                                shrexScreen.resume();
                            }
                            gameState = GameStateChange.GameStates.IN_GAME;
                            client.sendTCP(new GameStateChange(client.getID(), GameStateChange.GameStates.IN_GAME));

                        }
                    } else {
                        // Assets are still loading, show a loading screen or progress bar
                        setScreen(loadingScreen);
                    }
                }
            });

    }

    public void showLobbyScreen() {
        gameClient = initGameClient();
        setScreen(lobbyScreen);
        gameState = GameStateChange.GameStates.IN_LOBBY;

    }
    public void showPauseOverlay() {
        client.sendTCP(new GameStateChange(client.getID(), GameStateChange.GameStates.IN_PAUSE_MENU));
        setScreen(pauseOverlay);
    }
    public void showDeathScreen() {
        setScreen(deathScreen);
    }
    public void leaveLobby() {
        client.close();
        showMenuScreen();
    }
    public void leaveGame() {
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
    public GameClient initGameClient() {
        return new GameClient(this, "localhost", 8080, 8081);
    }
    public GamePreferences getGamePreferences() {
        return gamePreferences;
    }
}

