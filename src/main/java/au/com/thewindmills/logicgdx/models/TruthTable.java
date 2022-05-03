package au.com.thewindmills.logicgdx.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

public class TruthTable extends IoComponent {


    private Map<HashSet<Long>, HashMap<Long, Boolean>> table;

    public TruthTable() {
        super();
        table = new HashMap<HashSet<Long>, HashMap<Long, Boolean>>();
    }

    @Override
    public long addInput(String label) {
        long ioId = super.addInput(label);

        for (HashSet<Long> set : table.keySet()) {
            HashSet<Long> cloneSet = new HashSet<>(set);
            cloneSet.add(ioId);
            table.put(cloneSet, tableOutput());
        }

        table.put(new HashSet<Long>(){{add(ioId);}}, tableOutput());

        return ioId;
    }

    @Override 
    public long addOutput(String label) {
        long ioId = super.addOutput(label);

        for (HashSet<Long> set : table.keySet()) {
            table.put(set, tableOutput());
        }

        return ioId;
    }



    public void setRow(HashSet<String> inputs, HashMap<String, Boolean> outputs) {
        HashSet<Long> idSet = new HashSet<>();

        for (String label : inputs) {
            long ioId = getIoId(label);
            if (ioId != -1 && this.inputs.contains(ioId)) {
                idSet.add(ioId);
            } else {
                System.err.println("ioId does not exist or is an output not input!");
                throw new IllegalArgumentException();
            }
        }

        HashMap<Long, Boolean> idMap = new HashMap<>();

        for (String label : outputs.keySet()) {
            long ioId = getIoId(label);
            if (ioId != -1 && this.outputs.contains(ioId)) {
                idMap.put(ioId, outputs.get(label));
            } else {
                System.err.println("ioId does not exist or is an input not output!");
                throw new IllegalArgumentException();
            }
        }

        table.put(idSet, idMap);
    }


    private HashMap<Long, Boolean> tableOutput() {
        HashMap<Long, Boolean> out = new HashMap<>();
        for (long io : outputs) {
            out.put(io, false);
        }
        return out;
    }




    @Override
    protected void doUpdate(long id, boolean state) {
        HashSet<Long> idSet = new HashSet<>();

        for (Entry<Long, Boolean> entry : inputStates.entrySet()) {
            if (entry.getValue()) {
                idSet.add(entry.getKey());
            }
        }
        outputStates = table.get(idSet);

        if (outputStates == null) {
            throw new NullPointerException("Output states ended up null after update!");
        }
    }
    
    


}
