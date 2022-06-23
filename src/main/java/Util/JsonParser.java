package Util;

import DAO.UserBin;
import Variable.ENUMS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;

import java.util.Set;

public class JsonParser {

    private static final ObjectMapper objectMapper = getDefaultObjectMapper();

    private static ObjectMapper getDefaultObjectMapper(){
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        //defaultObjectMapper.registerModule(new JavaTimeModule());
        defaultObjectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return defaultObjectMapper;
    }

    public static String getJSONString(OperationResult result) {

        String jsonResult = "{\"Result\":\"JSON_ERROR\"}";

        try {
            ObjectWriter objectWriter = objectMapper.writer();

            if (result.getResult() == ENUMS.Result.SUCCESS && result.getBody() != null) jsonResult = objectWriter.writeValueAsString(result);
            else jsonResult = objectWriter.writeValueAsString(new OperationResultPOJO(result.getResult()));
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return jsonResult;
    }

    public static JSONObject parseJSON(OperationResult result) {

        return new JSONObject(getJSONString(result));
    }
}
