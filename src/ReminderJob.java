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
            System.out.println("System already ran");
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

            System.out.println("\nLate Returns Reminders\n\n");

            while (resultSet.next()) {
                String userName = resultSet.getString("userName");
                String userEmail = resultSet.getString("userMail");
                String returnDate = resultSet.getString("ReturnDate");
                String delayWeeks = resultSet.getString("Delay_Weeks");

                String subject_late = "Late return of item";
                String body_late = "Hi " + userName + "\nYou have a item which is delayed... The book was supposed to be returned by " + returnDate + " it is now delayed by " + delayWeeks + " weeks. Please return book directly \nThank you,LTU Library\n Email was sent to : " + userEmail + "\n";

                System.out.println(subject_late + "\n" + body_late + "\nSent to : " + userEmail);
            }

            String query = "SELECT r.ReservationID, r.UserID, u.userName, u.userMail, i.Title " +
                    "FROM reservation r " +
                    "JOIN item i ON r.ItemID = i.ItemID " +
                    "JOIN user u ON r.UserID = u.userID " +
                    "WHERE r.IsActive = 1 AND i.Availability > 0";
            Statement stmt2 = conn.createStatement();
            ResultSet rs = stmt2.executeQuery(query);

            System.out.println("\nReservation Available Reminders\n\n");

            // send email notifications to users
            while (rs.next()) {
                int reservationID = rs.getInt("ReservationID");
                String title = rs.getString("Title");
                String userEmail = rs.getString("userMail");
                String userName = rs.getString("userName");
                String bookTitle = rs.getString("Title");

                String subject_reservation = "Item Available for Loaning #" + reservationID + " Title: " + title;
                String body_reservation = "Hello "+ userName + ",\n\nThe item \"" + bookTitle + "\" is now available for pickup.\n\n" +
                        "Please come to the library to check out the item as soon as possible.\n\n" +
                        "Thank you,\nLTU Library \n";

                System.out.println(subject_reservation + "\n" + body_reservation + "\nSent to : " + userEmail + "\n");
            }

            Statement stmt3 = conn.createStatement();
            String sql3 = "INSERT INTO `library`.`email_reminder` (`sent`, `sent_timestamp`) VALUES ('true', ?);";
            PreparedStatement preparedStatement3 = conn.prepareStatement(sql3);
            preparedStatement3.setDate(1, sqlDate);

            preparedStatement3.executeUpdate();

            stmt.close();
            stmt2.close();
            stmt3.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Boolean reminderSent(){
        LocalDate currentDate = LocalDate.now();

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
