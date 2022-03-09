package au.com.thewindmills.kibi.logicApp.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.RectShape;
import au.com.thewindmills.kibi.appEngine.objects.entities.DraggableShapeEntity;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;
import au.com.thewindmills.kibi.appEngine.utils.io.json.JSONUtils;
import au.com.thewindmills.kibi.logicApp.models.LogicModel;

public class ComponentBody extends DraggableShapeEntity {

    protected LogicModel model;

    protected List<ComponentInOut> children;

    public ComponentBody(AppData data, String layer, int depth, Vector2 pos, String modelFileName) {
        super(data, layer, depth, pos, new RectShape());

        this.model = LogicModel.fromJson(JSONUtils.loadJsonObject(modelFileName), getData().getConnectionMap());
        
        this.model.setEntity(this);

        this.children = new ArrayList<ComponentInOut>();



        this.init(pos);
        

    }

    protected void init(Vector2 pos) {
        int highCount = Math.max(this.model.getInputCount(), this.model.getOutputCount());

        //TODO: Make this better
        ((RectShape) this.getShape()).setSize(50, highCount*25);
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
