package DBInterface;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBInit {

    private static final String FILE_PATH = "src/DBInterface/ImageDemo/";
    private static final String CFG_NAME = "newsdemo.cfg";
    private static final String RAND_TEXT[] = new String[]{
        "【太5:11】人若因我辱骂你们，逼迫你们，捏造各样坏话毁谤你们，你们就有福了。",
        "【太5:12】应当欢喜快乐，因为你们在天上的赏赐是大的。在你们以前的先知，人也是这样逼迫他们。",
        "【太5:13】你们是世上的盐。盐若失了味，怎能叫他再咸呢？以后无用，不过丢在外面，被人践踏了。",
        "【太5:14】你们是世上的光。城造在山上，是不能隐藏的。",
        "【太5:15】人点灯，不放在斗底下，是放在灯台上，就照亮一家的人。",
        "【太5:16】你们的光也当这样照在人前，叫他们看见你们的好行为，便将荣耀归给你们在天上的父。"
    };

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
                String word = "Test photo " + j + " for theme " + themeId + ". "
                        + RAND_TEXT[(int)(Math.random() * RAND_TEXT.length)];
                String info = "account=" + account + "num=" + themeId + " word=" + word;
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
