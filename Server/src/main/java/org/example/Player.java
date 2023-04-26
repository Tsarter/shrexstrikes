package org.example;

import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.Map;

public class Player {
    public float x, y, z;
    public float rotation;
    public int id;
    // Bounds of the player
    public BoundingBox boundingBox;
    public int health = 100;
    public int score = 0;
    public int timeLeft = 0;
    public int timeTilRespawn = 0;
    public int kills = 0;
    public boolean ready = false; // if the player is ready to start the game (map loaded, etc)
    public boolean alive = true; // if the player is alive
    public enum Character {Shrex, Fiona, Donkey}
    public Character character = Character.Shrex;

    public Player() { }
    public Player(float x, float z, int id) {
        this.x = x;
        this.z = z;
        this.rotation = 0;
        this.id = id;
        // set height to default 1, for now
        this.y = 1;
    }

    /**
     * Move the player to new location
     * @param
     */
    public void move(Map location) {
        this.x = (float) location.get("x");
        this.z = (float) location.get("z");
        this.rotation = (float) location.get("rotation");
    }
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
}