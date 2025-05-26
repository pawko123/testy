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
import static org.hamcrest.Matchers.startsWith;

public class RezerwacjaZGTests extends SeleniumTest{


    @ParameterizedTest
    @CsvSource({
            "'a.logo', " +
                    "https://www.bookero.pl",
            "'a.footer-link[href=\"https://bip.zielonagora.pl/891/18176/" +
                    "Instrukcja_dostepu_do_systemu_rezerwacyjnego_w_celu_umowienia_" +
                    "wizyty_w_Biurze_Rejestracji_Pojazdow_i_Praw_Jazdy/']', " +
                    "https://bip.zielonagora.pl/891/18176/Instrukcja_dostepu_do_" +
                    "systemu_rezerwacyjnego_w_celu_umowienia_wizyty_w_Biurze_Rejestracji_Pojazdow_i_Praw_Jazdy/",
            "'a.footer-link[href=\"https://bip.zielonagora.pl/535/Ochrona_Danych_Osobowych/']', " +
                    "https://bip.zielonagora.pl/535/Ochrona_Danych_Osobowych/"
    })
    public void correctLinkNavigation(String cssSelector, String expectedUrlPrefix) {
        driver.get("https://rezerwacja.zielona-gora.pl/");
        WebElement link = driver.findElement(By.cssSelector(cssSelector));
        link.click();

        // Wait for URL to start with expected prefix
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(d -> d.getCurrentUrl().startsWith(expectedUrlPrefix));

        String currentUrl = driver.getCurrentUrl();
        assertThat("URL should start with expected prefix", currentUrl, startsWith(expectedUrlPrefix));

        // Optional: Navigate back to test the next link
        driver.navigate().back();
    }
}
