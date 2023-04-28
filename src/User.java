import java.sql.*;

public class User {
    private String user_id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String usertype;
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
        signed_in = false;
    }
    public boolean user_is_admin(){
        return usertype.equals("Admin");
    }
    public String getUserId() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return usertype;
    }
}

