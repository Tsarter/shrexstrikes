package org.example.messages;

public class GameMode {
    public int idOfPlayer;
    public GameModes gameMode;
    public int roomId;
    public enum GameModes {
        ZOMBIES,
        PVP
    }
    public GameMode() {
    }
    public GameMode(int idOfPlayer, GameModes gameMode) {
        this.idOfPlayer = idOfPlayer;
        this.gameMode = gameMode;
    }
}
