/*
 * Project    : Live Photo
 * Application: Web Server
 * Author     : Liang Shuhao
 */
package WebServer;

import java.io.*;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.regex.*;
import DBInterface.*;

/**
 * FileHandler - Parse file path and load file.
 */
public class FileHandler {

    /*
     * Valid file path:
     *   Index: /
     *   Root : /xxx(.xxx)
     *   News : /integer
     *   Photo: /integer/integer.xxx
     */
    private final static Pattern ROOT_SPLIT = Pattern.compile(
            "^/(\\w+(\\.\\w+)*)/?$");
    private final static Pattern NEWS_SPLIT = Pattern.compile(
            "^/([0-9]{1,8})/?$");
    private final static Pattern PHOTO_SPLIT = Pattern.compile(
            "^/([0-9]{1,8})/([0-9]{1,8})(\\.\\w+)/?$");
    private final static Pattern SUFFIX_SPLIT = Pattern.compile(
            "\\.(\\w+)/?$");
    private final static String ROOT_DIR;
    private final static String[] PHOTO_SUFFIX = new String[]{
        "jpg", "jpeg", "png", "gif", "bmp", "ico"
    };
    private final static TreeSet<String> PHOTOS; // valid photo formats
    private final static int MAX_NEWS = 20;

    /* Decide runtime path and load validations */
    static {
        File file = new File("src");
        if (file.exists())
            ROOT_DIR = "src/";
        else
            ROOT_DIR = "";
        PHOTOS = new TreeSet<>();
        PHOTOS.addAll(Arrays.asList(PHOTO_SUFFIX));
    }

    private byte[] content;
    private String contentType;
    private int status;

    /* Parse the path and load the file. */
    public FileHandler(String path) {
        status = 200;
        parseSuffix(path);
        loadFile(path);
    }

    private void parseSuffix(String path) {
        Matcher matcher = SUFFIX_SPLIT.matcher(path);
        if (matcher.find()) {
            String suffix = matcher.group(1);
            if ("js".equals(suffix))
                contentType = "text/javascript";
            else if (PHOTOS.contains(suffix))
                contentType = "image/" + suffix;
            else
                contentType = "text/" + suffix;
        } else {
            contentType = "text/html";
        }
    }

    private void loadFile(String path) {
        if ("/".equals(path)) {
            if (loadBytes("root", "index.html") == false)
                return;
            content = HTMLProcessor.processIndex(content,
                    DBInterface.getRecentNews(MAX_NEWS));
            return;
        }

        Matcher news = NEWS_SPLIT.matcher(path);
        if (news.find()) {
            if (loadBytes("root", "page.html")) {
                content = HTMLProcessor.processPage(content,
                        DBInterface.getNews(Integer.parseInt(news.group(1))));
                if (content == null)
                    status = 404;
            }
            return;
        }

        Matcher root = ROOT_SPLIT.matcher(path);
        if (root.find()) {
            loadBytes("root", root.group(1));
            return;
        }

        Matcher photo = PHOTO_SPLIT.matcher(path);
        if (photo.find()) {
            content = DBInterface.getPhoto(
                    Integer.parseInt(photo.group(2)));
            return;
        }

        status = 404;
    }

    private boolean loadBytes(String dir, String name) {
        try {
            File file = new File(ROOT_DIR + "WebServer/" + dir + "/" + name);
            FileInputStream stream = new FileInputStream(file);
            content = new byte[(int) file.length()];
            stream.read(content);
        } catch (IOException e) {
            status = 404;
            return false;
        }
        return true;
    }

    /* Return content and related info. */
    public byte[] getContent() {
        if (content == null)
            return "<p>404 Not Found</p>".getBytes();
        return content;
    }

    public String getContentType() {
        return contentType;
    }

    public int getStatus() {
        return status;
    }
}
