package org.example;

import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.Map;

public class Player {
    public float x, y, z;
    public float rotation;
    public int id;
    // Bounds of the player
    BoundingBox boundingBox;
    public int health = 100;

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