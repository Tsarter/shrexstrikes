package org.example.gameSession.rooms;


import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.esotericsoftware.kryonet.Server;
import org.example.MyServer;
import org.example.Player;
import org.example.gameSession.GameSession;
import org.example.gameSession.rooms.zombies.spawner.Enemy;
import org.example.gameSession.rooms.zombies.spawner.EnemySpawner;
import org.example.messages.*;
import org.example.tasks.EnemyLocationUpdateTask;
import org.example.tasks.EnemySpawnerTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

public class ZombiesRoom extends GameSession {
    private List<Enemy> enemies = new ArrayList<>();
    private int score = 0;
    private int time = 0;
    private int gameSessionLength = 60;
    private int difficulty = 1;
    private Player player;
    private int currentWave = 0;
    private int zombiesRemaining = 0;
    private EnemySpawner enemySpawner;
    public Server server;
    public MyServer myServer;
    private EnemySpawnerTask enemySpawnerTask;
    private EnemyLocationUpdateTask enemyLocationUpdateTask;
    private Timer timer = new Timer();
    private static int zombieIdCounter = 0;
    int zombieDamage = 10;
    int zombieHealth = 100;
    public ZombiesRoom(MyServer myServer, int sessionId) {
        super(GameMode.GameModes.ZOMBIES, sessionId, myServer);
        this.myServer = myServer;
        this.server = myServer.getServer();
    }
    @Override
    public void processData(Object object) {
        if (object instanceof PlayerBullet) {
            PlayerBullet playerBullet = (PlayerBullet) object;
            Player player = super.getPlayers().get(playerBullet.owner);  // get the player that sent the bullet
            checkIfBulletHitEnemy(playerBullet, player);
        }
        if (object instanceof Player ) {
            Player playerClient = (Player) object;  // get the location that they sent
            player = super.getPlayers().get(playerClient.id); // get the player that sent their location

            if (player.id == playerClient.id) {
                // update the server's player object with the new location
                player.x = playerClient.x;
                player.y = playerClient.y;
                player.z = playerClient.z;
                player.rotation = playerClient.rotation;
                player.boundingBox = playerClient.getBoundingBox();
                player.name = playerClient.name;
            }

            //sendState();  // send info about all players in the room
        }
        if (object instanceof GameStateChange) {
            GameStateChange gameStateChange = (GameStateChange) object;
            if (gameStateChange.gameState == GameStateChange.GameStates.IN_GAME &&
                    super.getCurrentGameState() == GameStateChange.GameStates.IN_PAUSE_MENU) {
                // Resume the game, if the player was previously in the pause menu
                System.out.println("Resuming game");
                resumeGame();
            }
            else if (gameStateChange.gameState == GameStateChange.GameStates.IN_GAME) {
                System.out.println("Starting game");
                startGame();
            }
            else if (gameStateChange.gameState == GameStateChange.GameStates.IN_PAUSE_MENU) {
                System.out.println("Pausing game");
                pauseGame();
            }
        }
    }
    public void sendState() {
        // Send the current state of the game to all players in the room
        // Create a player array from the hashmap values
        Player[] playersList = super.getPlayers().values().toArray(new Player[0]);

        // send this array to all players in the room
        //server.sendToAllUDP(playersList);
        // send this array to all the players in the room
        for (Player p : super.getPlayers().values()) {
            server.sendToUDP(p.id, playersList);
        }

    }
    @Override
    public void startGame() {
        super.startGame();
        startNewWave();
        // Schedule the enemies location update task to run every 1 second
        int period = 1000;
        enemyLocationUpdateTask = new EnemyLocationUpdateTask(this, enemySpawner, period);
        timer.scheduleAtFixedRate(enemyLocationUpdateTask, 0, period);
    }

    public void startNewWave() {
        // Increase the wave number and calculate the number of zombies for the new wave
        currentWave++;
        int numPlayers = super.getPlayers().size();
        int baseZombies = currentWave * 10;
        zombiesRemaining = baseZombies + (numPlayers * 5);
        if (zombieDamage <= 30) {
            zombieDamage = zombieDamage + 5;
        }
        // Spawn zombies and send initial data to players
        spawnZombies();
        sendGameStatusToPlayers();
    }

