package project;

import org.testng.annotations.Test;

import activities.Retry;
import library.Performance_Audit_Library;

public class Performance_Audit_Workflow{
	
	Performance_Audit_Library library = new Performance_Audit_Library();
	
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
	
	//@Test(priority=2)
	public void next2_put_desktop_web_page_test_run(){
		try{
			library.waitTime();
			library.putDesktopWebPageTest();
		}catch (Exception e){			
			e.printStackTrace();
		}
	}
	
	//@Test(priority=3, retryAnalyzer=Retry.class)
	public void next3_put_mobile_web_page_test_run(){
		try{
			library.waitTime();
			library.putMobileWebPageTest();
		}catch(Exception e){			
			throw new RuntimeException("Failed - " + e.toString());
		}
	}
	
	@Test(priority=4)
	public void enter_urls_in_GtMetrix()
	{
		try{
			library.waitTime();
			library.openGTMetrixAndTakePerformanceForDesktop();
		}catch(Exception e){			
			throw new RuntimeException("Failed - " + e.toString());
		}
	}

}
