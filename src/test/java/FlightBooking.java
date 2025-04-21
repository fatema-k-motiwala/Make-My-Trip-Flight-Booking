import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.lang.model.element.Element;

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
        try {
        	driver.findElement(By.xpath("//span[@data-cy='closeModal']")).click();
            driver.findElement(By.xpath("//span[@data-cy='CustomModal_10']")).click();	
		} catch (Exception e) {
			// TODO: handle exception
		}
                
        //Selecting From City
        Actions action = new Actions(driver);
        WebElement fromCity = driver.findElement(By.id("fromCity"));
        fromCity.click();
        Thread.sleep(2000);
        WebElement inputFromCity = driver.findElement(By.xpath("//input[@placeholder='From']"));
        inputFromCity.sendKeys("Mumbai");
        Thread.sleep(2000);
        action.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();
        
//      Selecting To City
        WebElement toCity = driver.findElement(By.id("toCity"));
        toCity.click();
        WebElement inputToCity= driver.findElement(By.xpath("//input[@placeholder='To']"));
        Thread.sleep(2000);
        inputToCity.sendKeys("Dubai");
        Thread.sleep(2000);
        action.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();
        
// 	    Accepting Date From Customer 
        Scanner scanner = new Scanner(System.in);
		System.out.println("Enter date in this format: dd/MM/yyyy\n");
		String inputDate  = scanner.nextLine();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate ld = LocalDate.parse(inputDate, dtf);
		LocalDate today = LocalDate.now();
		
		while (ld.isBefore(today)) {
	        System.out.println("Date must be today or later.");
	        System.out.println("Enter date in this format: dd/MM/yyyy\n");
			inputDate  = scanner.nextLine();
			dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			ld = LocalDate.parse(inputDate, dtf);
	    } 
		scanner.close();
		String day = String.valueOf(ld.getDayOfMonth());
		DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM");
		String month = ld.format(monthFormatter); // Gives "January", "February", etc.
		String year = String.valueOf(ld.getYear());
		String reqmonthyr = month + " " + year;
		
//		Selecting departure date
		String currentmonthyr = driver.findElement(By.xpath("(//div[@class='DayPicker-Caption'])[1]/div")).getText();
		
		while(!(currentmonthyr.equals(reqmonthyr))){
			driver.findElement(By.xpath("//span[@aria-label='Next Month']")).click();
			currentmonthyr = driver.findElement(By.xpath("(//div[@class='DayPicker-Caption'])[1]/div")).getText();
		}
		List<WebElement> dates = driver.findElements(By.xpath("(//div[@class='DayPicker-Month'])[1]//div[@class='dateInnerCell']/p[1]"));
		for	(WebElement date : dates) {
			if(date.getText().equals(day)) {
				date.click();
				break;
			}
		}
		
		
		
//            
//        Thread.sleep(5000);
//        driver.quit();

	}
}
