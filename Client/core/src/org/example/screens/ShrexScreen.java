package org.example.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.example.MyGame;
import org.example.MyInputProcessor;
import org.example.Network;
import org.example.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShrexScreen implements ApplicationListener,Screen {
    private MyGame myGame;
    public ShrexScreen(MyGame myGame) {
        this.myGame = myGame;
    }
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
    private Model playerModel;
    private Material headLegsMaterial;
    private Material bodyMaterial;
    private MenuScreen menuScreen;
    private Game game;
    @Override
    public void create() {

        // load the 3D model of the map
        ModelLoader loader = new ObjLoader();
        Model mapModel = loader.loadModel(Gdx.files.internal("assets/mapBasic.obj"));
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
        Texture bodyTexture = new Texture(Gdx.files.internal("assets/Shrek_Body.png"));
        Texture headLegsTexture = new Texture(Gdx.files.internal("assets/Shrek_Head_Legs.png"));
        // create materials that reference the texture files
        bodyMaterial = new Material(TextureAttribute.createDiffuse(bodyTexture));
        headLegsMaterial = new Material(TextureAttribute.createDiffuse(headLegsTexture));


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
        playerModel = loader.loadModel(Gdx.files.internal("assets/Shrek.obj"));
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
        //playerModelInstance.transform.translate(cameraPosition);
        //playerModelInstance.transform.rotate(Vector3.Y, cameraAngle);


        modelBatch.begin(camera);
        modelBatch.render(groundModelInstance);
        //modelBatch.render(playerModelInstance);

        /**
         * If player is connected to the server, render all other players.
         */
        if (client.isConnected()) {
            // render all other players
            for (Player player : playersList) {
                // create a new instance of the player model for this player
                ModelInstance otherPlayerModelInstance = new ModelInstance(playerModel);
                Vector3 playerPosition = new Vector3(player.x, 0, player.z);
                // set the position and orientation of the player model instance
                otherPlayerModelInstance.transform.translate(playerPosition);
                otherPlayerModelInstance.transform.rotate(Vector3.Y, player.rotation);
                // set the material for each mesh in the player model instance
                otherPlayerModelInstance.materials.get(0).set(headLegsMaterial);
                otherPlayerModelInstance.materials.get(1).set(bodyMaterial);

                // render the player model instance
                modelBatch.render(otherPlayerModelInstance);

            }

            /**
             * Send this movement to the server.
             * The server should move "my player" and then send the updated board to all players.
             * So they know that this client moved aswell.
             */
            Map<String, Float> location = new HashMap<>();
            location.put("x",  cameraPosition.x);
            location.put("z",  cameraPosition.z);
            // get the angle of the camera direction
            float rotation = (float) Math.toDegrees(Math.atan2(cameraDirection.x, cameraDirection.z));
            location.put("rotation",  rotation);
            System.out.println("Sending location: " + location);
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
    @Override
    public void show() {
    }


    @Override
    public void hide() {
    }
    @Override
    public void render(float delta) {
        render();
    }

}
