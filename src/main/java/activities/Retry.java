package activities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import library.Mobile_Friendliness_Library;
import library.Page_Count_Library;
import library.Performance_Audit_Library;

public class Retry implements IRetryAnalyzer{
	
	/*private int count = 0;
    private static int maxTry = 10;*/

	public boolean retry(ITestResult result){
		if(!result.isSuccess()){
			 /*if(count < maxTry){
				 count++;                                     
				 result.setStatus(ITestResult.FAILURE);
		         return true; 
			 }else{
				 result.setStatus(ITestResult.FAILURE); 
			 }*/
			result.setStatus(ITestResult.FAILURE);
	        return true;			
		}else{
			result.setStatus(ITestResult.SUCCESS);
		}
		return false;
	}
	
	public static void reRunTheWebPageTestMobileViewFailedOne(){
		Performance_Audit_Library library = new Performance_Audit_Library();
		ExcelDataUtility testData = new ExcelDataUtility("./data/"+library.loadExcelFlieName()+".xlsx");		
		for(int i = 1; i <= testData.getTotalRowNumber("Web_Page_Test_Mobile"); i++){
			if(testData.isCellEmpty("Web_Page_Test_Mobile", 1, i)){
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
				}finally{
					library.waitTime();
				}
			}
		}		
	}
	
	public static void reRunTheWebPageTestDesktopViewFailedOne(){
		Performance_Audit_Library library = new Performance_Audit_Library();
		ExcelDataUtility testData = new ExcelDataUtility("./data/"+library.loadExcelFlieName()+".xlsx");		
		for(int i = 1; i <= testData.getTotalRowNumber("Web_Page_Test_Desktop"); i++){
			if(testData.isCellEmpty("Web_Page_Test_Desktop", 1, i)){
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
				}finally{
					library.waitTime();
				}
			}
		}		
	}
	
	public static void reRunTheGooglePageSpeedInsightFailedOne(){
		Performance_Audit_Library library = new Performance_Audit_Library();
		ExcelDataUtility testData = new ExcelDataUtility("./data/"+library.loadExcelFlieName()+".xlsx");
		for(int i = 1; i <= testData.getTotalRowNumber("Page_Insight"); i++){
			if(testData.isCellEmpty("Page_Insight", 1, i)){
				try{
					String url = testData.getCellData("Page_Insight", 0, i);
					String[] result = library.getGoogleSpeedScoreResult(url);	
					String Mobile_value = result[0];
					String Desktop_value = result[1];
					testData.setCellData("Page_Insight", 1, i, Mobile_value+"%");
					testData.setCellData("Page_Insight", 2, i, Desktop_value+"%");	
					System.out.println("Row "+i+" data entered successfully.");
				}catch(Exception e){
					System.err.println("Unable to enter data into the "+i+" row.");
				}finally{
					library.waitTime();
				}				
			}
		}
	}
	
	public static void reRunTheGTMetrixFailedOne(){
		Performance_Audit_Library library = new Performance_Audit_Library();
		ExcelDataUtility testData = new ExcelDataUtility("./data/"+library.loadExcelFlieName()+".xlsx");
		for(int i = 1; i <= testData.getTotalRowNumber("GT_metrix"); i++){
			if(testData.isCellEmpty("GT_metrix", 1, i)){
				try{
					String url = testData.getCellData("GT_metrix", 0, i);
					String[] result = library.getGradeInGTMetrix(url);	
					String rUrl = result[0];
					String gScore = result[1];
					String yScore = result[2];
					testData.setCellData("GT_metrix", 3, i, rUrl);
					testData.setCellData("GT_metrix", 1, i, gScore);	
					testData.setCellData("GT_metrix", 2, i, yScore);	
					System.out.println("Row "+i+" data entered successfully.");
				}catch(Exception e){
					System.err.println("Unable to enter data into the "+i+" row.");
				}finally{
					library.waitTime();
				}				
			}
		}
	}
	
	public static void reRunPageCountFailedOne(){
		Page_Count_Library pcLibrary = new Page_Count_Library();
		ExcelDataUtility testData = new ExcelDataUtility("./data/"+pcLibrary.loadExcelFlieName()+".xlsx");		
		for(int i = 1; i <= testData.getTotalRowNumber("Page_Count"); i++){
			if(testData.isCellEmpty("Page_Count", 1, i)){
				try {				
					String url = testData.getCellData("Page_Count", 0, i);
					String pageCount = pcLibrary.getPageCountForWebSites(url);
					testData.setCellData("Page_Count", 1, i, pageCount);
					System.out.println("Row "+i+" data entered successfully.");
				}catch(Exception e){
					System.err.println("Unable to enter data into the "+i+" row.");
				}finally{
					pcLibrary.waitTime();
				}
			}			
		}
	}
	
	public static void reRunMobileFriendlinessFailedOne(){
		Mobile_Friendliness_Library mfLibrary = new Mobile_Friendliness_Library();
		ExcelDataUtility testData = new ExcelDataUtility("./data/"+mfLibrary.loadExcelFlieName()+".xlsx");
		for(int i = 1; i <= testData.getTotalRowNumber("Mobile_Friendliness"); i++){
			if(testData.isCellEmpty("Mobile_Friendliness", 1, i)){
				try {				
					String url = testData.getCellData("Mobile_Friendliness", 0, i);
					String[] report = mfLibrary.getMobileFriendliness(url);
					String conclusion = report[0];
					String explanation = report[1];
					testData.setCellData("Mobile_Friendliness", 1, i, conclusion);
					testData.setCellData("Mobile_Friendliness", 2, i, explanation);
					System.out.println("Row "+i+" data entered successfully.");
				}catch(Exception e){
					System.err.println("Unable to enter data into the "+i+" row.");
				}finally{
					mfLibrary.waitTime();
				}
			}
		}
		
	}

}