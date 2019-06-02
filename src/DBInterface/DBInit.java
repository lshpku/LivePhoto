package DBInterface;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DBInit {

    public static void main(String[] args) {
        for(int i = 0; i < 5; ++i){
            createAccount("test" + i);
            String theme = "UNK_" + i;
            String description = "UNK news for test" + i;
            String account = "test" + i;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(new Date());
            String content = "theme=" + theme + "description=" + description + "account=" + account + "time=" + time;
            establishTheme(content);
//            deleteTheme(i);
        }
        for (int i = 0; i < 5; ++i){
            String photo = null;
            String file_path = "../WebServer/demo";
            byte[] byte_1 = getBytes(file_path + "1000" + i);
            try{
                photo = new String(byte_1,"ISO-8859-1");}
            catch(java.io.UnsupportedEncodingException e){
                e.printStackTrace();
            }
            String account = "test" + i;
            String word = "UNK photo for test" + i;
            String content = "account=" + account + "num=" + i + "word=" + word + "photo=" + photo;
            sendInfo(content);
//            readDB2Image("../WebServer/demoResult", i + ".jpg", i);
        }
//        getFile(getPhoto(2), "../WebServer/demoResult", "test.jpg");
        News n = getNews(2);
        News[] n_lis = getRecentNews(3);
    }
}
