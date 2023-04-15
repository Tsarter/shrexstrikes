package org.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import org.example.messages.GameStateChange;
import org.example.messages.PlayerBullet;
import org.example.screens.ShrexScreen;

public class MyInputProcessor implements InputProcessor {
    // Implement input event handling methods here...
    private ShrexScreen shrexScreen;
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean cursorCaptured = true;

    private float zoom;
    private boolean zoomingIn = false;
    private float movementSpeed = 50f; // Change this value to adjust sensitivity
    private float rotationSpeed = 10f; // Change this value to adjust sensitivity

    public MyInputProcessor(ShrexScreen shrexScreen) {
        this.shrexScreen = shrexScreen;
        this.zoom = 67;
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
            case Input.Keys.ESCAPE:
                if(shrexScreen.getMyGame().gameState == GameStateChange.GameStates.IN_GAME) {
                    // Pause the game
                    shrexScreen.getMyGame().gameState = GameStateChange.GameStates.IN_PAUSE_MENU;
                    shrexScreen.getMyGame().showPauseOverlay();
                    shrexScreen.pause();
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
        float speed = shrexScreen.cameraSpeed * delta;
        float y = shrexScreen.cameraPosition.y;

        // Get the camera direction vector in the xz plane
        Vector3 cameraDirectionXZ = new Vector3(shrexScreen.cameraDirection.x, 0f, shrexScreen.cameraDirection.z).nor();

        if (upPressed) {
            // Move in the direction of the camera direction vector in the xz plane
            shrexScreen.cameraPosition.add(cameraDirectionXZ.scl(speed));
        }
        if (downPressed) {
            shrexScreen.cameraPosition.sub(cameraDirectionXZ.scl(speed));
        }
        if (leftPressed) {
            // Get the perpendicular vector to the camera direction in the xz plane
            Vector3 cameraPerpendicularXZ = cameraDirectionXZ.crs(Vector3.Y).nor();
            shrexScreen.cameraPosition.sub(cameraPerpendicularXZ.scl(speed));
        }
        if (rightPressed) {
            Vector3 cameraPerpendicularXZ = cameraDirectionXZ.crs(Vector3.Y).nor();
            shrexScreen.cameraPosition.add(cameraPerpendicularXZ.scl(speed));
        }

        shrexScreen.cameraPosition.y = y;
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
            shrexScreen.camera.fieldOfView = 25;
            zoomingIn = true;
        }
        if (button == Input.Buttons.LEFT) {
            // shoot bullet
            shrexScreen.shootBullet();
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.RIGHT  && zoomingIn) {
            zoom = 67;
            shrexScreen.camera.fieldOfView = 67;
            zoomingIn = false;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Get the mouse input and calculate the camera's new position
        float slowerMovement = rotationSpeed / 2;
        float deltaX = -Gdx.input.getDeltaX() * slowerMovement * Gdx.graphics.getDeltaTime();
        float deltaY = -Gdx.input.getDeltaY() * slowerMovement * Gdx.graphics.getDeltaTime();
        shrexScreen.cameraDirection.rotate(Vector3.Y, deltaX);
        shrexScreen.cameraDirection.rotate(shrexScreen.cameraDirection.cpy().crs(Vector3.Y), deltaY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        float deltaX = -Gdx.input.getDeltaX() * rotationSpeed * Gdx.graphics.getDeltaTime();
        float deltaY = -Gdx.input.getDeltaY() * rotationSpeed * Gdx.graphics.getDeltaTime();
        shrexScreen.cameraDirection.rotate(Vector3.Y, deltaX);
        shrexScreen.cameraDirection.rotate(shrexScreen.cameraDirection.cpy().crs(Vector3.Y), deltaY);
        return false;
    }

    @Override
    public boolean scrolled(float screenX, float screenY) {
        return false;
    }
}
