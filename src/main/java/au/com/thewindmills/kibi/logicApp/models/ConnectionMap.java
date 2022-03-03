package au.com.thewindmills.kibi.logicApp.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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

        connections.put(inputConnection, outputConnection);
        outputModel.result();
        inputModel.result();
        



    }

    public void removeConnection(LogicModel inputModel, int inputNode, LogicModel outputModel, int outputNode) {
        connections.remove(new Connection(inputModel.id, inputNode), new Connection(outputModel.id, outputNode));
        outputModel.result();
        inputModel.result();
        
    }


    public void update(LogicModel outputModel, boolean[] state) {

        for (Entry<Connection, Connection> entry : connections.entrySet()) {
            
            if (entry.getValue().modelId == outputModel.id) {
                System.out.println(outputModel.id + " is updating " + entry.getKey().modelId);
                models.get(entry.getKey().modelId).update(
                    entry.getKey().nodeId,
                    state[entry.getKey().nodeId]
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
