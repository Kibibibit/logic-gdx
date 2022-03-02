package au.com.thewindmills.kibi.appEngine.gfx.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.AbstractShape;
import au.com.thewindmills.kibi.appEngine.utils.constants.DrawConstants;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;

public class UiButton extends UiEntity {

    private Color hoverColor = DrawConstants.HOVER_COLOR;
    private Color color;

    public UiButton(AppData data, String layer, int depth, Vector2 pos, AbstractShape shape) {
        super(data, layer, depth, pos, shape);
        this.color = shape.getFillColor();

    }

    public UiButton(Vector2 relativePos, AbstractShape shape, UiEntity parent) {
        super(relativePos, shape, parent);
        this.color = shape.getFillColor();
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
        boolean hovered = false;
        if (this.getData().getMouse().getContextEntity() != null) {
            hovered = this.getData().getMouse().getContextEntity().id == this.id;
        }

        if (hovered) {
            this.getShape().setFillColor(hoverColor);
        } else {
            this.getShape().setFillColor(color);
        }
    }

    @Override
    public void dispose() {
        
    }
    
    

}
