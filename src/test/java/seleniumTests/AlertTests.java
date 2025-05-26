package seleniumTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AlertTests extends SeleniumTest{
    @Test
     void alertButtonTest() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");
        WebElement button = driver.findElement(By.id("alertButton"));
        button.click();


        Alert alert = driver.switchTo().alert();
        assertThat(alert.getText(),is("You clicked a button"));
        Thread.sleep(100);
        alert.accept();

        assertThrows(NoAlertPresentException.class, () -> {
            driver.switchTo().alert();
        });
    }

    @Test
    void testTimedAlert() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("timerAlertButton")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        long start = System.currentTimeMillis();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        long duration = System.currentTimeMillis() - start;

        System.out.println("Alert apeared after: "+ duration + " ms");
        assertThat(duration / 1000, is(5L));
        assertThat(alert.getText(), is("This alert appeared after 5 seconds"));
        Thread.sleep(100);
        alert.accept();
    }

    @ParameterizedTest
    @ValueSource(strings = { "accept", "dismiss" })
    void testConfirmationAlert(String action) throws InterruptedException{
        driver.get("https://demoqa.com/alerts");

        driver.findElement(By.id("confirmButton")).click();
        Alert alert = driver.switchTo().alert();
        Thread.sleep(100);

        assertThat(alert.getText(), is("Do you confirm action?"));

        // Perform action based on parameter
        if (action.equals("accept")) {
            alert.accept();
        } else {
            alert.dismiss();
        }

        String resultText = driver.findElement(By.id("confirmResult")).getText();

        // Validate result
        if (action.equals("accept")) {
            assertThat(resultText, containsString("Ok"));
        } else {
            assertThat(resultText, containsString("Cancel"));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Zły bot","Dobry bot"})
    void testPromptAlert(String inputText) throws InterruptedException{
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("promtButton")).click();

        Alert alert = driver.switchTo().alert();
        assertThat(alert.getText(), is("Please enter your name"));


        alert.sendKeys(inputText);
        Thread.sleep(100);
        alert.accept();

        String resultText = driver.findElement(By.id("promptResult")).getText();
        assertThat(resultText, containsString(inputText));
    }
}
