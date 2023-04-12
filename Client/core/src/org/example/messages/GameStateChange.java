package org.example.messages;

public class GameStateChange {
    public int idOfPlayer;
    public GameStates gameState;
    public enum GameStates {
        IN_LOBBY,
        IN_GAME,
        IN_GAME_OVER
    }
    public GameStateChange(int idOfPlayer, GameStates gameState) {
        this.idOfPlayer = idOfPlayer;
        this.gameState = gameState;
    }
}
