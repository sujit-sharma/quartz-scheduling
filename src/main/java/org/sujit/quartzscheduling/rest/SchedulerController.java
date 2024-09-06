package org.sujit.quartzscheduling.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sujit.quartzscheduling.entity.JobDetails;
import org.sujit.quartzscheduling.scheduler.PrintTimeSchedulerJobConfig;
import org.sujit.quartzscheduling.service.JobDetailService;

import static org.sujit.quartzscheduling.constant.JobConstant.PRINT_TIME_JOB_KEY;

@RestController
@RequestMapping("scheduler")
@Slf4j
@RequiredArgsConstructor
public class SchedulerController {

    private final PrintTimeSchedulerJobConfig printTimeSchedulerJobConfig;

    private final JobDetailService jobDetailService;

    @PostMapping("/update")
    public ResponseEntity updateScheduler(@RequestBody SchedulerExpressionDto expressionDto) {
        printTimeSchedulerJobConfig.setPrintTimeSchedulerCornExpression(expressionDto.getExpression());
        printTimeSchedulerJobConfig.schedulePrintTimeSchedulerJob();

        JobDetails jobDetails = new JobDetails();
        jobDetails.setJobKey(PRINT_TIME_JOB_KEY);
        jobDetails.setExpression(expressionDto.getExpression());

        jobDetailService.saveOrUpdateJobDetail(jobDetails);

        return ResponseEntity.ok().build();
    }
}
