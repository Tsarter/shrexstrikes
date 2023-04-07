package org.example;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.example.animations.Pulse;
import org.example.messages.*;
import org.example.spawner.Enemy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class GameClient  {
    private String serverIp;
    private int serverTCPport;
    private int serverUDPport;
    private MyGame game;

    public GameClient(MyGame game,String serverIp, int serverTCPport, int serverUDPport) {
        this.serverIp = serverIp;
        this.serverTCPport = serverTCPport;
        this.serverUDPport = serverUDPport;
        this.game = game;


        try {
            game.setClient(new Client(50000, 50000));  // initialize client
            Network.register(game.getClient());  // register all the classes that are sent over the network
            // Add listener to tell the client, what to do after something is sent over the network
            game.getClient().addListener(new Listener() {

                /**
                 * We recieved something from the server.
                 * In this case, it is probably a list of all players.
                 */
                public void received(Connection connection, Object object) {
                    if (object instanceof Player[]) {
                        // get the list of players
                        game.setPlayersList((Player[]) object);
                        // update the lobby screen
                        if (game.gameState == MyGame.GameState.LOBBY) {
                            game.lobbyScreen.updateLobby();
                        }
                        if (game.gameType == MyGame.GameType.ONEvONE &&
                                game.gameState != MyGame.GameState.GAME) {
                            if (game.getPlayersList().length == 2) {
                                game.showShrexScreen();
                            }
                        }
                        if (game.gameType == MyGame.GameType.TWOvTWO &&
                                game.gameState != MyGame.GameState.GAME) {
                            if (game.getPlayersList().length == 4) {
                                game.showShrexScreen();
                            }
                        }
                        if (game.gameType == MyGame.GameType.ZOMBIES &&
                                game.gameState != MyGame.GameState.GAME) {
                            if (game.getPlayersList().length == 1) {
                                game.showShrexScreen();
                            }
                        }
                        if (game.gameType == MyGame.GameType.TESTING &&
                                game.gameState != MyGame.GameState.GAME) {
                            if (game.getPlayersList().length == 1) {
                                game.showShrexScreen();
                            }
                        }
                    }
                    // we recieved the server created player object
                    else if (object instanceof Player) {
                        game.setPlayer((Player) object);
                    }
                    // we recieved playerHit
                    else if (object instanceof PlayerHit) {
                        PlayerHit playerHit = (PlayerHit) object;
                        // if the player that was hit is the current player
                        game.shrexScreen.handleIncomingPlayerHit(playerHit);
                    }
                    else if (object instanceof Enemies) {
                        if (game.gameType == MyGame.GameType.ZOMBIES) {
                            Enemies enemiesInfo = (Enemies) object;
                            game.shrexScreen.handleIncomingEnemies(enemiesInfo);
                        }

                    }
                    else if (object instanceof EnemyHit) {
                        EnemyHit enemyHit = (EnemyHit) object;
                        game.shrexScreen.handleIncomingEnemyHit(enemyHit);
                    }
                    else if (object instanceof GameStarted) {
                        // gameStarted = true;
                    }
                    else if (object instanceof GameOver) {
                        // myGame.setScreen(new GameOverScreen(myGame));
                        //TODO: implement game over screen
                    }


                }
            });

            /**
             * Connect the client to the server.
             * If server is on a local machine, "localhost" should be used as host.
             * Ports should be the same as in the server.193.40.156.227 / localhost
             */
            game.getClient().start();
            game.getClient().connect(5000, "193.40.156.227", 8080, 8081);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
