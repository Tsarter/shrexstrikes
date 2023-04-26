package org.example;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import org.example.gameSession.GameSession;
import org.example.gameSession.GameSessionManager;
import org.example.gameSession.rooms.PVPRoom;
import org.example.gameSession.rooms.ZombiesRoom;
import org.example.messages.*;
import org.example.gameSession.rooms.zombies.spawner.EnemySpawner;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

public class MyServer {

    /**
     * We want to keep track of all players that are in the game.
     * We use a hashmap (python dictionary) that has their IP as key and Player object as value
     * Each player object has coordinates (x and y)
     */
    private HashMap<Integer, org.example.Player> players = new HashMap<>();
    private final Server server;
    // Define a data structure to associate each client's IP address with their player ID
    // Map<Integer, Integer> playerIds = new HashMap<>();
    // Keep track of the next available player ID
    int nextPlayerId = 0;
    private Timer timer;
    private List<BoundingBox> mapBounds;
    private static int roomId = 0;
    private GameSessionManager gameSessionManager;
    private boolean tasksStarted = false;
    public MyServer() throws IOException {
        timer = new Timer();
        server = new Server(50000, 50000);  // initialize server
        Network.register(server);  // register all the classes that are sent over the network

        // Add listener to tell the server, what to do after something is sent over the network
        server.addListener(new Listener() {

            /**
             * New player connected.
             * We want to make them a new player object to keep track of their coordinates
             */
            public void connected(Connection c) {
                // Get connection id
                // playerIds.put(c.getRemoteAddressTCP().getAddress(), c.getID());
                Player player = new Player(0, 0, c.getID());
                // Send the player to the client
                c.sendTCP(player);
                players.put(c.getID(), player);

                System.out.println(c.getID() + " connected");

            }

            /**
             * We received some data from one of the players.
             */
            public void received(Connection c, Object object) {
                if (gameSessionManager.players.containsKey(c.getID())) {
                    // Pass the data to the appropriate game session for processing
                    gameSessionManager.processData(c, object);
                } else if (object instanceof GameMode) {
                    GameMode gameMode = (GameMode) object;
                    Player player = players.get(c.getID());
                    // Create a new game session
                    if(GameMode.GameModes.ZOMBIES.equals(gameMode.gameMode)) {
                        ZombiesRoom zombiesRoom = new ZombiesRoom(MyServer.this, roomId);

                        gameSessionManager.addGameSession(zombiesRoom, roomId);
                        gameSessionManager.addPlayerToGameSession(player, roomId);
                        gameSessionManager.players.put(c.getID(), player);
                        roomId = roomId + 1;
                    }
                    // If PVP gamemode is selected
                    else if(GameMode.GameModes.PVP.equals(gameMode.gameMode)) {
                        // if room with this id exists, add player to it
                        if(gameSessionManager.getGameSessions().containsKey(gameMode.roomId)) {
                            gameSessionManager.addPlayerToGameSession(player, gameMode.roomId);
                            gameSessionManager.players.put(c.getID(), player);
                        }
                        // else create a new room
                        else {
                            PVPRoom pvpRoom = new PVPRoom(MyServer.this, gameMode.roomId, 10);
                            gameSessionManager.addGameSession(pvpRoom, gameMode.roomId);
                            gameSessionManager.addPlayerToGameSession(player, gameMode.roomId);
                            gameSessionManager.players.put(c.getID(), player);
                            //roomId = roomId + 1;
                        }
                    }
                }
                if (object instanceof MapBounds) {
                    mapBounds = ((MapBounds) object).boundingBox;
                }

            }

            /**
             * Someone disconnected from the game.
             * Removes that player from the game.
             */
            public void disconnected(Connection c) {
                Player player = players.get(c.getID());
                players.remove(c.getID());
                // Remove player from game session
                gameSessionManager.removePlayerFromGameSession(player);


            }
        });

        server.bind(8080, 8081);  // set ports for TCP, UDP. They must be equal with clients.
        server.start();  // start the server
        gameSessionManager = new GameSessionManager(this);

    }


    public Server getServer() {
        return server;
    }
    public List<BoundingBox> getMapBounds() {
        return mapBounds;
    }
}
