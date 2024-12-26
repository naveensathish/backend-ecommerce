package com.example.loginregister;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.loginregister.service.FileExportService;
import com.example.loginregister.service.SellerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ReportGenerator {
	private static final Logger logger = LoggerFactory.getLogger(SellerService.class);

	@Autowired
	private FileExportService fileExportService;

	@Scheduled(cron = "0 32 01 * * ?")
	public void generateReport() {
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
