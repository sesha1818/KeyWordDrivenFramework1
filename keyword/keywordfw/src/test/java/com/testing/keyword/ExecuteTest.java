package com.testing.keyword;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import com.testing.util.Constants;

public class ExecuteTest {
@Test
    public void testLogin() throws Exception {
        // TODO Auto-generated method stub
WebDriver driver;
DesiredCapabilities capabilities= DesiredCapabilities.chrome();
ChromeOptions options=new ChromeOptions();
options.addArguments("incognito");
options.addArguments("--disable-notifications");

capabilities.setCapability(ChromeOptions.CAPABILITY, options);

System.setProperty("webdriver.chrome.driver",Constants.CHROME_DRIVER_EXE);
driver=new ChromeDriver(options);
driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
driver.manage().window().maximize();


ReadExcelFile file = new ReadExcelFile();
ReadObject object = new ReadObject();
Properties allObjects = object.getObjectRepository();
UIOperations operation = new UIOperations(driver);


//Read keyword sheet
Sheet sheet = file.readExcel(System.getProperty("user.dir")+ "\\src\\test\\java\\com\\testing\\keyword","TestCase.xlsx" , "KeywordFramework");
//Find number of rows in excel file
    int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();
    //Create a loop over all the rows of excel file to read it
    for (int i = 1; i < rowCount+1; i++) {
        //Loop over all the rows
        Row row = sheet.getRow(i);
        //Check if the first cell contain a value, if yes, That means it is the new testcase name
        if(row.getCell(0).toString().length()==0){
        //Print testcase detail on console
            System.out.println(row.getCell(1).toString()+"----"+ row.getCell(2).toString()+"----"+
            row.getCell(3).toString()+"----"+ row.getCell(4).toString());
        //Call perform function to perform operation on UI
            
            operation.perform(allObjects, row.getCell(1).toString(), row.getCell(2).toString(),
                row.getCell(3).toString(), row.getCell(4).toString());
     }
        else{
            //Print the new testcase name when it started
                System.out.println("New Testcase->"+row.getCell(0).toString() +" Started");
                System.out.println("");
            }
        }
    }
}