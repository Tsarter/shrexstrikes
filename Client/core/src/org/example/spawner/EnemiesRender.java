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
    public void updateEnemies(List<HashMap> enemiesInfo) {
        for (HashMap enemyInfo : enemiesInfo) {
            String id = (String) enemyInfo.get("id");
            Enemy enemy = enemies.get(id);
            if (enemy != null) {
                Vector3 enemyPosition = new Vector3((float) enemyInfo.get("x"), (float) enemyInfo.get("y"), (float) enemyInfo.get("z"));
                float enemyDirection = (float) enemyInfo.get("rotation");
                enemy.update(enemyPosition, enemyDirection);
            }
            else {
                Enemy newEnemy = new Enemy();
            }
        }
    }

}
