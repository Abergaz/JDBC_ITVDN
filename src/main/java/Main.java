import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        String url =   "jdbc:mysql://localhost:3306/first_lesson";
        String userName = "root";
        String pass = "1111";

        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(url, userName, pass);
             BufferedReader bufferedReader = new BufferedReader(new FileReader("src\\main\\java\\book.sql"));
             Scanner scanner = new Scanner(bufferedReader);
             Statement statement = connection.createStatement();
        ){
            String line = "";
            StringBuilder stringBuilder = new StringBuilder("");
            while (scanner.hasNextLine()){
                line = scanner.nextLine();
                if (!line.endsWith(";")){
                    stringBuilder.append(line);
                }else{
                    line=line.substring(0,line.length()-1);
                    stringBuilder.append(line);
                    line=stringBuilder.toString();
                    statement.executeUpdate(line);
                    stringBuilder=new StringBuilder("");
                }
            }
            ResultSet resultSet = null;
            try {
                resultSet=statement.executeQuery("SELECT * FROM Books");
                while (resultSet.next()){
                    int bookId = resultSet.getInt("bookId");
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    System.out.println("bookI="+bookId+" name="+name+" price="+price);
                }
            }catch (SQLException e){
                System.err.println("SQLException message: "+e.getMessage());
                System.err.println("SQLException SQL state"+e.getSQLState());
                System.err.println("SQLException error code"+e.getErrorCode());
            }finally {
                if (resultSet!=null){
                    resultSet.close();
                }else {
                    System.err.println("Ошибка чтения данных с БД ");
                }
            }
        }
    }
}
