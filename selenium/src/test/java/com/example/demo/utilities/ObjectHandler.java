package com.example.demo.utilities;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Listeners;

/*
 * Description: This class is returning the locators from locators.xml
 */

@Listeners(TestListeners.class)
public class ObjectHandler {
	public static LocatorReader appLocators;
	public LocatorReader appLoca;
	static WebDriver driver;
	/** The logger. */
	static Logger logger = Logger.getLogger(ObjectHandler.class);
	static Properties prop;
	IAAssertions assertion;

	public ObjectHandler() {

	}

	public ObjectHandler(WebDriver driver) {
		// TODO Auto-generated constructor stub
		ObjectHandler.driver = driver;
		assertion = new IAAssertions(driver);
	}

	
	public static By byLocator(String locator) {
		By result = null;

		if (locator.startsWith("//")) {
			result = By.xpath(locator);
		} else if (locator.startsWith("css=")) {
			result = By.cssSelector(locator.replace("css=", ""));
		} else if (locator.startsWith("class=")) {
			result = By.className(locator.replace("class=", ""));
		} else if (locator.startsWith("tag=")) {
			result = By.tagName(locator.replace("tag=", ""));
		} else if (locator.startsWith("#")) {
			result = By.name(locator.replace("#", ""));
		} else if (locator.startsWith("plt=")) {
			result = By.name(locator.replace("plt=", ""));
		} else if (locator.startsWith("lt=")) {
			result = By.name(locator.replace("lt=", ""));
		} else if (locator.startsWith("(")) {
			result = By.xpath(locator);
		} else {
			result = By.id(locator);
		}

		return result;
	}

	public String createDynamicQueryXpath(String string, String toBeReplaceKey,
			String replacedValed) {
		String result = "";
		if (string.contains(toBeReplaceKey)) {
			result = string.replace(toBeReplaceKey, replacedValed);
		}
		return result;
	}

	public WebElement getWebElementOfLocator(String key) {
		String locator = appLocators.getLocator(key);
		return (driver.findElement(byLocator(locator)));
	}

	public List<WebElement> getWebElementsOfLocator(String key) {
		String locator = appLocators.getLocator(key);
		return (driver.findElements(byLocator(locator)));
	}

	/**
	 * Find element in list by text.
	 * 
	 * @param driver
	 *            the driver
	 * @param locator
	 *            the locator
	 * @param text
	 *            the text
	 * @return the string
	 */
	public static String findElementInListByText(WebDriver driver, String locator, String text) {

		int i = 1;
		String expectedLocator = "";
		while (true) {
			expectedLocator = locator + "/li[" + i++ + "]/span";
			String listNthTet = "";
			try {
				listNthTet = driver.findElement(By.xpath(expectedLocator)).getText();
			} catch (Exception e) {
				throw new ElementNotInteractableException("Element not found: " + text);
			}
			if (listNthTet.trim().equalsIgnoreCase(text.trim())) {
				break;
			}
		}
		return expectedLocator;
	}

	public void safeJavaScriptClick(WebElement element) {
		try {
			if (element.isEnabled() && element.isDisplayed()) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			} else {
				logger.error("Element could not be clicked" + element);
				JavaUtilities.screenShotCapture(driver, "CheckError", "SmartView");
			}
		} catch (Exception e) {
			logger.error("Element could not be clicked " + element + e);
			JavaUtilities.screenShotCapture(driver, "CheckError", "SmartView");
		}
	}
}