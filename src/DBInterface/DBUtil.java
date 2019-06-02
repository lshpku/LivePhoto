package DBInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DBUtil {
    // 定义数据库连接参数
    public static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/livephoto?serverTimezone=UTC";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";

    // 注册数据库驱动
    static {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            System.out.println("DB Driver loading failed!");
            e.printStackTrace();
        }
    }

    // 获取连接
    public static Connection getConn() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    // 关闭连接
    public static void closeConn(Connection conn) {
        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("DB connection closing failed!");
                e.printStackTrace();
            }
        }
    }

}