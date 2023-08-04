package ozpasyazilim.utils.core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FilenameUtils;
import ozpasyazilim.utils.log.Loghelper;


public class FiFile {

	public static void main(String[] args) {

		Loghelper.installLogger(true);

		String pathfile = "Y:\\TEST\\PANSIS\\SatisFaturasi-04.12.2019-17.37-Lite.xml";

		String pathfile2 = "Y:\\TEST\\PANSIS\\SatisFaturasi-04.12.2019-20.45.xml";
		String pathfile3 = "Y:\\TEST\\PANSIS\\SatisFaturasi-05.12.2019-20.45.xml";
		File file = new File(pathfile2);

		String result = getStrFilePathWoutExt(file.getPath());

		System.out.println(result);

		File file1 = new File(pathfile);
		File file2 = new File(pathfile2);
		File file3 = new File(pathfile3);

		List<File> files = new ArrayList();
		files.add(file1);
		files.add(file2);
		//files.add(file3);

		// XNOTE md note alalım
		Collections.sort(files, (o1, o2) -> {
			Collator collator = Collator.getInstance(Locale.getDefault());

			String sRegex = ".*-(\\d{2})\\.(\\d{2})\\.(\\d{4})-(\\d{2})\\.(\\d{2}).*";
			Matcher matcher = FiRegExp.match(sRegex, o1.getName());

			String v1 = "";
			if (matcher != null && matcher.groupCount() > 4) {
				v1 = matcher.group(3) + matcher.group(2) + matcher.group(1) + "_" + matcher.group(4) + matcher.group(5);
			}

			matcher = FiRegExp.match(sRegex, o2.getName());
			String v2 = "";
			if (matcher != null && matcher.groupCount() > 4) {
				v2 = matcher.group(3) + matcher.group(2) + matcher.group(1) + "_" + matcher.group(4) + matcher.group(5);
			}

			System.out.println("v1:" + v1);
			System.out.println("v2:" + v2);

			return collator.compare(v1, v2) * -1; // o1>o2 1,o1<o2 -1 :: -1 --> 1, küçükten büyüğe
			// -1 ile çarpınca büyükten küçüğe döner
		});

		//FiConsole.debug(files.get(0));

	}

	public static String getCurrentTimeStampForFile() {
		Date date = new Date();
		SimpleDateFormat formatter5 = new SimpleDateFormat("yyyyMMddhhmmss");
		return formatter5.format(date);
	}

	public static String removeFileExtension(String filename) {
		return FilenameUtils.removeExtension(filename);
	}

	public static Boolean checkSaveFolder(File file) {

		try {

			if (!file.exists()) {
				if (file.mkdir()) {
					// Loghelper.getInstance(thisclass).info("Directory is created." +
					// file.getName());
					return true;
				} else {
					// Loghelper.getInstance(thisclass).error("Failed to create directory !!!" +
					// file.getName());
					// System.out.println("Failed to create directory!");
					return false;
				}
			}

		} catch (Exception e) {
			// Loghelper.getInstance(thisclass).error("Hata :" +
			// UtilModel.exceptiontostring(e));
			return false;
		}

		return true;
	}

	public static Boolean existFileRelative(String filename) {

		File fileRelative4 = new File("./" + filename);
		return fileRelative4.exists();

	}

	public static File getFileRelativeToApplicationDir(String filename) {

		File fileRelative = new File("./" + filename);
		return fileRelative;

	}

