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

    private List<GameSession> gameSessions;
    public Map<InetAddress, Integer> playerIds = new HashMap<InetAddress, Integer>();
    public MyServer myServer;
    public GameSessionManager(MyServer myServer) {
        this.myServer = myServer;
        // Initialize the list of game sessions
        gameSessions = new ArrayList<GameSession>();
        /*gameSessions.add(new PVPRoom(myServer));
        gameSessions.add(new TestingRoom(myServer));
        gameSessions.add(new ZombiesRoom(myServer));*/
    }

    public void addPlayerToGameSession(Player player, GameMode.GameModes gameMode) {
        // Find the game session that matches the specified game mode
        GameSession gameSession = getGameSessionByMode(gameMode);

        // Add the player to the game session
        gameSession.addPlayer(player);
    }
    public void removePlayerFromGameSession(Player player) {
        // Find the game session that this player belongs to and remove the player
        for (GameSession gameSession : gameSessions) {
            if (gameSession.hasPlayer(player.id)) {
                gameSession.removePlayer(player);
                // find to what Inetaddress the id is mapped to and remove it
                for (Map.Entry<InetAddress, Integer> entry : playerIds.entrySet()) {
                    if (entry.getValue().equals(player.id)) {
                        playerIds.remove(entry.getKey());
                        break;
                    }
                }
                break;
            }
        }
    }
    public void processData(Connection c, Object data) {
        // Find the game session that this player belongs to
        GameSession gameSession = getGameSessionByConnection(c);

        // Pass the data to the game session for processing
        gameSession.processData(data);
    }

    private GameSession getGameSessionByMode(GameMode.GameModes gameMode) {
        // Find the game session that matches the specified game mode
        for (GameSession gameSession : gameSessions) {
            if (gameSession.getGameMode().equals(gameMode)) {
                return gameSession;
            }
        }

        // Game session not found, return null
        return null;
    }

    private GameSession getGameSessionByConnection(Connection c) {
        // Find the player ID associated with this connection
        InetAddress address = c.getRemoteAddressTCP().getAddress();
        int playerId = playerIds.get(address);

        // Find the game session that this player belongs to
        for (GameSession gameSession : gameSessions) {
            if (gameSession.hasPlayer(playerId)) {
                return gameSession;
            }
        }

        // Game session not found, return null
        return null;
    }
    public void addGameSession(GameSession gameSession) {
        gameSessions.add(gameSession);
    }
}

