import java.sql.*;
import java.util.ArrayList;

public class Database {
    private String url = "jdbc:mysql://localhost:3306/library";
    private String user = "root";
    private String password = "nipuni";

    public void updateData(String sql) {
        try {
            Connection connection = DriverManager.getConnection(url,user,password);
            Statement statement = connection.createStatement();

            statement.executeUpdate(sql);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getData(String sql, String columnName) {
        ArrayList<String> array = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(url,user,password);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                array.add(result.getString(columnName));
            }

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return array;
    }

}
