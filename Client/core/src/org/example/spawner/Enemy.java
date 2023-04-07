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

        enemyInstance.transform.setToTranslation(X, 1, Y);

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
}
