package org.example;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import org.example.messages.MapBounds;
import org.example.messages.PlayerBullet;
import org.example.messages.PlayerHit;

import java.util.ArrayList;
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
        kryo.register(PlayerBullet.class);
        kryo.register(PlayerHit.class);
        kryo.register(MapBounds.class);
        kryo.register(ArrayList.class);
        kryo.register(com.badlogic.gdx.math.collision.BoundingBox.class);
        kryo.register(com.badlogic.gdx.math.Vector3.class);
    }
}
