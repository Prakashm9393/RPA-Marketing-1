package activities;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SystemDetails {
	
	public String myIpAddress(){
		String myIp = null;
		try {
			InetAddress IP=InetAddress.getLocalHost();
			myIp = IP.toString();
		} catch (UnknownHostException e) {			
			e.printStackTrace();
		}
		return myIp;
	}

}
