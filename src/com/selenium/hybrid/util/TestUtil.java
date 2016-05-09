package com.selenium.hybrid.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

public class TestUtil {
	
	
	public static Object[][] getData(String testCaseName,XLSReader currentSuiteXLS)
	{
		int testCaseStartRow=1;
		
		int totalRows = currentSuiteXLS.getRowCount("TestData");
		
		for(int rowNum=testCaseStartRow;rowNum<=totalRows;rowNum++)
		{
			if(currentSuiteXLS.getCellData("TestData", rowNum, 1)!= null && currentSuiteXLS.getCellData("TestData", rowNum, 1).equalsIgnoreCase(testCaseName))
			{
				testCaseStartRow = rowNum;
				
				break;
			}
		}
		
		System.out.println("testCaseStartRow:"+testCaseStartRow);
		
		int colsStartRow = testCaseStartRow+1;
		
		int totalTestCols=0;
		
		int columnNum = 0;
		
		while(currentSuiteXLS.getCellData("TestData", colsStartRow, ++columnNum)!= null)
		{
			totalTestCols++;
			
		}
		System.out.println("totalTestCols:"+totalTestCols);
		
		int dataStartRow =colsStartRow+1;
		
		int totalTestRows = 0;
		
		while(currentSuiteXLS.getCellData("TestData", dataStartRow, 1)!= null)
		{
			totalTestRows++;
			
			dataStartRow++;
		}
		
		System.out.println("totalTestRows:"+totalTestRows);
		
		Object[][] data = new Object[totalTestRows][1];
		
		int row = 0;
		
		for(int rowNum=testCaseStartRow+2;rowNum<=testCaseStartRow+1+totalTestRows;rowNum++)
		{
			Hashtable<String,String> table = new Hashtable<String,String>();
			
			for(int colNum=1;colNum<=totalTestCols;colNum++)
			{
				table.put(currentSuiteXLS.getCellData("TestData", colsStartRow, colNum), currentSuiteXLS.getCellData("TestData", rowNum, colNum));
			}
			
			data[row][0] = table;
			
			System.out.println("data row:"+data[row][0]);
			
			row++;
		}
		
		return data;
	}

	
	public static boolean isTestCaseRunnable(String testCaseName,XLSReader currentSuiteXLS)
	{
		boolean runnable = false;
		int totalRows = currentSuiteXLS.getRowCount("TestCases");
		
		System.out.println("Total Rows:"+totalRows);
		for(int rowNum=2;rowNum<=totalRows;rowNum++)
		{
			if(currentSuiteXLS.getCellData("TestCases", rowNum, 1)!= null && currentSuiteXLS.getCellData("TestCases", rowNum, 1).equalsIgnoreCase(testCaseName))
			{
				if(currentSuiteXLS.getCellData("TestCases", rowNum, "Runmode")!= null && currentSuiteXLS.getCellData("TestCases", rowNum, "Runmode").equalsIgnoreCase("Y"))
				{
					runnable = true;
					break;
				}
			}
		}
		
		return runnable;
	}
	
	public static void main(String[] args) throws IOException
	{
		System.out.println(TestUtil.isTestCaseRunnable("MonthlyShoeDisplaysTest", new XLSReader(System.getProperty("user.dir")+"/src/com/selenium/hybrid/xls/ShoeShop Suite.xlsx")));
				
		//Object[][] data = util.getData("EmailSubscriptionTest", new XLSReader(System.getProperty("user.dir")+"/src/com/selenium/hybrid/xls/ShoeShop Suite.xlsx"));
		
		Object[][] data = TestUtil.getData("MonthlyShoeDisplaysTest", new XLSReader(System.getProperty("user.dir")+"/src/com/selenium/hybrid/xls/ShoeShop Suite.xlsx"));
		
		if(data != null)
		{
			System.out.println("data rows:"+data.length);
			
			for(int i=0;i<data.length;i++)
			{
				System.out.println(data[i][0]);
				Hashtable<String,String> table = ((Hashtable<String, String>) data[i][0]);
				if(table != null)
				{
					Enumeration<String> keys = table.keys();
					
					while(keys.hasMoreElements())
					{
						String key = keys.nextElement();
						
						String value = table.get(key);
						
						System.out.println(key+" "+value);
					}
				}
			}
		}
	}
}
