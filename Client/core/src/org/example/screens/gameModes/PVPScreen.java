package org.example.screens.gameModes;


public class PVPScreen extends GameScreen {

    private GameScreen gameScreen;
    private int timeLeft;
    private int timeLimit;
    public PVPScreen(GameScreen gameScreen, int timeLimit) {
        this.gameScreen = gameScreen;
        this.timeLimit = timeLimit;
        this.timeLeft = timeLimit;
    }

}
