package org.example.tasks;

import org.example.Player;
import org.example.gameSession.GameSession;
import org.example.gameSession.rooms.PVPRoom;

import java.util.TimerTask;

/**
 * This class is responsible for sending the updated player list to the clients.
 * It is called every 100ms.
 */
public class UpdatePlayersTask extends TimerTask {
    private GameSession gameSession;
    public UpdatePlayersTask(GameSession gameSession) {
        this.gameSession = gameSession;
    }
    @Override
    public void run() {
        for(int id : gameSession.getPlayers().keySet()) {
            gameSession.myServer.getServer().sendToUDP(id, gameSession.getPlayers());
        }
    }
}
