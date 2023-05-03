import org.quartz.SchedulerException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class Admin_controll extends JDialog{
    private JButton view_loans_btn;
    private JButton late_btn;
    private JButton return_btn;
    private JPanel Main_panel;
    private JTabbedPane tabbedPane1;
    private JPanel JP_View_loans;
    private JButton manage_btn;
    private JButton add_item_btn;
    private JButton signIn_btn;
    private JPanel JP_Late_Returns;
    private JPanel JP_Return_Loan;
    private JPanel JP_Loan_Item;
    private JPanel JP_Manage_Item;
    private JPanel JP_add_item;
    private JPanel JP_Search_Results;
    private JButton loan_btn;
    private JTable Jtble_View_Loans;
    private JPanel JP_Sign_in;
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JCheckBox show_pw;
    private JButton login_btn_ok;
    private JPanel JP_signed_out;
    private JButton reserveItem_btn;
    private JLabel Admin_actions_label;
    private JTextField tf_searchbar;
    private JPanel Searchbar;
    private JButton search_btn;
    private JPanel HomePage;
    private JButton reg_new_user_btn;
    private JPanel reg_user;
    private JTable u_late_returns_JTbl;
    private JTextField return_bc_tf;
    private JButton return_item_btn;
    private JTextField loan_BC_tf;
    private JButton loan_item_btn;
    private JTextField manage_tf_search;
    private JTable manage_JTble_searchresults;
    private JButton deleteSelectedButton;
    private JButton pushEditsButton;
    private JPanel Reserve_item;
    private JLabel SR_for_label;
    private JTable Jtble_search_results;
    private JTable reserve_Jtable_searchResults;
    private JButton reserveSelectedButton;
    private JButton manage_btn_search;
    private JTextField reserve_tf_searchterm;
    private JButton reserve_BTN_search;
    private JTextField reg_TF_name;
    private JTextField reg_TF_phone;
    private JTextField reg_TF_email;
    private JPasswordField reg_PF_pass;
    private JPasswordField reg_PF_Cpass;
    private JCheckBox reg_CB_showpass;
    private JButton reg_BTN_register;
    private JLabel reg_JLabel_Cpass;
    private JLabel reg_JLabel_pass;
    private JLabel reg_JLabel_email;
    private JLabel reg_JLabel_phone;
    private JLabel reg_JLabel_name;
    private JLabel reg_error_label;
    private JPanel Receipt_of_loan_return;
    private JLabel receipt_title_label;
    private JLabel receipt_ISBN_label;
    private JLabel receipt_lDate_labe;
    private JLabel receipt_rDate_label;
    private JComboBox manage_JCB_selectTable;
    private JScrollPane ScrollPane_View_loans;


    public void signed_out_state() {
        view_loans_btn.setVisible(false);
        late_btn.setVisible(false);
        return_btn.setVisible(false);
        loan_btn.setVisible(false);
        reserveItem_btn.setVisible(false);
        manage_btn.setVisible(false);
        add_item_btn.setVisible(false);
        Admin_actions_label.setVisible(false);
        tfEmail.setText("");
        pfPassword.setText("");
    }
    public void signed_in_state(User user) {
        if (user.user_is_admin()){
            view_loans_btn.setVisible(true);
            late_btn.setVisible(true);
            return_btn.setVisible(true);
            loan_btn.setVisible(true);
            reserveItem_btn.setVisible(true);
            manage_btn.setVisible(true);
            add_item_btn.setVisible(true);
            Admin_actions_label.setVisible(true);
        }
        else{
            view_loans_btn.setVisible(true);
            late_btn.setVisible(true);
            return_btn.setVisible(true);
            loan_btn.setVisible(true);
            reserveItem_btn.setVisible(true);
            manage_btn.setVisible(false);
            add_item_btn.setVisible(false);
            Admin_actions_label.setVisible(false);
        }

    }
    public Admin_controll (JFrame parent){
        super(parent);
        //Creates instant of user.
        User user1 = new User();
        EmailReminder emailReminder = new EmailReminder();
        //Makes the searchbar not resize depending on window size
        Searchbar.setMaximumSize(new Dimension(Searchbar.getPreferredSize().width, Searchbar.getPreferredSize().height));
        Searchbar.setMinimumSize(new Dimension(Searchbar.getPreferredSize().width, Searchbar.getPreferredSize().height));
        Searchbar.setPreferredSize(new Dimension(Searchbar.getPreferredSize().width, Searchbar.getPreferredSize().height));
        Searchbar.putClientProperty("JPanel.sizeVariant", "small");
        //Setting the opening state to signed out
        signed_out_state();
        //Create database connection to grab tables to insert
        DataBase_conn dbconn = new DataBase_conn();
        //Sets the windows title, content pane, size, makes it reziseable, relitive to parent object, and makes it close when closed.
        setTitle("Library System");
        setContentPane(Main_panel);
        URL iconURL = Admin_controll.class.getResource("/library.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());

        setMinimumSize(new Dimension(600, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    emailReminder.stopScheduler();
                } catch (SchedulerException ex) {
                    throw new RuntimeException(ex);
                }
            }
            @Override
            public void windowOpened(WindowEvent e) {
                emailReminder.startScheduler();
            }
        });

        //Sets data from database in JTable and makes it non editable.
        //Customizes UI to hide tabs
        tabbedPane1.setUI(new HiddenTabbedPaneUI());
        //Opens HomePage on start
        tabbedPane1.setSelectedIndex(9);
        manage_JCB_selectTable.addItem("item");
        manage_JCB_selectTable.addItem("item_copy");
        manage_JCB_selectTable.addItem("user");




        //When user presses login data gets checked against the database through function "getAutenticateUser(email, password);"
        //Then gets either denied or signed in.
        //Depending on user type the UI gets set.
        login_btn_ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean acces = false;
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

                acces = user1.getAutenticateUser(email, password);
                if(acces){
                    signIn_btn.setText("Sign out");
                    tabbedPane1.setSelectedIndex(0);
                    signed_in_state(user1);
                    Jtble_View_Loans.setModel(dbconn.view_loans_table(user1));
                }
                else{
                    JOptionPane.showMessageDialog(Admin_controll.this,
                            "Email or Password Invalid");
                }
            }
        });

        view_loans_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane1.setSelectedIndex(0);
            }
        });

        late_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane1.setSelectedIndex(1);
                u_late_returns_JTbl.setModel(dbconn.view_late_userReturns_table(user1));
            }
        });

        return_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane1.setSelectedIndex(2);
            }
        });

        signIn_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user1.signed_in){
                    user1.user_signout();
                    tabbedPane1.setSelectedIndex(8);
                    signIn_btn.setText("Sign In");
                    signed_out_state();
                }
                else{
                    tabbedPane1.setSelectedIndex(7);
                }
            }
        });

        //Function so that the checkmark hides and shows password.
        show_pw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (show_pw.isSelected()) {
                    pfPassword.setEchoChar((char) 0);
                } else {
                    pfPassword.setEchoChar('*');
                }
            }
        });
        loan_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane1.setSelectedIndex(3);
            }
        });
        reserveItem_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane1.setSelectedIndex(11);
            }
        });
        manage_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane1.setSelectedIndex(4);
            }
        });
        add_item_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane1.setSelectedIndex(5);
            }
        });

        search_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String search_txt = tf_searchbar.getText();
                Jtble_search_results.setModel(dbconn.search_results(search_txt, SR_for_label));
                tabbedPane1.setSelectedIndex(6);
                tf_searchbar.setText("");
            }
        });
        tf_searchbar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    search_btn.doClick();
                }
            }
        });
        tfEmail.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    login_btn_ok.doClick();
                }
            }
        });
        pfPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    login_btn_ok.doClick();
                }
            }
        });
        tabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(tabbedPane1.getSelectedIndex() == 11 || tabbedPane1.getSelectedIndex() ==4){
                    Searchbar.setVisible(false);
                }
                else {
                    Searchbar.setVisible(true);
                }
            }
        });
        reg_new_user_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane1.setSelectedIndex(10);
            }
        });

        //Checks if fields are empty and informs the user through color and text, otherwise adds user to database through function
        reg_BTN_register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get user input from the text fields
                String name = reg_TF_name.getText().trim();
                String phone = reg_TF_phone.getText().trim();
                String email = reg_TF_email.getText().trim();
                String password = new String(reg_PF_pass.getPassword());
                String cpassword = new String(reg_PF_Cpass.getPassword());

                // Check if any field is empty
                boolean hasEmptyField = false;
                if (name.isEmpty()) {
                    reg_JLabel_name.setForeground(Color.RED);
                    hasEmptyField = true;
                } else {
                    reg_JLabel_name.setForeground(Color.BLACK);
                }
                if (phone.isEmpty()) {
                    reg_JLabel_phone.setForeground(Color.RED);
                    hasEmptyField = true;
                } else {
                    reg_JLabel_phone.setForeground(Color.BLACK);
                }
                if (email.isEmpty()) {
                    reg_JLabel_email.setForeground(Color.RED);
                    hasEmptyField = true;
                } else {
                    reg_JLabel_email.setForeground(Color.BLACK);
                }
                if (password.isEmpty()) {
                    reg_JLabel_pass.setForeground(Color.RED);
                    hasEmptyField = true;
                } else {
                    reg_JLabel_pass.setForeground(Color.BLACK);
                }
                if (cpassword.isEmpty()) {
                    reg_JLabel_Cpass.setForeground(Color.RED);
                    hasEmptyField = true;
                } else {
                    reg_JLabel_Cpass.setForeground(Color.BLACK);
                }

                if (hasEmptyField) {
                    reg_error_label.setText("Please fill in all fields");
                    return;
                }

                // Check if passwords match
                if (!password.equals(cpassword)) {
                    reg_error_label.setText("Passwords do not match");
                    return;
                }

                dbconn.add_user(reg_TF_name,
                        reg_TF_phone,
                        reg_TF_email,
                        reg_PF_pass);

                reg_TF_name.setText("");
                reg_TF_phone.setText("");
                reg_TF_email.setText("");
                reg_PF_pass.setText("");
                reg_PF_Cpass.setText("");
                reg_error_label.setText("");
                reg_JLabel_name.setForeground(Color.BLACK);
                reg_JLabel_phone.setForeground(Color.BLACK);
                reg_JLabel_email.setForeground(Color.BLACK);
                reg_JLabel_pass.setForeground(Color.BLACK);
                reg_JLabel_Cpass.setForeground(Color.BLACK);

                JOptionPane.showMessageDialog(Admin_controll.this, "User registered successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

            }
        });

        //Allows the checkbox to show the password entered to the user
        reg_CB_showpass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (reg_CB_showpass.isSelected()) {
                    reg_PF_pass.setEchoChar((char) 0);
                    reg_PF_Cpass.setEchoChar((char) 0);
                } else {
                    reg_PF_pass.setEchoChar('*');
                    reg_PF_Cpass.setEchoChar('*');
                }
            }
        });

        //Everything under here until next comment is to make it posible to press enter intstead of the button.
        reg_TF_name.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    reg_BTN_register.doClick();
                }
            }
        });
        reg_TF_phone.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    reg_BTN_register.doClick();
                }
            }
        });
        reg_TF_email.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    reg_BTN_register.doClick();
                }
            }
        });
        reg_PF_pass.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    reg_BTN_register.doClick();
                }
            }
        });
        reg_PF_Cpass.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    reg_BTN_register.doClick();
                }
            }
        });
        return_item_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Boolean returned = dbconn.return_loan(return_bc_tf, user1);
                if (returned){
                    JOptionPane.showMessageDialog(Admin_controll.this, "Book Returned Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(Admin_controll.this, "Barcode not in system", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
                Jtble_View_Loans.setModel(dbconn.view_loans_table(user1));
                return_bc_tf.setText("");
            }
        });
        return_bc_tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    return_item_btn.doClick();
                }
            }
        });
        loan_item_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String barcode = loan_BC_tf.getText();
                String result = dbconn.loan_book(loan_BC_tf, user1);
                if(result.equals("Refrence")) {
                    JOptionPane.showMessageDialog(Admin_controll.this, "The book you are trying to loan is a reference copy and cannot be loaned.", "Error", JOptionPane.INFORMATION_MESSAGE);
                } else if (result.equals("Barcode")) {
                    JOptionPane.showMessageDialog(Admin_controll.this, "Barcode not in system", "Error", JOptionPane.INFORMATION_MESSAGE);
                } else if (result.equals("Copies")) {
                    JOptionPane.showMessageDialog(Admin_controll.this, "The book you are trying to loan is not available. Check the barcode again", "Error", JOptionPane.INFORMATION_MESSAGE);
                } else if (result.equals("Researcher")) {
                    JOptionPane.showMessageDialog(Admin_controll.this, "Researcher can loan up to 20 books only", "Error", JOptionPane.INFORMATION_MESSAGE);
                } else if (result.equals("Teacher")) {
                    JOptionPane.showMessageDialog(Admin_controll.this, "Teacher can loan up to 10 books only", "Error", JOptionPane.INFORMATION_MESSAGE);
                } else if (result.equals("Student")) {
                    JOptionPane.showMessageDialog(Admin_controll.this, "Student can loan up to 5 books only", "Error", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(Admin_controll.this, "Book Loaned Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dbconn.get_recipt(barcode, receipt_title_label, receipt_ISBN_label, receipt_lDate_labe, receipt_rDate_label);
                    tabbedPane1.setSelectedIndex(12);
                }
                Jtble_View_Loans.setModel(dbconn.view_loans_table(user1));
                loan_BC_tf.setText("");
            }
        });
        loan_BC_tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    loan_item_btn.doClick();
                }
            }
        });
        manage_btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String)manage_JCB_selectTable.getSelectedItem();
                String searchterm = manage_tf_search.getText();
                if(selected.equals("item")){
                    manage_JTble_searchresults.setModel(dbconn.search_results_item_manage(searchterm));
                }
                else if (selected.equals("item_copy")) {
                    manage_JTble_searchresults.setModel(dbconn.search_results_copy_manage(searchterm));
                }
                else {
                    manage_JTble_searchresults.setModel(dbconn.search_results_user_manage(searchterm));
                }

            }
        });
        manage_tf_search.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    manage_btn_search.doClick();
                }
            }
        });
        pushEditsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Defines which table is selected through a dropdown
                String selected = (String)manage_JCB_selectTable.getSelectedItem();
                int result = JOptionPane.showConfirmDialog(Admin_controll.this, "Are you sure you want to update the database?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if(selected.equals("item")){
                    if (result == JOptionPane.YES_OPTION) {
                        dbconn.push_edits_item(manage_JTble_searchresults);
                        JOptionPane.showMessageDialog(Admin_controll.this, "Database updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(Admin_controll.this, "Update cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                        manage_btn_search.doClick();
                    }
                }
                else if(selected.equals("item_copy")){
                    if (result == JOptionPane.YES_OPTION) {
                        dbconn.push_edits_item_copy(manage_JTble_searchresults);
                        JOptionPane.showMessageDialog(Admin_controll.this, "Database updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(Admin_controll.this, "Update cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                        manage_btn_search.doClick();
                    }
                }
                else{
                    if (result == JOptionPane.YES_OPTION) {
                        dbconn.push_edits_user(manage_JTble_searchresults);
                        JOptionPane.showMessageDialog(Admin_controll.this, "Database updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(Admin_controll.this, "Update cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                        manage_btn_search.doClick();
                    }
                }
            }
        });
        deleteSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String)manage_JCB_selectTable.getSelectedItem();
                int selectedRow = manage_JTble_searchresults.getSelectedRow();
                if (selected.equals("item")){
                    int result = JOptionPane.showConfirmDialog(Admin_controll.this, "Are you sure you want to delete item? OBS: Corresponding item copies will also be deleted", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (selectedRow != -1) {
                        // Get the value of the first column in the selected row
                        Object value = manage_JTble_searchresults.getValueAt(selectedRow, 0);
                        if (value != null && result == JOptionPane.YES_OPTION) {
                            int ItemID = Integer.parseInt(value.toString());
                            dbconn.delete_item(ItemID);
                            JOptionPane.showMessageDialog(Admin_controll.this, "Item deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                            manage_btn_search.doClick();
                        }
                    }
                }

                else if (selected.equals("item_copy")){
                    int result = JOptionPane.showConfirmDialog(Admin_controll.this, "Are you sure you want to delete item copy?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (selectedRow != -1) {
                        // Get the value of the first column in the selected row
                        Object value = manage_JTble_searchresults.getValueAt(selectedRow, 0);
                        if (value != null && result == JOptionPane.YES_OPTION) {
                            String Barcode = (String) value;
                            dbconn.delete_item_copy(Barcode);
                            JOptionPane.showMessageDialog(Admin_controll.this, "Item deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                            manage_btn_search.doClick();
                        }
                    }
                }

                else if (selected.equals("user")){
                    int result = JOptionPane.showConfirmDialog(Admin_controll.this, "Are you sure you want to delete user? OBS: Corresponding loans, and reservations will also be deleted", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (selectedRow != -1) {
                        // Get the value of the first column in the selected row
                        Object value = manage_JTble_searchresults.getValueAt(selectedRow, 0);
                        if (value != null && result == JOptionPane.YES_OPTION) {
                            int UserID = Integer.parseInt(value.toString());
                            dbconn.delete_user(UserID);
                            JOptionPane.showMessageDialog(Admin_controll.this, "Item deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                            manage_btn_search.doClick();
                        }
                    }
                }
            }
        });

        reserve_BTN_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchterm = reserve_tf_searchterm.getText();
                reserve_Jtable_searchResults.setModel(dbconn.search_results_reservation(searchterm));
                // Hides the ItemID column
                TableColumnModel columnModel = reserve_Jtable_searchResults.getColumnModel();
                TableColumn firstColumn = columnModel.getColumn(0);
                firstColumn.setMinWidth(0);
                firstColumn.setMaxWidth(0);

            }
        });
        reserve_tf_searchterm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    reserve_BTN_search.doClick();
                }
            }
        });
        reserveSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = reserve_Jtable_searchResults.getSelectedRow();
                if (selectedRow != -1) {
                    // Get the value of the first column in the selected row
                    Object value = reserve_Jtable_searchResults.getValueAt(selectedRow, 0);
                    if (value != null) {
                        int intValue = Integer.parseInt(value.toString());
                        dbconn.add_reservation(intValue, user1);
                        JOptionPane.showMessageDialog(Admin_controll.this, "Reservation added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

                    }
                }
                else{
                    JOptionPane.showMessageDialog(Admin_controll.this, "No item selected", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        setVisible(true);
    }
}

