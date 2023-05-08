package org.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
    public GameOverScreen gameOverScreen;
    private AssetManager assetManager;
    private GamePreferences gamePreferences;
    private HashMap<Integer, Player> players = new HashMap<>();
    private Player player;
    private Client client;
    public Music music;
    private Skin skin;
    private boolean showAds = true;
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
    public Skin getSkin() {
        return skin;
    }

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        player = new Player();
        gamePreferences = new GamePreferences();
        assetManager = new AssetManager();
        menuScreen = new MenuScreen(this);
        loadingScreen = new LoadingScreen(this);
        lobbyScreen = new LobbyScreen(this);
        deathScreen = new DeathScreen(this);
        pauseOverlay = new PauseOverlay(this);
        settingsScreen = new SettingsScreen(this);
        pvpRespawnScreen = new PVPRespawnScreen(this);
        gameOverScreen = new GameOverScreen(this);
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                // Asynchronously load the map
                assetManager.load("Shrek_Body.png", Texture.class);
                assetManager.load("Shrek_Head_Legs.png", Texture.class);
                assetManager.load("characters/Fiona/fiona.obj", Model.class);
                assetManager.load("Shrek.obj", Model.class);
                assetManager.load("characters/Shrek/Shrek.obj", Model.class);
                assetManager.load("maps/City/MediEvalCity.g3db", Model.class);
                assetManager.load("skyboxes/skyboxBasicBlue.png", Texture.class);
                assetManager.load("guns/sci-fi-gun/sci fi m254 gun - high poly.obj", Model.class);
                setScreen(menuScreen);
            }
        });
    }

    public void showMenuScreen() {
        if (client.isConnected()) {
            // If the client is connected, disconnect it
            client.close();
        }
        if (gameClient != null) {
            // If the gameClient is not null, set it to null
            gameClient = null;
        }
        if (gameMode == GameMode.GameModes.ZOMBIES) {
            // If the gameMode is zombies, hide all the enemies (zombies
            gameScreen.enemiesToHide.addAll(gameScreen.enemies.values());
        }
        if (gameScreen != null) {
            // If the gameScreen is not null, dispose it
            gameScreen.dispose();
            gameScreen = null;
        }
        gameState = GameStateChange.GameStates.IN_MENU;
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
                    if (gameScreen == null  || gameScreen instanceof PVPScreen) {
                        try {
                            gameScreen = new ZombiesScreen(MyGame.this);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (showAds){
                        setScreen(loadingScreen);
                        showAds = false;
                    }
                    else if (assetManager.update()) {
                        showAds = true;
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
                    }
                    else {
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
                gameState = GameStateChange.GameStates.IN_GAME;
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
                if (gameScreen == null || gameScreen instanceof ZombiesScreen) {
                    try {
                        gameScreen = new PVPScreen(MyGame.this,100);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (showAds){
                    setScreen(loadingScreen);
                    showAds = false;
                }
                else if (assetManager.update()) {
                    showAds = true;
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
    public void showGameOverScreen() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                setScreen(gameOverScreen);
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
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                setScreen(deathScreen);
            }
        });

    }
    public void leaveLobby() {
        client.close();
        showMenuScreen();
    }
    @Override
    public void render() {
        super.render();
    }
    public GameClient initGameClient() {
        // Server ip 193.40.156.227 / localhost
        return new GameClient(this, "localhost", 8080, 8081);
    }
    public GamePreferences getGamePreferences() {
        return gamePreferences;
    }
}

