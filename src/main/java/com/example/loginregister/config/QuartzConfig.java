package com.example.loginregister.config;

import static org.quartz.CronScheduleBuilder.cronSchedule;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.example.loginregister.jobs.ReportJob;

@Configuration
public class QuartzConfig {

	@Bean
	public JobDetail reportJobDetail() {
		return JobBuilder.newJob(ReportJob.class).withIdentity("reportJob").storeDurably().build();
	}

	@Bean
	public Trigger reportJobTrigger() {
		return TriggerBuilder.newTrigger().forJob(reportJobDetail()).withIdentity("reportJobTrigger")
				.withSchedule(cronSchedule("0 30 1 * * ?")).build();
	}

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(AutowiringSpringJobFactory jobFactory) {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setJobFactory(jobFactory);
		return factory;
	}
}
