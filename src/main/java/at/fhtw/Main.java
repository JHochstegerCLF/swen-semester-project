package at.fhtw;

import at.fhtw.presentation.Server;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector();
        try {
            injector.getInstance(Server.class).start(8080);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}