import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

public class FlightBooking {

	@Test
	public void bookFlight() throws InterruptedException {
		ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.popups", 0);
        options.setExperimentalOption("prefs", prefs);
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://www.makemytrip.com/");
        
        //Closing pop-ups
        driver.findElement(By.xpath("//span[@data-cy='closeModal']")).click();
        driver.findElement(By.xpath("//span[@data-cy='CustomModal_10']")).click();
        
        //Selecting From City
        Actions action = new Actions(driver);
        WebElement fromCity = driver.findElement(By.xpath("//div[@data-cy='flightSW']//input[@id='fromCity']"));
        fromCity.click();
        Thread.sleep(2000);
        fromCity.sendKeys("Mumbai");
        Thread.sleep(2000);
        action.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();

//      Selecting To City
        WebElement toCity = driver.findElement(By.xpath("//div[@data-cy='flightSW']//input[@id='toCity']"));
        toCity.click();
        WebElement toCityInput = driver.findElement(By.xpath("//div[@data-cy='flightSW']//input[@placeholder='To']"));
        Thread.sleep(2000);
        toCityInput.sendKeys("Dubai");
        Thread.sleep(2000);
        action.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();
        
        Thread.sleep(5000);
        driver.quit();

	}
}
