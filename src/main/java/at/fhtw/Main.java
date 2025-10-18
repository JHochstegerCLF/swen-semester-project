package at.fhtw;

import at.fhtw.presentation.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new Server().start(8080);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}