import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class EmailReminder {

    private Scheduler scheduler;

    public void startScheduler() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();

            scheduler.start();

            JobDetail reminder_job = newJob(ReminderJob.class).withIdentity("reminder").build();

            CronTrigger trigger = newTrigger()
                    .withIdentity("morning-send")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 9-15 ? * MON-FRI").withMisfireHandlingInstructionDoNothing())
                    .build();

            Trigger trigger_now = TriggerBuilder.newTrigger()
                    .withIdentity("reminderTrigger")
                    .startNow()
                    .build();

            scheduler.scheduleJob(reminder_job, trigger_now);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopScheduler() throws SchedulerException {
        if (scheduler != null) {
            scheduler.shutdown(true);
        }
    }
}
