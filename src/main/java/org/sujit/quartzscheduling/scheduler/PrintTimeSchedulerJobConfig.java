package org.sujit.quartzscheduling.scheduler;

import lombok.Setter;
import lombok.SneakyThrows;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.sujit.quartzscheduling.constant.SchedulerConstant;
import org.sujit.quartzscheduling.quartz.SingleTriggerCronJobScheduler;

@Component
public class PrintTimeSchedulerJobConfig {

    private final SingleTriggerCronJobScheduler singleTriggerCornJobScheduler;
    @Setter
    private String printTimeSchedulerCornExpression;

    public PrintTimeSchedulerJobConfig(SingleTriggerCronJobScheduler singleTriggerCornJobScheduler,
                                   @Value("0 /5 * * * ?") String logSchedulerCornExpression
                                   ) {
        this.singleTriggerCornJobScheduler = singleTriggerCornJobScheduler;
        this.printTimeSchedulerCornExpression = logSchedulerCornExpression;
    }


    @SneakyThrows
    @EventListener(ApplicationReadyEvent.class)
    public void schedulePrintTimeSchedulerJob() {
        singleTriggerCornJobScheduler.scheduleJob(JobKey.jobKey("PrintTimeScheduler"),
                PrintTimeScheduler.class,
                printTimeSchedulerCornExpression,
                SchedulerConstant.DEFAULT_TIMEZONE);
    }
}
