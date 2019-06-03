import WebServer.WebServer;
import BackGround.server;

public class LivePhoto {

    public static void main(String[] argv) {
        System.out.println("LivePhoto start...");
        new WebServer().start();
        new server().start();

    }
}