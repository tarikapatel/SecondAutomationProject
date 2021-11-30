package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.expectThrows;

public class MultiTest {
    static WebDriver driver;

    public static void clickOnElement(By by) {
        driver.findElement(by).click();
    }

    public static void typeText(By by, String text) {
        driver.findElement(by).sendKeys(text);
    }

    public static String getTextFromElement(By by) {
        return driver.findElement(by).getText();
    }

    public static String currentTimeStamp() {
        return (new SimpleDateFormat("ddMMyyyyhhmmss")).format(new Date());
    }

    public static void waitForClickable(By by, int timeInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    public static void waitForVisible(By by, int timeInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    @BeforeMethod
    public void openBrowser() {
        System.out.println(currentTimeStamp());
        System.setProperty("webdriver.chrome.driver","src/test/Drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://demo.nopcommerce.com/");
    }

    @Test
    public void verifyUserShouldBeAbleToRegisterSuccessfully() {

        //click on register button
        clickOnElement(By.xpath("//a[@class='ico-register']"));
        //verify user is on register page
        Assert.assertTrue(driver.getCurrentUrl().contains("register"));
        //Type first name
        typeText(By.id("FirstName"), "Johnn");
        //type second name
        typeText(By.id("LastName"), "Smithh");
        //select day from dropdown
        Select selectDay = new Select(driver.findElement(By.name("DateOfBirthDay")));
        selectDay.selectByVisibleText("15");
        //select month from dropdown
        Select selectMonth = new Select(driver.findElement(By.name("DateOfBirthMonth")));
        selectMonth.selectByValue("4");
        //select year from dropdown
        Select selectYear = new Select(driver.findElement(By.name("DateOfBirthYear")));
        selectYear.selectByValue("1926");
        //Type email
        String email = "Johnsmith+" + currentTimeStamp() + "@gmail.com";
        System.out.println(email);
        typeText(By.id("Email"),(email));
        //wait
        waitForVisible(By.id("Newsletter"), 20);
        //click on Newsletter checkbox
        clickOnElement(By.id("Newsletter"));
        //Type password
        typeText(By.id("Password"), "Test123!");
        //type confirm password
        typeText(By.id("ConfirmPassword"), "Test123!");
        //wait for register button is recognised
        waitForClickable(By.id("register-button"), 20);
        //click on register button
        clickOnElement(By.id("register-button"));
        waitForVisible(By.xpath("//div[@class='result']"),40);
        // Verify expected result is equal to actual result
        String actualRegisterSuccessMessage = getTextFromElement(By.xpath("//div[@class='result']"));
        String expectedRegisterSuccessMessage = "Your registration completed";
        assertEquals(actualRegisterSuccessMessage, expectedRegisterSuccessMessage);
    }
    @Test
    public void verifyUserIsAbleToNavigateToDesktopPage() {
        //Click on computer
        clickOnElement(By.xpath("//Div[@class='header-menu']/ul[1]/li[1]/a"));
        //click on desktops
        clickOnElement(By.xpath("//ul[@class='sublist']/li[1]/a"));
        //wait to verify user is on desktops page
        waitForVisible(By.xpath("//div[@class='page-title']/h1"), 40);
        String actualPageTitle = getTextFromElement(By.xpath("//div[@class='page-title']/h1"));
        String expectedPageTitle = "Desktops";
        //Verify actual result = expected
        assertEquals(actualPageTitle, expectedPageTitle);
    }
    @Test
    public void verifyNewsCommentIsSuccessfullyAdded(){
        //click on News Details
        clickOnElement(By.xpath("//div[@class='news-items']/div[2]/div[3]/a[@href='/nopcommerce-new-release']"));
        //type title
        typeText(By.id("AddNewComment_CommentTitle"),"Review");
        //type Comment
        typeText(By.id("AddNewComment_CommentText"),"Good Experience");
        //wait for new comment button is recognised
        waitForClickable(By.xpath("//button[@class='button-1 news-item-add-comment-button']"), 20);
        //click on New Comment button
        clickOnElement(By.xpath("//button[@class='button-1 news-item-add-comment-button']"));
        //wait for visibility on successful message
        waitForVisible(By.xpath("//div[@class='result']"), 40);
        String actualSuccessCommentMessage = getTextFromElement(By.xpath("//div[@class='result']"));
        String expectedSuccessCommentMessage = "News comment is successfully added.";
        //Verify actual result = expected result
        assertEquals(actualSuccessCommentMessage,expectedSuccessCommentMessage);

    }
    @Test
    public void verifyRegisteredUserIsAbleToReferAProductToAFriend(){
        //Register a user successfully
        verifyUserShouldBeAbleToRegisterSuccessfully();
        //user navigates to desktop page
        verifyUserIsAbleToNavigateToDesktopPage();
        //Click on build your own computer
        clickOnElement(By.xpath("//div[@class='product-item']/div[2]/h2/a[@href='/build-your-own-computer']"));
        //click on EmailAFriend button
        clickOnElement(By.xpath("//button[@class='button-2 email-a-friend-button']"));
        //type friends email
        typeText(By.id("FriendEmail"),"JohnSmith@gmail.com");
        // type personal message
        typeText(By.id("PersonalMessage"),"I refer this product to you.");
        //click Send email button
        clickOnElement(By.name("send-email"));
        //Verify actual result = Expected result
        String actualMessageSentConfirmation = getTextFromElement(By.xpath("//div[@class='result']"));
        String expectedMessageSentConfirmation = "Your message has been sent.";
        assertEquals(actualMessageSentConfirmation,expectedMessageSentConfirmation);
    }

    @AfterMethod
    public void closeBrowser() {
        driver.close();
    }
}

