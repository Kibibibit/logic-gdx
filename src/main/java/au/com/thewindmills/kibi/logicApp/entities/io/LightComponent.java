package au.com.thewindmills.kibi.logicApp.entities.io;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.RectShape;
import au.com.thewindmills.kibi.logicApp.entities.ComponentInOut;

public class LightComponent extends IoComponent{

    public static final String LIGHT_NAME = "Light";

    public static final int LIGHT_WIDTH = 20;
    public static final int LIGHT_HEIGHT = 20;

    public LightComponent(AppData data, String layer, int depth, Vector2 pos) {
        super(data, layer, depth, pos);
    }
    

    @Override
    protected void init(Vector2 pos) {

        ((RectShape) this.getShape()).setSize(LIGHT_WIDTH, LIGHT_HEIGHT);
        this.children.add(new ComponentInOut(this, 0, true));

    }

}
