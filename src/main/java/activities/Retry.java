package activities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

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
		ExcelDataUtility testData = new ExcelDataUtility("./data/Performance_Audit.xlsx");
		Performance_Audit_Library library = new Performance_Audit_Library();
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
		ExcelDataUtility testData = new ExcelDataUtility("./data/Performance_Audit.xlsx");
		Performance_Audit_Library library = new Performance_Audit_Library();
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
	
	public static void reRunPageCountFailedOne(){
		ExcelDataUtility testData = new ExcelDataUtility("./data/Performance_Audit.xlsx");
		Page_Count_Library pcLibrary = new Page_Count_Library();
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

}