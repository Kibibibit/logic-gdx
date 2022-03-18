package au.com.thewindmills.kibi.logicApp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Maps the input and output nodes of {@link LogicModel}s together,
 * and handles passing updates between them
 */
public class ConnectionMap {

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

    /**
     * Helper class to store in the hashmap
     */
    private class Connection {

        protected long modelId;
        protected int nodeId;

        protected Connection(long modelId, int nodeId) {
            this.modelId = modelId;
            this.nodeId = nodeId;
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
