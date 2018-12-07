package project;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import activities.ExcelDataUtility;
import library.Mobile_Friendliness_Library;

public class Mobile_Friendliness_Workflow{
	
	Mobile_Friendliness_Library mfLibrary = new Mobile_Friendliness_Library();
	ExcelDataUtility testData;
	String sheetName;
	
	@BeforeClass
	public void setUp(){
		try{
			testData = new ExcelDataUtility("./data/"+mfLibrary.loadExcelFlieName()+".xlsx");
			sheetName = "Mobile_Friendliness";
		}catch (Exception e){			
			e.printStackTrace();
		}
	}
	
	@Test
	public void start0_get_mobile_friendliness(){		
		for(int i = 1; i <= testData.getTotalRowNumber(sheetName); i++){
			try {				
				String url = testData.getCellData(sheetName, 0, i);
				String[] report = mfLibrary.getMobileFriendliness(url);
				String conclusion = report[0];
				String explanation = report[1];
				testData.setCellData(sheetName, 1, i, conclusion);
				testData.setCellData(sheetName, 2, i, explanation);
				System.out.println("Row "+i+" data entered successfully.");
			}catch(Exception e){
				System.err.println("Unable to enter data into the "+i+" row.");
			}finally{
				mfLibrary.waitTime();
			}
		}				
	}

}