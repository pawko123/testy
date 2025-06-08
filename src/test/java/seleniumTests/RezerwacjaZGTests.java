package seleniumTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        WebElement clickableLink = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(link));
        clickableLink.click();

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

    @Test
    public void testRezerwacjaZielonaGora() {
        driver.get("https://rezerwacja.zielona-gora.pl/");
        driver.manage().window().setSize(new Dimension(1024, 768));

        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        // Kliknij w element otwierający listę rozwijaną
        try {
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".multiselect__select")
            ));

            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", dropdown);
            dropdown.click();
            js.executeScript("arguments[0].click();", dropdown);
            System.out.println("Kliknięto w element otwierający listę rozwijaną.");
            Thread.sleep(2000); // 2 sekundy opóźnienia
        } catch (Exception e) {
            System.out.println("Nie udało się kliknąć w element listy rozwijanej: " + e.getMessage());
        }
        // Wybierz opcję "Odbiór prawo jazdy" z listy rozwijanej
        try {
            WebElement selectOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(), 'Odbiór prawo jazdy')]")));
            js.executeScript("arguments[0].click();", selectOption);
            System.out.println("Wybrano opcję: Odbiór prawo jazdy");
            Thread.sleep(2000); // 2 sekundy opóźnienia
        } catch (Exception e) {
            System.out.println("Nie udało się wybrać opcji 'Odbiór prawo jazdy': " + e.getMessage());
        }

        // Zmień miesiąc na lipiec 2025
        try {
            WebElement nextMonthButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(@class, 'calendar-nav-next')]")));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", nextMonthButton);
            js.executeScript("arguments[0].click();", nextMonthButton);
            System.out.println("Zmieniono miesiąc na lipiec 2025.");
            Thread.sleep(2000); // 2 sekundy opóźnienia
        } catch (Exception e) {
            System.out.println("Nie udało się zmienić miesiąca na lipiec: " + e.getMessage());
        }

        // Wybierz datę w lipcu
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.calendar-days-list-cell.is-open.is-valid")));
            List<WebElement> availableDates = driver.findElements(
                    By.cssSelector("div.calendar-days-list-cell.is-open.is-valid"));
            assertTrue(availableDates.size() > 0, "Nie znaleziono wolnych terminów w kalendarzu.");
            WebElement firstAvailableDate = availableDates.get(0);
            String selectedDateText = firstAvailableDate.getText().trim();
            js.executeScript("arguments[0].click();", firstAvailableDate);
            System.out.println("Wybrano wolny termin: " + selectedDateText);
            Thread.sleep(2000); // 2 sekundy opóźnienia
        } catch (Exception e) {
            System.out.println("Nie udało się wybrać wolnego terminu: " + e.getMessage());
        }

        // Wybierz godzinę
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.hours-list-item")));
            List<WebElement> availableTimes = driver.findElements(
                    By.cssSelector("div.hours-list-item"));
            assertTrue(availableTimes.size() > 0, "Nie znaleziono dostępnych godzin.");
            WebElement firstAvailableTime = availableTimes.get(0);
            String selectedTimeText = firstAvailableTime.getText().trim();
            js.executeScript("arguments[0].click();", firstAvailableTime);
            System.out.println("Wybrano godzinę: " + selectedTimeText);
            Thread.sleep(2000); // 2 sekundy opóźnienia
        } catch (Exception e) {
            System.out.println("Nie udało się wybrać godziny: " + e.getMessage());
        }

        // Kliknij przycisk "Przejdź do podsumowania"
        try {
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(@class, 'submit-button-step-1')]")));
            js.executeScript("arguments[0].click();", submitButton);
            System.out.println("Kliknięto przycisk 'Przejdź do podsumowania'.");
            Thread.sleep(2000); // 2 sekundy opóźnienia
        } catch (Exception e) {
            System.out.println("Nie udało się kliknąć przycisku 'Przejdź do podsumowania': " + e.getMessage());
        }
        // Wpisz imię i nazwisko "Jan Kowalski"
        try {
            WebElement nameInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("bookero-plugin-customer-name")));
            nameInput.click();
            nameInput.clear();
            nameInput.sendKeys("Jan Kowalski");
            System.out.println("Wpisano imię i nazwisko: Jan Kowalski");
            Thread.sleep(2000); // 2 sekundy opóźnienia
        } catch (Exception e) {
            System.out.println("Nie udało się wpisać imienia i nazwiska: " + e.getMessage());
        }

        // Wpisz email "jankowalski@wp.pl"
        try {
            WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("bookero-plugin-customer-email")));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", emailInput);
            js.executeScript("arguments[0].click();", emailInput);
            emailInput.clear();
            emailInput.sendKeys("jankowalski@wp.pl");
            System.out.println("Wpisano email: jankowalski@wp.pl");
            Thread.sleep(2000); // 2 sekundy opóźnienia
        } catch (Exception e) {
            System.out.println("Nie udało się wpisać emaila: " + e.getMessage());
        }

        // Wpisz numer telefonu "420 666 999"
        try {
            WebElement phoneInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("input.vti__input")));
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", phoneInput);
            js.executeScript("arguments[0].click();", phoneInput);
            phoneInput.clear();
            phoneInput.sendKeys("420 666 999");
            System.out.println("Wpisano numer telefonu: 420 666 999");
            Thread.sleep(2000); // 2 sekundy opóźnienia
        } catch (Exception e) {
            System.out.println("Nie udało się wpisać numeru telefonu: " + e.getMessage());
        }

        // Zaznacz checkbox
        try {
            WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("div.switcher.is-agreement")));
            js.executeScript("arguments[0].click();", checkbox);
            System.out.println("Zaznaczono checkbox.");
            Thread.sleep(2000); // 2 sekundy opóźnienia
        } catch (Exception e) {
            System.out.println("Nie udało się zaznaczyć checkboxa: " + e.getMessage());
        }

        // Kliknij przycisk "Wróć do rezerwacji"
        try {
            WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button.back-button")));
            js.executeScript("arguments[0].click();", backButton);
            System.out.println("Kliknięto przycisk 'Wróć do rezerwacji'.");
            Thread.sleep(2000); // 2 sekundy opóźnienia
        } catch (Exception e) {
            System.out.println("Nie udało się kliknąć przycisku 'Wróć do rezerwacji': " + e.getMessage());
        }
    }
}
