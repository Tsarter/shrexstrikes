package org.example.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.example.MyGame;
import org.example.messages.GameMode;

public class LoadingScreen implements Screen {

    private final MyGame game;
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private String loadingMessage;

    public LoadingScreen(MyGame game) {
        this.game = game;
        this.spriteBatch = new SpriteBatch();
        this.font = new BitmapFont();
        this.loadingMessage = "Loading...";
    }

    @Override
    public void show() {
        // Set the message to display while loading assets
        loadingMessage = "Loading assets...";
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the asset manager and display progress
        float progress = game.getAssetManager().getProgress();
        spriteBatch.begin();
        font.draw(spriteBatch, loadingMessage + " " + (int) (progress * 100) + "%", Gdx.graphics.getWidth() / 2.5f, Gdx.graphics.getHeight() / 2);
        spriteBatch.end();

        // Check if all assets are loaded
        if (game.getAssetManager().update()) {
            // All assets are loaded, switch to the next screen
            if (game.gameMode == GameMode.GameModes.ZOMBIES) {
                game.showZombiesScreen();
            }
            if (game.gameMode == GameMode.GameModes.PVP) {
                game.showPVPLobbyScreen();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
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
        // Dispose assets here if necessary
        spriteBatch.dispose();
        font.dispose();
    }
}
