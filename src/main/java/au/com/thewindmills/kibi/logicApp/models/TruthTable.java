package au.com.thewindmills.kibi.logicApp.models;

import java.util.HashMap;
import java.util.Map;

import au.com.thewindmills.kibi.logicApp.utils.Binary;

public class TruthTable extends LogicModel {
    private Map<Integer, Integer> table;

    

    public TruthTable(int inputCount, int outputCount) {
        super(inputCount, outputCount);

        table = new HashMap<Integer, Integer>((int) Math.pow(2, inputCount));

        for (int i = 0; i < Math.pow(2, inputCount); i++) {
            table.put(i, 0);
        }
    }

    public void setRow(int input, int output) {
        table.replace(input, output);
    }

    public void setRow(String input, String output) {
        setRow(Binary.parse(input), Binary.parse(output));
    }

    public int result(int input) {
        return table.get(input);
    }

    public int result(String input) {
        return table.get(Binary.parse(input));
    }

    public int size() {
        return table.size();
    }

    public boolean result(int input, int pos) {
        return (table.get(input) >> pos) % 2 == 1;
    }

    @Override
    protected int onUpdate(int input) {
        return result(input);
    }
}
