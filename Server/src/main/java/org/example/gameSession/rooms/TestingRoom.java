package org.example.gameSession.rooms;

import com.esotericsoftware.kryonet.Server;
import org.example.MyServer;
import org.example.gameSession.GameSession;
import org.example.messages.GameMode;

public class TestingRoom extends GameSession {

    public TestingRoom(MyServer myServer) {
        super(GameMode.GameModes.TESTING);
    }

}
