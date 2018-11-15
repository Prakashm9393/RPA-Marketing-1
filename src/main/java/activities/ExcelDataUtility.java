package activities;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDataUtility{
	
	 FileInputStream fis = null;
	 FileOutputStream fos = null;
	 XSSFWorkbook workbook = null;
	 XSSFSheet sheet = null;
	 XSSFRow row = null;
	 XSSFCell cell = null;
	 String xlFilePath;
	 
	 public ExcelDataUtility(String xlFilePath) throws Exception{
	    this.xlFilePath = xlFilePath;
        fis = new FileInputStream(xlFilePath);
        workbook = new XSSFWorkbook(fis);
        fis.close();		   
	}
	 
	public boolean setCellData(String sheetName, int colNumber, int rowNumber, String value){
		try{			
			sheet = workbook.getSheet(sheetName);
			row = sheet.getRow(rowNumber);
			if (row == null)
				row = sheet.createRow(rowNumber);
			cell = row.getCell(colNumber);
			if (cell == null)
				cell = row.createCell(colNumber);
			cell.setCellValue(value);			
			fos = new FileOutputStream(xlFilePath);
			workbook.write(fos);
			fos.close();
		}catch (Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;		
	}
	
	public String getCellData(String sheetName, int colNumber, int rowNumber){		
		String data = null;
		try{
			sheet = workbook.getSheet(sheetName);
			row = sheet.getRow(rowNumber);
			cell = row.getCell(colNumber);
			data = cell.getStringCellValue();
			return data;						
		}catch (Exception e){
			e.printStackTrace();
			return "row "+rowNumber+" or column "+colNumber +" does not exist  in Excel";
		}				
	}	
	
	public int getTotalRowNumber(String sheetName){
		int rowNum = 0;
		sheet = workbook.getSheet(sheetName);
		rowNum = sheet.getLastRowNum();
		return rowNum;		
	}
	
	public int getTotalColNumber(String sheetName){
		sheet = workbook.getSheet(sheetName);
		int colNum = sheet.getRow(0).getLastCellNum();
		return colNum;
	}

}
