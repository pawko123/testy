package seleniumTests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class FormTests extends SeleniumTest{
    @Test
    void testSubmitTextBoxForm() {
        driver.get("https://demoqa.com/text-box");

        // Fill the form
        driver.findElement(By.id("userName")).sendKeys("John Doe");
        driver.findElement(By.id("userEmail")).sendKeys("john.doe@example.com");
        driver.findElement(By.id("currentAddress")).sendKeys("123 Main St, Cityville");
        driver.findElement(By.id("permanentAddress")).sendKeys("456 Long Ave, Townsville");

        // Submit the form
        WebElement submitBtn = driver.findElement(By.id("submit"));
        js.executeScript("arguments[0].scrollIntoView(true);", submitBtn);
        submitBtn.click();

        // Wait for output to appear
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(d -> d.findElement(By.id("output")).isDisplayed());

        // Verify output contains expected data
        String nameOutput = driver.findElement(By.id("name")).getText();
        String emailOutput = driver.findElement(By.id("email")).getText();
        String currentAddressOutput = driver.findElement(By.xpath("//p[@id='currentAddress']")).getText();
        String permanentAddressOutput = driver.findElement(By.xpath("//p[@id='permanentAddress']")).getText();

        assertThat(nameOutput, containsString("John Doe"));
        assertThat(emailOutput, containsString("john.doe@example.com"));
        assertThat(currentAddressOutput, containsString("123 Main St"));
        assertThat(permanentAddressOutput, containsString("456 Long Ave"));
    }

    @Test
    void testFormWithInvalidEmail() {
        driver.get("https://demoqa.com/text-box");

        // Fill the form with invalid email
        driver.findElement(By.id("userName")).sendKeys("Jane Doe");
        driver.findElement(By.id("userEmail")).sendKeys("jane.doe[at]example.com"); // invalid
        driver.findElement(By.id("currentAddress")).sendKeys("789 North Ave, Test City");
        driver.findElement(By.id("permanentAddress")).sendKeys("101 East Rd, Testville");

        WebElement emailInput = driver.findElement(By.id("userEmail"));

        // Submit form
        WebElement submitBtn = driver.findElement(By.id("submit"));
        js.executeScript("arguments[0].scrollIntoView(true);", submitBtn);
        submitBtn.click();

        // The form should not submit: no output should appear
        boolean outputVisible = driver.findElements(By.id("output")).stream()
                .anyMatch(WebElement::isDisplayed);
        assertThat("Form should not submit with invalid email", outputVisible, is(false));

        // Check if email input has class "field-error" or red border
        String classAttr = emailInput.getAttribute("class");
        String borderColor = emailInput.getCssValue("border-color");

        assertThat("Email field should show validation error",
                classAttr.contains("field-error") || borderColor.equals("rgb(255, 0, 0)"), is(true));
    }
}
