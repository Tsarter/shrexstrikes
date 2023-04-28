package org.example.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.MyGame;

import static java.lang.Thread.sleep;


public class DeathScreen implements Screen {
    MyGame game;

    private Stage stage;
    public DeathScreen(MyGame game) {
     this.game = game;
    }
    @Override
    public void show() {


        stage = new Stage(new ScreenViewport());
        Skin skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
        Label label = new Label("You died", skin);
        TextButton button = new TextButton("Back to menu", skin);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.showMenuScreen();
                game.getPlayer().health = 100;
            }
        });
        TextButton payButton = new TextButton("Pay 3 euro to revive", skin);
        payButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.resume();
                // open link
                Gdx.net.openURI("https://buy.stripe.com/6oE8zp5dMbFNgXm4gg");
                // block game for 30 seconds
                label.setText("Your game will continue when you have paid.");
                game.getPlayer().health = 100;
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.padTop(200f);
        table.add(label).align(Align.center);
        table.row();
        table.add(button).align(Align.center);
        table.row();
        table.add(payButton).align(Align.center);
        Actor actor = new Actor();
        actor.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        actor.setColor(Color.BLACK);
        stage.addActor(actor);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCursorCatched(false);

    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }
    @Override
    public void hide() {
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
    public void dispose() {
        stage.dispose();
    }

}
