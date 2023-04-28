package org.example.messages;

public class GameStateChange {
    public int idOfPlayer;
    public GameStates gameState;
    public enum GameStates {
        IN_LOBBY,
        IN_GAME,
        IN_GAME_OVER,
        IN_DEATH_SCREEN,
        IN_PAUSE_MENU,
        IN_MENU,
        GAME_STARTING,
        GAME_OVER,
        READY,
        NOT_READY
    }
    public GameStateChange(int idOfPlayer, GameStates gameState) {
        this.idOfPlayer = idOfPlayer;
        this.gameState = gameState;
    }
    public GameStateChange() {
    }
}
