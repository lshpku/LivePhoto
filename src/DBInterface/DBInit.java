package DBInterface;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBInit {

    private static final String FILE_PATH = "src/DBInterface/ImageDemo/";
    private static final String CFG_NAME = "newsdemo.cfg";
    private static final String COM_TEXT = "Ye are the light of the world. A city that is set on an hill cannot be hid.";

    public static void main(String[] args) {
        new DBInit().refreshDatabase();
    }

    public void refreshDatabase() {

        /* Delete existing tables and create new tables */
        DBInterface.refreshTables();
        System.out.println("Table refreshed...");

        /* Get all news configurations */
        String lines[] = getText(FILE_PATH + CFG_NAME).split("\n");
        System.out.println("Configuration read in...");

        for (int i = 0; i < lines.length; i++) {
            String account = getItems(lines[i], "account")[0];
            String theme = getItems(lines[i], "theme")[0];
            String description = getItems(lines[i], "descr")[0];
            String photos[] = getItems(lines[i], "photo");

            /* Create corresponding account */
            DBInterface.createAccount(account, "passwd" + i);

            /* Establish news */
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = dateFormat.format(new Date());
            String content = "theme=" + theme + "description=" + description + "account=" + account + "time=" + time;
            int themeId = DBInterface.establishTheme(content);

            /* Establish bonding photos */
            for (int j = 0; j < photos.length; j++) {
                byte[] bytes = DBInterface.getBytes(FILE_PATH + photos[j] + ".jpg");
                String word = "Test photo " + j + " for theme " + themeId + ". " + COM_TEXT;
                String info = "account=" + account + "num=" + themeId + "word=" + word;
                DBInterface.sendInfo(info, bytes);
            }

            System.out.println("Established news: " + theme);
        }
    }

    private static String getText(String path) {
        try {
            byte[] bytes = DBInterface.getBytes(path);
            return new String(bytes, "utf-8");
        } catch (IOException e) {
            return "";
        }
    }

    private static String[] getItems(String line, String key) {
        int keyIdx = line.indexOf("-" + key + "=");
        if (keyIdx < 0)
            return new String[]{""};
        int startIdx = keyIdx + key.length() + 3;
        int endIdx = line.indexOf('\'', startIdx);
        String items = line.substring(startIdx, endIdx);
        return items.split(",");
    }
}
