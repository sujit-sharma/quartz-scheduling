package org.sujit.quartzscheduling.rest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class SchedulerExpressionDto {

    private String expression;
}
