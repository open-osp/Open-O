package tests;

import org.testng.annotations.Test;

import org.testng.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class GoogleTest {

    @Test
    public void testGooglePage() {

        // Open Google with headless Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://www.google.com");
        
        // Verify the title
        Assert.assertEquals("Google", driver.getTitle());
        System.out.println("The title is: " + driver.getTitle());

        // Close the browser
        driver.quit();
    }

}