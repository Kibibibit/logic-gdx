package au.com.thewindmills.logicgdx.models;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import au.com.thewindmills.logicgdx.models.json.Instruction;
import au.com.thewindmills.logicgdx.models.json.InstructionSet;
import au.com.thewindmills.logicgdx.utils.AppConstants;

public abstract class IoComponent {

    public static final String FIELD_ID = "id";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_IO_LABELS = "io_labels";
    public static final String FIELD_INPUTS = "inputs";
    public static final String FIELD_OUTPUTS = "outputs";

    protected long id;
    protected String name;
    private static long componentIdNext = 88000000;
    private static long componentIoIdNext = 11000000;

    protected Set<Long> inputs;
    protected Set<Long> outputs;

    protected Map<Long, String> ioLabels;

    protected Map<Long, Boolean> inputStates;
    protected Map<Long, Boolean> outputStates;

    public IoComponent(String name) {
        this.name = name;
        id = newComponentId();
        inputs = new HashSet<>();
        outputs = new HashSet<>();
        ioLabels = new HashMap<>();
        inputStates = new HashMap<>();
        outputStates = new HashMap<>();
    }

    protected abstract void doUpdate(long id, boolean state);

    protected abstract InstructionSet makeInstructionSet(InstructionSet set);

    public InstructionSet toInstructionSet() {
        InstructionSet set = new InstructionSet();

        set.setName(this.name);

        for (Long input : inputs) {
            set.addInstruction(Instruction.addInput(ioLabels.get(input)));
        }

        for (Long output : outputs) {
            set.addInstruction(Instruction.addOutput(ioLabels.get(output)));
        }
        
        set = makeInstructionSet(set);

        return set;
    }



    protected abstract ObjectNode toJsonObjectImpl(ObjectMapper mapper, ObjectNode node);

    public ObjectNode toJsonObject() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put(FIELD_ID, id);
        node.put(FIELD_TYPE, this.getClass().getSimpleName());
        node.put(FIELD_NAME, name);

        ObjectNode ioLabelNode = mapper.valueToTree(ioLabels);
        node.set(FIELD_IO_LABELS, ioLabelNode);
        node.set(FIELD_INPUTS, mapper.convertValue(inputs, ArrayNode.class));
        node.set(FIELD_OUTPUTS, mapper.convertValue(outputs, ArrayNode.class));

        node = toJsonObjectImpl(mapper, node);

