package sample;
 
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
 
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Test_13{
	public List<String> readExcel(String filePath,String fileName,String sheetName) throws IOException{

	    List<String> ans = new ArrayList<>();
	    //Create an object of File class to open xlsx file
	    
	    File file =    new File(filePath+"\\"+fileName);

	    //Create an object of FileInputStream class to read excel file

	    FileInputStream inputStream = new FileInputStream(file);

	    Workbook guru99Workbook = null;

	    //Find the file extension by splitting file name in substring  and getting only extension name

	    String fileExtensionName = fileName.substring(fileName.indexOf("."));

	    //Check condition if the file is xlsx file

	    if(fileExtensionName.equals(".xlsx")){

	    //If it is xlsx file then create object of XSSFWorkbook class

	    guru99Workbook = new XSSFWorkbook(inputStream);

	    }

	    //Check condition if the file is xls file

	    else if(fileExtensionName.equals(".xls")){

	        //If it is xls file then create object of XSSFWorkbook class

	        guru99Workbook = new HSSFWorkbook(inputStream);

	    }

	    //Read sheet inside the workbook by its name

	    Sheet guru99Sheet = guru99Workbook.getSheet(sheetName);

	    //Find number of rows in excel file

	    int rowCount = guru99Sheet.getLastRowNum()-guru99Sheet.getFirstRowNum();

	    //Create a loop over all the rows of excel file to read it

	    for (int i = 1; i < rowCount+1; i++) {

	        Row row = guru99Sheet.getRow(i);
	        ans.add(row.getCell(0).getStringCellValue());
	    }
	    return ans;
	}
	
	public void writeExcel(String filePath,String fileName,String sheetName, List<String> dataToWrite) throws IOException{

        //Create an object of File class to open xlsx file

        File file =    new File(filePath+"\\"+fileName);

        //Create an object of FileInputStream class to read excel file

        FileInputStream inputStream = new FileInputStream(file);

        Workbook guru99Workbook = null;

        //Find the file extension by splitting  file name in substring and getting only extension name

        String fileExtensionName = fileName.substring(fileName.indexOf("."));

        //Check condition if the file is xlsx file

        if(fileExtensionName.equals(".xlsx")){

        //If it is xlsx file then create object of XSSFWorkbook class

        guru99Workbook = new XSSFWorkbook(inputStream);

        }

        //Check condition if the file is xls file

        else if(fileExtensionName.equals(".xls")){

            //If it is xls file then create object of XSSFWorkbook class

            guru99Workbook = new HSSFWorkbook(inputStream);

        }

        

    //Read excel sheet by sheet name    

    Sheet sheet = guru99Workbook.getSheet(sheetName);

    //Get the current count of rows in excel file

    int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();
    for (int i = 1; i < rowCount+1; i++) {

        Row row = sheet.getRow(i);
        Cell cell = row.createCell(1);
        cell.setCellValue(dataToWrite.get(i - 1));
    }
    //Close input stream

    inputStream.close();

    //Create an object of FileOutputStream class to create write data in excel file

    FileOutputStream outputStream = new FileOutputStream(file);

    //write data in the excel file

    guru99Workbook.write(outputStream);

    //close output stream

    outputStream.close();
	}

	public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException { 
		WebDriver driver = new ChromeDriver();
		driver.get("http://www.google.com");
		driver.manage().window().maximize();
 
		// Create an object of Test_13 class
		Test_13 objExcelFile = new Test_13();
		int entrySize = 0;
		    
		// Prepare the path of excel file
		// searchIn stores the search keywords
		// searchOut stores the result addresses
		String filePath = System.getProperty("user.dir")+"\\src\\sample";

		List<String> searchIn = new ArrayList<>();
		List<String> searchOut = new ArrayList<>();
		searchIn = objExcelFile.readExcel(filePath,"readData.xlsx","data_1");
		entrySize = searchIn.size();

		for (int i = 0; i < entrySize; i++){
			WebElement searchbox = driver.findElement(By.name("q"));
			String keyword = searchIn.get(i);
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
			searchOut.add(ret);
		}
		objExcelFile.writeExcel(System.getProperty("user.dir")+"\\src\\sample","readData.xlsx","data_1", searchOut);
		Thread.sleep(2000);
		driver.quit();
	}
}
