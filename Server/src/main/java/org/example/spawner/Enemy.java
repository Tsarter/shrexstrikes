package org.example.spawner;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import org.example.Player;

public class Enemy extends ModelInstance {

    public int health;
    public int id;
    public float x;
    public float y;
    public float z;
    public float rotation;
    public String type;
    private Vector3 position = new Vector3();
    private float speed = 1f;

    public Enemy(ModelInstance instance, Vector3 position, float orientation, int id, float speed) {
        super(instance);
        this.health = 100;
        this.speed = speed;
        this.id = id;
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
        this.rotation = orientation;
        this.transform.translate(position);
        this.transform.rotate(0, 1, 0, orientation);
    }

    public void update(Player player, float delta) {
        if (player != null) {
            // if the enemy is close to the player, stop moving
            if (Math.abs(player.x - this.x) < 1 && Math.abs(player.z - this.z) < 1) {
                return;
            }
            // Get the position of the player
            Vector3 playerPosition = new Vector3(player.x, player.y, player.z);

            // rotate the enemy towards the player ignore the y axis
            rotation = (float) Math.atan2(playerPosition.x - this.x, playerPosition.z - this.z);
            // Rad to degrees
            rotation = (float) Math.toDegrees(rotation);
            // Move the enemy towards the player

            Vector3 enemyPosition = new Vector3(this.position);
            Vector3 direction = playerPosition.cpy().sub(enemyPosition).nor();
            enemyPosition.add(direction.scl(speed));
            this.position = enemyPosition;
            //this.position.y = 0.6f;
            this.x = enemyPosition.x;
            this.y = position.y;
            this.z = enemyPosition.z;
            System.out.println("Player position: " + playerPosition);
            // TODO: Implement enemy behavior, such as attacking

            // TODO: Implement collision detection and health management
        }
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
