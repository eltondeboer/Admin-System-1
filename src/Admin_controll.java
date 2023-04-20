import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Admin_controll extends JDialog{
    private JButton view_loans_btn;
    private JButton late_btn;
    private JButton return_btn;
    private JPanel Main_panel;
    private JTabbedPane tabbedPane1;
    private JPanel JP_View_loans;
    private JButton manage_btn;
    private JButton all_late_btn;
    private JButton signIn_btn;
    private JPanel JP_Late_Returns;
    private JPanel JP_Return_Loan;
    private JPanel JP_Loan_Item;
    private JPanel JP_Manage_Item;
    private JPanel JP_All_Late;
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
    private JTextField textField2;
    private JButton return_item_btn;
    private JTextField textField3;
    private JButton loan_item_btn;
    private JTextField textField4;
    private JTable table2;
    private JButton deleteSelectedButton;
    private JButton pushEditsButton;
    private JPanel Reserve_item;
    private JPanel Recipt_of_loan;
    private JTable table3;
    private JLabel SR_for_label;
    private JTable Jtble_search_results;
    private JTable table5;
    private JButton reserveSelectedButton;
    private JButton button1;
    private JTextField textField5;
    private JButton button2;
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


    public void signed_out_state() {
        view_loans_btn.setVisible(false);
        late_btn.setVisible(false);
        return_btn.setVisible(false);
        loan_btn.setVisible(false);
        reserveItem_btn.setVisible(false);
        manage_btn.setVisible(false);
        all_late_btn.setVisible(false);
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
            all_late_btn.setVisible(true);
            Admin_actions_label.setVisible(true);
        }
        else{
            view_loans_btn.setVisible(true);
            late_btn.setVisible(true);
            return_btn.setVisible(true);
            loan_btn.setVisible(true);
            reserveItem_btn.setVisible(true);
            manage_btn.setVisible(false);
            all_late_btn.setVisible(false);
            Admin_actions_label.setVisible(false);
        }

    }
    public Admin_controll (JFrame parent){
        super(parent);
        //Creates instant of user.
        User user1 = new User();
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
        setMinimumSize(new Dimension(600, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //Sets data from database in JTable and makes it non editable.
        //Customizes UI to hide tabs
        tabbedPane1.setUI(new HiddenTabbedPaneUI());
        //Opens HomePage on start
        tabbedPane1.setSelectedIndex(9);

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
        all_late_btn.addActionListener(new ActionListener() {
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
        setVisible(true);
    }
}

