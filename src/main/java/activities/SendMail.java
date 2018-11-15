package activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.testng.annotations.Test;

public class SendMail extends AutomaticSendMail {
	
	public static void sendSuccessMailForGWT3Url(){
		Properties prop = new Properties();		
		try {
			prop.load(new FileInputStream(new File("./mail.properties")));			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		SystemDetails mySys = new SystemDetails();
		String mySystem = mySys.myIpAddress();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String time = dateFormat.format(date);
		System.out.println("Sending....");
		sendSuccessReportByGmail(prop.getProperty("Automation.Mail.To"), prop.getProperty("Automation.Mail.Subject"), prop.getProperty("Automation.Mail.Body.Text"), time, prop.getProperty("Automation.Mail.CC"), prop.getProperty("Automation.Mail.For.GWT3Url"),mySystem);
		System.out.println("Sent.");
	}
	
	public static void sendSuccessMailForGWT3Result(){
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("./mail.properties")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		SystemDetails mySys = new SystemDetails();
		String mySystem = mySys.myIpAddress();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String time = dateFormat.format(date);
		System.out.println("Sending....");
		sendSuccessReportByGmail(prop.getProperty("Automation.Mail.To"), prop.getProperty("Automation.Mail.Subject"), prop.getProperty("Automation.Mail.Body.Text"), time, prop.getProperty("Automation.Mail.CC"), prop.getProperty("Automation.Mail.For.GWT3Result"),mySystem);
		System.out.println("Sent.");
	}
	
	public static void sendSuccessMailForGWT2Url(){
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("./mail.properties")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		SystemDetails mySys = new SystemDetails();
		String mySystem = mySys.myIpAddress();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String time = dateFormat.format(date);
		System.out.println("Sending....");
		sendSuccessReportByGmail(prop.getProperty("Automation.Mail.To"), prop.getProperty("Automation.Mail.Subject"), prop.getProperty("Automation.Mail.Body.Text"), time, prop.getProperty("Automation.Mail.CC"), prop.getProperty("Automation.Mail.For.GWT2Url"),mySystem);
		System.out.println("Sent.");
	}
	
	public static void sendSuccessMailForGWT2Result(){
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("./mail.properties")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		SystemDetails mySys = new SystemDetails();
		String mySystem = mySys.myIpAddress();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String time = dateFormat.format(date);
		System.out.println("Sending....");
		sendSuccessReportByGmail(prop.getProperty("Automation.Mail.To"), prop.getProperty("Automation.Mail.Subject"), prop.getProperty("Automation.Mail.Body.Text"), time, prop.getProperty("Automation.Mail.CC"), prop.getProperty("Automation.Mail.For.GWT2Result"),mySystem);
		System.out.println("Sent.");
	}	
	
	public static void sendFailureMail(){
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("./mail.properties")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		SystemDetails mySys = new SystemDetails();
		String mySystem = mySys.myIpAddress();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String time = dateFormat.format(date);
		System.out.println("Sending....");
		sendFailureReportByGmail(prop.getProperty("Automation.Mail.To"), prop.getProperty("Automation.Mail.Subject"), time, prop.getProperty("Automation.Mail.CC"),mySystem);
		System.out.println("Sent.");
	}	
	
	@Test
	public static void sendIntimationMail(){
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("./mail.properties")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		SystemDetails mySys = new SystemDetails();
		String mySystem = mySys.myIpAddress();		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String time = dateFormat.format(date);
		System.out.println("Sending....");
		sendIntimationMailByGmail(prop.getProperty("Automation.Mail.To"), prop.getProperty("Automation.Mail.Subject"), prop.getProperty("Automation.Mail.Body.Text"), time, prop.getProperty("Automation.Mail.CC"),mySystem);
		System.out.println("Sent.");
	}

}
