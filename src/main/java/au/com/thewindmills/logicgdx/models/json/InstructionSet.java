package au.com.thewindmills.logicgdx.models.json;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InstructionSet {
    
    private String name;
    private String uuid;
    private ComponentType type;

    private List<Instruction> instructions;

    public InstructionSet() {
        instructions = new ArrayList<>();
    }


    public void addInstruction(Instruction instruction) {
        instruction.setIndex(this.instructions.size());
        this.instructions.add(instruction);
    }

    public JsonNode toJsonObject(ObjectMapper mapper) throws JsonMappingException, JsonProcessingException {
        return mapper.readTree(mapper.writeValueAsString(this));
    }



    public List<Instruction> getInstructions() {
        return new ArrayList<>(instructions);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ComponentType getType() {
        return type;
    }

    public void setType(ComponentType type) {
        this.type = type;
    }

}
