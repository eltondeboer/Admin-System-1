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
    private JPanel Searchbar;
    private JTextField textField1;
    private JButton search_btn;
    private JTable Jtble_View_Loans;
    private JPanel JP_Sign_in;
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JCheckBox show_pw;
    private JButton login_btn_ok;

    public Admin_controll (JFrame parent){
        super(parent);
        DataBase_tbles dbconn = new DataBase_tbles();
        setTitle("Admin System");
        setContentPane(Main_panel);
        setMinimumSize(new Dimension(600, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Jtble_View_Loans.setModel(dbconn.get_items_test());
        Jtble_View_Loans.setVisible(true);
        tabbedPane1.setUI(new HiddenTabbedPaneUI());
        User user1 = new User();
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
                tabbedPane1.setSelectedIndex(7);
            }
        });

        login_btn_ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user1.signed_in){
                    user1.user_signout();
                    System.out.println(user1.name);
                }
                else{
                    boolean acces = false;
                    String email = tfEmail.getText();
                    String password = String.valueOf(pfPassword.getPassword());

                    acces = user1.getAutenticateUser(email, password);

                    if(acces){
                        signIn_btn.setText("Sign out");
                        tabbedPane1.setSelectedIndex(0);
                    }
                    else{
                        JOptionPane.showMessageDialog(Admin_controll.this,
                                "Email or Password Invalid");
                    }
                }
            }
        });

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
        setVisible(true);
    }
}

