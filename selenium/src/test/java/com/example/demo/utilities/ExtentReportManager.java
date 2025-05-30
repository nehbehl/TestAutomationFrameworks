package com.example.demo.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

/*
 *@Author: Neha Verma
  *@Desc: Class to handle Extent Reporting
 */

public class ExtentReportManager {

	public static ExtentReports extent;
	public static ExtentTest test;
	private static ExtentHtmlReporter htmlReporter;
	public static String reportPathNValue;
	public static String folderPathValue;

	public static ExtentReports getExtent() {
		if (extent != null)
			return extent;
		extent = new ExtentReports();
		extent.attachReporter(getHtmlReporter());
		return extent;
	}

	private static ExtentHtmlReporter getHtmlReporter() {
		folderPathValue = CreateDateTime.createDateTimeFolder();
		reportPathNValue = folderPathValue;
		htmlReporter = new ExtentHtmlReporter(reportPathNValue + "/report.html");
		// Configuring htmlReporter
		htmlReporter.config().setCSS(".row .col.s4 {\r\n" + "    width: 50%;\r\n" + "    margin-left: auto;\r\n"
				+ "    left: auto;\r\n" + "    right: auto;\r\n" + "}");
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setDocumentTitle("Demo Automation Report");
		htmlReporter.config().setReportName("Demo Test");
		htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setEncoding("UTF-8");

		return htmlReporter;
	}

	public static ExtentTest createTest(String name, String description) {
		test = extent.createTest(name, description);
		return test;
	}
}