import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://localhost:3306/first_lesson";
        String userName = "root";
        String pass = "1111";

        Class.forName("com.mysql.jdbc.Driver");

        try(Connection connection = DriverManager.getConnection(url,userName,pass))
        {
            System.out.println("Connection successful");
        }
    }
}
