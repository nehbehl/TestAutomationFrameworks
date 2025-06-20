import logging

from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait

from src.main.pages.base_page import BasePage
from src.main.search.context import Context
from selenium.webdriver.support import expected_conditions as ec


class TestEnvPage(BasePage):
    __test__ = False
    submit_btn = 'Submit'
    submit_form_btn = 'Submit_checkbox'

    driver: webdriver

    def __init__(self):
        options = webdriver.ChromeOptions()
        self.driver = webdriver.Remote('http://localhost:8085', options=options)

    def open_browser(self):
        self.driver.get(BasePage.testEnvPageUrl)
        return self

    def select_checkboxes(self):
        checkboxes = self.driver.find_elements(By.XPATH, "//*[contains(@class,'test-form')]//*[@class='input1']")
        for ch in checkboxes:
            ch.click()
        return self

    def click_form_submit_btn(self):
        self.driver.find_element(By.ID,self.submit_form_btn).click()
        return self

    def click_submit_btn(self):
        self.driver.find_element(By.ID,self.submit_btn).click()
        return self

    def select_checkboxes_under_parent(self):
        checkboxes = self.driver.find_element(By.XPATH, "//*[contains(@class,'test-form')]").find_elements(By.XPATH,
                                                                                                           ".//*[@class='input1']")
        for ch in checkboxes:
            ch.click()
        return self

    def find_test_element(self, locator_type, selector):
        logging.info("Find element By ")
        result = Context().set_strategy(self.driver, locator_type).execute_strategy(selector)
        assert result == True

    def click_wait_btn(self):
        self.driver.find_element(By.ID, "Wait_Submit").click()

    def execute_script(self, script):
        self.driver.execute_script(script)

    def click_test_button_wait(self):
        WebDriverWait(self.driver, 10).until(ec.visibility_of_element_located((By.ID, "wait_new_element")))

    def close(self):
        self.driver.quit()
