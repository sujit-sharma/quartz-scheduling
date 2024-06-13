package org.sujit.quartzscheduling.quartz;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.Job;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import java.util.TimeZone;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SingleTriggerCronJobSchedulerTest {

    @Mock
    private Scheduler scheduler;

    @InjectMocks
    private SingleTriggerCronJobScheduler cronJobScheduler;

    @Test
    void givenJobDescription_whenSchedule_thenShouldScheduleANewCronTrigger() throws Exception {
        final TimeZone timeZone = TimeZone.getDefault();
        final JobKey jobKey = JobKey.jobKey(UUID.randomUUID().toString());

        String cronExpression = "0 0/5 * * * ? *";
        cronJobScheduler.scheduleJob(jobKey, Job.class, cronExpression, timeZone);

        verify(scheduler, times(1)).scheduleJob(any(), any());
        assertThat(scheduler.getJobDetail(jobKey));
    }

    @Test
    void scheduleJob_ExceptionThrown() throws SchedulerException {
        JobKey jobKey = new JobKey("testJob");
        String cronExpression = "0 0 12 * * ?";
        TimeZone timeZone = TimeZone.getDefault();

        doThrow(SchedulerException.class).when(scheduler).scheduleJob(any(), any());

        assertThrows(SchedulerException.class, () ->
                cronJobScheduler.scheduleJob(jobKey, Job.class, cronExpression, timeZone)
        );
    }

}