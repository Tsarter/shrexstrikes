package org.example.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;
import org.example.MyGame;
import org.example.messages.GameMode;

import java.io.FileNotFoundException;

public class LoadingScreen implements Screen {

    private final MyGame game;
    private SpriteBatch spriteBatch;
    private VideoPlayer videoPlayer;
    private BitmapFont font;
    private String loadingMessage;
    private Stage stage;
    private TextButton skipButton;
    private Texture currentFrame;

    public LoadingScreen(MyGame game) {
        this.game = game;
        this.loadingMessage = "Loading...";

    }

    @Override
    public void show() {
        this.font = new BitmapFont();
        this.spriteBatch = new SpriteBatch();
        // Set the message to display while loading assets
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        loadingMessage = "Loading assets...";
        spriteBatch = new SpriteBatch();
        videoPlayer = VideoPlayerCreator.createVideoPlayer();
        // Skip button
        skipButton = new TextButton("Skip", game.getSkin());
        skipButton.setPosition(Gdx.graphics.getWidth() - skipButton.getWidth() - 80, 50);
        skipButton.scaleBy(2);
        skipButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

                //videoPlayer.dispose();
                // All assets are loaded, switch to the next screen
                if (game.gameMode == GameMode.GameModes.ZOMBIES) {
                    game.showZombiesScreen();
                }
                if (game.gameMode == GameMode.GameModes.PVP) {
                    game.showPVPLobbyScreen();
                }
            }
        });
        videoPlayer.setOnCompletionListener(new VideoPlayer.CompletionListener() {
            @Override
            public void onCompletionListener(FileHandle fileHandle) {
                videoPlayer.dispose();
                // spriteBatch.dispose();
                // All assets are loaded, switch to the next screen
                if (game.gameMode == GameMode.GameModes.ZOMBIES) {
                    game.showZombiesScreen();
                }
                if (game.gameMode == GameMode.GameModes.PVP) {
                    game.showPVPLobbyScreen();
                }
            }
        });
        try {
            // double random = Math.random();
            int currentAdId = game.getGamePreferences().getCurrentAdId();
            if (currentAdId == 0) {
                videoPlayer.play(Gdx.files.internal("ads/weAreBolt.webm"));
                game.music.dispose();
                game.music = Gdx.audio.newMusic(Gdx.files.internal("ads/weAreBolt.mp3"));
                game.getGamePreferences().setCurrentAdId(1);
                game.music.setVolume(game.getGamePreferences().getMusicVolume());
                game.music.play();
            } else if (currentAdId == 1) {
                videoPlayer.play(Gdx.files.internal("ads/weAreGrow.webm"));
                game.music.dispose();
                game.music = Gdx.audio.newMusic(Gdx.files.internal("ads/weAreGrow.mp3"));
                game.getGamePreferences().setCurrentAdId(2);
                game.music.setVolume(game.getGamePreferences().getMusicVolume());
                game.music.play();
            } else if (currentAdId == 2) {
                videoPlayer.play(Gdx.files.internal("ads/interstellar.webm"));
                game.music.dispose();
                game.music = Gdx.audio.newMusic(Gdx.files.internal("ads/interstellar.mp3"));
                game.getGamePreferences().setCurrentAdId(3);
                if (game.getGamePreferences().getMusicVolume() > 0.01 && game.getGamePreferences().getMusicVolume() < 0.7f){
                    game.music.setVolume(game.getGamePreferences().getMusicVolume() + 0.2f);
                }else{
                    game.music.setVolume(game.getGamePreferences().getMusicVolume());
                }
                game.music.play();
            } else if (currentAdId == 3) {
                videoPlayer.play(Gdx.files.internal("ads/CAMPUS.webm"));
                game.music.dispose();
                game.music = Gdx.audio.newMusic(Gdx.files.internal("ads/CAMPUS.mp3"));
                game.music.setVolume(game.getGamePreferences().getMusicVolume());
                game.getGamePreferences().setCurrentAdId(0);
                game.music.play();
            }
            System.out.println("Music volume is " + game.getGamePreferences().getMusicVolume());

        } catch (FileNotFoundException e) {
            Gdx.app.error("LoadingScreen", "Video file not found", e);
        }
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the asset manager and display progress
        float progress = game.getAssetManager().getProgress();
        videoPlayer.update();
        spriteBatch.begin();
        currentFrame = videoPlayer.getTexture();
        if (currentFrame != null) {
            int videoWidth = videoPlayer.getVideoWidth();
            int videoHeight = videoPlayer.getVideoHeight();
            int screenWidth = Gdx.graphics.getWidth();
            int screenHeight = Gdx.graphics.getHeight();
            int scaledVideoWidth = (int) (screenWidth);
            int scaledVideoHeight = (int) (screenWidth * 0.565);

            int x = (screenWidth - scaledVideoWidth) / 2;
            int y = (screenHeight - scaledVideoHeight) / 2;
            spriteBatch.draw(currentFrame, x, y,scaledVideoWidth, scaledVideoHeight);
        }

        // Check if all assets are loaded
        if (game.getAssetManager().update()) {
            // add the skip button
            stage.addActor(skipButton);
            // All assets are loaded, switch to the next screen
            if (game.gameMode == GameMode.GameModes.ZOMBIES) {
                // game.showZombiesScreen();
            }
            if (game.gameMode == GameMode.GameModes.PVP) {
                // game.showPVPLobbyScreen();
            }
        }
        else {
            font.draw(spriteBatch, loadingMessage + " " + (int) (progress * 100) + "%", Gdx.graphics.getWidth() - 200f, 50f);
        }
        spriteBatch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
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
        // Dispose assets here if necessary
        spriteBatch.dispose();
        font.dispose();
        game.music.stop();
    }

    @Override
    public void dispose() {
        // Dispose assets here if necessary
        spriteBatch.dispose();
        font.dispose();
        if (videoPlayer != null) {
            videoPlayer.dispose();
        }
    }
}
