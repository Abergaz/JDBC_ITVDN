import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;

public class RowSetLesson4 {
    static String url = "jdbc:mysql://localhost:3306/first_lesson";
    static String userName = "root";
    static String pass = "1111";


    public static void main(String[] args) throws SQLException, ClassNotFoundException, MalformedURLException {
        ResultSet resultSet = getResultSet();
        while (resultSet.next()) {
            System.out.println(resultSet.getString("name"));
        }
        //инициализируем набор строк через результирующий набор получнный выше, т.к. мы возаращали именно набор строк
        CachedRowSet cachedRowSet = (CachedRowSet) resultSet;
        //Выполняем запрос к БД через набор строк RowSet c параметром как через PrepareStatement
        cachedRowSet.setCommand("SELECT * FROM books WHERE price > ?");
        //отбираем из таблицы БД все книги с ценой больше 30
        cachedRowSet.setDouble(1, 30);
        //необходимо явно задать параметры подключения
        cachedRowSet.setUrl(url);
        cachedRowSet.setUsername(userName);
        cachedRowSet.setPassword(pass);
        //запускаем на выполнение
        cachedRowSet.execute();
        while (cachedRowSet.next()) {
            String name = cachedRowSet.getString("name");
            double price = cachedRowSet.getDouble("price");
            System.out.println(name + " " + price);
        }
        //!!! Переместились на 2 строку набора строк RowSet!!! а не таблицы!!!
        cachedRowSet.absolute(2);
        //Удаляем 2 строку из набора строк!!!
        cachedRowSet.deleteRow();
        //Перемещаемся на предыдущую строку от текущей
        cachedRowSet.beforeFirst();
        //Создаем подключение к БД для того чтобы передать удаление из набора сткрок RowSet в таблицу БД
        Connection connection = DriverManager.getConnection(url,userName,pass);
        connection.setAutoCommit(false);
        //Применяем изменения
        cachedRowSet.acceptChanges(connection);
    }

    static ResultSet getResultSet() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(url, userName, pass);
             Statement statement = connection.createStatement();
        ) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
            //Для того чтобы можно была не держать соединение открытым используем набор строк
            //Получем фабрику для набора строк RowSetFactory из RowSetProvider
            RowSetFactory rowSetFactory = RowSetProvider.newFactory();
            //Создаем кешированные набор строк
            CachedRowSet cachedRowSet = rowSetFactory.createCachedRowSet();
            //передаем реуузьтрующий набо ResultSet в набор строк RowSet
            cachedRowSet.populate(resultSet);
            //Возвращаем набор строк, т.к. RowSet расщиряет ResultSet
            return cachedRowSet;
        }
    }

}
