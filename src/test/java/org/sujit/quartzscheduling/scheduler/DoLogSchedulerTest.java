package org.sujit.quartzscheduling.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobKey;
import org.sujit.quartzscheduling.constant.SchedulerConstant;
import org.sujit.quartzscheduling.quartz.SingleTriggerCronJobScheduler;

import java.util.TimeZone;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DoLogSchedulerTest {

    private static final String EVERY_MINUTE_CRON_EXPRESSION = "0 * * * * ?";

    @Mock
    private SingleTriggerCronJobScheduler singleTriggerCronJobScheduler;

    private DoLogSchedulerJobConfig doLogSchedulerJobConfig;

    @BeforeEach
    void setUp() {
        doLogSchedulerJobConfig = new DoLogSchedulerJobConfig(singleTriggerCronJobScheduler,
                EVERY_MINUTE_CRON_EXPRESSION);
    }

    @Test
    void scheduleCorporateUsersDailyJob() throws Exception {
        TimeZone timeZone = SchedulerConstant.DEFAULT_TIMEZONE;
        doLogSchedulerJobConfig.scheduleLogSchedulerJob();
        verify(singleTriggerCronJobScheduler).scheduleJob(new JobKey("DoLogScheduler"), DoLogScheduler.class,
                EVERY_MINUTE_CRON_EXPRESSION, timeZone);
    }

}