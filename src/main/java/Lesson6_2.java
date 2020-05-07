import java.sql.*;

public class Lesson6_2 {
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
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            /**
             * Эмитируем неповторяющееся чтение non-repeatable read
             * Транзакция может получать разные значения между чтениями
             * если установить Connection.TRANSACTION_REPEATABLE_READ то результат чтений будет одинаковый, хотя данные изменились
             */
           ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("name") + " " + resultSet.getString("price"));
            }
            //вызываем второй поток для изменения данных
            new Lesson6_2.OtherTransaction().start();
            Thread.sleep(2000);
            //делаем снова выполняем выборку
            ResultSet resultSet2 = statement.executeQuery("SELECT * FROM books");
            while (resultSet2.next()) {
                System.out.println(resultSet2.getString("name") + " " + resultSet2.getString("price"));
            }
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
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED );
                //меняем данные
                statement.executeUpdate("UPDATE books set price = price+20 where bookId=5");
                connection.commit();

            } catch (Exception exception) {
                System.err.println(exception.getMessage());
            }
        }
    }
}
