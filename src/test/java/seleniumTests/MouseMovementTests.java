package seleniumTests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.v85.layertree.model.StickyPositionConstraint;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MouseMovementTests extends SeleniumTest{
    @Test
    public void TestInterectiveList(){
        driver.manage().window().maximize();
        driver.get("https://demoqa.com/sortable");

        Actions actions = new Actions(driver);

        // ===== TEST 1: List =====
        System.out.println("🔹 TEST: List");

        List<WebElement> listItems = driver.findElements(By.cssSelector("#demo-tabpane-list .list-group-item"));
        System.out.println("Przed zmianą: ");
        List<String> stringBefore = new ArrayList<>();
        for(WebElement element:listItems){
            String text = element.getText();
            stringBefore.add(text);
            System.out.println(text);
        }

        // Przeciągamy pierwszy element (One) na pozycję piątą (Five)
        WebElement source = listItems.get(0); // One
        WebElement target = listItems.get(4); // Five

        actions
                .clickAndHold(source)
                .moveToElement(target)
                .pause(500)
                .release()
                .perform();


        System.out.println("Po zmianie:");
        List<String> stringsAfter = new ArrayList<>();
        for(WebElement element:listItems){
            String text = element.getText();
            stringsAfter.add(text);
            System.out.println(text);
        }


        assertThat("The order of the list should have changed.",
                stringBefore, is(not(stringsAfter)));

        assertThat("The fifth element in the new list should be 'One'.",
                stringsAfter.get(4),
                is("One"));

    }

    @Test
    public void TextInteractiveGrid(){
        driver.manage().window().maximize();
        driver.get("https://demoqa.com/sortable");

        Actions actions = new Actions(driver);

        // Przejdź do zakładki "Grid"
        WebElement gridTab = driver.findElement(By.id("demo-tab-grid"));
        js.executeScript("arguments[0].click()",gridTab);


        System.out.println("🔹 TEST: Grid");

        List<WebElement> gridItems = driver.findElements(By.cssSelector("#demo-tabpane-grid .list-group-item"));

        System.out.println("Przed zmianą:");
        List<String> stringBefore = new ArrayList<>();
        for (WebElement element : gridItems) {
            String text = element.getText();
            stringBefore.add(text);
            System.out.println(text);
        }

        // Przeciągamy drugi element (Two) na piątą pozycję (Five)
        WebElement source = gridItems.get(1); // Two
        WebElement target = gridItems.get(4); // Five

        actions
                .clickAndHold(source)
                .moveToElement(target)
                .pause(500)
                .release()
                .perform();


        // Pobierz ponownie elementy po przeciągnięciu
        gridItems = driver.findElements(By.cssSelector("#demo-tabpane-grid .list-group-item"));
        System.out.println("Po zmianie:");
        List<String> stringsAfter = new ArrayList<>();
        for (WebElement element : gridItems) {
            String text = element.getText();
            stringsAfter.add(text);
            System.out.println(text);
        }

        assertThat("Kolejność gridu powinna się zmienić.",
                stringBefore, is(not(stringsAfter)));

        assertThat("Element 'Two' powinien być teraz na pozycji piątej.",
                stringsAfter.get(4),
                is("Two"));
    }

    @Test
    public void testResizeBoxWithRestriction() {
        driver.manage().window().maximize();
        driver.get("https://demoqa.com/resizable");
        WebElement box = driver.findElement(By.id("resizableBoxWithRestriction"));
        WebElement handle = box.findElement(By.cssSelector("span.react-resizable-handle"));

        Dimension beforeSize = box.getSize();
        Actions actions = new Actions(driver);

        // Przeciągnięcie uchwytu w prawo i w dół o 100px
        actions.clickAndHold(handle)
                .moveByOffset(200, 200)
                .release()
                .perform();

        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){

        }

        Dimension afterSize = box.getSize();

        System.out.println("Rozmiar przed: " + beforeSize);
        System.out.println("Rozmiar po:    " + afterSize);

        assertThat("Wysokosc nie powinna byc wyzsza niz 300", afterSize.getHeight(), lessThanOrEqualTo(300));
        assertThat("Szerokość powinna się zwiększyć", afterSize.getWidth(), greaterThan(beforeSize.getWidth()));
        assertThat("Wysokość powinna się zwiększyć", afterSize.getHeight(), greaterThan(beforeSize.getHeight()));
    }

    @Test
    public void testDragAndDropWithColorCheck() {
        driver.manage().window().maximize();
        driver.get("https://demoqa.com/droppable");
        Actions actions = new Actions(driver);

        WebElement draggable = driver.findElement(By.id("draggable"));
        WebElement droppable = driver.findElement(By.id("droppable"));

        // Przeciągnij i upuść
        actions.dragAndDrop(draggable, droppable).perform();

        // Sprawdź czy tekst się zmienił po upuszczeniu
        String dropText = droppable.getText();
        assertEquals("Dropped!", dropText, "Element docelowy powinien mieć tekst 'Dropped!'");

        // Sprawdź kolor tła po dropie (powinien być zielony)
        String bgColor = droppable.getCssValue("background-color");
        System.out.println("Kolor tła po dropie: " + bgColor);

        // Kolor w CSS jest zwykle w rgba, dla zielonego to mniej więcej "rgba(70, 130, 180, 1)"
        // Sprawdzimy, czy kolor zawiera wartości typowe dla zielonego (lub możesz dopasować na dokładne wartości)
        // Na stronie jest to rgba(70, 130, 180, 1) (SteelBlue)

        assertTrue(bgColor.contains("70") && bgColor.contains("130") && bgColor.contains("180"),
                "Kolor tła powinien być zielono-niebieski po upuszczeniu");
    }
}
