package org.example.gameSession;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import org.example.MyServer;
import org.example.Player;
import org.example.gameSession.rooms.PVPRoom;
import org.example.gameSession.rooms.TestingRoom;
import org.example.gameSession.rooms.ZombiesRoom;
import org.example.messages.GameMode;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSessionManager {

    private HashMap<Integer, GameSession> gameSessions;
    public Map<Integer, Player> players = new HashMap<Integer, Player>();
    public MyServer myServer;
    public GameSessionManager(MyServer myServer) {
        this.myServer = myServer;
        // Initialize the list of game sessions
        gameSessions = new HashMap<Integer, GameSession>();
        /*gameSessions.add(new PVPRoom(myServer));
        gameSessions.add(new TestingRoom(myServer));
        gameSessions.add(new ZombiesRoom(myServer));*/
    }

    public void addPlayerToGameSession(Player player, int roomId) {
        // Find the game session that matches the specified game mode

        gameSessions.get(roomId).addPlayer(player);

    }
    public void removePlayerFromGameSession(Player player) {
        // Find the game session that this player belongs to and remove the player
        for (GameSession gameSession : gameSessions.values()) {
            if (player != null && gameSession.hasPlayer(player.id)) {
                gameSession.removePlayer(player);
                if (gameSession.getPlayers().size() == 0) {
                    gameSessions.remove(gameSession.sessionId);
                }
                // Remove the player with the specified ID from the list of players
                players.remove(player.id);
                break;
            }
        }
    }
    public void processData(Connection c, Object data) {
        // Find the game session that this player belongs to
        GameSession gameSession = getGameSessionByConnectionId(c);

        // Pass the data to the game session for processing
        gameSession.processData(data);
    }


    private GameSession getGameSessionByConnectionId(Connection c) {
        // Find the player ID associated with this connection
        // InetAddress address = c.getRemoteAddressTCP().getAddress();
        int playerId = c.getID();

        // Find the game session that this player belongs to
        for (GameSession gameSession : gameSessions.values()) {
            if (gameSession.hasPlayer(playerId)) {
                return gameSession;
            }
        }

        // Game session not found, return null
        return null;
    }
    public void addGameSession(GameSession gameSession, int roomId) {

        gameSessions.put(roomId, gameSession);
    }
    public HashMap<Integer, GameSession> getGameSessions() {
        return gameSessions;
    }
}

