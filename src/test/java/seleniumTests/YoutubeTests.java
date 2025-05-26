package seleniumTests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class YoutubeTests extends SeleniumTest{
    @Test
    void testRandomYouTubeVideoIsPlaying() {
        driver.get("https://www.youtube.com");
        // Wait for the video thumbnails to load
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement rejectButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//button[@aria-label='Nie wyrażaj zgody na wykorzystywanie plików cookie " +
                            "i innych danych do opisanych celów']")));

            rejectButton.click();
            System.out.println("Rejected YouTube rules.");
        } catch (Exception e) {
            System.out.println("No rules dialog appeared.");
        }

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("tp-yt-iron-overlay-backdrop.opened")));

        WebElement openMoreButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[@aria-label='Przewodnik']")));
        openMoreButton.click();

        //idz do na czasie
        WebElement trendingLink = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//yt-formatted-string[text()='Na czasie']")));
        trendingLink.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("yt-tab-shape-wiz__tab")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("scrim")));

        WebElement randomVideo = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@id='video-title']")));

        randomVideo.click();

        // Wait for the video player to load
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("video")));

        // Wait a moment to let video play
        try {
            Thread.sleep(5000); // optional pause for 5s
        } catch (InterruptedException ignored) {}

        Boolean isPlaying = (Boolean) js.executeScript(
                "const video = document.querySelector('video');" +
                        "return video && !video.paused && !video.ended && video.readyState > 2;"
        );

        assertThat("The video should be playing", isPlaying, is(true));
    }
}
