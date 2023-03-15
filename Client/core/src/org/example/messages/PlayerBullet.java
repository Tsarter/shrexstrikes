package org.example.messages;



public class PlayerBullet {
    // bullet position
    public float x, y, z;
    // bullet direction
    public float dx, dy, dz;
    // bullet speed
    public float speed;
    // bullet owner
    public int owner;
    public void set(float x, float y, float z, float dx, float dy, float dz, float speed, int owner) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.speed = speed;
        this.owner = owner;
    }
}
