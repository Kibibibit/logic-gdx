package au.com.thewindmills.kibi.logicApp.models;

import au.com.thewindmills.kibi.appEngine.utils.maths.RandomUtils;
import au.com.thewindmills.kibi.logicApp.utils.Binary;

public abstract class LogicModel extends AbstractModel {

    private static final int MIN_DELAY = 1;
    private static final int MAX_DELAY = 8;

    protected final int propagationDelay;

    protected int outputState = 0;

    protected int inputState = 0;

    protected int inputCount;
    protected int outputCount;

    protected ConnectionMap connectionMap;

    private boolean[] inputBits;
    private boolean[] outputBits;

    public LogicModel(int inputCount, int outputCount, ConnectionMap connectionMap) {
        this.propagationDelay = RandomUtils.randomIntInRange(MIN_DELAY, MAX_DELAY);

        this.connectionMap = connectionMap;
        this.inputCount = inputCount;
        this.outputCount = outputCount;

        inputBits = new boolean[inputCount];
        outputBits = new boolean[outputCount];

        for (int i = 0; i < inputCount; i++) {
            inputBits[i] = false;
        }
        for (int i = 0; i < outputCount; i++) {
            outputBits[i] = false;
        }

    }

    public void update(int pos, boolean value) {

        Thread timerThread = new Thread(new Runnable() {
            public void run() {
                try {

                    inputBits[pos] = value;
                    Thread.sleep(propagationDelay);

                    

                    postDelay();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        timerThread.start();

    }

    private void postDelay() {
        System.out.println("POST DELAY " + id);
        this.onUpdate(Binary.getValueFromBits(inputBits));

    }

    public void trigger(int pos, boolean value) {
        System.out.println("MANUAL UPDATE! Setting gate with id " + id + " to input " + pos + " -> " + value);
        inputBits[pos] = value;
        this.postDelay();
    }

    protected abstract void onUpdate(int input);

    protected void postUpdate(int output) {
        this.setOutputBits(output);
        for (int i = 0; i < outputCount; i++) {
            System.out.println(id + " - " + outputBits[i]);
            this.connectionMap.update(this, i, outputBits[i]);
        }

    }

    protected int updateInputState() {
        return connectionMap.getInputState(this);
    }

    public int getInputState() {
        return this.inputState;
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

    public abstract int result(int input);

    public int result(String input) {
        return this.result(Binary.parse(input));
    }

    public int outputAtBit(int pos) {
        return ((outputState >> pos) % 2) << pos;
    }

    private void setOutputBits(int value) {
        int power = this.getOutputCount();

        while (value > 0) {
            if (value >= Math.pow(2, power)) {
                outputBits[power] = true;
                value -= Math.pow(2, power);
            }
            power--;
        }
    }

    public abstract int result(int input, int pos);

}
