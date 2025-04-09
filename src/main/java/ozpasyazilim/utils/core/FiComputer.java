package ozpasyazilim.utils.core;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import ozpasyazilim.utils.log.Loghelper;


public class FiComputer {

	public static void main(String[] args) {

		System.out.println("Host:" + getHostName());
		System.out.println("Mac:" + new FiComputer().getMacAddress());

	}

	public static String getHostName() {

		// N/A means unknown host name
		String hostname = "---";

		try {
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
		} catch (UnknownHostException ex) {
			Loghelper.get(FiComputer.class).error("Host ismi Bulunamadı.");
		}

		return hostname;
	}

	public static String getHostNameNullable() {

		// N/A means unknown host name
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return addr.getHostName();
		} catch (UnknownHostException ex) {
			Loghelper.get(FiComputer.class).error("Host ismi bulunamadı :"+ FiException.exToErrorLog(ex));
		}

		return null;
	}

	public String getMacAddress() {

		try {

			InetAddress ip = InetAddress.getLocalHost();
			//System.out.println("Current IP address : " + ip.getHostAddress());

			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();

			//System.out.print("Current MAC address : ");

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				//sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			//System.out.println(sb.toString());
			return sb.toString();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}


}
