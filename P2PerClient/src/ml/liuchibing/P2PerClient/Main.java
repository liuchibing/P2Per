package ml.liuchibing.P2PerClient;

import ml.liuchibing.P2PerLib.Client;
import ml.liuchibing.P2PerLib.Logger;

import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        Client client = new Client("test", "192.168.31.101", 42801, new Logger() {
            @Override
            public void Log(String msg) {
                System.out.print(LocalTime.now().toString() + ": " + msg + "\n");
            }
        });
        client.Start();
    }
}