        return node;
    }

    public static void saveJsonObject(IoComponent object) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(Paths.get(String.format(AppConstants.SAVE_PATH,object.getName())).toFile(), object.toInstructionSet().toJsonObject(mapper));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected abstract void fromJsonObjectImpl(ObjectNode object, Map<Long, Long> idMap) throws Exception;

    public static IoComponent fromJsonObject(ObjectNode object) throws Exception {
        Map<Long, Long> idMap = getJsonNumbers(object);
        return IoComponent.fromJsonObject(object, idMap);
    }

    public static IoComponent fromJsonObject(ObjectNode object, Map<Long, Long> idMap) throws Exception {

        if (!object.has(FIELD_TYPE)) {
            throw new IllegalArgumentException("Missing " + FIELD_TYPE + "!");
        }

        if (!object.has(FIELD_NAME)) {
            throw new IllegalArgumentException("Missing " + FIELD_NAME + "!");
        }

        IoComponent out;
        String name = object.get(FIELD_NAME).asText();

        switch (object.get(FIELD_TYPE).asText()) {

            case "TruthTable":
                out = new TruthTable(name);
                break;

            case "ChipComponent":
                out = new ChipComponent(name);
                break;

            case "ChipInOut":
                out = new ChipInOut(name);
                break;

            default:
                throw new Exception("Unrecognised object type!");
        }

        

        out.id = idMap.get(object.get(FIELD_ID).asLong());
        out.setName(name);


        Iterator<Entry<String, JsonNode>> ioLabelIterator = object.get(FIELD_IO_LABELS).fields();
        while (ioLabelIterator.hasNext()) {
            Entry<String, JsonNode> entry = ioLabelIterator.next();

            out.ioLabels.put(
                    idMap.get(Long.valueOf(entry.getKey())),
                    entry.getValue().asText());
        }

        ArrayNode inputArray = (ArrayNode) object.get(FIELD_INPUTS);
        for (JsonNode inputNode : inputArray) {
            long newId = idMap.get(Long.valueOf(inputNode.asText()));

            out.addInput(out.ioLabels.get(newId), newId);

            
        }

        ArrayNode outputArray = (ArrayNode) object.get(FIELD_OUTPUTS);
        for (JsonNode outputNode : outputArray) {
            long newId = idMap.get(Long.valueOf(outputNode.asText()));

            out.addOutput(out.ioLabels.get(newId), newId);

        }

        
        out.fromJsonObjectImpl(object, idMap);
        

        return out;

    }

    public static Map<Long, Long> getJsonNumbers(ObjectNode object) {

        ObjectMapper mapper = new ObjectMapper();

        String json;
        try {
            json = mapper.writeValueAsString(object);

            Map<Long, Long> idSet = new HashMap<>();
            for (String a : json.split("([^0-9.]+)")) {
                if (!a.isEmpty()) {
                    if (a.startsWith("11")) {
                        if (Long.valueOf(a) >= componentIoIdNext) {
                            componentIoIdNext = Long.valueOf(a) + 1;
                        }
                    } else {
                        if (Long.valueOf(a) >= componentIdNext) {
                            componentIdNext = Long.valueOf(a) + 1;
                        }
                    }

                    idSet.put(Long.valueOf(a), 0L);
                }
            }

            for (Long a : idSet.keySet()) {
                if (a.toString().startsWith("11")) {
                    idSet.put(a, newComponentIoId());
                } else {
                    idSet.put(a, newComponentId());
                }
            }

            return idSet;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.err.println("Try failed with no catch");
        return null;
    }

    public UpdateResponse update(long id, boolean state) {

        System.out.println("Updating " + id + "!");
        if (inputStates.get(id).equals(state)) {
            return new UpdateResponse(new HashMap<>(outputStates), false);
        }
        if (inputs.contains(id)) {
            inputStates.put(id, state);
        } else {
            throw new IllegalArgumentException();
        }

        doUpdate(id, state);

        for (Entry<Long, Boolean> entry : outputStates.entrySet()) {
            System.out.println("(" +
                    String.valueOf(entry.getKey() +
                            ") " +
                            ioLabels.get(entry.getKey()) +
                            " is " +
                            String.valueOf(entry.getValue())));
        }

        return new UpdateResponse(new HashMap<>(outputStates), true);

    }

    private long newIo(String label) {
        long ioId = newComponentIoId();
        ioLabels.put(ioId, label);
        return ioId;
    }

    private long newIo(String label, long ioId) {
        ioLabels.put(ioId, label);
        return ioId;
    }

    protected long addInputImpl(String label, long ioId) {
        inputs.add(ioId);
        inputStates.put(ioId, false);
        return ioId;
    }

    public long addInput(String label) {
        return addInputImpl(label, newIo(label));
    }

    public long addInput(String label, long ioId) {
        return addInputImpl(label, newIo(label, ioId));
    }

    protected long addOutputImpl(String label, long ioId) {
        outputs.add(ioId);
        outputStates.put(ioId, false);
        return ioId;
    }

    public long addOutput(String label) {
        return addOutputImpl(label, newIo(label));
    }

    public long addOutput(String label, long ioId) {
        return addOutputImpl(label, newIo(label, ioId));
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected static long newComponentId() {
        return componentIdNext++;
    }

    protected static long newComponentIoId() {
        return componentIoIdNext++;
    }

    public final long getIoId(String label) {
        if (!ioLabels.containsValue(label)) {
            System.err.println("Io labels doesn't have label: "+label);
            return -1;
        }

        for (Entry<Long, String> entry : ioLabels.entrySet()) {
            if (entry.getValue().equals(label)) {
                return entry.getKey();
            }
        }

        return -1;
    }

    public final String getIoLabel(long id) {
        return ioLabels.get(id);
    }

    public final boolean getInputState(long id) {
        return inputStates.get(id);
    }

    public final boolean getOutputState(long id) {
        return outputStates.get(id);
    }

    public final long getId() {
        return id;
    }

    public final Set<Long> getInputs() {
        return new HashSet<>(inputs);
    }

    public final Set<Long> getOutputs() {
        return new HashSet<>(outputs);
    }

    public final int getInputCount() {
        return inputs.size();
    }

    public final int getOutputCount() {
        return outputs.size();
    }

}
