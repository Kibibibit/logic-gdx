package au.com.thewindmills.logicgdx.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ChipInOut extends IoComponent {

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

        return node.set("in_to_out", mapper.valueToTree(inToOut));
    }


    
}
