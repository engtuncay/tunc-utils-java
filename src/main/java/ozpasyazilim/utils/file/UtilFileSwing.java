package ozpasyazilim.utils.file;

import java.awt.*;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import ozpasyazilim.utils.log.Loghelper;

public class UtilFileSwing {

	public static void main(String[] args) {
		Loghelper.setupDebugMode(true);
		klasorSec("demo");
	}

	public static File klasorSec(String title) {

		Loghelper.get((new Throwable().getStackTrace()[0].getClassName())).info("metod başladı...");

		// ::dosya-ac

		JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setApproveButtonText("Seç");
		chooser.setDialogTitle(title);
		String currentUsersHomeDir = System.getProperty("user.home");
		File userdir = new File(currentUsersHomeDir); // System.getProperty("user.dir")
		// System.out.println("user dir " + userdir);
		chooser.setCurrentDirectory(userdir); // new java.io.File(".")
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		// fileChooser.setFileFilter(new FileFilter() {
		// @Override
		// public boolean accept(File file) {
		// String String = "";
		// int i = file.getPath().lastIndexOf('.');
		// if (i >= 0) {
		// String = file.getPath().substring(i + 1);
		// }
		// return (file.isDirectory());
		// }
		//
		// @Override
		// public String getDescription() {
		// return "Csv Dosyaları";
		// }
		// });

		// int j = chooser.showOpenDialog(null);
		//
		// if (j == 0) {
		// // file selected do the works
		// File file2 = chooser.getSelectedFile();
		//
		// }

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
			System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
			return chooser.getSelectedFile();
		} else {
			System.out.println("No Selection ");
			return null;
		}

	}

	public static File selectFile(String title, String extdesc, String... extension) {

		//JFileChooser chooser = new JFileChooser();
		JFileChooser chooser = new JFileChooser() {
			@Override
			protected JDialog createDialog(Component parent) throws HeadlessException {
				// intercept the dialog created by JFileChooser
				JDialog dialog = super.createDialog(parent);
				dialog.setModal(true);  // set modality (or setModalityType)
				dialog.setAlwaysOnTop(true);
				return dialog;
			}
		};

		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setApproveButtonText("Seç");
		chooser.setDialogTitle(title);

		String currentUsersHomeDir = System.getProperty("user.home");
		File userdir = new File(currentUsersHomeDir); // System.getProperty("user.dir")
		// System.out.println("user dir " + userdir);
		chooser.setCurrentDirectory(userdir); // new java.io.File(".")
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		FileFilter filter = new FileNameExtensionFilter(extdesc, extension);

		chooser.setFileFilter(filter);

		//	chooser.setFileFilter(new FileFilter() {
		//	    @Override
		//	    public boolean accept(File file) {
		//		String String = "";
		//		int i = file.getPath().lastIndexOf('.');
		//		if (i >= 0) {
		//		    String = file.getPath().substring(i + 1);
		//		}
		//
		//		Boolean condition = String.toLowerCase().equals("xlsx");
		//		return condition;
		//		// (file.isDirectory());
		//	    }
		//
		//	    @Override
		//	    public String getDescription() {
		//		return "Uygun Dosyalar";
		//	    }
		//	});

		// int j = chooser.showOpenDialog(null);
		//
		// if (j == 0) {
		// // file selected do the works
		// File file2 = chooser.getSelectedFile();
		//
		// }

		//	new JFileChooser().addChoosableFileFilter(new FileFilter() {
		//
		//	        @Override
		//	        public boolean accept(File f) {
		//	            return f.getName().endsWith(".jpg");
		//	        }
		//
		//	        @Override
		//	        public String getDescription() {
		//	            return "JPEG files";
		//	        }
		//
		//	    });

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			//System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
			//System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
			return chooser.getSelectedFile();
		} else {
			//System.out.println("No Selection ");
			return null;
		}

	}

}
