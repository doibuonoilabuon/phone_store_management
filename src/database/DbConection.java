package database;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class DbConection {

    public static Connection getConnection() {
        Connection result = null;
        try {
            // Đăng ký SQL Server Driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // ĐÃ CẬP NHẬT: Thêm sendStringParametersAsUnicode và characterEncoding
            String url = "jdbc:sqlserver://WHISPERSTONE\\SQLEXPRESS01:1433;" 
                       + "databaseName=QuanLyDienThoai;" 
                       + "user=sa;" 
                       + "password=123456789;" 
                       + "encrypt=true;" 
                       + "trustServerCertificate=true;"
                       + "sendStringParametersAsUnicode=true;" 
                       + "characterEncoding=UTF-8;";           
            // Tạo kết nối
            result = DriverManager.getConnection(url);
         

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Không thể kết nối đến SQL Server!\n" + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return result;
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null && !c.isClosed()) {
                c.close();
              
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}