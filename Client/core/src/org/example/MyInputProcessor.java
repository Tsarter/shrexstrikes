package org.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import org.example.messages.GameStateChange;
import org.example.screens.gameModes.GameScreen;

public class MyInputProcessor implements InputProcessor {
    // Implement input event handling methods here...
    private GameScreen gameScreen;
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean jumpPressed;
    private boolean jumpOnGoing = false;
    private double jumpHeight = 0;
    private float jumpTime = 0f;
    private final float JUMP_DURATION = 1f;
    private final float initialPlayerHeight;
    private boolean cursorCaptured = true;

    private float zoom;
    private boolean zoomingIn = false;
    private float movementSpeed = 50f; // Change this value to adjust sensitivity
    private float rotationSpeed; // Change this value to adjust sensitivity
    private GamePreferences gamePreferences;

    public MyInputProcessor(GameScreen gameScreen, GamePreferences gamePreferences) {
        this.gameScreen = gameScreen;
        this.zoom = 67;
        this.rotationSpeed = gamePreferences.getMouseSensitivity();
        this.gamePreferences = gamePreferences;
        this.initialPlayerHeight = gameScreen.getMyGame().getPlayer().y;

    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                upPressed = true;
                break;
            case Input.Keys.S:
                downPressed = true;
                break;
            case Input.Keys.A:
                leftPressed = true;
                break;
            case Input.Keys.D:
                rightPressed = true;
                break;
            case Input.Keys.SPACE:
                if (!jumpOnGoing) {
                    jumpOnGoing = true;
                    jumpTime = 0f;
                }
                break;
            case Input.Keys.ESCAPE:
                if(gameScreen.getMyGame().gameState == GameStateChange.GameStates.IN_GAME) {
                    // Pause the game
                    gameScreen.getMyGame().gameState = GameStateChange.GameStates.IN_PAUSE_MENU;
                    gameScreen.getMyGame().showPauseOverlay();
                    gameScreen.pause();
                }
                if (cursorCaptured) {
                    Gdx.input.setCursorCatched(false);
                    cursorCaptured = false;
                } else {
                    Gdx.input.setCursorCatched(true);
                    cursorCaptured = true;
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                upPressed = false;
                break;
            case Input.Keys.S:
                downPressed = false;
                break;
            case Input.Keys.A:
                leftPressed = false;
                break;
            case Input.Keys.D:
                rightPressed = false;
                break;
            default:
                break;
        }
        return true;
    }

    public void updatePlayerMovement(float delta) {
        float speed = gameScreen.cameraSpeed * delta;
        float y = gameScreen.cameraPosition.y;

        // Get the camera direction vector in the xz plane
        Vector3 cameraDirectionXZ = new Vector3(gameScreen.cameraDirection.x, 0f, gameScreen.cameraDirection.z).nor();

        if (upPressed) {
            // Move in the direction of the camera direction vector in the xz plane
            gameScreen.cameraPosition.add(cameraDirectionXZ.scl(speed));
        }
        if (downPressed) {
            gameScreen.cameraPosition.sub(cameraDirectionXZ.scl(speed));
        }
        if (leftPressed) {
            // Get the perpendicular vector to the camera direction in the xz plane
            Vector3 cameraPerpendicularXZ = cameraDirectionXZ.crs(Vector3.Y).nor();
            gameScreen.cameraPosition.sub(cameraPerpendicularXZ.scl(speed));
        }
        if (rightPressed) {
            Vector3 cameraPerpendicularXZ = cameraDirectionXZ.crs(Vector3.Y).nor();
            gameScreen.cameraPosition.add(cameraPerpendicularXZ.scl(speed));
        }
        if (jumpOnGoing) {
            jumpTime += delta;
            if (jumpTime > JUMP_DURATION) {
                jumpOnGoing = false;
                y = initialPlayerHeight;
            } else {
                float jumpProgress = jumpTime / JUMP_DURATION;
                float yOffset = (float) (Math.sin(jumpProgress * Math.PI) * initialPlayerHeight * 0.5f);
                y = initialPlayerHeight + yOffset;
            }
        }
        gameScreen.cameraPosition.y = y;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Check if right mouse button is pressed
        if (button == Input.Buttons.RIGHT && !zoomingIn) {
            // Get a reference to the game's Camera object
            zoom = 25;
            gameScreen.camera.fieldOfView = 25;
            zoomingIn = true;
        }
        if (button == Input.Buttons.LEFT) {
            // shoot bullet
            gameScreen.shootBullet();
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.RIGHT  && zoomingIn) {
            zoom = 67;
            gameScreen.camera.fieldOfView = 67;
            zoomingIn = false;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Get the mouse input and calculate the camera's new position
        rotationSpeed = gamePreferences.getMouseSensitivity();
        float slowerMovement = rotationSpeed / 2;
        float deltaX = -Gdx.input.getDeltaX() * slowerMovement * Gdx.graphics.getDeltaTime();
        float deltaY = -Gdx.input.getDeltaY() * slowerMovement * Gdx.graphics.getDeltaTime();
        gameScreen.cameraDirection.rotate(Vector3.Y, deltaX);
        gameScreen.cameraDirection.rotate(gameScreen.cameraDirection.cpy().crs(Vector3.Y), deltaY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        rotationSpeed = gamePreferences.getMouseSensitivity();
        float deltaX = -Gdx.input.getDeltaX() * rotationSpeed * Gdx.graphics.getDeltaTime();
        float deltaY = -Gdx.input.getDeltaY() * rotationSpeed * Gdx.graphics.getDeltaTime();
        gameScreen.cameraDirection.rotate(Vector3.Y, deltaX);
        gameScreen.cameraDirection.rotate(gameScreen.cameraDirection.cpy().crs(Vector3.Y), deltaY);
        return false;
    }

    @Override
    public boolean scrolled(float screenX, float screenY) {
        return false;
    }
}
