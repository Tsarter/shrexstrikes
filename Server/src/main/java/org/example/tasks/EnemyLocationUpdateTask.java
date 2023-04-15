package org.example.tasks;

import org.example.MyServer;
import org.example.gameSession.rooms.ZombiesRoom;
import org.example.gameSession.rooms.zombies.spawner.EnemySpawner;
import org.example.messages.GameStateChange;

import java.util.TimerTask;

public class EnemyLocationUpdateTask extends TimerTask {
    EnemySpawner spawner;
    float period;
    ZombiesRoom zombiesRoom;
    public EnemyLocationUpdateTask(ZombiesRoom zombiesRoom, EnemySpawner spawner, float period) {
        this.spawner = spawner;
        this.period = period;
        this.zombiesRoom = zombiesRoom;
    }
    @Override
    public void run() {
        if (zombiesRoom.getPlayers().size() == 0) {
            cancel();
        }
        if (zombiesRoom.getCurrentGameState() == GameStateChange.GameStates.IN_GAME) {
            zombiesRoom.updateEnemyPositsions(period);
            zombiesRoom.sendGameStatusToPlayers();
        }

    }
}
