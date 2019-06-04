/*
 * Project    : Live Photo
 * Application: Web Server
 * Author     : Liang Shuhao
 */
package WebServer;

import java.io.*;
import DBInterface.*;

/**
 * HTMLProcessor - Generate index and news pages dynamically.
 */
public class HTMLProcessor {

    /**
     * processIndex - Generate index page with recent news.
     */
    public static byte[] processIndex(byte[] content, News[] newsList) {
        if (content == null || newsList == null)
            return content;
        String text = bytesToString(content);
        StringBuilder newsBuf = new StringBuilder();

        for (News news : newsList) {
            String photoUrl;
            if (news.photos.length == 0)
                photoUrl = "blank";
            else
                photoUrl = news.id + "/" + news.photos[0].id;
            newsBuf.append("<div class=\"newsblock\">")
                    .append("<div class=\"newsbkg\">")
                    .append("<a class=\"quiet\" style=\"color: black;\" href=\"/")
                    .append(news.id).append("/\">")
                    .append("<div class=\"newsboard\">")
                    .append("<div class=\"newsicon\"><img src=\"/")
                    .append(photoUrl).append(".jpg\"></div>")
                    .append("<div class=\"newsintro\">")
                    .append("<div class=\"title\">").append(news.title).append("</div>")
                    .append("<div style=\"display: flex; flex-wrap: wrap;\">")
                    .append("<div class=\"meta\">直播者：").append(news.account).append("</div>")
                    .append("<div class=\"meta\">开始时间：").append(news.stTime).append("</div>")
                    .append("</div></div></div></a></div></div>\n");
        }

        text = text.replaceAll("\\{\\?news\\}", newsBuf.toString());
        return stringToBytes(text);
    }

    /**
     * processPage - Generate news page with corresponding news.
     */
    public static byte[] processPage(byte[] content, News news) {
        if (content == null)
            return null;
        String text = bytesToString(content);

        StringBuilder metaBuf = new StringBuilder();
        metaBuf.append("<div class=\"meta\">直播者：").append(news.account).append("</div>")
                .append("<div class=\"meta\">开始时间：").append(news.stTime).append("</div>");

        StringBuilder photoBuf = new StringBuilder();
        for (int i = news.photos.length - 1; i >= 0; i--) {
            photoBuf.append("<div class=\"posediv\"><img class=\"poseimg\" src=\"/")
                    .append(news.id).append("/").append(news.photos[i].id)
                    .append(".jpg\" alt=\"").append(news.photos[i].intro).append("\" /></div>");
        }

        text = text.replaceAll("\\{\\?title\\}", news.title)
                .replaceAll("\\{\\?meta\\}", metaBuf.toString())
                .replaceAll("\\{\\?intro\\}", news.intro)
                .replaceAll("\\{\\?photo\\}", photoBuf.toString());
        return stringToBytes(text);
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
