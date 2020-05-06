import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;

public class BlobExample {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://localhost:3306/first_lesson";
        String userName = "root";
        String pass = "1111";

        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(url, userName, pass);
             Statement statement = connection.createStatement()
        ) {

            statement.executeUpdate("CREATE TABLE Images (name VARCHAR(15),d DATE, img BLOB)");
            PreparedStatement preparedStatement = null;

            try {
                Blob blob = connection.createBlob();
                BufferedImage bufferedImage =null;
                try {
                    bufferedImage = ImageIO.read(new File("src\\main\\resources\\smile.jpg"));
                    try(OutputStream outputStream = blob.setBinaryStream(1)) {
                        ImageIO.write(bufferedImage, "jpg", outputStream);
                    }
                } catch (IOException e) {
                    System.err.println("IOException message: " + e.getMessage());
                }

                preparedStatement = connection.prepareStatement("INSERT INTO Images (name,d,img) VALUES (?,{d ?},?)");
                preparedStatement.setString(1, "Smile");
                preparedStatement.setDate(2, Date.valueOf("2020-05-06"));
                preparedStatement.setBlob(3, blob);
                preparedStatement.execute();

                ResultSet resultSet = null;
                try {
                    resultSet = preparedStatement.executeQuery("SELECT * FROM Images");
                    while (resultSet.next()) {
                        Blob newBlob = resultSet.getBlob("img");
                        try {
                            BufferedImage newBufferedImage = ImageIO.read(newBlob.getBinaryStream());
                            File outputFile = new File("saved.jpg");
                            ImageIO.write(newBufferedImage, "jpg", outputFile);
                        } catch (IOException e) {
                            System.err.println("IOException message: " + e.getMessage());
                        }
                    }
                } catch (SQLException sqlException) {
                    System.err.println("SQLException message: " + sqlException.getMessage());
                    System.err.println("SQLException SQL state" + sqlException.getSQLState());
                    System.err.println("SQLException error code" + sqlException.getErrorCode());
                } finally {
                    if (resultSet!=null){
                        resultSet.close();
                    }else {
                        System.err.println("Ошибка чтения данных с БД ");
                    }
                }
            } catch (SQLException sqlException) {
                System.err.println("SQLException message: " + sqlException.getMessage());
                System.err.println("SQLException SQL state" + sqlException.getSQLState());
                System.err.println("SQLException error code" + sqlException.getErrorCode());
            } finally {
                preparedStatement.close();
            }
        } catch (SQLException sqlException) {
            System.err.println("SQLException message: " + sqlException.getMessage());
            System.err.println("SQLException SQL state" + sqlException.getSQLState());
            System.err.println("SQLException error code" + sqlException.getErrorCode());
        }
    }
}
