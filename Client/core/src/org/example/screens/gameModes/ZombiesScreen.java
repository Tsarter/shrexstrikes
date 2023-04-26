package org.example.screens.gameModes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import org.example.MyGame;
import org.example.Player;
import org.example.messages.GameStateChange;
import org.example.spawner.Enemy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.math.MathUtils.lerp;

public class ZombiesScreen extends GameScreen {

    public ZombiesScreen(MyGame myGame) throws IOException {
        super(myGame);
    }

}
