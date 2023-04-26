package org.example.tasks;

import org.example.gameSession.GameSession;
import org.example.gameSession.rooms.PVPRoom;

import java.util.TimerTask;

public class PVPGameTask extends TimerTask {
    private int timeLeft;
    private int timeLimit;
    private PVPRoom gameSession;
    public PVPGameTask(int timeLimit, PVPRoom gameSession) {
        this.timeLimit = timeLimit;
        this.timeLeft = timeLimit;
        this.gameSession = gameSession;
    }
    @Override
    public void run() {
        timeLeft--;
        if (timeLeft <= 0) {
            gameSession.endGame();
        }
        // If any player is waiting for respawn, decrease respawn time
        for (int id : gameSession.getPlayers().keySet()) {
            if (gameSession.getPlayers().get(id).timeTilRespawn != 0) {
                gameSession.getPlayers().get(id).timeTilRespawn--;
                if (gameSession.getPlayers().get(id).timeTilRespawn <= 0) {
                    gameSession.getPlayers().get(id).alive = true;
                }
            }
        }
        gameSession.timeLeft = timeLeft;
        gameSession.sendGameStatusToPlayers();
    }
}
