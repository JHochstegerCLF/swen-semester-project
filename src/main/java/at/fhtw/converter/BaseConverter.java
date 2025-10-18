package at.fhtw.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseConverter<T> {
    protected ObjectMapper objectMapper = new ObjectMapper();
    protected Class<T> type;

    public BaseConverter(Class<T> type) {
        this.type = type;
    }

    public String serialize(T t) {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public T deserialize(String s) {
        try {
            return objectMapper.readValue(s, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void convert(T obj) {
        final String CYAN = "\u001B[36m";
        final String GREEN = "\u001B[32m";
        final String RESET = "\u001B[0m";

        String serialized  = serialize(obj);
        System.out.println(CYAN + this.getClass().getSimpleName() + RESET);
        System.out.println(GREEN + serialized + RESET);
        T deserialized = deserialize(serialized);
        System.out.println(deserialized);
    }
}
