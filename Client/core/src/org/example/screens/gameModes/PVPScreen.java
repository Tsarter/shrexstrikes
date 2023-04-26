package org.example.screens.gameModes;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import org.example.MyGame;
import org.example.Player;
import org.example.spawner.Enemy;

import java.io.IOException;

public class PVPScreen extends GameScreen {

    private GameScreen gameScreen;
    private int timeLeft;
    private int timeLimit;
    public PVPScreen(MyGame myGame, int timeLimit) throws IOException {
        super(myGame);
        this.timeLimit = timeLimit;
        this.timeLeft = timeLimit;
    }
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        render();
        // update healt
        healthLabel.setText("Health: " + myGame.getPlayer().health);
        // Render the crosshair
        // Define the duration and scale of the animation
        float duration = 0.5f;
        float scale = 1.5f;

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

        /**
         * If player is connected to the server, render all other players.
         */
        if (myGame.getClient().isConnected()) {
            // render all other players
            for (Player otherPlayer : myGame.getPlayers()) {
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
}
