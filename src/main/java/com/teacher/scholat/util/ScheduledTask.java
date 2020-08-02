package com.teacher.scholat.util;

import com.teacher.scholat.controller.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    @Autowired
    private Statistic statistic;

    @Scheduled(cron = "0 0 16 ? * *")
    public void task1() {
      statistic.total();
    }
}
