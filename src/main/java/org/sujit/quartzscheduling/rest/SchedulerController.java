package org.sujit.quartzscheduling.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sujit.quartzscheduling.scheduler.PrintTimeSchedulerJobConfig;

@RestController
@RequestMapping("scheduler")
@Slf4j
@RequiredArgsConstructor
public class SchedulerController {

    private final PrintTimeSchedulerJobConfig printTimeSchedulerJobConfig;

    @PostMapping("/update")
    public ResponseEntity updateScheduler(@RequestBody SchedulerExpressionDto expressionDto) {
        printTimeSchedulerJobConfig.setPrintTimeSchedulerCornExpression(expressionDto.getExpression());

        printTimeSchedulerJobConfig.schedulePrintTimeSchedulerJob();

        return ResponseEntity.ok().build();
    }
}
