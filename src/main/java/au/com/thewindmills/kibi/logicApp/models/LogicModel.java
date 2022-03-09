package au.com.thewindmills.kibi.logicApp.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import au.com.thewindmills.kibi.appEngine.objects.entities.AppEntity;
import au.com.thewindmills.kibi.appEngine.utils.maths.BinaryUtils;
import au.com.thewindmills.kibi.appEngine.utils.maths.RandomUtils;
import au.com.thewindmills.kibi.logicApp.entities.ComponentBody;
import au.com.thewindmills.kibi.logicApp.models.components.IntegratedComponent;

/**
 * Represents a component that makes up a logic circuit.
 * Can either be basic gates, represented by {@link TruthTables},
 * or more complex circuits through {@link IntegratedComponent}s
 * <br><br>
 * This component is abstract, and needs to be created in engine with a {@link ComponentBody}
 * 
 * @author Kibi
 */
public abstract class LogicModel extends AbstractModel {

    protected static final Logger LOGGER = Logger.getLogger("LogicModel");


    public static final String FIELD_NAME = "name";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_INPUT_COUNT = "inputcount";
    public static final String FIELD_OUTPUT_COUNT = "outputcount";
    public static final String TYPE_TABLE = "table";
    public static final String TYPE_IC = "ic";

    public static final String CLASS_STRING = "STRING";
    public static final String CLASS_INTEGER = "INTEGER";


    /**
     * The minimum propagation delay in milliseconds
     * @see LogicModel#propagationDelay
     */
    private static final int MIN_DELAY = 1;

    /**
     * The maximum propagation delay in milliseconds
     * @see LogicModel#propagationDelay
     */
    private static final int MAX_DELAY = 8;

    /**
     * After recieving a change to this models inputs,
     * it will take this many milliseconds before the gate actually changes output state.
     * <br><br>
     * This helps with metastable circuits like latches
     */
    protected final int propagationDelay;

    /**
     * The previous output of the circuit. Used to determine if the model needs to update its connected models
     */
    protected int previousOutputState = -1;

    /**
     * The number of inputs for this model
     */
    protected int inputCount;

    /**
     * The number of outputs for this model
     */
    protected int outputCount;

    /**
     * The linked connection map that stores the connection between this model and all others in the scene/{@link IntegratedComponent}
     */
    protected ConnectionMap connectionMap;

    /**
     * The state of each of the input bits, where true -> 1, false -> 0
     */
    protected boolean[] inputBits;

    /**
     * The state of each of the output bits, where true -> 1, false -> 0
     */
    protected boolean[] outputBits;

    /**
     * The name of this model
     */
    protected final String name;

    /**
     * If this model is connected to a {@link AppEntity}, it uses this to update them
     */
    private ComponentBody entity = null;


    public LogicModel(String name, int inputCount, int outputCount, ConnectionMap connectionMap) {
        // Propagation delay needs to be random
        this.propagationDelay = RandomUtils.randomIntInRange(MIN_DELAY, MAX_DELAY);

        this.connectionMap = connectionMap;
        this.inputCount = inputCount;
        this.outputCount = outputCount;

        this.name = name;

        inputBits = new boolean[inputCount];
        outputBits = new boolean[outputCount];

        this.init();
        this.doUpdate();

    }

    /**
     * Any logic that needs to be done during the creation of the model should be placed here
     */
    protected abstract void init();

    /**
     * Called whenever the input state of the given bit pos is changed.
     * The output or input bits will not change until {@link LogicModel#propagationDelay} milliseconds have passed
     * @param pos - the bit that changed
     * @param value - the new value of that bit
     */
    public void update(int pos, boolean value) {

        Thread timerThread = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(propagationDelay);
                    inputBits[pos] = value;

                    doUpdate();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        timerThread.start();

    }

    /**
     * Runs {@link LogicModel#result()} and then {@link LogicModel#postUpdate()}
     */
    public void doUpdate() {
        result();
        postUpdate();
    }

