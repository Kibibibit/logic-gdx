package au.com.thewindmills.kibi.logicApp.models.components;

import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import au.com.thewindmills.kibi.appEngine.utils.maths.BinaryUtils;
import au.com.thewindmills.kibi.logicApp.models.ConnectionMap;
import au.com.thewindmills.kibi.logicApp.models.LogicModel;

/**
 * A representation of an IC. Contains its' own set of logic models and own
 * connection map,
 * 
 * @author Kibi
 */
public class IntergratedComponent extends LogicModel {

    public static final String FIELD_INTERNAL_MAP = "internal_map";

    private ConnectionMap internalMap;

    private IntergratedComponentInOut input;
    private IntergratedComponentInOut output;
    private boolean ready = false;

    public IntergratedComponent(String name, int inputCount, int outputCount, ConnectionMap connectionMap) {
        super(name, inputCount, outputCount, connectionMap);
        this.internalMap = new ConnectionMap();

        input = new IntergratedComponentInOut(this.name + "|input", inputCount, internalMap, true, this);
        output = new IntergratedComponentInOut(this.name + "|output", outputCount, internalMap, false, this);
    }

    public ConnectionMap getInternalMap() {
        return this.internalMap;
    }

    public boolean getReady() {
        return this.ready;
    }

    @Override
    public void result() {
        this.outputBits = this.output.getOutputBits().clone();
    }

    @Override
    public void doUpdate() {
        if (ready) {
            input.doUpdate();
        }
    }

    @Override
    protected void init() {

    }

    public void createFromConnectionMap(ConnectionMap connectionMap, Map<Long, Long> idMap) {
        if (this.internalMap.id == connectionMap.id) {
            System.err.println("Don't recursively add connection maps!");
            return;
        }
        for (Entry<Long, LogicModel> entry : connectionMap.getModels().entrySet()) {

            LogicModel clone = entry.getValue().internalClone(this.internalMap, idMap.get(entry.getKey()));
            
            System.out.println(clone.getName() + ", " + clone.id);
            internalMap.getModels().put(clone.id, clone);

        }

        this.internalMap.copyConnectionsToIc(connectionMap, this, idMap);

    }

    private void readyUp() {
        this.ready = true;
        this.input.readyUp();
        this.output.readyUp();

    }

    protected void doPostUpdate() {
        if (ready) {
            this.result();
            this.postUpdate();
        }
    }

    protected int getInputState() {
        return BinaryUtils.getValueFromBits(this.inputBits);
    }

    @Override
    protected Map<String, Object> addToJsonMap(Map<String, Object> map) {
        map.put(FIELD_INTERNAL_MAP, this.internalMap.toJson());
        return map;
    }

    @Override
    protected void getFromJson(JSONObject object) {

    }

    public IntergratedComponentInOut getInput() {
        return this.input;
    }

    public IntergratedComponentInOut getOutput() {
        return this.output;
    }

    public void setInput(IntergratedComponentInOut input) {
        this.input = input;
    }

    public void setOutput(IntergratedComponentInOut output) {
        this.output = output;
    }

    @Override
    public LogicModel makeInternalClone(ConnectionMap connectionMap, long id) {
        return null;
    }

}
