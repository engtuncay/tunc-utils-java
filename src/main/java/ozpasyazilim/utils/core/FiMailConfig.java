package ozpasyazilim.utils.core;

public class FiMailConfig {

	String username;
	String password;
	String fromAddress;
	String fromAdressLabel;
	String host;
	String smtpPort;

	// Getter and Setter

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getFromAdressLabel() {
		return fromAdressLabel;
	}

	public void setFromAdressLabel(String fromAdressLabel) {
		this.fromAdressLabel = fromAdressLabel;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}
}
