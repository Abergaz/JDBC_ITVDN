import java.sql.*;

public class Lesson5 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://localhost:3306/first_lesson";
        String userName = "root";
        String pass = "1111";

        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(url, userName, pass);
             Statement statement = connection.createStatement()
        ) {
            String createTable = "CREATE TABLE Fruit (name VARCHAR(15) NOT NULL, amount INTEGER, price DOUBLE NOT NULL, PRIMARY KEY (name))";
            String command1 = "INSERT INTO Fruit (name, amount, price) VALUES ('Apple',200,3.5)";
            String command2 = "INSERT INTO Fruit (name, amount, price) VALUES ('Orange',50,5.5)";
            String command3 = "INSERT INTO Fruit (name, amount, price) VALUES ('Lemon',30,5.5)";
            String command4 = "INSERT INTO Fruit (name, amount, price) VALUES ('Pineapple',20,7.5)";
            // обязательно отключить авто коммит для исп транзакуций, точек сокранения и потоков комманд
            connection.setAutoCommit(false);
            statement.executeUpdate(createTable);
            statement.executeUpdate(command1);
            //создаем точку сохранения Savepoint после выполнения первой команды
            Savepoint savepoint = connection.setSavepoint();
            statement.executeUpdate(command2);
            statement.executeUpdate(command3);
            statement.executeUpdate(command4);
            //фиксируем выполнение
             connection.commit();
            /**
             * до коммита можно вызвать rollback и тогде все изменения отменяться
             * connection.rollback();
             * можно откатиться к определеннной точке сохранения Savepoint
             * connection.rollback(savePoint);
             * произвести commit
             * connection.commit();
             * и освободить точку сохранения
             **/
            connection.releaseSavepoint(savepoint);

            /**
             * Маханизм групповых обновлений
             */
            //выстааляем авто коммит
            connection.setAutoCommit(true);
            String command5 = "INSERT INTO Fruit (name, amount, price) VALUES ('Pears',200,4.5)";
            String command6 = "INSERT INTO Fruit (name, amount, price) VALUES ('Mandarin',50,5.5)";
            String command7 = "INSERT INTO Fruit (name, amount, price) VALUES ('Grapefruit',30,5.5)";
            String command8 = "INSERT INTO Fruit (name, amount, price) VALUES ('Pomelo',20,7.5)";

            //собираем команды в группу
            statement.addBatch(command5);
            statement.addBatch(command6);
            statement.addBatch(command7);
            statement.addBatch(command8);
            //выполняем все команды группы
            statement.executeBatch();

        }

    }
}
