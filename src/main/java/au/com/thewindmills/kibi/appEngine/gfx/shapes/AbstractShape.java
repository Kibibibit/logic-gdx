package au.com.thewindmills.kibi.appEngine.gfx.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.utils.Batches;
import au.com.thewindmills.kibi.appEngine.utils.constants.DrawConstants;

public abstract class AbstractShape {

    protected Vector2 pos;
    protected EShape shape;
    protected Color strokeColor = DrawConstants.STROKE_COLOR;
    protected Color fillColor = DrawConstants.FILL_COLOR;


    public AbstractShape(EShape shape, Vector2 pos) {
        this.pos = new Vector2(pos.x, pos.y);
        this.shape = shape;
    }

    public AbstractShape(EShape shape, float x, float y) {
        this.pos = new Vector2(x, y);
        this.shape = shape;
    }

    public EShape geShape() {
        return this.shape;
    }


    protected abstract void drawShape(Batches batches);

    public void draw(Batches batches) {
        batches.shapeRenderer.set(ShapeType.Filled);
        drawShape(batches);
        batches.shapeRenderer.set(ShapeType.Line);
        drawShape(batches);
    }

}
