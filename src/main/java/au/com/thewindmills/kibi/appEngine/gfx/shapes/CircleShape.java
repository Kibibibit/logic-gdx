package au.com.thewindmills.kibi.appEngine.gfx.shapes;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.utils.constants.DrawConstants;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;

/**
 * Represents a rectange, can be drawn with a pos and size
 * 
 * @author Kibi
 */
public class CircleShape extends AbstractShape {

    private float radius;

    public CircleShape() {
        this(0,0,0);
    }

    public CircleShape(Vector2 pos, float radius) {
        this(pos.x, pos.y, radius);
    }

    public CircleShape(float x, float y, float radius) {
        super(EShape.CIRCLE, x, y);
        this.radius = radius;
    }

    @Override
    protected void drawStroke(Batches batches) {
        batches.shapeRenderer.circle(pos.x, pos.y, radius);
    }

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public boolean inBounds(float x, float y) {
        return this.pos.dst(x, y) < radius;
    }

    @Override
    protected void drawFill(Batches batches) {
        batches.shapeRenderer.circle(pos.x, pos.y, radius - (DrawConstants.STROKE_WIDTH));
        
    }

}
