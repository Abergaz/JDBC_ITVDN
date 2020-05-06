import java.sql.*;

public class MultipleResults {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://localhost:3306/first_lesson";
        String userName = "root";
        String pass = "1111";

        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(url, userName, pass)) {
            CallableStatement callableStatement = null;
            try {
                callableStatement = connection.prepareCall("{CALL tablesCount}");
                boolean hasResults = callableStatement.execute();
                ResultSet resultSet = null;
                try {
                    while (hasResults) {
                        resultSet = callableStatement.getResultSet();
                        while (resultSet.next()) {
                            System.out.println("Количество записей в таблице: " + resultSet.getInt(1));
                        }
                        hasResults = callableStatement.getMoreResults();
                    }
                } catch (SQLException sqlException) {
                    System.err.println("SQLException message: " + sqlException.getMessage());
                    System.err.println("SQLException SQL state" + sqlException.getSQLState());
                    System.err.println("SQLException error code" + sqlException.getErrorCode());
                } finally {
                    if (resultSet != null) {
                        resultSet.close();
                    } else {
                        System.err.println("Ошибка чтения данных с БД ");
                    }
                }
            } catch (SQLException sqlException) {
                System.err.println("SQLException message: " + sqlException.getMessage());
                System.err.println("SQLException SQL state" + sqlException.getSQLState());
                System.err.println("SQLException error code" + sqlException.getErrorCode());
            }finally {
                callableStatement.close();
            }
        }
    }
}
