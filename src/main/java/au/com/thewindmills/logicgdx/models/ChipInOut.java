package au.com.thewindmills.logicgdx.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import au.com.thewindmills.logicgdx.models.json.ComponentType;
import au.com.thewindmills.logicgdx.models.json.Instruction;
import au.com.thewindmills.logicgdx.models.json.InstructionSet;

public class ChipInOut extends IoComponent {

    public static final String FIELD_IN_TO_OUT = "in_to_out";

    private boolean isInput;

    Map<Long, Long> inToOut;

    public ChipInOut(String name, boolean isInput) {
        super(name);
        this.isInput = isInput;
        inToOut = new HashMap<>();
    }

    public boolean getIsInput() {
        return isInput;
    }

    protected void makeMapping(long in, long out) {
        inToOut.put(in, out);
    }

    @Override
    protected void doUpdate(long id, boolean state) {
        outputStates.put(inToOut.get(id), state);
    }

    

    @Override
    protected InstructionSet makeInstructionSet(InstructionSet set) {
        set.setType(ComponentType.CHIP_IN_OUT);
        return set;
    }

    @Override
    protected void readInstructionImpl(Instruction instruction) throws IOException {
        this.instructionError(instruction.getType());
        
    }


    
}
