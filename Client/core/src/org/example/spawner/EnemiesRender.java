package org.example.spawner;

import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class EnemiesRender {
    // Id and the enemy
    private HashMap<String, Enemy> enemies;

    public void LoadEnemies(HashMap<String, Enemy> enemies) {
        this.enemies = enemies;
    }


}
