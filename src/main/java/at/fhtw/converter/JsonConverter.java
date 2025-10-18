package at.fhtw.converter;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class JsonConverter<T> extends BaseConverter<T> {
    public JsonConverter(Class<T> type) {
        super(type);

        objectMapper = JsonMapper
                .builder()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .build();
    }
}
