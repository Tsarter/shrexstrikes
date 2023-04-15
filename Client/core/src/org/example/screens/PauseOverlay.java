package org.example.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.MyGame;

public class PauseOverlay implements Screen {
    private MyGame game;
    private Stage stage;
    private Skin skin;
    private float sensitivity;
    public PauseOverlay(MyGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
        // Button styling
        Texture borderTexture = new Texture("assets/shrekbg.png");
        NinePatch borderPatch = new NinePatch(borderTexture, 12, 12, 12, 12);
        BitmapFont customFont = new BitmapFont(Gdx.files.internal("assets/N2THxmg3XjwlAOwJTIgQL7g9.TTF.fnt"));
        customFont.getData().setScale(1f);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = customFont;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new NinePatchDrawable(borderPatch);
        // Sensitivity slider label
        Label sensitivityLabel = new Label("Sensitivity", skin);
        // Sensitivity slider
        Slider sensitivitySlider = new Slider(0.1f, 20, 0.5f, false, skin);
        sensitivity = game.getGamePreferences().getMouseSensitivity();
        sensitivitySlider.setValue(sensitivity);
        sensitivitySlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                sensitivity = sensitivitySlider.getValue();
                game.getGamePreferences().setMouseSensitivity(sensitivity);
                return true;
            }
        });

        // Exit button, that will return to the main menu
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.showMenuScreen();
            }
        });
        // Resume button, that will return to the game
        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.showShrexScreen();
            }
        });
        // Create table
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);
        table.add(exitButton).center().padBottom(10);
        table.row();
        table.add(resumeButton).center().padBottom(50);
        table.row();
        table.add(sensitivityLabel).center().padBottom(10);
        table.row();
        table.add(sensitivitySlider).center().padBottom(50);
        // Add buttons to the stage
        stage.addActor(table);

    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 0.8f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Draw the stage
        stage.act();
        stage.draw();
        Gdx.input.setCursorCatched(false);

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

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
