package org.example.messages;

public class GameMode {
    public int idOfPlayer;
    public GameModes gameMode;
    public enum GameModes {
        ZOMBIES,
        PVP,
        TESTING
    }
    public GameMode(int idOfPlayer, GameModes gameMode) {
        this.idOfPlayer = idOfPlayer;
        this.gameMode = gameMode;
    }
}
