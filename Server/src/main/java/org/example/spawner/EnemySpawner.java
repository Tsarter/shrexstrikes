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

    private Player player;
    private float spawnTimer;
    private int idCounter;

    public EnemySpawner() {
        this.enemies = new Array<Enemy>();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void spawnEnemy() {
        if (enemies.size < 3) {
            idCounter++;
            // Generate a random position for the enemy
            float x = MathUtils.random(-10f, 50f);
            float y = 0.7f;
            float z = MathUtils.random(-10f, 50f);
            float speed = MathUtils.random(0.5f, 2f);
            Enemy enemy = new Enemy(new ModelInstance(new Model()), new Vector3(x, y, z), 0, idCounter, speed);

            // Add the enemy to the list of active enemies
            enemies.add(enemy);
        }
    }
    public void updateEnemyPositsions(float period) {
        // Just wonky
        float delta = period;
        for (Enemy enemy : enemies) {
            enemy.update(player, delta);
        }
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
    public void removePlayer() {
        player = null;
    }
}
