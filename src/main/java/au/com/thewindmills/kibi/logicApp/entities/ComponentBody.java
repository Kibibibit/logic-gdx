package au.com.thewindmills.kibi.logicApp.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.AbstractShape;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.RectShape;
import au.com.thewindmills.kibi.appEngine.objects.entities.DraggableShapeEntity;
import au.com.thewindmills.kibi.appEngine.utils.constants.Colors;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;
import au.com.thewindmills.kibi.appEngine.utils.io.json.JSONUtils;
import au.com.thewindmills.kibi.logicApp.models.LogicModel;

public class ComponentBody extends DraggableShapeEntity {

    private static final float PER_INPUT = 27.5f;
    private static final float WIDTH = 60;
    private boolean mouseIn = false;

    protected LogicModel model;

    protected List<ComponentInOut> children;

    public ComponentBody(AppData data, String layer, int depth, Vector2 pos, String modelFileName, AbstractShape shape) {
        super(data, layer, depth, pos, shape);

        this.model = LogicModel.fromJson(JSONUtils.loadJsonObject(modelFileName), getData().getConnectionMap());
        
        this.model.setEntity(this);

        this.children = new ArrayList<ComponentInOut>();
        this.init(pos);
        this.setCanDelete(true);
    }

    public ComponentBody(AppData data, String layer, int depth, Vector2 pos, String modelFileName) {
        this(data, layer, depth, pos, modelFileName, new RectShape());
    }

    protected void init(Vector2 pos) {
        this.setTextColor(Colors.BLACK);
        int highCount = Math.max(this.model.getInputCount(), this.model.getOutputCount());
        this.getShape().setSize(WIDTH, highCount*PER_INPUT);
        this.setPos(pos);

        for (int in = 0; in < model.getInputCount(); in++) {
            this.children.add(new ComponentInOut(this, in, true));
            if (in > highCount) {
                highCount = in;
            }
        }

        for (int out = 0; out < model.getOutputCount(); out++) {
            this.children.add(new ComponentInOut(this, out, false));
            if (out > highCount) {
                highCount = out;
            }
        }
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
                    batches.spriteBatch, this.model.getName(),
                    this.getPos().x,
                    this.getPos().y);
        }
    }

    @Override
    protected void draw(Batches batches) {
        
    }

    @Override
    protected void step(float delta) {
        
    }

    public void onModelUpdate() {}


    @Override
    public void markForDisposal() {
        for (ComponentInOut cin : children) {
            cin.markForDisposal();
        }

        super.markForDisposal();
    }

    @Override
    public void dispose() {
        this.model.dispose();
    }

    public LogicModel getModel() {
        return this.model;
    }
    
}
