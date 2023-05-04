import com.sun.net.httpserver.Authenticator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class
DataBase_conn {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private static final String USERNAME = "java";
    private static final String PASSWORD = "Javaex12";

    public DefaultTableModel view_loans_table(User user) {

        NonEditableTableModel model = new NonEditableTableModel(new Object[]{"Title", "Type", "Director/Author", "Publisher/Country", "ReturnDate", "ISBN"}, 0);
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT i.ISBN, i.Title, i.Type, i.Director_Author, i.Publisher_Country, l.ReturnDate FROM Loan l Join item_copy ic on l.Barcode = ic.Barcode Join item i on ic.ItemID = i.ItemID Where userID=? and Returned = 0";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.getUserId());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                String type = resultSet.getString("Type");
                String dir_auth = resultSet.getString("Director_Author");
                String publisher = resultSet.getString("Publisher_Country");
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

        NonEditableTableModel model = new NonEditableTableModel(new Object[]{"Title", "Type", "Director/Author", "Publisher/Country", "ReturnDate", "ISBN"}, 0);
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT i.ISBN, i.Title, i.Type, i.Director_Author, i.Publisher_Country, l.ReturnDate FROM Loan l Join item_copy ic on l.Barcode = ic.Barcode Join item i on ic.ItemID = i.ItemID Where userID=? and curdate() > l.ReturnDate and l.Returned = 0";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.getUserId());

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                String type = resultSet.getString("Type");
                String dir_auth = resultSet.getString("Director_Author");
                String publisher = resultSet.getString("Publisher_Country");
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

    //Uses the stored procedure in the database to search and returns a table model with the results.
    public DefaultTableModel search_results(String search_text, JLabel txt_label) {
        txt_label.setText("Search above for title, type or clasification");

        NonEditableTableModel model = new NonEditableTableModel(new Object[]{"Title", "Type", "Director/Author", "Classification", "Publisher/Country", "ISBN", "Age Restriction", "Available Copies"}, 0);
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
                String publisher = resultSet.getString("Publisher_Country");
                String ISBN = resultSet.getString("ISBN");
                String Age_restriction = resultSet.getString("Age_Restriction");
                String Available = resultSet.getString("Availability");
                model.addRow(new Object[]{title, type, dir_auth, classification, publisher, ISBN, Age_restriction, Available});
            }

            if (resultSet == null) {
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
    //Connects to database and adds user to the database.
    public void add_user( JTextField name_tf,
                          JTextField phone_tf,
                          JTextField email_tf,
                          JPasswordField pass_pf){

        String name = name_tf.getText();
        String phone = phone_tf.getText();
        String email = email_tf.getText();
        String password = String.valueOf(pass_pf.getPassword());

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO `library`.`user` (`userName`, `userType`, `userMail`, `userPhoneNumber`, `userPass`) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, "Student");
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, password);

            int rows_effected = preparedStatement.executeUpdate();
            if (rows_effected == 0){
                System.out.println("Failed to add new User");
            }
            else{
                System.out.println("User added succesfully");
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public boolean return_loan(JTextField return_BC_tf, User user){

        String barcode = return_BC_tf.getText();

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "UPDATE loan SET Returned = 1 WHERE (Barcode = ?) and userID = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, barcode);
            preparedStatement.setString(2, user.getUserId());

            int rows_effected = preparedStatement.executeUpdate();

            stmt.close();
            conn.close();

            if (rows_effected == 0){
                return false;
            }
            else{
                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public String loan_book (JTextField loan_BC_tf, User user){

        String barcode = loan_BC_tf.getText();

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO loan (userID, Barcode, BorrowDate, Returned) VALUES (?, ?, CURDATE(), '0');";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, barcode);

            preparedStatement.executeUpdate();
            return "Success";

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Error: The book you are trying to loan is a reference copy and cannot be loaned.")) {
                return "Refrence";
            } else if (ex.getMessage().contains("Error: The book you are trying to loan is not available.")) {
                return "Copies";
            } else if (ex.getMessage().contains("Researcher can loan up to 20 books only")) {
                return "Researcher";
            } else if (ex.getMessage().contains("Teacher can loan up to 10 books only")) {
                return "Teacher";
            } else if (ex.getMessage().contains("Student can loan up to 5 books only")) {
                return "Student";
            } else {
                return "Barcode";
            }
        }
    }
    public void get_recipt (String barcode,
                              JLabel title,
                              JLabel isbn,
                              JLabel loan_date,
                              JLabel return_date){

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT i.Title, i.ItemID, l.BorrowDate, l.ReturnDate FROM loan l Join item_copy ic ON ic.Barcode = l.Barcode Join item i ON ic.ItemID = i.ItemID Where l.Barcode = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, barcode);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String title_recived = resultSet.getString("Title");
                String ItemID_recived = resultSet.getString("ItemID");
                String Date_of_loan = resultSet.getString("BorrowDate");
                String Date_of_return = resultSet.getString("ReturnDate");
                title.setText("Title: " + title_recived);
                isbn.setText("ItemID: " + ItemID_recived);
                loan_date.setText("Date of Loan: " + Date_of_loan);
                return_date.setText("Return Date: " + Date_of_return);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public DefaultTableModel search_results_item_manage(String search_text) {

        DefaultTableModel model = new DefaultTableModel(new Object[]{"ItemID" ,"ISBN", "Title", "Type", "Location", "Availability", "Max_loan_weeks", "Director/Author", "Classification", "Publisher/Country"}, 0);
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "call library.sp_item_search(?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, search_text);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String ItemID = resultSet.getString("ItemID");
                String ISBN = resultSet.getString("ISBN");
                String title = resultSet.getString("Title");
                String type = resultSet.getString("Type");
                String location = resultSet.getString("Location");
                int Availability = resultSet.getInt("Availability");
                int Max_loan_weeks = resultSet.getInt("MaxLoan_Weeks");
                String dir_auth = resultSet.getString("Director_Author");
                String classification = resultSet.getString("Classification");
                String publisher = resultSet.getString("Publisher_Country");
                model.addRow(new Object[]{ItemID ,ISBN, title, type, location, Availability, Max_loan_weeks, dir_auth, classification, publisher});
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Makes the first column uneditable
        DefaultTableModel uneditableModel = new DefaultTableModel() {
            @Override
            public int getColumnCount() {
                return model.getColumnCount();
            }

            @Override
            public int getRowCount() {
                return model.getRowCount();
            }

            @Override
            public Object getValueAt(int row, int column) {
                if (column == 0) {
                    return model.getValueAt(row, column);
                } else {
                    return model.getValueAt(row, column);
                }
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (column == 0) {
                    model.setValueAt(aValue, row, column);
                } else {
                    model.setValueAt(aValue, row, column);
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }

            @Override
            public String getColumnName(int column) {
                return model.getColumnName(column);
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return model.getColumnClass(column);
            }
        };

        return uneditableModel;
    }
    public DefaultTableModel search_results_copy_manage(String search_text) {

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Barcode", "IsReferenceCopy", "ItemID"}, 0);
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "call library.sp_item_copy_search(?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, search_text);

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                String Barcode = resultSet.getString("Barcode");
                String IsReferenceCopy = resultSet.getString("IsReferenceCopy");
                String ItemID = resultSet.getString("ItemID");
                model.addRow(new Object[]{Barcode, IsReferenceCopy, ItemID});
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Makes the first column uneditable
        DefaultTableModel uneditableModel = new DefaultTableModel() {
            @Override
            public int getColumnCount() {
                return model.getColumnCount();
            }

            @Override
            public int getRowCount() {
                return model.getRowCount();
            }

            @Override
            public Object getValueAt(int row, int column) {
                if (column == 0) {
                    return model.getValueAt(row, column);
                } else {
                    return model.getValueAt(row, column);
                }
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (column == 0) {
                    model.setValueAt(aValue, row, column);
                } else {
                    model.setValueAt(aValue, row, column);
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }

            @Override
            public String getColumnName(int column) {
                return model.getColumnName(column);
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return model.getColumnClass(column);
            }
        };

        return uneditableModel;
    }

    public DefaultTableModel search_results_user_manage(String search_text) {

        DefaultTableModel model = new DefaultTableModel(new Object[]{"UserID", "UserName", "UserType", "UserMail", "UserPhone"}, 0);
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "call library.sp_user_search(?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, search_text);

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                String UserID = resultSet.getString("userID");
                String UserName = resultSet.getString("userName");
                String UserType = resultSet.getString("userType");
                String UserMail = resultSet.getString("userMail");
                String UserPhone = resultSet.getString("userPhoneNumber");
                model.addRow(new Object[]{UserID, UserName, UserType, UserMail, UserPhone});
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Makes the first column uneditable
        DefaultTableModel uneditableModel = new DefaultTableModel() {
            @Override
            public int getColumnCount() {
                return model.getColumnCount();
            }

            @Override
            public int getRowCount() {
                return model.getRowCount();
            }

            @Override
            public Object getValueAt(int row, int column) {
                if (column == 0) {
                    return model.getValueAt(row, column);
                } else {
                    return model.getValueAt(row, column);
                }
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (column == 0) {
                    model.setValueAt(aValue, row, column);
                } else {
                    model.setValueAt(aValue, row, column);
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }

            @Override
            public String getColumnName(int column) {
                return model.getColumnName(column);
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return model.getColumnClass(column);
            }
        };

        return uneditableModel;
    }
    public static void push_edits_item(JTable model) {

        for (int i = 0; i < model.getRowCount(); i++){
            String ItemID = (String) model.getValueAt(i, 0);
            String ISBN = (String) model.getValueAt(i, 1);
            String Title = (String) model.getValueAt(i, 2);
            String Type = (String) model.getValueAt(i, 3);
            String Location = (String) model.getValueAt(i, 4);
            int Availability =(Integer) model.getValueAt(i, 5);
            int Max_loan_weeks = (Integer) model.getValueAt(i, 6);
            String Director_Author = (String) model.getValueAt(i, 7);
            String Classification = (String) model.getValueAt(i, 8);
            String Publisher_Country = (String) model.getValueAt(i, 9);

            String updateQuery = "UPDATE item SET ISBN=?, Title=?, Type=?, Location=?, Availability=?, MaxLoan_Weeks=?, Classification=?, Director_Author=?, Publisher_Country=? WHERE ItemID=?";
            try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, ISBN);
                stmt.setString(2, Title);
                stmt.setString(3, Type);
                stmt.setString(4, Location);
                stmt.setInt(5, Availability);
                stmt.setInt(6, Max_loan_weeks);
                stmt.setString(7, Classification);
                stmt.setString(8, Director_Author);
                stmt.setString(9, Publisher_Country);
                stmt.setString(10, ItemID);
                stmt.executeUpdate();


                stmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void push_edits_item_copy(JTable model) {

        for (int i = 0; i < model.getRowCount(); i++){
            String Barcode = (String) model.getValueAt(i, 0);
            String IsReferenceCopy = (String) model.getValueAt(i, 1);
            String ItemID = (String) model.getValueAt(i, 2);

            String updateQuery = "UPDATE item_copy SET Barcode = ?, ItemID = ?, IsReferenceCopy = ? WHERE Barcode = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, Barcode);
                stmt.setString(2, ItemID);
                stmt.setString(3, IsReferenceCopy);
                stmt.setString(4, Barcode);
                stmt.executeUpdate();


                stmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static void push_edits_user(JTable model) {

        for (int i = 0; i < model.getRowCount(); i++){
            String userID = (String) model.getValueAt(i, 0);
            String userName = (String) model.getValueAt(i, 1);
            String userType = (String) model.getValueAt(i, 2);
            String userMail = (String) model.getValueAt(i, 3);
            String userPhoneNumber = (String) model.getValueAt(i, 4);

            String updateQuery = "UPDATE user SET userName=?, userType=?, userMail=?, userPhoneNumber=? WHERE userID=?";
            try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, userName);
                stmt.setString(2, userType);
                stmt.setString(3, userMail);
                stmt.setString(4, userPhoneNumber);
                stmt.setString(5, userID);
                stmt.executeUpdate();


                stmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public DefaultTableModel search_results_reservation(String search_text) {

        NonEditableTableModel model = new NonEditableTableModel(new Object[]{"ItemID", "Title", "Type", "Director/Author", "Classification", "Publisher/Country", "ISBN", "Age Restriction", "Available Copies"}, 0);
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "call library.sp_item_search(?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, search_text);

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                int ItemID = resultSet.getInt("ItemID");
                String title = resultSet.getString("Title");
                String type = resultSet.getString("Type");
                String dir_auth = resultSet.getString("Director_Author");
                String classification = resultSet.getString("Classification");
                String publisher = resultSet.getString("Publisher_Country");
                String ISBN = resultSet.getString("ISBN");
                String Age_restriction = resultSet.getString("Age_Restriction");
                String Available = resultSet.getString("Availability");
                model.addRow(new Object[]{ItemID, title, type, dir_auth, classification, publisher, ISBN, Age_restriction, Available});
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }
    public void add_reservation(int ItemID, User user ){


        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO `library`.`reservation` (`ItemID`, `UserID`, `DateReserved`, `IsActive`) VALUES (?, ?, curdate(), '1');";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, ItemID);
            preparedStatement.setString(2, user.getUserId());

            int rows_effected = preparedStatement.executeUpdate();
            if (rows_effected == 0){
                System.out.println("Failed to add new Reserveation");
            }
            else{
                System.out.println("Reserveation added succesfully");
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void delete_item(int ItemID) {

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM `library`.`item_copy` WHERE (`ItemID` = ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, ItemID);

            preparedStatement.executeUpdate();

            Statement stmt2 = conn.createStatement();
            String sql2 = "DELETE FROM `library`.`item` WHERE (`ItemID` = ?);";
            PreparedStatement preparedStatement2 = conn.prepareStatement(sql2);
            preparedStatement2.setInt(1, ItemID);

            preparedStatement2.executeUpdate();

            stmt.close();
            stmt2.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void delete_item_copy(String Barcode) {

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM `library`.`item_copy` WHERE (`Barcode` = ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, Barcode);

            preparedStatement.executeUpdate();

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void delete_user(int UserID) {

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM `library`.`reservation` WHERE (`UserID` = ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, UserID);

            preparedStatement.executeUpdate();

            Statement stmt2 = conn.createStatement();
            String sql2 = "DELETE FROM `library`.`loan` WHERE (`UserID` = ?);";
            PreparedStatement preparedStatement2 = conn.prepareStatement(sql2);
            preparedStatement2.setInt(1, UserID);

            preparedStatement2.executeUpdate();

            Statement stmt3 = conn.createStatement();
            String sql3 = "DELETE FROM `library`.`user` WHERE (`UserID` = ?);";
            PreparedStatement preparedStatement3 = conn.prepareStatement(sql3);
            preparedStatement3.setInt(1, UserID);

            preparedStatement3.executeUpdate();

            stmt.close();
            stmt2.close();
            stmt3.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void send_reminder_email() {

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM library.people_to_remind;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                String userName = resultSet.getString("userName");
                String userMail = resultSet.getString("userMail");
                String returnDate = resultSet.getString("ReturnDate");
                String delayWeeks = resultSet.getString("Delay_Weeks");
                System.out.println("Hi " + userName + "\nYou have a item which is delayed... The book was supposed to be returned by " + returnDate + " it is now delayed by " + delayWeeks + " weeks. Please return book directly \n Email was sent to : " + userMail);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add_new_item(JTextField title_tf,
                             JTextField dir_auth_tf,
                             JTextField classification_tf,
                             JTextField publisher_tf,
                             JTextField ISBN_tf,
                             JTextField Age_restriction_tf,
                             JTextField Actor_tf,
                             JTextField Location_tf,
                             JComboBox type_cb,
                             JComboBox maxloan_cb){
        String title = title_tf.getText();
        String dir_auth = dir_auth_tf.getText();
        String classification = classification_tf.getText();
        String publisher = publisher_tf.getText();
        String ISBN = ISBN_tf.getText();
        String Actor = Actor_tf.getText();
        String Location = Location_tf.getText();
        String type = type_cb.getSelectedItem().toString();
        int maxloan = Integer.parseInt(maxloan_cb.getSelectedItem().toString());
        String ageResStr = Age_restriction_tf.getText();

        int Age_restriction = 0;
        if (!ageResStr.isEmpty()) {
            Age_restriction = Integer.parseInt(ageResStr);
        }
        else{
            Age_restriction = 0;
        }

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO `library`.`item` (`Title`, `Type`, `Classification`, `Director_Author`, `Publisher_Country`, `Age_Restriction`, `Actor`, `Availability` , `ISBN`, `Location`, `MaxLoan_Weeks`) VALUES (?, ?, ?, ?, ?, ?, ?, 0, ?, ?, ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, classification);
            preparedStatement.setString(4, dir_auth);
            preparedStatement.setString(5, publisher);
            preparedStatement.setInt(6, Age_restriction);
            preparedStatement.setString(7, Actor);
            preparedStatement.setString(8, ISBN);
            preparedStatement.setString(9, Location);
            preparedStatement.setInt(10, maxloan);

            int rows_effected = preparedStatement.executeUpdate();
            if (rows_effected == 0){
                System.out.println("Failed to add new User");
            }
            else{
                System.out.println("User added succesfully");
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int get_latest_itemID (){

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT ItemID FROM library.item ORDER BY ItemID DESC LIMIT 1;";
            PreparedStatement preparedStatement2 = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement2.executeQuery();

            int ItemID = 0;
            while (resultSet.next()) {
                ItemID = resultSet.getInt("ItemID");
            }

            stmt.close();
            conn.close();

            return ItemID;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public void add_itemCopy(String Barcode, int ItemID, int ReferenceCopy){


        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO `library`.`item_copy` (`Barcode`, `IsReferenceCopy`, `ItemID`) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, Barcode);
            preparedStatement.setInt(2, ReferenceCopy);
            preparedStatement.setInt(3, ItemID);

            int rows_effected = preparedStatement.executeUpdate();
            if (rows_effected == 0){
                System.out.println("Failed to add new User");
            }
            else{
                System.out.println("User added succesfully");
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
