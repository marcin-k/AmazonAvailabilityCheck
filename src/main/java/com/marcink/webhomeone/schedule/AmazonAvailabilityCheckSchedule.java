package com.marcink.webhomeone.schedule;

import com.marcink.webhomeone.service.AmazonAvailabilityService;
import com.marcink.webhomeone.service.SMSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

//@Component
public class AmazonAvailabilityCheckSchedule {

    private static Logger logger = LoggerFactory.getLogger(AmazonAvailabilityCheckSchedule.class);

    @Autowired
    SMSService smsService;

    @Autowired
    AmazonAvailabilityService amazonAvailabilityService;

    @Scheduled(fixedRate = 30000)
    public void scheduleFixedRateTask() {

        try{
            Random rn = new Random();
            int secondsBetweenRequests = rn.nextInt(120000) + 60000;
            Thread.sleep(secondsBetweenRequests);
            System.gc();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            if (!amazonAvailabilityService.checkPS5AvailabilityOnAmazon()){
//                smsService.sendText("PS5 Available on amazon now! - "+dtf.format(now));
                logger.info("test");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
