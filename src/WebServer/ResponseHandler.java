/*
 * Project    : Live Photo
 * Application: Web Server
 * Author     : Liang Shuhao
 */
package WebServer;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * ResponseHandler - Write responses.
 */
public class ResponseHandler {

    private final static String CRLF = "\r\n";

    private final Socket socket; // client socket
    private final OutputStream outStream;// output stream
    private final PrintWriter textWriter; // text writer
    private final DataOutputStream byteWriter; // byte writer

    private byte[] content;
    private String contentType;
    private int status = 200;

    public ResponseHandler(Socket socket) throws IOException {
        this.socket = socket;
        outStream = socket.getOutputStream();
        textWriter = new PrintWriter(outStream);
        byteWriter = new DataOutputStream(outStream);
    }

    public void setContent(byte[] content) {
        this.content = new byte[content.length];
        System.arraycopy(content, 0, this.content, 0, content.length);
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void writeAll() throws IOException {
        writeResponseLine();
        writeResponseHeader();
        textWriter.flush();
        byteWriter.write(content);
        byteWriter.flush();
    }

    /*
     * writeResponseLine - Write the response line with specified status.
     */
    private void writeResponseLine() {
        String line;
        if (status == 200)
            line = "HTTP/1.0 200 OK";
        else
            line = "HTTP/1.0 404 Not Found";
        textWriter.print(line + CRLF);
    }

    /*
     * writeResponseHeader - Write response headers about the server itself.
     */
    private void writeResponseHeader() {
        textWriter.print("Date: " + new Date().toString() + CRLF);
        textWriter.print("Server: Java Server de Group 10 (CentOS 7)" + CRLF);
        if (contentType != null) {
            textWriter.print("Content-Type: " + contentType + CRLF);
            if (status == 200 && contentType.equals("text/html") == false)
                textWriter.print("Cache-Control: max-age=600" + CRLF);
        }
        textWriter.print("Connection: closed" + CRLF);
        textWriter.print(CRLF);
    }
}
