package org.sujit.quartzscheduling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sujit.quartzscheduling.entity.JobDetails;

/**
 * Author: Sujit Sharma
 * Date:2024-09-03
 */
@Repository
public interface JobDetailRepository extends JpaRepository<JobDetails, String> {
}
