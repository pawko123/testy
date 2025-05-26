package seleniumTests;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class PlanUZTests extends SeleniumTest{
    @Test
    public void listMyLessons(){
        driver.get("https://plan.uz.zgora.pl/");
        driver.manage().window().setSize(new Dimension(834, 5233));
        driver.findElement(By.linkText("Plan grup")).click();
        driver.findElement(By.linkText("Informatyka")).click();
        driver.findElement(By.partialLinkText("33INF-SSI-SP")).click();
        //odznacznie innych grup
        driver.findElement(By.id("pg6")).click();
        driver.findElement(By.id("pg4")).click();
        driver.findElement(By.id("pg3")).click();
        driver.findElement(By.id("pg2")).click();

        //sprawdzanie czy nie ma zajec innych grup
        List<WebElement> elements = driver.findElements(By.className("PG"));

        boolean noneContainForbiddenChars = elements.stream().noneMatch(el -> {
            String text = el.getText();
            return text.contains("A") || text.contains("C") || text.contains("2") || text.contains("3");
        });

        assertThat("No PG element should contain A, C, 2, or 3", noneContainForbiddenChars, is(true));

        System.out.println("Plan lekcji:\n" + driver.findElement(By.cssSelector(".tab-content")).getText());
    }

    @Test
    public void testLightDarkMode(){
        driver.get("https://plan.uz.zgora.pl/");
        driver.manage().window().setSize(new Dimension(834, 5233));

       //kliknij theme toggle to enable dropdown menu
        WebElement themeToggle = driver.findElement(By.id("theme-toggle"));
        themeToggle.click();

        //sprawdzanie czy dropdown menu jest widoczne
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("ul.dropdown-menu[aria-labelledby='theme-toggle']")));
        assertThat("Dropdown should be visible", dropdown.isDisplayed(), is(true));

        //kliknij na ciemny motyw
        WebElement darkOption = dropdown.findElement(By.cssSelector("li.theme-option[data-theme='dark']"));
        darkOption.click();

        //sprawdzanie czy motyw jest ciemny
        WebElement icon = driver.findElement(By.cssSelector("#theme-toggle i"));
        String darkIconClass = icon.getAttribute("class");
        assertThat("Icon should be moon-stars in dark theme", darkIconClass,
                containsString("bi-moon-stars"));

        WebElement navbar = driver.findElement(By.cssSelector(".navbar-custom"));
        String navbarColor = navbar.getCssValue("background-color");
        assertThat("Navbar background color should be dark", navbarColor,
                containsString("rgb(48, 48, 48)"));

        //wroc do jasnego
        themeToggle.click();
        WebElement lightOption = dropdown.findElement(By.cssSelector("li.theme-option[data-theme='light']"));
        lightOption.click();

        icon = driver.findElement(By.cssSelector("#theme-toggle i"));
        String lightIconClass = icon.getAttribute("class");
        assertThat("Icon should be sun in light theme", lightIconClass,
                containsString("bi-sun-fill"));

        navbar = driver.findElement(By.cssSelector(".navbar-custom"));
        String lightNavbarColor = navbar.getCssValue("background-color");
        assertThat("Navbar background color should be lightgreen", lightNavbarColor,
                containsString("rgb(224, 238, 205)"));
    }
}
