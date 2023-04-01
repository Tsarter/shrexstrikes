package org.example.spawner;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.example.Player;

import java.util.TimerTask;

public class EnemySpawner {

    private final Array<Enemy> enemies;

    private float spawnTimer;

    public EnemySpawner() {
        this.enemies = new Array<Enemy>();
    }

    public void update(Player player) {

        // Update active enemies
        for (Enemy enemy : enemies) {
            enemy.update(delta, playerPosition);
        }
    }

    public void spawnEnemy() {
        // Generate a random position for the enemy
        float x = MathUtils.random(-10f, 10f);
        float y = MathUtils.random(-10f, 10f);
        float z = MathUtils.random(-10f, 10f);
        Enemy enemy = new Enemy(new ModelInstance(new Model()));

        // Add the enemy to the list of active enemies
        enemies.add(enemy);
    }

    public Array<Enemy> getEnemies() {
        return enemies;
    }

    public void removeEnemy(Enemy enemy) {
        enemies.removeValue(enemy, true);
    }

    public void removeAllEnemies() {
        enemies.clear();
    }
}
