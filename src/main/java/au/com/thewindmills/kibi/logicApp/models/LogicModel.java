package au.com.thewindmills.kibi.logicApp.models;

import java.util.LinkedList;
import java.util.Queue;

import au.com.thewindmills.kibi.appEngine.utils.maths.RandomUtils;

public abstract class LogicModel extends AbstractModel {

    private static final int MIN_DELAY = 1;
    private static final int MAX_DELAY = 8;

    protected final int propagationDelay;

    protected int outputState = 0;
    protected int previouseOutputState = 0;

    protected int inputState = 0;

    protected int inputCount;
    protected int outputCount;

    protected Queue<Integer> inputQueue;

    public LogicModel(int inputCount, int outputCount) {
        this.propagationDelay = RandomUtils.randomIntInRange(MIN_DELAY, MAX_DELAY);
        inputQueue = new LinkedList<Integer>();
        this.inputCount = inputCount;
        this.outputCount = outputCount;
    }


    protected abstract int onUpdate(int input);

    public void update() {

        inputQueue.add(inputState);

        if (inputQueue.size() >= propagationDelay) {
            outputState = onUpdate(inputQueue.poll());
            if (previouseOutputState != outputState) {
                // Update everything here!
            }
        }
        

    }

    

    public int getOutputState() {
        return this.outputState;
    }

    public int getInputCount() {
        return this.inputCount;
    }

    public int getOutputCount() {
        return this.outputCount;
    }



    
}
