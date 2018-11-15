package project;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TC001{
	
	RemoteWebDriver driver;
	
	@BeforeClass
	public void beforeClass(){
		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();		
	}
	
	@Test
	public void tc001() throws InterruptedException{
		driver.get("https://www.digikey.in/products/en/capacitors/aluminum-polymer-capacitors/69");	
		Select select = new Select(driver.findElementByXPath("(//select[@name='pageSize'])[1]"));
		int selectOptions = select.getOptions().size();
		select.selectByIndex(selectOptions - 1);
		Thread.sleep(2000);
		List<WebElement> dataSheet = driver.findElementsByXPath("//table[@id='productTable']/tbody/tr");		
		for (int i = 1; i <= dataSheet.size(); i++){
			String data = driver.findElementByXPath("//table[@id='productTable']/tbody/tr["+i+"]/td[2]/a").getAttribute("href");
			String img = driver.findElementByXPath("//table[@id='productTable']/tbody/tr["+i+"]/td[3]/a/img").getAttribute("src");
			System.out.println(data + "|" + img);
		}
	}

}
