package org.sujit.quartzscheduling.listener;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sujit.quartzscheduling.scheduler.PrintTimeSchedulerJobConfig;

import javax.sql.DataSource;
import java.sql.*;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.sujit.quartzscheduling.constant.JobConstant.PRINT_TIME_JOB_KEY;

/**
 * Author: Sujit Sharma
 * Date:2024-09-04
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseEventListener {

    private static final String LISTEN_CHANNEL = "job_details_update";

    @Autowired
    private DataSource dataSource;

    private final PrintTimeSchedulerJobConfig printTimeSchedulerJobConfig;

//    @Autowired
//    private QuartzJobUpdater quartzJobUpdater;

    private ExecutorService executorService;
    private Future<?> listenFuture;

    @PostConstruct
    public void startListening() {
        executorService = Executors.newSingleThreadExecutor();
        listenFuture = executorService.submit(this::listen);
    }

    @PreDestroy
    public void stopListening() {
        if (listenFuture != null) {
            listenFuture.cancel(true);
        }
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    private void listen() {
        try{ Connection connection = dataSource.getConnection();
             PGConnection pgConnection = connection.unwrap(PGConnection.class);
             Statement statement = connection.createStatement();

            statement.execute("LISTEN " + LISTEN_CHANNEL);

            while (!Thread.currentThread().isInterrupted()) {
                PGNotification[] notifications = pgConnection.getNotifications();
                if (notifications != null) {
                    for (PGNotification notification : notifications) {
                        handleNotification(notification);
                    }
                }

                // Sleep to reduce CPU usage
                Thread.sleep(1000);
            }

        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleNotification(PGNotification notification) {
        log.info("Notification handled {}", notification);
        StringTokenizer stringTokenizer = new StringTokenizer(notification.getParameter(), ",");
        String jobKey = stringTokenizer.nextToken();
        if (PRINT_TIME_JOB_KEY.equals(jobKey)) {
            printTimeSchedulerJobConfig.setPrintTimeSchedulerCornExpression(stringTokenizer.nextToken());
            printTimeSchedulerJobConfig.schedulePrintTimeSchedulerJob();
        }

//        quartzJobUpdater.updateJobs();
    }
}
