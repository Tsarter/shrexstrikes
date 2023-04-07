package org.example;


import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import org.example.messages.*;
import org.example.Player;
import org.example.spawner.Enemy;
import org.example.spawner.EnemySpawner;
import org.example.tasks.EnemyLocationUpdateTask;
import org.example.tasks.EnemySpawnerTask;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

public class MyServer {

    /**
     * We want to keep track of all players that are in the game.
     * We use a hashmap (python dictionary) that has their IP as key and Player object as value
     * Each player object has coordinates (x and y)
     */
    private final HashMap<java.net.InetSocketAddress, org.example.Player> players = new HashMap<>();
    private final Server server;
    // Define a data structure to associate each client's IP address with their player ID
    Map<InetAddress, Integer> playerIds = new HashMap<>();
    // Keep track of the next available player ID
    int nextPlayerId = 0;
    private Timer timer;
    private List<BoundingBox> mapBounds;
    private Player testPlayer;
    private EnemySpawner spawner;
    private TimerTask enemySpawnerTask;
    private TimerTask enemyLocationUpdateTask;
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
                // Assign the next available player ID to the client
                int playerId = nextPlayerId++;
                playerIds.put(c.getRemoteAddressTCP().getAddress(), playerId);

                Player player = new Player(0, 0, playerId);
                // Send the player to the client
                c.sendTCP(player);

                players.put(c.getRemoteAddressUDP(), player);

                System.out.println(c.getRemoteAddressUDP().toString() + " connected");

                sendState();  // send info about all players to all players

