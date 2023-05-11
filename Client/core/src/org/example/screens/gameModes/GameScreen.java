package org.example.screens.gameModes;

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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.example.MyGame;
import org.example.MyInputProcessor;
import org.example.Player;
import org.example.animations.Pulse;
import org.example.loader.ObjLoaderCustom;
import org.example.messages.*;
import org.example.spawner.Enemy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.badlogic.gdx.math.MathUtils.lerp;

public class GameScreen implements ApplicationListener,Screen {
    protected MyGame myGame;


    public HashMap<Integer, Enemy> enemies = new HashMap<Integer, Enemy>();
    private boolean created = false;
    protected ModelInstance templateEnemyModelInstance;
    public List<Enemy> enemiesToHide = new ArrayList<Enemy>();
    public GameScreen(MyGame myGame) throws IOException {
        this.myGame = myGame;

    }
    public GameScreen(){
    }
    public ModelBatch modelBatch;
    public Model model;
    public ModelInstance groundModelInstance;
    public PerspectiveCamera camera;
    public Vector3 cameraPosition;
    public Vector3 cameraDirection;
    private float cameraAngle;
    public float cameraSpeed;
    protected InputMultiplexer inputMultiplexer;
    protected MyInputProcessor myInputProcessor;
    protected ModelInstance playerModelInstance;

    protected DirectionalShadowLight shadowLight;
    protected ModelBatch shadowBatch;
    protected Environment environment;

    protected List<BoundingBox> mapBounds;
    protected BoundingBox playerBounds;
    protected Stage stage;
    protected Image crosshair;
    protected Label healthLabel;
    private Label ammoLabel;
    protected Label scoreLabel;
    protected Label waveLabel;
    protected Image gunHud;
    protected Label enemiesRemainingLabel;
    protected int score = 0;
    protected int currentWave = 0;
    private int enemiesRemaining = 0;
    public float zoom = 67;
    Map<Integer, Float> previousRotations;
    @Override
    public void create() {
        Bullet.init();
        previousRotations = new HashMap<Integer, Float>();
        // load the 3D model of the map
        //ModelLoader loader = new ObjLoaderCustom();
        ModelLoader loader = new ObjLoader();
        Model mapModel = myGame.getAssetManager().get("maps/City/MediEvalCity.g3db");
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
        environment = new Environment();
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
        // Model shrexModel =  myGame.getAssetManager().get("characters/Shrek/Shrek.obj", Model.class);
        // playerModelInstance = new ModelInstance(shrexModel);
        templateEnemyModelInstance = playerModelInstance.copy();
        ModelInstance gun = objLoaderCustom.loadGun();
        for (Node node : gun.nodes){
            node.translation.y = node.translation.y + 0.65f;
            node.translation.x = node.translation.x - 0.1f;
            node.translation.z = node.translation.z + 0.2f;
            node.rotation.setEulerAngles(95, 0, 0);
            playerModelInstance.nodes.add(node);
        }
        Matrix4 gunTransform = new Matrix4().translate(0.5f, 1.0f, 0.0f);
        gun.transform.set(gunTransform);
        playerModelInstance = playerModelInstance.copy();
        // Load all the walking shrek frames
        /*ModelInstance[] animationFrames = new ModelInstance[260];
        for (int i = 1; i < 250; i++) {
            String counter = "00000" + String.valueOf(i);
            int counterMaxLength = 6;
            counter = counter.substring(counter.length() - counterMaxLength);
            Model model = loader.loadModel(Gdx.files.internal("characters/WalkingShrek/walkingShrek_" + counter + ".obj"));
            animationFrames[i] = new ModelInstance(playerModel);
        }*/

        myInputProcessor = new MyInputProcessor(this, myGame.getGamePreferences());
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(myInputProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);

        playerBounds = new BoundingBox();
        //new ModelInstance(playerModel).calculateBoundingBox(playerBounds);
        // set cylinder as player bounds
        playerBounds.set(new Vector3(-0.1f, 0.3f, -0.1f), new Vector3(0.1f, 1, 0.1f));
        myGame.getPlayer().boundingBox = playerBounds;
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

        myGame.getClient().sendTCP(mapBoundsObject);
        myGame.getClient().sendTCP(myGame.getPlayer());
        // Initialize collsion between the map and the player
        //initializeCollision(mapModel, playerModel);

        //collisionWorld.addCollisionObject(obj.body, OBJECT_FLAG, GROUND_FLAG);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
        created = true;
    }

