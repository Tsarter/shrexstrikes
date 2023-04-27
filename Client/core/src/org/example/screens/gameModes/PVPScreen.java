package org.example.screens.gameModes;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import org.example.MyGame;
import org.example.Player;
import org.example.animations.Pulse;
import org.example.messages.GameStatus;
import org.example.messages.PlayerHit;
import org.example.spawner.Enemy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PVPScreen extends GameScreen {

    private GameScreen gameScreen;
    private int timeLimit;
    private int timeLeft;
    private Label timeLabel;
    private Label killLabel;
    public PVPScreen(MyGame myGame, int timeLimit) throws IOException {
        super(myGame);
        this.timeLimit = 120;
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
        timeLabel.setText("Time left: " + timeLeft);
        killLabel.setText("Kills: " + myGame.getPlayer().kills);
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
            for (Player otherPlayer : myGame.getPlayers().values()) {
                // don't render the player if they are the same as the current playerd
                if (myGame.getPlayer().id != otherPlayer.id && otherPlayer.alive) {
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
        shadowBatch.end();
        shadowLight.end();
        modelBatch.end();

    }
    @Override
    public void show() {
        stage = new Stage();
        // Add text to the stage to display the player's health
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        healthLabel = new Label("Health: " + myGame.getPlayer().health, labelStyle);
        killLabel = new Label("Kills: " + myGame.getPlayer().kills, labelStyle);
        timeLabel = new Label("Time left: " + timeLeft, labelStyle);
        // Create the crosshair image and center it on the screen
        Texture texture = new Texture("assets/crosshair-icon.png");
        crosshair = new Image(texture);
        // size the crosshair to 50x50 pixels
        crosshair.setSize(25, 25);
        crosshair.setPosition(
                Gdx.graphics.getWidth() / 2 - crosshair.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - crosshair.getHeight() / 2);
        healthLabel.setPosition(10, Gdx.graphics.getHeight() - 20);
        killLabel.setPosition(10, Gdx.graphics.getHeight() - 40);
        timeLabel.setPosition(10, Gdx.graphics.getHeight() - 60);

        stage.addActor(healthLabel);
        stage.addActor(killLabel);
        stage.addActor(timeLabel);
        // Add the crosshair to the stage
        stage.addActor(crosshair);

    }
    public void handleIncomingGameStatus(GameStatus gameStatus) {
        timeLeft = gameStatus.timeLeft;
        score = gameStatus.score;
    }
    public void handleIncomingPlayerHit(PlayerHit playerHit) {
        if (playerHit.idOfPlayerHit == myGame.getPlayer().id) {
            // update the health
            myGame.getPlayer().health -= playerHit.damage;
        } else if (playerHit.idOfPlayerWhoHit == myGame.getPlayer().id) {
            // Animates the crosshair when the player hit another player
            Pulse pulse = new Pulse();
            crosshair.addAction(pulse.Action(crosshair));
        }
        if (myGame.getPlayer().health <= 0) {
            myGame.showPVPRespawnScreen();
        }
    }
}
