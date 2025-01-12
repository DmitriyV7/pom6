package com.askomdch.driver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class Driver {

    private static WebDriver driver;

    private Driver(){}

    public static WebDriver getDriver(String browser){
        if (driver == null){
            try{
            switch (browser){
                case "chrome":
                    WebDriverManager.chromedriver().clearDriverCache().setup();
                    WebDriverManager.chromedriver().clearResolutionCache().setup();
                    ChromeOptions options = new ChromeOptions();
//                    options.addArguments("--headless"); // Run in headless mode
                    options.addArguments("--no-sandbox"); // Bypass OS security model
                    options.addArguments("--disable-gpu"); // Applicable to Windows os only
                    options.addArguments("--window-size=1920,1080");
//                    options.addArguments("--start-maximized"); // Maximize the browser on start
                    options.addArguments("--enable-automation");
                    options.addArguments("--disable-infobars");
                    options.addArguments("--disable-dev-shm-usage");
                    driver = new ChromeDriver(options);
                    break;
                case "firefox":
                    driver = new FirefoxDriver();
                    break;
                case "edge":
                    driver  = new EdgeDriver();
                    break;
                default:
                    throw new RuntimeException("Invalid Browser type: " + browser);
            }
            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
            }catch (Exception e){
                System.err.println("Error initializing WebDriver: " + e.getMessage());
            }
        }


        return driver;
    }

    public static void quitDriver(){
        if (driver != null){
            driver.quit();
            driver = null;
        }
    }
}