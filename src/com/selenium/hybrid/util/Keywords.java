package com.selenium.hybrid.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;


public class Keywords {
	
	public Properties CONFIG = null;
	public Properties OR = null;
	public WebDriver driver = null;
	
	public static Keywords keywords = null;
	
	//initializing xlsx file
	public static XLSReader suiteXLS = new XLSReader(System.getProperty("user.dir")+"/src/com/selenium/hybrid/xls/ShoeShop Suite.xlsx");
	
	//Singleton
	public static Keywords getInstance() 
	{
		keywords = new Keywords();
		
		return keywords;
	}
	
	private Keywords() 
	{
		//Loading properties files
		
		try{
	
			CONFIG = new Properties();
			
			FileInputStream fistr = new FileInputStream(System.getProperty("user.dir")+"/src/com/selenium/hybrid/config/config.properties");
					
			CONFIG.load(fistr);
			
			OR = new Properties();
			
			fistr = new FileInputStream(System.getProperty("user.dir")+"/src/com/selenium/hybrid/config/OR.properties");
			
			OR.load(fistr);
		}
		catch(Exception ex)
		{
			System.out.println("Error initializing properties files:"+ex.getMessage());
		}
	}

	
	public void executeKeywords(String testCaseName,Hashtable<String,String> data)
	{
		System.out.println("Executing keywords for the test case:"+testCaseName);
		String keyword = null;
		String objectKey = null;
		String dataColValue = null;
		
		for(int rowNum=2;rowNum<=suiteXLS.getRowCount("TestSteps");rowNum++)
		{
			if(suiteXLS.getCellData("TestSteps", rowNum, "TCID")!= null && suiteXLS.getCellData("TestSteps", rowNum, "TCID").equalsIgnoreCase(testCaseName))
			{
				keyword = suiteXLS.getCellData("TestSteps", rowNum, "Keyword");
				objectKey = suiteXLS.getCellData("TestSteps", rowNum, "Object");
				dataColValue = suiteXLS.getCellData("TestSteps", rowNum, "Data");
				
				String result = null;
				
				if(keyword.equalsIgnoreCase("openBrowser"))
				{
					result=openBrowser(dataColValue);
				}
				else if(keyword.equalsIgnoreCase("navigate"))
				{
					result=navigate(dataColValue);
				}
				else if(keyword.equalsIgnoreCase("click"))
				{
					result=click(objectKey);
				}
				else if(keyword.equalsIgnoreCase("clickByLinkText"))
				{
					result=clickByLinkText(data.get(dataColValue));
				}
				else if(keyword.equalsIgnoreCase("input"))
				{
					result=input(objectKey,data.get(dataColValue));
				}
				else if(keyword.equalsIgnoreCase("validateShoeContent"))
				{
					result=validateShoeContent();
				}
				else if(keyword.equalsIgnoreCase("validateEmailSubscription"))
				{
					result=validateEmailSubscription(data.get(dataColValue));
				}
				else if(keyword.equalsIgnoreCase("closeBrowser"))
				{
					result=closeBrowser();
				}
				
				System.out.println("result:"+result);
				
				String fileName = null;
				if(!result.equals("Pass"))
				{
					if(testCaseName.equals("MonthlyShoeDisplaysTest"))
					    fileName = testCaseName+"_"+keyword+"_"+data.get("Month")+".jpg";
					else if(testCaseName.equals("EmailSubscriptionTest"))
						fileName = testCaseName+"_"+keyword+"_"+data.get("Email")+".jpg";
					
					try{
						File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						
						FileUtils.copyFile(srcFile, new File(System.getProperty("user.dir")+"/screenshots/"+fileName));
					}
					catch(Exception ex)
					{
						System.out.println("Err taking screenshot...");
						
						System.out.println(ex.getMessage());
					}
					
					Assert.assertTrue(false, result);
				}
			}
		}
	}
	
	
	public String openBrowser(String browser,String version,String platform)
	{
		try{
			DesiredCapabilities cap = new DesiredCapabilities();
			
			if(browser.equalsIgnoreCase("Mozilla"))
			{
				cap = DesiredCapabilities.firefox();
			}
			
			if(browser.equalsIgnoreCase("Chrome"))
			{
				cap = DesiredCapabilities.chrome();
			}
			
			if(browser.equalsIgnoreCase("Edge"))
			{
				cap = DesiredCapabilities.edge();
			}
			
			if(browser.equalsIgnoreCase("IE"))
			{
				cap = DesiredCapabilities.internetExplorer();
			}
			
			cap.setVersion(version);
			
			if(platform.equalsIgnoreCase("Windows"))
				cap.setPlatform(Platform.WINDOWS);
			
			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),cap);
		}
		catch(Exception ex)
		{
			System.out.println("Error opening Remote Browser");
			
			ex.printStackTrace();
			
			return "Fail-Error opening Remote Browser";
		}
		return "Pass";
	}
	
	public String openBrowser(String browserName)
	{
		System.out.println("browserName:"+browserName);
		System.out.println("config browserName:"+CONFIG.getProperty(browserName));
		try{
		if(CONFIG.getProperty(browserName).equalsIgnoreCase("Mozilla"))
		{
			driver = new FirefoxDriver();
		}
		else if(CONFIG.getProperty(browserName).equalsIgnoreCase("Chrome"))
		{
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/drivers/chromedriver.exe");
			
			driver = new ChromeDriver();
		}
		else if(CONFIG.getProperty(browserName).equalsIgnoreCase("IE"))
		{
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"/drivers/IEServerDriver.exe");
			
			driver = new InternetExplorerDriver();
		}
		/*else if(CONFIG.getProperty(browserName).equalsIgnoreCase("Edge"))
		{
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")+"/drivers/MicrosoftWebDriver.exe");
			
			driver = new EdgeDriver();
		}*/
		driver.manage().window().maximize();
		
		driver.manage().timeouts().implicitlyWait(Integer.parseInt(CONFIG.getProperty("implicitWait")), TimeUnit.SECONDS);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return "Fail to open the browser";
		}
		return "Pass";
	}
	
	public String navigate(String testURL)
	{
		try{
		driver.get(CONFIG.getProperty(testURL));
		}
		catch(Exception ex)
		{
			return "Fail-not able to navigate to the URL-"+testURL;
		}
		
		return "Pass";
	}
	
	public String closeBrowser()
	{
		try{
			if(driver != null)
			{
				driver.close();
			}
		}
		catch(Exception ex)
		{
			System.out.println("Problem closing browser");
			return "Fail-Problem closing browser";
		}
		
		return "Pass";
	}
	
	public String click(String xpathKey)
	{
		try{
			driver.findElement(By.xpath(OR.getProperty(xpathKey))).click();
		}
		catch(Exception ex)
		{
			return "Fail-Not able to click the element with xpath key:"+xpathKey;
		}
		
		return "Pass";
	}
	
	public String clickByLinkText(String text)
	{
		try{
			driver.findElement(By.linkText(text)).click();
		}
		catch(Exception ex)
		{
			return "Fail-Not able to click the element with Link text:"+text;
		}
		
		return "Pass";
	}
	
	public String input(String xpathKey,String data)
	{
		try{
			driver.findElement(By.xpath(OR.getProperty(xpathKey))).sendKeys(data);
		}
		catch(Exception ex)
		{
			return "Fail-Not able to input the data in the element with xpathKey:"+xpathKey;
		}
		
		return "Pass";
	}
	
	
	/*******************App Specific Functions ****************/
	
	public String validateShoeContent()
	{
		String result = "";
		try{
			
			List<org.openqa.selenium.WebElement> listElms = driver.findElements(By.xpath(OR.getProperty("shoeList_xpath")));
		    
		    System.out.println("Number of shoes displayed:"+listElms.size());
		    
		    if(listElms.size() > 0)
		    {
			    for(int i=0;i<listElms.size();i++)
			    {
			    	
			    	String brand = driver.findElement(By.xpath(OR.getProperty("shoeList_xpath")+"["+(i+1)+"]"+"/div/table/tbody/tr[1]/td[2]")).getText();
			    	String name = driver.findElement(By.xpath(OR.getProperty("shoeList_xpath")+"["+(i+1)+"]"+"/div/table/tbody/tr[2]/td[2]")).getText();
			    	String price = driver.findElement(By.xpath(OR.getProperty("shoeList_xpath")+"["+(i+1)+"]"+"/div/table/tbody/tr[3]/td[2]")).getText();
			    	String desc = driver.findElement(By.xpath(OR.getProperty("shoeList_xpath")+"["+(i+1)+"]"+"/div/table/tbody/tr[4]/td[2]")).getText();
			    	String month = driver.findElement(By.xpath(OR.getProperty("shoeList_xpath")+"["+(i+1)+"]"+"/div/table/tbody/tr[5]/td[2]")).getText();
			    	
			    	System.out.println("brand:"+brand);
			    	
			    	System.out.println("name:"+name);
			    	
			    	System.out.println("price:"+ price);
			    	
			    	System.out.println("desc:"+desc);
			    	
			    	System.out.println("month:"+month);
			    	
			    				    	
			    	/*SoftAssert softAssert=new SoftAssert();
			    	
			    	softAssert.assertTrue(brand.length()>0, "There is no Brand of the shoe");
			    	
			    	
			    	softAssert.assertTrue(name.length()>0,"There is no name of the shoe");
			    	
			    	
			    	softAssert.assertTrue(price.length()>0,"There is no price of the shoe");
			    	
			    	
			    	softAssert.assertTrue(desc.length()>0,"There is no description of the shoe");
			    	
			    	
			    	softAssert.assertTrue(month.length()>0,"There is no release month of the shoe");*/
			    	
			    	
			    	
			    	
			    	org.openqa.selenium.WebElement imgTd = driver.findElement(By.xpath(OR.getProperty("shoeList_xpath")+"["+(i+1)+"]"+"/div/table/tbody/tr[6]/td[1]"));
			    	
			    	List<org.openqa.selenium.WebElement> imgElms = imgTd.findElements(By.tagName("img"));
			    	
			    	System.out.println("Image Elements:"+imgElms.size());
			    	
			    	//softAssert.assertTrue(imgElms.size()>0,"There is no Shoe Image");
			    	
			    	String imgSrc = imgElms.get(0).getAttribute("src");
			    	
			    	System.out.println("image source:"+imgSrc);
			    	
			    	//softAssert.assertTrue(imgSrc.length()>0, "Image src is null.No Image to display.");
			    	
			    	//softAssert.assertAll();
			    	
			    	
			    	
			    	
			    	if(brand.length()>0&&name.length()>0&&price.length()>0&&desc.length()>0&&month.length()>0&&imgElms.size()>0&&imgSrc.length()>0)
			    	{
			    		result = "Pass";
			    	}
			    	else{
			    		result="";
			    		if(brand.length()<= 0)
				    		result += "Fail -There is no Brand of the shoe"+"\n";
				    	if(name.length()<=0)
				    		result += "Fail -There is no name of the shoe"+"\n";
				    	if(price.length()<=0)
				    		result+= "Fail -There is no price of the shoe"+"\n";
				    	if(desc.length()<=0)
				    		result += "Fail -There is no description of the shoe"+"\n";
				    	if(month.length()<=0)
				    		result += "Fail -There is no release month of the shoe"+"\n";
				    	if(imgElms.size()<=0)
				    		result +="Fail -There is no Shoe Image"+"\n";
				    	if(imgSrc.length()<=0)
				    		result +="Fail -Image src is null.No Image to display."+"\n";
				    	
				    	break;
			    	}
			    }
			    	
		    }
		    else{
		    	
		        System.out.println("There are no records to display...");
		    	result = "Pass";
		    }
		}
		catch(Exception ex)
		{
			return "Fail- Monthly Shoe Content validation is failed.";
		}
		return result;
			
	}
	
	public String validateEmailSubscription(String email)
	{
		String result ="";
		try{
						
			List<org.openqa.selenium.WebElement> successDivElms = driver.findElements(By.xpath(OR.getProperty("subscription_success_div_xpath")));
			if(successDivElms.size()<=0)
				result += "Fail -Email Subscription is not successful"+"\n";
			if(successDivElms.size()> 0)
			{
				if(!successDivElms.get(0).getText().equals(OR.getProperty("subscription_successs_message")+" "+email))
				{
					result +="Fail -Invalid Subscription Message";
				}
				else{
					result = "Pass";
				}
			}
		
		}
		catch(Exception ex)
		{
			return "Fail- Email Subscription Validation Failed.";
		}
		
		return result;
	}
}
