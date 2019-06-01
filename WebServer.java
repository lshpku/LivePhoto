/*
 * Project    : Live Photo
 * Application: Web Server
 * Author     : Liang Shuhao
 */
package WebServer;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class WebServer {

    public static void main(String[] args) throws IOException {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                8, 24, 200, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        try {
            ServerSocket listSocket = new ServerSocket(80); // establish listening socket
            while (true) {
                Socket connSocket = listSocket.accept(); // wait for client's connection
                Runnable handler = new ClientHandler(connSocket);
                threadPool.execute(handler); // serve the client
            }
        } catch (IOException e) {
        }
    }
}
