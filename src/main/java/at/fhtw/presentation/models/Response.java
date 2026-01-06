package at.fhtw.presentation.models;

import at.fhtw.presentation.http.ContentType;
import at.fhtw.presentation.http.HttpStatus;
import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private HttpStatus status;
    private ContentType contentType;
    private String content;

    public void send(HttpExchange httpExchange) {
        httpExchange.getResponseHeaders().add("Cache-Control", "nocache");
        httpExchange.getResponseHeaders().add("Content-Type", contentType.type);

        try (httpExchange) {
            if (status == HttpStatus.NO_CONTENT) {
                httpExchange.sendResponseHeaders(status.code, -1);
            } else {
                byte[] responseBody = content.getBytes(StandardCharsets.UTF_8);
                httpExchange.sendResponseHeaders(status.code, responseBody.length);
                httpExchange.getResponseBody().write(responseBody);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}