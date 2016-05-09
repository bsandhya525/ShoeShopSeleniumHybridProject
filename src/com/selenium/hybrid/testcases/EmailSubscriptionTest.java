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

public class EmailSubscriptionTest {
	
	Keywords keywords = null;
	
	String browserResult = null;
	
	
	@BeforeTest
	public void verifyTestCaseRunnable()
	{
		if(!TestUtil.isTestCaseRunnable("EmailSubscriptionTest", Keywords.suiteXLS))
		{
			throw new SkipException("Skipping the testcase as runmode is set to N");
		}
		
	}

	@Test(dataProvider="getEmailData")
	public void testEmailSubscription(Hashtable<String,String> data)
	{
		if(!data.get("Runmode").equals("Y"))
		{
			throw new SkipException("Skipping the dataset as runmode is set to N");
		}
		
			
		keywords = Keywords.getInstance();
		keywords.executeKeywords("EmailSubscriptionTest", data);
	}
	
	
	
	@DataProvider
	public Object[][] getEmailData()
	{
		return TestUtil.getData("EmailSubscriptionTest",Keywords.suiteXLS);
	}
}
