package org.example.spawner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import org.example.MyGame;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import java.util.HashMap;

public class Enemy {
    public float X;
    public float Y;
    public float Z;
    public float rotation;
    public int health;
    public int id;
    public int type;
    private ModelInstance enemyInstance;
    float rotationSpeed = 20.0f;
    float movementSpeed = 2.0f;
    public Enemy(){}
    public Enemy(ModelInstance enemyInstance ,HashMap<String, ?> info) {
        this.id = (int) info.get("id");
        this.health = (int) info.get("health");
        this.X = (float) info.get("x");
        this.Y = (float) info.get("y");
        this.Z = (float) info.get("z");
        this.rotation = (float) info.get("rotation");
        ColorAttribute colorAttr = new ColorAttribute(ColorAttribute.Diffuse, Color.BLUE);
        Material material = enemyInstance.materials.get(1);
        material.set(colorAttr);

        this.enemyInstance = enemyInstance;

        enemyInstance.transform.translate(X, 1, Y);

    }
    public int getId() {
        return id;
    }

    public void update(HashMap<String, ?> info) {
        rotation = (float) info.get("rotation");
        X = (float) info.get("x");
        Y = (float) info.get("y");
        Z = (float) info.get("z");
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
    private void rotateEnemy() {
        float targetRotation = rotation;
        float enemyRotation = enemyInstance.transform.getRotation(new Quaternion()).getAngleAround(Vector3.Y);
        float rotationDiff = targetRotation - enemyRotation;
        float rotationSpeed = 5f * Gdx.graphics.getDeltaTime();
        if (Math.abs(rotationDiff) > 180f) {
            rotationDiff -= 360f * Math.signum(rotationDiff);
        }
        if (MathUtils.isEqual(enemyRotation, targetRotation, 0.4f)) {
            return; // stop rotating if already facing target within 0.1 degrees
        }
        float rotationAmount = rotationSpeed * Math.signum(rotationDiff);
        if (Math.abs(rotationAmount) > Math.abs(rotationDiff)) {
            rotationAmount = rotationDiff;
        }
        enemyRotation += rotationAmount;
        enemyInstance.transform.setToRotation(Vector3.Y, enemyRotation);
    }
}