                // Start the tasks if they haven't been started yet
                if (!tasksStarted) {
                    // Start the tasks
                    scheduleTasks();
                    tasksStarted = true;
                }
            }

            /**
             * We received some data from one of the players.
             */
            public void received(Connection c, Object object) {
                if (object instanceof Player ) {
                    Player player = players.get(c.getRemoteAddressUDP());  // get the player that sent their location
                    Player playerClient = (Player) object;  // get the location that they sent
                    testPlayer = playerClient;
                    spawner.setPlayer(testPlayer);
                    if (player.id == playerClient.id) {
                        // update the server's player object with the new location
                        player.x = playerClient.x;
                        player.z = playerClient.z;
                        player.rotation = playerClient.rotation;
                        player.boundingBox = playerClient.boundingBox;
                    }

                    sendState();  // send info about all players to all players
                }

                else if (object instanceof PlayerBullet) {
                    Player player = players.get(c.getRemoteAddressUDP());  // get the player that sent the bullet
                    PlayerBullet playerBullet = (PlayerBullet) object;  // get the bullet that they sent

                    // iterate over all the players and check if the bullet intersects with any of them
                    for (Player p : players.values()) {
                        if (p.id != player.id && p.boundingBox != null) {
                        Ray bulletRay = new Ray(playerBullet.getPosition(), playerBullet.getDirection());  // create a ray from the bullet
                            if (Intersector.intersectRayBoundsFast(bulletRay, p.boundingBox)) {
                                // check if there are any blocking objects between the player that fired the bullet and the player that was hit
                                boolean hit = true;
                                // get the distance from the player that fired the bullet to the player that was hit
                                float distance = playerBullet.getPosition().dst(p.boundingBox.getCenter(new com.badlogic.gdx.math.Vector3()));
                                Vector3 intersection = new Vector3();
                                for (BoundingBox bb : mapBounds) {
                                    if (Intersector.intersectRayBounds(bulletRay, bb, intersection)){
                                        // Object might be after the player that was hit, so check the distance
                                        if (intersection.dst(playerBullet.getPosition()) < distance) {
                                            hit = false;
                                            System.out.println("Player: " + p.id + " was hit by player: " + player.id + " but there was an object in the way.");
                                            break;
                                        }
                                    }

                                }
                                if (hit) {
                                    System.out.println("Player: " + p.id + " was hit by player: " + player.id);
                                    // send a message to all players that the player was hit
                                    server.sendToAllTCP(new PlayerHit(p.id, player.id));
                                    break;
                                }
                            } else{
                                System.out.println("Player missed");
                            }
                        }
                    }
                    // Check if the bullet hit any enemies
                    for (Enemy e : spawner.getEnemies()) {
                        if (e.getBoundingBox() != null) {
                            Ray bulletRay = new Ray(playerBullet.getPosition(), playerBullet.getDirection());  // create a ray from the bullet
                            if (Intersector.intersectRayBoundsFast(bulletRay, e.getBoundingBox())) {
                                // check if there are any blocking objects between the player that fired the bullet and the enemy that was hit
                                boolean hit = true;
                                // get the distance from the player that fired the bullet to the enemy that was hit
                                float distance = playerBullet.getPosition().dst(e.getBoundingBox().getCenter(new com.badlogic.gdx.math.Vector3()));
                                Vector3 intersection = new Vector3();
                                for (BoundingBox bb : mapBounds) {
                                    if (Intersector.intersectRayBounds(bulletRay, bb, intersection)){
                                        // Object might be after the enemy that was hit, so check the distance
                                        if (intersection.dst(playerBullet.getPosition()) < distance) {
                                            hit = false;
                                            System.out.println("Object between target and player.");
                                            break;
                                        }
                                    }

                                }
                                if (hit) {
                                    System.out.println("Enemy: " + e.id + " was hit by player: " + player.id);
                                    // send a message to all players that the enemy was hit
                                    int damage = 10;
                                    spawner.reduceHealth(e, damage);
                                    if (e.health <= 0) {
                                        server.sendToAllTCP(new EnemyHit(e.id, player.id, damage, true));
                                        spawner.removeEnemy(e);
                                        break;
                                    }
                                    server.sendToAllUDP(new EnemyHit(e.id, player.id, damage, false));
                                    break;
                                }
                            } else{
                                System.out.println("Player missed");
                            }
                        }
                    }
                } else if (object instanceof MapBounds) {
                    mapBounds = ((MapBounds) object).boundingBox;
                }

            }

            /**
             * Someone disconnected from the game.
             * Removes that player from the game.
             */
            public void disconnected(Connection c) {
                players.remove(c.getRemoteAddressUDP());
                spawner.removePlayer();
                spawner.removeAllEnemies();
                // System.out.println(c.getRemoteAddressUDP().toString()  + " disconnected");

                sendState();  // send info about all players to all players
                // Stop the tasks if all players have disconnected
                if (players.isEmpty()) {
                    // Cancel the tasks
                    cancelTasks();
                    tasksStarted = false;
                    //timer.cancel();
                }
            }
        });

        server.bind(8080, 8081);  // set ports for TCP, UDP. They must be equal with clients.
        server.start();  // start the server


    }

    /**
     * Sends all player objects to all players.
     * So everyone can see, where all the players are.
     */
    private void sendState() {
        // Create a player array from the hashmap values
        Player[] playersList = players.values().toArray(new Player[0]);

        // send this array to all of the connected clients
        server.sendToAllUDP(playersList);
    }
    /*
      * Gets called every 1 second from EnemyLocationUpdateTask
     */
    public void sendEnemies() {
        // Create a player array from the hashmap values
        HashMap enemies = new HashMap();
        for (Enemy e : spawner.getEnemies()) {
            HashMap enemyInfo = new HashMap();
            enemyInfo.put("x", e.x);
            enemyInfo.put("y", e.y);
            enemyInfo.put("z", e.z);
            enemyInfo.put("rotation", e.rotation);
            enemyInfo.put("type", e.type);
            enemyInfo.put("id", e.id);
            enemyInfo.put("health", e.health);
            System.out.println("Enemy: " + e.id + " health: " + e.health);
            enemies.put(e.id, enemyInfo);
        }
        if (enemies.size() > 0 && server.getConnections().length > 0) {
            Enemies enemiesObject = new Enemies(enemies);
            System.out.println("Sending enemies, total: " + enemies.size());
            // send this array to all of the connected clients
            server.sendToAllUDP(enemiesObject);
        }
    }
    private void scheduleTasks() {
        // Schedule the enemy spawner task to run every 5 seconds
        spawner = new EnemySpawner();
        enemySpawnerTask = new EnemySpawnerTask(spawner);
        timer.scheduleAtFixedRate(enemySpawnerTask, 5000, 5000);

        // Schedule the enemies location update task to run every 1 second
        int period = 1000;
        enemyLocationUpdateTask = new EnemyLocationUpdateTask(this, spawner, period);
        timer.scheduleAtFixedRate(enemyLocationUpdateTask, 6000, period);
    }
    private void cancelTasks() {
        enemySpawnerTask.cancel();
        enemyLocationUpdateTask.cancel();
    }

}
