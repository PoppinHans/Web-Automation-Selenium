package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.healthmarketscience.jackcess.*;
	
public class Test_17_2 {
	public static List<String> readAccess(String filePath, String fileName, String tableName, String columnName) throws IOException {
		// read Column "columnName" and save to a List<String>
		List<String> ans = new ArrayList<>();
		File file = new File(filePath + "\\" + fileName);
		Database db = DatabaseBuilder.open(file);
		Table table = db.getTable(tableName);
		
		for (Row row : table) {
			String ret = (String) row.get(columnName);
			ans.add(ret);
		}
		db.close();
		return ans;
	}
	
	public static void writeAccess(String filePath, String fileName, String tableName, String columnName, String columnWrite, List<String> dataToSearch, List<String> dataToWrite) throws IOException {
		// in entry row, if key to find equals search data
		// find key to write and update with write data
		File file = new File(filePath + "\\" + fileName);
		Database db = DatabaseBuilder.open(file);
		Table table = db.getTable(tableName);
		
		Cursor cursor = CursorBuilder.createCursor(table);
		for (int i = 0; i < dataToSearch.size(); i++) {
			for (Row row : cursor.newIterable().addMatchPattern(columnName, dataToSearch.get(i))) {
				row.put(columnWrite, dataToWrite.get(i));
				table.updateRow(row);
			}
		}
		db.close();
	}
	
	public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException { 
		WebDriver driver = new ChromeDriver();
		driver.get("http://www.google.com");
		driver.manage().window().maximize();
		
		int entrySize = 0;
		String filePath = System.getProperty("user.dir")+"\\src\\sample";
	    String fileName = "dataBase.accdb";
	    String tableName = "countries";
		String columnName = "CountryName";
		String columnWrite = "SearchPage";
		
		List<String> dataToSearch = new ArrayList<>();
		List<String> dataToWrite = new ArrayList<>();
		dataToSearch = readAccess(filePath, fileName, tableName, columnName);
		entrySize = dataToSearch.size();

		for (int i = 0; i < entrySize; i++){
			WebElement searchbox = driver.findElement(By.name("q"));
			String keyword = dataToSearch.get(i);
			String ret = "";

			searchbox.clear();
			searchbox.sendKeys(keyword);
			searchbox.submit();       
			driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);

			// Search each keyword and find website addresses
			List<WebElement> list = driver.findElements(By.className("_Rm"));
			for (int j = 0; j < list.size(); j++) {
				ret = ret + list.get(j).getText() + " + ";
			}
			dataToWrite.add(ret);
		}
		writeAccess(filePath, fileName, tableName, columnName, columnWrite, dataToSearch, dataToWrite);
		Thread.sleep(2000);
		driver.quit();
	}
}