    /**
     * Screen has its render(float delta) and applicationListener has its render()
      * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        if (myGame.gameState != GameStateChange.GameStates.IN_GAME) {
            // Only render the game if the game is in the in game state
            return;
        }
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        render();
        // update healt
        healthLabel.setText("Health: " + myGame.getPlayer().health);
        waveLabel.setText("Wave: " + currentWave);
        scoreLabel.setText("Score: " + score);
        enemiesRemainingLabel.setText("Enemies Remaining: " + enemiesRemaining);

        stage.act(delta);
        stage.draw();
        // Center the crosshair
        Gdx.input.setInputProcessor(inputMultiplexer);
        Gdx.input.setCursorCatched(true);

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
        modelBatch.render(groundModelInstance, environment);
        modelBatch.render(playerModelInstance);

        // Render enemies
        HashMap<Integer, Enemy> clonedEnemies = (HashMap<Integer, Enemy>) enemies.clone();
        for (Map.Entry<Integer, Enemy> entry : clonedEnemies.entrySet()) {
            Enemy enemy = entry.getValue();
            ModelInstance enemyModelInstance = enemy.getEnemyInstance();
            // Rotate the enemy model smoothly
            float previousRotation = previousRotations.getOrDefault(enemy.id, 0f);            //float rotationDelta = enemy.rotation - previousRotation;
            float currentRotation = lerp(previousRotation, enemy.rotation, 0.2f);
            previousRotations.put(enemy.id, currentRotation);
            // Smoothly update enemy position
            Vector3 currentPosition = enemyModelInstance.transform.getTranslation(new Vector3());
            Vector3 targetPosition = new Vector3(enemy.X, enemy.Y, enemy.Z);
            Vector3 newPosition = currentPosition.lerp(targetPosition, 0.05f);
            // Translate the enemy model to the new position and rotate it
            enemyModelInstance.transform.setToTranslation(newPosition);
            enemyModelInstance.transform.rotate(Vector3.Y, currentRotation);
            modelBatch.render(enemyModelInstance, environment);
            shadowBatch.render(enemyModelInstance);
        }
        /**
         * If player is connected to the server, render all other players.
         */
        if (myGame.getClient().isConnected()) {
            // render all other players
            for (Player otherPlayer : myGame.getPlayers().values()) {
                // don't render the player if they are the same as the current playerd
                if (myGame.getPlayer().id != otherPlayer.id) {
                // create a new instance of the player model for this player
                ModelInstance otherPlayerModelInstance = templateEnemyModelInstance.copy();
                Vector3 playerPosition = new Vector3(otherPlayer.x, otherPlayer.y - 0.4f, otherPlayer.z);

                // set the position and orientation of the player model instance
                otherPlayerModelInstance.transform.setToTranslation(playerPosition);
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
            myGame.getPlayer().x = cameraPosition.x;
            myGame.getPlayer().y = cameraPosition.y;
            myGame.getPlayer().z = cameraPosition.z;
            myGame.getPlayer().rotation = (float) Math.toDegrees(Math.atan2(cameraDirection.x, cameraDirection.z));
            myGame.getPlayer().boundingBox = playerBounds;
            myGame.getClient().sendUDP(myGame.getPlayer());

        }
        // Render the enemy far away, then stop rendering it.
        for (Enemy enemy: enemiesToHide) {
            modelBatch.render(enemy.getEnemyInstance(), environment);
            shadowBatch.render(enemy.getEnemyInstance());
            enemies.remove(enemy.id);
        }
        enemiesToHide.clear();
        shadowBatch.end();
        shadowLight.end();
        modelBatch.end();

    }

    @Override
    public void dispose() {
        if (modelBatch != null) {
            modelBatch.dispose();
        }
        if (shadowBatch != null) {
            shadowBatch.dispose();
        }

    }

    @Override
    public void resize(int width, int height) {
        // update the camera
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
        show();
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        System.out.println("Paused");
    }

    @Override
    public void resume() {
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
        Gdx.input.setCursorCatched(true);

        myGame.music.stop();
        stage = new Stage(new ScreenViewport());

        Texture gunTexture = new Texture("guns/Sniper/sniperHud.png");
        gunHud = new Image(gunTexture);
        gunHud.setOrigin(Align.center);
        Viewport viewport = new StretchViewport(gunHud.getWidth() * 0.5f, gunHud.getHeight() * 0.5f);
        gunHud.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        gunHud.setPosition(camera.viewportWidth - gunHud.getWidth(), 0);
        stage.addActor(gunHud);

        // Add text to the stage to display the player's health
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        healthLabel = new Label("Health: " + myGame.getPlayer().health, labelStyle);
        waveLabel = new Label("Wave: " + currentWave, labelStyle);
        enemiesRemainingLabel = new Label("Enemies Remaining: " + enemiesRemaining, labelStyle);
        scoreLabel = new Label("Score: " + score, labelStyle);
        // Create the crosshair image and center it on the screen
        Texture texture = new Texture("crosshair-icon.png");
        crosshair = new Image(texture);
        // size the crosshair to 50x50 pixels
        crosshair.setSize(25, 25);
        crosshair.setPosition(
                Gdx.graphics.getWidth() / 2 - crosshair.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - crosshair.getHeight() / 2);
        healthLabel.setPosition(10, Gdx.graphics.getHeight() - 20);
        waveLabel.setPosition(Gdx.graphics.getWidth() - waveLabel.getWidth() - 20f, Gdx.graphics.getHeight() - 20);
        enemiesRemainingLabel.setPosition(Gdx.graphics.getWidth() - enemiesRemainingLabel.getWidth() - 20f, Gdx.graphics.getHeight() - 40);
        scoreLabel.setPosition(10, Gdx.graphics.getHeight() - 40);
        // Add the health label to the stage
        stage.addActor(healthLabel);
        stage.addActor(waveLabel);
        stage.addActor(enemiesRemainingLabel);
        stage.addActor(scoreLabel);
        // Add the crosshair to the stage
        stage.addActor(crosshair);

    }
    @Override
    public void hide() {
        Gdx.input.setCursorCatched(false);
    }
    public void shootBullet() {
        // create a new bullet
        PlayerBullet bullet = new PlayerBullet();
        bullet.set(cameraPosition.x, cameraPosition.y, cameraPosition.z, cameraDirection.x, cameraDirection.y, cameraDirection.z, 100, myGame.getPlayer().id);
        myGame.getClient().sendUDP(bullet);

    }
    public void jump(){

    }
    public void handleDeath() {
        // Show death screen, when player dies
        myGame.showDeathScreen();
    }
    public void handleIncomingEnemies(Enemies enemiesInfo){

            System.out.println("Enemies received");
            currentWave = enemiesInfo.waveNumber;
            enemiesRemaining = enemiesInfo.enemies.size();
            score = enemiesInfo.score;
            for (Map.Entry<Integer, HashMap> entry : enemiesInfo.enemies.entrySet()) {
                //if the health is 0, we hide the enemy
                if ((int) entry.getValue().get("health") <= 0) {
                    if (enemies.containsKey(entry.getKey())) {
                        Enemy enemy = enemies.get(entry.getKey());
                        enemy.hide(); // moves enemy outside the map
                        enemiesToHide.add(enemy); // adds enemy to list of enemies to hide
                        // Then we wait for a new render() loop
                    }
                }
                else if (enemies.containsKey(entry.getKey())) {
                    enemies.get(entry.getKey()).update(entry.getValue());
                } else if (templateEnemyModelInstance != null){
                    ModelInstance enemyInstance = templateEnemyModelInstance.copy();
                    enemies.put(entry.getKey(), new Enemy(enemyInstance, entry.getValue()));
                }

            }

    }
    public void handleIncomingEnemyHit(EnemyHit enemyHit){
        // Add animation to the crosshair
        Pulse pulse = new Pulse();
        crosshair.addAction(pulse.Action(crosshair));
        if (enemies.containsKey(enemyHit.idOfEnemyHit)) {
            System.out.println("Enemy hit, health: " + enemies.get(enemyHit.idOfEnemyHit).health);
        }
        if (enemyHit.isDead) {
            System.out.println("Enemy is dead, id: " + enemyHit.idOfEnemyHit);
            Enemy enemy = enemies.get(enemyHit.idOfEnemyHit);
            enemy.hide(); // moves enemy outside the map
            enemiesToHide.add(enemy); // adds enemy to list of enemies to hide

        }
    }

    public MyGame getMyGame() {
        return myGame;
    }
    public boolean isCreated() {
        return created;
    }

}
