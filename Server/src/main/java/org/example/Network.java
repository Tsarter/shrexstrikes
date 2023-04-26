package org.example;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import org.example.gameSession.rooms.zombies.spawner.Enemy;
import org.example.messages.*;

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
        kryo.register(Enemies.class);
        kryo.register(EnemyHit.class);
        kryo.register(GameMode.class);
        kryo.register(GameMode.GameModes.class);
        kryo.register(GameStateChange.class);
        kryo.register(GameStateChange.GameStates.class);
        kryo.register(Enemy.class);
        kryo.register(Array.class);
        kryo.register(Object[].class);
        kryo.register(Player.Character.class);
        kryo.register(BoundingBox.class);

    }
}
