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
		LocalDate ldDepart = null;
		LocalDate ldReturn = null;

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
		ldDepart = LocalDate.parse(depatureDate, dtf);
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
			ldReturn = LocalDate.parse(returnDate, dtf);
			while (ldReturn.isBefore(today)) {
				System.out.println("Date must be today or later.");
				System.out.println("Enter date in this format: dd/MM/yyyy\n");
				returnDate = scanner.nextLine();
				dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				ldReturn = LocalDate.parse(returnDate, dtf);
			}
		}
//		No of Travellers
		System.out.println("Enter number of Adult Travellers (12y+)");
		int adult = scanner.nextInt();
		while ((adult < 1) || (adult > 9)) {
			System.out.println("Please enter a number between 1 and 9");
			adult = scanner.nextInt();
		}
		System.out.println("Enter number of Child Travellers (2y - 12y)");
		int child = scanner.nextInt();
		while ((child < 0) || (child > 6)) {
			System.out.println("Please enter a number between 0 and 6");
			child = scanner.nextInt();
		}
		System.out.println("Enter number of Infant Travellers (Below 2y)");
		int infant = scanner.nextInt();
		while (infant > adult) {
			System.out.println("Number of infants can be more than number of adults. Please enter aagain");
			infant = scanner.nextInt();
				while ((infant < 0) || (infant > 6)) {
					System.out.println("Please enter a number between 0 and 6");
					infant = scanner.nextInt();	
			}
		}

//		Travel Class
		System.out.println("Please choose Travel Class");
		System.out.println("1 for Economy/Premium Economy");
		System.out.println("2 for Premium Economy");
		System.out.println("3 for Business");
		System.out.println("4 for First Class");
		int travel_class = scanner.nextInt();
		while ((travel_class < 1) || (travel_class > 4)) {
			System.out.println("Please enter a number between 1 and 4");
			travel_class = scanner.nextInt();
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

//		Selecting type of trip
		if (trip == 1)
			driver.findElement(By.xpath("//li[@data-cy='oneWayTrip']")).click();
		else
			driver.findElement(By.xpath("//li[@data-cy='roundTrip']")).click();
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
		Thread.sleep(2000);
//		Selecting Depature Date
		String day = String.valueOf(ldDepart.getDayOfMonth());
		DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM");
		String month = ldDepart.format(monthFormatter);
		String year = String.valueOf(ldDepart.getYear());
		String reqmonthyr = (month + " " + year).replaceAll("\\s+", "");
		String currentmonthyr = driver.findElement(By.xpath("(//div[@class='DayPicker-Caption'])[1]/div")).getText();
		while (!((currentmonthyr.replaceAll("\\s+", "")).equals(reqmonthyr))) {
			driver.findElement(By.xpath("//span[@aria-label='Next Month']")).click();
			currentmonthyr = driver.findElement(By.xpath("(//div[@class='DayPicker-Caption'])[1]/div")).getText();
		}
		List<WebElement> dates = driver.findElements(By.xpath(
				"//div[@class='DayPicker-Day'][not(contains(@class, 'outside'))]//div[@class='dateInnerCell']/p[1]"));
		for (WebElement date : dates) {
			if (date.getText().equals(day)) {
				date.click();
				break;
			}
		}
		Thread.sleep(5000);
//		Selecting Return Date
		if (trip == 2) {
			day = String.valueOf(ldReturn.getDayOfMonth());
			month = ldReturn.format(monthFormatter);
			year = String.valueOf(ldReturn.getYear());
			reqmonthyr = (month + " " + year).replaceAll("\\s+", "");
			currentmonthyr = driver.findElement(By.xpath("(//div[@class='DayPicker-Caption'])[1]/div")).getText();
			while (!((currentmonthyr.replaceAll("\\s+", "")).equals(reqmonthyr))) {
				driver.findElement(By.xpath("//span[@aria-label='Next Month']")).click();
				currentmonthyr = driver.findElement(By.xpath("(//div[@class='DayPicker-Caption'])[1]/div")).getText();
			}
			dates = driver.findElements(By.xpath(
					"//div[@class='DayPicker-Day'][not(contains(@class, 'outside'))]//div[@class='dateInnerCell']/p[1]"));
			for (WebElement date : dates) {
				if (date.getText().equals(day)) {
					date.click();
					break;
				}
			}
		}
//		Entering Number of Travellers
		driver.findElement(By.xpath("//*[@data-cy='travellerText']")).click();
		driver.findElement(By.xpath("//li[@data-cy='adults-" + adult + "']")).click();
		driver.findElement(By.xpath("//li[@data-cy='children-" + child + "']")).click();
		driver.findElement(By.xpath("//li[@data-cy='infants-" + infant + "']")).click();
		driver.findElement(By.xpath("//li[@data-cy='travelClass-" + (travel_class - 1) + "']")).click();
		Thread.sleep(5000);

		driver.findElement(By.xpath("//button[@data-cy='travellerApplyBtn']")).click();
		driver.findElement(By.xpath("//p[@data-cy='submit']/a")).click();
		
		driver.quit();
		Thread.sleep(5000);

	}
}
