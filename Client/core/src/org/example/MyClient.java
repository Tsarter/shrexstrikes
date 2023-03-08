package org.example;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class MyClient implements ApplicationListener {

    private final Client client;
    private Player[] playersList;
    public MyClient() throws IOException {

        client = new Client();  // initialize client
        Network.register(client);  // register all the classes that are sent over the network

        // Add listener to tell the client, what to do after something is sent over the network
        client.addListener(new Listener() {

            /**
             * We recieved something from the server.
             * In this case, it is probably a list of all players.
             */
            public void received(Connection connection, Object object) {
                if (object instanceof Player[]) {

                    // get the list of players
                    playersList = (Player[]) object;

                }
            }
        });

        /**
         * Connect the client to the server.
         * If server is on a local machine, "localhost" should be used as host.
         * Ports should be the same as in the server.
         */
        client.start();
        client.connect(5000, "localhost", 3000, 3001);

    }
    public ModelBatch modelBatch;
    public Model model;
    public ModelInstance groundModelInstance;
    private PerspectiveCamera camera;
    public Vector3 cameraPosition;
    public Vector3 cameraDirection;
    private float cameraAngle;
    public float cameraSpeed;
    private InputMultiplexer inputMultiplexer;
    private MyInputProcessor myInputProcessor = new MyInputProcessor(this);

    private ModelInstance playerModelInstance;
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
        cameraSpeed = 20;

        // set up the model batch for rendering
        modelBatch = new ModelBatch();

        // load the texture files
        Texture bodyTexture = new Texture(Gdx.files.internal("C:\\Users\\Tanel\\Documents\\AA_PROJECTS\\AA TalTech stuff\\ShrexStrikes\\Client\\assets\\Shrek_Body.png"));
        Texture headLegsTexture = new Texture(Gdx.files.internal("C:\\Users\\Tanel\\Documents\\AA_PROJECTS\\AA TalTech stuff\\ShrexStrikes\\Client\\assets\\Shrek_Head_Legs.png"));
        // create materials that reference the texture files
        Material bodyMaterial = new Material(TextureAttribute.createDiffuse(bodyTexture));
        Material headLegsMaterial = new Material(TextureAttribute.createDiffuse(headLegsTexture));


        // create a simple rectangle mesh for the player model
        Mesh playerMesh = new Mesh(true, 4, 6, VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
        playerMesh.setVertices(new float[] {
                -0.5f, 0, -0.5f, 0, 1, 0, 0, 0,
                0.5f, 0, -0.5f, 0, 1, 0, 1, 0,
                0.5f, 0,  0.5f, 0, 1, 0, 1, 1,
                -0.5f, 0,  0.5f, 0, 1, 0, 0, 1,
        });
        playerMesh.setIndices(new short[] {0, 1, 2, 2, 3, 0});

        // create a new Model for the player model
        Model playerModel = loader.loadModel(Gdx.files.internal("C:\\Users\\Tanel\\Documents\\AA_PROJECTS\\AA TalTech stuff\\ShrexStrikes\\Client\\assets\\Shrek.obj"));
        for (Mesh mesh : playerModel.meshes) {
            mesh.scale(0.01f, 0.01f, 0.01f);
        }
        playerModelInstance = new ModelInstance(playerModel);
        playerModelInstance.materials.get(1).set(bodyMaterial);
        playerModelInstance.materials.get(0).set(headLegsMaterial);


        //myInputProcessor = new MyInputProcessor(this);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(myInputProcessor);
        //inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        float delta = Gdx.graphics.getDeltaTime();
        // Update player movement
        myInputProcessor.updatePlayerMovement(delta);

        camera.position.set(cameraPosition);
        camera.lookAt(cameraPosition.x + cameraDirection.x, cameraPosition.y + cameraDirection.y, cameraPosition.z + cameraDirection.z);
        camera.update();

        // render the player model
        // scale the player model
        playerModelInstance.transform.setToTranslation(cameraPosition);
        playerModelInstance.transform.rotate(Vector3.Y, cameraAngle);


        modelBatch.begin(camera);
        modelBatch.render(groundModelInstance);
        //modelBatch.render(playerModelInstance);

        /**
         * If this player inputs anything, send it to the server
         */
        if (client.isConnected()) {
            // render all other players
            for (Player player : playersList) {
                playerModelInstance.transform.setToTranslation(player.x, player.y, 1);
                modelBatch.render(playerModelInstance);

            }

            /**
             * Send this movement to the server.
             * The server should move "my player" and then send the updated board to all players.
             * So they know that this client moved aswell.
             */
            Map<String, Integer> location = new HashMap<>();
            location.put("x", (int) cameraPosition.x);
            location.put("y", (int) cameraPosition.y);
            client.sendUDP(location);
        }
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
