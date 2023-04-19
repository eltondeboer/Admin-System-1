import java.sql.*;

public class User {
    public String user_id;
    public String name;
    public String email;
    public String phone;
    public String password;
    public String usertype;
    public int user_amount_loan;
    public boolean signed_in = false;

    public boolean getAutenticateUser(String email1, String password1){

        final String DB_URL = "jdbc:mysql://localhost:3306/library";
        final String USERNAME = "java";
        final String PASSWORD = "Javaex12";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM user WHERE userMail=? and userPass=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email1);
            preparedStatement.setString(2, password1);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                user_id = resultSet.getString("userID");
                name = resultSet.getString("userName");
                email = resultSet.getString("userMail");
                phone = resultSet.getString("userPhoneNumber");
                password = resultSet.getString("userPass");
                usertype = resultSet.getString("userType");
                user_amount_loan = resultSet.getInt("currentLoan");
            }

            stmt.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        if(password != null){
            signed_in = true;
            return true;
        }
        else{
            return false;
        }
    }
    public void user_signout(){
        name = null;
        email = null;
        phone = null;
        password = null;
        usertype = null;
        user_id = null;
        user_amount_loan = 0;
        signed_in = false;
    }
    public boolean user_is_admin(){
        if (usertype.equals("Admin")){
            return true;
        }
        else{
            return false;
        }
    }
}


