package org.sujit.quartzscheduling.scheduler;

import lombok.Setter;
import lombok.SneakyThrows;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.sujit.quartzscheduling.constant.SchedulerConstant;
import org.sujit.quartzscheduling.entity.JobDetails;
import org.sujit.quartzscheduling.quartz.SingleTriggerCronJobScheduler;
import org.sujit.quartzscheduling.service.JobDetailService;

import static org.sujit.quartzscheduling.constant.JobConstant.PRINT_TIME_JOB_KEY;

@Component
public class PrintTimeSchedulerJobConfig {


    private final SingleTriggerCronJobScheduler singleTriggerCornJobScheduler;

    private final JobDetailService jobDetailService;
    @Setter
    private String printTimeSchedulerCornExpression;

    public PrintTimeSchedulerJobConfig(SingleTriggerCronJobScheduler singleTriggerCornJobScheduler,
                                   @Value("0 /5 * * * ?") String logSchedulerCornExpression, JobDetailService jobDetailService
                                   ) {
        this.singleTriggerCornJobScheduler = singleTriggerCornJobScheduler;
        this.printTimeSchedulerCornExpression = logSchedulerCornExpression;
        this.jobDetailService = jobDetailService;
    }


    @SneakyThrows
    @EventListener(ApplicationReadyEvent.class)
    public void schedulePrintTimeSchedulerJob() {
        saveJobScheduleDetails(printTimeSchedulerCornExpression);
        singleTriggerCornJobScheduler.scheduleJob(JobKey.jobKey(PRINT_TIME_JOB_KEY),
                PrintTimeScheduler.class,
                printTimeSchedulerCornExpression,
                SchedulerConstant.DEFAULT_TIMEZONE);
    }

    public void saveJobScheduleDetails(String printTimeSchedulerCornExpression) {
        JobDetails jobDetails = new JobDetails();
        jobDetails.setJobKey(PRINT_TIME_JOB_KEY);
        jobDetails.setExpression(printTimeSchedulerCornExpression);
        jobDetailService.saveOrUpdateJobDetail(jobDetails);
    }
}
