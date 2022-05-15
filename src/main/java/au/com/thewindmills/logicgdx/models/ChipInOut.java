package au.com.thewindmills.logicgdx.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import au.com.thewindmills.logicgdx.models.json.ComponentType;
import au.com.thewindmills.logicgdx.models.json.InstructionSet;

public class ChipInOut extends IoComponent {

    public static final String FIELD_IN_TO_OUT = "in_to_out";

    Map<Long, Long> inToOut;

    public ChipInOut(String name) {
        super(name);
        inToOut = new HashMap<>();
    }

    protected void makeMapping(long in, long out) {
        inToOut.put(in, out);
    }

    @Override
    protected void doUpdate(long id, boolean state) {
        outputStates.put(inToOut.get(id), state);
    }

    @Override
    protected ObjectNode toJsonObjectImpl(ObjectMapper mapper, ObjectNode node) {

        return node.set(FIELD_IN_TO_OUT, mapper.valueToTree(inToOut));
    }

    @Override
    protected void fromJsonObjectImpl(ObjectNode object, Map<Long,Long> idMap) {
       
        if (!object.has(FIELD_IN_TO_OUT)) {
            throw new IllegalArgumentException("Missing field " + FIELD_IN_TO_OUT);
        }

        Iterator<Entry<String, JsonNode>> iterator = object.get(FIELD_IN_TO_OUT).fields();
        
        Map<Long, Long> newInToOut = new HashMap<>();
        while (iterator.hasNext()) {

            Entry<String, JsonNode> entry = iterator.next();

            newInToOut.put(
                idMap.get(Long.valueOf(entry.getKey())),
                idMap.get(Long.valueOf(entry.getValue().asText()))
            );

        }

        inToOut = newInToOut;
        
    }

    @Override
    protected InstructionSet makeInstructionSet(InstructionSet set) {
        set.setType(ComponentType.CHIP_IN_OUT);
        return set;
    }


    
}
