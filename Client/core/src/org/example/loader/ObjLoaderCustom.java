package org.example.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import org.example.MyGame;

public class ObjLoaderCustom {
    ModelLoader loader = new com.badlogic.gdx.graphics.g3d.loader.ObjLoader();
    MyGame game;
    Model shrexModel;

    public ObjLoaderCustom(MyGame game) {
        this.game = game;
        shrexModel =  game.getAssetManager().get("characters/Shrek/Shrek.obj", Model.class);
    }
    public ModelInstance loadShrek() {
        for (Mesh mesh : shrexModel.meshes) {
            mesh.scale(0.01f, 0.01f, 0.01f);
        }
        // load the texture files
        Texture bodyTexture = game.getAssetManager().get("Shrek_Body.png");
        Texture headLegsTexture = game.getAssetManager().get("Shrek_Head_Legs.png");
        // create materials that reference the texture files
        Material bodyMaterial = new Material(TextureAttribute.createDiffuse(bodyTexture));
        Material headLegsMaterial = new Material(TextureAttribute.createDiffuse(headLegsTexture));

        ModelInstance playerModelInstance = new ModelInstance(shrexModel);
        playerModelInstance.transform.setToTranslation(0, 1, 0);
        playerModelInstance.materials.get(1).set(bodyMaterial);
        playerModelInstance.materials.get(0).set(headLegsMaterial);
        return playerModelInstance;
    }
    public ModelInstance loadFiona() {
        String folder = "characters/fiona/";
        Model playerModel = loader.loadModel(Gdx.files.internal(  "characters/fiona/fiona.obj"));
        // load the texture files
        Texture bodyTexture = new Texture(Gdx.files.internal(folder + "sss.png"));
        Texture headLegsTexture = new Texture(Gdx.files.internal(folder + "djddk.png"));
        // create materials that reference the texture files
        Material bodyMaterial = new Material(TextureAttribute.createDiffuse(bodyTexture));
        Material headLegsMaterial = new Material(TextureAttribute.createDiffuse(headLegsTexture));

        ModelInstance playerModelInstance = new ModelInstance(playerModel);
        playerModelInstance.transform.setToTranslation(0, 1, 0);
        playerModelInstance.materials.get(1).set(bodyMaterial);
        playerModelInstance.materials.get(0).set(headLegsMaterial);
        return playerModelInstance;
    }
    public ModelInstance loadMap() {

        return null;
    }
}
