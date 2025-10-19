package at.fhtw.presentation.models;

import at.fhtw.presentation.http.HttpMethod;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import lombok.Data;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Data
public class Context implements AutoCloseable {
    @NonNull
    private HttpExchange httpExchange;
    private Map<String, String> params;
    private Headers headers;
    private String body;
    private HttpMethod method;
    private String path;

    public Context(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        this.method = HttpMethod.valueOf(httpExchange.getRequestMethod());
        this.path = httpExchange.getRequestURI().getPath();
        this.headers = httpExchange.getRequestHeaders();
        try (InputStream is = httpExchange.getRequestBody()) {
            byte[] bytes = is.readAllBytes();
            this.body = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            this.body = "";
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        httpExchange.close();
    }
}
