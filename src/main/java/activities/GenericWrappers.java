package activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
//import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
//import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
//import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import activities.Log;


public class GenericWrappers{
	
	public String browserName, gHubIp, gHubPort;
	
	public GenericWrappers(){
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("./config.properties")));
			browserName = prop.getProperty("Launch.Browser.Name");
			gHubIp = prop.getProperty("Grid.Hub.IP");
			gHubPort = prop.getProperty("Grid.Hub.Port");
		}catch (FileNotFoundException e){			
			throw new RuntimeException("'config.properties' file not found in the project root path.");
		}catch (IOException e){
			throw new RuntimeException("Unable to read the config.properties file.");			
		}
	}
	
	protected static final ThreadLocal<GenericWrappers> driverThreadLocal = new ThreadLocal<GenericWrappers>();
	public RemoteWebDriver driver;	
	protected static String os = System.getProperty("os.name");
	public String primaryWindowHandle;

	public void setDriver(GenericWrappers wrappers) {
		driverThreadLocal.set(wrappers);
	}

	public RemoteWebDriver getDriver() {
		return driverThreadLocal.get().driver;
	}
	
	/**
	 * This method will launch the browser in local machine and maximise the browser and set the
	 * wait for 30 seconds and load the url
	 * @author Karthikeyan Rajendran on 20/10/2017 11:57:00 AM
	 * @param url - The url with http or https
	 * @return 
	 * 
	 */
	public RemoteWebDriver invokeApp(String browser) {
		return invokeApp(browser,false);
	}

	/**
	 * This method will launch the browser in grid node (if remote) and maximise the browser and set the
	 * wait for 30 seconds and load the url 
	 * @author Karthikeyan Rajendran on 20/10/2017 11:57:00 AM
	 * @param url - The url with http or https
	 * @return 
	 * 
	 */
	public synchronized RemoteWebDriver invokeApp(String browser, boolean bRemote) {
		
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");
	    
		try {			
			// this is for grid run
			if(bRemote){
				if(browser.equalsIgnoreCase("CHROME")){
					DesiredCapabilities dc = DesiredCapabilities.chrome();
					dc.setCapability("version", "");
					dc.setBrowserName("chrome");
					dc.setPlatform(Platform.LINUX);
					driver = new RemoteWebDriver(new URL("http://" + gHubIp + ":" + gHubPort + "/wd/hub"),dc);
				}
			}else{ // this is for local run
				if(browser.equalsIgnoreCase("chrome")){
					System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
					driver = new ChromeDriver();
					driver.manage().window().maximize();					
				}else if(browser.equalsIgnoreCase("chromeheadless")){
					System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");  
					ChromeOptions options = new ChromeOptions();  
					options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors");  
					driver = new ChromeDriver(options);
				}else{
					System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver.exe");
					driver = new FirefoxDriver();					
				}
			}
			GenericWrappers gw = new GenericWrappers();
			gw.driver = driver;
			setDriver(gw);
			
			getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);						
		} catch (Exception e) {
			Log.fatal("Unable to launch application "+browser+" browser."+e.toString());
			throw new RuntimeException("Unable to launch application "+browser+" browser."+e.toString());	
		}
		return getDriver();
	}
	
	/**
     * This is method is used to resize the window depends on resolution
     * @param resolution - input of resolution
     * @return boolean value
     * @author Karthikeyan Rajendran on 20/10/2017 11:57:00 AM
     * @throws Exception
     *//*
    public boolean invokeAppInMobileBrowser(){
    	boolean bReturn = false;    
    	int width = Integer.parseInt(ReadYml.mWidth);
    	int height = Integer.parseInt(ReadYml.mHeight);
    	try {    		
			Dimension d = new Dimension(width, height);
			getDriver().manage().window().setSize(d);
			Log.info("The view port size is "+width+"x"+height+".");
			bReturn = true;
		} catch (Exception e) {	
			Log.fatal("Unable resize the window "+e.toString());
			throw new RuntimeException("Unable resize the window "+e.toString());
		}
		return bReturn;
    }*/
    
    /**
     * This method is used to load AUT URL
     * and set the wait for 30 seconds.
     * @param url - URL that we want to load
     * @return boolean value
     * @author Karthikeyan Rajendran on 15/12/2017:18:00:00PM
     * @throws Exception
     */
    public boolean getUrl(String url){ 
     //Log4j Configuration XML file  
        DOMConfigurator.configure("log4j.xml");
     
     boolean bReturn = false;
     try {
      getDriver().get(url);
      Log.info("Invoke AUT: "+url+" in "+browserName+" browser successfully.");   
      getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
      Log.info("Implicit wait applied on the driver for 30 seconds.");               
     } catch (Exception e) {
      Log.fatal("Unable to launch the application "+url+" in the "+browserName+" browser. "+e.toString());        
     }
     return bReturn;
    }
	
	/**
	 * This method will close all the browsers
	 * @author Karthikeyan Rajendran on 20/10/2017 11:57:00 AM
	 */
	public void quitBrowser() {
		
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");
	    
		try {
			getDriver().quit();
			Log.info("Successfully closed the browsers.");
		} catch (Exception e) {
			Log.fatal("Unable to close the browsers."+e.toString());			
		}
	}
	
	/**
	 * This method is used enter give text into the object based on the locator
	 * and wait for 10 seconds until visibility of the web element
	 * @param locator - find and match the elements of web page
	 * @param input - the text which enter into the object 
	 * @return boolean value
	 * @author Karthikeyan Rajendran on 20/10/2017:13:58:00PM
	 * @throws Exception
	 */
	public boolean enterText(String locator,String input){
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");
		
		boolean bReturn = false;
		String[] data = locator.split("&");
		String key = data[0];
		String value = data[1];
		if(key.equalsIgnoreCase("ID")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(value)));
				getDriver().findElementById(value).clear();
				getDriver().findElementById(value).sendKeys(input);				
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("NAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(value)));
				getDriver().findElementByName(value).clear();
				getDriver().findElementByName(value).sendKeys(input);				
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("CLASSNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(value)));
				getDriver().findElementByClassName(value).clear();
				getDriver().findElementByClassName(value).sendKeys(input);				
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}					
		}else if(key.equalsIgnoreCase("TAGNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(value)));
				getDriver().findElementByTagName(value).clear();
				getDriver().findElementByTagName(value).sendKeys(input);				
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("CSSSELECTOR")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(value)));
				getDriver().findElementByCssSelector(value).clear();
				getDriver().findElementByCssSelector(value).sendKeys(input);				
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("XPATH")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(value)));
				getDriver().findElementByXPath(value).clear();
				getDriver().findElementByXPath(value).sendKeys(input);				
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}			
		}else{
			Log.error("Kindly, provide correct locator option for enterText.");			
			throw new RuntimeException("Kindly, provide correct locator option for enterText.");
		}
		return bReturn;
	}
	
	/**
	 * This method is used click on the given object based on the locator
	 * and wait for 10 seconds until visibility of the web element
	 * @param locator - find and match the elements of web page
	 * @return boolean value
	 * @author Karthikeyan Rajendran on 20/10/2017:13:58:00PM
	 * @throws Exception
	 */
	public boolean clickOn(String locator){
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");
		
		boolean bReturn = false;
		String[] data = locator.split("&");
		String key = data[0];
		String value = data[1];		
		if(key.equalsIgnoreCase("ID")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(value)));
				WebElement ele = getDriver().findElementById(value);
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", ele);				
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("NAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(value)));
				WebElement ele = getDriver().findElementByName(value);
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", ele);				
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("LINKTEXT")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 30);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(value)));
				getDriver().findElementByLinkText(value).click();				
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());								
			}			
		}else if(key.equalsIgnoreCase("PARTIALLINKTEXT")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 30);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(value)));
				getDriver().findElementByPartialLinkText(value).click();				
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("CLASSNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(value)));
				WebElement ele = getDriver().findElementByClassName(value);
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", ele);				
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("TAGNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(value)));
				WebElement ele = getDriver().findElementByTagName(value);
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", ele);				
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("CSSSELECTOR")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(value)));
				WebElement ele = getDriver().findElementByCssSelector(value);
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", ele);				
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("XPATH")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(value)));
				WebElement ele = getDriver().findElementByXPath(value);
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", ele);				
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());								
			}			
		}else{
			Log.error("Kindly, provide correct locator option for clickOn.");			
			throw new RuntimeException("Kindly, provide correct option.");
		}		
		return bReturn;
	}
	
	/**
     * This method is used to create red color border around the given web element
     * @param element - web element of the page
     * @param duration -time duration for the border
     * @author Karthikeyan Rajendran on 20/10/2017:13:58:00PM
     * @throws InterruptedException
     */
    public void highlightElement(WebElement element, int duration){
		
		JavascriptExecutor js = (JavascriptExecutor) getDriver();		
        String original_style = element.getAttribute("style");
        js.executeScript(
                "arguments[0].setAttribute(arguments[1], arguments[2])",
                element,
                "style",
                "border: 3px solid red; border-style: solid;");
        if (duration > 0) {
            try {
				Thread.sleep(duration * 1000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
            js.executeScript(
                    "arguments[0].setAttribute(arguments[1], arguments[2])",
                    element,
                    "style",
                    original_style);
        }
    }
    
    /**
     * This method is used to scroll down the page untill given webelement
     * and wait for 10 seconds to appear title of the web page
     * @param locator - find and match the elements of web page
     * @author Karthikeyan Rajendran on 20/10/2017:13:58:00PM
	 * @throws Exception
     */
    public void scrollToTheGivenWebElement(String locator){
    	String[] data = locator.split("&");
		String key = data[0];
		String value = data[1];
    	WebElement element = null;
    	if(key.equalsIgnoreCase("ID")){
    		try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(value)));
				element = getDriver().findElementById(value);
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView();", element);
			} catch (Exception e) {
				Log.fatal("Unable to find element "+e.toString());
			}
    	}else if(key.equalsIgnoreCase("XPATH")){
    		try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(value)));
				element = getDriver().findElementByXPath(value);
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView();", element);
			} catch (Exception e) {
				Log.fatal("Unable to find element "+e.toString());
			}
    	}else{
			Log.fatal("Kindly, provide correct locator option for element.");
			throw new RuntimeException("Kindly, provide correct locator option for element.");
		}    	 
    }
    
    /**
     * This method is used to select radio button option
     * and wait for 10 seconds to appear title of the web page
     * @param locator - find and match the elements of web page
     * @param text - value to be clicked
     * @return boolean value
     * @author Karthikeyan Rajendran on 20/10/2017:13:58:00PM
     * @throws Exception
     */
    public boolean autoCompleteTextField(String locator,String text){	
    	boolean bReturn = false;
    	
		String[] data = locator.split("&");
		String key = data[0];
		String value = data[1];
		List<WebElement> listOfName = null;
		if(key.equalsIgnoreCase("ID")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(value)));
				listOfName = getDriver().findElementsById(value);
				int sizeOfList = listOfName.size();
				for (int i = 0; i < sizeOfList; i++) {				
					if (listOfName.get(i).getText().trim().equalsIgnoreCase(text)) {
						listOfName.get(i).click();	
						Log.info(text+" is the selected.");					
						break;
					}
				}
			} catch (Exception e) {
				Log.fatal("Unable to find element "+e.toString());
			}
		}else if(key.equalsIgnoreCase("XPATH")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(value)));
				listOfName = getDriver().findElementsByXPath(value);
				int sizeOfList = listOfName.size();
				for (int i = 0; i < sizeOfList; i++) {				
					if (listOfName.get(i).getText().trim().equalsIgnoreCase(text)) {
						listOfName.get(i).click();	
						Log.info(text+" is the selected.");					
						break;
					}
				}
			} catch(Exception e){
				Log.fatal("Unable to find element "+e.toString());
			}
		}else{
			Log.fatal("Kindly, provide correct locator option for element.");
			throw new RuntimeException("Kindly, provide correct locator option for element.");
		} 
		return bReturn;
	}
    
	/**
	 * This method used to verify text in the element with expected value
	 * and wait for 10 seconds to appear title of the web page
	 * @param locator - find and match the elements of web page
	 * @param expected - expected value
	 * @return boolean value
	 * @author Karthikeyan Rajendran on 20/10/2017:13:58:00PM
	 * @throws Exception
	 */
	public boolean verifyText(String locator,String expected){
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");
		
	    boolean bReturn = false;	    
		String[] data = locator.split("&");
		String key = data[0];
		String value = data[1];
		if(key.equalsIgnoreCase("ID")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(value)));
				if(getDriver().findElementById(value).getText().trim().equals(expected)){					
					bReturn = true;
				}else{
					Log.error("The text: "+getDriver().findElementById(value).getText().trim()+" did not match with the value : "+ expected);					
				}
			} catch (Exception e) {	
				Log.fatal("Unable to find the given element "+value+" : "+e.toString());								
			}				
		}else if(key.equalsIgnoreCase("NAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(value)));	
				if(getDriver().findElementByName(value).getText().trim().equals(expected)){					
					bReturn = true;
				}else{
					Log.error("The text: "+getDriver().findElementById(value).getText().trim()+" did not match with the value : "+ expected);
				}
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" : "+e.toString());					
			}			
		}else if(key.equalsIgnoreCase("CLASSNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(value)));
				if(getDriver().findElementByClassName(value).getText().trim().equals(expected)){					
					bReturn = true;
				}else{
					Log.error("The text: "+getDriver().findElementById(value).getText().trim()+" did not match with the value : "+ expected);
				}
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" : "+e.toString());					
			}			
		}else if(key.equalsIgnoreCase("TAGNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(value)));	
				if(getDriver().findElementByTagName(value).getText().trim().equals(expected)){					
					bReturn = true;
				}else{
					Log.error("The text: "+getDriver().findElementById(value).getText().trim()+" did not match with the value : "+ expected);
				}
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" : "+e.toString());					
			}			
		}else if(key.equalsIgnoreCase("CSSSELECTOR")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(value)));	
				if(getDriver().findElementByCssSelector(value).getText().trim().equals(expected)){					
					bReturn = true;
				}else{
					Log.error("The text: "+getDriver().findElementById(value).getText().trim()+" did not match with the value : "+ expected);
				}
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" : "+e.toString());					
			}			
		}else if(key.equalsIgnoreCase("XPATH")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(value)));				
				if(getDriver().findElementByXPath(value).getText().trim().equals(expected)){					
					bReturn = true;
				}else{
					Log.error("The text: "+getDriver().findElementById(value).getText().trim()+" did not match with the value : "+ expected);
				}
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" : "+e.toString());					
			}			
		}else{
			Log.error("Kindly, provide correct locator option for verifyText.");			
			throw new RuntimeException("Kindly, provide correct locator option for verifyText.");
		}
		return bReturn;
	}
	
	/**
	 * This method used to verify the url of the page
	 * and wait for 10 seconds to appear title of the web page
	 * @param expected - web page's expected url
	 * @return boolean value
	 * @author Karthikeyan Rajendran on 20/10/2017:13:58:00PM
	 * @throws Exception
	 */
	public boolean verifyUrlOfThePage(String expected){	
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");
	    
		boolean bReturn = false;
		try{
			WebDriverWait wait = new WebDriverWait(getDriver(), 10);
			wait.until(ExpectedConditions.urlToBe(expected));
			Log.info("The expected "+expected+" url as same as the "+getDriver().getCurrentUrl()+" actual url.");
			bReturn = true;
		}catch (Exception e){			
			Log.fatal("The expected "+expected+" url wasn't same as the "+getDriver().getCurrentUrl()+" actual url."+e.toString());			
		}		
		return bReturn;
	}
	
	/**
	 * This method is used to select value in the dropdown by visible text
	 * @param locator - find and match the elements of web page
	 * @param visibleText - visible text of the dropdown
	 * @return boolean value
	 * @author Karthikeyan Rajendran on 20/10/2017:13:58:00PM
	 * @throws Exception
	 */
	public boolean selectByVisibleTextInDropdown(String locator,String visibleText){
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");
		
	    boolean bReturn = false;
		String[] data = locator.split("&");
		String key = data[0];
		String value = data[1];		
		if(key.equalsIgnoreCase("ID")){
			try {				
				new Select(getDriver().findElementById(value)).selectByVisibleText(visibleText);
				Log.info("The element with id: "+value+" is selected with visible text: "+visibleText);
				bReturn = true;
			} catch (Exception e) {	
				Log.fatal("The visible text: "+visibleText+" could not be selected. "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("NAME")){
			try {				
				new Select(getDriver().findElementByName(value)).selectByVisibleText(visibleText);
				Log.info("The element with name: "+value+" is selected with visible text: "+visibleText);
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("The visible text: "+visibleText+" could not be selected. "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("CLASSNAME")){
			try {					
				new Select(getDriver().findElementByClassName(value)).selectByVisibleText(visibleText);
				Log.info("The element with classname: "+value+" is selected with visible text: "+visibleText);
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("The visible text: "+visibleText+" could not be selected. "+e.toString());				
			}					
		}else if(key.equalsIgnoreCase("TAGNAME")){
			try {				
				new Select(getDriver().findElementByTagName(value)).selectByVisibleText(visibleText);
				Log.info("The element with tagname: "+value+" is selected with visible text: "+visibleText);
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("The visible text: "+visibleText+" could not be selected. "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("CSSSELECTOR")){
			try {				
				new Select(getDriver().findElementByCssSelector(value)).selectByVisibleText(visibleText);
				Log.info("The element with cssselector: "+value+" is selected with visible text: "+visibleText);
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("The visible text: "+visibleText+" could not be selected. "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("XPATH")){
			try {					
				new Select(getDriver().findElementByXPath(value)).selectByVisibleText(visibleText);
				Log.info("The element with xpath: "+value+" is selected with visible text: "+visibleText);
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("The visible text: "+visibleText+" could not be selected. "+e.toString());				
			}			
		}else{
			Log.error("Kindly, provide correct locator option for selectByVisibleTextInDropdown.");			
			throw new RuntimeException("Kindly, provide correct locator option for selectByVisibleTextInDropdown.");
		}
		return bReturn;
	}
	
	/**
	 * This method is used to select value in the dropdown by value
	 * @param locator - find and match the elements of web page
	 * @param dValue - value of the dropdown
	 * @return boolean value
	 * @author Karthikeyan Rajendran on 20/10/2017:13:58:00PM
	 * @throws Exception
	 */
	public boolean selectByValueInDropdown(String locator,String dValue){
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");
		
	    boolean bReturn = false;
		String[] data = locator.split("&");
		String key = data[0];
		String value = data[1];		
		if(key.equalsIgnoreCase("ID")){
			try {				
				new Select(getDriver().findElementById(value)).selectByValue(dValue);
				Log.info("The element with id: "+value+" is selected with value: "+dValue);
				bReturn = true;
			} catch (Exception e) {	
				Log.fatal("The value: "+dValue+" could not be selected. "+e.toString());				
			}							
		}else if(key.equalsIgnoreCase("NAME")){
			try {				
				new Select(getDriver().findElementByName(value)).selectByValue(dValue);
				Log.info("The element with name: "+value+" is selected with value: "+dValue);
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("The value: "+dValue+" could not be selected. "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("CLASSNAME")){
			try {				
				new Select(getDriver().findElementByClassName(value)).selectByValue(dValue);	
				Log.info("The element with classname: "+value+" is selected with value: "+dValue);
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("The value: "+dValue+" could not be selected. "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("TAGNAME")){
			try {				
				new Select(getDriver().findElementByTagName(value)).selectByValue(dValue);
				Log.info("The element with tagname: "+value+" is selected with value: "+dValue);
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("The value: "+dValue+" could not be selected. "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("CSSSELECTOR")){
			try {				
				new Select(getDriver().findElementByCssSelector(value)).selectByValue(dValue);
				Log.info("The element with cssselector: "+value+" is selected with value: "+dValue);
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("The value: "+dValue+" could not be selected. "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("XPATH")){
			try {				
				new Select(getDriver().findElementByXPath(value)).selectByValue(dValue);
				Log.info("The element with xpath: "+value+" is selected with value: "+dValue);
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("The value: "+dValue+" could not be selected. "+e.toString());				
			}			
		}else{
			Log.error("Kindly, provide correct locator option for selectByValueInDropdown.");			
			throw new RuntimeException("Kindly, provide correct locator option for selectByValueInDropdown.");
		}
		return bReturn;
	}
	
	/**
	 * This method is used to select value in the dropdown by index
	 * @param locator - find and match the elements of web page
	 * @param index - index of the dropdown
	 * @return boolean value
	 * @author Karthikeyan Rajendran on 20/10/2017:13:58:00PM
	 * @throws Exception
	 */
	public boolean selectByIndexInDropdown(String locator,int index){
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");
		
	    boolean bReturn = false;
		String[] data = locator.split("&");
		String key = data[0];
		String value = data[1];		
		if(key.equalsIgnoreCase("ID")){
			try {				
				new Select(getDriver().findElementById(value)).selectByIndex(index);
				Log.info("The element with id: "+value+" is selected with index: "+index);
				bReturn = true;
			} catch (Exception e) {	
				Log.fatal("The index: "+index+" could not be selected. "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("NAME")){
			try {				
				new Select(getDriver().findElementByName(value)).selectByIndex(index);
				Log.info("The element with name: "+value+" is selected with index: "+index);
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("The index: "+index+" could not be selected. "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("CLASSNAME")){
			try {				
				new Select(getDriver().findElementByClassName(value)).selectByIndex(index);	
				Log.info("The element with classname: "+value+" is selected with index: "+index);
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("The index: "+index+" could not be selected. "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("TAGNAME")){
			try {				
				new Select(getDriver().findElementByTagName(value)).selectByIndex(index);
				Log.info("The element with tagname: "+value+" is selected with index: "+index);
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("The index: "+index+" could not be selected. "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("CSSSELECTOR")){
			try {				
				new Select(getDriver().findElementByCssSelector(value)).selectByIndex(index);
				Log.info("The element with cssselector: "+value+" is selected with index: "+index);
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("The index: "+index+" could not be selected. "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("XPATH")){
			try {				
				new Select(getDriver().findElementByXPath(value)).selectByIndex(index);
				Log.info("The element with xpath: "+value+" is selected with index: "+index);
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("The index: "+index+" could not be selected. "+e.toString());				
			}			
		}else{
			Log.error("Kindly, provide correct locator option for selectByIndexInDropdown.");			
			throw new RuntimeException("Kindly, provide correct locator option for selectByIndexInDropdown.");
		}
		return bReturn;
	}
	
	/**
	 * This method is used to pause the execution of current thread for given time.
	 * @param ms - time in milliseconds
	 * @author Karthikeyan Rajendran on 20/10/2017:13:58:00PM
	 */
	public void waitTime(long ms){
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {	
			Log.fatal(e.toString());
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used to take screenshots	 
	 * @author Karthikeyan Rajendran on 20/10/2017:13:58:00PM
	 *//*
	public long takeSnap(){
		long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L; 
		try {
			FileUtils.copyFile(getDriver().getScreenshotAs(OutputType.FILE) , new File("./reporter/images/"+number+".jpg"));
		} catch (WebDriverException e) {
			e.printStackTrace();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {				
				e1.printStackTrace();
			}
			reportStep("The browser has been closed.", "FAIL");
		} catch (IOException e) {
			reportStep("The snapshot could not be taken", "WARN");
		}
		return number;
	}*/
	
	/**
	 * This method used to verify contain text in the element with expected value
	 * and wait for 10 seconds to appear title of the web page
	 * @param locator - find and match the elements of web page
	 * @param expected - expected value
	 * @return boolean value
	 * @author Karthikeyan Rajendran on 23/10/2017:14:40:00PM
	 * @throws Exception
	 */
	public boolean verifyTextContains(String locator,String expected){
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");
		
	    boolean bReturn = false;
		String[] data = locator.split("&");
		String key = data[0];
		String value = data[1];
		if(key.equalsIgnoreCase("ID")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(value)));
				if(getDriver().findElementById(value).getText().trim().contains(expected)){
					Log.info("The text: "+getDriver().findElementById(value).getText().trim()+" matches with the value : "+expected);
					bReturn = true;
				}else{
					Log.error("The text: "+getDriver().findElementById(value).getText().trim()+" did not match with the value : "+expected);					
				}
			} catch (Exception e) {	
				Log.error("Unable to find the given element "+value+" : "+e.toString());							
			}				
		}else if(key.equalsIgnoreCase("NAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(value)));	
				if(getDriver().findElementByName(value).getText().trim().contains(expected)){
					Log.info("The text: "+getDriver().findElementByName(value).getText().trim()+" matches with the value : "+expected);
					bReturn = true;
				}else{
					Log.error("The text: "+getDriver().findElementByName(value).getText().trim()+" did not match with the value : "+expected);
				}
			} catch (Exception e) {				
				Log.error("Unable to find the given element "+value+" : "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("CLASSNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(value)));
				if(getDriver().findElementByClassName(value).getText().trim().contains(expected)){
					Log.info("The text: "+getDriver().findElementByClassName(value).getText().trim()+" matches with the value : "+expected);
					bReturn = true;
				}else{
					Log.error("The text: "+getDriver().findElementByClassName(value).getText().trim()+" did not match with the value : "+expected);
				}
			} catch (Exception e) {				
				Log.error("Unable to find the given element "+value+" : "+e.toString());					
			}			
		}else if(key.equalsIgnoreCase("TAGNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(value)));	
				if(getDriver().findElementByTagName(value).getText().trim().contains(expected)){
					Log.info("The text: "+getDriver().findElementByTagName(value).getText().trim()+" matches with the value : "+expected);
					bReturn = true;
				}else{
					Log.error("The text: "+getDriver().findElementByTagName(value).getText().trim()+" did not match with the value : "+expected);
				}
			} catch (Exception e) {				
				Log.error("Unable to find the given element "+value+" : "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("CSSSELECTOR")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(value)));	
				if(getDriver().findElementByCssSelector(value).getText().trim().contains(expected)){
					Log.info("The text: "+getDriver().findElementByCssSelector(value).getText().trim()+" matches with the value : "+expected);
					bReturn = true;
				}else{
					Log.error("The text: "+getDriver().findElementByCssSelector(value).getText().trim()+" did not match with the value : "+expected);
				}
			} catch (Exception e) {				
				Log.error("Unable to find the given element "+value+" : "+e.toString());					
			}			
		}else if(key.equalsIgnoreCase("XPATH")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(value)));	
				if(getDriver().findElementByXPath(value).getText().trim().contains(expected)){
					Log.info("The text: "+getDriver().findElementByXPath(value).getText().trim()+" matches with the value : "+expected);
					bReturn = true;
				}else{
					Log.error("The text: "+getDriver().findElementByXPath(value).getText().trim()+" did not match with the value : "+expected);
				}
			} catch (Exception e) {				
				Log.error("Unable to find the given element "+value+" : "+e.toString());					
			}			
		}else{
			Log.fatal("Kindly, provide correct locator option for verifyText.");			
			throw new RuntimeException("Kindly, provide correct locator option for verifyText.");
		}
		return bReturn;
	}
	
	/**
	 * This method is used to find given element in the DOM
	 * and wait for 10 seconds to appear title of the web page
	 * @param locator - find and match the elements of web page
	 * @return boolean value
	 * @author Karthikeyan Rajendran on 25/10/2017:12:01:00PM
	 */
	public boolean findElement(String locator){
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");		
	    
		String[] data = locator.split("&");
		String key = data[0];
		String value = data[1];
		boolean bReturn = false;
		if (key.equalsIgnoreCase("ID")) {
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 1);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(value)));
				Log.info(value+" : id is avaliable in the DOM elements.");
				bReturn = true;
			} catch (Exception e) {
				Log.error("Unable to find the id : "+value+" in the DOM elements. "+e.toString());								
			} 
		}else if(key.equalsIgnoreCase("NAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 1);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(value)));
				Log.info(value+" : name is avaliable in the DOM elements.");
				bReturn = true;
			} catch (Exception e) {
				Log.error("Unable to find the name : "+value+" in the DOM elements. "+e.toString());				
			}
		}else if(key.equalsIgnoreCase("CLASSNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 1);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(value)));
				Log.info(value+" : class name is avaliable in the DOM elements.");
				bReturn = true;
			} catch (Exception e) {
				Log.error("Unable to find the class name : "+value+" in the DOM elements. "+e.toString());				
			}
		}else if(key.equalsIgnoreCase("TAGNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 1);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(value)));
				Log.info(value+" : tag name is avaliable in the DOM elements.");
				bReturn = true;
			} catch (Exception e) {
				Log.error("Unable to find the tag name : "+value+" in the DOM elements. "+e.toString());				
			}
		}else if(key.equalsIgnoreCase("CSSSELECTOR")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 1);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(value)));
				Log.info(value+" : css selector is avaliable in the DOM elements.");
				bReturn = true;
			} catch (Exception e) {
				Log.error("Unable to find the css selector : "+value+" in the DOM elements. "+e.toString());				
			}
		}else if(key.equalsIgnoreCase("XPATH")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 1);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(value)));
				Log.info(value+" : Xpath is avaliable in the DOM elements.");
				bReturn = true;
			} catch (Exception e) {
				Log.error("Unable to find the Xpath : "+value+" in the DOM elements. "+e.toString());				
			}
		}else{
			Log.fatal("Kindly, provide correct locator option for element.");			
			throw new RuntimeException("Kindly, provide correct locator option for element.");
		}
		return bReturn;
	}
	
	/**
	 * This method is used to get web element
	 * and wait for 10 seconds to appear title of the web page
	 * @param locator - find and match the elements of web page
	 * @return WebElement value
	 * @author Karthikeyan Rajendran on 06/11/2017:16:15:00PM
	 */
	public WebElement getElement(String locator){
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");		
	    
		String[] data = locator.split("&");
		String key = data[0];
		String value = data[1];
		WebElement wReturn = null;
		if (key.equalsIgnoreCase("ID")) {
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(value)));	
				wReturn = getDriver().findElementById(value);
			} catch (Exception e) {
				Log.error("Unable to find the id : "+value+" in the DOM elements. "+e.toString());							
			}			
		}else if(key.equalsIgnoreCase("NAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(value)));		
				wReturn = getDriver().findElementByName(value);
			} catch (Exception e) {
				Log.error("Unable to find the id : "+value+" in the DOM elements. "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("CLASSNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(value)));	
				wReturn = getDriver().findElementByClassName(value);
			} catch (Exception e) {
				Log.error("Unable to find the id : "+value+" in the DOM elements. "+e.toString());					
			}			
		}else if(key.equalsIgnoreCase("TAGNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(value)));	
				wReturn = getDriver().findElementByTagName(value);
			} catch (Exception e) {
				Log.error("Unable to find the id : "+value+" in the DOM elements. "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("CSSSELECTOR")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(value)));
				wReturn = getDriver().findElementByCssSelector(value);
			} catch (Exception e) {
				Log.error("Unable to find the id : "+value+" in the DOM elements. "+e.toString());					
			}			
		}else if(key.equalsIgnoreCase("XPATH")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(value)));	
				wReturn = getDriver().findElementByXPath(value);
			} catch (Exception e) {
				Log.error("Unable to find the id : "+value+" in the DOM elements. "+e.toString());					
			}			
		}else{
			Log.fatal("Kindly, provide correct locator option for element.");			
			throw new RuntimeException("Kindly, provide correct locator option for element.");
		}
		return wReturn;
	}	
	
	public boolean checkTheCheckBoxIsCheckedDefault(String locator){
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");		
	    
		String[] data = locator.split("&");
		String key = data[0];
		String value = data[1];
		boolean bReturn = false;
		if (key.equalsIgnoreCase("ID")) {
			try {					
				bReturn = getDriver().findElementById(value).isSelected();
				Log.info("The checkbox "+value+" is checked default.");
			} catch (Exception e) {
				Log.error("Unable to find the id : "+value+" in the DOM elements. "+e.toString());				
			}
		}
		return bReturn;	
	}
	
	/**
	 * This method is used to perform right click action on the clickable element
	 * and wait for 10 seconds to appear title of the web page
	 * @param locator - find and match the elements of web page
	 * @return boolean value
	 * @author Karthikeyan Rajendran on 13/11/2017:13:15:00PM
	 */
	public boolean rightClickOnElementAndClickOnNewTab(String locator){
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");		
	    
		String[] data = locator.split("&");
		String key = data[0];
		String value = data[1];
		WebElement ele;
		Actions builder = new Actions(getDriver());
		boolean bReturn = false;
		if(key.equalsIgnoreCase("ID")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(value)));
				ele = getDriver().findElementById(value);			
				builder.keyDown(Keys.CONTROL).keyDown(Keys.SHIFT).click(ele).keyUp(Keys.CONTROL).keyUp(Keys.SHIFT).build().perform();
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());
				throw new RuntimeException("Unable to find the given element "+value+" :"+e.toString());
			}		    
		}else if(key.equalsIgnoreCase("NAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(value)));
				ele = getDriver().findElementByName(value);
				builder.keyDown(Keys.CONTROL).keyDown(Keys.SHIFT).click(ele).keyUp(Keys.CONTROL).keyUp(Keys.SHIFT).build().perform();
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());
				throw new RuntimeException("Unable to find the given element "+value+" :"+e.toString());
			}			
		}else if(key.equalsIgnoreCase("LINKTEXT")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(value)));
				ele = getDriver().findElementByLinkText(value);
				builder.keyDown(Keys.CONTROL).keyDown(Keys.SHIFT).click(ele).keyUp(Keys.CONTROL).keyUp(Keys.SHIFT).build().perform();
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());
				throw new RuntimeException("Unable to find the given element "+value+" :"+e.toString());
			}			
		}else if(key.equalsIgnoreCase("PARTIALLINKTEXT")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(value)));
				ele = getDriver().findElementByPartialLinkText(value);
				builder.keyDown(Keys.CONTROL).keyDown(Keys.SHIFT).click(ele).keyUp(Keys.CONTROL).keyUp(Keys.SHIFT).build().perform();
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());
				throw new RuntimeException("Unable to find the given element "+value+" :"+e.toString());
			}			
		}else if(key.equalsIgnoreCase("CLASSNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(value)));
				ele = getDriver().findElementByClassName(value);
				builder.keyDown(Keys.CONTROL).keyDown(Keys.SHIFT).click(ele).keyUp(Keys.CONTROL).keyUp(Keys.SHIFT).build().perform();
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());
				throw new RuntimeException("Unable to find the given element "+value+" :"+e.toString());
			}			
		}else if(key.equalsIgnoreCase("TAGNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(value)));
				ele = getDriver().findElementByTagName(value);
				builder.keyDown(Keys.CONTROL).keyDown(Keys.SHIFT).click(ele).keyUp(Keys.CONTROL).keyUp(Keys.SHIFT).build().perform();
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());
				throw new RuntimeException("Unable to find the given element "+value+" :"+e.toString());
			}			
		}else if(key.equalsIgnoreCase("CSSSELECTOR")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(value)));
				ele = getDriver().findElementByCssSelector(value);
				builder.keyDown(Keys.CONTROL).keyDown(Keys.SHIFT).click(ele).keyUp(Keys.CONTROL).keyUp(Keys.SHIFT).build().perform();
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());
				throw new RuntimeException("Unable to find the given element "+value+" :"+e.toString());
			}			
		}else if(key.equalsIgnoreCase("XPATH")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(value)));
				ele = getDriver().findElementByXPath(value);
				builder.keyDown(Keys.CONTROL).keyDown(Keys.SHIFT).click(ele).keyUp(Keys.CONTROL).keyUp(Keys.SHIFT).build().perform();
				bReturn = true;
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());
				throw new RuntimeException("Unable to find the given element "+value+" :"+e.toString());
			}			
		}else{
			Log.error("Kindly, provide correct locator option for clickOn.");
			throw new RuntimeException("Kindly, provide correct option.");
		}
		return bReturn;
	}
	
	/**
	 * This method is used to perform mouse over action
	 * and wait for 10 seconds to appear title of the web page
	 * @param locator - find and match the elements of web page
	 * @return boolean value
	 * @author Karthikeyan Rajendran on 13/11/2017:13:30:00PM
	 */
	public boolean mouseOverAction(String locator){
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");		
	    
		String[] data = locator.split("&");
		String key = data[0];
		String value = data[1];
		WebElement ele;
		Actions builder = new Actions(getDriver());
		boolean bReturn = false;
		if(key.equalsIgnoreCase("ID")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(value)));
				ele = getDriver().findElementById(value);			
				builder.moveToElement(ele).build().perform();
				bReturn = true;
				Log.info("The mouse over by id : "+value+" is performed.");
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}		    
		}else if(key.equalsIgnoreCase("NAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(value)));
				ele = getDriver().findElementByName(value);
				builder.moveToElement(ele).build().perform();
				bReturn = true;
				Log.info("The mouse over by name : "+value+" is performed.");
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("LINKTEXT")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(value)));
				ele = getDriver().findElementByLinkText(value);
				builder.moveToElement(ele).build().perform();
				bReturn = true;
				Log.info("The mouse over by linktext : "+value+" is performed.");
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("PARTIALLINKTEXT")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(value)));
				ele = getDriver().findElementByPartialLinkText(value);
				builder.moveToElement(ele).build().perform();
				bReturn = true;
				Log.info("The mouse over by partial linktext : "+value+" is performed.");
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("CLASSNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(value)));
				ele = getDriver().findElementByClassName(value);
				builder.moveToElement(ele).build().perform();
				bReturn = true;
				Log.info("The mouse over by classname : "+value+" is performed.");
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("TAGNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(value)));
				ele = getDriver().findElementByTagName(value);
				builder.moveToElement(ele).build().perform();
				bReturn = true;
				Log.info("The mouse over by tagname : "+value+" is performed.");
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("CSSSELECTOR")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(value)));
				ele = getDriver().findElementByCssSelector(value);
				builder.moveToElement(ele).build().perform();
				bReturn = true;
				Log.info("The mouse over by cssselector : "+value+" is performed.");
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("XPATH")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(value)));
				ele = getDriver().findElementByXPath(value);
				builder.moveToElement(ele).build().perform();
				bReturn = true;
				Log.info("The mouse over by xpath : "+value+" is performed.");
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());				
			}			
		}else{
			Log.error("Kindly, provide correct locator option for mouseOverAction.");			
			throw new RuntimeException("Kindly, provide correct locator option for mouseOverAction.");
		}
		return bReturn;
	}
	
	/**
	 * This method is used to get list of web elements
	 * and wait for 10 seconds to appear title of the web page
	 * @param locator - find and match the elements of web page
	 * @return WebElement value
	 * @author Karthikeyan Rajendran on 13/11/2017:18:00:00PM
	 * @throws Exception
	 */
	public List<WebElement> getElements(String locator){
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");		
	    
		String[] data = locator.split("&");
		String key = data[0];
		String value = data[1];
		List<WebElement> wReturn = null;
		if (key.equalsIgnoreCase("ID")) {
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(value)));	
				wReturn = getDriver().findElementsById(value);
			} catch (Exception e) {
				Log.fatal("Unable to find element "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("NAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(value)));		
				wReturn = getDriver().findElementsByName(value);
			} catch (Exception e) {
				Log.fatal("Unable to find element "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("CLASSNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(value)));	
				wReturn = getDriver().findElementsByClassName(value);
			} catch (Exception e) {
				Log.fatal("Unable to find element "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("TAGNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(value)));	
				wReturn = getDriver().findElementsByTagName(value);
			} catch (Exception e) {
				Log.fatal("Unable to find element "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("CSSSELECTOR")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(value)));
				wReturn = getDriver().findElementsByCssSelector(value);
			} catch (Exception e) {
				Log.fatal("Unable to find element "+e.toString());				
			}			
		}else if(key.equalsIgnoreCase("XPATH")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(value)));	
				wReturn = getDriver().findElementsByXPath(value);
			} catch (Exception e) {
				Log.fatal("Unable to find element "+e.toString());				
			}			
		}else{
			Log.fatal("Kindly, provide correct locator option for getElements.");			
			throw new RuntimeException("Kindly, provide correct locator option for getElements.");
		}
		return wReturn;
	}
	
	/**
	 * This method is used to start chrome browser for windows OS
	 * @author Karthikeyan Rajendran on 14/11/2017:19:01:00PM
	 * @throws Exception
	 */
	public void startChromeServer(){
		if(os.contains("Windows")){
			try {
				Runtime.getRuntime().exec("./drivers/chromedriver.exe", null, new File("./drivers"));
				System.err.println("Starting ChromeDriver on 9515. Only local connections are allowed.");
				Log.info("Starting ChromeDriver on 9515. Only local connections are allowed.");
			} catch (Exception e) {
				Log.fatal("Unable to start Chrome Server error ===> "+e.getMessage());
				throw new RuntimeException("Unable to start Chrome Server error ===> "+e.getMessage());
			}			
		}		
	}
	
	/**
	 * This method is used to stop chrome browser for windows OS
	 * @author Karthikeyan Rajendran on 14/11/2017:19:01:00PM
	 * @throws Exception
	 */
	public void stopChromeServer(){
		if(os.contains("Windows")){
			try {			
				Runtime.getRuntime().exec("taskkill /F /IM " + "chromedriver.exe");
				Log.info("Stoping Chrome Server.");
			} catch (Exception e) {
				Log.fatal("Unable to stop Chrome Server error ===> "+e.getMessage());
				throw new RuntimeException("Unable to stop Chrome Server error ===> "+e.getMessage());
			}
		}		
	}	
	
	public void switchToChildWindows(){
		ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
		getDriver().switchTo().window(tabs.get(1));		
	}
	
	public void switchToParentWindow(){
		ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
		getDriver().switchTo().window(tabs.get(0));	
	}
	
	public void closeWindow(){
		getDriver().close();
	}
	
	/**
	 * This method used to verify the title of the page
	 * and wait for 10 seconds to appear title of the web page
	 * @param expected - web page's expected title
	 * @return boolean value
	 * @author Karthikeyan Rajendran 01/12/2017:15:00:00PM
	 * @throws Exception
	 */
	public boolean verifyTitleOfThePage(String expected){	
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");
	    
		boolean bReturn = false;
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), 10);
			wait.until(ExpectedConditions.titleContains(expected));
			Log.info("The Title of the page is "+getDriver().getTitle()+" same as the expected "+expected);
			bReturn = true;
		} catch (Exception e) {	
			Log.fatal("Unable to find "+expected+" title in the page "+e.toString());	
			throw new RuntimeException("Unable to find "+expected+" title in the page "+e.toString());
		}		
		return bReturn;
	}	
	
	public boolean verifyText(WebElement element,String expected){
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");
	    
	    boolean bReturn = false;
	    try {
			WebDriverWait wait = new WebDriverWait(getDriver(), 10);
			wait.until(ExpectedConditions.visibilityOf(element));
			if(element.getText().trim().equals(expected)){
				Log.info("The text: "+element.getText().trim()+" matches with the value : "+expected);
				bReturn = true;
			}else{
				Log.error("The text: "+element.getText().trim()+" did not match with the value : "+expected);					
			}
		} catch (Exception e) {
			Log.fatal("Unable to find the given element "+element+" : "+e.toString());			
			throw new RuntimeException("Unable to find the given element "+element+" : "+e.toString());
		}
		return bReturn;
	}
	
	public void waitTime(){
		try {
			Thread.sleep(3000);
		}catch(InterruptedException e){
			Log.fatal(e.toString());
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used to perform mouse over and click actions
	 * and wait for 10 seconds to appear title of the web page
	 * @param locator - find and match the elements of web page
	 * @return boolean value
	 * @author Karthikeyan Rajendran on 13/11/2017:13:30:00PM
	 */
	public boolean mouseOverAndClickAction(String locator){
		//Log4j Configuration XML file 	
	    DOMConfigurator.configure("log4j.xml");		
	    
		String[] data = locator.split("&");
		String key = data[0];
		String value = data[1];
		WebElement ele;
		Actions builder = new Actions(getDriver());
		boolean bReturn = false;
		if(key.equalsIgnoreCase("ID")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(value)));
				ele = getDriver().findElementById(value);			
				builder.moveToElement(ele).click().build().perform();
				bReturn = true;
				Log.info("The mouse over by id : "+value+" is performed.");
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());
				throw new RuntimeException("Unable to find the given element "+value+" :"+e.toString());
			}		    
		}else if(key.equalsIgnoreCase("NAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(value)));
				ele = getDriver().findElementByName(value);
				builder.moveToElement(ele).click().build().perform();
				bReturn = true;
				Log.info("The mouse over by name : "+value+" is performed.");
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());
				throw new RuntimeException("Unable to find the given element "+value+" :"+e.toString());
			}			
		}else if(key.equalsIgnoreCase("LINKTEXT")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(value)));
				ele = getDriver().findElementByLinkText(value);
				builder.moveToElement(ele).click().build().perform();
				bReturn = true;
				Log.info("The mouse over by linktext : "+value+" is performed.");
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());
				throw new RuntimeException("Unable to find the given element "+value+" :"+e.toString());
			}			
		}else if(key.equalsIgnoreCase("PARTIALLINKTEXT")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(value)));
				ele = getDriver().findElementByPartialLinkText(value);
				builder.moveToElement(ele).click().build().perform();
				bReturn = true;
				Log.info("The mouse over by partial linktext : "+value+" is performed.");
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());
				throw new RuntimeException("Unable to find the given element "+value+" :"+e.toString());
			}			
		}else if(key.equalsIgnoreCase("CLASSNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(value)));
				ele = getDriver().findElementByClassName(value);
				builder.moveToElement(ele).click().build().perform();
				bReturn = true;
				Log.info("The mouse over by classname : "+value+" is performed.");
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());
				throw new RuntimeException("Unable to find the given element "+value+" :"+e.toString());
			}			
		}else if(key.equalsIgnoreCase("TAGNAME")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(value)));
				ele = getDriver().findElementByTagName(value);
				builder.moveToElement(ele).click().build().perform();
				bReturn = true;
				Log.info("The mouse over by tagname : "+value+" is performed.");
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());
				throw new RuntimeException("Unable to find the given element "+value+" :"+e.toString());
			}			
		}else if(key.equalsIgnoreCase("CSSSELECTOR")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(value)));
				ele = getDriver().findElementByCssSelector(value);
				builder.moveToElement(ele).click().build().perform();
				bReturn = true;
				Log.info("The mouse over by cssselector : "+value+" is performed.");
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());
				throw new RuntimeException("Unable to find the given element "+value+" :"+e.toString());
			}			
		}else if(key.equalsIgnoreCase("XPATH")){
			try {
				WebDriverWait wait = new WebDriverWait(getDriver(), 10);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(value)));
				ele = getDriver().findElementByXPath(value);
				builder.moveToElement(ele).click().build().perform();
				bReturn = true;
				Log.info("The mouse over by xpath : "+value+" is performed.");
			} catch (Exception e) {				
				Log.fatal("Unable to find the given element "+value+" :"+e.toString());
				throw new RuntimeException("Unable to find the given element "+value+" :"+e.toString());
			}			
		}else{
			Log.error("Kindly, provide correct locator option for mouseOverAction.");			
			throw new RuntimeException("Kindly, provide correct locator option for mouseOverAction.");
		}
		return bReturn;
	}

}
