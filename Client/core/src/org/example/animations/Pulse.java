package org.example.animations;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class Pulse {
    public SequenceAction Action(Camera camera){
        Action scaleAction = Actions.scaleBy(0.5f, 0.5f, 0.2f);
        Action moveAction = Actions.moveBy(-5, -5, 0.2f);
        Action scaleDownAction = Actions.scaleBy(-0.5f, -0.5f, 0.2f);
        Action moveDownAction = Actions.moveBy(5, 5, 0.2f);

        ParallelAction parallelAction1 = Actions.parallel(
                scaleAction,
                moveAction
        );
        ParallelAction parallelAction2 = Actions.parallel(
                scaleDownAction,
                moveDownAction

        );
        SequenceAction sequenceAction = Actions.sequence(
                parallelAction1,
                parallelAction2
        );
        // Return the sequence of actions
        return sequenceAction;

    }
}
