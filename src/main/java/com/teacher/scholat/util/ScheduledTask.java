package com.teacher.scholat.util;

import com.teacher.scholat.controller.DeduplicationController;
import com.teacher.scholat.controller.StatisticController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    @Autowired
    private StatisticController statisticController;

    @Autowired
    private DeduplicationController seduplicationController;

    @Scheduled(cron = "0 0 0 * * ?")
    public void task1() {
        statisticController.total();
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void taskGetAllPaper() {
        statisticController.getAllPaper();
    }

    @Scheduled(cron = "0 20 1 * * ?")
    public void taskGetAllProject() {
        statisticController.getAllProject();
    }

    @Scheduled(cron = "0 40 1 * * ?")
    public void taskGetAllPatent() {
        statisticController.getAllPatent();
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void taskGetAllPublication() {
        statisticController.getAllPublication();
    }

    //论文定时去重,每周凌晨3点执行一次
    @Scheduled(cron = "0 0 3 ? * MON")
    public void taskDeduplicationPaper() {
        seduplicationController.deduplicationPaper();
    }

    //项目定时去重
    @Scheduled(cron = "0 20 3 ? * MON")
    public void taskDeduplicationProject() {
        seduplicationController.deduplicationProject();
    }

    //专利定时去重
    @Scheduled(cron = "0 40 3 ? * MON")
    public void taskDeduplicationPatent() {
        seduplicationController.deduplicationPatent();
    }

    //著作定时去重
    @Scheduled(cron = "0 45 10 * * ?")
    public void taskDeduplicationPublication() {
        seduplicationController.deduplicationPublication();
    }
}
