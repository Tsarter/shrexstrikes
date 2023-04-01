package org.example.assets;

import com.badlogic.gdx.assets.AssetManager;

public class Assets {
    public AssetManager manager = new AssetManager();
    public void load()
    {
        // manager.load(someTexture);
    }

    public void dispose()
    {
        manager.dispose();
    }

}
