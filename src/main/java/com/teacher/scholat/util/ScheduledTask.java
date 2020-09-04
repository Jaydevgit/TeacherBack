package com.teacher.scholat.util;

import com.teacher.scholat.controller.StatisticController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    @Autowired
    private StatisticController statisticController;

    @Scheduled(cron = "0 0 0 * * ?")
    public void task1() {
        statisticController.total();
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void taskGetAllPaper() {
        statisticController.getAllPaper();
    }
}
