package org.example.tasks;

import org.example.spawner.EnemySpawner;

import java.util.TimerTask;

public class EnemyLocationUpdateTask extends TimerTask {
    EnemySpawner spawner;
    float period;
    public EnemyLocationUpdateTask(EnemySpawner spawner, float period) {
        this.spawner = spawner;
        this.period = period;
    }
    @Override
    public void run() {

        spawner.updateEnemyPositsions(period);
    }
}
