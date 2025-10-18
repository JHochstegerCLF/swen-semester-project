package at.fhtw.presentation.handlers;

import at.fhtw.presentation.models.Context;
import at.fhtw.presentation.models.HttpMethod;
import at.fhtw.presentation.annotations.DELETE;
import at.fhtw.presentation.annotations.GET;
import at.fhtw.presentation.annotations.POST;
import at.fhtw.presentation.annotations.PUT;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.javatuples.Pair;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class BaseHandler implements HttpHandler {
    private final String param = "\\{.*\\}";
    private final Map<Pair<HttpMethod, String>, Consumer<HttpExchange>> routes;
    private final String initialPath;

    public BaseHandler(String initialPath) {
        this.initialPath = initialPath.toLowerCase();
        routes = new HashMap<>();
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        Context context = new Context(httpExchange);
        String path = httpExchange.getRequestURI().getPath();
        //Filter to correct Method
        Optional<Method> possibleRoute = Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(m -> Arrays.stream(m.getDeclaredAnnotations())
                        .anyMatch(ann -> {
                            if (!ann.annotationType().getSimpleName().equals(getMethod(httpExchange).name())) {
                                return false;
                            }
                            return matches(path, getPath(ann, getMethod(httpExchange)));
                        })
                )
                .findFirst();
        //When found finish setting up context and invoke method
        if (possibleRoute.isPresent()) {
            Method method = possibleRoute.get();
            context.setParams(getParams(path, getPathFromMethod(method, getMethod(httpExchange)))); //Not my best work, but it works since all the annotations have the same path value
            System.out.println(getMethod(httpExchange).name() + ": " + httpExchange.getRequestURI().getPath() + " -> " + method.getName());
            try {
                method.invoke(this, context);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            try {
                System.out.println(getMethod(httpExchange).name() + ": " + httpExchange.getRequestURI().getPath() + " -> Not found");
                httpExchange.sendResponseHeaders(404, -1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean matches(String requestPath, String route) {
        String[] requestParts = splitPath(requestPath, initialPath);
        String[] routeParts = splitPath(route, "^/");
        if (requestParts.length != routeParts.length) {
            return false;
        }
        for (int i = 0; i < requestParts.length; i++) {
            String requestPart = requestParts[i];
            String routePart = routeParts[i];
            if (routePart.matches(param)) {
                continue;
            }
            if (!routePart.equals(requestPart)) {
                return false;
            }
        }
        return true;
    }

    protected Map<String, String> getParams(String requestPath, String route) {
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

    protected HttpMethod getMethod(HttpExchange httpExchange) {
        return HttpMethod.valueOf(httpExchange.getRequestMethod());
    }

    protected String[] splitPath(String path, String prefix) {
        return Arrays.stream(path.toLowerCase().replaceFirst(prefix, "").split("/")).filter(s -> !s.isEmpty()).toArray(String[]::new);
    }

    protected String getPath(Annotation annotation, HttpMethod method) {
        return switch (method) {
            case GET -> ((GET) annotation).path();
            case POST -> ((POST) annotation).path();
            case PUT -> ((PUT) annotation).path();
            case DELETE -> ((DELETE) annotation).path();
            default -> null;
        };
    }

    protected String getPathFromMethod(Method method, HttpMethod httpMethod) {
        return switch (httpMethod) {
            case GET -> ((GET) method.getAnnotation(GET.class)).path();
            case POST -> ((POST) method.getAnnotation(POST.class)).path();
            case PUT -> ((PUT) method.getAnnotation(PUT.class)).path();
            case DELETE -> ((DELETE) method.getAnnotation(DELETE.class)).path();
            default -> null;
        };
    }
}
