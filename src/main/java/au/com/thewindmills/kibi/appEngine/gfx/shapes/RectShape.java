package au.com.thewindmills.kibi.appEngine.gfx.shapes;

import com.badlogic.gdx.math.Vector2;

import static au.com.thewindmills.kibi.appEngine.utils.constants.DrawConstants.STROKE_WIDTH;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;

/**
 * Represents a rectange, can be drawn with a pos and size
 * 
 * @author Kibi
 */
public class RectShape extends AbstractShape {

    private Vector2 size;

    public RectShape(Vector2 pos, Vector2 size) {
        super(EShape.RECT, pos);
        this.size = new Vector2(size.x, size.y);
    }

    public RectShape(float x, float y, float width, float height) {
        this(new Vector2(x, y), new Vector2(width, height));
    }

    @Override
    protected void drawStroke(Batches batches) {
        batches.shapeRenderer.rect(this.pos.x, this.pos.y, this.size.x, this.size.y);
    }

    @Override
    protected void drawFill(Batches batches) {
        batches.shapeRenderer.rect(this.pos.x + STROKE_WIDTH, this.pos.y + STROKE_WIDTH, this.size.x - STROKE_WIDTH*2, this.size.y - STROKE_WIDTH*2);
    }

    public Vector2 getSize() {
        return this.size;
    }

    @Override
    public boolean inBounds(float x, float y) {
        return x > pos.x && x < pos.x + size.x && y > pos.y && y < pos.y + size.y;
    }

    

}
