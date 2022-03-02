package au.com.thewindmills.kibi.appEngine.gfx.shapes;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.utils.Batches;

public class RectShape extends AbstractShape {

    private Vector2 size;

    public RectShape(Vector2 pos, Vector2 size) {
        super(EShape.RECT, pos);
        this.size = new Vector2(size.x, size.y);
    }

    public RectShape(float x, float y, float width, float height) {
        this(new Vector2(x, y), new Vector2(width, height));
    }

    public static RectShape fromCenter(Vector2 centerPos, Vector2 size) {
        Vector2 origin = new Vector2(
                centerPos.x - (size.x / 2),
                centerPos.y - (size.y / 2));

        return new RectShape(origin, size);
    }

    public static RectShape fromCenter(float x, float y, float width, float height) {
        return RectShape.fromCenter(new Vector2(x, y), new Vector2(width, height));
    }

    @Override
    protected void drawShape(Batches batches) {
        batches.shapeRenderer.rect(this.pos.x, this.pos.y, this.size.x, this.size.y);
    }

    public Vector2 getSize() {
        return this.size;
    }

}
