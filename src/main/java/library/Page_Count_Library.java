package library;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import activities.GenericWrappers;

public class Page_Count_Library extends GenericWrappers{
	
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