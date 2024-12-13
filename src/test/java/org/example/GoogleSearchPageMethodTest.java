package org.example;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class GoogleSearchPageMethodTest {

    WebDriver driver;
    ExtentReports extent;
    ExtentTest test;


    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @BeforeSuite
    public void setupReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("target/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @Test
    public void searchGoogleWithPageMethod() {
        test = extent.createTest("Search Google page");
        test.info("Starting the test");

        GoogleHomePage googlePage = new GoogleHomePage(driver);
        googlePage.launchPage("https://www.google.com");
        googlePage.search("Selenium TestNG Maven tutorial");

        System.out.println("Title of the page is: " + driver.getTitle());
        System.out.println("Title of the page is: " + driver.getTitle());

        test.pass("Test passed successfully");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
