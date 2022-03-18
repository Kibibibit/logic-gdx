package au.com.thewindmills.kibi.logicApp.entities;

import static au.com.thewindmills.kibi.appEngine.utils.constants.DrawConstants.NODE_RADIUS;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.gfx.shapes.AbstractShape;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.CircleShape;
import au.com.thewindmills.kibi.appEngine.gfx.ui.PopUpListener;
import au.com.thewindmills.kibi.appEngine.gfx.ui.components.UiTextPopUp;
import au.com.thewindmills.kibi.appEngine.objects.entities.DraggableShapeEntity;
import au.com.thewindmills.kibi.appEngine.utils.constants.Colors;
import au.com.thewindmills.kibi.appEngine.utils.constants.Layers;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;

public class ComponentInOut extends DraggableShapeEntity implements PopUpListener {

    private final int node;
    private final boolean input;
    private String label;

    private final Vector2 parentOffset;

    private final static int HOR_OFFSET = 10;

    private final ComponentBody parent;


    private boolean mouseIn = false;

    public ComponentInOut(ComponentBody parent, int node, boolean input) {
        super(parent.getData(), parent.getLayer(), parent.getDepth() + 1, parent.getPos().cpy(),
                new CircleShape(0, 0, NODE_RADIUS));
        this.label = (input ? parent.getModel().getInputNames() : parent.getModel().getOutputNames())[node];
        if (this.label.isBlank()) {
            this.label = (input ? "IN " : "OUT ") + node;
        }
        this.parent = parent;
        this.node = node;
        this.input = input;
        this.setTextColor(Colors.BLACK);
        AbstractShape parentShape = parent.getShape();

        float xOffset = this.input ? -HOR_OFFSET : parentShape.getWidth() + HOR_OFFSET;

        int count = this.input ? parent.getModel().getInputCount() : parent.getModel().getOutputCount();
        float div = parentShape.getHeight() / count;

        float yOffset = div * this.node + (div / 2);

        this.parentOffset = new Vector2(xOffset, yOffset);

        this.updatePosition();

    }

    public void light() {
        this.parentOffset.set(parentOffset.x * 2, 0);
    }

    private void updatePosition() {
        if (this.parentOffset != null) {
            this.getPos().set(parent.getPos().cpy().add(this.parentOffset));
            this.getShape().setPos(this.getPos());
        }
        
    }

    public ComponentBody getParent() {
        return this.parent;
    }

    public int getNode() {
        return this.node;
    }

    @Override
    public void onMouseEnter() {
        this.mouseIn = true;
    }

    @Override
    public void onMouseLeave() {
        this.mouseIn = false;
    }

    @Override
    public void onRenderText(Batches batches) {
        if (mouseIn) {
            batches.font.draw(
                    batches.spriteBatch, label,
                    this.getPos().x,
                    this.getPos().y);
        }
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

    public boolean isInput() {
        return this.input;
    }

    @Override
    public void doOnMouseReleased(int button) {
        if (button == Input.Buttons.RIGHT) {

            this.getData().displayPopUp(this, new UiTextPopUp(this.getData(), 0, 200, 100));

        }
    }

    @Override
    public void onMouseDragged() {
        if (!this.input || !this.getData().getConnectionMap().inputConnected(this.getParent().getModel(), this.node))
            new WireComponent(this.getData(), Layers.BELOW_MAIN, 0, this);
    }

    @Override
    public void onPopUpResult(String result) {
        if (result != null) {
            if (!result.strip().isEmpty()) {
                this.label = result;
                (this.input ? this.parent.getModel().getInputNames() : this.parent.getModel().getOutputNames())[this.node] = result;
            }
        }

    }

    public String getLabel() {
        return this.label;
    }

}
