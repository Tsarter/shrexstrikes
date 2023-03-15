package org.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import org.example.screens.ShrexScreen;

public class MyInputProcessor implements InputProcessor {
    // Implement input event handling methods here...
    private ShrexScreen shrexScreen;
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;

    private float movementSpeed = 50f; // Change this value to adjust sensitivity

    public MyInputProcessor(ShrexScreen shrexScreen) {
        this.shrexScreen = shrexScreen;
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
        if (upPressed) {
            shrexScreen.cameraPosition.add(shrexScreen.cameraDirection.nor().scl(speed));
        }
        if (downPressed) {
            shrexScreen.cameraPosition.sub(shrexScreen.cameraDirection.nor().scl(speed));
        }
        if (leftPressed) {
            shrexScreen.cameraPosition.sub(shrexScreen.cameraDirection.cpy().crs(Vector3.Y).nor().scl(speed));
        }
        if (rightPressed) {
            shrexScreen.cameraPosition.add(shrexScreen.cameraDirection.cpy().crs(Vector3.Y).nor().scl(speed));
        }
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Get the mouse input and calculate the camera's new position
        //float deltaX = -Gdx.input.getDeltaX() * movementSpeed * Gdx.graphics.getDeltaTime();
        // float deltaY = -Gdx.input.getDeltaY() * movementSpeed * Gdx.graphics.getDeltaTime();
        //shrexScreen.cameraDirection.rotate(Vector3.Y, deltaX);

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float screenX, float screenY) {
        return false;
    }
}
