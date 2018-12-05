package project;

import org.testng.annotations.Test;

import activities.Retry;
import library.Performance_Audit_Library;

public class Performance_Audit_Workflow{
	
	Performance_Audit_Library library = new Performance_Audit_Library();
	
<<<<<<< HEAD
    @Test(priority=0)
=======
	//@Test(priority=0)
>>>>>>> a8b36ba599f16539072d7ae22699ff16c830da8b
	public void start0_get_mobile_web_page_test_run(){
		
		try{
			library.getMobileWebPageTestResult();
		}catch(Exception e){			
			e.printStackTrace();
		}
	}
	
	@Test(priority=1)
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
		try{
			library.waitTime();
			library.putDesktopWebPageTest();
		}catch (Exception e){			
			e.printStackTrace();
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
<<<<<<< HEAD
	}
	
	@Test(priority=5)
	public void next5_Page_count(){
		try{
			library.waitTime();
			library.Page_count();
		}catch(Exception e){			
			throw new RuntimeException("Failed - " + e.toString());
		}
	}
=======
	}	
>>>>>>> a8b36ba599f16539072d7ae22699ff16c830da8b

}