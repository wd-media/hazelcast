package com.example.haszelcast;

import com.example.haszelcast.config.VehicleArchitectureUpdateScheduler;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@Log4j2
@SpringBootApplication
@EnableScheduling
public class HazelcastApplication {

    public static void main(final String[] args) {
        SpringApplication.run(HazelcastApplication.class, args);
    }

    @Bean
    CommandLineRunner runAtStartup(final VehicleArchitectureUpdateScheduler scheduler, final ApplicationContext ctx) {
        return args -> {
            log.info("Application context refreshed. Executing vehicle architecture update task post-startup.");
            try {
                scheduler.executeVehicleArchitectureUpdateTask(true); // Pass true to indicate startup context
            } catch (Exception e) {
                log.error("Fatal error during startup task. Shutting down: ", e);
                SpringApplication.exit(ctx, () -> 1); // Non-zero exit code indicates abnormal shutdown
            }
        };
    }
}
