package at.fhtw.presentation.handlers;

import at.fhtw.presentation.annotations.*;
import at.fhtw.presentation.http.ContentType;
import at.fhtw.presentation.http.HttpMethod;
import at.fhtw.presentation.http.HttpStatus;
import at.fhtw.presentation.models.Context;
import at.fhtw.presentation.models.Response;
import at.fhtw.services.AuthService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.Getter;
import org.javatuples.Pair;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class BaseHandler implements HttpHandler {
    private final AuthService authService;
    private final String param = "\\{.*\\}";
    private final Map<Pair<HttpMethod, String>, Consumer<HttpExchange>> routes;
    @Getter
    private final String initialPath;

    public BaseHandler(
            String initialPath,
            AuthService authService
    ) {
        this.authService = authService;
        this.initialPath = initialPath;
        routes = new HashMap<>();
    }

    // This method handles the routing and authentication for all endpoints
    @Override
    public void handle(HttpExchange httpExchange) {
        // Create Context object which gets passed to the correct endpoint method
        Context context = new Context(httpExchange);
        String path = httpExchange.getRequestURI().getPath();
        // Filter to correct Method
        Optional<Method> possibleRoute = Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(m -> Arrays.stream(m.getDeclaredAnnotations())
                        .anyMatch(ann -> {
                            if (!ann.annotationType().getSimpleName().equals(getMethod(httpExchange).name())) {
                                return false;
                            }
                            return matches(path, getRoute(ann, getMethod(httpExchange)));
                        })
                )
                .findFirst();
        // if route found check authentication finish context and invoke method
        if (possibleRoute.isPresent()) {
            Method method = possibleRoute.get();
            // check for authentication requirement
            if (method.isAnnotationPresent(Auth.class)) {
                // check if token is present
                if (context.getToken() == null) {
                    System.out.println(getMethod(httpExchange).name() + ": " + httpExchange.getRequestURI().getPath() + " -> Unauthorized: No Token");
                    new Response(
                            HttpStatus.UNAUTHORIZED,
                            ContentType.PLAIN_TEXT,
                            "No token provided"
                    ).send(httpExchange);
                    return;
                }
                // check token validity
                if (!authService.validateToken(context.getToken())) {
                    System.out.println(getMethod(httpExchange).name() + ": " + httpExchange.getRequestURI().getPath() + " -> Unauthorized: Invalid Token");
                    new Response(
                            HttpStatus.UNAUTHORIZED,
                            ContentType.PLAIN_TEXT,
                            "Invalid token"
                    ).send(httpExchange);
                    return;
                }
            }
            // finish setting up context
            context.setPathParams(getPathParams(path, getPathFromMethod(method, getMethod(httpExchange))));
            context.setQueryParams(getQueryParams(httpExchange.getRequestURI().getQuery()));
            System.out.println(getMethod(httpExchange).name() + ": " + httpExchange.getRequestURI().getPath() + " -> " + method.getName());
            // invoke method
            try {
                method.invoke(this, context);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println(getMethod(httpExchange).name() + ": " + httpExchange.getRequestURI().getPath() + " -> Not found");
            new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.PLAIN_TEXT,
                    "No context found for request"
            ).send(httpExchange);
        }
    }

    // checks if requestPath and route match ignoring possible path params
    private boolean matches(String requestPath, String route) {
        String[] requestParts = splitPath(requestPath, initialPath);
        String[] routeParts = splitPath(route, "^/");
        if (requestParts.length != routeParts.length) {
            return false;
        }
        for (int i = 0; i < requestParts.length; i++) {
            String requestPart = requestParts[i].toLowerCase();
            String routePart = routeParts[i].toLowerCase();
            if (routePart.matches(param)) {
                continue;
            }
            if (!routePart.equals(requestPart)) {
                return false;
            }
        }
        return true;
    }

    // extracts path params from requestPath
    protected Map<String, String> getPathParams(String requestPath, String route) {
        String[] requestParts = splitPath(requestPath, initialPath);
        String[] routeParts = splitPath(route, "^/");
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < requestParts.length; i++) {
            String routePart = routeParts[i];
            if (routePart.matches(param)) {
                params.put(routePart.substring(1, routePart.length() - 1), requestParts[i]);
            }
        }
        return params;
    }

    // extracts query params from query
    protected Map<String, String> getQueryParams(String query) {
        HashMap<String, String> params = new HashMap<>();
        if (query == null) {
            return params;
        }
        Arrays.stream(query.split("&"))
                .forEach(s -> {
                    String[] parts = s.split("=");
                    params.put(parts[0], parts[1]);
                });
        return params;
    }

    protected HttpMethod getMethod(HttpExchange httpExchange) {
        return HttpMethod.valueOf(httpExchange.getRequestMethod());
    }

    // splits the path and removes possible prefix like "/api/users/"
    protected String[] splitPath(String path, String prefix) {
        return Arrays.stream(path.replaceFirst(prefix, "").split("/")).filter(s -> !s.isEmpty()).toArray(String[]::new);
    }

    // returns the route stored in the annotation
    protected String getRoute(Annotation annotation, HttpMethod method) {
        return switch (method) {
            case GET -> ((GET) annotation).path();
            case POST -> ((POST) annotation).path();
            case PUT -> ((PUT) annotation).path();
            case DELETE -> ((DELETE) annotation).path();
            default -> null;
        };
    }

    // returns the route stored in the annotation of given Method
    protected String getPathFromMethod(Method method, HttpMethod httpMethod) {
        return switch (httpMethod) {
            case GET -> method.getAnnotation(GET.class).path();
            case POST -> method.getAnnotation(POST.class).path();
            case PUT -> method.getAnnotation(PUT.class).path();
            case DELETE -> method.getAnnotation(DELETE.class).path();
            default -> null;
        };
    }
}
