package au.com.thewindmills.kibi.appEngine.gfx.ui.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.ui.UiPanel;

public class UiAppBar extends UiPanel {

    public UiAppBar(AppData data, String layer, int depth, float height) {
        super(data, layer, depth, Gdx.graphics.getHeight()-height, 0, Gdx.graphics.getHeight(), height);



    }

    //TODO: Work this out properly
    @Override
    public UiAppBar withStrokeColor(Color color) {
        this.setStrokeColor(color);
        return this;
    }

    @Override
    public UiAppBar withFillColor(Color color) {
        this.setFillColor(color);
        return this;
    }


    
}
