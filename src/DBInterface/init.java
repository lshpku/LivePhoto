package DBInterface;

import java.text.SimpleDateFormat;
import java.util.Date;


public class init {

    public static void main(String[] args) {
        for(int i = 0; i < 5; ++i){
            DBInterface.createAccount("test" + i);
            String theme = "UNK_" + i;
            String description = "UNK news for test" + i;
            String account = "test" + i;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(new Date());
            String content = "theme=" + theme + "description=" + description + "account=" + account + "time=" + time;
            DBInterface.establishTheme(content);
//            DBInterface.deleteTheme(i);
        }
        for (int i = 0; i < 5; ++i){
            String photo = null;
            String file_path = "../WebServer/demo";
            byte[] byte_1 = DBInterface.getBytes(file_path + "1000" + i);
            try{
                photo = new String(byte_1,"ISO-8859-1");}
            catch(java.io.UnsupportedEncodingException e){
                e.printStackTrace();
            }
            String account = "test" + i;
            String word = "UNK photo for test" + i;
            String content = "account=" + account + "num=" + i + "word=" + word + "photo=" + photo;
            DBInterface.sendInfo(content);
//            DBInterface.readDB2Image("../WebServer/demoResult", i + ".jpg", i);
        }
//        DBInterface.getFile(DBInterface.getPhoto(2), "../WebServer/demoResult", "test.jpg");
        News n = DBInterface.getNews(2);
        News[] n_lis = DBInterface.getRecentNews(3);
    }
}
