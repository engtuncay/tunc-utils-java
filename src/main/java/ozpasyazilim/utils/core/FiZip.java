package ozpasyazilim.utils.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import ozpasyazilim.utils.log.Loghelper;

public class FiZip {

	public static void main(String[] args) {

		Date date = new Date();

		String timestamp = FiDate.tosTimestamptText(date);

		String pathtozip = "\\\\Server\\aktarim\\TORKU\\AKTARIM_YEDEK\\Aktarim_" + timestamp + ".zip";
		String filename = "Aktarim_";

		File file1 = new File("d:\\demozip.txt");
		File file2 = new File("d:\\demozip1.txt");

		List<File> listfile = new ArrayList<>();

		listfile.add(file1);
		listfile.add(file2);

		FiZip.saveFilesZipFormat(listfile, pathtozip);

	}


	public static void saveFilesZipFormat(List<File> listfile, String pathandfilename) {

		byte[] buffer = new byte[1024];

		try {

			FileOutputStream fos = new FileOutputStream(pathandfilename);
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (Iterator iterator = listfile.iterator(); iterator.hasNext();) {
				File file = (File) iterator.next();
				ZipEntry ze = new ZipEntry(file.getName());
				zos.putNextEntry(ze);
			}


			for (Iterator iterator = listfile.iterator(); iterator.hasNext();) {
				File file = (File) iterator.next();

				FileInputStream in = new FileInputStream(file.getAbsolutePath());
				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
				in.close();
			}

			zos.closeEntry();

			// remember close it
			zos.close();

			// System.out.println("Done");
			Loghelper.get(FiZip.class).info("Zip is done");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static File zipFileMaker(List<File> fileList, String zipFilename) {

		File zipfile = new File(zipFilename);

		//Loghelper.debug(ModMikroAktarimFileUtils.class, "zip file:"+ zipfile.getPath());

		// Create a buffer for reading the fileList
		byte[] buf = new byte[1024];
		try {

			// create the ZIP file
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
			// compress the fileList

			for (int i = 0; i < fileList.size(); i++) {

				FileInputStream in = new FileInputStream(fileList.get(i).getAbsolutePath());
				// add ZIP entry to output stream
				out.putNextEntry(new ZipEntry(fileList.get(i).getName()));
				// transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				// complete the entry
				out.closeEntry();
				in.close();

			}

			// complete the ZIP file
			out.close();
			return zipfile;

		} catch (IOException ex) {
			Loghelper.get(FiZip.class).error("Hata (zipleme):" + FiException.exTosMain(ex));
		}

		return null;
	}





}