	public static Boolean copyFileWithIndexNo(File source, File destination) {

		String fileNameAndExt = destination.getName();
		String extension = getExtensionFromFilename(fileNameAndExt);
		String filePathWoutExt = FiFile.getStrFilePathWoutExt(destination.getPath());
		String folderPathFromFileName = FiFile.getFolderPathFromFileName(destination.getPath());

		//Loghelper.debug(FiFile.class, "filename and ext:"+fileNameAndExt);
//		Loghelper.debug(FiFile.class, "file path wout ext:"+filePathWoutExt);
//		Loghelper.debug(FiFile.class, "folder path:"+folderPathFromFileName);

		File fileDest = new File(folderPathFromFileName); //destination.getParentFile();

//		Loghelper.debug(FiFile.class, "Dosya null mi fileBParenet:"+ fileDest.getPath());

		if (!fileDest.exists()) {
			fileDest.mkdirs();
		}

		//FIXME util metod boş dosya adı oluştur
		// aynı adda dosya var olup olmadığı kontrol edilir, varsa index atılır
		int i = 1;
		while (destination.exists()) {
			destination = new File(filePathWoutExt + "(" + i + ")." + extension);
			i++;
		}

		// Loghelper.getInstance(thisclass).info("Parent Path:" +
		// fileDest.getPath());

		InputStream inStream = null;
		OutputStream outStream = null;

		try {

			// File source = new File("C:\\folderA\\Afile.txt");
			// File destination = new File("C:\\folderB\\Afile.txt");

			inStream = Files.newInputStream(source.toPath());
			outStream = Files.newOutputStream(destination.toPath());

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}

			inStream.close();
			outStream.close();

			// delete the original file
			// source.delete();

			System.out.println("File is copied successful!");
			return true;

		} catch (IOException ex) {
			Loghelper.get(getClassi()).error(FiException.exceptionToStrMain(ex));
			return false;
		}
	}

	public static Boolean copyFileOverWrite(File source, File destination) {

		String fileNameAndExt = destination.getName();
		String extension = getExtensionFromFilename(fileNameAndExt);
		String filePathWoutExt = FiFile.getStrFilePathWoutExt(destination.getPath());
		String folderPathFromFileName = FiFile.getFolderPathFromFileName(destination.getPath());

		//Loghelper.debug(FiFile.class, "filename and ext:"+fileNameAndExt);
//		Loghelper.debug(FiFile.class, "file path wout ext:"+filePathWoutExt);
//		Loghelper.debug(FiFile.class, "folder path:"+folderPathFromFileName);

		File fileDest = new File(folderPathFromFileName); //destination.getParentFile();

		//Loghelper.debug(FiFile.class, "Dosya null mi fileBParenet:"+ fileDest.getPath());

		if (!fileDest.exists()) {
			fileDest.mkdirs();
		}

		//if (destination.exists()) {}

		InputStream inStream = null;
		OutputStream outStream = null;

		try {
			inStream = Files.newInputStream(source.toPath());
			outStream = Files.newOutputStream(destination.toPath());

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}

			inStream.close();
			outStream.close();

			// delete the original file
			// source.delete();

			System.out.println("File is copied successful!");
			return true;

		} catch (IOException ex) {
			Loghelper.get(getClassi()).error(FiException.exceptionToStrMain(ex));
			return false;
		}
	}
	private static String getFolderPathFromFileName(String filename) {
		Pattern pattern = Pattern.compile("^(.+)\\\\([^\\\\]+)$");
		Matcher matcher = pattern.matcher(filename);

		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return "-1";
		}
	}

	public static File checkGenerateFilename(File file) {

		int i = 1;
		while (file.exists()) {
			String filenameandext = file.getName();
			String extension = getExtensionFromFilename(filenameandext);
			String filename_wo_ext = getStrFilePathWoutExt(filenameandext);
			file = new File(filename_wo_ext + "(" + i + ")." + extension);
			i++;
		}

		return file;

	}

	public static void saveHtmlContent_to_File(String content) {

		String timestamp = FiDate.datetoString_timestampt3(new Date());

		// File file = new File("y:\\ornekhtml-" + timestamp + ".html");

		// Get the file reference
		Path path = Paths.get("y://ornekthtml-" + timestamp + ".html");

		// Use try-with-resource to get auto-closeable writer instance
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
			Loghelper.get(FiFile.class).info("the file is not saved");
			return;
		}

		Loghelper.get(FiFile.class).info("the file is saved");

		// try {
		// Files.write(Paths.get("d://ornekthtml-" + timestamp + ".html"),
		// content.getBytes());
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// BufferedWriter bw = null;
		// try {
		// bw = new BufferedWriter(new FileWriter(file));
		// bw.write(content);
		// bw.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}


	public static File selectFileDialogSwing(String title, String extension) {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setApproveButtonText("Seç");
		fileChooser.setDialogTitle(title);
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File file) {

				// null gelirse hepsini seçebilir
				if (extension == null) {
					return true;
				}

				String fileextension = "";
				int i = file.getPath().lastIndexOf('.');
				if (i >= 0) {
					fileextension = file.getPath().substring(i + 1);
				}
				return (fileextension.equals(extension)) || (file.isDirectory());
			}

			@Override
			public String getDescription() {
				return extension;
			}
		});

		int j = fileChooser.showOpenDialog(null);

		if (j == 0) {
			// file selected do the works
			File file = fileChooser.getSelectedFile();
			return file;
		}

		return null;

	}

	/**
	 * Reads given resource file as a String.
	 *
	 * @param resourceFileName the path to the resource file
	 * @return the file's contents
	 */
	public static String getResourceFileAsString(String resourceFileName) {
		InputStream is = FiFile.class.getClassLoader().getResourceAsStream(resourceFileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		return reader.lines().collect(Collectors.joining("\n"));
	}

	public static String getContent1OfRelativeFile(String fileName) {
		String filePath = "./" + fileName;
		try {
			String result = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
			return result;
		} catch (IOException e) {
			Loghelper.get(getClassi()).error(FiException.exceptionIfToString(e));
			e.printStackTrace();
			return null;
		}
	}

	private static Class<FiFile> getClassi() {
		return FiFile.class;
	}

	public static List<File> findFiles(String strFilestartswith, String sExtension, String path, Boolean trCharacterException) {
		return findFiles(strFilestartswith, sExtension, path);
	}

	/**
	 * CI case insensitive arama yapar
	 *
	 * @param txFileStartWith
	 * @param txExtension
	 * @param path
	 * @return
	 */
	public static List<File> findFilesCI(String txFileStartWith, String txExtension, String path) {
		return findFiles(FiString.regexChangePatternCaseInsensitive(txFileStartWith), txExtension, path);
	}

	public static List<File> findFiles(String txFileStartWith, String txExtension, String path) {

		//if(!path.matches(".*\\$")) path = path + "\\"; // sonuna tire koymaya gerek yok
		//Loghelper.getInstance(FiFile.class).debug("Path:"+path);

		String searchingPath = path;

		if (searchingPath == null) searchingPath = "";

		String pathescaped = FiString.escapePathString(searchingPath);

		File folder = new File(pathescaped);
		// gets you the list of files at this folder
		File[] arrFiles = folder.listFiles();

		List<File> listFiles = new ArrayList<>();
		if (arrFiles == null) return listFiles;

		// loop through each of the files looking for filenames that match
		for (int i = 0; i < arrFiles.length; i++) {

			String filename = arrFiles[i].getName();
			//Loghelper.getInstance(FiFile.class).debug("file:"+filename);

			if (filename.matches(String.format("(?i)%s.*\\.%s$", txFileStartWith, txExtension))) { // (?i) case insensitive yapar.
				//Loghelper.getInstance(FiFile.class).info("Dosya bulundu :" + arrFiles[i].getPath());
				listFiles.add(arrFiles[i]);
			}

		}
		return listFiles;
	}


	public static String getExtensionFromFilename(String filename) {

		Pattern pattern = Pattern.compile("^.*\\.(.*)");
		Matcher matcher = pattern.matcher(filename);

		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return "-1";
		}

	}

	public static String getStrFilePathWoutExt(String filename) {

		Pattern pattern = Pattern.compile("(.+)\\\\([^\\\\]+)\\.(.*)$"); //
		Matcher matcher = pattern.matcher(filename);

		if (matcher.find() && matcher.groupCount() > 1 && matcher.group(1) != null && matcher.group(2) != null) {
			return matcher.group(1) + "\\" + matcher.group(2);
		} else {
			return "-1";
		}

	}

	public static String getWorkingDirectory() {
		return System.getProperty("user.dir");
	}

}

