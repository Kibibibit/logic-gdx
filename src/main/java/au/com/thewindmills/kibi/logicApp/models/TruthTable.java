package au.com.thewindmills.kibi.logicApp.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import au.com.thewindmills.kibi.appEngine.utils.maths.BinaryUtils;

/**
 * This class maps a binary input to a binary out,
 * and can be used to represent any logic chip that does not have any loop backs.
 * <br><br>
 * A new Truth Table will return 0 for any input, and must be set up with {@link TruthTable#setRow()}
 *
 * @author Kibi
 */
public class TruthTable extends LogicModel {

    public static final String FIELD_TRUTH_TABLE = "truthtable";

    /**
     * Stores the mapping from an integer to the state of each bit of the output.
     */
    private Map<Integer, boolean[]> table;

    public TruthTable(String name, int inputCount, int outputCount, ConnectionMap connectionMap) {
        super(name, inputCount, outputCount, connectionMap);
    }


    @Override
    protected void init() {
        table = new HashMap<Integer, boolean[]>((int) Math.pow(2, inputCount));

        //Set all rows to all false
        for (int i = 0; i < Math.pow(2, inputCount); i++) {
            table.put(i,  new boolean[outputCount]);
        }
    }

    /**
     * Sets the output bits of the given input to the given output,
     * then updates the gate.
     * @param input
     * @param output
     */
    public void setRow(int input, boolean[] output) {
        if (output.length == outputCount) {
            table.replace(input, output);
        } else {
            System.err.println("BAD OUTPUT BITS");
        }
        this.doUpdate();
        
    }

    /**
     * Sets the output bits of the given input to the given output,
     * then updates the gate.
     * @param input
     * @param output
     */
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
    }


    @Override
    protected Map<String, Object> addToJsonMap(Map<String, Object> map) {

        map.put(FIELD_TYPE, TYPE_TABLE);

        Map<Integer, String> tableMap = new HashMap<>();

        for (Entry<Integer, boolean[]> entry : table.entrySet()) {
            tableMap.put(entry.getKey(), BinaryUtils.getStringFromBits(entry.getValue()));
        }

        map.put(FIELD_TRUTH_TABLE, tableMap);

        return map;
    }


    @Override
    protected void getFromJson(JSONObject object) {

        if (!object.containsKey(FIELD_TRUTH_TABLE)) {
            LOGGER.severe("JSON is missing field " + FIELD_TRUTH_TABLE);
            return;
        }

        if (!(object.get(FIELD_TRUTH_TABLE) instanceof JSONObject)) {
            LOGGER.severe("JSON has " + FIELD_TRUTH_TABLE + " in wrong type!");
            return;
        }

        //Go through inputs and map
        for (int i = 0; i < Math.pow(2,inputCount); i++) {
            setRowFromJson(i, (JSONObject) object.get(FIELD_TRUTH_TABLE));
        }

        
    }

    private void setRowFromJson(int index, JSONObject object) {

        if (!object.containsKey(String.valueOf(index))) {
            LOGGER.severe("MISSING KEY " + index + "!");
            return;
        }

        setRow(index, BinaryUtils.getBitsFromString((String) object.get(String.valueOf(index))));
        

    }


}
