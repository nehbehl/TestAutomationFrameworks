package com.example.demo.sanity;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.example.demo.pages.ActionPage;
import com.example.demo.pages.LandingPage;
import com.example.demo.pages.UploadPage;
import com.example.demo.utilities.BrowserUtilities;
import com.example.demo.utilities.ExtentReportManager;
import com.example.demo.utilities.FileLoader;
import com.example.demo.utilities.TestListeners;

/* 
 * @Author: Neha Verma
 * 
 * Desc: Demo test
 * */

@Listeners(TestListeners.class)
public class DemoTest {

	/** The driver. */
	private static WebDriver driver = null;
	LandingPage landingPage = null;
	UploadPage uploadPage = null;
	ActionPage actionPage = null;
	Properties prop;
	int size=3;
	
	/** Before class. */
	@BeforeClass
	public void beforeClass() {
		driver = BrowserUtilities.launchBrowser(driver);
		landingPage = new LandingPage(driver);
		uploadPage = new UploadPage(driver);
		actionPage = new ActionPage(driver);
		prop = FileLoader.loadPropertiesFile("config.properties");
		TestListeners.extentLogger = ExtentReportManager.extent
				.createTest("******* Demo Test Begins *******");
		TestListeners.extentLogger.info("******* Demo Test Begins *******");
		
	}

	@Test
	public void appLaunch() {
		BrowserUtilities.appLaunch(driver, prop.getProperty("baseUrl"));
		landingPage.validateHeader();
	}
	
	@Test(dependsOnMethods={"appLaunch"})
	public void uploadFile() {
		landingPage.clickUploadLink();
		uploadPage.uploadFile();		
	}

	@Test(dependsOnMethods={"uploadFile"})
	public void validateDropdown() {
		driver.navigate().back();
		landingPage.clickDropdownLink();
		actionPage.validateDropdownOptions(size);
	}

	@AfterClass
	public void afterClass() {
		
		TestListeners.extentLogger = ExtentReportManager.extent
				.createTest("******* Demo Test Ends *******");
		TestListeners.extentLogger.info("******* Demo Test Ends *******");
		
		ExtentReportManager.extent.flush();
		driver.quit();
	}
}