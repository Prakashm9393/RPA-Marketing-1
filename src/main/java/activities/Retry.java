package activities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer{
	
	/*private int count = 0;
    private static int maxTry = 10;*/

	@Override
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

}
