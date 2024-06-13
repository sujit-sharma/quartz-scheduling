package org.sujit.quartzscheduling.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

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
