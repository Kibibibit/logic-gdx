package au.com.thewindmills.kibi.logicApp.entities.io;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.RectShape;
import au.com.thewindmills.kibi.logicApp.entities.ComponentInOut;

public class SwitchComponent extends IoComponent {

    public static final String SWITCH_NAME = "Switch";

    private static final int SWITCH_HEIGHT = 50;
    private static final int SWITCH_WIDTH = 50;

    public SwitchComponent(AppData data, String layer, int depth, Vector2 pos) {
        super(data, layer, depth, pos);
    }

    @Override
    protected void init(Vector2 pos) {

        ((RectShape) this.getShape()).setSize(SWITCH_WIDTH, SWITCH_HEIGHT);
        this.children.add(new ComponentInOut(this, 0, false));

    }

    @Override
    public void doOnMouseReleased(int button) {
        if (button == Input.Buttons.LEFT) {
            this.getModel().trigger(0, !this.getModel().getOutputBits()[0]);
        }
    }

}
