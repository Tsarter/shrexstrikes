package org.example.spawner;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import org.example.loader.ObjLoader;
import java.util.HashMap;

public class Enemy {
    private float coordinateX;
    private float coordinateY;
    private float coordinateZ;
    private float rotation;
    private int health;
    private int id;
    private int type;
    public ModelInstance enemyInstance;
    public Enemy(){}
    public Enemy(HashMap<String, ?> info) {
        this.id = (int) info.get("id");
        this.health = (int) info.get("health");
        this.coordinateX = (float) info.get("x");
        this.coordinateY = (float) info.get("y");
        this.coordinateZ = (float) info.get("z");
        this.rotation = (float) info.get("rotation");
        org.example.loader.ObjLoader objLoader = new org.example.loader.ObjLoader();
        this.enemyInstance = objLoader.loadShrek();
        enemyInstance.transform.translate(coordinateX, coordinateY, coordinateZ);
    }
    public int getId() {
        return id;
    }

    public void update(HashMap<String, ?> info) {
        rotation = (float) info.get("rotation");
        Vector3 enemyPosition = (Vector3) new Vector3((float) info.get("x"), (float) info.get("y"), (float) info.get("z"));
        enemyInstance.transform.translate(enemyPosition);
        enemyInstance.transform.rotate(0, 1, 0, rotation);

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
