package org.example.animations;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.awt.*;

public class GunShoot {
    public SequenceAction Action(Camera camera){
        Action moveDownAction = Actions.moveTo(camera.viewportWidth / 2 + 10, -10, 0.1f);
        Action moveUpAction = Actions.moveTo(camera.viewportWidth / 2, 0, 0.1f);
        SequenceAction sequenceAction = Actions.sequence(
                moveDownAction,
                moveUpAction
        );
        // Return the sequence of actions
        return sequenceAction;

    }
    public SequenceAction MuzzleFlash(Camera camera){
        Action moveDownAction = Actions.moveTo(camera.viewportWidth / 2 + 10, -10, 0.1f);
        Action fadeInAction = Actions.fadeIn(0.1f);
        Action moveUpAction = Actions.moveTo(camera.viewportWidth / 2, 0, 0.1f);
        Action fadeOutAction = Actions.fadeOut(0.1f);

        ParallelAction parallelAction1 = Actions.parallel(
                fadeInAction,
                moveDownAction
        );
        ParallelAction parallelAction2 = Actions.parallel(
                fadeOutAction,
                moveUpAction
        );
        SequenceAction sequenceAction = Actions.sequence(
                parallelAction1,
                parallelAction2
        );
        // Return the sequence of actions
        return sequenceAction;

    }
}
