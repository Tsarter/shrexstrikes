package org.example.gameSession.rooms;

import com.esotericsoftware.kryonet.Server;
import org.example.MyServer;
import org.example.gameSession.GameSession;
import org.example.messages.GameMode;

public class PVPRoom extends GameSession {
    private boolean gameStarted;

    public PVPRoom(MyServer myServer, int sessionID) {
        super(GameMode.GameModes.PVP, sessionID);
        gameStarted = false;
    }

    public void startGame() {
        super.startGame();
        gameStarted = true;
        sendGameStartToPlayers();
    }

    public void endGame() {
        super.endGame();
        gameStarted = false;
        sendGameEndToPlayers();
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    private void sendGameStartToPlayers() {
        // Code to send information about the game start to all players in the room
        // ...
    }

    private void sendGameEndToPlayers() {
        // Code to send information about the game end to all players in the room
        // ...
    }
}

