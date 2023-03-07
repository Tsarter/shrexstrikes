package org.example;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

import java.util.Scanner;

import com.badlogic.gdx.graphics.GL20;



public class MyClient implements ApplicationListener {

    private final Client client;

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
                    Player[] playersList = (Player[]) object;

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

        modelBatch.begin(camera);
        modelBatch.render(groundModelInstance);
        modelBatch.end();
        /**
         * If this player inputs anything, send it to the server
         */
        if (client.isConnected() && false) {
            // this is like input() in python.
            Scanner scanner = new Scanner(System.in);
            String input = scanner.next();
            Character direction = input.charAt(0);  // We only get the first character.
            /**
             * Send this movement to the server.
             * The server should move "my player" and then send the updated board to all players.
             * So they know that this client moved aswell.
             */
            client.sendUDP(direction);
        }
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
