package au.com.thewindmills.kibi.logicApp.models;

import java.util.HashMap;
import java.util.Map;

import au.com.thewindmills.kibi.logicApp.utils.BinaryUtils;

public class TruthTable extends LogicModel {


    public TruthTable(int inputCount, int outputCount, ConnectionMap connectionMap) {
        super(inputCount, outputCount, connectionMap);
    }


    private Map<Integer, boolean[]> table;


    @Override
    protected void init() {
        table = new HashMap<Integer, boolean[]>((int) Math.pow(2, inputCount));

        for (int i = 0; i < Math.pow(2, inputCount); i++) {
            table.put(i,  new boolean[outputCount]);
        }

        System.out.println(id + " truth table just created!");
    }

    public void setRow(int input, boolean[] output) {
        if (output.length == outputCount) {
            table.replace(input, output);
        } else {
            System.err.println("BAD OUTPUT BITS");
        }
        this.result();
        
    }

    public void setRow(String input, String output) {
        setRow(BinaryUtils.parse(input), BinaryUtils.getBitsFromString(output));
    }

    public boolean[] getRow(int input) {
        return table.get(input);
    }

    public boolean[] getRow(boolean[] input) {
        if (input.length == this.getInputCount()) {
            return table.get(BinaryUtils.getValueFromBits(input));
        } else {

            System.err.println("BAD BOOLEAN STRING");

            return new boolean[input.length];
        }
    }


    public int size() {
        return table.size();
    }


    @Override
    public void result() {
        this.outputBits = this.getRow(this.inputBits);
        this.postUpdate();      
    }
}
