package org.example.gameSession.rooms;

import com.esotericsoftware.kryonet.Server;
import org.example.MyServer;
import org.example.gameSession.GameSession;
import org.example.messages.GameMode;

import java.io.IOException;
/*
 * Initialy I created for testing purposes. Turns out it's not needed.
 */
public class TestingRoom extends GameSession {
    public TestingRoom(GameMode.GameModes gameMode, int sessionId) throws IOException {
        super(gameMode, sessionId, new MyServer());
        System.out.println("TestingRoom created");
    }

}
