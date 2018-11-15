package library;

import java.text.DecimalFormat;

import org.testng.annotations.Test;

import activities.GenericWrappers;
import activities.ExcelDataUtility;

public class Performance_Audit_Library extends GenericWrappers{
	
	public void getMobileWebPageTestResult() throws Exception{
		ExcelDataUtility testData = new ExcelDataUtility("./data/Performance_Audit.xlsx");
		invokeApp(browserName);
		getUrl("https://www.webpagetest.org/");
		clickOn("linktext&Login with Google");
		waitTime(1000);		
		enterText("id&identifierId", "performance.rpa@gmail.com");
		waitTime(1000);
		clickOn("id&identifierNext");
		waitTime(2000);
		enterText("Xpath&//input[@type='password']", "Ameexusa@2018");		
		waitTime(1000);
		clickOn("id&passwordNext");
		waitTime(2000);	
		for(int i = 1; i <= testData.getTotalRowNumber("Web_Page_Test_Mobile"); i++){
			getUrl("https://www.webpagetest.org/");
			String url = testData.getCellData("Web_Page_Test_Mobile", 0, i);
			enterText("id&url", url);
			selectByVisibleTextInDropdown("id&browser", "Galaxy S7 - Chrome");
			String advanced_settings = getDriver().findElementById("advanced_settings").getAttribute("class"); 
			if(advanced_settings.isEmpty()){
				clickOn("id&advanced_settings");
			}	
			selectByVisibleTextInDropdown("id&connection", "LTE (12 Mbps/12 Mbps 70ms RTT)");
			enterText("id&number_of_tests", "1");
			clickOn("id&viewBoth");
			clickOn("Xpath&//div[@id='start_test-container']//button");
			waitTime(3000);
			String result = driver.getCurrentUrl();
			testData.setCellData("Web_Page_Test_Mobile", 2, i,result);						
		}
		clickOn("linktext&Logout");
		closeWindow();
	}	
	
	public void getDesktopWebPageTestResult() throws Exception{
		ExcelDataUtility testData = new ExcelDataUtility("./data/Performance_Audit.xlsx");
		invokeApp(browserName);
		getUrl("https://www.webpagetest.org/");
		clickOn("linktext&Login with Google");
		waitTime(1000);		
		enterText("id&identifierId", "performance.rpa@gmail.com");
		waitTime(1000);
		clickOn("id&identifierNext");
		waitTime(2000);
		enterText("Xpath&//input[@type='password']", "Ameexusa@2018");		
		waitTime(1000);
		clickOn("id&passwordNext");
		waitTime(2000);	
		for(int i = 1; i <= testData.getTotalRowNumber("Web_Page_Test_Desktop"); i++){
			getUrl("https://www.webpagetest.org/");
			String url = testData.getCellData("Web_Page_Test_Desktop", 0, i);
			enterText("id&url", url);
			selectByVisibleTextInDropdown("id&browser", "Chrome");
			String advanced_settings = getDriver().findElementById("advanced_settings").getAttribute("class"); 
			if(advanced_settings.isEmpty()){
				clickOn("id&advanced_settings");
			}	
			selectByVisibleTextInDropdown("id&connection", "Cable (5/1 Mbps 28ms RTT)");
			enterText("id&number_of_tests", "1");
			clickOn("id&viewBoth");
			clickOn("Xpath&//div[@id='start_test-container']//button");
			waitTime(3000);
			String result = driver.getCurrentUrl();
		    testData.setCellData("Web_Page_Test_Desktop", 4, i,result);			    						
		}
		clickOn("linktext&Logout");
		closeWindow();
	}
	
	
	public void putDesktopWebPageTest() throws Exception{
		ExcelDataUtility testData = new ExcelDataUtility("./data/Performance_Audit.xlsx");
		invokeApp(browserName);
		for(int i = 1; i <= testData.getTotalRowNumber("Web_Page_Test_Desktop"); i++){
			String url = testData.getCellData("Web_Page_Test_Desktop", 4, i);
			getUrl(url);
			waitTime(3000);
			String str = getDriver().findElementByXPath("//table[@id='tableResults']/tbody/tr[3]/td[@id='fvLoadTime']").getText();
			StringBuffer sb = new StringBuffer(str);	
			for(int j = 0; j < sb.length(); j++){
				if(sb.charAt(j) == 's'){
					sb.deleteCharAt(j);
			    }
			}			
			String strg = sb.toString();
			float f = Float.parseFloat(strg);
			DecimalFormat decimalFormat = new DecimalFormat("#.##");
			float twoDigitsF = Float.valueOf(decimalFormat.format(f));
			String loadTime = String.valueOf(twoDigitsF);
			String requests = driver.findElementByXPath("//table[@id='tableResults']/tbody/tr[3]/td[@id='fvRequestsDoc']").getText();
			String bytes = driver.findElementByXPath("//table[@id='tableResults']/tbody/tr[3]/td[@id='fvBytesInDoc']").getText();
			testData.setCellData("Web_Page_Test_Desktop", 3, i, loadTime);
			testData.setCellData("Web_Page_Test_Desktop", 2, i, requests);
			testData.setCellData("Web_Page_Test_Desktop", 1, i, bytes);
			}
		closeWindow(); 
	}
		
