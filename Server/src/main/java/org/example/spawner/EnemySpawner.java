package org.example.spawner;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Server;
import org.example.Player;

import java.util.TimerTask;

public class EnemySpawner {

    private final Array<Enemy> enemies;

    private Player player;
    private float spawnTimer;
    private static int idCounter;
    private Server server;

    public EnemySpawner(Server server) {
        this.enemies = new Array<Enemy>();
        this.server = server;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void spawnEnemy() {
        if (enemies.size < 25) {
            idCounter = idCounter + 1;
            System.out.println("Spawning enemy with id: " + idCounter);
            // Generate a random position for the enemy
            float x = MathUtils.random(-50f, 50f);
            float y = 0.4f;
            float z = MathUtils.random(-50f, 50f);
            float speed = MathUtils.random(0.5f, 2f);
            Enemy enemy = new Enemy(new ModelInstance(new Model()), new Vector3(x, y, z), 0, idCounter, speed, server);

            // Add the enemy to the list of active enemies
            enemies.add(enemy);
        }
    }
    public void updateEnemyPositsions(float period) {
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
    public void reduceHealth(Enemy enemy, int damage) {
        enemy.health -= damage;
    }
}
