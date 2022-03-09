package au.com.thewindmills.kibi.logicApp.entities.io;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.CircleShape;
import au.com.thewindmills.kibi.logicApp.entities.ComponentInOut;

public class LightComponent extends IoComponent{

    public static final String LIGHT_NAME = "Light";

    public static final int LIGHT_RADIUS = 20;

    public LightComponent(AppData data, String layer, int depth, Vector2 pos) {
        super(data, layer, depth, pos, new CircleShape());
    }
    

    @Override
    protected void init(Vector2 pos) {

        this.getShape().setWidth(LIGHT_RADIUS*2);
        this.children.add(new ComponentInOut(this, 0, true));
        this.children.get(0).light();

    }

}
