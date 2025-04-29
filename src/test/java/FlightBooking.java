import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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

//      Accepting details from customer
		Scanner scanner = new Scanner(System.in);
//		OneWay or Round Trip
		System.out.println("Enter 1. For One Way Trip \nEnter 2. For Round Trip");
		int trip = scanner.nextInt();
		while (!((trip == 1) || (trip == 2))) {
			System.out.println("Please enter correct number\n");
			trip = scanner.nextInt();
		}
		scanner.nextLine();
		
//		From City & To City
		System.out.println("Enter From City for flight booking");
		String customerFromCity = scanner.nextLine();
		System.out.println("Enter To City for flight booking");
		String customerToCity = scanner.nextLine();
//		Departure Date
		System.out.println("Enter depature date in this format: dd/MM/yyyy\n");
		String depatureDate = scanner.nextLine();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate ldDepart = LocalDate.parse(depatureDate, dtf);
		LocalDate today = LocalDate.now();
		while (ldDepart.isBefore(today)) {
			System.out.println("Date must be today or later.");
			System.out.println("Enter date in this format: dd/MM/yyyy\n");
			depatureDate = scanner.nextLine();
			dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			ldDepart = LocalDate.parse(depatureDate, dtf);
		}
//		Return Date
		if (trip == 2) {
			System.out.println("Enter return date in this format: dd/MM/yyyy\n");
			String returnDate = scanner.nextLine();
			LocalDate ldReturn = LocalDate.parse(returnDate, dtf);
			while (ldReturn.isBefore(today)) {
				System.out.println("Date must be today or later.");
				System.out.println("Enter date in this format: dd/MM/yyyy\n");
				returnDate = scanner.nextLine();
				dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				ldReturn = LocalDate.parse(returnDate, dtf);
			}
		}
		scanner.close();

//      Opening site and Closing pop-ups
		WebDriver driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.get("https://www.makemytrip.com/");
		try {
			driver.findElement(By.xpath("//span[@data-cy='closeModal']")).click();
			driver.findElement(By.xpath("//span[@data-cy='CustomModal_10']")).click();
		} catch (Exception e) {
			// TODO: handle exception
		}

//      Selecting From City
		Actions action = new Actions(driver);
		WebElement fromCity = driver.findElement(By.id("fromCity"));
		fromCity.click();
		Thread.sleep(2000);
		WebElement inputFromCity = driver.findElement(By.xpath("//input[@placeholder='From']"));
		inputFromCity.sendKeys(customerFromCity);
		Thread.sleep(2000);
		action.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();

//      Selecting To City
		WebElement toCity = driver.findElement(By.id("toCity"));
		toCity.click();
		WebElement inputToCity = driver.findElement(By.xpath("//input[@placeholder='To']"));
		Thread.sleep(2000);
		inputToCity.sendKeys(customerToCity);
		Thread.sleep(2000);
		action.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();

		String day = String.valueOf(ldDepart.getDayOfMonth());
		DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM");
		String month = ldDepart.format(monthFormatter); // Gives "January", "February", etc.
		String year = String.valueOf(ldDepart.getYear());
		String reqmonthyr = month + " " + year;

//		Selecting departure date
		String currentmonthyr = driver.findElement(By.xpath("(//div[@class='DayPicker-Caption'])[1]/div")).getText();

		while (!(currentmonthyr.equals(reqmonthyr))) {
			driver.findElement(By.xpath("//span[@aria-label='Next Month']")).click();
			currentmonthyr = driver.findElement(By.xpath("(//div[@class='DayPicker-Caption'])[1]/div")).getText();
		}
		List<WebElement> dates = driver
				.findElements(By.xpath("(//div[@class='DayPicker-Month'])[1]//div[@class='dateInnerCell']/p[1]"));
		for (WebElement date : dates) {
			if (date.getText().equals(day)) {
				date.click();
				break;
			}
		}

//            
//        Thread.sleep(5000);
//        driver.quit();

	}
}
