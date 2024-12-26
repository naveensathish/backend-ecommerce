package com.example.loginregister.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.example.loginregister.service.FileExportService;
import com.example.loginregister.service.SellerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ReportJob implements Job {
	private static final Logger logger = LoggerFactory.getLogger(SellerService.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		FileExportService fileExportService = applicationContext.getBean(FileExportService.class);

		String repoName = "seller";

		try {
			long entityCount = fileExportService.countEntities(repoName);

			if (entityCount > 10) {
				byte[] report = fileExportService.exportDataIfExceedsLimit(0, "excel", repoName);
				logger.info("\n\nReport generation attempted for " + entityCount + " records.\n\n");
			} else {
				logger.info("\n\nNot enough data to generate a report. Only " + entityCount + " records found.");
			}
		} catch (Exception e) {
			System.err.println("Error generating report: " + e.getMessage());
		}
	}
}
