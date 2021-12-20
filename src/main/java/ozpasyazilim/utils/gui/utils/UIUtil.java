package ozpasyazilim.utils.gui.utils;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;

import ozpasyazilim.utils.gui.icons.IconManager;


public class UIUtil {
	public UIUtil() {
	}

	public static void showAtCenter(JDialog d) {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = d.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		d.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}

	public static void showAtCenter(JFrame d) {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = d.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		d.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}

	public static ImageIcon getIcon(String name) {
		return new ImageIcon(IconManager.class.getResource(name));
	}

	public static void setOpenningDialogSettings(JDialog dialog) {
		showAtCenter(dialog);
		dialog.setAlwaysOnTop(true);
		dialog.setVisible(true);
	}

	public static void setOpenningDialogSettingsWithoutAlwaysOnTop(JDialog dialog) {
		showAtCenter(dialog);
		dialog.setVisible(true);
	}
}
