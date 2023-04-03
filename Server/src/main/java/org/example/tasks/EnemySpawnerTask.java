package org.example.tasks;

import org.example.spawner.EnemySpawner;

import java.util.TimerTask;

public class EnemySpawnerTask extends TimerTask {
    EnemySpawner spawner;

    public EnemySpawnerTask(EnemySpawner spawner) {
        this.spawner = spawner;
    }

    @Override
    public void run() {

        spawner.spawnEnemy();

    }
}
