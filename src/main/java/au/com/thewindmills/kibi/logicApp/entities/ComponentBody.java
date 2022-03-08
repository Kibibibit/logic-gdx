package au.com.thewindmills.kibi.logicApp.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import au.com.thewindmills.kibi.appEngine.AppData;
import au.com.thewindmills.kibi.appEngine.gfx.shapes.AbstractShape;
import au.com.thewindmills.kibi.appEngine.objects.entities.DraggableShapeEntity;
import au.com.thewindmills.kibi.appEngine.utils.gfx.Batches;
import au.com.thewindmills.kibi.appEngine.utils.io.json.JSONUtils;
import au.com.thewindmills.kibi.logicApp.models.LogicModel;

public class ComponentBody extends DraggableShapeEntity {

    private LogicModel model;

    private List<ComponentInOut> children;

    public ComponentBody(AppData data, String layer, int depth, Vector2 pos, AbstractShape shape, String modelFileName) {
        super(data, layer, depth, pos, shape);

        this.model = LogicModel.fromJson(JSONUtils.loadJsonObject(modelFileName), getData().getConnectionMap());
        
        this.children = new ArrayList<ComponentInOut>();

        for (int in = 0; in < model.getInputCount(); in++) {
            this.children.add(new ComponentInOut(this, in, true));
        }

        for (int out = 0; out < model.getOutputCount(); out++) {
            this.children.add(new ComponentInOut(this, out, false));
        }


    }

    @Override
    protected void draw(Batches batches) {
        
    }

    @Override
    protected void step(float delta) {
        
    }


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
