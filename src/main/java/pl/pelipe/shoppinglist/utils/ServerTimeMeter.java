package pl.pelipe.shoppinglist.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;

@Component
public class ServerTimeMeter {

    @Scheduled(fixedRate = 1200000)
    public void printTime() {
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        long startTime = runtimeBean.getStartTime();
        Date startDate = new Date(startTime);
        System.out.println("[***INFO***] Server start time = " + startDate);
    }
}