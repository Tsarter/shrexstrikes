package org.example.messages;

import org.example.spawner.Enemy;

import java.util.HashMap;

public class Enemies {
    public HashMap<Integer, HashMap> enemies;

    public Enemies() { }
    public Enemies(HashMap<Integer, HashMap> enemies) {
        this.enemies = enemies;
    }

}