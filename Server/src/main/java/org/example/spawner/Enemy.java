package org.example.spawner;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.esotericsoftware.kryonet.Server;
import org.example.Player;
import org.example.messages.PlayerHit;

public class Enemy extends ModelInstance {

    public int health;
    public int id;
    public float x;
    public float y;
    public float z;
    public float rotation;
    public int damage;
    public String type;
    private Vector3 position = new Vector3();
    private float speed = 1f;
    private BoundingBox boundingBox;
    private Vector3 boundsSize = new Vector3(0.4f, 1f, 0.4f);
    private Server server;
    public Enemy(ModelInstance instance, Vector3 position, float orientation, int id, float speed, Server server) {
        super(instance);
        this.server = server;
        this.health = 100;
        this.speed = speed;
        this.damage = 5;
        this.id = id;
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
        this.rotation = orientation;
        this.transform.translate(position);
        this.transform.rotate(0, 1, 0, orientation);
        this.boundingBox = new BoundingBox(new Vector3(this.x - boundsSize.x, this.y - boundsSize.y, this.z - boundsSize.z),
                new Vector3(this.x + boundsSize.x, this.y + boundsSize.y, this.z + boundsSize.z));
    }

    public void update(Player player, float delta) {
        if (player != null) {
            // if the enemy is close to the player, deal damage to the player and stop moving
            if (Math.abs(player.x - this.x) < 1 && Math.abs(player.z - this.z) < 1) {
                player.health -= damage;
                PlayerHit playerHit = new PlayerHit(player.id, -1, damage);
                server.sendToAllTCP(playerHit);
                return;
            }
            // Get the position of the player
            Vector3 playerPosition = new Vector3(player.x, player.y, player.z);

            // rotate the enemy towards the player ignore the y axis
            rotation = (float) Math.atan2(playerPosition.x - this.x, playerPosition.z - this.z);
            // Rad to degrees
            rotation = (float) Math.toDegrees(rotation);
            // Move the enemy towards the player

            Vector3 enemyPosition = new Vector3(this.x, this.y, this.z);
            Vector3 direction = playerPosition.cpy().sub(enemyPosition).nor();
            enemyPosition.add(direction.scl(speed));
            this.position = enemyPosition;
            this.position.y = 0.6f;
            this.x = enemyPosition.x;
            this.y = position.y;
            this.z = enemyPosition.z;
            // Update the bounding box
            this.boundingBox = new BoundingBox(new Vector3(this.x - boundsSize.x, this.y - boundsSize.y, this.z - boundsSize.z),
                    new Vector3(this.x + boundsSize.x, this.y + boundsSize.y, this.z + boundsSize.z));

            // TODO: Implement enemy behavior, such as attacking

        }
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
}
