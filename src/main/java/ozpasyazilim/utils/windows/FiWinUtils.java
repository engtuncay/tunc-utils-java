package ozpasyazilim.utils.windows;

import java.io.*;

public class FiWinUtils {

	private static final String REGQUERY_UTIL = "reg query ";
	private static final String REGSTR_TOKEN = "REG_SZ";
	private static final String DESKTOP_FOLDER_CMD = REGQUERY_UTIL
			+ "\"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\"
			+ "Explorer\\Shell Folders\" /v DESKTOP";

	private static String userDirectory;

	private static void setUserDirectory(String userDirectory) {
		FiWinUtils.userDirectory = userDirectory;
	}

	private FiWinUtils() {
	}

	public static String getUserDirOrDesktopDir() {
		if (userDirectory != null) return userDirectory;
		return findCurrenUserDesktopPath();
	}

	private static String findCurrenUserDesktopPath() {

		try {

			Process process = Runtime.getRuntime().exec(DESKTOP_FOLDER_CMD);
			StreamReader reader = new StreamReader(process.getInputStream());

			reader.start();
			process.waitFor();
			reader.join();
			String result = reader.getResult();
			int p = result.indexOf(REGSTR_TOKEN);

			if (p == -1) return null;
			String desktopDir = result.substring(p + REGSTR_TOKEN.length()).trim();
			setUserDirectory(desktopDir);
			return desktopDir;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * TEST
	 */
	public static void main(String[] args) {
		System.out.println("Desktop directory : "
				+ getUserDirOrDesktopDir());
	}

	static class StreamReader extends Thread {
		private InputStream is;
		private StringWriter sw;

		StreamReader(InputStream is) {
			this.is = is;
			sw = new StringWriter();
		}

		public void run() {
			try {
				int c;
				while ((c = is.read()) != -1)
					sw.write(c);
			} catch (IOException e) {
				;
			}
		}

		String getResult() {
			return sw.toString();
		}
	}
}