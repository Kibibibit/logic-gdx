package au.com.thewindmills.logicgdx.models;

import java.util.HashMap;
import java.util.Map;

public class ChipInOut extends IoComponent {

    Map<Long, Long> inToOut;

    public ChipInOut() {
        super();
        inToOut = new HashMap<>();
    }

    protected void makeMapping(long in, long out) {
        inToOut.put(in, out);
    }

    @Override
    protected void doUpdate(long id, boolean state) {
        outputStates.put(inToOut.get(id), state);
    }


    
}
