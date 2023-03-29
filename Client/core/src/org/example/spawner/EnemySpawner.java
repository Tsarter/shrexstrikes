package org.example.spawner;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class EnemySpawner {
    private final Model enemyModel;
    private final Array<Enemy> enemies;
    private final float spawnInterval;
    private float spawnTimer;

    public EnemySpawner(Model enemyModel, float spawnInterval) {
        this.enemyModel = enemyModel;
        this.enemies = new Array<Enemy>();
        this.spawnInterval = spawnInterval;
        this.spawnTimer = 0;
    }

    public void update(float delta) {
        // Update spawn timer
        spawnTimer += delta;

        // Spawn a new enemy if the spawn interval has elapsed
        if (spawnTimer >= spawnInterval) {
            spawnEnemy();
            spawnTimer = 0;
        }

        // Update active enemies
        for (Enemy enemy : enemies) {
            enemy.update(delta);
        }
    }

    private void spawnEnemy() {
        // Generate a random position for the enemy
        float x = MathUtils.random(-10f, 10f);
        float y = MathUtils.random(-10f, 10f);
        float z = MathUtils.random(-10f, 10f);

        // Create a new enemy at the random position
        ModelInstance enemyInstance = new ModelInstance(enemyModel, new Vector3(x, y, z));
        Enemy enemy = new Enemy(enemyInstance);

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
