import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            System.out.println(user.user_is_admin());
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
        setVisible(true);
    }
}

