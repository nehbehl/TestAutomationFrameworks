package com.example.demo.pages;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Listeners;

import com.example.demo.utilities.ObjectHandler;
import com.example.demo.utilities.TestListeners;

@Listeners(TestListeners.class)
public class ActionPage {
    static Logger logger = Logger.getLogger(ActionPage.class);
	ObjectHandler objHandle;

    public ActionPage(WebDriver driver) {
        objHandle = new ObjectHandler(driver);
    }

    public void validateDropdownOptions(int size) {
        try {
            Select dropdownLoc = new Select(objHandle.getWebElementOfLocator("ActionPage.dropdown"));
            List<WebElement> count = dropdownLoc.getOptions();
            if(count.size()==size) {
                TestListeners.extentLogger
                .pass("Dropdown displayed with options - " + count.size());
                logger.info("Dropdown displayed with options - " + count.size());
                TestListeners.extentLogger
                .pass("Dropdown displayed with default selection - " + dropdownLoc.getFirstSelectedOption().getText());
                
            } else {
                TestListeners.extentLogger
                .fail("Dropdown displayed with lesser than " + size +  " options - " + count.size());
                logger.error("Dropdown displayed with lesser than " + size + " options - " + count.size());
            }
        } catch(Exception e) {
            TestListeners.extentLogger
            .fail("Something went wrong when validating dropdown, Stack Trace: " + e);
            logger.error("Something went wrong when validating dropdown, Stack Trace: " + e);
        }
    }    
}