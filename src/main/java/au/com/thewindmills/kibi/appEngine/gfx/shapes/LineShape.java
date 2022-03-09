package au.com.thewindmills.kibi.appEngine.gfx.shapes;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;
import au.com.thewindmills.kibi.appEngine.utils.maths.AngleUtils;
import au.com.thewindmills.kibi.appEngine.utils.maths.VectorUtils;

import static au.com.thewindmills.kibi.appEngine.utils.constants.DrawConstants.STROKE_WIDTH;

public class LineShape extends AbstractShape {

    private Vector2 end;
    private float weight;

    public LineShape() {
        this(0,0,0,0,0);
    }

    public LineShape(float x1, float y1, float x2, float y2, float weight) {
        super(EShape.LINE, x1, y1);
        this.end = new Vector2(x2,y2);
        this.weight = weight;
    }

    public LineShape(Vector2 pos, Vector2 end, float weight) {
        super(EShape.LINE , pos);
        this.end = end.cpy();
        this.weight = weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setEnd(Vector2 end) {
        this.end.set(end.x, end.y);
    }

    public void setEnd(float x, float y) {
        this.end.set(x,y);
    }

    @Override
    public boolean inBounds(float x, float y) {

        Vector2 point = new Vector2(x,y);

        double angle = Math.atan2(this.end.y - this.pos.y , this.end.x - this.pos.x);
        double angle2 = (Math.PI/2) + angle;

        Vector2 a = this.pos.cpy().add(AngleUtils.lengthDir(angle2, this.weight/2));
        Vector2 b = this.pos.cpy().sub(AngleUtils.lengthDir(angle2, this.weight/2));
        Vector2 c = this.end.cpy().add(AngleUtils.lengthDir(angle2, this.weight/2));
        Vector2 d = this.end.cpy().sub(AngleUtils.lengthDir(angle2, this.weight/2));

        float area = this.pos.dst(this.end) * a.dst(b);

        float triArea = VectorUtils.triArea(a, b, point) +
                        VectorUtils.triArea(b, c, point) +
                        VectorUtils.triArea(c, d, point) +
                        VectorUtils.triArea(d, a, point);

        return triArea < area;
    }

    @Override
    protected void drawStroke(Batches batches) {
        batches.shapeRenderer.rectLine(this.pos, this.end, this.weight + 2*STROKE_WIDTH);        
    }

    @Override
    protected void drawFill(Batches batches) {
        batches.shapeRenderer.rectLine(this.pos, this.end, this.weight);   
        
    }

    @Override
    public float getHeight() {
        
        return (float) Math.abs(this.pos.y - this.end.y);
    }

    @Override
    public float getWidth() {
        return (float) Math.abs(this.pos.x - this.end.x);
    }

    @Override
    public void setHeight(float height) {
        this.end.set(this.end.y, this.pos.y + height);
        
    }

    @Override
    public void setWidth(float width) {
        this.end.set(this.end.x, this.pos.x + width);
        
    }

    @Override
    public void setSize(float width, float height) {
        this.end.set(width, height);
    }
    
    
}
