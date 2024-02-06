package com.example.haszelcast.config;

import com.example.haszelcast.service.TimeService;
import com.example.haszelcast.service.VehicleArchitectureService;
import com.example.haszelcast.service.VehicleArchitectureServiceImpl;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.lock.FencedLock;
import com.hazelcast.map.IMap;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Log4j2
@Component
public class VehicleArchitectureUpdateScheduler {

    private static final String VEH_ARCHITECTURE_TASK_5_SECONDS_CRON = "0/5 * * * * ?";
    private static final String VEH_ARCHITECTURE_TASK_MIDNIGHT_EVERY_DAY_CRON = "0 0 0 * * ?";

    private static final String VEH_ARCHITECTURE_LOCK = "vehicleArchitectureLock";
    private static final String VEH_ARCHITECTURE_TASK_TRACKER = "vehicleArchitectureTaskTracker";
    private static final String VEH_ARCHITECTURE_TASK_TRACKER_KEY = "vehicleArchitectureTaskTrackerKey";
    private static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final TimeService timeService;
    private final HazelcastInstance hazelcastInstance;
    private final VehicleArchitectureService vehicleArchitectureService;

    public VehicleArchitectureUpdateScheduler(final HazelcastInstance hazelcastInstance,
                                              final VehicleArchitectureService vehicleArchitectureService,
                                              final TimeService timeService) {
        this.hazelcastInstance = hazelcastInstance;
        this.vehicleArchitectureService = vehicleArchitectureService;
        this.timeService = timeService;
    }

    @Scheduled(cron = VEH_ARCHITECTURE_TASK_5_SECONDS_CRON)
//    @Scheduled(cron = VEH_ARCHITECTURE_TASK_1_DAY_CRON)
    public void executeVehicleArchitectureUpdateTask() {
        executeVehicleArchitectureUpdateTask(false);
    }

    public void executeVehicleArchitectureUpdateTask(boolean isStartup) {

        final SimpleDateFormat dateFormat = new SimpleDateFormat(ISO_8601_DATE_FORMAT);
        final String currentTime = dateFormat.format(new Date());
        log.info("Vehicle Architecture Update Task started at {}.", currentTime);

        final Map<String, String> vehicleArchitectureUpdates = this.vehicleArchitectureService.retrieveVehicleArchitectureMap();

        final FencedLock lock = this.hazelcastInstance.getCPSubsystem().getLock(VEH_ARCHITECTURE_LOCK);
        boolean locked = false;

        try {
            // Attempt to acquire the lock
            log.debug("Attempt to acquire the lock {}", VEH_ARCHITECTURE_LOCK);

            locked = lock.tryLock();
            if (!locked) {
                log.info("Could not acquire lock, another task is already running.");
                return; // Exit if the lock is not acquired, indicating another task is in progress.
            }

            log.debug("Lock successfully acquired, execute the task.");

            executeUpdateTask(vehicleArchitectureUpdates);

        } catch (Exception e) {
            if (isStartup) {
                throw new VehicleArchitectureUpdateSchedulerException("Failed to execute startup task, shutting down.", e);
            } else {
                log.error("An error occurred during the scheduled execution of the vehicle architecture update task: ", e);
            }
        } finally {
            if (locked) {
                try {
                    lock.unlock();
                    log.debug("Lock released successfully.");
                } catch (Exception e) {
                    log.error("Failed to release lock: ", e);
                }
            }
        }
    }

    private void executeUpdateTask(final Map<String, String> updates) {

        final IMap<String, Date> taskTrackerMap = this.hazelcastInstance.getMap(VEH_ARCHITECTURE_TASK_TRACKER);
        final Date lastExecution = taskTrackerMap.get(VEH_ARCHITECTURE_TASK_TRACKER_KEY);

        final Date now = timeService.now();
        if (lastExecution == null || !this.timeService.isWithinTimeWindow(lastExecution, now)) {
            log.debug("Updating vehicle architecture map : {}", updates);
            this.vehicleArchitectureService.updateVehicleArchitectureMap(updates);
            taskTrackerMap.put(VEH_ARCHITECTURE_TASK_TRACKER_KEY, now);
        } else {
            log.debug("Task execution skipped as it falls within the same 5-second window as the last execution.");
        }

    }

}
