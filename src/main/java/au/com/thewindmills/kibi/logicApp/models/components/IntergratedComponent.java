package au.com.thewindmills.kibi.logicApp.models.components;

import java.util.Map;

import org.json.simple.JSONObject;

import au.com.thewindmills.kibi.appEngine.utils.maths.BinaryUtils;
import au.com.thewindmills.kibi.logicApp.models.ConnectionMap;
import au.com.thewindmills.kibi.logicApp.models.LogicModel;

/**
 * A representation of an IC. Contains its' own set of logic models and own connection map,
 * 
 * @author Kibi
 */
public class IntergratedComponent extends LogicModel {

    public static final String FIELD_CHILDREN = "children";

    private ConnectionMap internalMap;

    private IntergratedComponentInOut input;
    private IntergratedComponentInOut output;

    public IntergratedComponent(String name, int inputCount, int outputCount, ConnectionMap connectionMap) {
        super(name, inputCount, outputCount, connectionMap);
        this.internalMap = new ConnectionMap();

        input = new IntergratedComponentInOut(this.name+"|input", inputCount, internalMap, true, this);
        output = new IntergratedComponentInOut(this.name+"|output", outputCount, internalMap, false, this);
    }

    public ConnectionMap getInternalMap() {
        return this.internalMap;
    }

    @Override
    public void result() {
        this.outputBits = this.output.getOutputBits().clone();
    }

    @Override
    public void doUpdate() {
        input.doUpdate();
    }

    @Override
    protected void init() {
        
    }

    protected void doPostUpdate() {
        this.result();
        this.postUpdate();
    }

    protected int getInputState() {
        return BinaryUtils.getValueFromBits(this.inputBits);
    }

    @Override
    protected Map<String, Object> addToJsonMap(Map<String, Object> map) {
        map.put(FIELD_CHILDREN, this.internalMap.getModels());
        
        return map;
    }

    @Override
    protected void getFromJson(JSONObject object) {
        
        
    }


    
    
}
