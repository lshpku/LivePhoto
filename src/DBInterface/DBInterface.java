package DBInterface;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class DBInterface {

    public static int getAccountId(String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        int account_id = -1;
        try {
            conn = DBUtil.getConn();
            String sql = "select * from account where name =?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();
            while (rs.next()) {
                account_id = rs.getInt("id");
            }
        } catch (SQLException e) {
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
    public static String getAccountName(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        String account_name = null;
        try {
            conn = DBUtil.getConn();
            String sql = "select * from account where id =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                account_name = rs.getString("name");
            }
        } catch (SQLException e) {
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
        return account_name;
    }
    public static HashMap<String, ArrayList<Object>> getAccountContent(String name) {
        int account_id = getAccountId(name);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        HashMap<String, ArrayList<Object>> res = new HashMap<String, ArrayList<Object>>();
        ArrayList<Object> titles = new ArrayList<Object>();
        ArrayList<Object> ids = new ArrayList<Object>();
        try {
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
        } catch (SQLException e) {
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
    public static void deleteTheme(int news_id) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConn();
            String sql = "delete from news where id =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, news_id);
            ps.executeUpdate();
        } catch (SQLException e) {
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
    public static int establishTheme(String content) {
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
        try {
            date = sdf.parse(time);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Timestamp timestamp = new Timestamp(date.getTime());
        int news_id = -1;
        int account_id = getAccountId(account);
        try {
            conn = DBUtil.getConn();
            String sql = "insert into news(title,intro,account_id,news_time) values" + "(?,?,?,?)";
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, theme);
            ps.setString(2, description);
            ps.setInt(3, account_id);
            ps.setTimestamp(4, timestamp);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                news_id = rs.getInt(1);
            }
        } catch (SQLException e) {
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
    public static void sendInfo(String content) {
        int account_idx = content.indexOf("account=");
        int news_idx = content.indexOf("num=");
        int description_idx = content.indexOf("word=");
        int photo_idx = content.indexOf("photo=");
        String account_name = content.substring(account_idx + "account=".length(), news_idx);
        int account_id = getAccountId(account_name);
        int news_id = Integer.parseInt(content.substring(news_idx + "num=".length(), description_idx));
        String description = content.substring(description_idx + "word=".length(), photo_idx);
        byte[] photos = null;
        try {
            photos = content.substring(photo_idx + "photo=".length(), content.length()).getBytes("ISO-8859-1");
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConn();
            String sql = "insert into photo (photo, intro, news_id) values(?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setBytes(1, photos);
            ps.setString(2, description);
            ps.setInt(3, news_id);
            ps.executeUpdate();
        } catch (SQLException e) {
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

    public static byte[] getPhoto(int photo_id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        byte[] photos = null;
        try {
            conn = DBUtil.getConn();
            String sql = "select * from photo where id =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, photo_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                photos = rs.getBytes("photo");
            }
        } catch (SQLException e) {
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
    public static News getNews(int news_id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        News res = null;
        try {
            conn = DBUtil.getConn();
            String sql = "select * from news where id =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, news_id);
            rs = ps.executeQuery();
            String title = null;
            String intro = null;
            String account_name = null;
            String news_time = null;
            if (rs.next()) {
                account_name = getAccountName(rs.getInt("account_id"));
                title = rs.getString("title");
                intro = rs.getString("intro");
                news_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rs.getTimestamp("news_time"));
            }
            News n = new News(news_id, account_name, title, intro, news_time);
            ArrayList<Photo> p = new ArrayList<>();
            sql = "select * from photo where news_id =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, news_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                int p_id = rs.getInt("id");
                String p_intro = rs.getString("intro");
                byte[] p_content = rs.getBytes("photo");
                p.add(new Photo(p_id, p_intro, p_content));
            }
            Photo[] _p = new Photo[p.size()];
            for (int i = 0; i < p.size(); ++i) {
                _p[i] = (Photo) p.get(i);
            }
            n.setPhoto(_p);
            res = n;
        } catch (SQLException e) {
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
    public static News[] getRecentNews(int k) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        News[] res = null;
        try {
            conn = DBUtil.getConn();
            String sql = "select * from news order by news_time desc limit ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, k);
            rs = ps.executeQuery();

            ArrayList<News> n = new ArrayList<>();
            while (rs.next()) {
                n.add(getNews(rs.getInt("id")));
            }
            res = new News[n.size()];
            for (int i = 0; i < n.size(); ++i) {
                res[i] = (News) n.get(i);
            }
        } catch (SQLException e) {
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

    // 测试函数：建立新账户
    public static void createAccount(String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConn();
            String sql = "insert into account (name) values(?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
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

    // 测试函数：读取数据库中new_id的图片，写入"filepath/filename"
    public static void readDB2Image(String filepath, String fileName, int news_id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConn();
            String sql = "select * from photo where news_id =?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, news_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                getFile(rs.getBytes("photo"), filepath, fileName);
            }
        } catch (SQLException e) {
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

    // 图片->byte[]
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    // byte[]->图片
    public static void getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "/" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
