package at.fhtw;

import at.fhtw.presentation.Server;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // creates the injector so guice injection works
        Injector injector = Guice.createInjector(new Module());
        try {
            // creates a new instance of the server and starts it
            injector.getInstance(Server.class).start(8080);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}