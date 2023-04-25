package org.example.gameSession.rooms;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.esotericsoftware.kryonet.Server;
import org.example.MyServer;
import org.example.Player;
import org.example.gameSession.GameSession;
import org.example.messages.GameMode;
import org.example.messages.GameStateChange;
import org.example.messages.PlayerBullet;
import org.example.messages.PlayerHit;
import org.example.tasks.PVPGameTask;

import java.util.Timer;

public class PVPRoom extends GameSession {
    private boolean gameStarted;
    public MyServer myServer;
    private Timer timer;
    private int roomSize = 2;


    public PVPRoom(MyServer myServer, int sessionID) {
        super(GameMode.GameModes.PVP, sessionID);
        gameStarted = false;
        this.myServer = myServer;
    }

    public void startGame() {
        super.startGame();
        gameStarted = true;
        // Start PVPGameTask
        PVPGameTask pvpGameTask = new PVPGameTask(120, this);
        timer = new Timer();
        timer.scheduleAtFixedRate(pvpGameTask, 0, 1000);
        sendGameStartToPlayers();
        System.out.println("Players with id " + super.getPlayers().keySet() + " started a game in room " + sessionId);
    }

    public void endGame() {
        super.endGame();
        gameStarted = false;
        sendGameEndToPlayers();
    }
    @Override
    public void processData(Object data) {
        if (data instanceof GameStateChange) {
            GameStateChange gameStateChange = (GameStateChange) data;
            if (gameStateChange.gameState == GameStateChange.GameStates.READY) {
                Player player = super.getPlayers().get(gameStateChange.idOfPlayer);
                player.ready = true;
                // Check if room is full
                if (super.getPlayers().size() == roomSize) {
                    // Check if all players are ready
                    boolean allReady = true;
                    for (Player p : super.getPlayers().values()) {
                        if (p.ready == false) {
                            allReady = false;
                            break;
                        }
                    }
                    if (allReady) {
                        startGame();
                    }
                }
            }
            if (gameStateChange.gameState == GameStateChange.GameStates.NOT_READY) {
                Player player = super.getPlayers().get(gameStateChange.idOfPlayer);
                player.ready = false;
            }
        }
        else if (data instanceof Player) {
            Player playerClient = (Player) data;  // get the location that they sent
            Player player = super.getPlayers().get(playerClient.id); // get the player that sent their location

            if (player.id == playerClient.id) {
                // update the server's player object with the new location
                player.x = playerClient.x;
                player.z = playerClient.z;
                player.rotation = playerClient.rotation;
                player.boundingBox = playerClient.boundingBox;
            }

            sendUpdatedPlayerLocations();  // send info about all players to all players
        }
        else if (data instanceof PlayerBullet) {
            PlayerBullet playerBullet = (PlayerBullet) data;  // get the bullet that they sent
            Player player = super.getPlayers().get(playerBullet.owner);  // get the player that sent the bullet

            // iterate over all the players and check if the bullet intersects with any of them
            for (Player p : super.getPlayers().values()) {
                if (p.id != player.id && p.boundingBox != null) {
                    Ray bulletRay = new Ray(playerBullet.getPosition(), playerBullet.getDirection());  // create a ray from the bullet
                    if (Intersector.intersectRayBoundsFast(bulletRay, p.boundingBox)) {
                        // check if there are any blocking objects between the player that fired the bullet and the player that was hit
                        boolean hit = true;
                        // get the distance from the player that fired the bullet to the player that was hit
                        float distance = playerBullet.getPosition().dst(p.boundingBox.getCenter(new com.badlogic.gdx.math.Vector3()));
                        Vector3 intersection = new Vector3();
                        for (BoundingBox bb : myServer.getMapBounds()) {
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
                            handlePlayerHit(p.id, player.id, 10);
                            break;
                        }
                    } else{
                        System.out.println("Player missed");
                    }
                }
            }

        }
    }
    private void sendUpdatedPlayerLocations(){
        // Send the updated player locations to all the players in the room
        for (Player player : super.getPlayers().values()) {
            myServer.getServer().sendToUDP(player.id, super.getPlayers().values().toArray());
        }
    }
    private void handlePlayerHit(int playerHitID, int playerWhoHitID, int damage) {
        // Update the player's health
        Player playerWhoGotHit = super.getPlayers().get(playerHitID);
        playerWhoGotHit.health -= damage;
        if (playerWhoGotHit.health <= 0) {
            // Player died
            playerWhoGotHit.health = 0;
            playerWhoGotHit.alive = false;
            // Check if all players are dead
            boolean allDead = true;
            for (Player p : super.getPlayers().values()) {
                if (p.alive == true) {
                    allDead = false;
                    break;
                }
            }
            if (allDead) {
                // All players are dead, end the game
                endGame();
            }
        }
        PlayerHit playerHit = new PlayerHit(playerHitID, playerWhoHitID, damage);
        // Notify all players in the room that a player was hit
        for (Player player : super.getPlayers().values()) {
            myServer.getServer().sendToTCP(player.id, playerHit);
        }
    }

    private void sendGameStartToPlayers() {
        // Notify all players in the room that the game has started
        super.startGame();
        for (Player player : super.getPlayers().values()) {
            GameStateChange gameStateChange = new GameStateChange(player.id, GameStateChange.GameStates.GAME_STARTING);
            myServer.getServer().sendToTCP(player.id, gameStateChange);
        }
    }

    private void sendGameEndToPlayers() {
        // Notify all players in the room that the game has ended
        super.endGame();
        for (Player player : super.getPlayers().values()) {
            GameStateChange gameStateChange = new GameStateChange(player.id, GameStateChange.GameStates.GAME_OVER);
            myServer.getServer().sendToTCP(player.id, gameStateChange);
        }
    }
}