    /**
     * This method will bypass the propagation delay and set the bit of a gate.
     * Should only be used for testing, or for components such as buttons
     * @param pos - The bit to set
     * @param value - The value of the new bit
     */
    public void trigger(int pos, boolean value) {
        inputBits[pos] = value;
        doUpdate();
    }

    /**
     * Called after {@link LogicModel#result()}. Checks to see if the output state of the model has changed,
     * and if it has, update any connected models through the {@link ConnectionMap}
     */
    protected void postUpdate() {

        if (this.previousOutputState != BinaryUtils.getValueFromBits(outputBits)) {
            System.out.println(id + " is changing output state to " + BinaryUtils.getValueFromBits(outputBits));
            this.connectionMap.update(this, outputBits);

        }

        this.previousOutputState = BinaryUtils.getValueFromBits(outputBits);

        if (this.entity != null) {
            this.entity.onModelUpdate();
        }

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

    /**
     * Do any subclass specific conversions for json, eg adding truth table rows, or making
     * references to existing models
     * @param map
     * @return
     */
    protected abstract Map<String, Object> addToJsonMap(Map<String, Object> map);

    /**
     * Converts this model into a JSONObject for saving
     * @return
     */
    public JSONObject toJson() {

        Map<String, Object> jsonMap = new HashMap<>();

        jsonMap.put(FIELD_NAME, this.name);
        jsonMap.put(FIELD_INPUT_COUNT, this.inputCount);
        jsonMap.put(FIELD_OUTPUT_COUNT, this.outputCount);

        jsonMap = this.addToJsonMap(jsonMap);


        return new JSONObject(jsonMap);
    }

    protected abstract void getFromJson(JSONObject object);

    protected static boolean checkField(JSONObject object, String fieldname, String type) {
        if (object.containsKey(fieldname)) {

            switch(type) {
                case CLASS_STRING:
                    if (object.get(fieldname) instanceof String) {
                        return true;
                    }
                    break;

                case CLASS_INTEGER:
                    //This will need casting after the fact
                    if (object.get(fieldname) instanceof Long) {
                       return true;
                    }
                    break;

                default:
                    LOGGER.severe("Unrecognised type " + type);
                    return false;
            }

            LOGGER.severe("Field " + fieldname + " is not of type " + type + "!");
            return false;

        }
        LOGGER.severe("Object is missing field " + fieldname + "!");
        return false;
    }

    public static LogicModel fromJson(JSONObject object, ConnectionMap connectionMap) {

        //Check that all relevant fields are there
        Map<String, String> fields = new HashMap<>();

        fields.put(FIELD_NAME, CLASS_STRING);
        fields.put(FIELD_INPUT_COUNT, CLASS_INTEGER);
        fields.put(FIELD_OUTPUT_COUNT, CLASS_INTEGER);
        fields.put(FIELD_TYPE, CLASS_STRING);
        for (Entry<String, String> field : fields.entrySet()) {
            if (!checkField(object, field.getKey(), field.getValue())) {
                return null;
            }
        }

        
        String type = (String) object.get(FIELD_TYPE);
        String name = (String) object.get(FIELD_NAME);
        int inputCount = (int) ((Long) object.get(FIELD_INPUT_COUNT)).longValue();
        int outputCount = (int) ((Long) object.get(FIELD_OUTPUT_COUNT)).longValue();

        LogicModel out;

        if (type.equals(TYPE_IC)) {
            out = new IntegratedComponent(name, inputCount, outputCount, connectionMap);
        } else if (type.equals(TYPE_TABLE)) {
            out = new TruthTable(name, inputCount, outputCount, connectionMap);
        } else {
            LOGGER.severe("Unrecognised type " + type);
            return null;
        }

        out.getFromJson(object);

        return out;


    }

    public void setEntity(ComponentBody body) {
        this.entity = body;
    }



}
