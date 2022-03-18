package au.com.thewindmills.kibi.logicApp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import au.com.thewindmills.kibi.appEngine.utils.io.json.JSONUtils;
import au.com.thewindmills.kibi.logicApp.entities.io.LightComponent;
import au.com.thewindmills.kibi.logicApp.entities.io.SwitchComponent;
import au.com.thewindmills.kibi.logicApp.models.components.IntergratedComponent;
import au.com.thewindmills.kibi.logicApp.models.components.IntergratedComponentInOut;

/**
 * Maps the input and output nodes of {@link LogicModel}s together,
 * and handles passing updates between them
 */
public class ConnectionMap {

    public static final String FIELD_CONNECTIONS = "connections";
    private static final String FIELD_CHILDREN = "children";
    public static long nextId = 0;
    public final long id = nextId;

    /**
     * A map linking the id of each model to the model itself
     */
    private Map<Long, LogicModel> models;

    /**
     * Maps the input node of a model to the output node of another model.
     * This ensures one input can only be controlled by one output, but one output
     * can control many inputs
     */
    private Map<Connection, Connection> connections;

    public ConnectionMap() {
        nextId++;
        models = new HashMap<Long, LogicModel>();
        connections = new HashMap<Connection, Connection>();
    }

    /**
     * Creates a new connection between the given input and output
     * @param inputModel
     * @param inputNode
     * @param outputModel
     * @param outputNode
     */
    public void addConnection(LogicModel inputModel, int inputNode, LogicModel outputModel, int outputNode) {

        models.put(inputModel.id, inputModel);
        models.put(outputModel.id, outputModel);

        Connection inputConnection = new Connection(inputModel.id, inputNode);
        Connection outputConnection = new Connection(outputModel.id, outputNode);

        connections.put(inputConnection, outputConnection);
        outputModel.doUpdate();
        inputModel.doUpdate();
        update(outputModel, outputModel.outputBits);

    }

    /**
     * Severes a connection between two nodes
     * @param inputModel
     * @param inputNode
     * @param outputModel
     * @param outputNode
     */
    public void removeConnection(LogicModel inputModel, int inputNode, LogicModel outputModel, int outputNode) {
        connections.remove(new Connection(inputModel.id, inputNode), new Connection(outputModel.id, outputNode));
        inputModel.update(inputNode, false);

    }

    /**
     * Update all connected inputs to the given output model, based on the output model state
     * @param outputModel
     * @param state
     */
    public void update(LogicModel outputModel, boolean[] state) {

        for (Entry<Connection, Connection> entry : connections.entrySet()) {

            if (entry.getValue().modelId == outputModel.id) {
                System.out.println(outputModel.id + " is updating " + entry.getKey().modelId);
                models.get(entry.getKey().modelId).update(
                        entry.getKey().nodeId,
                        state[entry.getValue().nodeId]);
            }

        }

    }

    /**
     * Dispose the given logic model, removing all its connections
     * @param logicModel
     */
    public void dispose(LogicModel logicModel) {

        List<Connection> keysToRemove = new ArrayList<Connection>();

        for (Entry<Connection, Connection> entry : this.connections.entrySet()) {

            if (entry.getKey().modelId == logicModel.id || entry.getValue().modelId == logicModel.id) {
                keysToRemove.add(entry.getKey());
            }

        }

        for (Connection key : keysToRemove) {

            connections.remove(key);

            models.get(key.modelId).update(key.nodeId, false);

        }

        models.remove(logicModel.id);

    }

    public boolean inputConnected(LogicModel model, int nodeId) {
        return connections.containsKey(new Connection(model.id, nodeId));
    }


    public Map<Long, LogicModel> getModels() {
        return this.models;
    }

