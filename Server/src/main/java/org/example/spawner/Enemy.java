package org.example.spawner;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import org.example.Player;

public class Enemy extends ModelInstance {
    private final Vector3 velocity;
    public int health;
    public int id;
    public float x;
    public float y;
    public float z;
    public float rotation;
    public String type;

    public Enemy(ModelInstance instance, Vector3 position, float orientation, int id) {
        super(instance);
        this.velocity = new Vector3(1f, 0, 1f);
        this.health = 100;
        this.id = id;
        this.transform.translate(position);
        this.transform.rotate(0, 1, 0, orientation);
    }

    public void update(Player player, float delta) {
        if (player != null) {
            // Get the position of the player
            Vector3 playerPosition = new Vector3(player.x, player.y, player.z);

            // rotate the enemy towards the player ignore the y axis
            float rotation = (float) Math.atan2(playerPosition.x - this.transform.getTranslation(new Vector3()).x, playerPosition.z - this.transform.getTranslation(new Vector3()).z);
            System.out.println("Rotation" + rotation);

            // Move the enemy towards the player
            // this.transform.translate(velocity.scl(delta / 1000f));

            // print the position of the enemy
            System.out.println("EnemyPosition" + this.transform.getTranslation(new Vector3()));
            System.out.println("enemyRotation" + this.transform.getRotation(new Quaternion()));
            System.out.println("PlayerPosition" + playerPosition);
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
