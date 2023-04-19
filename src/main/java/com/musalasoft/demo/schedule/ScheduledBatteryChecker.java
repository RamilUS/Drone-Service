package com.musalasoft.demo.schedule;

import com.musalasoft.demo.repo.DroneRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledBatteryChecker {
    Logger logger = LoggerFactory.getLogger(ScheduledBatteryChecker.class);
    private final DroneRepo droneRepo;

    @Scheduled(fixedDelayString = "${app.delay}")
    public void run() {
        droneRepo.findAll().forEach(drone ->
                logger.info("Battery level of drone:" + drone.getSerialNumber() + " is " + drone.getBatteryLevel() + "%.")
        );
    }


}
