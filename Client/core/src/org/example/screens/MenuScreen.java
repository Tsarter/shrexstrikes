package org.example.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.MyGame;

public class MenuScreen implements Screen {
    private Stage stage;
    private MyGame myGame;
    private Skin skin;

    private Skin buttonSkin;

    private Texture backgroundTexture;
    private TextureRegionDrawable backgroundDrawable;


    public MenuScreen(MyGame myGame) {
        this.myGame = myGame;
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        // Load background texture
        backgroundTexture = new Texture(Gdx.files.internal("assets/shrexstrikesbg.png"));

        // Create background drawable
        backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        Texture borderTexture = new Texture("assets/shrekbg.png");
        NinePatch borderPatch = new NinePatch(borderTexture, 12, 12, 12, 12);

        BitmapFont customFont = new BitmapFont(Gdx.files.internal("assets/N2THxmg3XjwlAOwJTIgQL7g9.TTF.fnt"));
        customFont.getData().setScale(1f);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = customFont;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = new NinePatchDrawable(borderPatch);


        // Create title label
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        skin.add("default", labelStyle);

        //Label titleLabel = new Label("Shrex Strikes", skin, "default");
        //titleLabel.setAlignment(Align.center);
        //titleLabel.setPosition(Gdx.graphics.getWidth() / 2f - titleLabel.getWidth() / 2f, Gdx.graphics.getHeight() * 2 / 3f);

        Image background = new Image(backgroundDrawable);

        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        // Create start button
        TextButton startButton = new TextButton("Start", buttonStyle);
        startButton.setTransform(true);
        startButton.setScale(1.3f);


        TextButton settingsButton = new TextButton("Settings", buttonStyle);
        settingsButton.setTransform(true);
        settingsButton.setScale(0.6f);




        // Add UI elements to the table


        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Logic for starting the game
                myGame.showShrexScreen();
            }
        });


        // Add actors to stage
        Table table = new Table();
        table.setFillParent(true);
        table.setBackground(backgroundDrawable);
        table.add(background);
        table.row();
        table.add(startButton).width((float) (startButton.getWidth() * 1.2)).padRight(50);
        table.padBottom(-10);
        table.row();
        table.add(settingsButton).width((float) (settingsButton.getWidth() * 1.2)).padLeft(94);
        table.padBottom(30);
        stage.addActor(table);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
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
        skin.dispose();
    }
}

