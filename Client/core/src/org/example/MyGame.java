package org.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.esotericsoftware.kryonet.Client;
import org.example.messages.GameMode;
import org.example.messages.GameStateChange;
import org.example.screens.*;
import org.example.screens.gameModes.GameScreen;
import org.example.screens.gameModes.PVPScreen;
import org.example.screens.gameModes.ZombiesScreen;

import java.io.IOException;
import java.util.HashMap;

public class MyGame extends Game {

    public GameMode.GameModes gameMode;

    public GameStateChange.GameStates gameState;
    private GameClient gameClient;
    // Screens
    private MenuScreen menuScreen;
    public GameScreen gameScreen; // Can be ZombiesScreen or PVPScreen
    private LoadingScreen loadingScreen;
    public LobbyScreen lobbyScreen;
    public DeathScreen deathScreen;
    public PauseOverlay pauseOverlay; // also a screen, just called Overlay
    public SettingsScreen settingsScreen;
    public PVPRespawnScreen pvpRespawnScreen;
    private AssetManager assetManager;
    private GamePreferences gamePreferences;
    private HashMap<Integer, Player> players = new HashMap<>();
    private Player player;
    private Client client;
    public Music music;
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public HashMap<Integer, Player> getPlayers() {
        return players;
    }
    public void setPlayers(HashMap<Integer, Player> players) {
        this.players = players;
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
        settingsScreen = new SettingsScreen(this);
        pvpRespawnScreen = new PVPRespawnScreen(this);
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
        if (client != null) {
            // If the gameClient is not null, close it
            client.close();
            gameClient = null;
        }
        if (gameMode == GameMode.GameModes.ZOMBIES) {
            // If the gameMode is zombies, hide all the enemies (zombies
            gameScreen.enemiesToHide.addAll(gameScreen.enemies.values());
        }
        setScreen(menuScreen);
    }
    public void showZombiesScreen() {
        if (gameClient == null) {
            // If the gameClient is null, create it
            gameClient = initGameClient();
        }
        // Some thing with libgdx that I don't understand, everything needs to be on the main thread or smt.
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    if (assetManager.update()) {
                        // All assets have been loaded, show the gameScreen
                        if (gameState != GameStateChange.GameStates.IN_GAME) {
                            if (gameScreen.isCreated() == false) {
                                // To avoid 2x creation of the gameScreen
                                gameScreen.create();
                            }
                            setScreen(gameScreen);
                            if (gameState == GameStateChange.GameStates.IN_PAUSE_MENU) {
                                // If the game was paused, unpause it
                                gameScreen.resume();
                            }
                            gameState = GameStateChange.GameStates.IN_GAME;
                            client.sendTCP(new GameStateChange(client.getID(), GameStateChange.GameStates.IN_GAME));

                        }
                    } else if (gameScreen == null) {
                        try {
                            gameScreen = new ZombiesScreen(MyGame.this);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        // Assets are still loading, show a loading screen
                        setScreen(loadingScreen);
                    }
                }
            });

    }
    public void showPVPScreen() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                setScreen(gameScreen);
            }
        });
    }
    public void showPVPLobbyScreen() {
        if (gameClient == null) {
            // If the gameClient is null, create it
            gameClient = initGameClient();
        }
        // Some thing with libgdx that I don't understand, everything needs to be on the main thread or smt.
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                if (gameScreen == null) {
                    try {
                        gameScreen = new PVPScreen(MyGame.this,100);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                } else if (assetManager.update()) {
                    // All assets have been loaded, notify the server that the client is ready
                    if (gameScreen.isCreated() == false) {
                        // To avoid 2x creation of the pvpScreen
                        gameScreen.create();
                    }
                    setScreen(lobbyScreen);
                    gameState = GameStateChange.GameStates.READY;
                    client.sendTCP(new GameStateChange(client.getID(), GameStateChange.GameStates.READY));

                } else {
                    // Assets are still loading, show a loading screen
                    setScreen(loadingScreen);
                }

            }
        });

    }
    public void showPVPRespawnScreen() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                setScreen(pvpRespawnScreen);
            }
        });
    }
    public void showSettingsScreen() {
        setScreen(settingsScreen);
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

