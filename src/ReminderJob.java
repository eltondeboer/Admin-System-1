import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.*;
import java.time.LocalDate;

public class ReminderJob implements Job {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private static final String USERNAME = "java";
    private static final String PASSWORD = "Javaex12";


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        if (reminderSent()){
            System.out.println("already ran");
            return;
        }

        LocalDate currentDate = LocalDate.now();
        java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);

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

            Statement stmt2 = conn.createStatement();
            String sql2 = "INSERT INTO `library`.`email_reminder` (`sent`, `sent_timestamp`) VALUES ('true', ?);";
            PreparedStatement preparedStatement2 = conn.prepareStatement(sql2);
            preparedStatement2.setDate(1, sqlDate);

            preparedStatement2.executeUpdate();

            stmt.close();
            stmt2.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Boolean reminderSent(){
        LocalDate currentDate = LocalDate.now();
        java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);


        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM library.email_reminder ORDER BY ReminderID DESC LIMIT 1;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            // check if result set is empty
            if (!resultSet.next()) {
                return false;
            }

            java.util.Date date_last_sent = resultSet.getDate("sent_timestamp");


            stmt.close();
            conn.close();

            if (date_last_sent != null && ((Date) date_last_sent).toLocalDate().equals(currentDate)){
                return true;
            }
            else{
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
