/*
 * Project    : Live Photo
 * Application: Web Server
 * Author     : Liang Shuhao
 */
package WebServer;

import java.io.*;
import java.net.Socket;
import java.util.*;

/*
 * ClientHandler - Handle the connection with one client socket.
 */
class ClientHandler implements Runnable {

    private final Socket socket;

    public ClientHandler(Socket connSocket) {
        socket = connSocket;
    }

    /*
     * run - Serve the client from requests to responses.
     */
    public void run() {
        try {
            try {
                RequestHandler request = new RequestHandler(socket);
                ResponseHandler response = new ResponseHandler(socket);
                request.readAll();
                FileHandler content = new FileHandler(request.getPath());
                response.setContent(content.getContent());
                response.setStatus(content.getStatus());
                response.setContentType(content.getContentType());
                response.writeAll();
            } catch (NoSuchElementException e) { // in case scanner fails
            } finally {
                socket.close();
            }
        } catch (IOException e) {
        }
    }
}
