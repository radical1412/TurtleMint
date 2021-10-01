package com.Turtlemint;

import com.Common.Base;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

public class TurtleMint_POM {

    WebDriver driver;
    Base prop;
    SoftAssert softAssert;
    WebDriverWait wait;

    @FindBy(xpath = "//div[@class = 'banner-bg-content']//a[@href = '/life-insurance']")
    WebElement selectLife;
    @FindBy(xpath = "//div[@data-url = 'term-plan']")
    WebElement selectTermPlan;
    @FindBy(id = "Radio-M")
    WebElement selectMale;
    @FindBy(id = "Radio-F")
    WebElement selectFemale;
    @FindBy(xpath = "//input[@placeholder= 'Enter Date']")
    WebElement tbDate;
    @FindBy(xpath = "//select[@class][1]")
    WebElement selectYear;
    @FindBy(xpath = "//select[@class][2]")
    WebElement selectMonth;
    @FindBy(id = "Radio-true")
    WebElement chewYes;
    @FindBy(id = "Radio-false")
    WebElement chewNo;
    @FindBy(name = "maxIncome")
    WebElement ddIncome;
    @FindBy(name = "coverAmount")
    WebElement ddCover;
    @FindBy(name = "customerName")
    WebElement tbName;
    @FindBy(name = "paidUserMobile")
    WebElement tbMobile;
    @FindBy(name = "paidUserEmail")
    WebElement tbEmail;
    @FindBy(xpath = "//button[span[text() = 'Next']]")
    WebElement btnNext;
    @FindBy(xpath = "//button[span[text() = 'Back']]")
    WebElement btnBack;
    @FindBy(xpath = "//button[@class = 'btn green submit']")
    WebElement btnTutOK;
    @FindBy(xpath = "//div[img[@src = '/images/expert.svg']]")
    WebElement btnToWait;


    public TurtleMint_POM(WebDriver driver) {
        this.driver = driver;
        prop = new Base();
        softAssert = new SoftAssert();
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 20, 100);
    }

    public void selectInsurance() {
        waitClickable(selectLife).click();
        waitClickable(selectTermPlan).click();
    }

    public void setGender(String gender) {
        if (gender.equalsIgnoreCase("Male")) {
            waitClickable(selectMale).click();
        } else if (gender.equalsIgnoreCase("Female")) {
            waitClickable(selectFemale).click();
        } else {
            Assert.fail("Please provide proper gender.");
        }
    }

    public void setDOB(String date) {
        /*Use format of date as dd/mm/yyyy*/
        String selectDate = add25Years(date);
        String[] splitDate = selectDate.split("-");
        tbDate.click();
        new Select(selectYear).selectByValue(splitDate[0]);
        new Select(selectMonth).selectByValue(splitDate[1]);
        driver.findElement(By.xpath("//div[contains(@class, 'react-datepicker') and text() = '" + splitDate[2] + "']")).click();
    }

    public void setChew(String option) {
        if (option.equalsIgnoreCase("Yes")) {
            waitClickable(chewYes).click();
        } else if (option.equalsIgnoreCase("No")) {
            waitClickable(chewNo).click();
        } else {
            Assert.fail("Please provide proper option.");
        }
    }

    public void setAnnualIncome(double income) {
        //Give income in Lacs
        ddIncome.click();
        if (3 > income) {
            driver.findElement(By.xpath("//li[@value = '299999']")).click();
        } else if (3 < income && income < 5) {
            driver.findElement(By.xpath("//li[@value = '499999']")).click();
        } else if (5 < income && income < 7) {
            driver.findElement(By.xpath("//li[@value = '699999']")).click();
        } else if (7 < income && income < 10) {
            driver.findElement(By.xpath("//li[@value = '999999']")).click();
        } else if (10 < income && income < 15) {
            driver.findElement(By.xpath("//li[@value = '1499999']")).click();
        } else if (15 < income) {
            driver.findElement(By.xpath("//li[@value = '9900000']")).click();
        }
        goNext();
    }

    public void setAssuredValue(String cover) {
        waitClickable(ddCover).click();
        driver.findElement(By.xpath("//li[contains(text(), '" + cover + "')]")).click();
        goNext();
    }

    public void setDetails(String name, String mobile, String email) {
        waitClickable(tbName).sendKeys(name);
        waitClickable(tbMobile).sendKeys(mobile);
        waitClickable(tbEmail).sendKeys(email);
        goNext();
    }

    public void viewDetails(String provider) throws InterruptedException {
        waitPresent(By.xpath("//button[@class = 'btn green submit']"));
        while (!checkIntercepted(By.xpath("//button[@class = 'btn green submit']"))) {
            waitClickable(btnTutOK).click();
        }
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        boolean a = jse.executeScript("return document.readyState").equals("complete");
        boolean b = (Long) jse.executeScript("return jQuery.active") == 0;
        System.out.println(a + "\n" + b);
        String currentCompany = null;
        while (!(a && b)) {
            System.out.println("Inside While");
            Thread.sleep(1000);
        }
        List<WebElement> btnsViewDetails = driver.findElements(By.xpath("//div[contains(@class, 'layout-xs-column')]//button[@data-auto]"));
        if (btnsViewDetails.isEmpty()) {
            softAssert.fail("There are no providers in this range.");
        } else {
            Iterator<WebElement> itr = btnsViewDetails.iterator();
            while (itr.hasNext()) {
                currentCompany = itr.next().getAttribute("data-auto");
                if (currentCompany.contains(provider)) {
                    driver.findElement(By.xpath("//div[contains(@class, 'layout-xs-column')]//button[@data-auto ='" + currentCompany + "']")).click();
                    break;
                }
            }
            if (!currentCompany.contains(provider)) {
                System.out.println("None of the options match the required provider.");
            }
        }
    }

    public String getURL() {
        String URL = driver.getCurrentUrl();
        return URL;
    }

    public void goNext() {
        waitClickable(btnNext).click();
    }

    public void goBack() {
        waitClickable(btnBack).click();
    }

    public static String add25Years(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate currentDate = LocalDate.parse(date, formatter);
        String newDate = currentDate.plusYears(25).toString();
        return newDate;
    }

    public WebElement waitClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        return element;
    }

    public boolean checkIntercepted(By element) {
        boolean a = true;
        int i = 0;
        while (a && i <= 10) {
            try {
                driver.findElement(element).click();
                a = false;
            } catch (ElementClickInterceptedException e) {
                if (i > 10) {
                    a = false;
                } else {
                    a = true;
                }
            }
            i++;
        }
        return !a;
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
