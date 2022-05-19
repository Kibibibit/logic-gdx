package au.com.thewindmills.logicgdx.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import au.com.thewindmills.logicgdx.models.json.ComponentType;
import au.com.thewindmills.logicgdx.models.json.Instruction;
import au.com.thewindmills.logicgdx.models.json.InstructionSet;

public class ChipComponent extends IoComponent {

    public static final String FIELD_MATRIX = "matrix";
    public static final String FIELD_IN_MAP = "in_map";
    public static final String FIELD_OUT_MAP = "out_map";

    private ConnectionMatrix matrix;
    private ChipInOut inChip;
    private ChipInOut outChip;

    private Map<Long, Long> inMap;
    private Map<Long, Long> outMap;

    public ChipComponent(String name) {
        super(name);
        matrix = new ConnectionMatrix();
        inChip = new ChipInOut(name + " in", true);
        outChip = new ChipInOut(name + " out", false);
        inMap = new HashMap<>();
        outMap = new HashMap<>();
    }


    @Override
    protected long addInputImpl(String label, long ioId) {
        super.addInputImpl(label, ioId);
        long inId = inChip.addInput(label + " in");
        long outId = inChip.addOutput(label + " out");
        inChip.makeMapping(inId, outId);
        inMap.put(ioId, inId);
        inMap.put(inId, ioId);
        outMap.put(ioId, outId);
        outMap.put(outId, ioId);

        matrix.addChild(inChip);

        return ioId;
    }

    @Override 
    protected long addOutputImpl(String label, long ioId) {
        super.addOutputImpl(label, ioId);
        long inId = outChip.addInput(label + " in");
        long outId = outChip.addOutput(label + " out");
        outChip.makeMapping(inId, outId);
        inMap.put(ioId, inId);
        inMap.put(inId, ioId);
        outMap.put(ioId, outId);
        outMap.put(outId, ioId);
        
        matrix.addChild(outChip);
        return ioId;
    }

    public void addChild(IoComponent child) {
        matrix.addChild(child);
    }

    public void setInternalMapping(long input, long output, boolean value) {
        matrix.setMatrix(input, output, value);
    }

    public void setExternalMappingIn(long internalInput, long externalInput, boolean value) {
        matrix.setMatrix(internalInput,outMap.get(externalInput), value);
    }

    public void setExternalMappingOut(long internalOutput, long externalOutput, boolean value) {
        matrix.setMatrix(inMap.get(externalOutput), internalOutput, value);
    }

    public void setExternalMapping(long externalInput, long externalOutput, boolean value) {
        matrix.setMatrix(inMap.get(externalOutput), outMap.get(externalInput), value);
    }


    @Override
    protected void doUpdate(long id, boolean state) {
        UpdateResponse inUpdate = inChip.update(inMap.get(id), state);
        if (inUpdate.updated) {
            for (Entry<Long, Boolean> entry : inUpdate.result.entrySet()) {
                matrix.update(inChip.getId(), entry.getKey(), entry.getValue());
            }
        }
        
        for (Entry<Long, Boolean> entry : outChip.outputStates.entrySet()) {
            long ioId = outMap.get(entry.getKey());
            outputStates.put(ioId, entry.getValue());
        }
    }

  


    @Override
    protected InstructionSet makeInstructionSet(InstructionSet set) {
        set.setType(ComponentType.CHIP);
        set = matrix.makeInstructionSet(set);
        return set;
    }


    @Override
    protected void readInstructionImpl(Instruction instruction) throws IOException {
        matrix.readInstruction(instruction);
        
    }
    
}
