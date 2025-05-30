package com.example.demo.utilities;

import java.io.IOException;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

/*
 *@Author: Neha Verma
  @Desc: This listeners class enables report generation, execution logging and UI locator repo loading
   at different events of test cycle.
 */

public class TestListeners implements ITestListener {
	public static ExtentReports extentReport;
	public static ExtentTest extentLogger;
	/** The logger. */
	static Logger logger;
	static WebDriver driver;
	
	public void onTestStart(ITestResult result) {
		extentLogger = extentReport.createTest(result.getName());
	}

	public void onTestSuccess(ITestResult result) {
		extentReport.flush();
	}

	public void onTestFailure(ITestResult result) {
		extentReport.flush();
	}
	
	public void onTestSkipped(ITestResult result) {
		extentLogger.skip("Test was skipped because of an explicit failure.");
		extentReport.flush();
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
	}
	

	public void onStart(ITestContext context) {
		logger = Logger.getLogger("logs");
		Appender fh = null;
		try {
			fh = new FileAppender(new SimpleLayout(), "logfile.log");
			logger.addAppender(fh);
			fh.setLayout(new SimpleLayout());

		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		extentReport = ExtentReportManager.getExtent();
		String user_dir = System.getProperty("user.dir");

		// xml file path when executing from batch file
		String providedPath = System.getProperty("LocatorFile");

		if (providedPath != null) {

			user_dir = user_dir.replace("bin", "");
		}		 
		ObjectHandler.appLocators = new LocatorReader("locators.xml");
	}

	public void onFinish(ITestContext context) {
		extentReport.flush();
	}

}
