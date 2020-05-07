import java.sql.*;

public class Lesson6_1 {
    static String url = "jdbc:mysql://localhost:3306/first_lesson";
    static String userName = "root";
    static String pass = "1111";

    public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(url, userName, pass);
             Statement statement = connection.createStatement()
        ) {
            //Для работы с транзакциями выключаем autoCommit
            //Connection.TRANSACTION_READ_COMMITTED - уровень по умолчанию
            connection.setAutoCommit(false);
            //устанавливаем уровень изоляции ранзакции
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            /**
             * Эмитируем грязное чтение dirty read (читает не закомиченные данные)
             * это когда почитанных данных уже может не быть в таблице
             * например была начна транзакция котороя откатилась
             */
            statement.executeUpdate("UPDATE books set price=100 where bookId=1");
            //вызываем второй поток для четия данных
            new OtherTransaction().start();
            Thread.sleep(2000);
            //делаем откат изменения транзакции
            connection.rollback();
        }
    }

    static class OtherTransaction extends Thread {
        @Override
        public void run() {
            try (Connection connection = DriverManager.getConnection(url, userName, pass);
                 Statement statement = connection.createStatement()
            ) {
                //Для работы с транзакциями выключаем autoCommit
                connection.setAutoCommit(false);
                //устанавливаем уровень изоляции ранзакции
                //Connection.TRANSACTION_READ_COMMITTED - уровень по умолчанию
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                //читаем данные
                ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("name") + " " + resultSet.getString("price"));
                }

            } catch (Exception exception) {
                System.err.println(exception.getMessage());
            }
        }
    }
}
