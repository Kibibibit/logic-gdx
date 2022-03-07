package au.com.thewindmills.kibi.appEngine.gfx.ui.components;

import com.badlogic.gdx.Gdx;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.ui.UiPanel;

public class UiAppBar extends UiPanel {

    public UiAppBar(AppData data, String layer, int depth, float height) {
        super(data, layer, depth, 0, Gdx.graphics.getHeight()-height, Gdx.graphics.getWidth(), height);
    }


    
}
