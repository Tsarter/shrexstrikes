package org.example;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import org.example.messages.PlayerId;

import java.util.HashMap;

public class Network {

    /**
     * Register classes that are sent over the network.
     * This class should be equal in server and client.
     */
    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Player.class);
        kryo.register(Player[].class);
        kryo.register(HashMap.class);
        kryo.register(PlayerId.class);
    }
}
