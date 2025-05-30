package com.example.demo.utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeSuite;

import io.github.bonigarcia.wdm.WebDriverManager;

/*
 * Desc: This utility caters to methods for browser handling
 */


public class BrowserUtilities {

	/** The logger. */
	static Logger logger = Logger.getLogger(BrowserUtilities.class);
	static Properties configProperties;
	static String downloadPath = "";
	
	
	@BeforeSuite
	public void logReport() throws Exception {
		logger = Logger.getLogger("Logs");
		Appender fh = null;
		try {
			fh = new FileAppender(new SimpleLayout(), "log.log");
			logger.addAppender(fh);
			fh.setLayout(new SimpleLayout());
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	
		
	public static WebDriver launchBrowser(WebDriver driver) {
		try {
			configProperties = JavaUtilities.loadPropertiesFile("config.properties");
			String autoDownload = configProperties.getProperty("autoDownloadChromeDriver");
			if (System.getProperty("autoDownloadChromeDriver") != null) {
				autoDownload = System.getProperty("autoDownloadChromeDriver");
			}
			String browserName = configProperties.getProperty("browser");

			if (browserName.toUpperCase().trim().equalsIgnoreCase("CHROME")) {
				if (autoDownload.equalsIgnoreCase("yes")) {
					driver = launchChromeBrowser(driver);
				}
			} else {
				logger.info("Driver does not exist");
				driver = null;
			}
			if (driver != null) {
				driver.manage().window().maximize();
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
				logger.info("Browser launched successfully.");
				logger.info("Browser Name: " + cap.getBrowserName().toString());
				logger.info("Browser Version: " + cap.getBrowserVersion().toString());
				logger.info("OS Name: " + System.getProperty("os.name"));
				logger.info("OS Version: " + System.getProperty("os.version"));
				return driver;
			}
		} catch (Exception e) {
			logger.error("Error in launching browser, STACK TRACE: " + e);
		}
		return driver;
	}
	
		
	private static void closeChromeBrowserInstance() {
		try {
			Thread.sleep(5000);
			logger.info("------------------------------------------");
			Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
			Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
			logger.info("Successfully closed all Chrome browser instances");
			Thread.sleep(5000);
		} catch (Exception e) {
			logger.error("Error in closing chrome browser instance,STACK TRACE: " + e);
		}
	}
	

	private static WebDriver launchChromeBrowser(WebDriver driver) {
		try {
			closeChromeBrowserInstance();
			WebDriverManager.chromedriver().clearDriverCache().setup();
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("profile.default_content_setting_values.automatic_downloads", 1);
			chromePrefs.put("download.prompt_for_download", false);
			chromePrefs.put("download.directory_upgrade", true);
			chromePrefs.put("safebrowsing.enabled", true);
			chromePrefs.put("download.default_directory", downloadPath);
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);
			options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			options.setCapability(ChromeOptions.CAPABILITY, options);
			driver = new ChromeDriver(options);			
			return driver;
		} catch (Exception e) {
			logger.error("Error in launching chrome browser, STACK TRACE: " + e);
		}
		return null;
	}
	
	public static void appLaunch(WebDriver driver, String url) {
		try {
			driver.get(url);
		} catch(Exception e) {
			logger.error("Error in launching app, STACK TRACE: " + e);
		}
		
	}
}