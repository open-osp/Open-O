package tests;

import org.testng.annotations.Test;

import org.testng.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class GoogleTest {
    
    @Test
    public void testGooglePage() {

        // Open Google
        System.setProperty("webdriver.chrome.driver", "/opt/homebrew/bin/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.google.com");
        
        // Verify the title
        Assert.assertEquals("Google", driver.getTitle());
        System.out.println("The title is: " + driver.getTitle());

        // Close the browser
        driver.quit();
    }

}