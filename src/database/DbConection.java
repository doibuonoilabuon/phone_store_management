package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConection {
    public static void main(String[] args) {

        String url = "jdbc:sqlserver://WHISPERSTONE\\SQLEXPRESS01:1433;"
                   + "databaseName=master;"
                   + "user=sa;"
                   + "password=123456789;"
                   + "encrypt=true;"
                   + "trustServerCertificate=true;";

        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Kết nối thành công!");
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }
}