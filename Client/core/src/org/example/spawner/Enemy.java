package org.example.spawner;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class Enemy {
    private float coordinateX;
    private float coordinateY;
    private float coordinateZ;
    private int health;
    private int id;
    private int type;
    private ModelInstance enemyInstance;
    public Enemy(){}
    public Enemy(ModelInstance enemyInstance) {
        this.enemyInstance = enemyInstance;
    }
    public int getId() {
        return id;
    }

    public void update(Vector3 enemyPosition, float enemyDirection) {

        enemyInstance.transform.translate(enemyPosition);
        enemyInstance.transform.rotate(0, 1, 0, enemyDirection);

    }

    public void setHealth(int health) {
        this.health = health;
    }
    public ModelInstance getEnemyInstance() {
        return enemyInstance;
    }
    public void loadEnemyInstance(ModelInstance enemyInstance) {
        this.enemyInstance = enemyInstance;
    }
    public void loadShrexInstance() {
        /*AssetManager manager = new AssetManager();
        manager.load("shrex.obj", ModelInstance.class);
        manager.finishLoading();*/
    }
}
