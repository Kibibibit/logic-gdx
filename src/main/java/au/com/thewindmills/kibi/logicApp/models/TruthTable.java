package au.com.thewindmills.kibi.logicApp.models;

import java.util.HashMap;
import java.util.Map;

import au.com.thewindmills.kibi.logicApp.utils.Binary;

public class TruthTable extends LogicModel {
    private Map<Integer, Integer> table;

    //TODO: Change this to take int in -> list of bools out

    public TruthTable(int inputCount, int outputCount, ConnectionMap connectionMap) {
        super(inputCount, outputCount, connectionMap);

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

    @Override
    public int result(int input) {
        return table.get(input);
    }  

    @Override
    public int result(int input, int pos) {
        return ((table.get(input) >> pos) % 2) << pos;
    }

    public int size() {
        return table.size();
    }

    @Override
    public void onUpdate(int input) {
        int result = result(input);
        System.out.println("Getting input " + input + " in " + id + ", and returning " + result);
        this.postUpdate(result);
    }
}
