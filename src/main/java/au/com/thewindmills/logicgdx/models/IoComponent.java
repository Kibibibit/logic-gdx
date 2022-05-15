package au.com.thewindmills.logicgdx.models;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.thewindmills.logicgdx.models.json.Instruction;
import au.com.thewindmills.logicgdx.models.json.InstructionSet;
import au.com.thewindmills.logicgdx.models.json.InstructionType;
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

    public void saveJsonObject()
            throws JsonMappingException, StreamWriteException, DatabindException, JsonProcessingException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(Paths.get(String.format(AppConstants.SAVE_PATH, this.getName())).toFile(),
                this.toInstructionSet().toJsonObject(mapper));

    }

    protected abstract void readInstructionImpl(Instruction instruction) throws IOException;

    private void readInstruction(Instruction instruction) throws IOException {
        switch(instruction.getType()) {
            case ADD_INPUT:
                this.addInput(instruction.getInstructionString());
                break;
            case ADD_OUTPUT:
                this.addOutput(instruction.getInstructionString());
                break;
            default:
                this.readInstructionImpl(instruction);
                break;
        }
    }


    public static IoComponent fromJson(String name) throws StreamReadException, DatabindException, IOException {

        ObjectMapper mapper = new ObjectMapper();

        InstructionSet set = mapper.readValue(Paths.get(String.format(AppConstants.SAVE_PATH, name)).toFile(), InstructionSet.class);
        IoComponent component;

        switch (set.getType()) {
            case CHIP:
                component = new ChipComponent(name);
                break;
            case CHIP_IN_OUT:
                throw new IOException("Don't generate ChipInOuts from instructions!!!");
            case TRUTH:
                component = new TruthTable(name);
                break;
            default:
                throw new IOException("Got unrecognised component type " + set.getType().toString());
            
        }

        List<Instruction> instructionsSorted = set.getInstructions();
        instructionsSorted.sort(new Comparator<Instruction>() {
            public int compare(Instruction a, Instruction b) {
                return a.getIndex() - b.getIndex();
            }
        });

        for (Instruction instruction : instructionsSorted) {
            component.readInstruction(instruction);
        }
        
        return component;

    }

    protected final void instructionError(InstructionType type) throws IOException {
        throw new IOException("Got bad instruction type: "+type.name()+" for class: "+this.getClass().getSimpleName());
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
            System.err.println("Io labels doesn't have label: " + label);
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