//	public List actReadfromcsvfiletolist_rut(File csvRutfile) {
//
//		List listRutcsv = new ArrayList();
//		String csvFile = csvRutfile.getPath();
//		BufferedReader br = null;
//		String line = "";
//		String cvsSplitBy = ";";
//		try {
//			FileInputStream fr = new FileInputStream(csvFile);
//			InputStreamReader isr = new InputStreamReader(fr, Charset.forName("ISO-8859-9"));
//			br = new BufferedReader(isr);
//
//			line = br.readLine();
//
//			// if the file is not delimited by semicolon, change it to comma
//			String[] testcolumns = line.split(cvsSplitBy);
//			if (testcolumns.length < 3) {
//				cvsSplitBy = ",";
//			}
//
//			// csv file is reading
//			while ((line = br.readLine()) != null) {
//
//				try {
//					EntCari musterirut = new EntCari();
//					String[] columns = line.split(cvsSplitBy);
//
//					if (columns[0].length() < 3) {
//						int ekleneceksifirmiktari = 3 - columns[0].length();
//
//						String strsifir = "";
//						while (ekleneceksifirmiktari > 0) {
//							strsifir = strsifir + "0";
//							ekleneceksifirmiktari--;
//						}
//						columns[0] = (strsifir + columns[0]);
//					}
//					musterirut.setTemsilcikodu(columns[0]);
//					musterirut.setGunkodu(FiDate.gunkodunacevir(columns[1]));
//					musterirut.setMusterikodu(columns[2]);
//					if (columns.length > 2)
//						musterirut.setAciklama(columns[3]);
//					listRutcsv.add(musterirut);
//				} catch (IndexOutOfBoundsException e) {
//					Platform.runLater(() -> {
//						FxDialogShow.showModalError("dosyada hata var \n" + e.toString());
//					});
//
//				}
//
//			}
//
//			if (br != null) {
//				try {
//					br.close();
//				} catch (IOException e) {
//					Loghelper2.getInstance(this.getClass()).info("Hata :" + FiException.exceptiontostring(e));
//				}
//			}
//			System.out.println("Csv Aktarıldı");
//		} catch (FileNotFoundException e) {
//			Loghelper2.getInstance(this.getClass()).info("Hata :" + FiException.exceptiontostring(e));
//		} catch (IOException e) {
//			Loghelper2.getInstance(this.getClass()).info("Hata :" + FiException.exceptiontostring(e));
//		} finally {
//			if (br != null) {
//				try {
//					br.close();
//				} catch (IOException e) {
//					Loghelper2.getInstance(this.getClass()).info("Hata :" + FiException.exceptiontostring(e));
//				}
//			}
//		}
//		return listRutcsv;
//	}