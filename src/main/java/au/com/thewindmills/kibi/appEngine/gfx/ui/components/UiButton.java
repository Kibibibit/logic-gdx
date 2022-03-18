package au.com.thewindmills.kibi.appEngine.gfx.ui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.AbstractShape;
import au.com.thewindmills.kibi.appEngine.gfx.ui.UiEntity;
import au.com.thewindmills.kibi.appEngine.utils.constants.Colors;
import au.com.thewindmills.kibi.appEngine.utils.constants.DrawConstants;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;

public class UiButton extends UiEntity {

    private Color hoverColor = DrawConstants.HOVER_COLOR;
    private Color color;

    private String label;


    public UiButton(AppData data, String layer, int depth, Vector2 pos, AbstractShape shape, String label) {
        super(data, layer, depth, pos, shape);
        this.color = shape.getFillColor();
        this.label = label;
        
        this.setTextColor(Colors.WHITE);

    }

    public UiButton(Vector2 relativePos, AbstractShape shape, UiEntity parent, String label) {
        super(relativePos, shape, parent);
        this.color = shape.getFillColor();
        this.label = label;

        this.setTextColor(Colors.WHITE);
    }


    @Override
    public void onRenderText(Batches batches) {
        batches.font.draw(
            batches.spriteBatch, label, 
            this.getPos().x,
            this.getPos().y + this.getShape().getHeight()/2 + (batches.font.getCapHeight()/2), 
            this.getShape().getWidth(), 
            Align.center, 
            false);
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
    public void dispose() {}

    @Override
    public void onMouseEnter() {
        this.getShape().setFillColor(hoverColor);
    }

    @Override
    public void onMouseLeave() {
        this.getShape().setFillColor(color);
    }

}
