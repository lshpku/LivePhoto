/*
 * Project    : Live Photo
 * Application: Web Server
 * Author     : Liang Shuhao
 */
package WebServer;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTMLProcessor - Generate index and news pages dynamically.
 */
public class HTMLProcessor {

    private static String cfgPath;

    public static void setCfgPath(String path) {
        cfgPath = path;
    }

    public static byte[] processIndex(byte[] content) {
        String[] newsArray = loadNews();
        String text = bytesToString(content);
        StringBuilder newsBuf = new StringBuilder();
        for (int i = newsArray.length - 1; i >=0; i--) {
            if (newsArray[i].equals(""))
                break;
            String id = getItem(newsArray[i], "id")[0];
            String title = getItem(newsArray[i], "title")[0];
            String[] metas = getItem(newsArray[i], "meta");
            String[] photos = getItem(newsArray[i], "photo");

            newsBuf.append("<div class=\"newsblock\">")
                    .append("<div class=\"newsbkg\">")
                    .append("<a class=\"quiet\" style=\"color: black;\" href=\"/")
                    .append(id).append("/\">")
                    .append("<div class=\"newsboard\">")
                    .append("<div class=\"newsicon\"><img src=\"/")
                    .append(id).append("/").append(photos[0]).append("\"></div>")
                    .append("<div class=\"newsintro\">")
                    .append("<div class=\"title\">").append(title).append("</div>")
                    .append("<div style=\"display: flex; flex-wrap: wrap;\">");
            for (String item : metas) {
                newsBuf.append("<div class=\"meta\">").append(item).append("</div>");
            }
            newsBuf.append("</div></div></div></a></div></div>\n");
        }

        text = text.replaceAll("\\{\\?news\\}", newsBuf.toString());
        return stringToBytes(text);
    }

    public static byte[] processPage(byte[] content, int newsId) {
        String[] newsArray = loadNews();
        String text = bytesToString(content);
        for (String news : newsArray) {
            if (news.equals(""))
                break;
            String id = getItem(news, "id")[0];
            if (Integer.parseInt(id) == newsId) {
                String title = getItem(news, "title")[0];
                String intro = getItem(news, "intro")[0];

                String[] metas = getItem(news, "meta");
                StringBuilder metaBuf = new StringBuilder();
                for (String item : metas)
                    metaBuf.append("<div class=\"meta\">").append(item).append("</div>");

                String[] photos = getItem(news, "photo");
                StringBuilder photoBuf = new StringBuilder();
                for (int i = photos.length - 1; i >= 0; i--)
                    photoBuf.append("<div class=\"posediv\"><img class=\"poseimg\" src=\"/")
                            .append(id).append("/").append(photos[i]).append("\"></div>");

                text = text.replaceAll("\\{\\?title\\}", title)
                        .replaceAll("\\{\\?meta\\}", metaBuf.toString())
                        .replaceAll("\\{\\?intro\\}", intro)
                        .replaceAll("\\{\\?photo\\}", photoBuf.toString());
                return stringToBytes(text);
            }
        }
        return null;
    }

    private static String[] getItem(String line, String name) {
        Pattern pattern = Pattern.compile(
                "-" + name + "=((\\'.*?\\')|([^\\s]*))(\\s+|$)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1).replaceAll("\\'", "").split(",");
        }
        return new String[]{""};
    }

    private static String[] loadNews() {
        File file = new File(cfgPath);
        try {
            InputStream in = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            in.read(bytes);
            String lines = new String(bytes, "utf-8");
            return lines.split("\n");
        } catch (IOException ex) {
        }
        return null;
    }

    private static String bytesToString(byte[] content) {
        try {
            return new String(content, "utf-8");
        } catch (UnsupportedEncodingException ex) {
        }
        return null;
    }

    private static byte[] stringToBytes(String content) {
        try {
            return content.getBytes("utf-8");
        } catch (UnsupportedEncodingException ex) {
        }
        return null;
    }
}
