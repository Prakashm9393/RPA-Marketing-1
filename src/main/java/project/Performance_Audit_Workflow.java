package project;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import activities.ExcelDataUtility;
import library.Performance_Audit_Library;

public class Performance_Audit_Workflow{
	
	Performance_Audit_Library library = new Performance_Audit_Library();	
	ExcelDataUtility testData;	
	
	@BeforeClass
	public void setUp(){
		try{
			testData = new ExcelDataUtility("./data/"+library.loadExcelFlieName()+".xlsx");			
		}catch (Exception e){			
			e.printStackTrace();
		}
	}
	
  //  @Test(priority=0)
	public void start0_get_mobile_web_page_test_run(){		
		for(int i = 1; i <= testData.getTotalRowNumber("Web_Page_Test_Mobile"); i++){
			try{
				String url = testData.getCellData("Web_Page_Test_Mobile", 0, i);
				String[] result = library.getMobileViewResponseTimeInWebPageTest(url);
				String rUrl = result[0];
				String rTime = result[1];
				testData.setCellData("Web_Page_Test_Mobile", 2, i, rUrl);
				testData.setCellData("Web_Page_Test_Mobile", 1, i, rTime);
				System.out.println("Row "+i+" data entered successfully.");
			}catch(Exception e){			
				System.err.println("Unable to enter data into the "+i+" row.");
				library.quitBrowser();
			}finally{
				library.quitBrowser();
				library.waitTime();
			}			
		}
	}
	
	//@Test(priority=1)
	public void next1_put_desktop_web_page_test_run(){
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
				library.quitBrowser();
			}finally{
				library.quitBrowser();
				library.waitTime();
			}
		}
	}	
	
	@Test(priority=2)
	public void next2_get_google_page_speed_run(){
		for(int i = 1; i <= testData.getTotalRowNumber("Page_Insight"); i++){
			try{
				String url = testData.getCellData("Page_Insight", 0, i);
				String[] result = library.getGoogleSpeedScoreResult(url);	
				String Mobile_value = result[0];
				String Desktop_value = result[1];
				String Tti_M = result[2];
				String Fmp_M = result[3];
				String Tti_D = result[4];
				String Fmp_D = result[5];				
				testData.setCellData("Page_Insight", 1, i, Mobile_value+"%");
				testData.setCellData("Page_Insight", 4, i, Desktop_value+"%");
				testData.setCellData("Page_Insight", 2, i, Tti_M);
				testData.setCellData("Page_Insight", 3, i, Fmp_M);
				testData.setCellData("Page_Insight", 5, i, Tti_D);
				testData.setCellData("Page_Insight", 6, i, Fmp_D);
				System.out.println("Row "+i+" data entered successfully.");
			}catch(Exception e){
				System.err.println("Unable to enter data into the "+i+" row.");				
			}finally{
				library.quitBrowser();
				library.waitTime();
			}
		}		
	}		
	
	//@Test(priority=3)
	public void next3_get_gtmetrix(){
		for(int i = 1; i <= testData.getTotalRowNumber("GT_metrix"); i++){
			try{
				String url = testData.getCellData("GT_metrix", 0, i);
				String[] result = library.getGradeInGTMetrix(url);	
				String rUrl = result[0];
				String gScore = result[1];
				String yScore = result[2];
				String pageloadtime = result[3];
				testData.setCellData("GT_metrix", 4, i, rUrl);
				testData.setCellData("GT_metrix", 1, i, gScore);	
				testData.setCellData("GT_metrix", 2, i, yScore);	
				testData.setCellData("GT_metrix", 3, i, pageloadtime);
				System.out.println("Row "+i+" data entered successfully.");
			}catch(Exception e){
				System.err.println("Unable to enter data into the "+i+" row.");
				library.quitBrowser();
			}finally{
				library.quitBrowser();
				library.waitTime();
			}
		}		
	}
}