import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {

    public static Connection getConnection() {

        Connection conn = null;

        try {

            String url = "jdbc:sqlserver://localhost;instanceName=SQLEXPRESS;databaseName=PhoneStore;encrypt=true;trustServerCertificate=true";
            String user = "sa";
            String password = "123456789"; // password SQL Server của bạn

            conn = DriverManager.getConnection(url, user, password);

            System.out.println("Ket noi database PhoneStore thanh cong!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}