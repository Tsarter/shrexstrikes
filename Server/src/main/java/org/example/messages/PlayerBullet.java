package org.example.messages;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
public class PlayerBullet {
    // bullet position
    public float x, y, z;
    // bullet direction
    public float dx, dy, dz;
    // bullet speed
    public float speed;
    // bullet owner
    public int owner;

    public static BoundingBox getBoundingBox() {
        return new BoundingBox(new Vector3(0,0,0), new Vector3(0.1f,0.1f,0.1f));
    }
    public Vector3 getPosition() {
        return new Vector3(x, y, z);
    }
    public Vector3 getDirection() {
        return new Vector3(dx, dy, dz);
    }
}
