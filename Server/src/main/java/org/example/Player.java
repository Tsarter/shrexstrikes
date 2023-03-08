package org.example;

import java.util.Map;

public class Player {
    int x, y;
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Move the player to new location
     * @param
     */
    public void move(Map location) {
        this.x = (int) location.get("x");
        this.y = (int) location.get("y");
    }
}