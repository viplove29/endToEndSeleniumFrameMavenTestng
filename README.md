Run Selenium Test end-to-end 


1. Set Up Maven Project
Step:
1.	Open your IDE (e.g., IntelliJ IDEA or Eclipse).
2.	Create a new Maven project.
Example:
•	Use the Maven archetype-quickstart template for simplicity.
•	Name your project, e.g., SeleniumTestNGProject.
________________________________________
2. Update pom.xml with Dependencies
Step: Add Selenium, TestNG, and WebDriverManager dependencies to pom.xml.
Example:
xml
Copy code
<dependencies>
    <!-- Selenium Java -->
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.10.0</version>
    </dependency>

    <!-- TestNG -->
    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>7.8.0</version>
        <scope>test</scope>
    </dependency>

    <!-- WebDriverManager -->
    <dependency>
        <groupId>io.github.bonigarcia</groupId>
        <artifactId>webdrivermanager</artifactId>
        <version>5.6.2</version>
    </dependency>
</dependencies>
________________________________________
3. Create a Test Class
Step: Set up a basic test using TestNG annotations.
Example:
java
Copy code
package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class GoogleSearchTest {

    WebDriver driver;

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
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
________________________________________
4. Configure TestNG XML
Step: Create a testng.xml file to define test execution.
Example:
xml
Copy code
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="TestSuite">
    <test name="GoogleTests">
        <classes>
            <class name="tests.GoogleSearchTest"/>
        </classes>
    </test>
</suite>
________________________________________
5. Run Tests
Step: Use TestNG to execute the tests.
Example:
1.	Right-click on testng.xml → Run as TestNG Suite.
2.	Alternatively, use Maven:
bash
Copy code
mvn test
________________________________________
6. Add a Page Object Model (Optional)
Step: Structure your project using the Page Object Model for better maintainability.
Example:
•	Page Class: GoogleHomePage.java
java
Copy code
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GoogleHomePage {
    WebDriver driver;

    By searchBox = By.name("q");

    public GoogleHomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void search(String query) {
        driver.findElement(searchBox).sendKeys(query);
        driver.findElement(searchBox).submit();
    }
}
•	Test Class: GoogleSearchTest.java
java
Copy code
@Test
public void searchGoogle() {
    GoogleHomePage googlePage = new GoogleHomePage(driver);
    googlePage.search("Selenium TestNG Maven tutorial");
    System.out.println("Title of the page is: " + driver.getTitle());
}
________________________________________
7. Generate Reports
Step: Use TestNG’s default reporting or add additional reporting frameworks like ExtentReports.
Example for ExtentReports: Add the dependency to pom.xml:
xml
Copy code
<dependency>
    <groupId>com.aventstack</groupId>
    <artifactId>extentreports</artifactId>
    <version>5.0.9</version>
</dependency>
Update the test code to include ExtentReports:
java
Copy code
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class GoogleSearchTest {
    ExtentReports extent;

    @BeforeSuite
    public void setupReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @Test
    public void searchGoogle() {
        ExtentTest test = extent.createTest("Search Google Test");
        test.info("Navigating to Google");
        driver.get("https://www.google.com");
        test.pass("Test Passed: Title is " + driver.getTitle());
    }

    @AfterSuite
    public void tearDownReport() {
        extent.flush();
    }
}
. Update Lombok Version
Ensure you're using the latest version of Lombok, as recent versions are better equipped to handle module-related changes.
•	Update Lombok in your pom.xml (for Maven projects):
xml
Copy code
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.30</version> <!-- Replace with the latest version -->
    <scope>provided</scope>
</dependency>
•	For Gradle, update the Lombok dependency:
groovy
Copy code
implementation 'org.projectlombok:lombok:1.18.30'
annotationProcessor 'org.projectlombok:lombok:1.18.30'
________________________________________
2. Add JVM Arguments
Pass additional JVM arguments to allow Lombok to access the internal APIs.
Maven Projects:
Add the following to your pom.xml under the <build> section:
xml
Copy code
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <compilerArgs>
                    <arg>--add-exports=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED</arg>
                    <arg>--add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED</arg>
                </compilerArgs>
            </configuration>
        </plugin>
    </plugins>
</build>
Ensure Proper ExtentReports Initialization
Make sure you’ve initialized ExtentReports and attached a reporter properly.
Correct Example:
java
Copy code
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class TestReport {
    ExtentReports extent;
    ExtentTest test;

    @BeforeSuite
    public void setupReport() {
        // Initialize the reporter and attach it to ExtentReports
        ExtentSparkReporter spark = new ExtentSparkReporter("ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @Test
    public void sampleTest() {
        // Create a test in the report
        test = extent.createTest("Sample Test");
        test.info("Starting the test");
        test.pass("Test passed successfully");
    }

    @AfterSuite
    public void tearDownReport() {
        // Flush the report to write data to the file
        extent.flush();
    }
}
________________________________________
2. Verify Report File Location
Ensure the output file (e.g., ExtentReport.html) is being created in the specified location.
•	Check if the file path is correct:
java
Copy code
ExtentSparkReporter spark = new ExtentSparkReporter("reports/ExtentReport.html");
If the path doesn’t exist, create the folder manually (reports in this case).
•	Verify if the report file contains any content.
________________________________________
3. Add Logs to the Tests
For the report to contain meaningful information, you need to log actions in your test cases.
Example:
java
Copy code
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
________________________________________
4. Ensure extent.flush() is Called
Calling extent.flush() is crucial; without it, the report won’t save data to the output file.
Example:
java
Copy code
@AfterSuite
public void tearDownReport() {
    extent.flush(); // Finalize the report
}
________________________________________
5. Debugging Steps
•	Check Console Logs: Ensure there are no errors or exceptions during the test execution that could prevent the report from being written.
•	Test with a Minimal Example: Run this simple example to confirm ExtentReports is working:
java
Copy code
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class MinimalTest {
    public static void main(String[] args) {
        ExtentSparkReporter spark = new ExtentSparkReporter("SimpleReport.html");
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(spark);

        ExtentTest test = extent.createTest("Basic Test");
        test.info("This is a basic test");
        test.pass("The test passed");

        extent.flush(); // Finalize the report
        System.out.println("Report generated.");
    }
}
________________________________________
6. Verify Dependencies
Ensure you’re using the correct version of ExtentReports.
Add this to your pom.xml if not already present:
xml
Copy code
<dependency>
    <groupId>com.aventstack</groupId>
    <artifactId>extentreports</artifactId>
    <version>5.0.9</version>
</dependency>
________________________________________
7. Check for Browser or WebDriver Issues
If you're performing Selenium tests, ensure that the WebDriver actions are being logged properly. Use appropriate try-catch blocks to log failures.
________________________________________
8. Use a Valid HTML File
Ensure the ExtentSparkReporter points to an accessible and valid HTML file path. If using a relative path, ensure the directories exist.
________________________________________
By following these steps, you should be able to see content in your ExtentReports output. Let me know if you face any specific issues or errors!

Ensure Proper ExtentReports Initialization
Make sure you’ve initialized ExtentReports and attached a reporter properly.
Correct Example:
java
Copy code
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class TestReport {
    ExtentReports extent;
    ExtentTest test;

    @BeforeSuite
    public void setupReport() {
        // Initialize the reporter and attach it to ExtentReports
        ExtentSparkReporter spark = new ExtentSparkReporter("ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @Test
    public void sampleTest() {
        // Create a test in the report
        test = extent.createTest("Sample Test");
        test.info("Starting the test");
        test.pass("Test passed successfully");
    }

    @AfterSuite
    public void tearDownReport() {
        // Flush the report to write data to the file
        extent.flush();
    }
}
________________________________________
2. Verify Report File Location
Ensure the output file (e.g., ExtentReport.html) is being created in the specified location.
•	Check if the file path is correct:
java
Copy code
ExtentSparkReporter spark = new ExtentSparkReporter("reports/ExtentReport.html");
If the path doesn’t exist, create the folder manually (reports in this case).
•	Verify if the report file contains any content.
________________________________________
3. Add Logs to the Tests
For the report to contain meaningful information, you need to log actions in your test cases.
Example:
java
Copy code
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
________________________________________
4. Ensure extent.flush() is Called
Calling extent.flush() is crucial; without it, the report won’t save data to the output file.
Example:
java
Copy code
@AfterSuite
public void tearDownReport() {
    extent.flush(); // Finalize the report
}
________________________________________
5. Debugging Steps
•	Check Console Logs: Ensure there are no errors or exceptions during the test execution that could prevent the report from being written.
•	Test with a Minimal Example: Run this simple example to confirm ExtentReports is working:
java
Copy code
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class MinimalTest {
    public static void main(String[] args) {
        ExtentSparkReporter spark = new ExtentSparkReporter("SimpleReport.html");
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(spark);

        ExtentTest test = extent.createTest("Basic Test");
        test.info("This is a basic test");
        test.pass("The test passed");

        extent.flush(); // Finalize the report
        System.out.println("Report generated.");
    }
}
________________________________________
6. Verify Dependencies
Ensure you’re using the correct version of ExtentReports.
Add this to your pom.xml if not already present:
xml
Copy code
<dependency>
    <groupId>com.aventstack</groupId>
    <artifactId>extentreports</artifactId>
    <version>5.0.9</version>
</dependency>
________________________________________
7. Check for Browser or WebDriver Issues
If you're performing Selenium tests, ensure that the WebDriver actions are being logged properly. Use appropriate try-catch blocks to log failures.
________________________________________
8. Use a Valid HTML File
Ensure the ExtentSparkReporter points to an accessible and valid HTML file path. If using a relative path, ensure the directories exist.
________________________________________
By following these steps, you should be able to see content in your ExtentReports output. Let me know if you face any specific issues or errors!

Pre-Requisites
•	Java AWT Desktop Class: The Desktop class works on most systems, but you need a GUI-enabled environment. It won’t work in headless servers.
•	Report File Path: Ensure the ExtentReport.html file path is correct. If needed, use absolute paths.
________________________________________
Sample Output
After the test finishes:
1.	The Extent Report (ExtentReport.html) is generated.
2.	The file automatically opens in the default web browser.

3.	To use Spotless in your project, you need to add the appropriate dependencies and configuration to your build tool. Spotless is a code formatter that can enforce consistent formatting for various languages, including Java. It can integrate with Maven or Gradle.
________________________________________
For Maven: Spotless Dependency
Add the following plugin to your pom.xml:
xml
Copy code
<build>
    <plugins>
        <plugin>
            <groupId>com.diffplug.spotless</groupId>
            <artifactId>spotless-maven-plugin</artifactId>
            <version>2.43.0</version> <!-- Use the latest version -->
            <configuration>
                <java>
                    <googleJavaFormat>
                        <version>1.15.0</version> <!-- Use the latest version -->
                    </googleJavaFormat>
                </java>
            </configuration>
        </plugin>
    </plugins>
</build>
________________________________________
Usage in Maven
•	Run Spotless to check the code:
sh
Copy code
mvn spotless:check
•	Automatically format the code:
sh
Copy code
mvn spotless:apply
________________________________________
For Gradle: Spotless Dependency
Add the following plugin to your build.gradle file:
groovy
Copy code
plugins {
    id 'com.diffplug.spotless' version '6.22.0' // Use the latest version
}

spotless {
    java {
        googleJavaFormat('1.15.0') // Use the latest version
    }
}
________________________________________
Usage in Gradle
•	Run Spotless to check the code:
sh
Copy code
./gradlew spotlessCheck
•	Automatically format the code:
sh
Copy code
./gradlew spotlessApply
________________________________________
Custom Formatting
If you want to customize formatting rules, Spotless supports a wide range of formatters and configurations. For example:
Custom Configuration (Optional):
groovy
Copy code
spotless {
    java {
        target 'src/**/*.java'  // Specify target files
        importOrder('java', 'javax', 'org', 'com') // Define import order
        removeUnusedImports() // Automatically remove unused imports
        googleJavaFormat('1.15.0')
    }
}



