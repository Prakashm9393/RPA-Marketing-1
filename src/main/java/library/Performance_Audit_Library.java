package library;

import java.text.DecimalFormat;
import activities.ExcelDataUtility;
import activities.GenericWrappers;

public class Performance_Audit_Library extends GenericWrappers{
	
	public String[] getMobileViewResponseTimeInWebPageTest(String url) throws Exception{
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
		waitUntilElementIsVisible("Xpath&//table[@id='tableResults']", 300);
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
		clickOn("linktext&Logout");
		closeWindow();
		return new String[] {result,loadTime};
	}
	
	public String[] getDesktopViewResponseTimeInWebPageTest(String url) throws Exception{
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
		waitUntilElementIsVisible("Xpath&//table[@id='tableResults']", 300);
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
		clickOn("linktext&Logout");
		closeWindow();
		return new String[] {result,loadTime,requests,bytes};
	}			
	
	public String[] getGoogleSpeedScoreResult(String url) throws Exception{
		invokeApp(browserName);
		getUrl("https://developers.google.com/speed/pagespeed/insights/");
		enterText("name&url", url);			
		mouseOverAndClickAction("Xpath&//div[@role='button' and text()=' ANALYZE ']");
		waitTime(60000);			
		String Mobile_value = getElement("Xpath&(//div[@class='lh-gauge__percentage'])[1]").getText();
		mouseOverAndClickAction("Xpath&//div[@class='tab-title' and text()='Desktop']");			
		waitTime(800);
		String Desktop_value = getElement("Xpath&(//div[@class='lh-gauge__percentage'])[2]").getText();			
	    closeWindow();
		return new String[] {Mobile_value,Desktop_value};
	}
	
	public String[] getGTMetrixGoogleGradeResult(String url) throws Exception{
		invokeApp(browserName);
		getUrl("https://gtmetrix.com/");
		clickOn("linktext&Log In");
		waitTime(1000);		
		enterText("id&li-email", "manika.kannappan@ameexusa.com");
		enterText("id&li-password", "ameex123");		
		waitTime(1000);
		clickOn("xpath&//button[text()='Log In']");
		waitTime(2000);	
		getUrl("https://gtmetrix.com/");
		enterText("name&url", url);
		clickOn("Xpath&//button[text()='Analyze']");
		waitTime(60000);			
		String Pagespeedscore = getElement("(//div[@class='report-score'])[1]/span/i").getText();
		String [] aSplit = Pagespeedscore.split("-");
		String Googlescore = aSplit[2];
		String Gpercent = getElement("(//div[@class='report-score'])[1]/span/span").getText();
		String Googlescorepercent = Googlescore.concat(Gpercent);
		String Yscore = getElement("(//div[@class='report-score'])[2]/span/i").getText();
		String [] aSplit1 = Yscore.split("-");
		String yscorevalue = aSplit1[2];
		String ypercent = getElement("(//div[@class='report-score'])[2]/span/span").getText();
		String yscorepercent = yscorevalue.concat(ypercent);
		String result = getDriver().getCurrentUrl();
		clickOn("linktext&Logout");
		closeWindow();
		return new String[] {result,Googlescorepercent,yscorepercent};
		}		
}