package org.example.gameSession;

import org.example.Player;
import org.example.messages.GameMode;

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

    public enum gameState {
        RUNNING,
        PAUSED,
        ENDED
    }
    private gameState currentGameState;
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
        return;
    }
    public void pauseGame() {
        currentGameState = gameState.PAUSED;
    }
    public void resumeGame() {
        currentGameState = gameState.RUNNING;
    }
    public void endGame() {
        currentGameState = gameState.ENDED;
    }
    public void updateGameState() {

    }
    public HashMap<Integer, Player> getPlayers() {
        return players;
    }
    public HashMap<Integer, Player> getScoreboard() {
        return scoreboard;
    }
}
