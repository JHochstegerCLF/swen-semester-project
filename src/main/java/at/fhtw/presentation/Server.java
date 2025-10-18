package at.fhtw.presentation;

import at.fhtw.presentation.handlers.MediaHandler;
import at.fhtw.presentation.handlers.RatingHandler;
import at.fhtw.presentation.handlers.UserHandler;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

public class Server {
    HttpServer server;
    public void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        bind("/api/users", UserHandler.class);
        bind("/api/media", MediaHandler.class);
        bind("/api/ratings", RatingHandler.class);

        server.start();
        System.out.println("Server started on port " + port);
    }

    private void bind(String path, Class<? extends HttpHandler> handler) {
        try {
            server.createContext(path, handler.getDeclaredConstructor(String.class).newInstance(path));
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