	public void putMobileWebPageTest() throws Exception{
		ExcelDataUtility testData = new ExcelDataUtility("./data/Performance_Audit.xlsx");
		invokeApp(browserName);
		getUrl("https://www.webpagetest.org/");
		clickOn("linktext&Login with Google");
		waitTime(1000);		
		enterText("id&identifierId", "performance.rpa@gmail.com");
		waitTime(1000);
		clickOn("id&identifierNext");
		waitTime(2000);
		enterText("Xpath&//input[@type='password']", "Ameexusa@2018");		
		waitTime(1000);
		clickOn("id&passwordNext");
		waitTime(2000);
		for(int i = 1; i <= testData.getTotalRowNumber("Web_Page_Test_Mobile"); i++){
			String url = testData.getCellData("Web_Page_Test_Mobile", 2, i);
			getUrl(url);
			waitTime(3000);
			String str = getDriver().findElementByXPath("//table[@id='tableResults']/tbody/tr[3]/td[@id='fvLoadTime']").getText();
			StringBuffer sb = new StringBuffer(str);	
			for (int j = 0; j < sb.length(); j++) {
				if(sb.charAt(j) == 's'){
				sb.deleteCharAt(j);
			    }
			}			
			String strg = sb.toString();
			float f = Float.parseFloat(strg);
			DecimalFormat decimalFormat = new DecimalFormat("#.##");
			float twoDigitsF = Float.valueOf(decimalFormat.format(f));
			String loadTime = String.valueOf(twoDigitsF);				
			testData.setCellData("Web_Page_Test_Mobile", 1, i, loadTime);				
		}	
		clickOn("linktext&Logout");
		closeWindow();
	}
	@Test
	public void manika_putPageInsights() throws Exception{
		ExcelDataUtility testData = new ExcelDataUtility("./data/Performance_Audit.xlsx");
		invokeApp(browserName);
		for(int i = 1; i <= testData.getTotalRowNumber("Page_Insight"); i++){
			getUrl("https://developers.google.com/speed/pagespeed/insights/");
			String url = testData.getCellData("Page_Insight", 0, i);
			enterText("name&url", url);
			driver.findElementByXPath("//div[@role='button']").click();			clickOn("Xpath&//div[@class='button button-red analyze jfk-button main-submit jfk-button-standard']");
			waitTime(3000);
			String  Desktop_value, Mobile_value;
			Mobile_value = getDriver().findElementByXPath("(//div[@class='lh-gauge__percentage'])[1]").getText();
			driver.findElementByXPath("(//div[@class='tab-title'])[2]").click();
			Desktop_value = getDriver().findElementByXPath("(//div[@class='lh-gauge__percentage'])[2]").getText();
			testData.setCellData("Page_Insight", 1, i, Mobile_value);
			testData.setCellData("Page_Insight", 2, i, Desktop_value);
			
		}	
		clickOn("linktext&Logout");
		closeWindow();
	}

}
