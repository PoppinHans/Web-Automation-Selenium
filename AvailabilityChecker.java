package sample;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AvailabilityChecker {

	static final String from = "from@gmail.com";
	static final String password = "password";
	static final String to = "to@gmail.com";
	static final String sub = "SendEmails Testing";
	static final String msg = "Iphone X is available. I am wikibear, yeah...";
	static final String filePath = System.getProperty("user.dir")+"\\src\\sample";
	static final int timeWait = 10;
	static int count = 0;
	
	public static void main(String[] args) throws InterruptedException, FileNotFoundException {
		// TODO Auto-generated method stub
		List<String> filenames = new ArrayList<>();
		String fileName;
		WebDriver driver = new ChromeDriver();
		driver.get("http://www.apple.com");
		driver.manage().window().maximize();

		WebElement element;
		element = driver.findElement(By.xpath("//*[@id=\"ac-globalnav\"]/div/ul[2]/li[4]/a"));
		element.click();
		Thread.sleep(2000);
		fileName = Screen.take(driver, filePath, ++count);
		filenames.add(new String(fileName));
		
		element = driver.findElement(By.xpath("//*[@id=\"chapternav\"]/div/ul/li[1]/a/figure"));
		element.click();
		Thread.sleep(2000);
		fileName = Screen.take(driver, filePath, ++count);
		filenames.add(new String(fileName));
		
		element = driver.findElement(By.xpath("//*[@id=\"ac-localnav\"]/div/div[2]/div[2]/div[2]/div[2]/a"));
		element.click();
		Thread.sleep(2000);
		fileName = Screen.take(driver, filePath, ++count);
		filenames.add(new String(fileName));
		
		element = driver.findElement(By.xpath("//*[@id=\"dimensionScreensize-5_8inch-select\"]"));
		element.click();
		Thread.sleep(2000);
		fileName = Screen.take(driver, filePath, ++count);
		filenames.add(new String(fileName));
		
		element = driver.findElement(By.xpath("//*[@id=\"Item2VERIZON/US_label\"]/img"));
		element.click();
		Thread.sleep(2000);
		fileName = Screen.take(driver, filePath, ++count);
		filenames.add(new String(fileName));
		
		// uncomment the following to test blocking

		element = driver.findElement(By.xpath("//*[@id=\"tabs_dimensionColor\"]/fieldset/ul/li[1]/div/div[2]/img"));
		element.click();
		Thread.sleep(2000);
		fileName = Screen.take(driver, filePath, ++count);
		filenames.add(new String(fileName));
		
		// uncomment the following to test passing
		/*
		element = driver.findElement(By.xpath("//*[@id=\"tabs_dimensionColor\"]/fieldset/ul/li[2]/div/div[2]/img"));
		element.click();
		Thread.sleep(2000);
		fileName = Screen.take(driver, filePath, ++count);
		filenames.add(new String(fileName));
		*/
		
		element = driver.findElement(By.xpath("//*[@id=\"tabs_dimensionCapacity\"]/fieldset/ul/li[1]/div[2]/div[2]/div/div/div/button"));
		element.click();
		Thread.sleep(2000);
		fileName = Screen.take(driver, filePath, ++count);
		filenames.add(new String(fileName));
		
		/*
		element = driver.findElement(By.xpath("/html/body/overlay[8]/materializer/div/div/product-locator/div[2]/div[2]/div[1]/div[1]/div[2]/div[2]/button/span[1]"));
		System.out.println(element.getText());
	
		element = driver.findElement(By.xpath("//*[@id=\"delivery-MQCK2LL/A\"]"));
		element.click();
		Thread.sleep(2000);
		*/
		try {
			element = (new WebDriverWait(driver, timeWait)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/overlay[8]/materializer/div/div/product-locator/div[2]/div[2]/div[1]/div[1]/div[2]/div[2]/div")));
			System.out.println("Available!");
			Mailer.send(from, password, to, sub, msg, filenames);
		}
		catch (org.openqa.selenium.TimeoutException timeEx) {
			System.out.println("Not available after waiting for: " +  timeWait + " seconds.");
			timeEx.printStackTrace();
			driver.quit();
			return;
		}	
		Thread.sleep(2000);
		driver.quit();
	}
}
