package org.sujit.quartzscheduling.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.sujit.quartzscheduling.constant.SchedulerConstant;
import org.sujit.quartzscheduling.quartz.SingleTriggerCronJobScheduler;

@Component
@Slf4j
@DisallowConcurrentExecution
public class DoLogScheduler  implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Logger Job get executed");
        log.info("Do log related task here !!");
    }
}

@Configuration
class DoLogSchedulerJobConfig {
    private final SingleTriggerCronJobScheduler singleTriggerCornJobScheduler;
    private final String logSchedulerCornExpression;

    public DoLogSchedulerJobConfig(SingleTriggerCronJobScheduler singleTriggerCornJobScheduler,
                                   @Value("${quartz.logScheduler.expression: 0 * * * * ?}") String logSchedulerCornExpression
                                   ) {
        this.singleTriggerCornJobScheduler = singleTriggerCornJobScheduler;
        this.logSchedulerCornExpression = logSchedulerCornExpression;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void scheduleLogSchedulerJob() throws SchedulerException {
        singleTriggerCornJobScheduler.scheduleJob(JobKey.jobKey("DoLogScheduler"),
                DoLogScheduler.class,
                logSchedulerCornExpression,
                SchedulerConstant.DEFAULT_TIMEZONE);
    }
}
