package com.askomdch.base_test;

import com.askomdch.config.ConfigReader;
import com.askomdch.driver.Driver;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseTest {

    protected static WebDriver driver;
    protected static ExtentReports reports;
    protected static ExtentTest extentTest;

    @BeforeTest
    public void beforeTest(){
        reports = new ExtentReports(System.getProperty("user.dir") + "/test-report/ExtentReport.html", true);
        reports.addSystemInfo("OS NAME", System.getProperty("os.name"));
        reports.addSystemInfo("ENGINEER", System.getProperty("user.name"));
        reports.addSystemInfo("ENVIRONMENT", "QA");
        reports.addSystemInfo("JAVA VERSION", System.getProperty("java.version"));
    }

    @AfterTest
    public void afterTest(){
        reports.flush();
        reports.close();
    }

    @BeforeMethod
    public void setUp(){
        driver = Driver.getDriver(ConfigReader.getProperty("browser"));
        driver.get(ConfigReader.getProperty("url"));
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {
        if (extentTest != null) {
            if (result.getStatus() == ITestResult.FAILURE){
                extentTest.log(LogStatus.FAIL, "TEST CASE FAILED: " + result.getName());
                extentTest.log(LogStatus.FAIL, "ERROR MESSAGE: " + result.getThrowable());
                String screenshotPath = getScreenshot(driver, result.getName());
                extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(screenshotPath));
            } else if (result.getStatus() == ITestResult.SKIP){
                extentTest.log(LogStatus.SKIP, "TEST CASE SKIPPED: " + result.getName());
            } else if (result.getStatus() == ITestResult.SUCCESS){
                extentTest.log(LogStatus.PASS, "TEST CASE PASSED: " + result.getName());
            }
            reports.endTest(extentTest);
        }
        Driver.quitDriver();
    }

    private String getScreenshot(WebDriver driver, String screenshotName) throws IOException {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String path = System.getProperty("user.dir") + "/test-report/" + screenshotName + dateName + ".png";
        File finalDestination = new File(path);
        Files.copy(source.toPath(), finalDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return path;
    }
}
