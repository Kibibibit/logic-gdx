package au.com.thewindmills.logicgdx.models.json;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Instruction {
    

    private static final String ADD_STRING = "%s";

    public static final String LABEL_SEP = ";";
    public static final String VALUE_SEP = "=";
    public static final String IO_SEP = ",";

    private int index;
    private InstructionType type;
    private String instructionString;

    public Instruction() {
        type = InstructionType.INVALID;
    }

    private Instruction(InstructionType type) {
        this.type = type;
    }

    public static Instruction addInput(String label) {
        return new Instruction(InstructionType.ADD_INPUT).setInstructionString(String.format(ADD_STRING, label));
    }

    public static Instruction addOutput(String label) {
        return new Instruction(InstructionType.ADD_OUTPUT).setInstructionString(String.format(ADD_STRING, label));
    }

    public static Instruction setRow(Set<String> inputs, Map<String, Boolean> outputs) {
        String instructionString = "%s%s%s";

        String inputString = "";
        String outputString = "";

        for (String i : inputs) {
            inputString = String.format("%s%s%s", inputString, LABEL_SEP, i);
        }

        for (Entry<String, Boolean> entry : outputs.entrySet()) {
            outputString = String.format("%s%s%s%s%s", outputString, LABEL_SEP, entry.getKey(), VALUE_SEP, entry.getValue().toString());
        }

        inputString = inputString.replaceFirst(LABEL_SEP, "");
        outputString = outputString.replaceFirst(LABEL_SEP, "");

        return new Instruction(InstructionType.SET_ROW).setInstructionString(String.format(instructionString, inputString, IO_SEP, outputString));
    }

    public static Instruction addChild(int index, String name) {
        return new Instruction(InstructionType.ADD_CHILD).setInstructionString(
            String.format("%d%s%s", index, VALUE_SEP,name)
            );
    }

    public static Instruction mapping(int inIndex, String inLabel, int outIndex, String outLabel) {
        String instructionString = String.format("%s%s%s",
         String.format("%d%s%s", inIndex, VALUE_SEP, inLabel),
         IO_SEP,
         String.format("%d%s%s", outIndex, VALUE_SEP, outLabel)
        );

        return new Instruction(InstructionType.MAPPING).setInstructionString(instructionString);
    }


    private Instruction setInstructionString(String string) {
        this.instructionString = string;
        return this;
    }

    public String getInstructionString() {
        return instructionString;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setType(InstructionType type) {
        this.type = type;
    }

    public InstructionType getType() {
        return type;
    }

}
