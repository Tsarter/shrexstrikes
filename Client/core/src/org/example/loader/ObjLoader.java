package org.example.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;

import java.util.List;

public class ObjLoader {
    ModelLoader loader = new com.badlogic.gdx.graphics.g3d.loader.ObjLoader();
    public ModelInstance loadShrek() {
        Model playerModel = loader.loadModel(Gdx.files.internal("C:\\Users\\Tanel\\Documents\\AA_PROJECTS\\AA TalTech stuff\\ShrexStrikes\\Client\\assets\\Shrek.obj"));
        for (Mesh mesh : playerModel.meshes) {
            mesh.scale(0.01f, 0.01f, 0.01f);
        }
        // load the texture files
        Texture bodyTexture = new Texture(Gdx.files.internal("assets/Shrek_Body.png"));
        Texture headLegsTexture = new Texture(Gdx.files.internal("assets/Shrek_Head_Legs.png"));
        // create materials that reference the texture files
        Material bodyMaterial = new Material(TextureAttribute.createDiffuse(bodyTexture));
        Material headLegsMaterial = new Material(TextureAttribute.createDiffuse(headLegsTexture));

        ModelInstance playerModelInstance = new ModelInstance(playerModel);
        playerModelInstance.transform.setToTranslation(0, 1, 0);
        playerModelInstance.materials.get(1).set(bodyMaterial);
        playerModelInstance.materials.get(0).set(headLegsMaterial);
        return playerModelInstance;
    }
    public ModelInstance loadFiona() {
        String folder = "assets/characters/fiona/";
        Model playerModel = loader.loadModel(Gdx.files.internal(  "assets/characters/fiona/fiona.obj"));
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
