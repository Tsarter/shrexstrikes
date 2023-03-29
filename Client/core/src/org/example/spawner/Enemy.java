package org.example.spawner;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class Enemy extends ModelInstance {
    private final Vector3 velocity;
    private int health;

    public Enemy(ModelInstance instance) {
        super(instance);
        this.velocity = new Vector3(0, 0, -1f);
        this.health = 100;
    }

    public void update(float delta, Vector3 playerPosition) {
        // Calculate the direction to the player
        Vector3 direction = playerPosition.cpy().sub(transform.getTranslation(new Vector3())).nor();

        // Move the enemy towards the player
        Vector3 position = transform.getTranslation(new Vector3());
        position.add(direction.scl(velocity).scl(delta));
        transform.setTranslation(position);

        // TODO: Implement enemy behavior, such as attacking

        // TODO: Implement collision detection and health management
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
