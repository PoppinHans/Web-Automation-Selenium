package sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class IphoneChecker {
	static final int CONFIG_NUM = 1;
	static final int WAIT_TIME_SEC = 10;
	static final int SLEEP_TIME_MILSEC = 2000;
	static final int LOOP_SIZE = 4;
	static final int NUM_LOOP = 1;
	static final String FILE_PATH = System.getProperty("user.dir")+"\\src\\sample";
	static final String CONF_NAME = "ConfigurationSheet.xlsx";
	static final String CHEC_NAME = "CheckListSheet.xlsx";
	static final String REPO_NAME = "ReportSheet.xlsx";
	static final String SHEET_NAME = "Sheet1";
	static final String XPATH_AVAILABLE = "/html/body/overlay[8]/materializer/div/div/product-locator/div[2]/div[2]/div[1]/div[1]/div[2]/div[2]/div";
	
	static int count = 0;
	static boolean[] isDone = new boolean[1];
	static String[] loopPath = new String[LOOP_SIZE];
	static String[] loopResult = new String[LOOP_SIZE];
	
	public static void main(String[] args) throws InterruptedException, IOException {
		// load configuration file
		List<String> confFile = Read.configuration(FILE_PATH, CONF_NAME, SHEET_NAME).get(CONFIG_NUM);
		String from = confFile.get(0);
		String password = confFile.get(1);
		String to = confFile.get(2);
		String sub = confFile.get(3);
		String msg = confFile.get(4);
		
		// create new web driver
		List<String> filenames = new ArrayList<>();
		String fileName;
		WebDriver driver = new ChromeDriver();
		driver.get("http://www.apple.com");
		driver.manage().window().maximize();
		
		// create new web element
		WebElement element;
		
		// load checklist file
		isDone[0] = false;
		List<String> checFile = Read.checklist(FILE_PATH, CHEC_NAME, SHEET_NAME, loopPath, isDone);
		if (isDone[0] == true) {
			// Mailer.send(from, password, to, sub, msg, filenames);
			return;
		}
		
		List<String> dataToWrite = new ArrayList<>();
		for (int i = 0; i < checFile.size() - NUM_LOOP; i++) {
			try {
				element = driver.findElement(By.xpath(checFile.get(i)));
				element.click();
				Thread.sleep(SLEEP_TIME_MILSEC);
				dataToWrite.add("Pass");
				fileName = Screen.take(driver, FILE_PATH, ++count);
				filenames.add(new String(fileName));
			} catch (Exception E) {
				dataToWrite.add("Fail");
				continue;
			}
		}
		Arrays.fill(loopResult, "Fail");
		while (Arrays.asList(loopResult).contains("Fail")) {
			for (int i = 0; i < LOOP_SIZE; i++) {
				if (loopResult[i].equals("Pass")) {
					continue;
				}
				String xPath = loopPath[i]; 
				try {
					element = driver.findElement(By.xpath(xPath));
					element.click();
					Thread.sleep(SLEEP_TIME_MILSEC);
					
					element = (new WebDriverWait(driver, WAIT_TIME_SEC)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPATH_AVAILABLE)));
					System.out.println("Available!");
					loopResult[i] = "Pass";
				} catch (org.openqa.selenium.TimeoutException timeEx) {
					System.out.println("Not available after waiting for: " +  WAIT_TIME_SEC + " seconds.");
					continue;
				} finally {
					fileName = Screen.take(driver, FILE_PATH, ++count);
					filenames.add(new String(fileName));
				}
			}
		}
		dataToWrite.add("Pass");
		
		Write.checklist(FILE_PATH, CHEC_NAME, SHEET_NAME, dataToWrite, loopResult);
		Write.report(FILE_PATH, REPO_NAME, SHEET_NAME, dataToWrite, loopResult);
		Mailer.send(from, password, to, sub, msg, filenames);
	}
}
