package com.example.demo.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import org.apache.log4j.Logger;

public class FileHandler {
	/** The logger. */
	static Logger logger = Logger.getLogger(FileHandler.class);
	Properties prop;
	public static String globalDownloadPath = "";

	public FileHandler() {
		prop = JavaUtilities.loadPropertiesFile("config.properties");
	}

	// Method to check if a file is prent in download folder
	public boolean isFilePresent(String qoName) {
		boolean flag = false;
		try {
			String downloadPath = globalDownloadPath;
			File folder = new File(downloadPath);
			File[] listOfFiles = folder.listFiles();
			logger.info("------Path-----:::: " + downloadPath);
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].getName().equalsIgnoreCase(qoName + ".xlsx")) {
					flag = true;
				}
			}
		} catch (Exception e) {
			logger.info("Error in verifying if file is present" + e);
		}
		return flag;
	}

	// Method to move a file from one location to another
	public static void moveFile(String source, String destination) {
		try {
			Path temp = Files.move(Paths.get(source), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
			logger.info("------Source Path----- " + source);
			TestListeners.extentLogger.pass("------Source Path----- " + source);
			logger.info("------Destination Path-----" + destination);
			TestListeners.extentLogger.pass("------Destination Path-----" + destination);
			if (temp != null) {
				logger.info("Successfully file moved to " + destination);
				TestListeners.extentLogger.pass("Successfully file moved to " + destination);
			} else {
				logger.error("Failed to move the file");
				TestListeners.extentLogger.fail("Failed to move file");
			}
		} catch (IOException e) {
			logger.error("Failed to move the file " + e);
		}
	}
	
	// Method to copy a file from one location to another
	public static void copyFile(String source, String destination) {
		try {
			Path temp = Files.copy(Paths.get(source), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
			logger.info("------Source Path----- " + source);
			TestListeners.extentLogger.pass("------Source Path----- " + source);
			logger.info("------Destination Path-----" + destination);
			TestListeners.extentLogger.pass("------Destination Path-----" + destination);
			if (temp != null) {
				logger.info("Successfully file copied to " + destination);
				TestListeners.extentLogger.pass("Successfully file copied to " + destination);
			} else {
				logger.error("Failed to copy the file");
				TestListeners.extentLogger.fail("Failed to copy file");
			}
		} catch (IOException e) {
			logger.error("Failed to copy the file " + e);
		}
	}

	public static void checkAndCreateDirectory(String downloadPath) {
		try {
			File dir = new File(downloadPath);
			boolean dirExists = dir.exists();
			if (dirExists) {
				logger.info("Download Directory already exists: " + downloadPath);
				globalDownloadPath = downloadPath;
			} else {
				new File(downloadPath).mkdir();
				if (new File(downloadPath).exists()) {
					logger.info("Directory created: " + downloadPath);
					globalDownloadPath = downloadPath;
				} else {
					String userDir = System.getProperty("user.dir");
					String path = userDir + "\\AutomationDownloads\\";
					File d = new File(path);
					if (d.exists()) {
						logger.info("Download Directory already exists: " + path);
						globalDownloadPath = path;
					} else {
						d.mkdir();
						if (d.exists()) {
							logger.info("Directory created: " + path);
							globalDownloadPath = path;
						} else {
							logger.error("Failed to create directory: " + path);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Failed to check and create directory, " + e);
		}
	}

	public static void compareTwoFiles(String fileName, String actualFilePath, String expectedFilePath) {
		try {
			String row1, row2;
			BufferedReader fileReader1, fileReader2;
			int counter = 1;
			boolean flag = false;
			fileReader1 = new BufferedReader(new FileReader(actualFilePath + "\\" + fileName));
			fileReader2 = new BufferedReader(new FileReader(expectedFilePath + "\\" + fileName));
			logger.info("Comparing File - " + fileName);
			TestListeners.extentLogger.info("Comparing File - " + fileName);
			while ((row1 = fileReader1.readLine()) != null && (row2 = fileReader2.readLine()) != null) {
				if (row1.equalsIgnoreCase(row2)) {
					flag = true;
				} else {
					logger.info("Reported Failure :");
					logger.info("Expected data: " + row2);
					logger.info("Actual data: " + row1);
					flag = false;
					break;
				}
				counter++;
			}
			fileReader1.close();
			fileReader2.close();
			if (flag) {
				logger.info("Successfully compared all " + counter + " rows of file: " + fileName);
				TestListeners.extentLogger.pass("Successfully compared all " + counter + " rows of file: " + fileName);
			} else {
				logger.error("Failed to compare file " + fileName);
				TestListeners.extentLogger.fail("Failed to compare file " + fileName);
			}
		} catch (Exception e) {
			logger.error("Error in comparing file, STACK TRACE: " + e);
			TestListeners.extentLogger.fail("Error in comparing file, STACK TRACE: " + e);
		}
	}
	
	public static String createFolder(String folderPath, String folderName) {
		String folderLocation = "";
		try {
			folderLocation = folderPath + folderName;
			boolean file = new File(folderLocation).mkdir();
			if (file) {
				return folderLocation;
			}
		} catch (Exception e) {
			logger.error("Error in creting folder, STACK TRACE: " + e);
			TestListeners.extentLogger.fail("Error in creting folder, STACK TRACE: " + e);
		}
		return folderLocation;
	}

}
