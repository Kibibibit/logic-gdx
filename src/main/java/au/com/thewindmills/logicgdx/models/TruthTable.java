package au.com.thewindmills.logicgdx.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
                System.err.println("ioId "+ioId+" does not exist or is an input not output!");
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
    protected ObjectNode toJsonObjectImpl(ObjectMapper mapper, ObjectNode node) {

        ObjectNode tableNode = mapper.createObjectNode();

        for (Entry<HashSet<Long>, HashMap<Long, Boolean>> entry : table.entrySet()) {

            String hashString = "";

            for (long id : entry.getKey()) {
                if (hashString.trim().length() == 0) {
                    hashString = String.valueOf(id);
                } else {
                    hashString = String.format("%s;%d", hashString, id);
                }

            }

            tableNode.set(hashString, mapper.valueToTree(entry.getValue()));

        }

        return node.set(FIELD_TABLE, tableNode);
    }

    @Override
    protected void fromJsonObjectImpl(ObjectNode object, Map<Long, Long> idMap) {

        if (!object.has(FIELD_TABLE)) {
            throw new IllegalArgumentException("Missing field " + FIELD_TABLE);
        }

        if (object.get(FIELD_TABLE) instanceof ObjectNode) {

            ObjectNode tableObject = (ObjectNode) object.get(FIELD_TABLE);

            Iterator<Entry<String, JsonNode>> tableIterator = tableObject.fields();

            while (tableIterator.hasNext()) {
                Entry<String, JsonNode> entry = tableIterator.next();

                HashSet<String> set = new HashSet<>();

                for (String value : entry.getKey().split(";")) {
                    if (!value.isEmpty()) {
                        set.add(ioLabels.get(idMap.get(Long.valueOf(value))));
                    }

                }

                HashMap<String, Boolean> map = new HashMap<>();

                Iterator<Entry<String, JsonNode>> iterator = entry.getValue().fields();

                while (iterator.hasNext()) {
                    Entry<String, JsonNode> entry2 = iterator.next();

                    map.put(
                            ioLabels.get(
                                    idMap.get(
                                            Long.valueOf(entry2.getKey()))),
                            entry2.getValue().asBoolean());

                }

                setRow(set, map);

            }

        } else {
            throw new IllegalArgumentException(FIELD_TABLE + " is in wrong format!");
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

}
