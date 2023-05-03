// Press ⇧ twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;


public class Main {
    public static void main(String[] args){

        Admin_controll adminControll = new Admin_controll(null);

        EmailReminder emailReminder = new EmailReminder();

        adminControll.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    emailReminder.stopScheduler();
                } catch (SchedulerException ex) {
                    throw new RuntimeException(ex);
                }
                System.exit(0); // add this line to exit the program
            }
            @Override
            public void windowOpened(WindowEvent e) {
                emailReminder.startScheduler();
            }
        });
    }
}