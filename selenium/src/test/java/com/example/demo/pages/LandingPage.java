package com.example.demo.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Listeners;

import com.example.demo.utilities.ObjectHandler;
import com.example.demo.utilities.TestListeners;

@Listeners(TestListeners.class)
public class LandingPage {
    static Logger logger = Logger.getLogger(LandingPage.class);
	ObjectHandler objHandle;

    public LandingPage(WebDriver driver) {
        objHandle = new ObjectHandler(driver);
    }

    public void validateHeader() {
        try {
            objHandle.getWebElementOfLocator("LandingPage.headerLabel").isDisplayed();
            TestListeners.extentLogger
            .pass("Application launched successfully");
            logger.info("Application launched successfully");
        } catch(Exception e) {
            TestListeners.extentLogger
            .fail("Something went wrong when validating links, Stack Trace: " + e);
            logger.error("Something went wrong when validating links, Stack Trace: " + e);
        }
    }
    
    public void clickUploadLink() {
        try {
            objHandle.getWebElementOfLocator("LandingPage.uploadLink").isDisplayed();
            objHandle.getWebElementOfLocator("LandingPage.uploadLink").click();
            
            TestListeners.extentLogger
            .pass("Clicked upload link successfully");
            logger.info("Clicked upload link successfully");
        } catch(Exception e) {
            TestListeners.extentLogger
            .fail("Something went wrong when clicking upload link, Stack Trace: " + e);
            logger.error("Something went wrong when clicking upload link, Stack Trace: " + e);
        }
    }

        public void clickDropdownLink() {
        try {
            objHandle.getWebElementOfLocator("LandingPage.dropdownLink").isDisplayed();
            objHandle.getWebElementOfLocator("LandingPage.dropdownLink").click();
            
            TestListeners.extentLogger
            .pass("Clicked dropdown link successfully");
            logger.info("Clicked dropdown link successfully");
        } catch(Exception e) {
            TestListeners.extentLogger
            .fail("Something went wrong when clicking dropdown link, Stack Trace: " + e);
            logger.error("Something went wrong when clicking dropdown link, Stack Trace: " + e);
        }
    }
}