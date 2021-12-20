package ozpasyazilim.utils.gui.components;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ImageButton extends JButton {

	Image image;
	ImageObserver imageObserver;

	public ImageButton(String filename) {
		super();
		ImageIcon icon = new ImageIcon(filename);
		image = icon.getImage();
		imageObserver = icon.getImageObserver();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(image, 0, 0, getWidth(), getHeight(), imageObserver);
	}
}