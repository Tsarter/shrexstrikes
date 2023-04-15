package org.example;

import com.badlogic.gdx.math.collision.BoundingBox;

public class Player {

    public float x, y, z;
    public float rotation;

    public  int id;
    public int health = 100;
    public int score = 0;

    public BoundingBox boundingBox;

    /**
     * Empty constructor is needed here to receive Player objects over the network.
     */
    public Player() { }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }
}