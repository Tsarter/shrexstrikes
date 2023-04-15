package org.example.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;


import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.GamePreferences;
import org.example.MyGame;
import org.example.messages.GameMode;

import java.util.Arrays;

public class MenuScreen implements Screen {
    private Stage stage;
    private MyGame myGame;
    private Skin skin;

    private Music menuMusic;
    private float musicVolume;

    private Skin buttonSkin;

    private Texture backgroundTexture;
    private TextureRegionDrawable backgroundDrawable;
    private SelectBox<String> gameTypeSelectBox;

    Skin slider = new Skin(Gdx.files.internal("assets/uiskin.json"));


    public MenuScreen(MyGame myGame) {
        this.myGame = myGame;
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
        Gdx.input.setInputProcessor(stage);
        setGamePreferences(); // Set game preferences
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/best_theme.mp3"));
        menuMusic.setVolume(musicVolume);
        menuMusic.setLooping(true);

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

        // Style select box
        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
        BitmapFont smallerFont = new BitmapFont(Gdx.files.internal("assets/N2THxmg3XjwlAOwJTIgQL7g9.TTF.fnt"));
        smallerFont.getData().setScale(0.5f);
        selectBoxStyle.font = smallerFont;
        selectBoxStyle.fontColor = Color.WHITE;
        Pixmap pixmap = new Pixmap(128, 128, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        Texture selectBoxtexture = new Texture(pixmap);
        pixmap.dispose();
        NinePatch selectBoxPatch = new NinePatch(selectBoxtexture, 12, 12, 12, 12);
        selectBoxStyle.background = new NinePatchDrawable(borderPatch);
        selectBoxStyle.listStyle = new List.ListStyle();
        selectBoxStyle.listStyle.font = smallerFont;
        selectBoxStyle.listStyle.fontColorSelected = Color.WHITE;
        selectBoxStyle.listStyle.fontColorUnselected = Color.WHITE;
        selectBoxStyle.listStyle.selection = new NinePatchDrawable(borderPatch);
        selectBoxStyle.scrollStyle = new ScrollPane.ScrollPaneStyle();
        selectBoxStyle.scrollStyle.background = new NinePatchDrawable(selectBoxPatch);
        selectBoxStyle.scrollStyle.vScrollKnob = new NinePatchDrawable(borderPatch);
        selectBoxStyle.scrollStyle.vScroll = new NinePatchDrawable(borderPatch);
        selectBoxStyle.scrollStyle.hScrollKnob = new NinePatchDrawable(borderPatch);
        selectBoxStyle.scrollStyle.hScroll = new NinePatchDrawable(borderPatch);



        Slider volumeSlider = new Slider(0f, 1f, 0.1f, false, slider);
        volumeSlider.setValue(menuMusic.getVolume());
        volumeSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                // Update volume when slider value changes
                menuMusic.setVolume(volumeSlider.getValue());
                return true;
            }
        });

        Label volumeLabel = new Label("Volume: ", slider);
        //Label volumeLabel = new Label("Volume: " + volumeSlider.getValue(), slider);


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

        // Create the game type selection widget
        gameTypeSelectBox = new SelectBox<>(selectBoxStyle);
        Array<String> gameTypes = new Array();
        for (GameMode.GameModes gameType : GameMode.GameModes.values()) {
            gameTypes.add(gameType.toString());
        }
        gameTypeSelectBox.setItems(gameTypes);
        gameTypeSelectBox.setSelectedIndex(0);


        TextButton settingsButton = new TextButton("Settings", buttonStyle);
        settingsButton.setTransform(true);
        settingsButton.setScale(0.6f);


        // Add UI elements to the table
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Save the volume level to preferences
                myGame.getGamePreferences().setMusicVolume(volumeSlider.getValue());
                // Logic for starting the game
                myGame.gameMode = GameMode.GameModes.valueOf(gameTypeSelectBox.getSelected());
                if (myGame.gameMode == GameMode.GameModes.ZOMBIES) {
                    myGame.showShrexScreen();
                }
                //TODO: Add logic for other game modes
            }
        });


        // Add actors to stage
        Table table = new Table();
        table.setFillParent(true);
        table.setBackground(backgroundDrawable);
        //table.add(background);
        table.row();
        table.padTop(500f);
        table.add(gameTypeSelectBox).padBottom(50f).row();
        table.row();
        table.add(startButton).width((float) (startButton.getWidth() * 1.2)).padRight(50);
        table.padBottom(-10);
        table.row();
        table.add(settingsButton).width((float) (settingsButton.getWidth() * 1.2)).padLeft(94);
        table.padBottom(30);
        table.row();
        table.add(volumeLabel).pad(3);
        table.row();
        table.add(volumeSlider).pad(3);
        stage.addActor(table);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        menuMusic.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
        // Load assets in the background
        myGame.getAssetManager().update(20);
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
        menuMusic.stop();
    }

    @Override
    public void dispose() {
        menuMusic.stop();
        menuMusic.dispose();
        stage.dispose();
        skin.dispose();
    }
    public void setGamePreferences() {
        // Set volume
        musicVolume = myGame.getGamePreferences().getMusicVolume();
    }
}

