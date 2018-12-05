package project;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import activities.ExcelDataUtility;
import activities.Retry;
import library.Performance_Audit_Library;

public class Performance_Audit_Workflow{
	
	Performance_Audit_Library library = new Performance_Audit_Library();	
	ExcelDataUtility testData;	
	
	@BeforeClass
	public void setUp(){
		try{
			testData = new ExcelDataUtility("./data/Performance_Audit.xlsx");			
		}catch (Exception e){			
			e.printStackTrace();
		}
	}
	
    //@Test(priority=0)
	public void start0_get_mobile_web_page_test_run(){
		
		try{
			library.getMobileWebPageTestResult();
		}catch(Exception e){			
			e.printStackTrace();
		}
	}
	
	//@Test(priority=1)
	public void next1_get_desktop_web_page_test_run(){		
		try{
			library.waitTime();
			library.getDesktopWebPageTestResult();
		}catch (Exception e){			
			e.printStackTrace();
		}
	}
	
	@Test(priority=2)
	public void next2_put_desktop_web_page_test_run(){
		for(int i = 1; i <= testData.getTotalRowNumber("Web_Page_Test_Desktop"); i++){
			try {
				String url = testData.getCellData("Web_Page_Test_Desktop", 0, i);
				String[] result = library.getDesktopViewResponseTimeInWebPageTest(url);
				String rUrl = result[0];
				String rTime = result[1];
				String rRequest = result[2];
				String rBytes = result[3];
				testData.setCellData("Web_Page_Test_Desktop", 4, i, rUrl);
				testData.setCellData("Web_Page_Test_Desktop", 3, i, rTime);
				testData.setCellData("Web_Page_Test_Desktop", 2, i, rRequest);
				testData.setCellData("Web_Page_Test_Desktop", 1, i, rBytes);
				System.out.println("Row "+i+" data entered successfully.");
			}catch(Exception e){
				System.err.println("Unable to enter data into the "+i+" row.");				
			}
		}
	}	
	
	//@Test(priority=3)
	public void next3_get_google_page_speed_run(){
		try{
			library.waitTime();
			library.getGoogleSpeedScoreResult();
		}catch (Exception e){			
			e.printStackTrace();
		}
	}
	

	//@Test(priority=4, retryAnalyzer=Retry.class)
	public void next4_put_mobile_web_page_test_run(){
		try{
			library.waitTime();
			library.putMobileWebPageTest();
		}catch(Exception e){			
			throw new RuntimeException("Failed - " + e.toString());
		}

	}	
	
}