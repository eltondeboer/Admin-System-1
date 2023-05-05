import org.quartz.SchedulerException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
//This things are varibles that connect the code to the gui, and above are the imports
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
    private JTextField add_tf_title;
    private JTextField add_tf_classification;
    private JTextField add_tf_dirAuth;
    private JTextField add_tf_pubCountry;
    private JTextField add_tf_ageRes;
    private JTextField add_tf_actor;
    private JTextField add_tf_ISBN;
    private JTextField add_tf_location;
    private JComboBox add_cb_type;
    private JComboBox add_cb_maxLoan;
    private JButton add_btn_newitem;
    private JLabel add_label_details;
    private JLabel add_lbl_title;
    private JLabel add_lbl_classification;
    private JLabel add_lbl_DirAuth;
    private JLabel add_lbl_PubCounty;
    private JLabel add_lbl_type;
    private JLabel add_lbl_ageres;
    private JLabel add_lbl_actor;
    private JLabel add_lbl_isbn;
    private JLabel add_lbl_Location;
    private JLabel add_lbl_maxLoan;
    private JPanel add_item_copy_after_item;
    private JTextField addCopy_tf_barcode;
    private JCheckBox addCopy_cb_refCopy;
    private JButton addCopy_btn_add;
    private JButton item_copy_btn;
    private JPanel add_only_item_copy;
    private JTextField copy_tf_barcode;
    private JTextField copy_tf_itemID;
    private JCheckBox copy_CB_refCopy;
    private JButton copy_btn_add;

    //This funtion is used to set tha gui state when you are signed out
    public void signed_out_state() {
        view_loans_btn.setVisible(false);
        late_btn.setVisible(false);
        return_btn.setVisible(false);
        loan_btn.setVisible(false);
        reserveItem_btn.setVisible(false);
        manage_btn.setVisible(false);
        item_copy_btn.setVisible(false);
        add_item_btn.setVisible(false);
        Admin_actions_label.setVisible(false);
        tfEmail.setText("");
        pfPassword.setText("");
    }
    //This funtion is used to set tha gui state when you are signed in, and check if you are admin or not.
    public void signed_in_state(User user) {
        if (user.user_is_admin()){
            view_loans_btn.setVisible(true);
            late_btn.setVisible(true);
            return_btn.setVisible(true);
            loan_btn.setVisible(true);
            reserveItem_btn.setVisible(true);
            manage_btn.setVisible(true);
            item_copy_btn.setVisible(true);
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
            item_copy_btn.setVisible(false);
        }

    }
    //Here we create the constructor for the admin controller, which controlls the whole ui.
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
        setMinimumSize(new Dimension(900, 700));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //This is a action listner for when the windoe closes the scheduler stops, and starts when it opens.
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
                    //Sets the view_loans table to the table in the database for the user. So it shows on startup.
                    Jtble_View_Loans.setModel(dbconn.view_loans_table(user1));
                }
                else{
                    JOptionPane.showMessageDialog(Admin_controll.this,
                            "Email or Password Invalid");
                }
            }
        });
        //When user presses view_loans_btn the view changes.
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
                //Sets the late_returns table to the table in the database for the user.
                u_late_returns_JTbl.setModel(dbconn.view_late_userReturns_table(user1));
            }
        });
        //When user presses return_btn the view changes.
        return_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane1.setSelectedIndex(2);
            }
        });

        //Checks if user is already signed in or not, and changes the view accordingly.
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

        //When user presses search_btn the view changes.
        loan_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane1.setSelectedIndex(3);
            }
        });
        //when user presses reserveItem_btn the view changes.
        reserveItem_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane1.setSelectedIndex(11);
            }
        });
        //when user presses manage_btn the view changes.
        manage_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane1.setSelectedIndex(4);
            }
        });
        //when user presses add_item_btn the view changes.
        add_item_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                add_cb_type.getActionListeners()[0].actionPerformed(new ActionEvent(add_item_btn, ActionEvent.ACTION_PERFORMED, null));
                tabbedPane1.setSelectedIndex(5);
            }
        });

        //When user presses search the searchterm is grabbed and sent to the database to be searched for.
        search_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String search_txt = tf_searchbar.getText();
                Jtble_search_results.setModel(dbconn.search_results(search_txt, SR_for_label));
                tabbedPane1.setSelectedIndex(6);
                tf_searchbar.setText("");
            }
        });

        //When user presses enter the corresponding button is clicked
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

        //Hides the searchbar when user is in the manage tab or the reserve tab.
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

        //When user presses the reserve button the view changes.
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

        //When user presses the return button the entered barcode gets checked and if it exists it gets returned.
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

        //Makes it posible to press enter intstead of the button.
        return_bc_tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    return_item_btn.doClick();
                }
            }
        });

        //Checks all loan condtions and the performs the loan.
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
        //Makes it posible to press enter intstead of the button.
        loan_BC_tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    loan_item_btn.doClick();
                }
            }
        });

        //Searches for the entered searchterm and if it exists it gets displayed.
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
        //Makes it posible to press enter intstead of the button.
        manage_tf_search.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    manage_btn_search.doClick();
                }
            }
        });

        //Pushes the edits to the database when user preses the button.
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

        //Deletes the selected row from the database.
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

        //Adds the reservation to the database.
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
        //Makes it possible to search by pressing enter.
        reserve_tf_searchterm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    reserve_BTN_search.doClick();
                }
            }
        });

        //Adds the reservation to the database for the selected item.
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
        //Checks if fields are empty and informs the user through color and text, otherwise adds user to database through function
        add_btn_newitem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = add_tf_title.getText().trim();
                String actor = add_tf_actor.getText().trim();
                String ageResStr = add_tf_ageRes.getText().trim();
                int ageRes = 0;
                if (!ageResStr.isEmpty()) {
                    ageRes = Integer.parseInt(ageResStr);
                }
                else{
                    ageRes = 0;
                }
                String dirAuth = add_tf_dirAuth.getText().trim();
                String ISBN = add_tf_ISBN.getText().trim();
                String Classification = add_tf_classification.getText().trim();
                String location = add_tf_location.getText().trim();
                String PubCountry = add_tf_pubCountry.getText().trim();

                boolean success = false;
                int result = 0;
                boolean hasEmptyField = false;
                if (title.isEmpty()) {
                    hasEmptyField = true;
                    add_lbl_title.setForeground(Color.red);
                }
                else {
                    add_lbl_title.setForeground(Color.black);
                }
                if (dirAuth.isEmpty()) {
                    hasEmptyField = true;
                    add_lbl_DirAuth.setForeground(Color.red);
                }
                else {
                    add_lbl_DirAuth.setForeground(Color.black);
                }
                if (Classification.isEmpty()) {
                    hasEmptyField = true;
                    add_lbl_classification.setForeground(Color.red);
                }
                else {
                    add_lbl_classification.setForeground(Color.black);
                }
                if (location.isEmpty()) {
                    hasEmptyField = true;
                    add_lbl_Location.setForeground(Color.red);
                }
                else {
                    add_lbl_Location.setForeground(Color.black);
                }
                if (PubCountry.isEmpty()) {
                    hasEmptyField = true;
                    add_lbl_PubCounty.setForeground(Color.red);
                }
                else {
                    add_lbl_PubCounty.setForeground(Color.black);
                }

                if(hasEmptyField){
                    JOptionPane.showMessageDialog(Admin_controll.this, "Please fill in required fields. (Title, Director/Author, Classification, Location, Publisher/Country)", "Error", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                else{
                    success = dbconn.add_new_item(add_tf_title, add_tf_dirAuth, add_tf_classification, add_tf_pubCountry, add_tf_ISBN, add_tf_ageRes, add_tf_actor, add_tf_location, add_cb_type, add_cb_maxLoan);

                    if (success) {
                        result = JOptionPane.showConfirmDialog(Admin_controll.this, "Item added successfully, Do you want to add corresponding Item Copies", "Confirmation", JOptionPane.YES_NO_OPTION);
                        add_tf_pubCountry.setText("");
                        add_tf_location.setText("");
                        add_tf_classification.setText("");
                        add_tf_ISBN.setText("");
                        add_tf_dirAuth.setText("");
                        add_tf_ageRes.setText("");
                        add_tf_actor.setText("");
                        add_tf_title.setText("");
                        add_lbl_ageres.setForeground(Color.BLACK);
                        add_lbl_PubCounty.setForeground(Color.BLACK);
                        add_lbl_Location.setForeground(Color.BLACK);
                        add_lbl_classification.setForeground(Color.BLACK);
                        add_lbl_DirAuth.setForeground(Color.BLACK);
                        add_lbl_title.setForeground(Color.BLACK);
                        manage_btn_search.doClick();
                    }
                    else{
                        JOptionPane.showMessageDialog(Admin_controll.this, "Something went wrong, try again later", "Error", JOptionPane.INFORMATION_MESSAGE);
                    }

                }

                if (result == JOptionPane.YES_OPTION) {
                    tabbedPane1.setSelectedIndex(13);
                }
            }
        });
        //Here you can add corresponding item copies to the item you just added.
        addCopy_btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int RefrenceCopy;
                int ItemID = dbconn.get_latest_itemID();
                String Barcode = addCopy_tf_barcode.getText();
                boolean itemAdded = false;

                if(addCopy_cb_refCopy.isSelected()){
                    RefrenceCopy = 1;
                }
                else{
                    RefrenceCopy = 0;
                }

                itemAdded = dbconn.add_itemCopy(Barcode, ItemID, RefrenceCopy);

                if (itemAdded) {
                    addCopy_tf_barcode.setText("");
                    JOptionPane.showMessageDialog(Admin_controll.this, "Item Copy added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(Admin_controll.this, "Something went wrong, try again later", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        //Changes the fields depending on what type of item you want to add
        add_cb_type.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(add_cb_type.getSelectedItem().toString().equals("Book")){
                    add_lbl_actor.setVisible(false);
                    add_tf_actor.setVisible(false);
                    add_lbl_ageres.setVisible(false);
                    add_tf_ageRes.setVisible(false);
                    add_lbl_PubCounty.setText("Publisher");
                    add_lbl_isbn.setVisible(true);
                    add_tf_ISBN.setVisible(true);
                    add_lbl_DirAuth.setText("Author");
                }
                else{
                    add_lbl_actor.setVisible(true);
                    add_tf_actor.setVisible(true);
                    add_lbl_ageres.setVisible(true);
                    add_tf_ageRes.setVisible(true);
                    add_lbl_PubCounty.setText("Country");
                    add_lbl_isbn.setVisible(false);
                    add_tf_ISBN.setVisible(false);
                    add_lbl_DirAuth.setText("Director");
                }
            }
        });
        //Changes the view to the add item copy tab
        item_copy_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane1.setSelectedIndex(14);
            }
        });
        copy_btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int RefrenceCopy;
                int itemID = Integer.parseInt(copy_tf_itemID.getText());
                boolean itemAdded = false;
                String Barcode = copy_tf_barcode.getText();

                if (copy_CB_refCopy.isSelected()) {
                    RefrenceCopy = 1;
                }
                else{
                    RefrenceCopy = 0;
                }

                itemAdded = dbconn.add_itemCopy(Barcode, itemID, RefrenceCopy);

                if (itemAdded) {
                    copy_tf_barcode.setText("");
                    copy_tf_itemID.setText("");
                    JOptionPane.showMessageDialog(Admin_controll.this, "Item Copy added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(Admin_controll.this, "Something went wrong, try again later", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        setVisible(true);
    }
}

