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
        gameSession.timeLeft = timeLeft;
        gameSession.sendGameStatusToPlayers();
    }
}
