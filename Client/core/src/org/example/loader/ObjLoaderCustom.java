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
    Model gunModel;

    public ObjLoaderCustom(MyGame game) {
        this.game = game;
        game.getAssetManager().unload("characters/Shrek/Shrek.obj");
        game.getAssetManager().load("characters/Shrek/Shrek.obj", Model.class);
        game.getAssetManager().finishLoading();
        shrexModel = game.getAssetManager().get("characters/Shrek/Shrek.obj", Model.class);

        game.getAssetManager().unload("guns/sci-fi-gun/sci fi m254 gun - high poly.obj");
        game.getAssetManager().load("guns/sci-fi-gun/sci fi m254 gun - high poly.obj", Model.class);
        game.getAssetManager().finishLoading();
        gunModel = game.getAssetManager().get("guns/sci-fi-gun/sci fi m254 gun - high poly.obj", Model.class);
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
    public ModelInstance loadGun() {
        Model gunModel = game.getAssetManager().get("guns/sci-fi-gun/sci fi m254 gun - high poly.obj", Model.class);
        ModelInstance gunInstance = new ModelInstance(gunModel);
        for (Mesh mesh : gunModel.meshes) {
            mesh.scale(0.03f, 0.03f, 0.03f);
        }
        gunInstance.transform.setToTranslation(0, 0, 0);

        return gunInstance;
    }
}
