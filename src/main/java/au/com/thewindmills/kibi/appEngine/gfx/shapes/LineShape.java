package au.com.thewindmills.kibi.appEngine.gfx.shapes;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;

import static au.com.thewindmills.kibi.appEngine.utils.constants.DrawConstants.STROKE_WIDTH;

public class LineShape extends AbstractShape {

    private Vector2 end;
    private float width;

    public LineShape() {
        this(0,0,0,0,0);
    }

    public LineShape(float x1, float y1, float x2, float y2, float width) {
        super(EShape.LINE, x1, y1);
        this.end = new Vector2(x2,y2);
        this.width = width;
    }

    public LineShape(Vector2 pos, Vector2 end, float width) {
        super(EShape.LINE , pos);
        this.end = end.cpy();
        this.width = width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setEnd(Vector2 end) {
        this.end.set(end.x, end.y);
    }

    public void setEnd(float x, float y) {
        this.end.set(x,y);
    }

    @Override
    public boolean inBounds(float x, float y) {
        return false;
    }

    @Override
    protected void drawStroke(Batches batches) {
        batches.shapeRenderer.rectLine(this.pos, this.end, this.width + 2*STROKE_WIDTH);        
    }

    @Override
    protected void drawFill(Batches batches) {
        batches.shapeRenderer.rectLine(this.pos, this.end, this.width);   
        
    }

    
    
}
