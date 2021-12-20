package ozpasyazilim.utils.gui.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import ozpasyazilim.utils.core.FTPException;
import ozpasyazilim.utils.core.FTPUtility;

/**
 * Execute file download in a background thread and update the progress.
 *
 * @author www.codejava.net
 *
 */
public class DownloadTask extends SwingWorker<Void, Void> {

	private static final int BUFFER_SIZE = 4096;

	private String host;
	private int port;
	private String username;
	private String password;

	private String downloadPath;
	private String saveDir;

	private SwingFileDownloadFTP gui;

	public DownloadTask(String host, int port, String username, String password, String downloadPath, String saveDir,
		SwingFileDownloadFTP gui) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.downloadPath = downloadPath;
		this.saveDir = saveDir;
		this.gui = gui;
	}

	/**
	 * Executed in background thread
	 */
	@Override
	protected Void doInBackground() throws Exception {
		FTPUtility util = new FTPUtility(host, port, username, password);
		try {
			util.connect();

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			long totalBytesRead = 0;
			int percentCompleted = 0;

			long fileSize = util.getFileSize(downloadPath);
			gui.setFileSize(fileSize);

			String fileName = new File(downloadPath).getName();

			File downloadFile = new File(saveDir + File.separator + fileName);
			FileOutputStream outputStream = new FileOutputStream(downloadFile);

			util.downloadFile(downloadPath);
			InputStream inputStream = util.getInputStream();

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
				totalBytesRead += bytesRead;
				percentCompleted = (int) (totalBytesRead * 100 / fileSize);
				setProgress(percentCompleted);
			}

			outputStream.close();

			util.finish();
		} catch (FTPException ex) {
			JOptionPane.showMessageDialog(null, "Error downloading file: " + ex.getMessage(), "Error",
				JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			setProgress(0);
			cancel(true);
		} finally {
			util.disconnect();
		}

		return null;
	}

	/**
	 * Executed in Swing's event dispatching thread
	 */
	@Override
	protected void done() {
		if (!isCancelled()) {
			JOptionPane.showMessageDialog(null, "File has been downloaded successfully!", "Message",
				JOptionPane.INFORMATION_MESSAGE);
		}
	}
}