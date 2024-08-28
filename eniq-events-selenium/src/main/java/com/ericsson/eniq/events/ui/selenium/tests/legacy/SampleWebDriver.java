package com.ericsson.eniq.events.ui.selenium.tests.legacy;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;

public class SampleWebDriver {


    @Test
    public void sampleTest() {
        FirefoxProfile firefoxProfile = new FirefoxProfile();

        //This line is needed to avoid the firebug startup screen
        firefoxProfile.setPreference("extensions.firebug.currentVersion", "1.8.1"); // Avoid startup screen

        WebDriver driver = new FirefoxDriver(firefoxProfile);       
        try {
            // Go to Google Home Page
            driver.get("http://www.google.com");

            // Look for search textbox and enter search term there
            WebElement searchBox = driver.findElement(By.name("q"));
            searchBox.sendKeys("WebDriver API");

            // Click on 'Search'
            WebElement searchButton = driver.findElement(By.name("btnG"));
            searchButton.click();

            // Not required or recommended any where, but just wait for the last
            // click()
            // operation to get completed fine
            Thread.sleep(2000);

            System.out.println("What's the current Url: "
                    + driver.getCurrentUrl());

            // if you wish to take screenshot of this page, you can!
            File scrFile = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(
                    "h:\\googlesearch-webdriverapi.png"));

            // Close the driver, once you're done.
            driver.close();
        } catch (Exception e) {
            e.printStackTrace(); // For debugging purposes
        }
    }
}
