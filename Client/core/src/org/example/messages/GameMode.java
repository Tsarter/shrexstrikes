package org.example.messages;

public class GameMode {
    public int idOfPlayer;
    public GameModes gameMode;
    public int roomId;
    public enum GameModes {
        ZOMBIES,
        PVP,
        TESTING
    }
    public GameMode(int idOfPlayer, GameModes gameMode, int roomId) {
        this.idOfPlayer = idOfPlayer;
        this.gameMode = gameMode;
        this.roomId = roomId;
    }
}
