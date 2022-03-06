package au.com.thewindmills.kibi.logicApp.models.components;

import au.com.thewindmills.kibi.logicApp.models.ConnectionMap;
import au.com.thewindmills.kibi.logicApp.models.LogicModel;

/**
 * A representation of an IC. Contains its' own set of logic models and own connection map,
 * 
 * @author Kibi
 */
public class IntegratedComponent extends LogicModel {

    

    public IntegratedComponent(int inputCount, int outputCount, ConnectionMap connectionMap) {
        super(inputCount, outputCount, connectionMap);
    }

    @Override
    public void result() {
        
    }

    @Override
    protected void init() {
        
    }


    
    
}
