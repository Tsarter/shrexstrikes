package org.example.tasks;

import org.example.gameSession.GameSession;

import java.util.TimerTask;

public class PVPGameTask extends TimerTask {
    private int timeLeft;
    private int timeLimit;
    private GameSession gameSession;
    public PVPGameTask(int timeLimit, GameSession gameSession) {
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
    }
}
