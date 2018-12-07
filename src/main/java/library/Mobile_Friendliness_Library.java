package library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.WebElement;

import activities.GenericWrappers;

public class Mobile_Friendliness_Library extends GenericWrappers{
	
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
	
	public String[] getMobileFriendliness(String url) throws Exception{
		invokeApp(browserName);
		getUrl("https://www.bing.com/webmaster/tools/mobile-friendliness");		
		waitUntilElementIsVisible("id&url-input-field", 300);
		enterText("id&url-input-field", url);	
		clickOn("id&url-input-button");
		waitUntilElementIsVisible("id&result-section", 300);			
		String result = getElement("id&conclusion").getText();
		List<WebElement> eles = getElements("xpath&//div[@id='explaination']//div[@class='reason-title']");
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < eles.size(); i++){
			list.add(eles.get(i).getText());
		}
		String explaination = String.join(",", list);
		closeWindow();
		return new String[] {result,explaination};
	}
	
}