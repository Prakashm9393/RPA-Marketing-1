package project;

import org.testng.annotations.Test;

import library.Performance_Audit_Library;

public class TC001{
	
	//@Test
	public void test_TC001(){
		Performance_Audit_Library paLibrary = new Performance_Audit_Library();
		try{
			String[] result = paLibrary.getMobileViewResponseTimeInWebPageTest("https://www.sfwmd.gov/");
			String rUrl = result[0];
			String rTime = result[1];
			System.out.println("The Result urls is: "+ rUrl);
			System.out.println("The Load time is: "+ rTime);
		}catch(Exception e){			
			e.printStackTrace();
		}
		
	}
	
	@Test
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

}
