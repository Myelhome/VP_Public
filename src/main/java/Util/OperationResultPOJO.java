package Util;

import Variable.ENUMS;
import com.fasterxml.jackson.annotation.JsonGetter;

public class OperationResultPOJO {
    private final ENUMS.Result result;

    public OperationResultPOJO(ENUMS.Result result){
        this.result = result;
    }

    @JsonGetter("Result")
    public ENUMS.Result getStartedDateString(){
        return result;
    }
}
