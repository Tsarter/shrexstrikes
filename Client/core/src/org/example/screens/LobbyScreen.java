package org.example.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.MyGame;
import org.example.Player;


public class LobbyScreen implements Screen {
    private MyGame game;
    private Stage stage;
    TextButton joinButton;
    TextButton leaveButton;
    Skin skin;
    Label title;
    Table layout;
    public LobbyScreen(MyGame game) {
        this.game = game;
    }
    @Override
    public void show() {
        // create UI elements
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/uiskin.json"));
        title = new Label(String.valueOf(game.gameMode), skin);

        joinButton = new TextButton("Join", skin);
        leaveButton = new TextButton("Leave", skin);
        leaveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Logic for starting the game
                game.leaveLobby();
            }
        });
        layout = new Table(skin);
        layout.setFillParent(true);
        layout.add(title).padBottom(10f).row();
        layout.add("Total players: " + String.valueOf(game.getPlayers().size()) + " / 2").padBottom(10f).row();
        layout.add("Players:").padBottom(10f).row();
        for (Player player : game.getPlayers().values()) {
            layout.add("Player name: " + String.valueOf(player.name)).padBottom(10f).row();
        }
        // Add UI elements to the table
        // layout.add(joinButton).padRight(10f);
        layout.add(leaveButton).padLeft(10f);
        stage.addActor(layout);
        // create layout
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
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
    }
    public void updateLobby() {

    }
}
