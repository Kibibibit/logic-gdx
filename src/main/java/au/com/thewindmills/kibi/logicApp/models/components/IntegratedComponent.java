package au.com.thewindmills.kibi.logicApp.models.components;

import au.com.thewindmills.kibi.logicApp.models.ConnectionMap;
import au.com.thewindmills.kibi.logicApp.models.LogicModel;

public class IntegratedComponent extends LogicModel {

    

    public IntegratedComponent(int inputCount, int outputCount, ConnectionMap connectionMap) {
        super(inputCount, outputCount, connectionMap);
    }

    @Override
    public int result(int input) {
        return 0;
    }

    @Override
    public int result(int input, int pos) {
        return 0;
    }

    @Override
    protected void onUpdate(int input) {
        
    }

    
    
}
