package com.Common;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class Base {

    public WebDriver driver;
    ExtentSparkReporter reporter;
    ExtentReports report;
    ExtentTest test;
    SoftAssert softAssert;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        reporter = new ExtentSparkReporter("Reports\\ExtentReportResults.html");
        reporter.config().setReportName("TurtleMint_POC");
        report = new ExtentReports();
        report.attachReporter(reporter);
        test = report.createTest("MultipleTests");
        softAssert = new SoftAssert();
    }

    @BeforeMethod
    @Parameters("site")
    public void launchChrome(String site) throws IOException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        //wait = new WebDriverWait(driver, 20, 150);
        if(site.equals("Flipkart")){
            driver.get(readProp().getProperty("flipURL"));
        }
        else if(site.equals("Turtlemint")){
            driver.get(readProp().getProperty("turtleURL"));
        }
        else {
            Assert.fail("Proper website is not requested.");
        }

    }

    @AfterMethod
    public void close(ITestResult result) throws IOException {
        if (!result.isSuccess()) {
            testFail(result);
        } else {
            test.log(Status.PASS, "The test was executed successfully.");
            driver.quit();
        }
    }

    @AfterClass
    public void endReports(){
        report.flush();
    }

    public Properties readProp() throws IOException {
        Properties prop = new Properties();
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/config"));
        prop.load(br);
        return prop;
    }

    public void testFail(ITestResult result) throws IOException {
        File scrShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String now = DateTimeFormatter.ofPattern("dd-MM-yy HH.mm.ss").format(LocalDateTime.now());
        FileUtils.copyFile(scrShot, new File("FailedScr/" + now + ".png"));
        String path = Paths.get("FailedScr/" + now + ".png").toAbsolutePath().toString();
        test.log(Status.FAIL,"", test.addScreenCaptureFromPath(path).getModel().getMedia().get(0));
        test.log(Status.INFO, result.getThrowable());
    }
}
