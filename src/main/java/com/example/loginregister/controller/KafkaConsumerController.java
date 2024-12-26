package com.example.loginregister.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.loginregister.service.KafkaOrderConsumerService;

@RestController
@CrossOrigin(origins = { "http://localhost:5000", "http://localhost:5001", "http://localhost:3000",
		"http://localhost:3001" })
@RequestMapping("/api/report")
public class KafkaConsumerController {

	private static final Logger logger = LoggerFactory.getLogger(SellerController.class);

	@Autowired
	private KafkaOrderConsumerService kafkaOrderConsumerService;

	@GetMapping("/list")
	public ResponseEntity<List<String>> listFiles(@RequestParam(required = false) String date,
			@RequestParam(required = false) String icn) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String currentDate = date != null && !date.isEmpty() ? convertDateToYYYYMMDD(date) : sdf.format(new Date());
		File directory = new File(kafkaOrderConsumerService.ORDERS_REPORT_DIRECTORY + "/" + currentDate);

		logger.info("\n\nDirectory path: " + directory.getAbsolutePath());
		logger.info("Received date: " + date);
		logger.info("Received ICN: " + icn);

		if (!directory.exists() || !directory.isDirectory()) {
			logger.error("Directory does not exist or is not a valid directory.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonList("Reports directory not found."));
		}

		File c1Directory = new File(directory, "C1");
		if (!c1Directory.exists() || !c1Directory.isDirectory()) {
			logger.error("No C1 directory found under: " + directory.getAbsolutePath());
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonList("C1 directory not found."));
		}

		if (icn != null && !icn.isEmpty()) {
			logger.info("Filtering by ICN: " + icn);
			File icnDirectory = new File(c1Directory, icn);
			if (!icnDirectory.exists() || !icnDirectory.isDirectory()) {
				logger.error("ICN directory not found for: " + icn);
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Collections.singletonList("No reports found for the specified ICN."));
			}
			logger.info("Found ICN Directory: " + icnDirectory.getAbsolutePath());
		}

		List<File> allFiles = listFilesRecursively(c1Directory);
		logger.info("Searching for .xlsx files...");

		if (date != null && !date.isEmpty()) {
			logger.info("Filtering files with date: " + date);
			String formattedDate = convertDateToYYYYMMDD(date);
			if (formattedDate != null) {
				allFiles = allFiles.stream()
	                    .filter(file -> file.getName().contains(formattedDate) && (icn == null || file.getAbsolutePath().contains(icn)))
	                    .collect(Collectors.toList());
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Collections.singletonList("Invalid date format."));
			}
		}
 
		List<String> fileNamesList = allFiles.stream().map(file -> sanitizeFileName(file.getName()))
				.collect(Collectors.toList());

		if (!fileNamesList.isEmpty()) {
			return ResponseEntity.ok(fileNamesList);
		} else {
			logger.info("No reports found for the selected filters.");
			return ResponseEntity.ok(new ArrayList<>());
		}
	}
 
	private List<File> listFilesRecursively(File dir) {
		List<File> fileList = new ArrayList<>();
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					fileList.addAll(listFilesRecursively(file));
				} else if (file.getName().endsWith(".xlsx")) {
					fileList.add(file);
				}
			}
		}
		return fileList;
	}

	private String sanitizeFileName(String fileName) {
		fileName = fileName.trim();
		fileName = fileName.replaceAll("^~\\$+", "");
		fileName = fileName.replaceAll("[^a-zA-Z0-9_\\-\\.]", "_");
		return fileName;
	}

	public String convertDateToYYYYMMDD(String date) {
		try {
			SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
			Date parsedDate = inputFormat.parse(date);
			return outputFormat.format(parsedDate);
		} catch (Exception e) {
			logger.error("Invalid date format: " + date, e);
			return null;
		}
	}

	@GetMapping("/download/{icn}/{fileName}")
	public ResponseEntity<Resource> downloadExcelReport(@PathVariable String icn, @PathVariable String fileName,
			@RequestParam(required = false) String date) throws IOException {

		String formattedDate = (date != null && !date.isEmpty()) ? convertDateToYYYYMMDD(date) : null;

		String fullPath = kafkaOrderConsumerService.ORDERS_REPORT_DIRECTORY + "/"
				+ (formattedDate != null ? formattedDate + "/" : "") + "C1/" + icn + "/" + fileName;

		logger.info("\n\nFull file path for download: " + fullPath);

		File file = new File(fullPath);

		if (!file.exists()) {
			logger.error("File not found: " + fullPath);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		Path path = file.toPath();
		Resource resource = new UrlResource(path.toUri());

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
				.body(resource); 
	}

}