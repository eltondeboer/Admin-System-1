import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class DataBase_tbles {

    public DefaultTableModel view_loans_table(User user) {

        final String DB_URL = "jdbc:mysql://localhost:3306/library";
        final String USERNAME = "java";
        final String PASSWORD = "Javaex12";

        DefaultTableModel model = null;
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT i.ISBN, i.Title, i.Type, i.Director_Author, i.Publisher FROM Loan l Join item_copy ic on l.Barcode = ic.Barcode Join item i on ic.ISBN = i.ISBN Where userID=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.user_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            model = new DefaultTableModel();

            model.addColumn("ISBN");
            model.addColumn("Title");
            model.addColumn("Type");
            model.addColumn("Director/Author");
            model.addColumn("Publisher");

            while (resultSet.next()) {
                String ISBN = resultSet.getString("ISBN");
                String title = resultSet.getString("Title");
                String type = resultSet.getString("Type");
                String dir_auth = resultSet.getString("Director_Author");
                String publisher = resultSet.getString("Publisher");
                model.addRow(new Object[]{ISBN, title, type, dir_auth, publisher});
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }
}
