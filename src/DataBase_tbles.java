import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class DataBase_tbles {

    public DefaultTableModel get_items_test() {

        final String DB_URL = "jdbc:mysql://localhost:3306/Library";
        final String USERNAME = "java";
        final String PASSWORD = "Javaex12";

        DefaultTableModel model = null;
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM Item";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            model = new DefaultTableModel();

            model.addColumn("Item ID");
            model.addColumn("Title");
            model.addColumn("Type");
            model.addColumn("Director/Author");

            while (resultSet.next()) {
                int id = resultSet.getInt("itemID");
                String title = resultSet.getString("Title");
                String type = resultSet.getString("Type");
                String dir_auth = resultSet.getString("Director_Author");
                model.addRow(new Object[]{id, title, type, dir_auth});
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }
}
