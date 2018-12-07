package library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import activities.GenericWrappers;

public class Page_Count_Library extends GenericWrappers{
	
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
	
	public String getPageCountForWebSites(String url) throws Exception{
		invokeApp(browserName);		
		driver.get("https://www.google.com");		
		driver.findElement(By.xpath("//input[@class='gLFyf gsfi']")).sendKeys("site:", url);
		driver.findElement(By.xpath("//input[@class='gLFyf gsfi']")).sendKeys(Keys.ENTER);
		WebElement element = driver.findElement(By.xpath("//div[@id='resultStats']"));
		String result = element.getText();
		String [] aSplit = result.split(" ");	
		String pageCount = aSplit[1];
		closeWindow();
		return pageCount;
    }

}