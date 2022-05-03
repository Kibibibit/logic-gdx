package au.com.thewindmills.logicgdx.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ConnectionMatrix {

    private Map<Long, IoComponent> children;

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
    }

    public void addChild(IoComponent child) {
        addChild(child, false);
    }

    public void addChild(IoComponent child, boolean reverse) {

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

    public void setMatrix(long input, long output, boolean value) {
        matrix.put(mapping(input,output), value);
    }

    private String mapping(long input, long output) {
        return String.format("%d:%d", input,output);
    }

    public Map<Long, Boolean> update(long childId, long output, boolean state) {

        Map<Long, Boolean> out = new HashMap<>();

        for (long input: inputs) {
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

    public ObjectNode toJsonObject(ObjectMapper mapper) {
        ObjectNode out = mapper.createObjectNode();

        ObjectNode childrenNode = mapper.createObjectNode();

        for (IoComponent child : children.values()) {
            childrenNode.set(String.valueOf(child.id), child.toJsonObject());
        }

        out.set("children",  childrenNode);
        out.set("matrix", mapper.valueToTree(matrix));

        return out;
    }

    
}
