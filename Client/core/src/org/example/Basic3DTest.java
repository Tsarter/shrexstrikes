/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.example;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector3;


/**
 * See: http://blog.xoppa.com/basic-3d-using-libgdx-2/
 * @author Xoppa
 */
public class Basic3DTest implements ApplicationListener {
    public ModelBatch modelBatch;
    public Model model;

    public ModelInstance instance;
    public AssetManager assetManager;
    public ModelInstance modelInstance;
    public ModelInstance groundModelInstance;
    private PerspectiveCamera camera;
    public Vector3 cameraPosition;
    public Vector3 cameraDirection;
    private float cameraAngle;
    public float cameraSpeed;
    private InputMultiplexer inputMultiplexer;

    @Override
    public void create() {
        // load the 3D model of the map
        ModelLoader loader = new ObjLoader();
        Model mapModel = loader.loadModel(Gdx.files.internal("C:\\Users\\Tanel\\Documents\\AA_PROJECTS\\AA TalTech stuff\\ShrexStrikes\\Client\\assets\\mapBasic.obj"));
        groundModelInstance = new ModelInstance(mapModel);

        // create a perspective camera to view the game world
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraPosition = new Vector3(0, 1, 0);
        cameraDirection = new Vector3(0, 0, -1);
        cameraAngle = 0;
        cameraSpeed = 1;

        // set up the model batch for rendering
        modelBatch = new ModelBatch();

        MyInputProcessor myInputProcessor = new MyInputProcessor(this);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(myInputProcessor);
        //inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.position.set(cameraPosition);
        camera.lookAt(cameraPosition.x + cameraDirection.x, cameraPosition.y + cameraDirection.y, cameraPosition.z + cameraDirection.z);
        camera.update();

        modelBatch.begin(camera);
        modelBatch.render(groundModelInstance);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        model.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

}
class MyInputProcessor implements InputProcessor {
    // Implement input event handling methods here...
    private Basic3DTest basic3DTest;

    public MyInputProcessor(Basic3DTest basic3DTest) {
        this.basic3DTest = basic3DTest;
    }
    @Override
    public boolean keyDown(int keycode) {
        // update player movement based on key input
        switch (keycode) {
            case Input.Keys.W:
                basic3DTest.cameraPosition.add(basic3DTest.cameraDirection.scl(basic3DTest.cameraSpeed));
                break;
            case Input.Keys.S:
                basic3DTest.cameraPosition.sub(basic3DTest.cameraDirection.scl(basic3DTest.cameraSpeed));
                break;
            case Input.Keys.A:
                basic3DTest.cameraDirection.rotate(Vector3.Y, 5);
                break;
            case Input.Keys.D:
                basic3DTest.cameraDirection.rotate(Vector3.Y, -5);
                break;
            default:
                break;
        }
        return false;
    }
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
    @Override
    public boolean scrolled(float screenX, float screenY) {
        return false;
    }
}