package org.example.screens.gameModes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import org.example.MyGame;
import org.example.Player;
import org.example.animations.Pulse;
import org.example.messages.GameStateChange;
import org.example.messages.PlayerHit;
import org.example.spawner.Enemy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.math.MathUtils.lerp;

public class ZombiesScreen extends GameScreen {

    public ZombiesScreen(MyGame myGame) throws IOException {
        super(myGame);
    }
    public void handleIncomingPlayerHit(PlayerHit playerHit) {
        if (playerHit.idOfPlayerHit == myGame.getPlayer().id) {
            // update the health
            myGame.getPlayer().health -= playerHit.damage;
        } else if (playerHit.idOfPlayerWhoHit == myGame.getPlayer().id) {
            // Animates the crosshair when the player hit another player
            Pulse pulse = new Pulse();
            crosshair.addAction(pulse.Action(crosshair));
        } else if (playerHit.idOfPlayerWhoHit == -1){
            // We got hit by zombie
            myGame.getPlayer().health -= playerHit.damage;
        }
        if (myGame.getPlayer().health <= 0) {
            myGame.showDeathScreen();
        }
    }
}
