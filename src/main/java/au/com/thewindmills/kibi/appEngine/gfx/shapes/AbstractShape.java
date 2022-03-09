package au.com.thewindmills.kibi.appEngine.gfx.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.utils.constants.DrawConstants;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;

/**
 * Contains the drawing methods, color styles,
 * position and shape to draw a shape. Should be extended for each value of {@link EShape}
 * 
 * @author Kibi
 */
public abstract class AbstractShape {

    /**
     * The origin position of the shape, bottom left (I think?)
     */
    protected Vector2 pos;

    /**
     * The type of shape
     */
    protected EShape shape;

    /**
     * The color the outline will be drawn in
     */
    protected Color strokeColor = DrawConstants.STROKE_COLOR;

    /**
     * The color the fill of the shape will drawn in
     */
    protected Color fillColor = DrawConstants.FILL_COLOR;
    
    public AbstractShape(EShape shape) {
        this.shape = shape;
        this.pos = new Vector2(0,0);
    }

    public AbstractShape(EShape shape, Vector2 pos) {
        this.pos = new Vector2(pos.x, pos.y);
        this.shape = shape;
    }

    public AbstractShape(EShape shape, float x, float y) {
        this.pos = new Vector2(x, y);
        this.shape = shape;
    }

    public void setStrokeColor(Color color) {
        this.strokeColor = color;
    }

    public void setFillColor(Color color) {
        this.fillColor = color;
    }

    public Color getFillColor() {
        return this.fillColor;
    }
 
    public EShape geShape() {
        return this.shape;
    }

    public void setPos(float x, float y) {
        this.pos.set(x, y);
    }

    public void setPos(Vector2 point) {
        this.setPos(point.x, point.y);
    }

    /**
     * Returns true if the given point is within the bounds of this shape
     * @param x
     * @param y
     * @return
     */
    public abstract boolean inBounds(float x, float y);

    /**
     * Returns true if the given point is within the bounds of this shape
     * @param point
     * @return
     */
    public boolean inBounds(Vector2 point) {
        return inBounds(point.x, point.y);
    }


    protected abstract void drawStroke(Batches batches);

    protected abstract void drawFill(Batches batches);

    public void draw(Batches batches) {
        //Draw the fill
        batches.shapeRenderer.set(ShapeType.Filled);
        batches.shapeRenderer.setColor(strokeColor);
        drawStroke(batches);
        batches.shapeRenderer.setColor(fillColor);
        drawFill(batches);
    }

    public abstract float getHeight();
    public abstract float getWidth();

}
