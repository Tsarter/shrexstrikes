package org.example.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.MyGame;

public class PVPRespawnScreen implements Screen {

    private MyGame game;
    private Stage stage;
    private Label countdownLabel;
    private int countdownSeconds = 5; // set the initial countdown time

    public PVPRespawnScreen(MyGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        this.stage = new Stage(new ScreenViewport());
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        // create the countdown label with initial text
        countdownLabel = new Label("Respawning in " + countdownSeconds + " seconds", skin);
        // set the label position
        countdownLabel.setPosition(Gdx.graphics.getWidth() / 2 - countdownLabel.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        // add the label to the stage
        stage.addActor(countdownLabel);
        // Set the input processor to the stage
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // update the countdown timer
        countdownSeconds -= delta;
        // update the label text
        countdownLabel.setText("Respawning in " + (int)game.getPlayer().timeTilRespawn + " seconds");
        // if the countdown is finished, switch back to the game screen
        if (game.getPlayer().timeTilRespawn <= 0) {
            if (game.getPlayer().timeLeft <= 6)
                game.showGameOverScreen();
            else if (game.getPlayer().timeLeft > 0){
                game.showPVPScreen();
            }
        }
        // clear the screen and draw the stage
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // nothing to do here
    }

    @Override
    public void resume() {
        // nothing to do here
    }

    @Override
    public void hide() {
        // nothing to do here
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
