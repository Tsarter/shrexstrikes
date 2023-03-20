package org.example.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Screen;
import org.example.MyGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuScreen implements Screen {
    private Stage stage;
    private MyGame myGame;
    private Image mapImage;

    private Map<String, Texture> mapImagesMap = new HashMap<String, Texture>();

    public MenuScreen(MyGame myGame) {
        this.myGame = myGame;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("assets/uiskin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
        // Add actors to the stage, such as buttons and labels
        TextButton startButton = new TextButton("Start", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton payToWinButton = new TextButton("Enable 2x damage", skin);

        // Choose your map
        // more options: "Far Far Away", "Fairy Godmother's Cottage", "Poison Apple Inn", "Pied Piper's hideout", "Rumplestiltskin's Castle"
        String[] mapOptions = {"Shrex's Swamp", "Duloc", "Dragon's Keep"}; // Replace with your map names
        SelectBox<String> mapSelectBox = new SelectBox<>(skin);
        mapSelectBox.setItems(mapOptions);
        mapSelectBox.setSelected("Shrex's Swamp");
        // images of maps
        Texture shrexSwamp = new Texture(Gdx.files.internal("assets/menu/mapImages/shrekSwamp.png"));
        mapImagesMap.put("Shrex's Swamp", shrexSwamp);
        Texture duloc = new Texture(Gdx.files.internal("assets/menu/mapImages/duloc.png"));
        mapImagesMap.put("Duloc", duloc);
        mapImage = new Image(shrexSwamp);
        Texture dragonKeep = new Texture(Gdx.files.internal("assets/menu/mapImages/dragonsKeep.jpg"));
        mapImagesMap.put("Dragon's Keep", dragonKeep);

        mapImage.setSize(400f, 200f);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Logic for starting the game
                System.out.println("Start button clicked");
                myGame.showShrexScreen();
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Logic for opening the settings screen
                System.out.println("Settings button clicked");
            }
        });
        payToWinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Open a internal browser to a payment page
                System.out.println("Pay to win button clicked");
                // Create a pop up
                Dialog dialog = new Dialog("Double your damage", skin);
                dialog.text("Doubling your damage will cost you 5 euros.");

                dialog.key(com.badlogic.gdx.Input.Keys.ENTER, true);
                dialog.key(com.badlogic.gdx.Input.Keys.ESCAPE, false);
                // If the user clicks "yes", open the payment page
                dialog.button("Yes", true);
                dialog.button("!No", false);
                dialog.show(stage);

                dialog.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (actor.toString().equals("TextButton: Yes")) {
                            Gdx.net.openURI("https://buy.stripe.com/test_bIY5ne4wi2FWbjW8ww");
                            // change payToWinButton text to "Double damage enabled" and make it green
                            payToWinButton.setText("2x damage enabled");
                            payToWinButton.setColor(0, 1, 0, 1);

                        }
                    }
                });
            }
        });

        // Add a ChangeListener to the mapSelectBox
        mapSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Get the selected map name
                Texture selectedMap = mapImagesMap.get(mapSelectBox.getSelected());
                mapImage.setDrawable(new TextureRegionDrawable(new TextureRegion(selectedMap)));

            }
        });
        Table table = new Table();
        table.setFillParent(true);
        table.add(startButton).pad(10);
        table.row();
        table.add(settingsButton).pad(10);
        table.row();
        table.add(payToWinButton).pad(10);
        table.row();
        table.add(mapSelectBox).bottom().left().pad(10);
        table.row();
        table.add(mapImage).bottom().right().pad(10).size(mapImage.getWidth(), mapImage.getHeight());
        stage.addActor(table);
    }

    @Override
    public void show() {
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
    }
}

