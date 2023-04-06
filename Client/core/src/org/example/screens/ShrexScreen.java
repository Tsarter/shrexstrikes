package org.example.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;


import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.example.MyGame;
import org.example.MyInputProcessor;
import org.example.Network;
import org.example.Player;
import org.example.loader.ObjLoaderCustom;
import org.example.messages.Enemies;
import org.example.messages.MapBounds;
import org.example.messages.PlayerBullet;
import org.example.messages.PlayerHit;
import org.example.spawner.Enemy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShrexScreen implements ApplicationListener,Screen {
    final static short GROUND_FLAG = 1 << 8;
    final static short OBJECT_FLAG = 1 << 9;
    final static short ALL_FLAG = -1;
    private MyGame myGame;

    private final Client client;
    private Player[] playersList;
    private Player player;
    private HashMap<Integer, Enemy> enemies = new HashMap<Integer, Enemy>();
    private boolean gameStarted = false;
    private ModelInstance templateEnemyModelInstance;
    public ShrexScreen(MyGame myGame) throws IOException {
        this.myGame = myGame;
        client = new Client(50000, 50000);  // initialize client
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
                // we recieved the server created player object
                else if (object instanceof Player) {
                    player = (Player) object;
                }
                // we recieved playerHit
                else if (object instanceof PlayerHit) {
                    PlayerHit playerHit = (PlayerHit) object;
                    // if the player that was hit is the current player
                    if (playerHit.idOfPlayerHit == player.id) {
                        // update the health
                        player.health -= 10;
                    }
                }
                else if (object instanceof Enemies) {

                        if (gameStarted) {
                            System.out.println("Enemies received");
                            Enemies enemiesInfo = (Enemies) object;
                            for (Map.Entry<Integer, HashMap> entry : enemiesInfo.enemies.entrySet()) {
                                if (enemies.containsKey(entry.getKey())) {
                                    enemies.get(entry.getKey()).update(entry.getValue());
                                } else if (templateEnemyModelInstance != null){
                                    Model model = new Model();
                                    ModelInstance enemyInstance = templateEnemyModelInstance.copy();
                                    enemies.put(entry.getKey(), new Enemy(enemyInstance, entry.getValue()));
                                }

                            }
                        }

                }


            }
        });

        /**
         * Connect the client to the server.
         * If server is on a local machine, "localhost" should be used as host.
         * Ports should be the same as in the server.193.40.156.227
         */
        client.start();
        client.connect(5000, "localhost", 8080, 8081);

    }
    // gets called when collision is detected
    class MyContactListener extends ContactListener {
        @Override
        public boolean onContactAdded (int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
            System.out.println("Collision detected onContactAdded");
            return true;
        }

    }


    MyContactListener contactListener;
    public ModelBatch modelBatch;
    public Model model;
    public ModelInstance groundModelInstance;
    public PerspectiveCamera camera;
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

    private DirectionalShadowLight shadowLight;
    private ModelBatch shadowBatch;
    private Environment environment = new Environment();

    private List<BoundingBox> mapBounds;
    private BoundingBox playerBounds;
    private Stage stage;
    private Image crosshair;
    private Label healthLabel;

    public float zoomOnRightClickAmount = 30;
    public float zoom = 67;
    public float fieldOfView = 67;


    @Override
    public void create() {
        Bullet.init();
        // load the 3D model of the map
        //ModelLoader loader = new ObjLoaderCustom();
        ModelLoader loader = new ObjLoader();
        Model mapModel = myGame.getAssetManager().get("assets/maps/City/MediEvalCity.g3db");
        groundModelInstance = new ModelInstance(mapModel);
        groundModelInstance.transform.setToTranslation(0, 0.5f, 0);


        // create a perspective camera to view the game world
        camera = new PerspectiveCamera(zoom, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraPosition = new Vector3(0, 1.5f, 0);
        camera.position.set(cameraPosition);
        cameraDirection = new Vector3(0, 0, -1);
        camera.direction.set(cameraDirection);
        camera.near = 0.2f;
        camera.far = 100f;
        cameraAngle = 0;
        cameraSpeed = 6;
        // set up the model batch for rendering
        modelBatch = new ModelBatch();


        // create a directional light for casting shadows
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        shadowLight = new DirectionalShadowLight(10024, 10024, 60f, 60f, 1f, 300f);
        shadowLight.set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f);
        environment.add(shadowLight);
        environment.shadowMap = shadowLight;

        // create a shadow batch for rendering shadows
        shadowBatch = new ModelBatch(new DepthShaderProvider());
        // make the groundModelInstance green
        Material groundMaterial = new Material();
        groundMaterial.set(ColorAttribute.createDiffuse(0.4f, 1, 0.4f, 1f)); // diffuse color


        // create a new Model for the player model
        // My custom ObjLoaderCustom (load fiona or shrex)
        ObjLoaderCustom objLoaderCustom = new ObjLoaderCustom(myGame);
        // Load shrex model
        playerModelInstance = objLoaderCustom.loadShrek();
        templateEnemyModelInstance = playerModelInstance.copy();
        // Load all the walking shrek frames
        /*ModelInstance[] animationFrames = new ModelInstance[260];
        for (int i = 1; i < 250; i++) {
            String counter = "00000" + String.valueOf(i);
            int counterMaxLength = 6;
            counter = counter.substring(counter.length() - counterMaxLength);
            Model model = loader.loadModel(Gdx.files.internal("assets/characters/WalkingShrek/walkingShrek_" + counter + ".obj"));
            animationFrames[i] = new ModelInstance(playerModel);
        }*/



        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(myInputProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);

        playerBounds = new BoundingBox();
        //new ModelInstance(playerModel).calculateBoundingBox(playerBounds);
        // set cylinder as player bounds
        playerBounds.set(new Vector3(-0.1f, 0.3f, -0.1f), new Vector3(0.1f, 1, 0.1f));
        player.boundingBox = playerBounds;
        mapBounds = new ArrayList<>();
        for (Node node : mapModel.nodes) {
            if (node.id.contains("Ground") || node.id.contains("Grass") || node.id.contains("Red") || node.id.contains("White")) {
                continue;
            }
            BoundingBox box = new BoundingBox();
            node.calculateBoundingBox(box);
            mapBounds.add(box);
        }
        MapBounds mapBoundsObject = new MapBounds();
        mapBoundsObject.boundingBox = mapBounds;

        client.sendTCP(mapBoundsObject);
        client.sendTCP(player);
        // Initialize collsion between the map and the player
        //initializeCollision(mapModel, playerModel);

        //collisionWorld.addCollisionObject(obj.body, OBJECT_FLAG, GROUND_FLAG);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
        gameStarted = true;
    }

    /**
     * Screen has its render(float delta) and applicationListener has its render()
      * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        render();
        // update healt
        healthLabel.setText("Health: " + player.health);
        // Render the crosshair
        stage.act(delta);
        stage.draw();
    }
    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();
        // Save the player's and cameras position
        Vector3 oldPos = playerModelInstance.transform.getTranslation(new Vector3());
        Vector3 oldCamPos = camera.position.cpy();

        myInputProcessor.updatePlayerMovement(delta);

        camera.position.set(cameraPosition);

        // update the transform of the playerModelInstance
        float playerModelRotation = (float) Math.toDegrees(Math.atan2(cameraDirection.x, cameraDirection.z));
        playerModelInstance.transform.set(new Vector3(cameraPosition.x, cameraPosition.y - 1f, cameraPosition.z), new Quaternion().set(Vector3.Y, playerModelRotation), new Vector3(1f, 1f, 1f));



        // Check if the new position of the player is colliding with the map
        Vector3 newPos = playerModelInstance.transform.getTranslation(new Vector3());
        playerBounds = new BoundingBox();
        playerBounds.set(new Vector3(newPos.x - 0.1f, newPos.y + 0.5f, newPos.z - 0.1f), new Vector3(newPos.x + 0.1f, newPos.y + 0.7f, newPos.z + 0.1f));

        // Check for collisions with the map
        for (BoundingBox bounds : mapBounds) {
            if (bounds.intersects(playerBounds)) {
                // The player has collided with an object in the map
                // Move the player back to their prevous position or prevent further movement
                playerModelInstance.transform.setTranslation(oldPos);
                camera.position.set(oldCamPos);
                cameraPosition.set(oldCamPos);
                System.out.println("Collision detected");
                break;
            }
        }

        //camera.lookAt(cameraPosition.x + cameraDirection.x, cameraPosition.y + cameraDirection.y, cameraPosition.z + cameraDirection.z);
        camera.direction.set(cameraDirection);
        camera.update();
        // Render the player and the ground
        shadowLight.begin(Vector3.Zero, camera.direction);
        shadowBatch.begin(shadowLight.getCamera());
        modelBatch.begin(camera);
        shadowBatch.render(groundModelInstance);
        modelBatch.render(groundModelInstance, environment);
        modelBatch.render(playerModelInstance);
        shadowBatch.render(playerModelInstance);

        // Render enemies
        for (Map.Entry<Integer, Enemy> entry : enemies.entrySet()) {
            Enemy enemy = entry.getValue();
            ModelInstance enemyModelInstance = enemy.getEnemyInstance();
            enemyModelInstance.transform.setToRotation(Vector3.Y, enemy.rotation);
            enemyModelInstance.transform.translate(enemy.X, enemy.Y, enemy.Z);
            modelBatch.render(enemyModelInstance, environment);
            shadowBatch.render(enemyModelInstance);
        }
        /**
         * If player is connected to the server, render all other players.
         */
        if (client.isConnected()) {
            // render all other players
            for (Player otherPlayer : playersList) {
                // don't render the player if they are the same as the current playerd
                if (player.id != otherPlayer.id) {
                // create a new instance of the player model for this player
                ModelInstance otherPlayerModelInstance = templateEnemyModelInstance.copy();
                Vector3 playerPosition = new Vector3(otherPlayer.x, -0.4f, otherPlayer.z);

                // set the position and orientation of the player model instance
                otherPlayerModelInstance.transform.translate(playerPosition);
                otherPlayerModelInstance.transform.rotate(Vector3.Y, otherPlayer.rotation);

                // render the player model instance and its shadow
                modelBatch.render(otherPlayerModelInstance, environment);
                shadowBatch.render(otherPlayerModelInstance);
                }

            }

            /**
             * Send this movement to the server.
             * The server should move "my player" and then send the updated board to all players.
             * So they know that this client moved aswell.
             */
            player.x = cameraPosition.x;
            player.z = cameraPosition.z;
            player.rotation = (float) Math.toDegrees(Math.atan2(cameraDirection.x, cameraDirection.z));
            player.boundingBox = playerBounds;
            client.sendUDP(player);

        }
        shadowBatch.end();
        shadowLight.end();
        modelBatch.end();
        int k = 9;

    }

    @Override
    public void dispose() {
        shadowBatch.dispose();
        modelBatch.dispose();
        model.dispose();
        contactListener.dispose();
        playerModel.dispose();

    }

    @Override
    public void resize(int width, int height) {
        // update the camera
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
        // use orthographic projection for the crosshair


    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
    @Override
    public void show() {
        stage = new Stage();
        // Add text to the stage to display the player's health
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        healthLabel = new Label("Health: " + player.health, labelStyle);

        // Create the crosshair image and center it on the screen
        Texture texture = new Texture("assets/crosshair-icon.png");
        crosshair = new Image(texture);
        // size the crosshair to 50x50 pixels
        crosshair.setSize(25, 25);
        crosshair.setPosition(
                Gdx.graphics.getWidth() / 2 - crosshair.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - crosshair.getHeight() / 2);
        healthLabel.setPosition(10, Gdx.graphics.getHeight() - 20);
        // Add the health label to the stage
        stage.addActor(healthLabel);

        // Add the crosshair to the stage
        stage.addActor(crosshair);

    }
    @Override
    public void hide() {
        stage.dispose();
    }
    public void shootBullet() {
        // create a new bullet
        PlayerBullet bullet = new PlayerBullet();
        bullet.set(cameraPosition.x, cameraPosition.y, cameraPosition.z, cameraDirection.x, cameraDirection.y, cameraDirection.z, 100, player.id);
        client.sendUDP(bullet);

    }

    public void initializeCollision(Model mapModel, Model playerModel) {
//        btTriangleMesh triangleMapMesh = new btTriangleMesh();
//        btBvhTriangleMeshShape mapShape;
//
//        // create collisonWorld
//        collisionConfiguration = new btDefaultCollisionConfiguration();
//        dispatcher = new btCollisionDispatcher(collisionConfiguration);
//        broadphase = new btDbvtBroadphase();
//        collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfiguration);
//        // create a collision shape for the map
//        for (Mesh mesh : mapModel.meshes) {
//            float[] vertices = new float[mesh.getNumVertices() * 3];
//            mesh.getVertices(vertices);
//            short[] indices = new short[mesh.getNumIndices()];
//            mesh.getIndices(indices);
//
//            for (int i = 0; i < indices.length; i += 3) {
//                Vector3 vertex1 = new Vector3(vertices[indices[i] * 3], vertices[indices[i] * 3 + 1], vertices[indices[i] * 3 + 2]);
//                Vector3 vertex2 = new Vector3(vertices[indices[i + 1] * 3], vertices[indices[i + 1] * 3 + 1], vertices[indices[i + 1] * 3 + 2]);
//                Vector3 vertex3 = new Vector3(vertices[indices[i + 2] * 3], vertices[indices[i + 2] * 3 + 1], vertices[indices[i + 2] * 3 + 2]);
//
//                triangleMapMesh.addTriangle(vertex1, vertex2, vertex3);
//            }
//        }
//        mapShape = new btBvhTriangleMeshShape(triangleMapMesh, true);
//        mapBody = new btRigidBody(0, null, mapShape);
//        collisionWorld.addCollisionObject(mapBody, GROUND_FLAG, ALL_FLAG);
//
//        // create a collision shape for the player and add it to the collisionWorld
//        btCollisionShape playerShape = new btBoxShape(new Vector3(1, 2, 1)); // Example box shape for the player
//        playerBody = new btRigidBody(1, null, playerShape);
//        collisionWorld.addCollisionObject(playerBody, OBJECT_FLAG, ALL_FLAG);

    }

}
