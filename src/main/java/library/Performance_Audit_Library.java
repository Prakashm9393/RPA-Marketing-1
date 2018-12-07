package library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Properties;

import activities.GenericWrappers;

public class Performance_Audit_Library extends GenericWrappers{
	
	public String loadExcelFlieName(){
		Properties prop = new Properties();
		String fileName = null;
		try {
			prop.load(new FileInputStream(new File("./config.properties")));
			fileName = prop.getProperty("Excel.FileName");
		}catch (FileNotFoundException e){			
			throw new RuntimeException("'config.properties' file not found in the project root path.");
		}catch (IOException e){
			throw new RuntimeException("Unable to read the config.properties file.");			
		}
		return fileName;
	}
	
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
		String mobileValue = getElement("Xpath&(//div[@class='lh-gauge__percentage'])[1]").getText();
		mouseOverAndClickAction("Xpath&//div[@class='tab-title' and text()='Desktop']");			
		waitTime(800);
		String desktopValue = getElement("Xpath&(//div[@class='lh-gauge__percentage'])[2]").getText();			
	    closeWindow();
		return new String[] {mobileValue,desktopValue};
	}
	
	public String[] getGradeInGTMetrix(String url) throws Exception{
		invokeApp(browserName);
		getUrl("https://gtmetrix.com/");
		clickOn("linktext&Log In");
		waitUntilElementIsVisible("id&li-email", 300);		
		enterText("id&li-email", "manika.kannappan@ameexusa.com");
		enterText("id&li-password", "ameex123");		
		clickOn("xpath&//button[text()='Log In']");
		waitUntilElementIsVisible("xpath&//header//li[@class='user-nav-welcome']", 300);		
		enterText("name&url", url);
		clickOn("Xpath&//button[text()='Analyze']");
		waitUntilElementIsVisible("xpath&(//div[@class='report-score'])[1]/span/i", 300);				
		String pageSpeedScore = getElement("xpath&(//div[@class='report-score'])[1]/span/i").getAttribute("class");
		String [] aSplit = pageSpeedScore.split("-");
		String googlescore = aSplit[2];
		String gPercent = getElement("xpath&(//div[@class='report-score'])[1]/span/span").getText();
		String googleScorePercent = googlescore.concat(gPercent);
		String yScore = getElement("xpath&(//div[@class='report-score'])[2]/span/i").getAttribute("class");
		String [] aSplit1 = yScore.split("-");
		String yScoreValue = aSplit1[2];
		String yPercent = getElement("xpath&(//div[@class='report-score'])[2]/span/span").getText();
		String yScorePercent = yScoreValue.concat(yPercent);
		String result = getDriver().getCurrentUrl();
		clickOn("linktext&Log Out");
		closeWindow();
		return new String[] {result,googleScorePercent,yScorePercent};
    }		
	
	public String[] getmobilefriendly(String url) throws Exception{
		invokeApp(browserName);
		getUrl("https://search.google.com/test/mobile-friendly");		
		clickOn("Xpath&//a[text()='Sign in']");
		waitTime(1000);		
		enterText("id&identifierId", "performance.rpa@gmail.com");
		waitTime(1000);
		clickOn("id&identifierNext");
		waitTime(2000);
		enterText("Xpath&//input[@type='password']", "Ameexusa@2018");		
		waitTime(1000);
		clickOn("id&passwordNext");
		waitTime(2000);
		waitUntilElementIsVisible("xpath&//input[@aria-label='Enter a URL to test']", 300);
		enterText("xpath&//input[@aria-label='Enter a URL to test']", url);	
		clickOn("Xpath&//div[text()='run test']");
		waitTime(60000);			
		String result = getElement("Xpath&//div[@class='N5SJAd ZcmAe']").getText();
		String resulturl = getDriver().getCurrentUrl();
		closeWindow();
		return new String[] {result,resulturl};
	}
}