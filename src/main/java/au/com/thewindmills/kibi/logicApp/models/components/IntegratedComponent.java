package au.com.thewindmills.kibi.logicApp.models.components;

import java.util.Map;

import org.json.simple.JSONObject;

import au.com.thewindmills.kibi.logicApp.models.ConnectionMap;
import au.com.thewindmills.kibi.logicApp.models.LogicModel;

/**
 * A representation of an IC. Contains its' own set of logic models and own connection map,
 * 
 * @author Kibi
 */
public class IntegratedComponent extends LogicModel {

    

    public IntegratedComponent(String name, int inputCount, int outputCount, ConnectionMap connectionMap) {
        super(name, inputCount, outputCount, connectionMap);
    }

    @Override
    public void result() {
        
    }

    @Override
    protected void init() {
        
    }

    @Override
    public JSONObject toJson() {
        return null;
    }

    @Override
    protected Map<String, Object> addToJsonMap(Map<String, Object> map) {
        return null;
    }

    @Override
    protected void getFromJson(JSONObject object) {
        
        
    }


    
    
}
