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

import java.time.ZonedDateTime;

@Component
@Slf4j
@DisallowConcurrentExecution
public class PrintTimeScheduler implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Time right now is {}", ZonedDateTime.now());
        log.info("Do log related task here !!");
    }
}
