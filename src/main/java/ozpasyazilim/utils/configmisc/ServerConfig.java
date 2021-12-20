package ozpasyazilim.utils.configmisc;

public class ServerConfig {

	private String server;
	private String name;
	private String serverDb;
	private String serverUser;
	private String serverKey;

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getServerDb() {
		return serverDb;
	}

	public void setServerDb(String serverDb) {
		this.serverDb = serverDb;
	}

	public String getServerUser() {
		return serverUser;
	}

	public void setServerUser(String serverUser) {
		this.serverUser = serverUser;
	}

	public String getServerKey() {
		return serverKey;
	}

	public void setServerKey(String serverKey) {
		this.serverKey = serverKey;
	}

	public String getName() {return name;}

	public void setName(String name) {this.name = name;}

}
