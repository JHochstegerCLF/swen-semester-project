package at.fhtw.presentation;

import at.fhtw.presentation.handlers.BaseHandler;
import at.fhtw.presentation.handlers.MediaHandler;
import at.fhtw.presentation.handlers.RatingHandler;
import at.fhtw.presentation.handlers.UserHandler;
import com.google.inject.Inject;
import com.sun.net.httpserver.HttpServer;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class Server {
    private final UserHandler userHandler;
    private final MediaHandler mediaHandler;
    private final RatingHandler ratingHandler;
    private HttpServer server;

    public void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext(userHandler.getInitialPath(), userHandler);
        server.createContext(mediaHandler.getInitialPath(), mediaHandler);
        server.createContext(ratingHandler.getInitialPath(), ratingHandler);

        server.start();
        System.out.println("Server started on port " + port);
    }
}
