package com.Flipkart;

import com.Common.Base;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class FlipKart_POM {

    WebDriver driver;
    WebDriverWait wait;
    Base prop;
    SoftAssert softAssert;
    String URL;
    HttpURLConnection connection;

    public FlipKart_POM(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 20, 150);
        prop = new Base();
        softAssert = new SoftAssert();
    }

    By tbSearch = By.xpath("//input[@class = '_3704LK']");
    By btnCloseLogin = By.xpath("//button[@class = '_2KpZ6l _2doB4z']");
    By checkLoad = By.xpath("//div[@class = '_3U-Vxu']");

    public void exitLogin(){
        waitClickable(btnCloseLogin).click();
    }

    public void searchItem(String item) {
        driver.findElement(tbSearch).sendKeys(item, Keys.RETURN);
    }

    public void checkRedirection() {
        waitPresent(checkLoad);
        String currentURL = driver.getCurrentUrl();
        int index = currentURL.indexOf("m/");
        String baseUrl = currentURL.substring(0, index+1);
        List<WebElement> links = driver.findElements(By.xpath("//a[@class = '_2rpwqI']"));
        Iterator<WebElement> it = links.iterator();
        while(it.hasNext()){
            URL = it.next().getAttribute("href");
            if(URL == null || URL.isEmpty()){
                System.out.println(URL + "\nThere is not redirection.\n");
                continue;
            }
            if(!URL.startsWith(baseUrl)){
                System.out.println(URL + "\nURL belongs to another domain, skipping it.\n");
                continue;
            }
            try {
                connection = (HttpURLConnection)(new URL(URL).openConnection());
                connection.setRequestMethod("HEAD");
                connection.connect();
                int respCode = connection.getResponseCode();

                if(respCode >= 400){
                    System.out.println(URL+"\nFailure");
                }
                else{
                    System.out.println(URL+"\nSuccess");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public WebElement waitClickable(By element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        return driver.findElement(element);
    }

    public Boolean waitPresent(By element) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(element));
            return true;
        } catch (Exception e) {
            softAssert.fail("The Element is not Present!");
            return false;
        }
    }
}
