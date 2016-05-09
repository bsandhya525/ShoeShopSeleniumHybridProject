package com.selenium.hybrid.util;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class XLSReader {
	
	
	private Workbook workbook=null;
	
	private Sheet sheet = null;
	
	FileInputStream fistr=null;
	
	FileOutputStream fostr= null;
	
	String fileName = "";
	
	public XLSReader(String fname){
		
		this.fileName = fname;
		try{
		fistr = new FileInputStream(fname);
		
		workbook = new XSSFWorkbook(fistr);
		}
		catch(IOException ioe)
		{
			System.out.println("Error constructing XLSReader:"+ioe.getMessage());
		}
		
		sheet = workbook.getSheetAt(0);
		
	}
	
	public int getRowCount(String sheetName)
	{
		int rowcount = 0;
		
		if(workbook != null)
		{
			int sheetIndex = workbook.getSheetIndex(sheetName);
			
			if (sheetIndex !=-1)
			{
				sheet = workbook.getSheetAt(sheetIndex);
				
				rowcount = sheet.getLastRowNum()+ 1;
			}
		}
		
		
		return rowcount;
	}
	
	
	public int getColumnCount(String sheetName)
	{
		int colCount =0;
		
		if(workbook != null)
		{
			int sheetIndex = workbook.getSheetIndex(sheetName);
			
			if(sheetIndex != -1)
			{
				sheet = workbook.getSheetAt(sheetIndex);
				
				Row firstrow = sheet.getRow(0);
				
				if(firstrow != null)
				{
					//System.out.println("firstrow not null");
					
					Iterator<Cell> cells = firstrow.cellIterator();
					
					while(cells.hasNext())
					{
						Cell cell  = cells.next();
						//System.out.println(cell.getStringCellValue());
						colCount++;
					}
				}
			}
		}
		
		
		return colCount;
	}
	
	
	public String getCellData(String sheetName,int rowNum,int colNum)
	{
		String cellData =null;
		
		int sheetIndex = workbook.getSheetIndex(sheetName);
		
		if(sheetIndex != -1)
		{
			sheet = workbook.getSheetAt(sheetIndex);
			
			Row row = sheet.getRow(rowNum-1);
			
			if(row != null)
			{
				Cell cell = row.getCell(colNum-1);
				
				if(cell != null)
				{
				
					if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC ||cell.getCellType() == Cell.CELL_TYPE_FORMULA )
					{
						cellData = String.valueOf(cell.getNumericCellValue());
						
						if(HSSFDateUtil.isCellDateFormatted(cell))
						{
							Date date = cell.getDateCellValue();
							
							Calendar cal = Calendar.getInstance();
							
							cal.setTime(date);
							
							cellData = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DATE)+"/"+cal.get(Calendar.YEAR);
						}
					}
					else if (cell.getCellType()== Cell.CELL_TYPE_STRING)
					{
						cellData = cell.getStringCellValue();
					}
					else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
					{
						cellData = String.valueOf(cell.getBooleanCellValue());
					}
					else if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
					{
						cellData ="";
					}
				}
			}
		}
		
		return cellData;
	}
	
	
	public String getCellData(String sheetName,int rowNum,String colName)
	{
		String cellData =null;
		
		int colNum = -1;
		
		int sheetIndex = workbook.getSheetIndex(sheetName);
		
		if(sheetIndex != -1)
		{
			sheet = workbook.getSheetAt(sheetIndex);
			
			Row row = sheet.getRow(0);
			
			if(row != null)
			{

				Iterator<Cell> cellItr = row.cellIterator();
				
				while(cellItr.hasNext())
				{
					Cell cell = cellItr.next();
					
					if(cell.getStringCellValue().equalsIgnoreCase(colName))
					{
						colNum = cell.getColumnIndex();
					}
				}
			}
			
			row = sheet.getRow(rowNum-1);
			
			if(row != null)
			{
				Cell cell = row.getCell(colNum);
				
				if(cell != null)
				{
				
					if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC ||cell.getCellType() == Cell.CELL_TYPE_FORMULA )
					{
						cellData = String.valueOf(cell.getNumericCellValue());
						
						if(HSSFDateUtil.isCellDateFormatted(cell))
						{
							Date date = cell.getDateCellValue();
							
							Calendar cal = Calendar.getInstance();
							
							cal.setTime(date);
							
							cellData = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DATE)+"/"+cal.get(Calendar.YEAR);
						}
					}
					else if (cell.getCellType()== Cell.CELL_TYPE_STRING)
					{
						cellData = cell.getStringCellValue();
					}
					else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
					{
						cellData = String.valueOf(cell.getBooleanCellValue());
					}
					else if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
					{
						cellData ="";
					}
				}
			}
		}
		
		return cellData;
	}
	
	
	public void setCellData(String sheetName, int rowNum, int colNum, String data) throws IOException
	{
		int sheetIndex = workbook.getSheetIndex(sheetName);
		
		if(sheetIndex != -1)
		{
			sheet = workbook.getSheetAt(sheetIndex);
			
			Row  row = sheet.getRow(rowNum-1);
			
			if(row == null)
			{
				row = sheet.createRow(rowNum-1);
			}
			
			Cell cell = row.getCell(colNum-1);
			
			if(cell == null)
			{
				cell = row.createCell(colNum-1);
			}
			
			CellStyle style = workbook.createCellStyle();
			
			style.setWrapText(true);
			
			cell.setCellStyle(style);
			
			cell.setCellValue(data);
			
			fostr = new FileOutputStream(fileName);
			
			workbook.write(fostr);
			
			fostr.flush();
			
			fostr.close();
		}
		
	}
	
	
	public void setCellData(String sheetName, int rowNum, String colName, String data) throws IOException
	{
		int colNum = -1;
		
		int sheetIndex = workbook.getSheetIndex(sheetName);
		
		if(sheetIndex != -1)
		{
			sheet = workbook.getSheetAt(sheetIndex);
			
			Row row = sheet.getRow(0);
			
			if(row != null)
			{
				Iterator<Cell> cellItr = row.cellIterator();
				
				while(cellItr.hasNext())
				{
					Cell cell = cellItr.next();
					
					if(cell.getStringCellValue().equalsIgnoreCase(colName))
					{
						colNum = cell.getColumnIndex();
					}
				}
			}
			
			row = sheet.getRow(rowNum-1);
			
			if(row == null)
			{
				row = sheet.createRow(rowNum-1);
			}
			
			Cell cell = row.getCell(colNum);
			
			if(cell == null)
			{
				cell = row.createCell(colNum);
			}
			
			CellStyle style = workbook.createCellStyle();
			
			style.setWrapText(true);
			
			cell.setCellStyle(style);
			
			cell.setCellValue(data);
			
			fostr = new FileOutputStream(fileName);
			
			workbook.write(fostr);
			
			fostr.flush();
			
			fostr.close();
		}
		
	}
	
		
	public static void main(String[] args) throws IOException
	{
		XLSReader reader = new XLSReader(System.getProperty("user.dir")+"/data.xlsx");
		
		System.out.println("rowcount:"+reader.getRowCount("TestData"));
		
		System.out.println("colcount:"+reader.getColumnCount("TestData"));
		
		System.out.println("celldata:"+reader.getCellData("TestData", 2, 2));
		
		reader.setCellData("TestData", 5, 1, "http://google.com");
		
		reader.setCellData("TestData", 5, "Search", "Samosa");
	}

}
