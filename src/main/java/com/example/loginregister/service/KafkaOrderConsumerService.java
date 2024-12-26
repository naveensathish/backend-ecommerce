package com.example.loginregister.service;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.loginregister.controller.AuthController;
import com.example.loginregister.entity.Order;
import com.example.loginregister.repository.OrderRepository;

@Service
public class KafkaOrderConsumerService {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private OrderRepository orderRepository;

	private static final String REPORT_BASE_DIRECTORY = "C:/reportstemp/Daily Report/";
	private static final String REPORT_DIRECTORY = "C:/order-reports/single_order_data/"; 
	public static final String ORDERS_REPORT_DIRECTORY = "C:/reportstemp/Daily Report/";

	DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private static LocalDateTime lastProcessedCycleEndTime = null;
	private static final int CYCLE_GRACE_PERIOD_SECONDS = 30;

	private static boolean isReportGenerated = false;

	@KafkaListener(topics = "report-generation-topic", groupId = "group_id")
	public void generateReportForOrdersInCurrentCycle(String message) throws IOException {
		try {
			LocalDateTime now = LocalDateTime.now();

			LocalDateTime cycleEndTime = getEndOfCurrentCycle(now);

			if (cycleEndTime.equals(lastProcessedCycleEndTime) && isReportGenerated) {
				logger.info("Cycle " + cycleEndTime.format(customFormatter)
						+ " already processed successfully. Skipping report generation.");
				return;
			}

			long timeToWaitInSeconds = now.until(cycleEndTime, ChronoUnit.SECONDS);
			if (timeToWaitInSeconds > 0) {
				logger.info("Waiting until the cycle end: " + cycleEndTime.format(customFormatter));
				Thread.sleep(timeToWaitInSeconds * 1000);
			}

			lastProcessedCycleEndTime = cycleEndTime;

			LocalDateTime cycleStartTime = cycleEndTime.minusMinutes(15);
			logger.info("Cycle start time: " + cycleStartTime.format(customFormatter));
			logger.info("Cycle end time: " + cycleEndTime.format(customFormatter));
			String cycleStartTimeStr = cycleStartTime.format(customFormatter);
			String cycleEndTimeStr = cycleEndTime.format(customFormatter);
			List<Order> orders = orderRepository.findOrdersByOrderDateBetween(cycleStartTimeStr, cycleEndTimeStr);

			if (orders.isEmpty()) {
				logger.info("No orders found in the current 15-minute cycle.");
				return;
			}

			generateConsolidatedExcelReport(orders, cycleStartTime, cycleEndTime);
			isReportGenerated = true;

		} catch (InterruptedException e) {
			System.err.println("Error during waiting for cycle end: " + e.getMessage());
		} catch (IOException e) { 
			System.err.println("Error during report generation: " + e.getMessage());
			isReportGenerated = false;
		}
	}

	private LocalDateTime getEndOfCurrentCycle(LocalDateTime now) {
		int totalMinutes = now.getHour() * 60 + now.getMinute();

		int nextCycleEnd = (int) Math.ceil(totalMinutes / 15.0) * 15;
		int cycleEndHour = nextCycleEnd / 60;
		int cycleEndMinute = nextCycleEnd % 60;

		return LocalDateTime.of(now.toLocalDate(), LocalTime.of(cycleEndHour, cycleEndMinute, 0));
	}
	
//	private LocalDateTime getEndOfCurrentCycle(LocalDateTime now) {
//	    int totalMinutes = now.getHour() * 60 + now.getMinute(); 
//	    int nextCycleEnd = (int) Math.ceil(totalMinutes / 15.0) * 15;
//
//	    int cycleEndHour = nextCycleEnd / 60;
//	    int cycleEndMinute = nextCycleEnd % 60;
//
//	    // Check if the calculated hour is 24, reset to 0 and adjust the date 
//	    if (cycleEndHour == 24) {
//	        cycleEndHour = 0;
//	        now = now.plusDays(1);  // Move to the next day
//	    }
//
//	    return LocalDateTime.of(now.toLocalDate(), LocalTime.of(cycleEndHour, cycleEndMinute, 0)); 
//	}


	private LocalDateTime getStartOfCurrentCycle(LocalDateTime cycleEndTime) {
		return cycleEndTime.minusMinutes(15);
	}

	private String calculateICNFolderPath(LocalDateTime cycleStartTime) {
		int totalMinutes = cycleStartTime.getHour() * 60 + cycleStartTime.getMinute();

		int nearestCycleEndTime = (int) (Math.ceil(totalMinutes / 15.0) * 15); 
		int icnHour = nearestCycleEndTime / 60;  
		int icnMinute = nearestCycleEndTime % 60;
		return String.format("ICN%02d%02d", icnHour, icnMinute);
	}

	private void generateConsolidatedExcelReport(List<Order> orders, LocalDateTime cycleStartTime,
			LocalDateTime cycleEndTime) throws IOException {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Orders");

			Row headerRow = sheet.createRow(0);
			String[] headers = { "Order ID", "User ID", "Product ID", "Product Name", "Quantity", "Total Amount",
					"Status", "Shipping Address", "Seller ID", "Pin code", "Username", "Email", "Phone Number",
					"Order Date" };
			for (int i = 0; i < headers.length; i++) {
				headerRow.createCell(i).setCellValue(headers[i]);
			}

			int rowNum = 1;
			for (Order order : orders) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(order.getOrderId());
				row.createCell(1).setCellValue(order.getUserId());
				row.createCell(2).setCellValue(order.getProduct_id());
				row.createCell(3).setCellValue(order.getProductName());
				row.createCell(4).setCellValue(order.getQty());
				row.createCell(5).setCellValue(order.getTotalAmount());
				row.createCell(6).setCellValue(order.getStatus());
				row.createCell(7).setCellValue(order.getShippingAddress());
				row.createCell(8).setCellValue(order.getSellerId());
				row.createCell(9).setCellValue(order.getPincode());
				row.createCell(10).setCellValue(order.getUsername());
				row.createCell(11).setCellValue(order.getEmail());
				row.createCell(12).setCellValue(order.getPhonenumber());
				row.createCell(13).setCellValue(order.getOrderDate());
			}

			String formattedTime = cycleStartTime.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
			String uniqueID = UUID.randomUUID().toString().substring(0, 8);
			String fileName = "CONSOLIDATED_ORDER_REPORT_" + formattedTime + "_" + uniqueID + ".xlsx";

			String currentDateStr = cycleStartTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			String cycleFolder = calculateICNFolderPath(cycleStartTime);
			String reportDirectory = REPORT_BASE_DIRECTORY + currentDateStr +"/C1/"+cycleFolder;
			File reportDir = new File(reportDirectory);
			if (!reportDir.exists()) {
				if (reportDir.mkdirs()) {
					logger.info("Created folder structure for cycle report: " + reportDir.getPath()); 
				}
			}
			File excelReportFile = new File(reportDir, fileName);
			try (FileOutputStream fileOut = new FileOutputStream(excelReportFile)) {
				workbook.write(fileOut);
				logger.info("Consolidated Excel Report generated successfully: " + fileName); 
			}

		} catch (IOException e) {
			System.err.println("Error generating Excel report: " + e.getMessage());
		}
	}
}
