package org.example;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class testMyClient extends ApplicationAdapter {

    @Override
    public void create () {
        Gdx.gl.glClearColor(1, 0, 0, 1);
    }

    @Override
    public void render () {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
