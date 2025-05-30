package com.example.demo.utilities;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;

import org.apache.log4j.Logger;

class CheckLogging implements Runnable {
	static Logger logger = Logger.getLogger(CheckLogging.class);

	public void run() {
		try {
			int getInitialLogLine, getFinalLogLine;
			do {
				getInitialLogLine = 0;
				getInitialLogLine = getLineNo();
				// Waiting for 12 minutes
				Thread.sleep(720000);
				getFinalLogLine = 0;
				getFinalLogLine = getLineNo();
				if (getFinalLogLine > getInitialLogLine) {
				} else {
					Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
					Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
					TestListeners.extentLogger.warning(
							"The execution took more that 12 minutes, so we're explicitly ending the driver instance");
					ExtentReportManager.extent.flush();
				}
			} while (getFinalLogLine > getInitialLogLine);
		} catch (Exception e) {
			logger.error("Error in checking logging, STACK TRACE: " + e);
		}
	}

	public int getLineNo() {
		int lineNumber = 0;
		try {
			File file = new File("./logs/logs.log");
			if (file.exists()) {
				FileReader fr = new FileReader(file);
				LineNumberReader lnr = new LineNumberReader(fr);
				while (lnr.readLine() != null) {
					lineNumber++;
				}
				lnr.close();
				fr.close();
			} else {
				logger.error("File does not exists!");
			}
		} catch (Exception e) {
			logger.error("Error in getting line number, STACK TRACE: " + e);
		}
		return lineNumber;
	}

}
