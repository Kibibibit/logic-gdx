package au.com.thewindmills.kibi.logicApp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        update(outputModel, outputModel.outputBits);
        



    }

    public void removeConnection(LogicModel inputModel, int inputNode, LogicModel outputModel, int outputNode) {
        connections.remove(new Connection(inputModel.id, inputNode), new Connection(outputModel.id, outputNode));
        inputModel.update(inputNode, false);
        
        
        
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
