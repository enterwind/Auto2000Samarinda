package vay.enterwind.auto2000samarinda.pubnub.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Created by novay on 05/03/18.
 */

public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T fromJson(byte[] value, Class<T> clazz) throws Exception {
        return mapper.readValue(value, clazz);
    }

    public static <T> T fromJson(String value, Class<T> clazz) throws Exception {
        return mapper.readValue(value, clazz);
    }

    public static String asJson(Object value) throws Exception {
        return mapper.writeValueAsString(value);
    }

    public static <T> T convert(Map<String, Object> value, Class<T> clazz) throws Exception {
        return mapper.convertValue(value, clazz);
    }

}
