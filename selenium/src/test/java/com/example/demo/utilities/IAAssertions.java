package com.example.demo.utilities;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Listeners;

@Listeners(TestListeners.class)
public class IAAssertions {
	public static LocatorReader appLocators;
	ObjectHandler objHandle = null;
	/** The logger. */
	static Logger logger = Logger.getLogger(IAAssertions.class);
	static WebDriver driver;
	static Properties configProperties;

	public IAAssertions() {

	}

	public IAAssertions(WebDriver driver) {
		IAAssertions.driver = driver;
		configProperties = FileLoader.loadPropertiesFile("config.properties");
	}

	/**
	 * Wait till Element displayed.
	 * 
	 * @param Webelement
	 *            , driver, elementName
	 */
	public void waitTillWebElementIsDisplayed(WebElement wEle, WebDriver driver, String elementName,
			String waitType) {
		try {
			configProperties = FileLoader.loadPropertiesFile("");
			String wtype = configProperties.getProperty(waitType);
			long waitDuration = Long.parseLong(wtype);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration));
			wait.until(ExpectedConditions.elementToBeClickable(wEle));
		} catch (Exception e) {
			logger.error(elementName + " not present and webelement is " + wEle + e);
			TestListeners.extentLogger
					.fail(elementName + " not present and webelement is " + wEle + e);
			JavaUtilities.screenShotCapture(driver, elementName, "SmartView");
		}
	}

	/**
	 * Wait till Element Loaded.
	 * 
	 * @param locator
	 *            , elementName
	 */
	public void waitTillElementIsLoaded(String locator, String elementName, String waitType) {
		try {
			
			String wtype = configProperties.getProperty(waitType);
			long waitDuration = Long.parseLong(wtype);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration));
			wait.until(ExpectedConditions.elementToBeClickable(ObjectHandler.byLocator(locator)));
		} catch (Exception e) {
			logger.info(elementName + " not present and webelement is " + locator + e);
			TestListeners.extentLogger.fail(elementName + " present and webelement is " + locator);
		}
	}

	/**
	 * Updated by: Nupur Pandey Updated on Date: 03/31/2017 Wait till List Web Elements are loaded.
	 * 
	 * @param list
	 *            web element , wait type
	 */
	public void waitTillElementsAreLoaded(List<WebElement> lwEle, String waitType) {
		try {
			
			String wtype = configProperties.getProperty(waitType);
			long waitDuration = Long.parseLong(wtype);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration));
			wait.until(ExpectedConditions.visibilityOfAllElements(lwEle));
		} catch (Exception e) {
			logger.info(lwEle + " not present and webelement is " + lwEle + e);
			TestListeners.extentLogger.fail(lwEle + " present and webelement is " + lwEle);
		}
	}

	/**
	 * Updated by: Nupur Pandey Updated on Date: 04/03/2017 Wait method using thread.sleep
	 * 
	 * @param wait
	 *            type
	 */

	public void longWait(String waitType) {
		
		String wtype = configProperties.getProperty(waitType);
		long waitDuration = Long.parseLong(wtype);
		try {
			Thread.sleep(waitDuration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void implicitWait(String waitType) {
		try {
			
			String wtype = configProperties.getProperty(waitType);
			long waitDuration = Long.parseLong(wtype);
			driver.manage().timeouts().implicitlyWait(waitDuration, TimeUnit.SECONDS);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public void waitTillInvisibilityOfElement(String locator, String elementName) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
			wait.until(ExpectedConditions
					.invisibilityOfElementLocated(ObjectHandler.byLocator(locator)));
		} catch (Exception e) {
			logger.error(elementName + " presence and webelement is " + locator + e);
			TestListeners.extentLogger.fail("Error in validating "  + elementName + " presence and webelement is " + locator + e);
		}
	}

	public void waitTillInvisibilityOfElement(String locator, String elementName, String waitType) {
		try {
			
			String wtype = configProperties.getProperty(waitType);
			long waitDuration = Long.parseLong(wtype);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration));
			wait.until(ExpectedConditions
					.invisibilityOfElementLocated(ObjectHandler.byLocator(locator)));
		} catch (Exception e) {
			logger.error(elementName + " present and webelement is " + locator + e);
		}
	}

	public void waitTillVisibilityOfElement(String locator, String elementName) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(ObjectHandler.byLocator(locator)));
		} catch (Exception e) {
			logger.error(elementName + " present and webelement is " + locator + e);
		}
	}
	
	public void waitTillWebElementIsDisplayed(String locator, String elementName) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
			wait.until(ExpectedConditions
					.elementToBeClickable(ObjectHandler.byLocator(locator)));
		} catch (Exception e) {
			logger.error(elementName + " present and webelement is " + locator + e);
			TestListeners.extentLogger
					.fail(elementName + " present and webelement is " + locator + e);
		}
	}

	public void waitTillVisibilityOfElement(String locator, String elementName, String waitType) {
		try {
			
			String wtype = configProperties.getProperty(waitType);
			long waitDuration = Long.parseLong(wtype);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration));
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(ObjectHandler.byLocator(locator)));
		} catch (Exception e) {
			logger.error(elementName + " present and webelement is " + locator + e);
		}
	}
}