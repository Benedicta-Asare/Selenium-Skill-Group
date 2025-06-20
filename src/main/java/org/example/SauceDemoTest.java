package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class SauceDemoTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @Test(priority = 1)
    // Login to the website
    public void testLogin() throws InterruptedException {
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        Thread.sleep(1000);
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_list")));
    }

    @Test(priority = 2)
    // Interact with the product page
    public void testProductPage() throws InterruptedException {
        Select select = new Select(driver.findElement(By.className("product_sort_container")));
        Thread.sleep(1000);
        select.selectByVisibleText("Price (low to high)");
        Thread.sleep(1000);

        driver.findElement(By.id("add-to-cart-sauce-labs-onesie")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("add-to-cart-sauce-labs-bike-light")).click();
        Thread.sleep(1000);

        WebElement cartIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")));
        Assert.assertEquals(cartIcon.getText(), "2", "Cart counter did not match expected value");
    }

    @Test(priority = 3)
    public void testCheckout() throws InterruptedException {
        // Fill out the checkout form
        driver.findElement(By.className("shopping_cart_link")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("checkout")).click();
        Thread.sleep(1000);

        driver.findElement(By.id("first-name")).sendKeys("Tessa");
        driver.findElement(By.id("last-name")).sendKeys("Khan");
        driver.findElement(By.id("postal-code")).sendKeys("00233");
        Thread.sleep(1000);
        driver.findElement(By.id("continue")).click();
        Thread.sleep(2000);

        // Finalize Checkout
        driver.findElement(By.id("finish")).click();
        Thread.sleep(1000);

        WebElement orderConfirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("complete-header")));
        Assert.assertEquals(orderConfirmation.getText(), "Thank you for your order!", "Order completion message did not match");

        driver.findElement(By.id("back-to-products")).click();
    }

    @Test(priority = 4)
    // Logout
    public void testLogout() throws InterruptedException {
        driver.findElement(By.id("react-burger-menu-btn")).click();
        Thread.sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Logout"))).click();
    }

    @AfterClass
    public void tearDown() throws InterruptedException {
        Thread.sleep(1000);
        driver.quit();
    }
}