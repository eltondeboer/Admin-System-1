import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class DataBase_conn {

    public DefaultTableModel view_loans_table(User user) {

        final String DB_URL = "jdbc:mysql://localhost:3306/library";
        final String USERNAME = "java";
        final String PASSWORD = "Javaex12";

        NonEditableTableModel model = new NonEditableTableModel(new Object[]{"Title", "Type", "Director/Author", "Publisher", "ReturnDate", "ISBN"}, 0);
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT i.ISBN, i.Title, i.Type, i.Director_Author, i.Publisher, l.ReturnDate FROM Loan l Join item_copy ic on l.Barcode = ic.Barcode Join item i on ic.ISBN = i.ISBN Where userID=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.user_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                String type = resultSet.getString("Type");
                String dir_auth = resultSet.getString("Director_Author");
                String publisher = resultSet.getString("Publisher");
                String returndate = resultSet.getString("ReturnDate");
                String ISBN = resultSet.getString("ISBN");
                model.addRow(new Object[]{title, type, dir_auth, publisher, returndate, ISBN});
            }


            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }
    public DefaultTableModel view_late_userReturns_table(User user) {

        final String DB_URL = "jdbc:mysql://localhost:3306/library";
        final String USERNAME = "java";
        final String PASSWORD = "Javaex12";

        NonEditableTableModel model = new NonEditableTableModel(new Object[]{"Title", "Type", "Director/Author", "Publisher", "ReturnDate", "ISBN"}, 0);
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT i.ISBN, i.Title, i.Type, i.Director_Author, i.Publisher, l.ReturnDate FROM Loan l Join item_copy ic on l.Barcode = ic.Barcode Join item i on ic.ISBN = i.ISBN Where userID=? and curdate() > l.ReturnDate";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.user_id);

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                String type = resultSet.getString("Type");
                String dir_auth = resultSet.getString("Director_Author");
                String publisher = resultSet.getString("Publisher");
                String returndate = resultSet.getString("ReturnDate");
                String ISBN = resultSet.getString("ISBN");
                model.addRow(new Object[]{title, type, dir_auth, publisher, returndate, ISBN});
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }
    public DefaultTableModel search_results(String search_text, JLabel txt_label) {
        txt_label.setText("Search above for title, type or clasification");

        final String DB_URL = "jdbc:mysql://localhost:3306/library";
        final String USERNAME = "java";
        final String PASSWORD = "Javaex12";

        NonEditableTableModel model = new NonEditableTableModel(new Object[]{"Title", "Type", "Director/Author", "Classification", "Publisher", "ISBN"}, 0);
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "call library.sp_item_search(?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, search_text);

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                String type = resultSet.getString("Type");
                String dir_auth = resultSet.getString("Director_Author");
                String classification = resultSet.getString("Classification");
                String publisher = resultSet.getString("Publisher");
                String ISBN = resultSet.getString("ISBN");
                model.addRow(new Object[]{title, type, dir_auth, classification, publisher, ISBN});
            }
            System.out.println("Number of rows in DefaultTableModel: " + model.getRowCount());

            if (!resultSet.isBeforeFirst()) {
                txt_label.setText("No results found for: " + search_text);
            } else {
                txt_label.setText("Search results for: " + search_text);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }
}
