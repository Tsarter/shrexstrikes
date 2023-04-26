package org.example.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.MyGame;
import org.example.Player;

/**
 * This class is responsible for displaying the game end screen. (PVP game mode)
 */
public class GameOverScreen implements Screen {
    private MyGame game;
    private Stage stage;
    private Skin skin;
    private Label title;
    private TextButton backButton;
    public GameOverScreen(MyGame game) {
        this.game = game;

    }

    @Override
    public void show() {
        this.skin = game.getSkin();
        this.stage = new Stage(new ScreenViewport());

        // Set up title label
        title = new Label("Game Over", skin);
        title.setPosition(Gdx.graphics.getWidth() / 2 - title.getWidth() / 2, Gdx.graphics.getHeight() - 100f);

        // Set up kill count table
        Table table = new Table(skin);
        table.defaults().pad(10f);
        table.setPosition(Gdx.graphics.getWidth() / 2 - table.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        for (Player player : game.getPlayers().values()) {
            Label nameLabel = new Label(player.name, skin);
            Label killsLabel = new Label(Integer.toString(player.kills), skin);
            table.add(nameLabel).align(Align.left);
            table.add(killsLabel).align(Align.right);
            table.row();
        }

        // Set up back button
        backButton = new TextButton("Back to Menu", skin);
        backButton.setPosition(Gdx.graphics.getWidth() / 2 - backButton.getWidth() / 2, 50f);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.showMenuScreen();
            }
        });

        // Add actors to stage
        stage.addActor(title);
        stage.addActor(table);
        stage.addActor(backButton);
        // Set up input processing
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
