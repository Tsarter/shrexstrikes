package org.example;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

class MyInputProcessor implements InputProcessor {
    // Implement input event handling methods here...
    private MyClient myClient;
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;

    public MyInputProcessor( MyClient myClient) {
        this.myClient = myClient;
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
        float speed = myClient.cameraSpeed * delta;
        if (upPressed) {
            myClient.cameraPosition.add(myClient.cameraDirection.nor().scl(speed));
        }
        if (downPressed) {
            myClient.cameraPosition.sub(myClient.cameraDirection.nor().scl(speed));
        }
        if (leftPressed) {
            myClient.cameraDirection.rotate(Vector3.Y, speed * 5);
        }
        if (rightPressed) {
            myClient.cameraDirection.rotate(Vector3.Y, -speed * 5);
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
