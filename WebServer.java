import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

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

/*
 * ClientHandler - Handle the connection with one client socket.
 */
class ClientHandler implements Runnable {

    private Socket socket;
    private final static Pattern lineSpliter = Pattern.compile(
            "(.+)\\s+(.+)\\s+(.+)");
    private final static Pattern headerSpliter = Pattern.compile(
            "(.+):\\s+(.+)");
    private final static String CRLF = "\r\n";

    public ClientHandler(Socket connSocket) {
        socket = connSocket;
    }

    /*
    * run - Serve the client from requests to responses.
     */
    public void run() {
        try {
            try {
                InputStream inStream = socket.getInputStream(); // input stream
                OutputStream outStream = socket.getOutputStream(); // output stream
                Scanner in = new Scanner(inStream); // input reader
                PrintWriter out = new PrintWriter(outStream, true); // output writer
                Map<String, String> headerMap = new TreeMap<>(); // header recorder

                String reqLine[] = readRequestLine(in); // read request line
                if (reqLine != null)
                    System.out.println("> " + reqLine[0] + " " + reqLine[1] + " " + reqLine[2]);
                else {
                    System.out.println("Bad Request Line!");
                    return;
                }

                while (true) { // read request headers
                    String reqHeader[] = readRequestHeader(in);
                    if (reqHeader == null)
                        break;
                    System.out.println("  > " + reqHeader[0] + " " + reqHeader[1]);
                }

                String resBody = getTextFile(reqLine[1]); // write responses
                if (resBody != null)
                    writeResponseLine(out, 200);
                else
                    writeResponseLine(out, 400);
                writeResponseHeader(out);
                out.print(resBody);
                out.flush();
            } finally {
                socket.close();
            }
        } catch (IOException e) {
        }
    }

    /*
    * readRequestLine - Read the request line and return as
    *     ["GET", "/", "HTTP/1.1"] or null if the format is wrong.
     */
    private String[] readRequestLine(Scanner in) {
        String line = in.nextLine();
        Matcher matcher = lineSpliter.matcher(line);
        if (matcher.find()) {
            String elems[] = new String[3];
            elems[0] = matcher.group(1);
            elems[1] = matcher.group(2);
            elems[2] = matcher.group(3);
            return elems;
        }
        return null;
    }

    /*
    * readRequestHeader - Read a request header and return as
    *     ["Connection", "keep-alive"] or null if the format is wrong.
     */
    private String[] readRequestHeader(Scanner in) {
        String header = in.nextLine();
        Matcher matcher = headerSpliter.matcher(header);
        if (matcher.find()) {
            String elems[] = new String[2];
            elems[0] = matcher.group(1);
            elems[1] = matcher.group(2);
            return elems;
        }
        return null;
    }

    /*
    * getTextFile - Get the content of the specified text file.
     */
    private String getTextFile(String fileName) {
        File file = new File(fileName);
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        try {
            return new String(fileContent, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /*
    * writeResponseLine - Write the response line with specified status.
     */
    private void writeResponseLine(PrintWriter out, int status) {
        String line;
        if (status == 200)
            line = "HTTP/1.0 200 OK";
        else
            line = "HTTP/1.0 404 Not Found";
        out.print(line + CRLF);
    }

    /*
    * writeResponseHeader - Write response headers about the server itself.
     */
    private void writeResponseHeader(PrintWriter out) {
        out.print("Date: " + new Date().toString() + CRLF);
        out.print("Server: Java Server de Group 10 (CentOS 7)" + CRLF);
        out.print("Content-Type: text/html" + CRLF);
        out.print("Connection: closed" + CRLF);
        out.print(CRLF);
    }
}
