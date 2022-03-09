package au.com.thewindmills.kibi.logicApp.models.components;

import au.com.thewindmills.kibi.appEngine.utils.maths.BinaryUtils;
import au.com.thewindmills.kibi.logicApp.models.ConnectionMap;
import au.com.thewindmills.kibi.logicApp.models.TruthTable;

public class IntergratedComponentInOut extends TruthTable{

    private int nodeCount;
    private boolean input;
    private IntergratedComponent parent;

    public IntergratedComponentInOut(String name, int nodeCount, ConnectionMap connectionMap, boolean input, IntergratedComponent parent) {
        super(name, nodeCount, nodeCount, connectionMap);
        this.nodeCount = nodeCount;
        this.input = input; 
        this.parent = parent;
    }


    @Override
    public void result() {
        if (this.input) {
            this.outputBits = this.getRow(this.parent.getInputState());
        } else {
            super.result();
        }
    }

    @Override
    protected void postUpdate() {
        if (input) {
            super.postUpdate();
            return;
        }

        if (this.previousOutputState != BinaryUtils.getValueFromBits(outputBits)) {
            this.parent.doPostUpdate();

        }

        this.previousOutputState = BinaryUtils.getValueFromBits(outputBits);

    }


    protected void setInputBits(boolean[] bits) {
        this.inputBits = bits.clone();
    }

    @Override
    protected void init() {
        super.init();
        
        for (int i = 0; i < Math.pow(2, nodeCount); i++) {
            this.setRow(i, BinaryUtils.getBitsFromValue(i, nodeCount));
        }

    }
    
}
