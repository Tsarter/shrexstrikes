package org.example;

import java.util.Map;

public class Player {
    float x, z;
    float rotation;
    int id;
    public Player(float x, float z, int id) {
        this.x = x;
        this.z = z;
        this.rotation = 0;
        this.id = id;
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
}