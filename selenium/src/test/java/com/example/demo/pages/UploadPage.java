package com.example.demo.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Listeners;

import com.example.demo.utilities.ObjectHandler;
import com.example.demo.utilities.TestListeners;

@Listeners(TestListeners.class)
public class UploadPage {
    static Logger logger = Logger.getLogger(UploadPage.class);
	ObjectHandler objHandle;

    public UploadPage(WebDriver driver) {
        objHandle = new ObjectHandler(driver);
    }

    public void uploadFile() {
        try {
            objHandle.getWebElementOfLocator("UploadPage.uploadTextBox").sendKeys(System.getProperty("user.dir") + "\\src\\test\\resources\\data\\test.txt");
            TestListeners.extentLogger
            .pass("File uploaded successfully");
            logger.info("File uploaded successfully");
        } catch(Exception e) {
            TestListeners.extentLogger
            .fail("Something went wrong when uploading file, Stack Trace: " + e);
            logger.error("Something went wrong when uploading file, Stack Trace: " + e);
        }
    }    
}