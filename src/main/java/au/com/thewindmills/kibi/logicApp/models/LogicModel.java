package au.com.thewindmills.kibi.logicApp.models;

import au.com.thewindmills.kibi.appEngine.utils.maths.RandomUtils;
import au.com.thewindmills.kibi.logicApp.utils.BinaryUtils;

public abstract class LogicModel extends AbstractModel {

    private static final int MIN_DELAY = 1;
    private static final int MAX_DELAY = 8;

    protected final int propagationDelay;

    protected int previousOutputState = -1;

    protected int inputCount;
    protected int outputCount;

    protected ConnectionMap connectionMap;

    protected boolean[] inputBits;
    protected boolean[] outputBits;

    public LogicModel(int inputCount, int outputCount, ConnectionMap connectionMap) {
        this.propagationDelay = RandomUtils.randomIntInRange(MIN_DELAY, MAX_DELAY);

        this.connectionMap = connectionMap;
        this.inputCount = inputCount;
        this.outputCount = outputCount;

        inputBits = new boolean[inputCount];
        outputBits = new boolean[outputCount];

        this.init();

        this.result();

    }


    protected abstract void init();

    public void update(int pos, boolean value) {

        Thread timerThread = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(propagationDelay);
                    inputBits[pos] = value;

                    result();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        timerThread.start();

    }


    public void trigger(int pos, boolean value) {
        System.out.println("MANUAL UPDATE! Setting gate with id " + id + " to input " + pos + " -> " + value);
        inputBits[pos] = value;
        result();
    }

    protected void postUpdate() {

        if (this.previousOutputState != BinaryUtils.getValueFromBits(outputBits)) {
            System.out.println(id + " is changing output state to " + BinaryUtils.getValueFromBits(outputBits));
            this.connectionMap.update(this, outputBits);

        }

        this.previousOutputState = BinaryUtils.getValueFromBits(outputBits);

    }


    public int getInputCount() {
        return this.inputCount;
    }

    public int getOutputCount() {
        return this.outputCount;
    }

    /**
     * Calculate output bits based on input bits
     */
    public abstract void result();



    public void dispose() {

        this.connectionMap.dispose(this);

    }

    public boolean[] getOutputBits() {

        return this.outputBits.clone();

    }



}
