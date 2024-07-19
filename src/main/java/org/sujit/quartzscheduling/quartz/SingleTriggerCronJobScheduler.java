package org.sujit.quartzscheduling.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.Trigger.TriggerState.COMPLETE;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.sujit.quartzscheduling.constant.JobConstant.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class SingleTriggerCronJobScheduler {

    private final Scheduler scheduler;

    public void scheduleJob(final JobKey jobKey,
                            final Class<? extends Job> jobClass,
                            final String cronExpression,
                            final TimeZone timeZone) throws SchedulerException {
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        log.info("Scheduling quartz job ");
        if (doesExist(jobDetail)) {
            rescheduleIfCronHasChanged(jobDetail, cronExpression, timeZone);
            log.info("Rescheduled Job {} with cron value {}", jobKey.getName(), cronExpression);

        }
        else {
            scheduleJobWithCronTrigger(jobKey, jobClass, cronExpression, timeZone);
            log.info("Scheduled Job {} with cron value {}", jobKey.getName(), cronExpression);
        }
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

    public void removeJob(final JobKey jobKey) throws SchedulerException {
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);

        if (doesExist(jobDetail)) {
            scheduler.deleteJob(jobKey);
        } else {
            throw new IllegalArgumentException(JOB_WITH_PROVIDED_KEY_DOES_NOT_EXIST);
        }
    }


    private boolean doesExist(JobDetail job) {
        return Objects.nonNull(job);
    }

    private void rescheduleIfCronHasChanged(JobDetail job, String cronExpression, TimeZone timeZone) throws SchedulerException {
        final List<? extends Trigger> triggers = scheduler.getTriggersOfJob(job.getKey());
        CronTrigger cronTrigger = getJobAssociatedCronTrigger(triggers);
        if (doesNotHaveSameCronExpression(cronTrigger, cronExpression) || scheduler.getTriggerState(cronTrigger.getKey()) == (Trigger.TriggerState.ERROR)) {
            rescheduleJobWithNewCronTrigger(job, cronTrigger.getKey(), cronExpression, timeZone);
        }
    }

    private CronTrigger getJobAssociatedCronTrigger(List<? extends Trigger> triggers) {
        Trigger trigger = getAssociatedTriggerOrThrow(triggers);
        throwIfNotCronTrigger(trigger);
        return (CronTrigger) trigger;
    }

    private void rescheduleJobWithNewCronTrigger(JobDetail job,
                                                 TriggerKey oldTriggerKey,
                                                 String cronExpression,
                                                 TimeZone timeZone) throws SchedulerException {
        scheduler.rescheduleJob(
                oldTriggerKey,
                newTrigger()
                        .forJob(job)
                        .withIdentity(UUID.randomUUID().toString(), job.getKey().getGroup())
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression).inTimeZone(timeZone))
                        .build());
    }

    private Trigger getAssociatedTriggerOrThrow(List<? extends Trigger> triggers) {
        List<? extends Trigger> nonCompletedTriggers = triggers.stream()
                .filter(trigger -> !isCompletedTrigger(trigger))
                .collect(Collectors.toList());

        if (nonCompletedTriggers.size() != 1) {
            throw new IllegalStateException(JOB_SHOULD_HAVE_A_SINGLE_TRIGGER);
        }

        return nonCompletedTriggers.get(0);
    }

    private boolean isCompletedTrigger(Trigger trigger) {
        try {
            return scheduler.getTriggerState(trigger.getKey()) == COMPLETE;
        } catch (SchedulerException e) {
            throw new IllegalStateException("couldn't get trigger state", e);
        }
    }

    private boolean doesNotHaveSameCronExpression(CronTrigger cronTrigger, String cronExpression) {
        return !cronTrigger.getCronExpression().equals(cronExpression);
    }


    private void throwIfNotCronTrigger(Trigger trigger) {
        if (!(trigger instanceof CronTrigger)) {
            throw new IllegalStateException(SCHEDULED_TRIGGER_SHOULD_BE_CRON_TRIGGER);
        }
    }



}
