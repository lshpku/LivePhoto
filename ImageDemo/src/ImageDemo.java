/**
 *
 */

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * @author Administrator 测试写入数据库以及从数据库中读取
 */
public class ImageDemo {
    public static HashMap<String, ArrayList<Object>> getAccountContent(String name){
        int account_id = getAccountId(name);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<String, ArrayList<Object> > res = new HashMap<String, ArrayList<Object> >();
        ArrayList<Object> titles = new ArrayList<Object>();
        ArrayList<Object> ids = new ArrayList<Object>();
        try{
            conn = DBUtil.getConn();
            String sql = "select * from news where account_id =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, account_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                String t = rs.getString("title");
                Integer i = rs.getInt("id");
                titles.add(t);
                ids.add(i);
            }
            res.put("id", ids);
            res.put("title", titles);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConn(conn);
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }
    public static int getAccountId(String name){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int account_id = -1;
        try{
            conn = DBUtil.getConn();
            String sql = "select * from account where name =?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();
            account_id = rs.getInt("id");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConn(conn);
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return account_id;
    }
    public static void deleteTheme(int news_id){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = DBUtil.getConn();
            String sql = "delete from news where id =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, news_id);
            ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConn(conn);
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static int establishTheme(String content){
        Connection conn = null;
        PreparedStatement ps = null;
        int theme_idx = content.indexOf("theme=");
        int description_idx = content.indexOf("description=");
        int account_idx = content.indexOf("account=");
        int time_idx = content.indexOf("time=");
        String theme = content.substring(theme_idx + "theme=".length(), description_idx);
        String description = content.substring(description_idx + "description=".length(), account_idx);
        String account = content.substring(account_idx + "account=".length(), time_idx);
        String time = content.substring(time_idx + "time=".length(), content.length());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try{
            date = sdf.parse(time);
        } catch (java.text.ParseException e){
            e.printStackTrace();
        }
        Timestamp timestamp = new Timestamp(date.getTime());
        int news_id = -1;
        int account_id = getAccountId(account);
        try{
            String sql = "insert into news (title, intro, account_id, time)values(?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, theme);
            ps.setString(2, description);
            ps.setInt(3, account_id);
            ps.setTimestamp(4, timestamp);
            news_id = ps.executeUpdate(sql, ps.RETURN_GENERATED_KEYS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConn(conn);
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return news_id;
    }
    public static void sendInfo(String content){
        int account_idx = content.indexOf("account=");
        int news_idx = content.indexOf("num=");
        int description_idx = content.indexOf("word=");
        int photo_idx = content.indexOf("photo=");
        String account_name = content.substring(account_idx + "account=".length(), news_idx);
        int account_id = getAccountId(account_name);
        int news_id = Integer.parseInt(content.substring(news_idx + "num=".length(), description_idx));
        String description = content.substring(description_idx + "word=".length(), photo_idx);
        byte[] photos = content.substring(photo_idx + "photo=".length(), content.length()).getBytes();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int photo_id = -1;
        try {
            conn = DBUtil.getConn();
            String sql = "insert into photo (photo, intro, news_id) values(?,?,?)";
            ps = conn.prepareStatement(sql);
//            Blob photos_blob = conn.createBlob();
//            photos_blob.setBytes(1, photos);
//            ps.setBlob(1, photos_blob);
            ps.setBytes(1, photos);
            ps.setString(2, description);
            ps.setInt(3, news_id);
            photo_id = ps.executeUpdate(sql, ps.RETURN_GENERATED_KEYS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConn(conn);
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

//        try {
//            conn = DBUtil.getConn();
//            String sql = "select * from news where account_id=?";
//            ps = conn.prepareStatement(sql);
//            ps.setInt(1, account_id);
//            rs = ps.executeQuery();
//            byte[] photo_ids = rs.getBytes("photos");
//            ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(photo_ids);
//            ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
//            ArrayList<Integer> photo_lis = (ArrayList<Integer>)inputStream.readObject();
//            inputStream.close();
//            arrayInputStream.close();
//            photo_lis.add(photo_id);
//
//            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
//            objectOutputStream.writeObject(photo_lis);
//            objectOutputStream.flush();
//            photo_ids = arrayOutputStream.toByteArray();
//            objectOutputStream.close();
//            arrayOutputStream.close();
//
//            sql = "update news set photos=? where account_id=?";
//            ps = conn.prepareStatement(sql);
//            ps.setBytes(1, photo_ids);
//            ps.setInt(2, account_id);
//            ps.executeUpdate();
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            DBUtil.closeConn(conn);
//            if (null != ps) {
//                try {
//                    ps.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

    }
    public static byte[] getPhoto(int photo_id){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        byte[] photos = null;
        try{
            conn = DBUtil.getConn();
            String sql = "select * from photo where id =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, photo_id);
            rs = ps.executeQuery();
            photos = rs.getBytes("photo");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConn(conn);
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return photos;
    }
    public static News getNews(int news_id){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        News res = null;
        try{
            conn = DBUtil.getConn();
            String sql = "select * from news where id =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, news_id);
            rs = ps.executeQuery();
            String title = rs.getString("title");
            String intro = rs.getString("intro");

            News n = new News(title, intro);
            ArrayList<Photo> p = new ArrayList<>();
            sql = "select * from photo where news_id =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, news_id);
            rs = ps.executeQuery();
            String p_intro = rs.getString("intro");
            byte[] p_content = rs.getBytes("photo");
            p.add(new Photo(p_intro, p_content));
            while (rs.next()) {
                p_intro = rs.getString("intro");
                p_content = rs.getBytes("photo");
                p.add(new Photo(p_intro, p_content));
            }
            Photo[] _p = new Photo[p.size()];
            for (int i=0; i<p.size(); ++i){
                _p[i] = (Photo)p.get(i);
            }
            n.setPhoto(_p);
            res = n;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConn(conn);
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }
    public static News[] getRecentNews(int k){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        News[] res = null;
        try{
            conn = DBUtil.getConn();
            String sql = "select top ? * from news order by time";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, k);
            rs = ps.executeQuery();
            int id = rs.getInt("id");
            ArrayList<News> n = new ArrayList<>();
            n.add(getNews(id));
            while(rs.next()){
                n.add(getNews(rs.getInt("id")));
            }
            res = new News[n.size()];
            for (int i=0;i < n.size();++i){
                res[i] = (News)n.get(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConn(conn);
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    // 将图片插入数据库
    public static void readImage2DB() {
        String path = "/Users/wangyanbin/Desktop/test1.jpg";
        Connection conn = null;
        PreparedStatement ps = null;
        FileInputStream in = null;
        try {
            in = ImageUtil.readImage(path);
            conn = DBUtil.getConn();
            String sql = "insert into photo (id,name,photo)values(?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, 1);
            ps.setString(2, "Tom");
            ps.setBinaryStream(3, in, in.available());
            int count = ps.executeUpdate();
            if (count > 0) {
                System.out.println("插入成功！");
            } else {
                System.out.println("插入失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConn(conn);
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    // 读取数据库中图片
    public static void readDB2Image() {
        String targetPath = "/Users/wangyanbin/Desktop/test/test1.jpg";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConn();
            String sql = "select * from photo where id =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, 1);
            rs = ps.executeQuery();
            while (rs.next()) {
                InputStream in = rs.getBinaryStream("photo");
                ImageUtil.readBin2Image(in, targetPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConn(conn);
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    //测试
    public static void main(String[] args) {
//        readImage2DB();
//        readDB2Image();
    }
}