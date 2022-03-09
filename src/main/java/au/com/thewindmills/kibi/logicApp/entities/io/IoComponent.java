package au.com.thewindmills.kibi.logicApp.entities.io;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.AbstractShape;
import au.com.thewindmills.kibi.appEngine.utils.constants.Colors;
import au.com.thewindmills.kibi.logicApp.entities.ComponentBody;

public class IoComponent extends ComponentBody {

    public static final String PRE_NO_RENDER = "|NORENDER|";
    public static final String IO_NAME = PRE_NO_RENDER+ "IO";
    public static final String IO_FILENAME = IO_NAME + ".json";

    private final Color fillColor;

    public IoComponent(AppData data, String layer, int depth, Vector2 pos, AbstractShape shape) {
        super(data, layer, depth, pos, IO_FILENAME, shape);
        fillColor = this.getShape().getFillColor();
    }

    @Override
    public void onModelUpdate() {

        this.setFillColor(this.getModel().getOutputBits()[0] ? Colors.BLUE : this.fillColor);

    }


}
