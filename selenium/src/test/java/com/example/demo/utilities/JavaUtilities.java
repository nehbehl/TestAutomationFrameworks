package com.example.demo.utilities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

/**
 * The Class Utils.
 */
@Listeners(TestListeners.class)
public class JavaUtilities {

	/** The logger. */
	static Logger logger = Logger.getLogger(JavaUtilities.class);
	static WebDriver driver;
	static Properties prop;
	ExtentReports extentReport;
	public static String dragDropJavaScript;
	static String killPdf = "taskkill /IM \"AcroRd32.exe\" /f";

	@BeforeSuite
	public void logReport() throws Exception {
		logger = Logger.getLogger("IALog");
		Appender fh = null;
		try {
			fh = new FileAppender(new SimpleLayout(), "IAAutomationLogFile.log");
			logger.addAppender(fh);
			fh.setLayout(new SimpleLayout());

		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	/**
	 * Load properties file.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the properties
	 */
	public static Properties loadPropertiesFile(String fileName) {
		String user_dir = System.getProperty("user.dir");
		Properties prop = new Properties();

		try {
			// File path when executing from batch file
			String propertyFilePath = System.getProperty("PropertyFile");
			if (propertyFilePath != null) {
				user_dir = user_dir.replace("bin", "");
			}
			FileInputStream fs = new FileInputStream(user_dir + "//src//test//resources//" + fileName);
			prop.load(fs);
			fs.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
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

	/**
	 * Wait for list to appear.
	 * 
	 * @param refreshIcon
	 *            the refresh icon
	 * @param waitInMins
	 *            the wait in mins
	 * @param retryIntervalInSecs
	 *            the retry interval in secs
	 */
	public static void waitForListToAppear(WebElement refreshIcon, int waitInMins, int retryIntervalInSecs) {

		int maxLimit = 0;

		while (true) {
			if (refreshIcon.isDisplayed() && maxLimit++ < waitInMins * 6) {

				System.out.println("Waiting for next 10 secs.");
				waitForElement(retryIntervalInSecs);
			} else {
				if (maxLimit < waitInMins * 6) {
					System.out.println("Tables are fetched");
					break;
				}
				logger.error("Stopping: Tables are not fetched.");
				break;
			}
		}
	}

	/**
	 * Wait for element.
	 * 
	 * @param sec
	 *            the sec
	 */
	public static void waitForElement(int sec) {

		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Method for Killing running Login Threads
	public static void killLoginThreads() {
		try {
			Set<Thread> currentScriptThreads = new HashSet<Thread>();
			Set<Thread> allRunningThreads = Thread.getAllStackTraces().keySet();
			for (Thread thread : allRunningThreads) {
				String threadName = thread.getName();
				if (threadName.equalsIgnoreCase("AutoThread"))
					currentScriptThreads.add(thread);
			}
			if (!currentScriptThreads.isEmpty()) {
				for (Thread thread : currentScriptThreads) {
					logger.info("Killed running thread: " + thread.getName());
					thread.interrupt();
				}
			}
		} catch (Exception e) {
			logger.error("Unable to kill Login Threads,STACK TRACE: " + e);
		}
	}

			
	// Method to generate random numbers
	public static int getRandomNo(int bound) {
		int ri;
		Random randomInt = new Random();
		do {
			ri = randomInt.nextInt(bound);
		} while (ri < 0);
		return ri;
	}

	public static int getRandomNoFromRange(int from, int to) {
		Random random = new Random();
		int n1 = from;
		int n2 = to;
		int randomNumber;
		if (n2 > n1) {
			randomNumber = random.nextInt(n2 - n1) + n1;
		} else {
			randomNumber = n2;
		}
		return randomNumber;
	}
	
	public static ArrayList<String> mergeLists(List<String> list1, List<String> list2) {
		try {
			ArrayList<String> updatedList= new ArrayList<String>(list1);
			updatedList.addAll(list2);
			return updatedList;
		} catch (Exception e) {
			TestListeners.extentLogger.fail("Error in merging lists, STACK TRACE: " + e);
			logger.error("Error in merging lists, STACK TRACE: " + e);
		}
		return null;
	}

	public static int[] getRandomNoList(int selectCount, int totalCount) {
		int orderOfSelection[] = new int[selectCount];
		Random randomGenerator = new Random();
		for (int i = 0; i < selectCount; i++) {
			boolean flag = true;
			int tempInt;
			do {
				tempInt = randomGenerator.nextInt(totalCount);
			} while (tempInt < 0);
			if (i == 0) {
				orderOfSelection[i] = tempInt;
				logger.debug("Generated : " + orderOfSelection[i]);
			} else {
				for (int cnt = 0; cnt < i; cnt++) {
					if (orderOfSelection[cnt] == tempInt) {
						i--;
						flag = false;
						logger.debug("Already generated, hence changing : " + tempInt);
					}
				}
				if (flag) {
					orderOfSelection[i] = tempInt;
					logger.debug("Generated : " + orderOfSelection[i]);
				}
			}
		}
		return orderOfSelection;
	}

	// Method to generate random numbers with out zero
	public static int getRandomNoWithoutZero(int bound) {
		int ri;
		Random randomInt = new Random();
		if (bound > 0) {
			do {
				ri = randomInt.nextInt(bound + 1);
			} while (ri <= 0);
			return ri;
		}
		return bound;
	}
	
	public static int[] getOrderOfSelection(int columnCount, int selectColumn) {
		if (selectColumn > columnCount) {

			logger.info("selectColumn ");
		}
		int orderOfSelection[] = new int[selectColumn];
		Random randomGenerator = new Random();
		for (int idx = 0; idx < selectColumn; idx++) {
			boolean ins = true;
			int tempInt;
			do {
				tempInt = randomGenerator.nextInt(columnCount);
			} while (tempInt < 0);

			if (idx == 0) {
				orderOfSelection[idx] = tempInt;
				logger.debug("Generated : " + orderOfSelection[idx]);
			} else {
				for (int cnt = 0; cnt < idx; cnt++) {
					if (orderOfSelection[cnt] == tempInt) {
						idx--;
						ins = false;
						logger.debug("Already generated, hence changing : " + tempInt);
					}
				}
				if (ins) {
					orderOfSelection[idx] = tempInt;
					logger.debug("Generated : " + orderOfSelection[idx]);
				}
			}
		}
		for (int idx = 0; idx < selectColumn; idx++) {
		}
		return orderOfSelection;
	}

	public static int[] getOrderOfSelectionWithoutZero(int columnCount, int selectColumn) {
		int orderOfSelection[] = new int[selectColumn];

		Random randomGenerator = new Random();

		if (columnCount < selectColumn) {
			return getOrderOfSelectionWithoutZero(columnCount, columnCount);
		}

		if (columnCount == 1 && selectColumn == 1) {
			orderOfSelection[0] = 1;
			return orderOfSelection;
		}

		for (int idx = 0; idx < selectColumn; idx++) {
			boolean ins = true;
			int tempInt;
			do {
				tempInt = randomGenerator.nextInt(columnCount + 1);
			} while (tempInt < 0 || tempInt == 0);

			if (idx == 0) {
				orderOfSelection[idx] = tempInt;
				logger.debug("Generated : " + orderOfSelection[idx]);
			} else {
				for (int cnt = 0; cnt < idx; cnt++) {
					if (orderOfSelection[cnt] == tempInt) {
						idx--;
						ins = false;
						logger.debug("Already generated, hence changing : " + tempInt);
					}
				}
				if (ins) {
					orderOfSelection[idx] = tempInt;
					logger.debug("Generated : " + orderOfSelection[idx]);
				}
			}
		}

		for (int idx = 0; idx < selectColumn; idx++) {
		}
		return orderOfSelection;
	}

	public static String screenShotCapture(WebDriver driver, String qoName, String flowName) {
		String destFile = null;
		prop = JavaUtilities.loadPropertiesFile("config.properties");
		
		if (prop.getProperty("enableReportScreenshot").trim().equalsIgnoreCase("Yes")) {
			String tm = getTimestamp();
			destFile = ExtentReportManager.reportPathNValue + "\\" + tm + "_" + qoName + ".png";
			String path = ".\\" + tm + "_" + qoName + ".png";
			try {
				File screenShotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(screenShotFile, new File(destFile));
				TestListeners.extentLogger.info("Screenshot saved for report: " + destFile);
				logger.info("Screenshot saved for report: " + destFile);
				TestListeners.extentLogger.log(Status.INFO, "Image",
						MediaEntityBuilder.createScreenCaptureFromPath(path).build());
			} catch (Exception e) {
				TestListeners.extentLogger.error("Error while saving screenshot for report: " + e);
				logger.error("Error while saving screenshot for report: " + e);
			}
		}
		return destFile;
	}


	public static String getTimestamp() {
		String tm = CreateDateTime.getCurrentSystemDateAndYear()
				+ Integer.toString(CreateDateTime.getHourAndMinuteForCurrentSystemTime(0))
				+ Integer.toString(CreateDateTime.getHourAndMinuteForCurrentSystemTime(1))
				+ Integer.toString(CreateDateTime.getHourAndMinuteForCurrentSystemTime(2));
		return tm;

	}

	public static String getDateTime() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String dateStr = dateFormat.format(date);
		return dateStr;
	}
	
	public static String getDateTimeHoursMinutes() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		String dateStr = dateFormat.format(date);
		return dateStr;
	}
	
	public static String getMinuteAdjustedTime(String time,int minute) {
		String adjustedDateStr="";
	    try {
	    	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	        Date date = dateFormat.parse(time);
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        calendar.add(Calendar.MINUTE, minute);
	        Date adjustedDate = calendar.getTime();
	        adjustedDateStr = dateFormat.format(adjustedDate);
	    } catch (Exception e) {
	    	TestListeners.extentLogger.fail("Error in getting subtracted time, STACK TRACE: " + e);
			logger.error("Error in getting subtracted time, STACK TRACE: " + e);
	    }
	    return adjustedDateStr;
	}
	
	public static String getTimeCalculation(String startTime, String endTime) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date startDate = dateFormat.parse(startTime);
		Date endDate = dateFormat.parse(endTime);
		long difference = endDate.getTime() - startDate.getTime();
		String diffSeconds = String.valueOf(difference / 1000 % 60);
		String diffMinutes = String.valueOf(difference / (60 * 1000) % 60);
		String diffHours = String.valueOf(difference / (60 * 60 * 1000) % 24);
		String diffDays = String.valueOf(difference / (24 * 60 * 60 * 1000));
		String str = diffDays + " " + diffHours + ":" + diffMinutes + ":" + diffSeconds;
		return str;
	}
	
	public static String getTimeCalculations(String startTime, String endTime) {
		String str = "";
		try {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Date startDate = dateFormat.parse(startTime);
			Date endDate = dateFormat.parse(endTime);
			long difference = endDate.getTime() - startDate.getTime();
			String diffSeconds = String.valueOf(difference / 1000 % 60);
			String diffMinutes = String.valueOf(difference / (60 * 1000) % 60);
			String diffHours = String.valueOf(difference / (60 * 60 * 1000) % 24);
			str = diffHours + ":" + diffMinutes + ":" + diffSeconds;
		} catch (Exception e) {
			TestListeners.extentLogger.fail("Error in getting calculated time, STACK TRACE: " + e);
			logger.error("Error in getting calculated time, STACK TRACE: " + e);
		}
		return str;
	}


	public static int[] copyArrayElements(int[] firstArray, int secondArrayValue) {
		int firstArrayLenght = firstArray.length + 1;
		int[] result = Arrays.copyOf(firstArray, firstArrayLenght);
		result[firstArray.length] = secondArrayValue;
		return result;
	}

	public static void windowScrollDown(WebDriver driver, String locator) {
		WebElement scroll = driver.findElement(By.xpath(locator));
		scroll.sendKeys(Keys.PAGE_DOWN);
	}

	public static void windowScrollUp(WebDriver driver, String locator) {
		WebElement scroll = driver.findElement(By.xpath(locator));
		scroll.sendKeys(Keys.PAGE_UP);
	}

	public static void scrollingToElement(WebDriver driver, String locator) {
		WebElement element = driver.findElement(By.xpath(locator));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

	}

	public static void setBrowserZoomLevel(WebDriver driver, double zoomLevel) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("document.body.style.zoom = '" + zoomLevel + "'");
	}

	public static void loadDragDropJavaScript(String filepath) throws Exception {
		try {
			dragDropJavaScript = FileLoader.readFile("JSFiles", filepath);
		} catch (IOException e) {
			TestListeners.extentLogger.fail("Unable to Load JavaScript needed to perform Drag-Drop Operations");
			throw new Exception("Unable to Load JavaScript needed to perform Drag-Drop Operations" + e.getMessage());
		}
	}

	public static void clickByOffset(WebDriver driver, WebElement ele, int xOffset, int yOffset) {
		try {
			Actions build = new Actions(driver);
			build.moveToElement(ele, xOffset, yOffset).click().build().perform();
		} catch (Exception e) {
			TestListeners.extentLogger.fail("Unable to click element by offset" + e);
			logger.info("Unable to click element by offset" + e);
		}
	}

	public static void dragAndDropByOffset(WebDriver driver, String source, String target, int xOffset, int yOffset)
			throws Exception {
		try {
			String javaScript;
			javaScript = dragDropJavaScript + "; $(function () {$(\"" + source + "\").simulate('drag',$('" + target
					+ "'), {x:" + xOffset + ", y:" + yOffset + "});});";
			((JavascriptExecutor) driver).executeScript(javaScript);
		} catch (Exception e) {
			TestListeners.extentLogger.fail("Unable to do drag and drop of elements by offset" + e);
			logger.info("Unable to do drag and drop of elements by offset" + e);
		}
	}

	public static void dragAndDropActions(WebDriver driver, WebElement sourceLocator, WebElement destinationLocator) {
		try {
			Actions action = new Actions(driver);
			action.dragAndDrop(sourceLocator, destinationLocator).build().perform();
		} catch (Exception e) {
			TestListeners.extentLogger.fail("Error in drag and drop using actions" + e);
			logger.info("Error in drag and drop using actions" + e);
		}
	}

	public static void dragAndDropActionsByOffset(WebDriver driver, WebElement sourceLocator, int xOffset,
			int yOffset) {
		try {
			Actions action = new Actions(driver);
			action.dragAndDropBy(sourceLocator, xOffset, yOffset).build().perform();
		} catch (Exception e) {
			TestListeners.extentLogger.fail("Error in drag and drop using actions ByOffset " + e);
			logger.info("Error in drag and drop using actions ByOffset " + e);
		}
	}

	public static String AddScreenshot(String tabName) {
		String pathvalue = "";
		try {
			JavaUtilities.screenShotCapture(driver, tabName, "Adhoc");
			pathvalue = "Screenshots" + "\\" + tabName + ".png";
		} catch (Exception e) {
			TestListeners.extentLogger.fail("Screenshot could not be captured for " + tabName);
			throw new NoSuchElementException("Screenshot could not be captured for " + tabName);
		}
		return pathvalue;
	}

	public static void doubleClickEvent(WebDriver driver, WebElement ele) {
		try {
			/* assertion.longWait("lwait"); */
			Actions action = new Actions(driver).doubleClick(ele);
			action.perform();
		} catch (Exception e) {
			logger.info("Error in double clicking " + e);
		}
	}

	public static void fireFoxClickEvent(WebDriver driver, WebElement ele) {
		try {
			prop = JavaUtilities.loadPropertiesFile("config.properties");
			if (prop.getProperty("browserName").equals("FIREFOX") || prop.getProperty("browserName").equals("IE")) {
				Actions action = new Actions(driver).click(ele);
				action.build().perform();
			}
		} catch (Exception e) {
			logger.info("Error in firefox clicking " + e);
		}
	}

	public static void fireFoxDoubleClickEvent(WebDriver driver, WebElement ele) {
		try {
			prop = JavaUtilities.loadPropertiesFile("config.properties");
			if (prop.getProperty("browserName").equals("FIREFOX")) {
				Actions act = new Actions(driver);
				act.moveToElement(ele).doubleClick().perform();
				((JavascriptExecutor) driver).executeScript("var evt = document.createEvent('MouseEvents');"
						+ "evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
						+ "arguments[0].dispatchEvent(evt);", ele);
			}
		} catch (Exception e) {
			logger.info("Error in firefox double clicking " + e);
		}
	}

	public static void doubleClickAndEnterData(WebDriver driver, WebElement ele, String txt) {
		try {
			Actions builder = new Actions(driver);
			Actions seriesOfActions = builder.doubleClick(ele).sendKeys(txt);
			seriesOfActions.perform();
		} catch (Exception e) {
			logger.info("Error in double clicking and entering data " + e);
		}
	}

	public static void doubleClickMouseEvent(WebDriver driver, WebElement element) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("var evt = document.createEvent('MouseEvents');"
					+ "evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
					+ "arguments[0].dispatchEvent(evt);", element);
		} catch (Exception e) {
			logger.info("Error in double click using mouse events " + e);
		}
	}

	public static void scrollToElement(WebDriver driver, WebElement ele) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);", ele);
		} catch (Exception e) {
			logger.info("Error in scrolling to element " + e);
		}
	}
	
	public static String getElementCssPropertiesValue(WebDriver driver, WebElement ele, String property) {
		String elementValue = "";
		try {
			String script = "return window.getComputedStyle(arguments[0], '::after').getPropertyValue('" + property
					+ "');";
			JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
			elementValue = (String) jsExecutor.executeScript(script, ele);
		} catch (Exception e) {
			logger.info("Error in scrolling to element " + e);
		}
		return elementValue;
	}
	
	public static void scrollToContainsElement(WebDriver driver, String containsEle,String actualEle) {
		try {
			for (int i = 0; i < 10; i++) {
				List<WebElement> list = driver.findElements(By.xpath(containsEle));
				if (list.size() > 2) {
					scrollToElement(driver, list.get(list.size() - 1));
				}
				if (driver.findElements(By.xpath(actualEle)).size() > 0) {
					break;
				}
			}
		} catch (Exception e) {
			logger.info("Error in scrolling to element " + e);
		}
	}
	
	public static void scrollDown(WebDriver driver) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("window.scrollTo(0,document.body.scrollHeight);");
		} catch (Exception e) {
			logger.info("Error in scrolling to element " + e);
		}
	}
	
	public static void javaScriptHoverAndClick(WebDriver driver, WebElement hoverElement, WebElement clickElement) {
		ObjectHandler objHandle = new ObjectHandler(driver);
		IAAssertions assertion = new IAAssertions(driver);
		String javaScript = "var evObj = document.createEvent('MouseEvents');"
				+ "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
				+ "arguments[0].dispatchEvent(evObj);";
		((JavascriptExecutor) driver).executeScript(javaScript, hoverElement);
		assertion.waitTillWebElementIsDisplayed(clickElement, driver, "click element", "mediumWait");
		TestListeners.extentLogger.pass("Successfully hovered and clicked on element");
		logger.info("Successfully hovered and clicked on element");

		objHandle.safeJavaScriptClick(clickElement);
	}

	public static void hoverAndClick(WebDriver driver, WebElement hoverElement, WebElement clickElement) {
		ObjectHandler objHandle = new ObjectHandler(driver);
		Actions action = new Actions(driver);
		action.moveToElement(hoverElement).build().perform();
		JavaUtilities.waitForElement(1);
		objHandle.safeJavaScriptClick(clickElement);
	}
	
	public static void rightClick(WebDriver driver, WebElement ele) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			String script = "var evt = document.createEvent('MouseEvents');" + "var RIGHT_CLICK_BUTTON_CODE = 2;"
					+ "evt.initMouseEvent('contextmenu', true, true, window, 1, 0, 0, 0, 0, false, false, false, false, RIGHT_CLICK_BUTTON_CODE, null);"
					+ "arguments[0].dispatchEvent(evt)";
			js.executeScript(script, ele);
		} catch (Exception e) {
			logger.error("Error in right click using java script" + e);
		}
	}

	public static void rightClickWithActions(WebDriver driver, WebElement ele) {
		try {
			Actions action = new Actions(driver);
			action.moveToElement(ele);
			action.contextClick(ele).build().perform();
		} catch (Exception e) {
			logger.error("Error in rightclick using actions classes " + e);
		}
	}

	public static String formatNumber(String StrVal, int decimalPrecision) {
		String rowVal = null;
		String str = "";
		while (decimalPrecision > 0) {
			str += "#";
			decimalPrecision--;
		}
		NumberFormat formatter = new DecimalFormat("#0." + str);
		double doub = Double.parseDouble(StrVal);
		rowVal = formatter.format(doub);
		return rowVal;
	}

	public static String formateDate(String strVal) throws ParseException {
		String rowVal = null;
		try {
			SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
			SimpleDateFormat inputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
			Date date = inputFormat.parse(strVal);
			rowVal = outputFormat.format(date);
		} catch (Exception e) {
			logger.error("Error in formatting date " + e);
			TestListeners.extentLogger.fail("Error in formatting date " + e);
		}
		return rowVal;
	}
	
	public static String formateDateForSmartViewReport(String strVal) throws ParseException {
		String rowVal = null;
		try {
			SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
			SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date date = inputFormat.parse(strVal);
			rowVal = outputFormat.format(date);
		} catch (Exception e) {
			logger.error("Error in formatting date " + e);
			TestListeners.extentLogger.fail("Error in formatting date " + e);
		}
		return rowVal;
	}

	public static String getNumberFormat(int decimalPrecision) {
		String str = "";
		while (decimalPrecision > 0) {
			str += "#";
			decimalPrecision--;
		}
		// NumberFormat formatter = new DecimalFormat("#0."+ str);
		return str;
	}

	public static boolean clearLogs() {
		PrintWriter writer;
		prop = JavaUtilities.loadPropertiesFile("config.properties");
		String logsPath = prop.getProperty("logsPath");
		try {
			writer = new PrintWriter(logsPath);
			writer.print("");
			writer.close();
			logger.info("Logs clear successfully");
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.warn("Please check logPath in property file " + e);
			TestListeners.extentLogger.skip("Please check logPath in property file " + e);
		}
		return false;
	}
	public static String dateFormatInMMDDYYYY() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String s = formatter.format(date);
		return s;
	}

	public static String getLaggedTime() throws ParseException {
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		String time = sdfTime.format(now);
		Date d = sdfTime.parse(time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MINUTE, 10);
		time = sdfTime.format(cal.getTime());
		return time;
	}

	public static void dragAndMoveOffset(WebDriver driver, WebElement source, WebElement destination, int xOffSet,
			int yOffSet) {
		try {
			Actions action = new Actions(driver);
			action.moveToElement(source).pause(5000).clickAndHold(source).pause(3000).moveByOffset(0, 0)
					.moveToElement(destination).moveByOffset(xOffSet, yOffSet).pause(3000).release().perform();
		} catch (Exception e) {
			logger.error("Error in drag and move offset: " + e);
			TestListeners.extentLogger.fail("Error in drag and move offset: " + e);
		}
	}

	public static String getCurrentIFrame(WebDriver driver) {
		String currentFrame = null;
		try {
			JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
			currentFrame = (String) jsExecutor.executeScript("return self.name");
		} catch (Exception e) {
			logger.error("Error in getting current frame: " + e);
			TestListeners.extentLogger.fail("Error in getting current frame: " + e);
		}
		return currentFrame;
	}

	public static void extractJSLogs(WebDriver driver) {
		try {
			prop = JavaUtilities.loadPropertiesFile("config.properties");
			if (prop.getProperty("browserName").equalsIgnoreCase("Chrome")) {
				LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
				logger.info("*******************Browser Console JavaScript logs******************");
				TestListeners.extentLogger.info("*******************Browser Console JavaScript logs******************");

				for (LogEntry entry : logEntries) {
					logger.info(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
					if (entry.getMessage().equalsIgnoreCase("failed")
							|| entry.getLevel().getName().equalsIgnoreCase("Severe")) {
						logger.error("Please review Browser Console Java Script logs for errors in current execution : "
								+ entry.getMessage());
						TestListeners.extentLogger.warning(
								"Please review Browser Console Java Script logs for errors in current execution : "
										+ entry.getMessage());
					}
				}
				logger.info("*******************Browser Console JavaScript logs end******************");
				TestListeners.extentLogger
						.info("*******************Browser Console JavaScript logs end******************");
			}
		} catch (Exception e) {
			logger.error("Error in extracting JS logs, Stack Trace: " + e);
			TestListeners.extentLogger.fail("Error in extracting JS logs, Stack Trace: " + e);
		}
	}
	
	public static void extractHSVJSLogs(WebDriver driver) {
		try {
			prop = JavaUtilities.loadPropertiesFile("config.properties");
			if (prop.getProperty("browserName").equalsIgnoreCase("Chrome")) {
				LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
				logger.info("*******************Browser Console JavaScript logs******************");
				TestListeners.extentLogger.info("*******************Browser Console JavaScript logs******************");
				for (LogEntry entry : logEntries) {
					logger.info(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
					if (entry.getMessage().equalsIgnoreCase("failed")
							|| entry.getLevel().getName().equalsIgnoreCase("Severe")) {
						if (!entry.getMessage().contains("(reading 'split')")) {
							logger.error(
									"Please review Browser Console Java Script logs for errors in current execution : "
											+ entry.getMessage());
							TestListeners.extentLogger.warning(
									"Please review Browser Console Java Script logs for errors in current execution : "
											+ entry.getMessage());
						}
					}
				}
				logger.info("*******************Browser Console JavaScript logs end******************");
				TestListeners.extentLogger
						.info("*******************Browser Console JavaScript logs end******************");
			}
		} catch (Exception e) {
			logger.error("Error in extracting hsv JS logs, Stack Trace: " + e);
			TestListeners.extentLogger.fail("Error in extracting hsv JS logs, Stack Trace: " + e);
		}
	}
	
	public static void waitTillFileDownloads(File file) {
		try {
			int time=0;
			while(!file.exists()) {
				time++;	
				if(time > 4573949) {
					logger.error("Report took too long to download, hence aborted wait");
					TestListeners.extentLogger.error("Report took too long to download, hence aborted wait");				
					break;				
				}
			}
		} catch(Exception e) {
			logger.error("Something went wrong while waiting for file download, Stack Trace: " + e);
			TestListeners.extentLogger.fail("Something went wrong while waiting for file download, Stack Trace: " + e);
		}
	}

	public static boolean fileDownloadCheck(String reportName, String fileType) {
		boolean flag = false;
		File file;
		String filename;
		try {
			prop = JavaUtilities.loadPropertiesFile("config.properties");
			file = new File(FileHandler.globalDownloadPath + reportName + fileType);
			filename = file.getName().substring(reportName.lastIndexOf("\"") + 1);
			long sizeInKB = file.length() / 1024 + 1;
			waitTillFileDownloads(file);
			if (file.exists()) {
				logger.info("File: " + filename + " is downloaded successfully");
				TestListeners.extentLogger.pass("File: " + filename + " is downloaded successfully");
				fileSizeCheck(reportName, sizeInKB);
				flag = true;
			} else {
				logger.error(
						"Unable to download/locate the File: " + filename + " due to server and system time mismatch");
				TestListeners.extentLogger.fail(
						"Unable to download/locate the File: " + filename + " due to server and system time mismatch");
			}
		} catch (Exception e) {
			logger.error("Error in downloading or verifying report: " + e);
			TestListeners.extentLogger.fail("Error in downloading or verifying report: " + e);
		}
		return flag;
	}
	
	public static void validateFileSize(String fileName, String fileType, long fileSize) {
		try {
			long actualFileSize = JavaUtilities.getFileSize(fileName, fileType);
			if (fileSize == actualFileSize) {
				logger.info("Successfully validated file size as expected: " + fileSize + " KB");
				TestListeners.extentLogger.pass("Successfully validated file size as expected: " + fileSize + " KB");
			} else {
				logger.error("Failed to validate file size, Actual:" + actualFileSize + " KB and Expected: " + fileSize
						+ " KB");
				TestListeners.extentLogger.fail("Failed to validate file size, Actual:" + actualFileSize
						+ " KB and Expected: " + fileSize + " KB");
			}
		} catch (Exception e) {
			logger.error("Error in validating file size, STACK TRACE: " + e);
			TestListeners.extentLogger.fail("Error in validating file size, STACK TRACE: " + e);
		}
	}
	
	public static long getFileSize(String reportName, String fileType) {
		try {
			File file = new File(FileHandler.globalDownloadPath + reportName + fileType);
			long sizeInKB = file.length() / 1024 + 1;
			return sizeInKB;
		} catch (Exception e) {
			logger.error("Error in getting file size STACK TRACE: " + e);
			TestListeners.extentLogger.fail("Error in getting file size STACK TRACE: " + e);
		}
		return 0;
	}
	
	public static long getFileSizeInBytes(String reportName, String fileType) {
		try {
			File file = new File(FileHandler.globalDownloadPath + reportName + fileType);
			long sizeinBytes = file.length();
			return sizeinBytes;
		} catch (Exception e) {
			logger.error("Error in getting file size STACK TRACE: " + e);
			TestListeners.extentLogger.fail("Error in getting file size STACK TRACE: " + e);
		}
		return 0;
	}
	
	public static void fileNameStartsWithDownloadCheck(String trimmedReportName) {
	try {
		String filename = "";
		boolean flag = false;
		File dir = new File(FileHandler.globalDownloadPath);
		File files[] = dir.listFiles();
		for (File file : files) {
			filename = file.getName().substring(file.getName().lastIndexOf("\"") + 1);
			if (file.getName().startsWith(trimmedReportName)) {
				flag = true;
				break;
			}
		}
		if (flag) {
			logger.info("File: " + filename + " is downloaded successfully");
			TestListeners.extentLogger.pass("File: " + filename + " is downloaded successfully");
		} else {
			logger.error(
					"Unable to download/locate the File: " + filename + " due to server and system time mismatch");
			TestListeners.extentLogger.fail(
					"Unable to download/locate the File: " + filename + " due to server and system time mismatch");
		}

	} catch (Exception e) {
		logger.error("Error in downloading or verifying report: " + e);
		TestListeners.extentLogger.fail("Error in downloading or verifying report: " + e);
	}
	}

	public static void waitForFileDownload(String second, String reportPath) {
		try {
			int count = Integer.parseInt(second) / 2;
			File file = new File(reportPath);
			for (int i = 0; i < count; i++) {
				Thread.sleep(2000);
				if (file.exists()) {
					break;
				} else {
					logger.info("Downloading......");
				}
			}
		} catch (Exception e) {

		}
	}
	
	public static void waitExplicitly(int min) {
		try {
			int count = min * 2;
			for (int i = 0; i < count; i++) {
				Thread.sleep(30000);
				logger.info("Waiting......");
				TestListeners.extentLogger.info("Waiting......");
			}
		} catch (Exception e) {
			
		}
	}

	public static void fileSizeCheck(String itemName, long sizeInKB) {
		try {
			if (sizeInKB > 0) {
				logger.info("File: " + itemName + " size is: " + sizeInKB + " KB");
				TestListeners.extentLogger.pass("File: " + itemName + "size is: " + sizeInKB + " KB");
			} else {
				logger.info("File: " + itemName + " size is: " + sizeInKB + " KB which is less than 1 KB");
				TestListeners.extentLogger
						.error("File: " + itemName + " size is: " + sizeInKB + " KB which is less than 1 KB");
			}
		} catch (Exception e) {
			logger.error("Something went wrong while performing file size check, Exception: " + e);
			TestListeners.extentLogger.fail("Something went wrong while performing file size check, Exception: " + e);
		}
	}

	public static void deleteExistingDownload(String itemName, String fileType) {
		try {
			prop = JavaUtilities.loadPropertiesFile("config.properties");
			File file = new File(FileHandler.globalDownloadPath + itemName + fileType);
			if (file.exists()) {
				file.delete();
				logger.info("Deleted existing item: " + itemName + fileType);
				TestListeners.extentLogger.info("Deleted existing item: " + itemName + fileType);
			}
		} catch (Exception e) {
			logger.error("Something went wrong while deleting existing download, Exception: " + e);
			TestListeners.extentLogger.fail("Something went wrong while deleting existing download, Exception: " + e);
		}
	}

	public static String getFutureDate(int expectedCount,String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, expectedCount);
		String output = sdf.format(c.getTime());
		return output;
	}
	
	public static String getUpcomingTime(int num, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.HOUR, 0);
		c.add(Calendar.MINUTE, num);
		c.add(Calendar.SECOND, 0);
		Date upcomingDate = c.getTime();
		return dateFormat.format(upcomingDate);
	}
	
	public static String getUpcomingHoursTime(int num, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.HOUR, num);
		c.add(Calendar.MINUTE, 0);
		c.add(Calendar.SECOND, 0);
		Date upcomingDate = c.getTime();
		return dateFormat.format(upcomingDate);
	}

	public static String yesterdaysDate() {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		return dateFormat.format(cal.getTime());
	}

	public static void handleSavePopup() {
		try {
			prop = loadPropertiesFile("ia.properties");
			if (prop.getProperty("browserName").equalsIgnoreCase("firefox")) {
				String user_dir = System.getProperty("user.dir");
				Runtime.getRuntime().exec(user_dir + prop.getProperty("ffExportScriptPath"));
			} else if (prop.getProperty("browserName").equalsIgnoreCase("IE")) {
				String user_dir = System.getProperty("user.dir");
				Runtime.getRuntime().exec(user_dir + prop.getProperty("ieExportScriptPath"));
			}
		} catch (Exception e) {
			logger.error("Error in handle save pop-up, Exception: " + e);
			TestListeners.extentLogger.fail("Error in handle save pop-up, Exception: " + e);
		}
	}

	public static String getUpcomingDate(int num, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date currentDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		c.add(Calendar.YEAR, 0);
		c.add(Calendar.MONTH, 0);
		c.add(Calendar.DATE, num);
		c.add(Calendar.HOUR, 0);
		c.add(Calendar.MINUTE, 0);
		c.add(Calendar.SECOND, 0);
		Date upcomingDate = c.getTime();
		return dateFormat.format(upcomingDate);
	}

	public static String getCurrentDate(String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date currentDate = new Date();
		return dateFormat.format(currentDate);
	}

	public static boolean fileDownloadCheck(String reportName, String fileType, String path) {
		try {
			File file = new File(path + reportName + fileType);
			if (file.exists()) {
				long sizeInKB = file.length() / 1024 + 1;
				if (sizeInKB > 0) {
					logger.info("File: " + reportName + " size is: " + sizeInKB + " KB");
					TestListeners.extentLogger.pass("File: " + reportName + "size is: " + sizeInKB + " KB");
					return true;
				} else {
					logger.error("File: " + reportName + " size is: " + sizeInKB + " KB which is less than 1 KB");
					TestListeners.extentLogger
							.error("File: " + reportName + " size is: " + sizeInKB + " KB which is less than 1 KB");
				}
			} else {
				logger.error("File does not exists, please check manually on location: " + file);
				TestListeners.extentLogger.fail("File does not exists, please check manually on location: " + file);
			}
		} catch (Exception e) {
			logger.error("Error in downloading or verifying report: " + e);
			TestListeners.extentLogger.fail("Error in downloading or verifying report: " + e);
		}
		return false;
	}
	
	public static boolean validateFileDownload(String reportName, String fileType) {
		boolean flag = false;
		File file;
		String fileName;
		try {
			prop = JavaUtilities.loadPropertiesFile("config.properties");
			file = new File(FileHandler.globalDownloadPath + reportName + fileType);
			fileName = file.getName().substring(reportName.lastIndexOf("\"") + 1);
			long sizeInKB = getFileSize(reportName, fileType);
			if (file.exists()) {
				logger.info("File: " + fileName + " is downloaded successfully");
				TestListeners.extentLogger.pass("File: " + fileName + " is downloaded successfully");
				fileSizeCheck(reportName, sizeInKB);
				flag = true;
			} else {
				logger.error(
						"Unable to download/locate the File: " + fileName + " due to server and system time mismatch");
				TestListeners.extentLogger.fail(
						"Unable to download/locate the File: " + fileName + " due to server and system time mismatch");
			}
		} catch (Exception e) {
			logger.error("Error in downloading or verifying report: " + e);
			TestListeners.extentLogger.fail("Error in downloading or verifying report: " + e);
		}
		return flag;
	}

	public static void deleteIfFileExists(String fileName, String ext) {
		try {
			prop = JavaUtilities.loadPropertiesFile("config.properties");
			File file = new File(FileHandler.globalDownloadPath + fileName + ext);
			if (file.exists()) {
				file.delete();
				logger.info("Existing file " + fileName + ext + " is deleted successfully");
				TestListeners.extentLogger.info("Existing file " + fileName + ext + " is deleted successfully");
			}
		} catch (Exception e) {
			logger.error("Error in deleting existing file, Exception: " + e);
			TestListeners.extentLogger.fail("Error in deleting existing file, Exception: " + e);
		}
	}

	public void mouseHoverOverElement(WebDriver driver, WebElement element) {
		try {
			Actions action = new Actions(driver);
			action.moveToElement(element).build().perform();
		} catch (Exception e) {
			logger.error("Error in mouse hover over element, Exception: " + e);			
		}
	}

	public static void checkAlert() {
		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
			logger.info("Alert handled");
		} catch (Exception e) {
			logger.info("No alert found");
		}
	}

	public static void checkAlert(WebDriver driver) {
		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
			logger.info("Alert handled");
		} catch (Exception e) {
			logger.info("No alert found");
		}
	}

	public static void actionClick(WebDriver driver, WebElement element) {
		try {
			Actions action = new Actions(driver);
			action.moveToElement(element).click().build().perform();
		} catch (Exception e) {
			logger.error("Error in action click, Exception: " + e);
		}
	}

	public static void sendKeysUsingAction(WebDriver driver, WebElement element, String value) {
		try {
			Actions action = new Actions(driver);
			action.sendKeys(element, value).perform();
		} catch (Exception e) {
			logger.error("Error in sending Keys using Action, Exception: " + e);
		}

	}
	
	public static String getTodaysDateAgainstInputFormat(String format) {
		String output = null;
		try {
			Date date = new Date();
			SimpleDateFormat simpleformat = new SimpleDateFormat(format);
			output = simpleformat.format(date);
		} catch (Exception e) {
			logger.error("Error in formatting date " + e);
			TestListeners.extentLogger.fail("Error in formatting date " + e);
		}
		return output;
	}
	
	public static String getInputDateInExpectedFormat(String inputDate, String format) {
		String output = null;
		try {
			SimpleDateFormat parser = new SimpleDateFormat("M/d/yyyy hh:mm");
			Date date = parser.parse(inputDate);
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			output = formatter.format(date);
		} catch (Exception e) {
			logger.error("Error in formatting input date in expected format" + e);
			TestListeners.extentLogger.fail("Error in formatting input date in expected format " + e);
		}
		return output;
	}
	
	public static String getLastDayOfMonth() {
		String output = null;
		try {
			String date = getTodaysDateAgainstInputFormat("M/d/yy");
			LocalDate convertedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("M/d/yy"));
			convertedDate = convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");
			output = convertedDate.format(formatter);
		} catch (Exception e) {
			logger.error("Error in getting last day of the month, STACK TRACE: " + e);
			TestListeners.extentLogger.fail("Error in getting last day of the month, STACK TRACE: " + e);
		}
		return output;
	}
	
	public static int getMonthInIntFromDate(String date) {
		int month=0;
		try {
			LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
			month = localDate.getMonthValue();
		} catch (Exception e) {
			logger.error("Error in getting month from date , STACK TRACE: " + e);
			TestListeners.extentLogger.fail("Error in getting month from date , STACK TRACE: " + e);
		}
		return month;
	}
	
	public static int convertMonthNameToInt(String monthName) {
		int month=0;
		try {
			Date date = new SimpleDateFormat("MMMM", Locale.ENGLISH).parse(monthName);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            month = cal.get(Calendar.MONTH);
            month = month + 1 ;
		} catch (Exception e) {
			logger.error("Error in converting month name to integer , STACK TRACE: " + e);
			TestListeners.extentLogger.fail("Error in converting month name to integer , STACK TRACE: " + e);
		}
		return month;
	}
	
	public static int getDayYear(String date,String outputName) {
		int output=0;
		try {
	        LocalDate currentDate
	            = LocalDate.parse(date);
	        if(outputName.contains("day")) {
	        	output = currentDate.getDayOfMonth();
	        }else {
	        	output = currentDate.getYear();
	        } 
		} catch (Exception e) {
			logger.error("Error in getting day and year, STACK TRACE: " + e);
			TestListeners.extentLogger.fail("Error in getting day and year, STACK TRACE: " + e);
		}
		return output;
	}
	
	//for instance if input is dddd MMMM yy(0004 Mar 21) output will be EEEE MMMM yy(Saturday March 21)
	public static String getInputFormatInLocaleFormat(String selectedtimestampFormat) {
		String toBeReplaced = selectedtimestampFormat;
		try {
			int count = 0;
			int length = toBeReplaced.length();
			for (int i = 0; i < length - 1; i++) {
				char ch = toBeReplaced.charAt(i);
				if (ch == 'd')
					count++;
			}
			if (count > 2) {
				selectedtimestampFormat = toBeReplaced.replace("d", "E");
			}
		} catch (Exception e) {
			TestListeners.extentLogger.fail("Error in converting given timestamp format in Locale format   " + e);
			logger.error("Error in converting given timestamp format in Locale format   " + e);
		}
		return selectedtimestampFormat;
	}
	
	public static String getNextMonthDate(String expectedDateFormat) {
		String nextMonthDate = "";
		try {
			  SimpleDateFormat dateFormat = new SimpleDateFormat(expectedDateFormat);
		      Calendar calendar = Calendar.getInstance();
		      calendar.add(Calendar.MONTH, 1);
		      nextMonthDate = dateFormat.format(calendar.getTime());
		} catch (Exception e) {
			TestListeners.extentLogger.fail("Error in getting next month date. " + e);
			logger.error("Error in getting next month date. " + e);
		}
		return nextMonthDate;
	}
	
	public static String getValidFutureDate() {
		String futureDate = "",newMonth, newDay;
		try {
		      Calendar calendar = Calendar.getInstance();
		      calendar.set(Calendar.DATE, JavaUtilities.getRandomNoFromRange(1,28));
		      calendar.set(Calendar.MONTH, JavaUtilities.getRandomNoFromRange(1,12));
		      calendar.add(Calendar.YEAR, 2);
		      int mYear = calendar.get(Calendar.YEAR);
              int mMonth = calendar.get(Calendar.MONTH);
              int mDay = calendar.get(Calendar.DAY_OF_MONTH);
              newMonth = String.format("%02d", mMonth);
              newDay = String.format("%02d", mDay);
              futureDate = newMonth+"/"+newDay+"/"+mYear;
		} catch (Exception e) {
			TestListeners.extentLogger.fail("Error in getting next month date. " + e);
			logger.error("Error in getting next month date. " + e);
		}
		return futureDate;
	}
	
	public static String[] addMinutesToServerTime(int setHour, int setMin, int addMinsBy) {
		String futureTime[] = new String[2];
		try {
	        int time = setHour * 60 + setMin;  
	        time += addMinsBy;         
	        int newHour = (time / 60) % 24;  
	        int newMinutes = time % 60;        
	        String formattedHour = String.format("%02d", newHour);
	        String formattedMinutes = String.format("%02d", newMinutes);
	        futureTime[0] = formattedHour;
	        futureTime[1] = formattedMinutes;
		} catch (Exception e) {
			TestListeners.extentLogger.fail("Error in adding "+addMinsBy+" minutes to server time." + e);
			logger.error("Error in adding "+addMinsBy+" minutes to server time." + e);
		}
		return futureTime;
	}
	
	public static int compareYears(int year, int compareToYear) {
		int output = 1;
		try {
			Year actualYear = Year.of(year);
			Year expectedYear = Year.of(compareToYear);
			output = actualYear.compareTo(expectedYear);
		} catch (Exception e) {
			TestListeners.extentLogger.fail("Error in comparing years, STACK TRACE: " + e);
			logger.error("Error in comparing years, STACK TRACE: " + e);
		}
		return output;
	}
	
	public static int compareMonths(int month, int compareTomonth) {
		int output = 1;
		try {
			 YearMonth actualMonth = YearMonth.of(0, month);
		     YearMonth expectedMonth = YearMonth.of(0, compareTomonth);
			 output = actualMonth.compareTo(expectedMonth);
		} catch (Exception e) {
			TestListeners.extentLogger.fail("Error in comparing months, STACK TRACE: " + e);
			logger.error("Error in comparing months, STACK TRACE: " + e);
		}
		return output;
	}
	
	public static int compareDates(String date, String compareToDate) {
		int output = 1;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");
			Date actualDate = sdf.parse(date);
			Date expectedDate = sdf.parse(compareToDate);
			output = actualDate.compareTo(expectedDate);
		} catch (Exception e) {
			TestListeners.extentLogger.fail("Error in comparing dates, STACK TRACE: " + e);
			logger.error("Error in comparing dates, STACK TRACE: " + e);
		}
		return output;
	}
	
	public static String numberFormatting(int number) {
		try {
			char[] suffix = { ' ', 'k', 'M', 'B', 'T', 'P', 'E' };
			long numValue = number;
			int value = (int) Math.floor(Math.log10(numValue));
			int base = value / 3;
			if (value >= 3 && base < suffix.length) {
				String s = new DecimalFormat().format(numValue / Math.pow(10, base * 3)) + suffix[base];
				return s;
			} else {
				String s = new DecimalFormat("#,##0").format(numValue);
				return s;
			}
		} catch (Exception e) {
			TestListeners.extentLogger.fail("Error in getting formatted number, STACK TRACE: " + e);
			logger.error("Error in getting formatted number, STACK TRACE: " + e);
		}
		return "";
	}
	
	public static String getTimeByTimeZone(String time, String timeZone) {
		try {
			String dateFormat = "hh:mm";
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			Date date = formatter.parse(time);
			TimeZone tz = TimeZone.getTimeZone(timeZone);
			formatter.setTimeZone(tz);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.setTimeZone(tz);
			//For future reference
			// int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
			// int minute = calendar.get(Calendar.MINUTE);
			// String t = hourOfDay + ":" + minute;
			String t = formatter.format(calendar.getTime());
			return t;
		} catch (Exception e) {
			TestListeners.extentLogger.fail("Error in getting time by time zone, STACK TRACE: " + e);
			logger.error("Error in getting time by time zone, STACK TRACE: " + e);
		}
		return "";
	}
	
	public static List<String> getList(String propertyName, String splitExpression) {
		prop = JavaUtilities.loadPropertiesFile("config.properties");
		String[] list = prop.getProperty(propertyName).split(splitExpression);
		return Arrays.asList(list);
	}

	
	public static int compareDateTime(String date, String compareToDate, String dateTimeFormat) {
		int output = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
			Date actualDate = sdf.parse(date);
			Date expectedDate = sdf.parse(compareToDate);
			output = expectedDate.compareTo(actualDate);
		} catch (Exception e) {
			TestListeners.extentLogger.fail("Error in comparing date time, STACK TRACE: " + e);
			logger.error("Error in comparing date time, STACK TRACE: " + e);
		}
		return output;
	}
	
	public List<String> getValuesList(List<WebElement> list) {
		List<String> valueList = new ArrayList<String>();
		try {
			for (WebElement value : list) {
				valueList.add(value.getText());
			}
		} catch (Exception e) {
			logger.error("Error in getting values list, STACK TRACE :"+e);
			TestListeners.extentLogger.fail("Error in getting values list, STACK TRACE :"+e);
		}
		return valueList;
	}
	
	public static String convertEpochToDate(long value) {
		String formattedDate = null;
		try {
			Date date = new Date(value);
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			formattedDate = format.format(date);
		} catch (Exception e) {
			logger.error("Error in converting epoch to date, STACK TRACE :" + e);
			TestListeners.extentLogger.fail("Error in converting epoch to date, STACK TRACE :" + e);
		}
		return formattedDate;
	}

	public static String getElementsCssProperty(WebDriver driver, WebElement ele, String cssProperty) {
		String actualBackgroundColor = null;
		try {
			actualBackgroundColor = (String) ((JavascriptExecutor) driver).executeScript(
					"return window.getComputedStyle(arguments[0]).getPropertyValue(arguments[1]);", ele, cssProperty);
		} catch (Exception e) {
			logger.error("Error in validating element css property, STACK TRACE :" + e);
			TestListeners.extentLogger.fail("Error in validating element css property, STACK TRACE :" + e);
		}
		return actualBackgroundColor;
	}
	
	public static void decompressZipFile(String zipFilePath, String outputDir) {
		String fileName;
		ZipEntry entry;
		ZipInputStream zipInputStream = null;
		try {
			fileName = zipFilePath.split("\\\\")[zipFilePath.split("\\\\").length - 1];
			zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath));
			while ((entry = zipInputStream.getNextEntry()) != null) {
				String entryName = entry.getName();
				File outputFile = new File(outputDir, entryName);
				if (entry.isDirectory()) {
					outputFile.mkdirs();
				} else {
					BufferedOutputStream bufferedOutputStream = null;
					try {
						bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
						byte[] buffer = new byte[1024];
						int bytesRead;
						while ((bytesRead = zipInputStream.read(buffer)) != -1) {
							bufferedOutputStream.write(buffer, 0, bytesRead);
						}
					} catch (IOException e) {
						// Handle I/O exception while writing file
						logger.error("Error writing file " + entryName + ": " + e.getMessage());
						TestListeners.extentLogger.fail("Error writing file " + entryName + ": " + e.getMessage());
					} finally {
						try {
							if (bufferedOutputStream != null) {
								bufferedOutputStream.close();
							}
						} catch (IOException e) {
							logger.error("Error closing output stream: " + e.getMessage());
							TestListeners.extentLogger.fail("Error closing output stream: " + e.getMessage());
						}
					}
				}
				zipInputStream.closeEntry();
			}
			logger.info("Successfully decompressed zip file: " + fileName);
			TestListeners.extentLogger.pass("Successfully decompressed zip file: " + fileName);
		} catch (Exception e) {
			logger.error("Error in decompressing zip file, STACK TRACE: " + e);
			TestListeners.extentLogger.fail("Error in decompressing zip file, STACK TRACE: " + e);
		} finally {
			try {
				if (zipInputStream != null) {
					zipInputStream.close();
				}
			} catch (IOException e) {
				logger.error("Error closing ZipInputStream: " + e.getMessage());
				TestListeners.extentLogger.fail("Error closing ZipInputStream: " + e.getMessage());
			}
		}
	}
	
	public static void javaScriptHoverOverElement(WebDriver driver, WebElement hoverElement) {
		try {
			String javaScript = "var evObj = document.createEvent('MouseEvents');"
					+ "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
					+ "arguments[0].dispatchEvent(evObj);";
			((JavascriptExecutor) driver).executeScript(javaScript, hoverElement);
		} catch (Exception e) {
			logger.info("Error in hovering over element, STACK TRACE: " + e);
		}
	}
}