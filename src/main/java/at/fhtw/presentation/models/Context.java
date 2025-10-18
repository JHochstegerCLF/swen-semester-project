package at.fhtw.presentation.models;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import lombok.*;

import java.util.Map;

@Data
public class Context {
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
        this.body = httpExchange.getRequestBody() == null ? "" : httpExchange.getRequestBody().toString();
    }
}
