package ozpasyazilim.utils.core;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * A utility class that provides functionality for downloading files from a FTP
 * server.
 *
 * @author www.codejava.net
 *
 */
public class FTPUtility {

	// FTP server information
	private String host;
	private int port;
	private String username;
	private String password;

	private FTPClient ftpClient = new FTPClient();
	private int replyCode;

	private InputStream inputStream;

	public FTPUtility(String host, int port, String user, String pass) {
		this.host = host;
		this.port = port;
		this.username = user;
		this.password = pass;
	}

	/**
	 * Connect and login to the server.
	 *
	 * @throws FTPException
	 */
	public void connect() throws FTPException {
		try {
			ftpClient.connect(host, port);
			replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				throw new FTPException("FTP serve refused connection.");
			}

			boolean logged = ftpClient.login(username, password);
			if (!logged) {
				// failed to login
				ftpClient.disconnect();
				throw new FTPException("Could not login to the server.");
			}

			ftpClient.enterLocalPassiveMode();

		} catch (IOException ex) {
			throw new FTPException("I/O error: " + ex.getMessage());
		}
	}

	/**
	 * Gets size (in bytes) of the file on the server.
	 *
	 * @param filePath
	 *            Path of the file on server
	 * @return file size in bytes
	 * @throws FTPException
	 */
	public long getFileSize(String filePath) throws FTPException {
		try {
			FTPFile file = ftpClient.mlistFile(filePath);
			if (file == null) {
				throw new FTPException("The file may not exist on the server!");
			}
			return file.getSize();
		} catch (IOException ex) {
			throw new FTPException("Could not determine size of the file: " + ex.getMessage());
		}
	}

	/**
	 * Start downloading a file from the server
	 *
	 * @param downloadPath
	 *            Full path of the file on the server
	 * @throws FTPException
	 *             if client-server communication error occurred
	 */
	public void downloadFile(String downloadPath) throws FTPException {
		try {

			boolean success = ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			if (!success) {
				throw new FTPException("Could not set binary file type.");
			}

			inputStream = ftpClient.retrieveFileStream(downloadPath);

			if (inputStream == null) {
				throw new FTPException("Could not open input stream. The file may not exist on the server.");
			}
		} catch (IOException ex) {
			throw new FTPException("Error downloading file: " + ex.getMessage());
		}
	}

	/**
	 * Complete the download operation.
	 */
	public void finish() throws IOException {
		inputStream.close();
		ftpClient.completePendingCommand();
	}

	/**
	 * Log out and disconnect from the server
	 */
	public void disconnect() throws FTPException {
		if (ftpClient.isConnected()) {
			try {
				if (!ftpClient.logout()) {
					throw new FTPException("Could not log out from the server");
				}
				ftpClient.disconnect();
			} catch (IOException ex) {
				throw new FTPException("Error disconnect from the server: " + ex.getMessage());
			}
		}
	}

	/**
	 * Return InputStream of the remote file on the server.
	 */
	public InputStream getInputStream() {
		return inputStream;
	}
}