package project;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import activities.ExcelDataUtility;
import activities.Retry;
import library.Mobile_Friendliness_Library;
import library.Performance_Audit_Library;

public class TC001{
	
	Performance_Audit_Library library = new Performance_Audit_Library();
	ExcelDataUtility testData;
	
	@BeforeClass
	public void setUp(){
		try {
			testData = new ExcelDataUtility("./data/Performance_Audit.xlsx");
		}catch(Exception e){			
			e.printStackTrace();
		}
	}
	
	//@Test
	public void test_TC001(){
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
			}finally{
				library.waitTime();
			}			
		}		
	}
	
	//@Test
	public void test_TC002(){
		Performance_Audit_Library paLibrary = new Performance_Audit_Library();
		try{
			String[] result = paLibrary.getDesktopViewResponseTimeInWebPageTest("https://www.sfwmd.gov/");
			String rUrl = result[0];
			String rTime = result[1];
			String rRequest = result[2];
			String rBytes = result[3];
			System.out.println("The Result urls is: "+ rUrl);
			System.out.println("The Load time is: "+ rTime);
			System.out.println("The Number of request: "+ rRequest);
			System.out.println("The Page size: "+ rBytes);
		}catch(Exception e){			
			e.printStackTrace();
		}		
	}
	
	//@Test
	public void test_TC003(){
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
	
	//@Test
	public void test_TC004(){
		Retry.reRunPageCountFailedOne();
	}
    @Test
	public void test_TC005(){
		try{
			String[] result = library.getGoogleSpeedScoreResult("https://www.digikey.in");
			System.out.println("Mobile Vaule: "+result[0]+"%");
			System.out.println("Desktop Vaule: "+result[1]+"%");
			System.out.println("M-timetoInteractive: "+result[2]);
			System.out.println("M-firstMeaningfulPaint: "+result[3]);
			System.out.println("D-timetoInteractive: "+result[4]);
			System.out.println("D-firstMeaningfulPaint: "+result[5]);
		}catch(Exception e){			
			e.printStackTrace();
			library.closeWindow();
		}
    }		
    //@Test
	public void test_TC006(){
		try{
			String[] result = library.getGradeInGTMetrix("https://www.digikey.in");
			System.out.println("URL: "+result[0]);
			System.out.println("gscore: "+result[1]);
			System.out.println("yslow: "+result[2]);
			System.out.println("fullLoadTime: "+result[3]);
		}catch(Exception e){			
			e.printStackTrace();
		}
    }	
    
	public void test_TC007(){
		Mobile_Friendliness_Library ml = new Mobile_Friendliness_Library();
		try{
			String[] result = ml.getMobileFriendliness("https://www.digikey.in");
			System.out.println("Explaination: "+result[1]);
			System.out.println("result: "+result[0]);
		}catch(Exception e){			
			e.printStackTrace();
		}
    }
}