package org.example;

import com.badlogic.gdx.math.collision.BoundingBox;

public class Player {

    public float x, y, z;
    public float rotation;
    public int id;
    // Bounds of the player
    public BoundingBox boundingBox;
    public int health = 100;
    public int score = 0;
    public int kills = 0;
    public int deaths = 0;
    public int damage = 0;
    public int shots = 0;
    public int hits = 0;
    public int accuracy = 0;
    public int ping = 0;
    public int team = 0;
    public boolean ready = false; // if the player is ready to start the game (map loaded, etc)
    public enum Character {Shrex, Fiona, Donkey}
    public Character character = Character.Shrex;

    /**
     * Empty constructor is needed here to receive Player objects over the network.
     */
    public Player() { }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }
}