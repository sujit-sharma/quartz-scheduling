package org.sujit.quartzscheduling.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.TimeZone;
import java.util.UUID;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Component
@RequiredArgsConstructor
@Slf4j
public class SingleTriggerCronJobScheduler {

    private final Scheduler scheduler;

    public void scheduleJob(final JobKey jobKey,
                            final Class<? extends Job> jobClass,
                            final String cronExpression,
                            final TimeZone timeZone) throws SchedulerException {
        scheduleJobWithCronTrigger(jobKey, jobClass, cronExpression, timeZone);
        log.debug("Scheduled Job {} with cron value {}", jobKey.getName(), cronExpression);
    }


    private void scheduleJobWithCronTrigger(JobKey jobKey,
                                            Class<? extends Job> jobClass,
                                            String cronExpression,
                                            TimeZone timeZone) throws SchedulerException {
        final JobDetail job = newJob(jobClass)
                .storeDurably()
                .requestRecovery()
                .withIdentity(jobKey)
                .build();

        final CronTrigger cronTrigger = newTrigger()
                .forJob(job)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression).inTimeZone(timeZone))
                .withIdentity(UUID.randomUUID().toString(), jobKey.getGroup())
                .build();

        this.scheduler.scheduleJob(job, cronTrigger);
    }

}
