package tests;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Set;
import java.util.List;

public class AddAppointmentTest extends BaseTest {

    // Test the add appointment successful scenario
    @Test
    public void testAddAppointmentSuccessful() throws InterruptedException {
        WebDriver driver = createHeadlessDriver();
        WebDriverWait wait = createWait(driver);

        try {
            // Login
            login(driver, wait);
            Assert.assertTrue(isLoginSuccessful(driver));
            System.out.println("Login successful, the current URL is: " + driver.getCurrentUrl());

            // Click the "08:00" button to open the add appointment window
            WebElement appointmentButton = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("08:00")));
            appointmentButton.click();

            // Store the current window handle
            String mainWindowHandle = driver.getWindowHandle();

            // Switch to the add appointment window
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
            Set<String> allWindowHandles = driver.getWindowHandles();
            for (String windowHandle : allWindowHandles) {
                if (!windowHandle.equals(mainWindowHandle)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }

            // Ensure the URL of the add appointment window is correct
            wait.until(ExpectedConditions.urlContains("oscar/appointment/addappointment.jsp"));

            // Locate the search field, type patient's first name, and click the patient suggestion
            WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("keyword")));
            searchField.sendKeys("Alex");
            Thread.sleep(1000);
            List<WebElement> suggestions = driver.findElements(By.tagName("li"));
            for (WebElement suggestion : suggestions) {
                if (suggestion.getText().contains("ALEX, JOHN")) {
                    suggestion.click();
                    break;
                }
            }

            // Select reason
            Select reasonSelect = new Select(driver.findElement(By.name("reasonCode")));
            reasonSelect.selectByVisibleText("Testing");

            // Scroll to the bottom of the page
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

            // Click the "Add Appointment" button
            WebElement addAppointmentButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("addButton")));
            addAppointmentButton.click();

            // Display the main page for 2 sec
            Thread.sleep(2000);
        } finally {
            driver.quit();
        }
    }
}
