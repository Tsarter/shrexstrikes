package org.example.tasks;

import com.esotericsoftware.kryonet.Server;
import org.example.MyServer;
import org.example.spawner.EnemySpawner;

import java.util.TimerTask;

public class EnemyLocationUpdateTask extends TimerTask {
    EnemySpawner spawner;
    float period;
    MyServer server;
    public EnemyLocationUpdateTask(MyServer server, EnemySpawner spawner, float period) {
        this.spawner = spawner;
        this.period = period;
        this.server = server;
    }
    @Override
    public void run() {

        spawner.updateEnemyPositsions(period);
        server.sendEnemies();
    }
}
