package org.sujit.quartzscheduling.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author: Sujit Sharma
 * Date:2024-09-03
 */

@Entity(name = "job_details")
@Getter
@Setter
public class JobDetails implements Serializable {

    @Id
    private String jobKey;

    private String expression;

}
