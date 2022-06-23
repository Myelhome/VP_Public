package Util;

import Variable.ENUMS;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;


public final class OperationResult {
    ENUMS.Result result;
    Object body;

    public OperationResult(ENUMS.Result result, Object body) {
        this.result = result;
        this.body = body;
    }

    @JsonIgnore
    public ENUMS.Result getResult() {
        return result;
    }

    @JsonGetter("Result")
    public Object getBody() {
        return body;
    }
}
