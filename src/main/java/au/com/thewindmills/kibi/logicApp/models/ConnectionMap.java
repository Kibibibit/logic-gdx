package au.com.thewindmills.kibi.logicApp.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import au.com.thewindmills.kibi.logicApp.utils.Binary;

public class ConnectionMap {

    private Map<Long, LogicModel> models;

    private Map<Connection, Connection> connections;

    public ConnectionMap() {
        models = new HashMap<Long, LogicModel>();
        connections = new HashMap<Connection, Connection>();
    }

    public void addConnection(LogicModel inputModel, int inputNode, LogicModel outputModel, int outputNode) {

        models.put(inputModel.id, inputModel);
        models.put(outputModel.id, outputModel);

        Connection inputConnection = new Connection(inputModel.id, inputNode);
        Connection outputConnection = new Connection(outputModel.id, outputNode);

        //DO something here with the set, go output to set of inputs
        connections.put(inputConnection, outputConnection);

    }

    public void removeConnection(LogicModel inputModel, int inputNode, LogicModel outputModel, int outputNode) {
        connections.remove(new Connection(inputModel.id, inputNode), new Connection(outputModel.id, outputNode));
    }


    public int getInputState(LogicModel model) {

        if (!models.containsKey(model.id)) {
            return -1;
        }

        int result = 0;

        for (Entry<Connection, Connection> entry : connections.entrySet()) {
            if (entry.getKey().modelId == model.id) {
                result += models.get(entry.getValue().modelId).outputAtBit(entry.getKey().nodeId);
            }
        }

        return result;
    }

    public void update(LogicModel outputModel, int outputNode, boolean state) {

        for (Entry<Connection, Connection> entry : connections.entrySet()) {
            
            if (entry.getValue().modelId == outputModel.id && entry.getValue().nodeId == outputNode) {
                models.get(entry.getKey().modelId).update(
                    entry.getKey().nodeId,
                    state
                );
            }

        }
 
    }


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
