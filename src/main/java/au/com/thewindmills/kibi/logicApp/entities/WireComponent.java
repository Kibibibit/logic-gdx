package au.com.thewindmills.kibi.logicApp.entities;

import com.badlogic.gdx.graphics.Color;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.LineShape;
import au.com.thewindmills.kibi.appEngine.objects.entities.ShapeEntity;
import au.com.thewindmills.kibi.appEngine.utils.constants.Colors;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;

public class WireComponent extends ShapeEntity {

    private ComponentInOut start;
    private ComponentInOut end;

    private boolean startInput;

    private Color color;

    public WireComponent(AppData data, String layer, int depth, ComponentInOut start) {
        super(data, layer, depth, start.getPos(), new LineShape(start.getPos(), data.getMouse().getCameraPos(), 5f));
        this.start = start;

        startInput = this.start.isInput();

        color = this.getShape().getFillColor();
        this.setCanDelete(true);
        data.getMouse().startDrawingWire(this);
    }

    public void setEnd(ComponentInOut end) {
        this.end = end;

        ComponentInOut input = start;
        ComponentInOut output = end;

        if (!startInput) {
            input = end;
            output = start;
        }
        this.getData().getConnectionMap()
            .addConnection(input.getParent().getModel(), input.getNode(), output.getParent().getModel(), output.getNode());

    }

    @Override
    protected void draw(Batches batches) {
        ComponentInOut output = start;
        if (startInput) {
            if (end != null) {
                if (!end.isInput()) {
                    output = end;
                    
                }
            } else {
                this.setFillColor(color);
                return;
            }
        }
        
        if (output.getParent().getModel().getOutputBits()[output.getNode()]) {
            this.setFillColor(Colors.BLUE);
        } else {
            this.setFillColor(color);
        }
    }

    @Override
    public void preStep(float delta) {
        if (start.willDispose()) {
            this.markForDisposal();
        }

        if (end != null) {
            if (end.willDispose()) {
                this.markForDisposal();
            }
        }
    }

    @Override
    protected void step(float delta) {
        this.getShape().setPos(start.getPos().x, start.getPos().y);
        if (this.end != null) {
            this.getShape().setSize(end.getPos().x, end.getPos().y);
        }
    }

    @Override
    public void dispose() {
        if (end != null) {
            ComponentInOut input = start;
            ComponentInOut output = end;

            if (!startInput) {
                input = end;
                output = start;
            }
            this.getData().getConnectionMap()
                .removeConnection(input.getParent().getModel(), input.getNode(), output.getParent().getModel(), output.getNode());
        }

        
    }


    public ComponentInOut getStart() {
        return this.start;
    }

}
