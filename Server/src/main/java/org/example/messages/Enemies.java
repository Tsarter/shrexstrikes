package org.example.messages;

import java.util.HashMap;

public class Enemies {
    public HashMap<Integer, HashMap> enemies;
    public int waveNumber;
    public int score;

    public Enemies() {
    }
    public Enemies(HashMap<Integer, HashMap> enemies, int waveNumber,int score) {
        this.enemies = enemies;
        this.waveNumber = waveNumber;
        this.score = score;
    }

}
