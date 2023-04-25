package org.example;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.example.messages.*;

import java.io.IOException;


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
                    if (game.gameMode == GameMode.GameModes.PVP) {
                        handlePVP(connection, object);
                    }
                    if (game.gameMode == GameMode.GameModes.ZOMBIES) {
                        handleZombies(connection, object);
                    }

                    if (object instanceof Player[]) {
                        // get the list of players
                        game.setPlayersList((Player[]) object);
                    }
                    // we recieved the server created player object
                    else if (object instanceof Player) {
                        game.setPlayer((Player) object);
                        int roomID = 100; // For testing purposes the id is 100
                        game.getClient().sendTCP(new GameMode(game.getPlayer().id, game.gameMode, 100));
                    }


                }
            });

            /**
             * Connect the client to the server.
             * If server is on a local machine, "localhost" should be used as host.
             * Ports should be the same as in the server.193.40.156.227 / localhost
             */
            game.getClient().start();
            game.getClient().connect(5000, "localhost", 8080, 8081);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handlePVP(Connection connection,Object object){
        // we recieved playerHit
        if (object instanceof PlayerHit) {
            PlayerHit playerHit = (PlayerHit) object;
            // if the player that was hit is the current player
            game.gameScreen.handleIncomingPlayerHit(playerHit);
        }
        if (object instanceof GameStateChange) {
            GameStateChange gameStateChange = (GameStateChange) object;
            if (gameStateChange.gameState == GameStateChange.GameStates.GAME_STARTING) {
                game.showPVPScreen();
            }
        }
    }
    public void handleZombies(Connection connection,Object object){
        if (object instanceof Player[]) {
            // get the list of players
            game.setPlayersList((Player[]) object);

            if (game.gameMode == GameMode.GameModes.ZOMBIES){
                if(game.gameState !=GameStateChange.GameStates.IN_GAME &&
                        game.getPlayersList().length == 1) {

                    game.showZombiesScreen();
                }
            }
        }
        // we recieved playerHit
        else if (object instanceof PlayerHit) {
            PlayerHit playerHit = (PlayerHit) object;
            // if the player that was hit is the current player
            game.gameScreen.handleIncomingPlayerHit(playerHit);
        }
        else if (object instanceof Enemies) {
            if (game.gameMode == GameMode.GameModes.ZOMBIES) {
                Enemies enemiesInfo = (Enemies) object;
                game.gameScreen.handleIncomingEnemies(enemiesInfo);
            }
        }
        else if (object instanceof EnemyHit) {
            EnemyHit enemyHit = (EnemyHit) object;
            game.gameScreen.handleIncomingEnemyHit(enemyHit);
        }
    }
}

