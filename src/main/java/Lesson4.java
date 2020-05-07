import java.sql.*;

public class Lesson4 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://localhost:3306/first_lesson";
        String userName = "root";
        String pass = "1111";

        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(url, userName, pass);
             //устанавливаем парметры прокручиваемого и обновляемого набора
             //при работе с набором ResultSet  всегда открыто подклчение к БД
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ) {
            ResultSet resultSet = null;
            try {
                resultSet = statement.executeQuery("SELECT * FROM books");
                while (resultSet.next()){
                    int id = resultSet.getInt("bookId");
                    double price = resultSet.getDouble("price");
                    if (id==4){
                        resultSet.updateString("name","Spartacus (discount)");
                        resultSet.updateDouble("price",10.5);
                        resultSet.updateRow();//чтобы изменения применились
                    }
                }
                //встаем на второю строку если она есть
                if (resultSet.absolute(2)) System.out.println(resultSet.getString("name"));
                //возвращаемся на строку назад
                if (resultSet.previous()) System.out.println(resultSet.getString("name"));
                //переход на последнию стоку
                if (resultSet.last()) System.out.println(resultSet.getString("name"));
                //перехлдим на - 3 строки от текущей
                if (resultSet.relative(-3)) System.out.println(resultSet.getString("name"));
                // будем выводить имя поля и его значение, через метаданные
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                while (resultSet.next()){
                    //цикл для обхода полей таблицы
                    for (int i = 1; i <=resultSetMetaData.getColumnCount() ; i++) {
                        String fieldName = resultSetMetaData.getColumnName(i);
                        String value = resultSet.getString(fieldName);
                        System.out.print(fieldName+"="+value+" ");
                    }
                    System.out.println();
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
        }
    }
}