package au.com.thewindmills.kibi.logicApp.models.components;

import au.com.thewindmills.kibi.appEngine.utils.maths.BinaryUtils;
import au.com.thewindmills.kibi.logicApp.models.ConnectionMap;
import au.com.thewindmills.kibi.logicApp.models.TruthTable;

public class IntergratedComponentInOut extends TruthTable {

    private int nodeCount;
    private boolean input;
    private IntergratedComponent parent;
    private boolean ready = false;

    public IntergratedComponentInOut(String name, int nodeCount, ConnectionMap connectionMap, boolean input,
            IntergratedComponent parent) {
        super(name, nodeCount, nodeCount, connectionMap);
        doConstruct(nodeCount, input, parent);
        

    }

    public IntergratedComponentInOut(long id, String name, int nodeCount, ConnectionMap connectionMap, boolean input, IntergratedComponent parent) {
        super(id, name, nodeCount, nodeCount, connectionMap);
        doConstruct(nodeCount, input, parent);
    }

    private void doConstruct(int nodeCount, boolean input, IntergratedComponent parent) {
        this.nodeCount = nodeCount;
        this.input = input;

        this.init();
    }

    public void readyUp() {
        this.ready = true;
    }

    @Override
    public void result() {
        if (ready) {
            if (this.input) {
                this.outputBits = this.getRow(this.parent.getInputState());
            } else {
                super.result();
            }
        }
    }

    @Override
    protected void postUpdate() {
        if (ready) {
            if (input) {
                super.postUpdate();
                return;
            }

            if (this.previousOutputState != BinaryUtils.getValueFromBits(outputBits)) {
                this.parent.doPostUpdate();

            }

            this.previousOutputState = BinaryUtils.getValueFromBits(outputBits);
        }
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
    @Override
    public IntergratedComponentInOut makeInternalClone(ConnectionMap connectionMap, long id) {
        IntergratedComponentInOut clone = new IntergratedComponentInOut(id, this.name, this.nodeCount, connectionMap, this.input, this.parent);
        clone.setTable(this.getTable());
        return clone;
    }

}
