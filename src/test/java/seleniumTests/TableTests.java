package seleniumTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TableTests extends SeleniumTest{
    @ParameterizedTest
    @CsvSource({
            "1, Last Name",
            "2, First Name",
            "3, Email",
            "4, Due",
            "5, Web Site"
    })
    public void testTableSorting(int columnIndex, String columnName){
        driver.get("https://the-internet.herokuapp.com/tables");

        // XPathy
        String headerXPath = "//table[@id='table1']//th[" + columnIndex + "]";
        String columnXPath = "//table[@id='table1']/tbody/tr/td[" + columnIndex + "]";

        // Pobierz dane PRZED kliknięciem
        List<WebElement> beforeClickCells = driver.findElements(By.xpath(columnXPath));
        List<String> originalOrder = new ArrayList<>();
        for (WebElement cell : beforeClickCells) {
            originalOrder.add(cell.getText().trim());
        }

        // Kliknij nagłówek kolumny
        driver.findElement(By.xpath(headerXPath)).click();

        // Pobierz dane PO kliknięciu
        List<WebElement> afterClickCells = driver.findElements(By.xpath(columnXPath));
        List<String> newOrder = new ArrayList<>();
        for (WebElement cell : afterClickCells) {
            newOrder.add(cell.getText().trim());
        }

        // Przygotuj oczekiwaną kolejność
        List<String> expectedOrder = new ArrayList<>(originalOrder);
        if (columnName.equals("Due")) {
            // Usuń znaki $ i konwertuj na liczby do sortowania
            expectedOrder.sort(Comparator.comparingDouble(s -> Double.parseDouble(s.replace("$", ""))));
        } else {
            expectedOrder.sort(String.CASE_INSENSITIVE_ORDER);
        }

        assertThat("Dane nie zostały poprawnie posortowane rosnąco po kliknięciu nagłówka.",
                newOrder, is(expectedOrder));

        driver.quit();
    }
}
