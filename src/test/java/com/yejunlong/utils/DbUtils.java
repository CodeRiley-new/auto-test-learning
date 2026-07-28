package com.yejunlong.utils;

import java.sql.*;

public class DbUtils {

    //连接数据库
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test_db?useSSL=false&serverTimezone=UTC";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "123456";

    /**
     * 查询单个字符串值
     * 例如：SELECT firstname FROM booking WHERE id = 1
     */
    public static String queryString(String sql){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            //加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            //连接数据库
            conn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);

            //创建Statement
            stmt = conn.createStatement();

            //执行SQL
            rs = stmt.executeQuery(sql);

            //处理结果
            if(rs.next()){
                return rs.getString(1);
            }
            return null;
        }catch (Exception e){
            throw new RuntimeException("查询失败"+e.getMessage(),e);
        }finally {
            try {
                if(rs != null)rs.close();
                if(stmt != null)stmt.close();
                if(conn != null)conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询单个整数值
     * 例如：SELECT COUNT(*) FROM booking
     */
    public static int queryInt(String sql){
        String result = queryString(sql);
        return result == null ? 0 : Integer.parseInt(result);
    }

    /**
     * 测试数据库连接
     */
    public static void main(String[] args) {
        try {
            int count = queryInt("SELECT COUNT(*) FROM booking");
            System.out.println(" 数据库连接成功！共有 " + count+" 条数据");
        }catch (Exception e){
            System.err.println("数据库连接失败！"+e.getMessage());
        }
    }

    /**
     * 执行 INSERT / UPDATE / DELETE 语句
     * @param sql SQL语句
     * @return 影响的行数
     */
    public static int executeSql(String sql) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            int rows = stmt.executeUpdate(sql);
            System.out.println("✅ 执行SQL成功，影响 " + rows + " 行");
            return rows;
        } catch (Exception e) {
            throw new RuntimeException("执行SQL失败：" + e.getMessage(), e);
        } finally {
            close(null, stmt, conn);
        }
    }

    // ============== 连接管理 ==============

    private static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    private static void close(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
