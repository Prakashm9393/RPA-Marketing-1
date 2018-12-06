package project;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import activities.ExcelDataUtility;
import library.Page_Count_Library;

public class Page_Count_Workflow{
	
	Page_Count_Library pcLibrary = new Page_Count_Library();
	ExcelDataUtility testData;
	String sheetName;
	
	@BeforeClass
	public void setUp(){
		try{
			testData = new ExcelDataUtility("./data/Performance_Audit.xlsx");
			sheetName = "Page_Count";
		}catch (Exception e){			
			e.printStackTrace();
		}
	}
	
	@Test
	public void start0_get_page_count(){		
		for(int i = 1; i <= testData.getTotalRowNumber(sheetName); i++){
			try {				
				String url = testData.getCellData(sheetName, 0, i);
				String pageCount = pcLibrary.getPageCountForWebSites(url);
				testData.setCellData(sheetName, 1, i, pageCount);
				System.out.println("Row "+i+" data entered successfully.");
			}catch(Exception e){
				System.err.println("Unable to enter data into the "+i+" row.");
			}finally{
				pcLibrary.waitTime();
			}
		}				
	}

}