    public void zombieKilled() {
        zombiesRemaining--;
        if (zombiesRemaining == 0) {
            // All zombies have been killed, start a new wave
            startNewWave();
        }
    }

    private void spawnZombies() {
        // Code to randomly spawn zombies in the game world
        for (int i = 0; i < zombiesRemaining; i++) {
            zombieIdCounter = zombieIdCounter + 1;
            // System.out.println("Spawning enemy with id: " + zombieIdCounter);
            // Generate a random position for the enemy
            float x = MathUtils.random(-50f, 50f);
            float y = 0.4f;
            float z = MathUtils.random(-50f, 50f);
            float speed = MathUtils.random(0.5f, 2f);
            Enemy enemy = new Enemy(new ModelInstance(new Model()), new Vector3(x, y, z), 0, zombieIdCounter, speed, this);

            // Add the enemy to the list of active enemies
            enemies.add(enemy);
        }
    }
    public void updateEnemyPositsions(float period) {
        float delta = period;
        if (player == null) {
            return;
        }
        for (Enemy enemy : enemies) {
            enemy.update(player, delta);
        }
    }

    public void sendGameStatusToPlayers() {
        // Code to send information about the current game state to all players in the room
        HashMap enemyStatuses = new HashMap();
        for (Enemy e : enemies) {
            HashMap enemyInfo = new HashMap();
            enemyInfo.put("x", e.x);
            enemyInfo.put("y", e.y);
            enemyInfo.put("z", e.z);
            enemyInfo.put("rotation", e.rotation);
            enemyInfo.put("type", e.type);
            enemyInfo.put("id", e.id);
            enemyInfo.put("health", e.health);
            enemyStatuses.put(e.id, enemyInfo);
        }
        if (enemies.size() > 0 && server.getConnections().length > 0) {
            Enemies enemiesObject = new Enemies(enemyStatuses, currentWave, score);
            System.out.println("Sending enemies to room " + super.sessionId + ", total: " + enemies.size());
            // send this array to all the players in the room
            for (Player p : super.getPlayers().values()) {
                server.sendToUDP(p.id, enemiesObject);
            }
        }
    }
    public void checkIfBulletHitEnemy(PlayerBullet playerBullet, Player player) {
        // Check if the bullet hit any enemies
        for (Enemy e : enemies) {
            if (e.getBoundingBox() != null) {
                Ray bulletRay = new Ray(playerBullet.getPosition(), playerBullet.getDirection());  // create a ray from the bullet
                if (Intersector.intersectRayBoundsFast(bulletRay, e.getBoundingBox())) {
                    // check if there are any blocking objects between the player that fired the bullet and the enemy that was hit
                    boolean hit = true;
                    // get the distance from the player that fired the bullet to the enemy that was hit
                    float distance = playerBullet.getPosition().dst(e.getBoundingBox().getCenter(new com.badlogic.gdx.math.Vector3()));
                    Vector3 intersection = new Vector3();
                    for (BoundingBox bb : myServer.getMapBounds()) {
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
                        // send a message to all players in the room to tell them that the enemy was hit
                        enemies.get(enemies.indexOf(e)).dealDamage(zombieDamage);
                        if (e.health <= 0) {
                            for (Player p : super.getPlayers().values()) {
                                server.sendToUDP(p.id, new EnemyHit(e.id, player.id, zombieDamage, true));
                            }
                            player.score += 20;
                            score += 20;
                            enemies.remove(e);
                            break;
                        } else{
                            for (Player p : super.getPlayers().values()) {
                                server.sendToUDP(p.id, new EnemyHit(e.id, player.id, zombieDamage, false));
                            }
                            player.score += 10;
                            score += 10;
                        }
                        break;
                    }
                } else{
                    // System.out.println("Player missed");
                }
            }
        }
    }
}
