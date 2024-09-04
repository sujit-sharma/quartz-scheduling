package org.sujit.quartzscheduling.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobKey;
import org.sujit.quartzscheduling.constant.SchedulerConstant;
import org.sujit.quartzscheduling.entity.JobDetails;
import org.sujit.quartzscheduling.quartz.SingleTriggerCronJobScheduler;
import org.sujit.quartzscheduling.service.JobDetailService;

import java.util.TimeZone;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrintTimeSchedulerJobConfigTest {

    private static final String EVERY_TWO_MINUTE_CRON_EXPRESSION = "0 1/2 * * * ?";


    @Mock
    private SingleTriggerCronJobScheduler singleTriggerCronJobScheduler;

    private PrintTimeSchedulerJobConfig printTimeSchedulerJobConfig;

    @Mock
    private JobDetailService jobDetailService;

    @BeforeEach
    void setUp() {
        printTimeSchedulerJobConfig = new PrintTimeSchedulerJobConfig(singleTriggerCronJobScheduler,
                EVERY_TWO_MINUTE_CRON_EXPRESSION, jobDetailService);
    }

    @Test
    void scheduleCorporateUsersDailyJob() throws Exception {
        doNothing().when(jobDetailService).saveOrUpdateJobDetail(any(JobDetails.class));

        TimeZone timeZone = SchedulerConstant.DEFAULT_TIMEZONE;
        printTimeSchedulerJobConfig.schedulePrintTimeSchedulerJob();
        verify(singleTriggerCronJobScheduler).scheduleJob(new JobKey("PrintTimeScheduler"), PrintTimeScheduler.class,
                EVERY_TWO_MINUTE_CRON_EXPRESSION, timeZone);
//        verify(printTimeSchedulerJobConfig, times(1)).saveJobScheduleDetails(anyString());
    }

}