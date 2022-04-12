package au.com.thewindmills.logicgdx.models;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class IoComponent {

    protected final long id;
    protected String name;
    private static long componentIdNext = 0;
    private static long componentIoIdNext = 0;

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

    protected abstract ObjectNode toJsonObjectImpl(ObjectMapper mapper, ObjectNode node);

    public ObjectNode toJsonObject() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("id", id);
        node.put("type", this.getClass().getSimpleName());
        node.put("name", name);

        ObjectNode ioLabelNode = mapper.valueToTree(ioLabels);
        node.set("io_labels", ioLabelNode);
        node.set("inputs", mapper.convertValue(inputs, ArrayNode.class));
        node.set("outputs", mapper.convertValue(outputs, ArrayNode.class));

        node = toJsonObjectImpl(mapper, node);

        return node;
    }

    public static void saveJsonObject(IoComponent object) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(Paths.get(object.getName()+".json").toFile(), object.toJsonObject());
        } catch (IOException e) {
            e.printStackTrace();
        }




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

    public long addInput(String label) {
        long ioId = newIo(label);
        System.out.println("Adding input of id " + ioId + " (" + label + ") to " + id);
        inputs.add(ioId);
        inputStates.put(ioId, false);
        return ioId;
    }

    public long addOutput(String label) {
        long ioId = newIo(label);
        System.out.println("Adding output of id " + ioId + " (" + label + ") to " + id);
        outputs.add(ioId);
        outputStates.put(ioId, false);
        return ioId;
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
