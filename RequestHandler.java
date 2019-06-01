/*
 * Project    : Live Photo
 * Application: Web Server
 * Author     : Liang Shuhao
 */
package WebServer;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.*;

/**
 * RequestHandler - Read and parse requests.
 */
public class RequestHandler {

    private final static Pattern LINE_SPLIT = Pattern.compile(
            "(.+)\\s+(.+)\\s+(.+)");
    private final static Pattern HEADER_SPLIT = Pattern.compile(
            "(.+):\\s+(.+)");

    private final Socket socket; // client socket
    private final InputStream inStream;// input stream
    private final Scanner scanner; // input reader

    public RequestHandler(Socket socket) throws IOException {
        this.socket = socket;
        inStream = socket.getInputStream(); // input stream
        scanner = new Scanner(inStream); // input reader
    }

    /*
     * readAll - Read and parse all requests.
     */
    public void readAll() {
        if (readRequestLine() == false)
            return;
        while (readRequestHeader()) {
        }
    }

    /*
     * readRequestLine - Read the request line as 
     *     ["GET", "/", "HTTP/1.1"], return true if success.
     */
    private boolean readRequestLine() {
        String line = scanner.nextLine();
        Matcher matcher = LINE_SPLIT.matcher(line);
        if (matcher.find()) {
            method = matcher.group(1);
            path = matcher.group(2);
            protocol = matcher.group(3);
            return true;
        }
        return false;
    }

    /*
     * readRequestHeader - Read a request header as
     *     ["Connection", "keep-alive"], return true if success.
     */
    private boolean readRequestHeader() {
        String header = scanner.nextLine();
        Matcher matcher = HEADER_SPLIT.matcher(header);
        if (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            return true;
        }
        return false;
    }

    private String method;
    private String path;
    private String protocol;
    
    public String getMethod(){
        return method;
    }
    public String getPath(){
        return path;
    }
    public String getProtocol(){
        return protocol;
    }
}
