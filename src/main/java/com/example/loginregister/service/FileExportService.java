package com.example.loginregister.service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.loginregister.repository.CartItemRepository;
import com.example.loginregister.repository.ProductRepository;
import com.example.loginregister.repository.SellerRepository;
import com.example.loginregister.repository.StoredImageRepository;
import com.opencsv.CSVWriter;

@Service
public class FileExportService {

	private static final Logger logger = LoggerFactory.getLogger(FileExportService.class);
	private static final String BASE_DIRECTORY = System.getProperty("user.home") + "/Desktop/reports/";

	@Autowired
	private SellerRepository sellerRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private StoredImageRepository storedImageRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	public byte[] exportSellerData(long sizeLimitInBytes, String format) throws Exception {
		return exportDataIfExceedsLimit(sizeLimitInBytes, format, "seller");
	}

	public byte[] exportProductData(long sizeLimitInBytes, String format) throws Exception {
		return exportDataIfExceedsLimit(sizeLimitInBytes, format, "product");
	}

	public byte[] exportStoredImageData(long sizeLimitInBytes, String format) throws Exception {
		return exportDataIfExceedsLimit(sizeLimitInBytes, format, "storedImage");
	}

	public byte[] exportCartItemData(long sizeLimitInBytes, String format) throws Exception {
		return exportDataIfExceedsLimit(sizeLimitInBytes, format, "cartItem");
	}

	public byte[] exportDataIfExceedsLimit(long sizeLimitInBytes, String format, String repoName) throws Exception {
		List<?> entities = fetchEntities(repoName);

		if (entities.size() <= 1000000) {
			logger.info("\n\n10 records found in {}. No report generated.", repoName);
			return new byte[0];
		}

		logger.info("\n\nMore than 10 records found in {}. Generating report.\n\n", repoName);
		byte[] fileContent = generateFile(format, entities);
		String filename = generateFilename(repoName + "_report", format.equalsIgnoreCase("excel") ? "xlsx" : "csv");
		saveFile(fileContent, filename);

		logger.info("\n\nReport generated successfully for {}: {} with size {} bytes.", repoName, filename,
				fileContent.length);

		return fileContent;
	}

	private List<?> fetchEntities(String repoName) {
		switch (repoName.toLowerCase()) {
		case "seller":
			List<?> sellers = sellerRepository.findAll();
			logger.info("\n\nFetched {} records from seller repository.", sellers.size());
			return sellers;
		case "product":
			List<?> products = productRepository.findAll();
			logger.info("\n\nFetched {} records from product repository.", products.size());
			return products;
		case "storedImage":
			List<?> storedImages = storedImageRepository.findAll();
			logger.info("\n\nFetched {} records from stored image repository.", storedImages.size());
			return storedImages;
		case "cartItem":
			List<?> cartItems = cartItemRepository.findAll();
			logger.info("\n\nFetched {} records from cart item repository.", cartItems.size());
			return cartItems;
		default:
			throw new IllegalArgumentException("\n\nUnsupported repository: " + repoName);
		}
	}

	public long countEntities(String repoName) {
		switch (repoName.toLowerCase()) {
		case "seller":
			return sellerRepository.count();
		case "product":
			return productRepository.count();
		case "storedImage":
			return storedImageRepository.count();
		case "cartItem":
			return cartItemRepository.count();
		default:
			throw new IllegalArgumentException("\n\nUnsupported repository: " + repoName);
		}
	}

	private byte[] generateFile(String format, List<?> entities) throws Exception {
		switch (format.toLowerCase()) {
		case "excel":
			return generateExcel(entities);
		case "csv":
			return generateCSV(entities);
		default:
			throw new IllegalArgumentException("\n\nUnsupported format: " + format);
		}
	}

	private void saveFile(byte[] fileContent, String filename) throws Exception {
		Path path = Paths.get(BASE_DIRECTORY + filename);
		Files.createDirectories(path.getParent());
		Files.write(path, fileContent);
		logger.info("\n\nFile saved to {}", path);
	}

	private byte[] generateExcel(List<?> entities) throws Exception {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet(getEntityName(entities));
			createExcelHeader(sheet, entities.get(0));

			int rowNum = 1;
			for (Object entity : entities) {
				Row row = sheet.createRow(rowNum++);
				createExcelRow(entity, row);
			}

			try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
				workbook.write(out);
				logger.info("\n\nExcel file generated successfully with {} records.", entities.size());
				return out.toByteArray();
			}
		}
	}

	private void createExcelHeader(Sheet sheet, Object entity) {
		Row headerRow = sheet.createRow(0);
		var fields = entity.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			headerRow.createCell(i).setCellValue(fields[i].getName());
		}
	}

	private void createExcelRow(Object entity, Row row) {
		var fields = entity.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			try {
				Object value = fields[i].get(entity);
				row.createCell(i).setCellValue(value != null ? value.toString() : "");
			} catch (IllegalAccessException e) {
				logger.error("\n\nFailed to access field value.", e);
			}
		}
	}

	private byte[] generateCSV(List<?> entities) throws Exception {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				CSVWriter writer = new CSVWriter(new OutputStreamWriter(out))) {

			createCSVHeader(writer, entities.get(0));
			for (Object entity : entities) {
				createCSVRow(writer, entity);
			}

			logger.info("\n\nCSV file generated successfully with {} records.", entities.size());
			return out.toByteArray();
		}
	}

	private void createCSVHeader(CSVWriter writer, Object entity) {
		var fields = entity.getClass().getDeclaredFields();
		String[] header = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			header[i] = fields[i].getName();
		}
		writer.writeNext(header);
	}

	private void createCSVRow(CSVWriter writer, Object entity) {
		var fields = entity.getClass().getDeclaredFields();
		String[] data = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			try {
				Object value = fields[i].get(entity);
				data[i] = value != null ? value.toString() : "";
			} catch (IllegalAccessException e) {
				logger.error("Failed to access field value.", e);
			}
		}
		writer.writeNext(data);
	}

	public String generateFilename(String prefix, String extension) {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		return prefix + "_" + timestamp + "." + extension;
	}

	private String getEntityName(List<?> entities) {
		return entities.get(0).getClass().getSimpleName() + "s";
	}
}
