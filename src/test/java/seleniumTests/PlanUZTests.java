package seleniumTests;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class PlanUZTests {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "/home/pawko/IdeaProjects/testy/Driver/geckodriver");
        driver = new FirefoxDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void p1() {
        driver.get("http://www.plan.uz.zgora.pl/");
        driver.manage().window().setSize(new Dimension(834, 5233));
        driver.findElement(By.linkText("Plan nauczycieli")).click();
        driver.findElement(By.linkText("B")).click();
        driver.findElement(By.linkText("dr inż. Jacek Bieganowski")).click();
        System.out.println("" + driver.findElement(By.cssSelector(".main")).getText());
        assertThat(driver.findElement(By.cssSelector(".main")).getText(), containsString("Seminarium IMEI"));
    }
}
