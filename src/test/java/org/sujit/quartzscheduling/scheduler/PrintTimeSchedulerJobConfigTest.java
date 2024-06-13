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
class PrintTimeSchedulerJobConfigTest {

    private static final String EVERY_TWO_MINUTE_CRON_EXPRESSION = "0 1/2 * * * ?";


    @Mock
    private SingleTriggerCronJobScheduler singleTriggerCronJobScheduler;

    private PrintTimeSchedulerJobConfig printTimeSchedulerJobConfig;

    @BeforeEach
    void setUp() {
        printTimeSchedulerJobConfig = new PrintTimeSchedulerJobConfig(singleTriggerCronJobScheduler,
                EVERY_TWO_MINUTE_CRON_EXPRESSION);
    }

    @Test
    void scheduleCorporateUsersDailyJob() throws Exception {
        TimeZone timeZone = SchedulerConstant.DEFAULT_TIMEZONE;
        printTimeSchedulerJobConfig.schedulePrintTimeSchedulerJob();
        verify(singleTriggerCronJobScheduler).scheduleJob(new JobKey("PrintTimeScheduler"), PrintTimeScheduler.class,
                EVERY_TWO_MINUTE_CRON_EXPRESSION, timeZone);
    }

}