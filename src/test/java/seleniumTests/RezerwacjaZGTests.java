package seleniumTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

public class RezerwacjaZGTests extends SeleniumTest{


    @Test
    public void checkBookeroImageLink(){
        driver.get("https://rezerwacja.zielona-gora.pl/");
        WebElement bookeroImageLink = driver.findElement(By.cssSelector("a.logo"));

        // Click the image link
        bookeroImageLink.click();

        // Wait for the redirection to complete
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(webDriver -> webDriver.getCurrentUrl().startsWith("https://www.bookero.pl"));

        // Assert that the current URL is correct
        assertThat(driver.getCurrentUrl(), startsWith("https://www.bookero.pl"));
    }

    @ParameterizedTest
    @CsvSource({
            "Regulamin, https://bip.zielonagora.pl/891/18176/Instrukcja_dostepu_do_systemu_rezerwacyjnego_w_celu_umowienia_wizyty_w_Biurze_Rejestracji_Pojazdow_i_Praw_Jazdy/",
            "Polityka prywatności, https://bip.zielonagora.pl/535/Ochrona_Danych_Osobowych/"
    })
    public void testFooterLinksRedirection(String linkText, String expectedUrl) {
        // Navigate to the target page
        driver.get("https://rezerwacja.zielona-gora.pl/");

        // Locate the link by its text
        WebElement link = driver.findElement(By.linkText(linkText));

        // Click the link
        link.click();

        String originalWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        // Wait for the redirection to complete
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(webDriver -> webDriver.getCurrentUrl().equals(expectedUrl));

        // Assert that the current URL matches the expected URL
        assertThat(driver.getCurrentUrl(), is(expectedUrl));
    }
}
