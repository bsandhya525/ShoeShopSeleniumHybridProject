package com.selenium.hybrid.testcases;

import java.util.Hashtable;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.selenium.hybrid.util.Keywords;
import com.selenium.hybrid.util.TestUtil;

public class EmailSubscriptionTest_Grid {
	
	Keywords keywords = null;
	
	String browserResult = null;
	
	@Parameters({"browser","version","platform"})
	@BeforeTest
	public void verifyTestCaseRunnable(String browser,String version,String platform)
	{
		if(!TestUtil.isTestCaseRunnable("EmailSubscriptionTest", Keywords.suiteXLS))
		{
			throw new SkipException("Skipping the testcase as runmode is set to N");
		}
		
		keywords = Keywords.getInstance();
		
		browserResult = keywords.openBrowser(browser,version,platform);
	}

	@Test(dataProvider="getEmailData")
	public void testEmailSubscription(Hashtable<String,String> data)
	{
		if(!data.get("Runmode").equals("Y"))
		{
			throw new SkipException("Skipping the dataset as runmode is set to N");
		}
		
		if(browserResult.equals("Pass"))
			keywords.executeKeywords("EmailSubscriptionTest", data);
		else
			Assert.assertTrue(false,browserResult);
		
		
	}
	
	@AfterTest
	public void release()
	{
		keywords.closeBrowser();
	}
	
	@DataProvider
	public Object[][] getEmailData()
	{
		return TestUtil.getData("EmailSubscriptionTest",Keywords.suiteXLS);
	}
}
