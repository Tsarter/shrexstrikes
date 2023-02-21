package org.example;

public class Player {
    int x, y;
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Move the player by 1 unit.
     * Players can not move out of some set bounds. (coordinates must be within 0 and 9)
     * @param direction (NESW)
     */
    public void move(Character direction) {
        if (direction == 'N') {
            this.y = Math.max(0, this.y - 1);
        }
        if (direction == 'S') {
            this.y = Math.min(9, this.y + 1);
        }
        if (direction == 'W') {
            this.x = Math.max(0, this.x - 1);
        }
        if (direction == 'E') {
            this.x = Math.min(9, this.x + 1);
        }
    }
}