    public void createIc() {
        int inputCount = 0;
        int outputCount = 0;
        long internalId = 2L;

        List<String> inputNames = new ArrayList<>();
        List<String> outputNames = new ArrayList<>();

        Map<Long, Long> idMap = new HashMap<>();
        
        for (Entry<Long, LogicModel> entry : this.getModels().entrySet()) {
            idMap.put(entry.getKey(), internalId);
            internalId++;
            if (entry.getValue().getEntity() instanceof SwitchComponent) {
                inputNames.add(entry.getValue().getOutputNames()[0]);
                inputCount++;
                System.out.println(entry.getKey() + ", " + entry.getValue().getEntity());
                continue;
            }
            if (entry.getValue().getEntity() instanceof LightComponent) {
                outputNames.add(entry.getValue().getInputNames()[0]);
                outputCount++;
                System.out.println(entry.getKey() + ", " + entry.getValue().getEntity());
                continue;
            }
        }
        System.out.println("Ignore incoming BAD INPUT bits, they are not fatal unless otherwise specified");
        IntergratedComponent ic = new IntergratedComponent("IC", inputCount, outputCount, this);

        idMap.put(ic.getInput().id, 0L);
        idMap.put(ic.getOutput().id, 1L);
        
        ic.setInputNames(inputNames.toArray(new String[0]));
        ic.setOutputNames(outputNames.toArray(new String[0]));

        ic.createFromConnectionMap(this, idMap);

        JSONUtils.writeToFile(ic.toJson());
    }

    private Map<Connection, Connection> getConnections() {
        return this.connections;
    }

    public void copyConnectionsToIc(ConnectionMap other, IntergratedComponent ic, Map<Long, Long> idMap) {
        
        int inputIndex = 0;
        int outputIndex = 0;
        this.models.put(0L, ic.getInput().internalClone(this, 0L));
        ic.setInput((IntergratedComponentInOut) this.models.get(0L));

        this.models.put(1L, ic.getOutput().internalClone(this, 1L));
        ic.setOutput((IntergratedComponentInOut) this.models.get(1L));

        List<Long> idsToRemove = new ArrayList<>();
        

        for (Entry<Connection, Connection> connection : other.getConnections().entrySet()) {
            
            Connection inputConnection = connection.getKey();
            inputConnection.modelId = idMap.get(inputConnection.modelId);

            Connection outputConnection = connection.getValue();
            outputConnection.modelId = idMap.get(outputConnection.modelId);

            LogicModel inputModel = models.get(inputConnection.modelId);
            LogicModel outputModel = models.get(outputConnection.modelId);
            
            if (inputModel.getEntity() instanceof LightComponent) {
                idsToRemove.add(inputConnection.modelId);
                inputConnection.modelId = 0L;
                inputModel = this.models.get(0L);
                inputConnection.nodeId = inputIndex;
                // This currently isnt right, we need some kind of node map
                inputIndex++;

            }
            if (outputModel.getEntity() instanceof SwitchComponent) {

                idsToRemove.add(outputConnection.modelId);
                outputConnection.modelId = 1L;
                outputModel = this.models.get(1L);
                outputConnection.nodeId = outputIndex;
                outputIndex++;

            }
            
            connections.put(inputConnection, outputConnection);

        }

        for (Long id : idsToRemove) {
            models.remove(id);
        }

    }

    public void clear() {
        this.models.clear();
        this.connections.clear();
    }

    public JSONObject toJson() {
        Map<String, Object> output = new HashMap<>();

        Map<String, String> connectionsObjectMap = new HashMap<>();
        for (Entry<Connection, Connection> connection : this.connections.entrySet()) {
            connectionsObjectMap.put(connection.getKey().toString(), connection.getValue().toString());
        }

        Map<String, JSONObject> modelObjectmap = new HashMap<>();
        for (Entry<Long, LogicModel> entry : this.models.entrySet()) {
            modelObjectmap.put(entry.getKey().toString(), entry.getValue().toJson());
        }
        
        output.put(FIELD_CONNECTIONS, connectionsObjectMap);
        output.put(FIELD_CHILDREN, modelObjectmap);

        return new JSONObject(output);


    }

    /**
     * Helper class to store in the hashmap
     */
    private static class Connection {

        protected long modelId;
        protected int nodeId;

        protected Connection(long modelId, int nodeId) {
            this.modelId = modelId;
            this.nodeId = nodeId;
        }

        @Override
        public String toString() {
            return modelId+":"+nodeId;
        }

        public static Connection fromString(String string) {
            String[] arr = string.split(":");
            return new Connection(Long.parseLong(arr[0]), Integer.parseInt(arr[1]));
        }

        @Override
        public int hashCode() {
            return ((int) modelId << 4) + nodeId;
        }

        @Override
        public boolean equals(Object other) {
            if (other.getClass().equals(this.getClass())) {
                return this.hashCode() == other.hashCode();
            }
            return false;
        }

    }

}
