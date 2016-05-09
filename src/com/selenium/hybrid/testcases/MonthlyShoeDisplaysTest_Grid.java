package com.selenium.hybrid.testcases;

import org.testng.annotations.Test;
import org.testng.Assert;
import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import com.selenium.hybrid.util.Keywords;
import com.selenium.hybrid.util.TestUtil;

public class MonthlyShoeDisplaysTest_Grid {
	
	Keywords keywords = null;
	
	String browserResult = null;
	
	@Parameters({"browser","version","platform"})
	@BeforeTest
	public void verifyTestCaseRunmode(String browser,String version,String platform)
	{
		if(!TestUtil.isTestCaseRunnable("MonthlyShoeDisplaysTest",Keywords.suiteXLS))
		{
			throw new SkipException("Skipping the testcase execution as the runmode is set to N.");
		}
		
		keywords = Keywords.getInstance();
		
		browserResult = keywords.openBrowser(browser,version,platform);
	}
	
	@Test(dataProvider="getMonthsData")
	public void testMonthlyShoeDisplays(Hashtable<String,String> data) 
	{
		if(!data.get("Runmode").equalsIgnoreCase("Y"))
		{
			throw new SkipException("Skipping the data set execution as the runmode is set to N");
		}
		
		if(browserResult.equals("Pass"))
			keywords.executeKeywords("MonthlyShoeDisplaysTest",data);
		else
			Assert.assertTrue(false,browserResult);
		
		
	}
	
	@AfterTest
	public void release()
	{
		keywords.closeBrowser();
	}
	
	@DataProvider
	public Object[][] getMonthsData()
	{
		return TestUtil.getData("MonthlyShoeDisplaysTest", Keywords.suiteXLS);
	}

}
