package au.com.thewindmills.logicgdx.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
        inChip = new ChipInOut(name + " in");
        outChip = new ChipInOut(name + " out");
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

    protected void setInChip(ChipInOut inChip) {
        this.inChip = inChip;
    }

    protected void setOutChip(ChipInOut outChip) {
        this.outChip = outChip;
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
    protected ObjectNode toJsonObjectImpl(ObjectMapper mapper, ObjectNode node) {

        node.set(FIELD_MATRIX, matrix.toJsonObject(mapper));
        node.set(FIELD_IN_MAP, mapper.valueToTree(inMap));
        node.set(FIELD_OUT_MAP, mapper.valueToTree(outMap));

        return node;
    }


    @Override
    protected void fromJsonObjectImpl(ObjectNode object, Map<Long, Long> idMap) throws Exception {
        
        if (!object.has(FIELD_MATRIX)) {
            throw new IllegalArgumentException("Missing " + FIELD_MATRIX);
        }

        matrix.fromJsonObject(this, (ObjectNode) object.get(FIELD_MATRIX), idMap);

        if (!object.has(FIELD_IN_MAP)) {
            throw new IllegalArgumentException("Missing " + FIELD_IN_MAP);
        }

        Iterator<Entry<String, JsonNode>> newInMapIterator = object.get(FIELD_IN_MAP).fields();
        
        Map<Long, Long> newInMap = new HashMap<>();
        while (newInMapIterator.hasNext()) {

            Entry<String, JsonNode> entry = newInMapIterator.next();

            newInMap.put(
                idMap.get(Long.valueOf(entry.getKey())),
                idMap.get(Long.valueOf(entry.getValue().asText()))
            );

        }

        inMap = newInMap;

        if (!object.has(FIELD_OUT_MAP)) {
            throw new IllegalArgumentException("Missing " + FIELD_OUT_MAP);
        }

        Iterator<Entry<String, JsonNode>> newOutMapIterator = object.get(FIELD_OUT_MAP).fields();
        
        Map<Long, Long> newOutMap = new HashMap<>();
        while (newOutMapIterator.hasNext()) {

            Entry<String, JsonNode> entry = newOutMapIterator.next();

            newOutMap.put(
                idMap.get(Long.valueOf(entry.getKey())),
                idMap.get(Long.valueOf(entry.getValue().asText()))
            );

        }

        outMap = newOutMap;
    }
    
}
