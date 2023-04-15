package org.example.gameSession;

import org.example.Player;
import org.example.messages.GameMode;
import org.example.messages.GameStateChange;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;

public class GameSession {
    public int sessionId;
    private HashMap<Integer, Player> players = new HashMap<>();
    private GameMode.GameModes gameMode;

    public void processData(Object data) {

    }

    public Object getGameMode() {
        return gameMode;
    }

    public boolean hasPlayer(int playerId) {
        if (players.containsKey(playerId)) {
            return true;
        }
        return false;
    }

    private GameStateChange.GameStates currentGameState;
    private Timer timer;
    // scoreboard
    private HashMap<Integer, Player> scoreboard = new HashMap<>();

    public GameSession(GameMode.GameModes gameMode, int sessionId) {
        this.gameMode = gameMode;
        this.sessionId = sessionId;
    }
    public void addPlayer(Player player) {
        players.put(player.id, player);
    }

    public void removePlayer(Player player) {
        players.remove(player.id);
    }
    public void startGame() {
        currentGameState = GameStateChange.GameStates.IN_GAME;
    }
    public void pauseGame() {
        currentGameState = GameStateChange.GameStates.IN_PAUSE_MENU;
    }
    public void resumeGame() {
        currentGameState = GameStateChange.GameStates.IN_GAME;
    }
    public void endGame() {
        currentGameState = GameStateChange.GameStates.IN_GAME_OVER;
    }
    public void updateGameState() {

    }
    public HashMap<Integer, Player> getPlayers() {
        return players;
    }
    public HashMap<Integer, Player> getScoreboard() {
        return scoreboard;
    }
    public GameStateChange.GameStates getCurrentGameState() {
        return currentGameState;
    }
}
