package org.example.animations;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class Pulse {
    public SequenceAction Action(Actor crosshair){
        Action scaleAction = Actions.scaleTo(1.5f, 1.5f, 0.2f);
        //Action moveAction = Actions.moveTo(crosshair.getX() - 10, crosshair.getY(), 0.2f);
        Action scaleDownAction = Actions.scaleTo(1f, 1f, 0.2f);
        //Action moveDownAction = Actions.moveTo(crosshair.getX() + 10, crosshair.getY(), 0.2f);

        SequenceAction sequenceAction = Actions.sequence(
                scaleAction,
                scaleDownAction
        );
        // Return the sequence of actions
        return sequenceAction;

    }
}
