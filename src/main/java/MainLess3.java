import java.sql.*;

public class MainLess3 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://localhost:3306/first_lesson";
        String userName = "root";
        String pass = "1111";

        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(url, userName, pass)) {
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.prepareStatement("INSERT INTO books (name,price) VALUES (?,?)");
                preparedStatement.setString(1, "Schilder's list");
                preparedStatement.setDouble(2, 32.5);
                preparedStatement.execute();
                ResultSet resultSet = null;
                try {
                    resultSet = preparedStatement.executeQuery("SELECT * FROM  books");
                    while (resultSet.next()) {
                        int bookId = resultSet.getInt("bookId");
                        String name = resultSet.getString("name");
                        double price = resultSet.getDouble("price");
                        System.out.println("bookId="+bookId+" name="+name+" price="+price);
                    }
                }catch (SQLException sqlException){
                    System.err.println("SQLException message: "+sqlException.getMessage());
                    System.err.println("SQLException SQL state"+sqlException.getSQLState());
                    System.err.println("SQLException error code"+sqlException.getErrorCode());
                }finally {
                    if (resultSet!=null){
                        resultSet.close();
                    }else {
                        System.err.println("Ошибка чтения данных с БД ");
                    }
                }
            }catch (SQLException sqlException){
                System.err.println("SQLException message: "+sqlException.getMessage());
                System.err.println("SQLException SQL state"+sqlException.getSQLState());
                System.err.println("SQLException error code"+sqlException.getErrorCode());
            }finally {
                preparedStatement.close();
            }

            CallableStatement callableStatement = null;
            try{
                callableStatement =connection.prepareCall("{CALL booksCount(?)}");
                callableStatement.registerOutParameter(1,Types.INTEGER);
                callableStatement.execute();
                System.out.println("Колисчество заисей в таблице: "+callableStatement.getInt(1));
            }catch (SQLException sqlException){
                System.err.println("SQLException message: "+sqlException.getMessage());
                System.err.println("SQLException SQL state"+sqlException.getSQLState());
                System.err.println("SQLException error code"+sqlException.getErrorCode());
            }finally {
                callableStatement.close();
            }
        }
    }
}
