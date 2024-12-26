package com.example.loginregister.config;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AutowiringSpringJobFactory implements JobFactory {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler){
        Class<? extends Job> jobClass = bundle.getJobDetail().getJobClass();
        return applicationContext.getBean(jobClass);
    }
}
