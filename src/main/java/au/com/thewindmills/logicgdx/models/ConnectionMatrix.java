package au.com.thewindmills.logicgdx.models;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import au.com.thewindmills.logicgdx.models.json.Instruction;
import au.com.thewindmills.logicgdx.models.json.InstructionSet;
import au.com.thewindmills.logicgdx.utils.AppConstants;

public class ConnectionMatrix {

    public static final String FIELD_MATRIX = "matrix";
    public static final String FIELD_CHILDREN = "children";

    private Map<Long, IoComponent> children;

    private Map<Integer, Long> indexToChild;

    private Map<Long, Long> ioToChild;

    private Set<Long> inputs;
    private Set<Long> outputs;

    private Map<String, Boolean> matrix;

    public ConnectionMatrix() {
        children = new HashMap<>();
        inputs = new HashSet<>();
        outputs = new HashSet<>();
        matrix = new HashMap<>();
        ioToChild = new HashMap<>();
        indexToChild = new HashMap<>();
    }

    public void addChild(IoComponent child) {

        children.put(child.getId(), child);

        for (long input : child.getInputs()) {
            inputs.add(input);
            for (long output : outputs) {
                matrix.put(mapping(input, output), false);
            }
            ioToChild.put(input, child.getId());
        }
        for (long output : child.getOutputs()) {
            outputs.add(output);
            for (long input : inputs) {
                matrix.put(mapping(input, output), false);
            }
            ioToChild.put(output, child.getId());
        }
    }

    public String setMatrix(long input, long output, boolean value, boolean update) {
        String mapping = mapping(input, output);
        matrix.put(mapping, value);
        if (update) {
            UpdateResponse updateRes;
            if (value) {
                updateRes = children.get(ioToChild.get(input)).update(input,
                    children.get(ioToChild.get(output)).getOutputState(output));
            } else {
                updateRes = children.get(ioToChild.get(input)).update(input,false);
            }
            if (updateRes.updated) {
                for (Entry<Long, Boolean> entry : updateRes.result.entrySet()) {
                    this.update(ioToChild.get(input), entry.getKey(), entry.getValue());
                }
            }
        }
        return mapping;
    }

    public void setMatrix(long input, long output, boolean value) {
        setMatrix(input, output, value, false);

    }

    private String mapping(long input, long output) {
        return String.format("%d:%d", input, output);
    }

    public Map<Long, Boolean> update(long childId, long output, boolean state) {

        Map<Long, Boolean> out = new HashMap<>();

        for (long input : inputs) {
            if (matrix.get(mapping(input, output))) {
                UpdateResponse response = children.get(ioToChild.get(input)).update(input, state);
                if (response.updated) {

                    for (long id : response.result.keySet()) {

                        update(ioToChild.get(id), id, response.result.get(id));
                    }

                }

            }
        }

        return out;
    }

    public void update(long output, boolean state) {
        update(ioToChild.get(output), output, state);
    }

    public InstructionSet makeInstructionSet(InstructionSet set) {
        int i = 2;

        Map<Long, Integer> indexMap = new HashMap<>();

        for (IoComponent child : children.values()) {
            if (!(child instanceof ChipInOut)) {
                indexMap.put(child.id, i);
                if (!Paths.get(String.format(AppConstants.SAVE_PATH, child.name)).toFile().exists()) {
                    try {
                        child.saveJsonObject();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                set.addInstruction(
                        Instruction.addChild(i++, child.name));
            } else {
                if (((ChipInOut) child).getIsInput()) {
                    indexMap.put(child.id, 0);
                } else {
                    indexMap.put(child.id, 1);
                }
            }
        }

        for (Entry<String, Boolean> entry : matrix.entrySet()) {

            if (entry.getValue()) {

                Long inIo = Long.valueOf(entry.getKey().split(":")[0]);
                Long outIo = Long.valueOf(entry.getKey().split(":")[1]);

                IoComponent input = children.get(ioToChild.get(inIo));
                IoComponent output = children.get(ioToChild.get(outIo));

                set.addInstruction(Instruction.mapping(
                        indexMap.get(input.id), input.getIoLabel(inIo),
                        indexMap.get(output.id), output.getIoLabel(outIo)));

            }

        }

        return set;
    }

    public void readInstruction(Instruction instruction) throws IOException {

        if (!indexToChild.containsKey(0) || !indexToChild.containsKey(1)) {
            for (IoComponent child : children.values()) {
                if (child instanceof ChipInOut) {
                    if (((ChipInOut) child).getIsInput()) {
                        indexToChild.put(0, child.id);
                    } else {
                        indexToChild.put(1, child.id);
                    }
                }
            }
        }

        switch (instruction.getType()) {
            case ADD_CHILD:

                String name = instruction.getInstructionString().split(Instruction.VALUE_SEP)[1];
                int index = Integer.valueOf(instruction.getInstructionString().split(Instruction.VALUE_SEP)[0]);
                IoComponent child = IoComponent.fromJson(name);

                this.addChild(child);
                this.indexToChild.put(index, child.id);

                break;
            case MAPPING:

                String inString = instruction.getInstructionString().split(Instruction.IO_SEP)[0];
                String outString = instruction.getInstructionString().split(Instruction.IO_SEP)[1];

                IoComponent input = children.get(
                        indexToChild.get(
                                Integer.valueOf(inString.split(Instruction.VALUE_SEP)[0])));

                String inLabel = inString.split(Instruction.VALUE_SEP)[1];

                IoComponent output = children.get(
                        indexToChild.get(
                                Integer.valueOf(outString.split(Instruction.VALUE_SEP)[0])));

                String outLabel = outString.split(Instruction.VALUE_SEP)[1];

                this.setMatrix(input.getIoId(inLabel), output.getIoId(outLabel), true);

                break;
            default:
                throw new IOException(
                        "Unrecognised instruction: " + instruction.getType().name() + " for ConnectionMatrix");

        }

    }

}
