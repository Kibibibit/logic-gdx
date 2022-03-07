package au.com.thewindmills.kibi.appEngine.gfx.ui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.AbstractShape;
import au.com.thewindmills.kibi.appEngine.gfx.ui.UiEntity;
import au.com.thewindmills.kibi.appEngine.utils.constants.DrawConstants;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;

public class UiButton extends UiEntity {

    private Color hoverColor = DrawConstants.HOVER_COLOR;
    private Color color;
    private ButtonPress buttonPress;

    public UiButton(AppData data, String layer, int depth, Vector2 pos, AbstractShape shape, ButtonPress buttonPress) {
        super(data, layer, depth, pos, shape);
        this.color = shape.getFillColor();
        this.buttonPress = buttonPress;

    }

    public UiButton(Vector2 relativePos, AbstractShape shape, UiEntity parent, ButtonPress buttonPress) {
        super(relativePos, shape, parent);
        this.color = shape.getFillColor();
        this.buttonPress = buttonPress;
    }

    @Override
    protected void draw(Batches batches) {
        
    }

    @Override
    public void setFillColor(Color color) {
        this.color = color;
        this.getShape().setFillColor(color);
    }


    @Override
    protected void step(float delta) {
    }

    @Override
    public void dispose() {
        
    }

    @Override
    public void doOnMousePressed(int button) {
        this.buttonPress.onPressed(button);
    }

    @Override
    public void doOnMouseReleased(int button) {
        this.buttonPress.onReleased(button);
    }

    @Override
    public void onMouseEnter() {
        this.getShape().setFillColor(hoverColor);
    }

    @Override
    public void onMouseLeave() {
        this.getShape().setFillColor(color);
    }
    

    public interface ButtonPress {
        public abstract void onPressed(int button);
        public abstract void onReleased(int button);
    }
    

}
