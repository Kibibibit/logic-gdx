package au.com.thewindmills.logicgdx.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import au.com.thewindmills.logicgdx.models.json.ComponentType;
import au.com.thewindmills.logicgdx.models.json.Instruction;
import au.com.thewindmills.logicgdx.models.json.InstructionSet;

public class TruthTable extends IoComponent {

    public static final String FIELD_TABLE = "table";

    private Map<HashSet<Long>, HashMap<Long, Boolean>> table;

    public TruthTable(String name) {
        super(name);
        table = new HashMap<HashSet<Long>, HashMap<Long, Boolean>>();
    }

    @Override
    protected long addInputImpl(String label, long ioId) {
        super.addInputImpl(label, ioId);
        for (HashSet<Long> set : table.keySet()) {
            HashSet<Long> cloneSet = new HashSet<>(set);
            cloneSet.add(ioId);
            table.put(cloneSet, tableOutput());
        }

        table.put(new HashSet<Long>() {
            {
                add(ioId);
            }
        }, tableOutput());

        return ioId;
    }

    @Override
    protected long addOutputImpl(String label, long ioId) {
        super.addOutputImpl(label, ioId);
        for (HashSet<Long> set : table.keySet()) {
            table.put(set, tableOutput());
        }

        return ioId;
    }

    public void setRow(Set<String> inputs, Map<String, Boolean> outputs) {
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
                System.err.println("ioId " + ioId + " does not exist or is an input not output!");
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

    @Override
    protected InstructionSet makeInstructionSet(InstructionSet set) {
        set.setType(ComponentType.TRUTH);

        for (Entry<HashSet<Long>, HashMap<Long, Boolean>> entry : table.entrySet()) {

            Set<String> inSet = new HashSet<>();
            for (Long id : entry.getKey()) {
                inSet.add(ioLabels.get(id));
            }

            Map<String, Boolean> outMap = new HashMap<>();

            for (Entry<Long, Boolean> outEntry : entry.getValue().entrySet()) {
                outMap.put(ioLabels.get(outEntry.getKey()), outEntry.getValue());
            }

            set.addInstruction(Instruction.setRow(inSet, outMap));

        }

        return set;
    }

    @Override
    protected void readInstructionImpl(Instruction instruction) throws IOException {
        switch (instruction.getType()) {
            case SET_ROW:
                Set<String> inputs = new HashSet<>();
                Map<String, Boolean> outputs = new HashMap<>();

                String inString = instruction.getInstructionString().split(Instruction.IO_SEP)[0];
                String outString = instruction.getInstructionString().split(Instruction.IO_SEP)[1];
                

                for (String in : inString.split(Instruction.LABEL_SEP)) {
                    if (!in.isEmpty()) inputs.add(in);
                }

                for (String out : outString.split(Instruction.LABEL_SEP)) {
                    outputs.put(out.split(Instruction.VALUE_SEP)[0],
                            Boolean.valueOf(out.split(Instruction.VALUE_SEP)[1]));
                }


                this.setRow(inputs, outputs);

                break;
            default:
                this.instructionError(instruction.getType());
                break;

        }

    }

}
