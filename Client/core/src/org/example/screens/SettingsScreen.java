package org.example.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.MyGame;
import org.example.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SettingsScreen implements Screen {

    private Stage stage;
    private Table table;
    private Skin skin;
    private TextField usernameField;
    private SelectBox<String> playerSelectBox;
    private TextButton saveButton;
    private MyGame myGame;
    private Skin slider = new Skin(Gdx.files.internal("uiskin.json"));
    private Slider volumeSlider;
    private Slider soundSlider;
    private CheckBox tickBox;
    public SettingsScreen(MyGame myGame) {
        this.myGame = myGame;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.table = new Table(skin);
        this.table.setFillParent(true);

        Label usernameLabel = new Label("Change username", skin);
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        BitmapFont usernameFont = new BitmapFont(Gdx.files.internal("N2THxmg3XjwlAOwJTIgQL7g9.TTF.fnt"));
        usernameFont.getData().setScale(0.5f);
        textFieldStyle.font = usernameFont;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = new NinePatchDrawable(new NinePatch(new Texture("shrekbg.png"), 12, 12, 22, 12));
        // Cursor color
        Pixmap pixmap = new Pixmap(3, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture cursorTexture = new Texture(pixmap);
        pixmap.dispose();
        Drawable cursorDrawable = new TextureRegionDrawable(new TextureRegion(cursorTexture));
        textFieldStyle.cursor = cursorDrawable;
        usernameField = new TextField(myGame.getGamePreferences().getUsername(), textFieldStyle);
        table.add(usernameLabel).padBottom(0).row();
        table.add(usernameField).padBottom(30).row();


        // Style for select box
        Texture borderTexture = new Texture("shrekbg.png");
        NinePatch borderPatch = new NinePatch(borderTexture, 12, 12, 12, 12);
        SelectBox.SelectBoxStyle playerSelectBoxStyle = new SelectBox.SelectBoxStyle();
        BitmapFont smallerFont = new BitmapFont(Gdx.files.internal("N2THxmg3XjwlAOwJTIgQL7g9.TTF.fnt"));
        smallerFont.getData().setScale(0.6f);
        playerSelectBoxStyle.font = smallerFont;
        playerSelectBoxStyle.fontColor = Color.WHITE;
        Pixmap cursorPixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        cursorPixmap.setColor(Color.BLACK);
        cursorPixmap.fill();
        Texture selectBoxtexture = new Texture(cursorPixmap);
        cursorPixmap.dispose();
        NinePatch selectBoxPatch = new NinePatch(selectBoxtexture, 12, 12, 12, 12);
        playerSelectBoxStyle.background = new NinePatchDrawable(borderPatch);
        playerSelectBoxStyle.listStyle = new List.ListStyle();
        playerSelectBoxStyle.listStyle.font = smallerFont;
        playerSelectBoxStyle.listStyle.fontColorSelected = Color.WHITE;
        playerSelectBoxStyle.listStyle.fontColorUnselected = Color.WHITE;
        playerSelectBoxStyle.listStyle.selection = new NinePatchDrawable(borderPatch);
        playerSelectBoxStyle.scrollStyle = new ScrollPane.ScrollPaneStyle();
        playerSelectBoxStyle.scrollStyle.background = new NinePatchDrawable(selectBoxPatch);
        playerSelectBoxStyle.scrollStyle.vScrollKnob = new NinePatchDrawable(borderPatch);
        playerSelectBoxStyle.scrollStyle.vScroll = new NinePatchDrawable(borderPatch);
        playerSelectBoxStyle.scrollStyle.hScrollKnob = new NinePatchDrawable(borderPatch);
        playerSelectBoxStyle.scrollStyle.hScroll = new NinePatchDrawable(borderPatch);
        // Label & Player select box (Shrek, Fionza, Donkey)
        Label playerLabel = new Label("Change your character", skin);
        playerSelectBox = new SelectBox<String>(playerSelectBoxStyle);
        ArrayList<String> characterNames = new ArrayList<String>();
        for (Player.Character character : Player.Character.values()) {
            characterNames.add(character.toString());
        }
        playerSelectBox.setItems(characterNames.toArray(new String[0]));
        playerSelectBox.setSelected(myGame.getGamePreferences().getCharacter().toString());
        playerSelectBox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                // Update character when select box value changes
                myGame.getGamePreferences().setCharacter(Player.Character.valueOf(playerSelectBox.getSelected()));
                return true;
            }
        });
        table.add(playerLabel).padBottom(0).row();
        table.add(playerSelectBox).padBottom(30).row();

        // Volume slider & label
        Label volumeLabel = new Label("Volume: ", slider);
        volumeSlider = new Slider(0f, 1f, 0.1f, false, slider);
        volumeSlider.setValue(myGame.getGamePreferences().getMusicVolume());
        volumeSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                // Update volume when slider value changes
                myGame.music.setVolume(volumeSlider.getValue());
                return true;
            }
        });
        table.add(volumeLabel).row();
        table.add(volumeSlider).padBottom(30).row();

        // Sound slider & label
        Label soundLabel = new Label("Sounds: ", slider);
        soundSlider = new Slider(0f, 1f, 0.1f, false, slider);
        soundSlider.setValue(myGame.getGamePreferences().getSoundVolume());
        table.add(soundLabel).row();
        table.add(soundSlider).padBottom(30).row();


        // Buy Shrex Blue button
        TextButton buyShrexBlueButton = new TextButton("Buy Shrex Green", skin);
        buyShrexBlueButton.addListener(new ClickListener(){
           @Override
           public void clicked(InputEvent event, float x, float y){
               // Open url
                Gdx.net.openURI("https://buy.stripe.com/28odTJ8pY25d5eE9AB");
           }
        });
        table.add(buyShrexBlueButton).center().padBottom(10).row();

        // Save button
        saveButton = new TextButton("Save settings", skin);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveSettings();

            }
        });
        //table.add(saveButton).center().padBottom(10).row();

        // Exit to menu button
        TextButton exitButton = new TextButton("Back to menu", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveSettings();
                myGame.showMenuScreen();
            }
        });
        tickBox = new CheckBox("Enable ads", skin); // Replace "Enable Feature" with your desired label
        tickBox.setChecked( myGame.getGamePreferences().getAdsEnabled());
        tickBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                myGame.getGamePreferences().setAdsEnabled(tickBox.isChecked());
            }
        });
        table.add(tickBox).center().padBottom(10).row();
        table.add(exitButton).center().padBottom(10).row();

        // Add table to stage
        stage.addActor(table);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    // other methods from the Screen interface that can be left empty

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}
    public void saveSettings() {
        myGame.getGamePreferences().setMusicVolume(volumeSlider.getValue());
        myGame.getGamePreferences().setUsername(usernameField.getText());
        myGame.getGamePreferences().setSoundVolume(soundSlider.getValue());
    }
}
