package DBInterface;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBInit {

    private static final String FILE_PATH = "src/DBInterface/ImageDemo/";
    private static final String CFG_NAME = "newsdemo.cfg";
    private static final String RAND_TEXT[] = new String[] {
        "【太5:1】耶稣看见这许多的人，就上了山，既已坐下，门徒到他跟前来。",
        "【太5:2】他就开口教训他们，说：",
        "【太5:3】“虚心的人有福了，因为天国是他们的。",
        "【太5:4】哀恸的人有福了，因为他们必得安慰。",
        "【太5:5】温柔的人有福了，因为他们必承受地土。",
        "【太5:6】饥渴慕义的人有福了，因为他们必得饱足。",
        "【太5:7】怜恤人的人有福了，因为他们必蒙怜恤。",
        "【太5:8】清心的人有福了，因为他们必得见神。",
        "【太5:9】使人和睦的人有福了，因为他们必称为神的儿子。",
        "【太5:10】为义受逼迫的人有福了，因为天国是他们的。",
        "【太5:11】人若因我辱骂你们，逼迫你们，捏造各样坏话毁谤你们，你们就有福了。",
        "【太5:12】应当欢喜快乐，因为你们在天上的赏赐是大的。在你们以前的先知，人也是这样逼迫他们。",
        "【太5:13】你们是世上的盐。盐若失了味，怎能叫它再咸呢？以后无用，不过丢在外面，被人践踏了。",
        "【太5:14】你们是世上的光。城造在山上，是不能隐藏的。",
        "【太5:15】人点灯，不放在斗底下，是放在灯台上，就照亮一家的人。",
        "【太5:16】你们的光也当这样照在人前，叫他们看见你们的好行为，便将荣耀归给你们在天上的父。",
        "【太5:17】莫想我来要废掉律法和先知；我来不是要废掉，乃是要成全。",
        "【太5:18】我实在告诉你们：就是到天地都废去了，律法的一点一画也不能废去，都要成全。",
        "【太5:19】所以，无论何人废掉这诫命中最小的一条，又教训人这样做，他在天国要称为最小的；但无论何人遵行这诫命，又教训人遵行，他在天国要称为大的。",
        "【太5:20】我告诉你们：你们的义若不胜于文士和法利赛人的义，断不能进天国。",
        "【太5:21】你们听见有吩咐古人的话，说：‘不可杀人’，又说：‘凡杀人的 ，难免受审判。’",
        "【太5:22】只是我告诉你们：凡向弟兄动怒的，难免受审判。凡骂弟兄是拉加的，难免公会的审断；凡骂弟兄是魔利的，难免地狱的火。",
        "【太5:23】所以，你在祭坛上献礼物的时候，若想起弟兄向你怀怨，",
        "【太5:24】就把礼物留在坛前，先去同弟兄和好，然后来献礼物。",
        "【太5:25】你同告你的对头还在路上，就赶紧与他和息，恐怕他把你送给审判官，审判官交付衙役，你就下在监里了。",
        "【太5:26】我实在告诉你：若有一文钱没有还清，你断不能从那里出来。"
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
