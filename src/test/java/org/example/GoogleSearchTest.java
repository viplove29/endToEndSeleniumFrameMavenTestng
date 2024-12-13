package org.example;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class GoogleSearchTest {

    WebDriver driver;

    ExtentReports extent;
    ExtentTest test;

    @BeforeSuite
    public void setupReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("target/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void searchGoogle() {
        driver.get("https://www.google.com");
        System.out.println("Title of the page is: " + driver.getTitle());
        // Create a test in the report
        test = extent.createTest("Sample Test");
        test.info("Starting the test");
        test.pass("Test passed successfully");
    }

    @Test
    public void searchTest() {
        test = extent.createTest("Search Test");
        test.info("Navigating to Google");
        driver.get("https://www.google.com");
        String title = driver.getTitle();
        test.info("Page title is: " + title);

        if (title.contains("Google")) {
            test.pass("Title verification passed");
        } else {
            test.fail("Title verification failed");
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite
    public void tearDownReport() {
        extent.flush();

        // Automatically open the report in the default browser
        File reportFile = new File("target/ExtentReport.html");
        if (reportFile.exists()) {
            try {
                Desktop.getDesktop().browse(reportFile.toURI());
            } catch (IOException e) {
                System.err.println("Failed to open report: " + e.getMessage());
            }
        } else {
            System.err.println("Report file does not exist!");
        }
    }
}
