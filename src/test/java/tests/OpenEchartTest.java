package tests;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Set;

public class OpenEchartTest {

    // Test the login successful scenario
    @Test
    public void testOpenEchartSuccessful() {

        // Open the main page
        System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver"); // Set the path to the chromedriver executable
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.get("http://localhost:8080/");

        // Locate the username and password fields
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        WebElement passwordField = driver.findElement(By.name("password")); 

        // Enter the username and password
        usernameField.sendKeys("oscardoc");
        passwordField.sendKeys("mac2002#");

        // Locate and click the login button
        WebElement loginButton = driver.findElement(By.cssSelector("input[type='submit'][value='Login']"));
        loginButton.click();
        
        // Verify the login was successful
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("http://localhost:8080/oscar/provider/providercontrol.jsp"));
        System.out.println("Login successful, the current URL is: " + currentUrl);

        // Click on the 'Search' tab on the menu bar
        WebElement searchTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@onclick, \"popupPage2('../demographic/search.jsp');\")]")));
        searchTab.click();
        System.out.println("Search demographic page open successful.");

        // Store the current window handle
        String mainWindowHandle = driver.getWindowHandle();

        // Switch to the search demographic window
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        Set<String> allWindowHandles = driver.getWindowHandles();
        for (String windowHandle : allWindowHandles) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        // Ensure the URL of the search demographic window is correct
        wait.until(ExpectedConditions.urlContains("http://localhost:8080/oscar/demographic/search.jsp"));
        System.out.println("Location of the search demographic window is: " + driver.getCurrentUrl());

        // Enter "%" in the search bar and press Enter to get all patients
        WebElement searchBar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("keyword")));
        searchBar.sendKeys("%");
        searchBar.sendKeys(Keys.RETURN);
        System.out.println("Patients are listed on page " + driver.getCurrentUrl());

        // Close the browser
        // driver.quit();

    }
}
