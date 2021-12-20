package ozpasyazilim.utils.gui.fxcomponents;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import ozpasyazilim.utils.windows.FiWinUtils;

import java.io.File;

public class FiFileGui {

	/**
	 * extensions : *.extension şeklinde yazılmalı
	 *
	 * @param title
	 * @param extDescription
	 * @param extensions
	 */
	public static File actFileChooserFromDesktop(String title, String extDescription, String... extensions) {

		final FileChooser chooser = new FileChooser();
		chooser.setTitle(title);
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extDescription, extensions));
		chooser.setInitialDirectory(new File(FiWinUtils.getDesktopDirectory()));

		File file = chooser.showOpenDialog(null);

		if (file != null) {
			//Loghelperr.getInstance(getClass()).debug(" File:" + file.getAbsolutePath());
			//file.setText(file.getAbsolutePath());
		} else {
			//throw new FileNotFoundException("Dosya bulunamadı");
		}

		return file;
	}

	public static File actFolderChooserFromDesktop(String title) {

		final DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle(title);
		chooser.setInitialDirectory(new File(FiWinUtils.getDesktopDirectory()));
		//chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extDescription, extensions));

		File file = chooser.showDialog(null);

		if (file != null) {
			//Loghelperr.getInstance(getClass()).debug(" File:" + file.getAbsolutePath());
			//file.setText(file.getAbsolutePath());
		} else {
			//throw new FileNotFoundException("Dosya bulunamadı");
		}

		return file;
	}

	public static File actFileChooserForExcelFromDesktop(String title) {
		return actFileChooserFromDesktop(title, "Excel Dosyalar", "*.xlsx", "*.xls");
	}

	public static File actFileChooserForExcelFromDesktop() {
		return actFileChooserFromDesktop("Excel Dosyasını Seçiniz...", "Excel Dosyalar", "*.xlsx", "*.xls");
	}

	public static File actFileChooserForExcelXlsxFromDesktop() {
		return actFileChooserFromDesktop("Excel Dosyasını Seçiniz...", "Excel Dosyalar", "*.xlsx");
	}

	public static File actFileChooserForXmlFromDesktop() {
		return actFileChooserFromDesktop("Xml Dosyasını Seçiniz...", "Xml Dosya", "*.xml");
	}


	public static Boolean checkFileResult(File fileExcel) {
		if (fileExcel == null) {
			//new FxDialogShow().showPopNotification("Dosya Seçilmedi veya Hata oluştu.");
			return false;
		}
		return true;
	}
}
