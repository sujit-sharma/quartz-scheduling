package org.sujit.quartzscheduling.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sujit.quartzscheduling.entity.JobDetails;
import org.sujit.quartzscheduling.repository.JobDetailRepository;

/**
 * Author: Sujit Sharma
 * Date:2024-09-03
 */
@Service
@RequiredArgsConstructor
public class JobDetailService {

    private final JobDetailRepository jobDetailRepository;

    public void saveOrUpdateJobDetail(JobDetails jobDetails) {

        jobDetailRepository.save(jobDetails);
        
        // Implement logic to save or update job details
    }

//    private JobDetails findByJobKey(String jobKey) {
//        return jobDetailRepository.findById(jobKey).ifPresent(jobDetails -> {});
//    }

}
