package au.com.thewindmills.logicgdx.models;

import java.util.Map;

public class UpdateResponse {

    public Map<Long, Boolean> result;
    public boolean updated;

    public UpdateResponse(Map<Long, Boolean> result, boolean updated) {
        this.result = result;
        this.updated = updated;
    }
    
}
