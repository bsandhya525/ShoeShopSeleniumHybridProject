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

public class MonthlyShoeDisplaysTest {
	
	Keywords keywords = null;
	
	String browserResult = null;
	
	
	@BeforeTest
	public void verifyTestCaseRunmode()
	{
		if(!TestUtil.isTestCaseRunnable("MonthlyShoeDisplaysTest",Keywords.suiteXLS))
		{
			throw new SkipException("Skipping the testcase execution as the runmode is set to N.");
		}
		
	}
	
	@Test(dataProvider="getMonthsData")
	public void testMonthlyShoeDisplays(Hashtable<String,String> data) 
	{
		if(!data.get("Runmode").equalsIgnoreCase("Y"))
		{
			throw new SkipException("Skipping the data set execution as the runmode is set to N");
		}
		
		
		keywords = Keywords.getInstance();
		
		keywords.executeKeywords("MonthlyShoeDisplaysTest",data);
	}
	
		
	@DataProvider
	public Object[][] getMonthsData()
	{
		return TestUtil.getData("MonthlyShoeDisplaysTest", Keywords.suiteXLS);
	}

}
