package ozpasyazilim.utils.gui.icons;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import java.awt.image.ColorModel;

import java.awt.image.PixelGrabber;

import java.net.URL;

import javax.swing.ImageIcon;

public class IconManager {
	public IconManager() {
	}

	public static ImageIcon getCloseImage() {
		return new ImageIcon(IconManager.class.getResource("16cancel.png"));
	}

	public static ImageIcon getClearImage() {
		return new ImageIcon(IconManager.class.getResource("clear.png"));
	}

	public static ImageIcon getFindImage() {
		return new ImageIcon(IconManager.class.getResource("16find.png"));
	}

	public static ImageIcon getAddImage() {
		return new ImageIcon(IconManager.class.getResource("16plus.png"));
	}

	public static ImageIcon getSaveImage() {
		return new ImageIcon(IconManager.class.getResource("16save.png"));
	}

	public static ImageIcon getRefreshImage() {
		return new ImageIcon(IconManager.class.getResource("refresh.jpg"));
	}

	public static ImageIcon getRemoveImage() {
		return new ImageIcon(IconManager.class.getResource("16cancel.png"));
	}

	public static ImageIcon getSubmitImage() {
		return new ImageIcon(IconManager.class.getResource("16yes.png"));
	}

	public static ImageIcon getRefuseImage() {
		return new ImageIcon(IconManager.class.getResource("16cancel.png"));
	}

	public static ImageIcon getPrintImage() {
		return new ImageIcon(IconManager.class.getResource("16print.png"));
	}

	public static ImageIcon getIcon(String iconName) {
		return new ImageIcon(IconManager.class.getResource(iconName));
	}

	public static BufferedImage getBufferedImage(String imageName) {

		// Loghelper2.getInstance(IconManager.class).info("buffer image");
		// System.out.println(IconManager.class.getResource("/16calender.png"));

		try {

			// URL location2 = IconManager.class.getResource("");
			// Loghelper2.getInstance(IconManager.class).info("location :" + location2.getPath());

			URL location = IconManager.class.getResource(imageName); // imagename:16calendar.png

			return createBufferedImage(Toolkit.getDefaultToolkit().getImage(location));
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static BufferedImage createBufferedImage(Image image) throws InterruptedException, IllegalArgumentException {
		loadImage(image);
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		ColorModel cm = getColorModel(image);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		BufferedImage bi = gc.createCompatibleImage(w, h, cm.getTransparency());
		Graphics2D g = bi.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return bi;
	}

	public static void loadImage(Image image) throws InterruptedException, IllegalArgumentException {
		Component dummy = new Component() {
		};
		MediaTracker tracker = new MediaTracker(dummy);
		tracker.addImage(image, 0);
		tracker.waitForID(0);
		if (tracker.isErrorID(0)) throw new IllegalArgumentException();
	}

	public static ColorModel getColorModel(Image image) throws InterruptedException, IllegalArgumentException {
		PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		if (!pg.grabPixels()) throw new IllegalArgumentException();
		return pg.getColorModel();
	}



}
