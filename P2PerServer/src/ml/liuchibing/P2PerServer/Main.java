package ml.liuchibing.P2PerServer;

import ml.liuchibing.P2PerLib.*;

import java.time.LocalTime;

public class Main {
    static final int PORT = 42800;

    public static void main(String[] args) {
        Server server = new Server(PORT, new Logger() {
            @Override
            public void Log(String msg) {
                System.out.print(LocalTime.now().toString() + ": " + msg);
            }
        });
        server.Start();
    }

}