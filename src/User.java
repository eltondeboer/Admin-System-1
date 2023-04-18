import java.sql.*;

public class User {
    public String name;
    public String email;
    public String phone;
    public String password;
    public String usertype;
    public boolean signed_in = false;

    public boolean getAutenticateUser(String email1, String password1){

        final String DB_URL = "jdbc:mysql://localhost:3306/Library";
        final String USERNAME = "java";
        final String PASSWORD = "Javaex12";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE email=? and Password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email1);
            preparedStatement.setString(2, password1);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                name = resultSet.getString("Name");
                email = resultSet.getString("email");
                phone = resultSet.getString("Phone");
                password = resultSet.getString("Password");
                usertype = resultSet.getString("Type");
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
        signed_in = false;
    }
}


