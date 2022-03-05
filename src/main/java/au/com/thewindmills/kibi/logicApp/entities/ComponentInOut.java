package au.com.thewindmills.kibi.logicApp.entities;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.gfx.shapes.CircleShape;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.RectShape;
import au.com.thewindmills.kibi.appEngine.objects.entities.DraggableShapeEntity;
import static au.com.thewindmills.kibi.appEngine.utils.constants.DrawConstants.NODE_RADIUS;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;

public class ComponentInOut extends DraggableShapeEntity {

    private final int node;
    private final boolean input;

    private final Vector2 parentOffset;

    private final static int HOR_OFFSET = 10;

    private final ComponentBody parent;

    public ComponentInOut(ComponentBody parent, int node, boolean input) {
        super(parent.getData(), parent.getLayer(), parent.getDepth() + 1, parent.getPos().cpy(),
                new CircleShape(0, 0, NODE_RADIUS));

        this.parent = parent;

        this.node = node;
        this.input = input;

        RectShape parentBox = (RectShape) parent.getShape();

        float xOffset = this.input ? -HOR_OFFSET : parentBox.getSize().x + HOR_OFFSET;

        int count = this.input ? parent.getModel().getInputCount() : parent.getModel().getOutputCount();
        float div = parentBox.getSize().y / count;

        float yOffset = div*this.node + (div/2); 


        this.parentOffset = new Vector2(xOffset, yOffset);

        this.updatePosition();

    }

    private void updatePosition() {
        this.getPos().set(parent.getPos().cpy().add(this.parentOffset));
        this.getShape().setPos(this.getPos());
    }

    @Override
    protected void draw(Batches batches) {
    }

    @Override
    protected void preStep(float delta) {
        this.updatePosition();
    }

    @Override
    protected void step(float delta) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void mouseDragged() {

    }

}
