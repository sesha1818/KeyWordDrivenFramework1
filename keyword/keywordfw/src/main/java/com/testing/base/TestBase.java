package com.testing.base;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.testing.util.Constants;
import com.testing.util.ExtentManager;
public class TestBase {


	public WebDriver driver;
	public ExtentReports extent=ExtentManager.getInstance();
	public ExtentTest logger;

	//Open Browser, initialize driver object and launch url
	public void init(String bType,String url){
		if(bType.equals("Firefox")){
			try{
				System.setProperty("webdriver.gecko.driver", Constants.FIREFOX_DRIVER_EXE);
				driver= new FirefoxDriver();
			} catch (Exception e){
				e.printStackTrace();
				Assert.fail("Error opening Browser");
			}
		}
		else if (bType.equals("IE")) {
			try {
				System.setProperty("webdriver.ie.driver", Constants.IE_DRIVER_EXE);
				InternetExplorerOptions options = new InternetExplorerOptions();
				options.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, "true");
				options.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, "true");
				driver= new InternetExplorerDriver(options);
			} catch (Exception e){
				e.printStackTrace();
				Assert.fail("Error opening Browser");
			}
		}
		else if(bType.equals("Chrome")){
			try {
				//This piece of code will be used to open browser incognito mode, so that even if your gmail is already logged in, it won't be an issue	
				DesiredCapabilities capabilities= DesiredCapabilities.chrome();
				ChromeOptions options=new ChromeOptions();
				options.addArguments("incognito");
				options.addArguments("--disable-notifications");

				capabilities.setCapability(ChromeOptions.CAPABILITY, options);

				System.setProperty("webdriver.chrome.driver",Constants.CHROME_DRIVER_EXE);
				driver=new ChromeDriver(options);
			} catch (Exception e){
				System.out.println("Exception in Chrome Browser");
				e.printStackTrace();
				Assert.fail("Error opening Browser");
			}
		}
		else 
			System.out.println("Browser Name is invalid or not supported in the framework");

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(300, TimeUnit.SECONDS);
		System.out.println("Browser Opened - "+ bType);
		driver.get(url);
		driver.manage().timeouts().pageLoadTimeout(300, TimeUnit.SECONDS);
		System.out.println(url+" is Opened - ");
	}

	//quit webdriver session
	public void terminate() {
		driver.quit();
	}

	//close current browser
	public void close() {
		driver.close();
	}

	public void log(String... text) {
		try {
			String logText="";
			for (String temp: text) {			
				logText+=temp;
			}
			//logger.log(Status.INFO, logText);

			System.out.println(logText);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	// Element high lighter code
	public void highLightElement(WebDriver d, WebElement e) 
	{
		JavascriptExecutor js=(JavascriptExecutor)d; 
		js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", e);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		js.executeScript("arguments[0].setAttribute('style','border: solid 2px white');", e); 
	}
	
	public void jsClick(WebElement el) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("arguments[0].click();", el);
			log("Element clicked");
		} catch (Exception e){
			System.out.println("=============================================================");
			log("Exception-jsClick(): "+e.getMessage());
			takeScreenShot();
			e.printStackTrace();
			System.out.println("=============================================================");
		}
	}

	// Scroll Up page by co-ordinates
	public void scrollUp() throws Exception{
		try {
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("window.scrollBy(0,-250)", "");
			log("Page scrolled up");
		} catch (Exception e){
			System.out.println("=============================================================");
			log("Exception-scrollUp(): "+e.getMessage());
			takeScreenShot();
			e.printStackTrace();
			System.out.println("=============================================================");
		}       
	}

	// Scroll Down page by co-ordinates
	public void scrollDown() throws Exception{
		try {
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("window.scrollBy(0,250)", "");
			log("Page scrolled down");   
		} catch (Exception e){
			System.out.println("=============================================================");
			log("Exception-scrollDown(): "+e.getMessage());
			takeScreenShot();
			e.printStackTrace();
			System.out.println("=============================================================");
		}    
	}

	// Scroll element to view
	public void scrollIntoView(WebElement element) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
			log("Page scrolled down");
		} catch (Exception e){
			System.out.println("=============================================================");
			log("Exception-scrollIntoView(): "+e.getMessage());
			takeScreenShot();
			e.printStackTrace();
			System.out.println("=============================================================");
		}    
	}

	public void reportPass(String msg){
		logger.log(Status.PASS, msg);
		System.out.println("**PASS: "+msg);
	}

	public void reportFailure(String msg) {
		logger.log(Status.FAIL, msg);
		System.out.println("**FAIL: "+msg);
		takeScreenShot();		
	}

	public void reportHardFailure(String failureMessage) throws IOException{
		logger.log(Status.FAIL, failureMessage);
		takeScreenShot();
		Assert.fail(failureMessage);
	}

	// Take screenshot of page and save in screenshots folder
	public void takeScreenShot() {
		Date date=new Date();
		String screenshotFile=date.toString().replace(":", "_").replace(" ", "_")+".png";
		String filePath=Constants.REPORT_PATH+"screenshots\\"+screenshotFile;

		try {
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Take screenshot of page and save in screenshots folder
		public void takeTheScreenShot(String test) {
			Date date=new Date();
			String screenshotFile=date.toString().replace(":", "_").replace(" ", "_")+".jpg";
			String filePath=Constants.REPORT_PATH+"screenshots\\"+test+"\\"+screenshotFile;

			try {
				File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(scrFile, new File(filePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	// Take screenshot of page and save in screenshots folder including ToolTip
	public void takeScreenShotTip(String test) {
		try {
			Date date = new Date();
			String screenshotFile = date.toString().replace(":", "_").replace(" ", "_");
			String filePath = Constants.REPORT_PATH + "screenshots";
			File output = new File(filePath);
			output.mkdir();
			filePath = filePath + "\\" + test;
			output = new File(filePath);
			output.mkdir();
			filePath = filePath + "\\" + screenshotFile + ".png";
			output = new File(filePath);
			output.createNewFile();
			BufferedImage img = getScreenAsBufferedImg();
			ImageIO.write(img, "png", output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BufferedImage getScreenAsBufferedImg() {
		BufferedImage img = null;
		try {
			Robot r;
			r = new Robot();
			r.keyPress(KeyEvent.VK_PRINTSCREEN);
			wait(1);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			if(clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
				Transferable transferable = clipboard.getContents(null);
				img = (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
				if(img == null) {
					throw new Exception("Printscreen was unsuccessful. No image content in clipboard.");
				}
			}else {
				throw new Exception("Printscreen was unsuccessful. No image content in clipboard.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}

	// Take screenshot of page and save in screenshots folder
	public void takeScreenShot(String fileName) {
		Date date=new Date();
		String screenshotFile=fileName.split("\\.")[0]+date.toString().replace(":", "_").replace(" ", "_")+"."+fileName.split("\\.")[1];
		String filePath=Constants.REPORT_PATH+"screenshots\\"+screenshotFile;

		try {
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//logger.log(Status.INFO,logger.addScreenCapture(filePath));
		try {
			logger.info("Screenshot",MediaEntityBuilder.createScreenCaptureFromPath(filePath).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Code for Static wait
	public void wait(int timeToWaitInSec){
		try {
			Thread.sleep(timeToWaitInSec * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Read data from excel -> Start row from 1
	public String readData(String fileName, String sheetName, int rowIndex, int cellIndex) throws Exception {
		File location = new File(fileName);
		FileInputStream fis = new FileInputStream(location);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet1 = wb.getSheet(sheetName);

		String data;
		try {
			data = sheet1.getRow(rowIndex).getCell(cellIndex).getStringCellValue();
		} catch (NullPointerException e) {
			data = "";
		}

		wb.close();

		return data;
	}

	// Write data to excel
	public void setData(String fileName, String sheetName, int rowIndex, int cellIndex, String data) throws Exception {
		File location = new File(fileName);
		FileInputStream fis = new FileInputStream(location);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet1 = wb.getSheet(sheetName);
		if (sheet1.getRow(rowIndex) == null) {
			sheet1.createRow(rowIndex).createCell(cellIndex).setCellValue(data);
		} else {
			sheet1.getRow(rowIndex).createCell(cellIndex).setCellValue(data);
		}
		FileOutputStream fos = new FileOutputStream(location);
		wb.write(fos);
		wb.close();
	}

	// Get count of rows from excel
	public int getRowCount(String fileName, String sheetName) throws IOException {
		File location = new File(fileName);
		FileInputStream fis = new FileInputStream(location);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet1 = wb.getSheet(sheetName);
		wb.close();
		return sheet1.getLastRowNum()+1;
	}
	
	//To switch the top most window
	public void switchWindow() throws InterruptedException{
		Set<String> winid = driver.getWindowHandles();
        Iterator<String> iter = winid.iterator();
        iter.next();
        String tab = iter.next();
        Thread.sleep(3000);
        driver.switchTo().window(tab);
	}
	
	//To wait until element is visible
	public WebElement waitForElementToVisible(WebElement element) throws Exception{
		WebElement find;
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(500))
				.pollingEvery(Duration.ofMillis(500)).ignoring(Exception.class);
		find = wait.until(ExpectedConditions.visibilityOf(element));
		return find;
	}
	
	//To wait until element is clickable
	public WebElement waitForElementToClickable(WebElement element) throws Exception{
		WebElement find;
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(100))
				.pollingEvery(Duration.ofMillis(500)).ignoring(Exception.class);
		find = wait.until(ExpectedConditions.elementToBeClickable(element));
		return find;
	}
	
	
	//To check the Page is loaded 
	public void checkPageReady() throws InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (int i = 0; i <= 50; i++) {
			if (js.executeScript("return document.readyState").toString().equals("complete")) {
				break;
			}
			else
			{
				Thread.sleep(1000);
			}
		}
	}
	
	//To select option from drop down via Index/Text/Value
	public void selectFromDropDown(WebElement element,String type,String text,String index,String value){
		Select slt=new Select(element);
		if(type.equalsIgnoreCase("text")){
			slt.selectByVisibleText(text);
		}else if(type.equalsIgnoreCase("index")){
			slt.selectByIndex(Integer.valueOf(index));
		}else if(type.equalsIgnoreCase("value")){
			slt.selectByValue(value);
		}
	}
}
