package ozpasyazilim.utils.gui.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import javafx.application.Platform;
import org.jdesktop.swingx.JXPanel;
import ozpasyazilim.utils.gui.fxcomponents.FxDialogShow;
import ozpasyazilim.utils.gui.fxcomponents.FxPopNotification;

public class OzPanel extends JXPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5425443601230305172L;
	
	// panel hakkında dokumantasyon bilgi
	private String strpanelbilgi;
	//
	private String txtid;


	public OzPanel() {
		super();
		// ctrl + o basınca panel ile ilgili bilgi verir
		this.registerKeyboardAction(e -> {
			openPanelBilgidialog();
		}, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);

	}

	private void openPanelBilgidialog() {

		//JOptionPane.showMessageDialog(this, getStrpanelbilgi());

		Platform.runLater(() -> {
			new FxDialogShow().showModalInfo(getStrpanelbilgi());
		});
	}

	/**
	 * since every gui extends DPanel a unique messagePane variable is to be
	 * used for user interaction
	 */
	public final static DMessagePane getMessagePane(Component comp) {
		return new DMessagePane(JOptionPane.getFrameForComponent(comp));
	}

	public void closing() {

	}

	public void setviewNotification(String text) {
		Toaster toasterManager = new Toaster();
		toasterManager.showToaster(text);
	}

	public void setviewNotifyResul(Boolean result, String text) {

		String message = "";

		if (result) {
			message += " ++ İşlem Başarılı \n";
		} else {
			message += " !--! Sorun oluştu \n";
		}

		if (text != null) message += text;

		Toaster toasterManager = new Toaster();
		toasterManager.showToaster(message);

	}


	public void setviewNotificationModelresult(ReturnObjectAbs modelresult) {

//		Toaster toasterManager = new Toaster();
//		if (modelresult.getResult()) {
//			toasterManager.showToaster("işlem başarılı");
//		} else {
//			toasterManager.showToaster("Başarısız işlem. Hata:" + modelresult.getMessageSpecial());
//		}

		Platform.runLater(() -> {
			if (modelresult.getResult()) {
				new FxPopNotification().showPopNotification("İşlem Başarılı");
			} else {
				new FxPopNotification().showPopNotification("Hata Oluştu...\n"+ modelresult.getMessageSpecial());
			}
		});



	}

	public void showMessageDialog(String message) {
		//JOptionPane.showMessageDialog(null, message);
		Platform.runLater(() -> {
			new FxDialogShow().showModalInfo(message);
		});
	}

	// FIXME birleştirme yukarı ile
	public void showMessageDialogwarning(String message) {

		if (message == null) message = "null";
		Toolkit.getDefaultToolkit().beep();

		//		JOptionPane optionPane = new JOptionPane(message, JOptionPane.WARNING_MESSAGE);
		//		JDialog dialog = optionPane.createDialog("Uyarı!");
		//		dialog.setAlwaysOnTop(true);
		//		dialog.setVisible(true);

		String finalMessage = message;
		Platform.runLater(() -> {
			new FxDialogShow().showModalWarning(finalMessage);
		});
	}


	public void openDialog(OzPanel panel, Integer width, Integer height) {

		JDialog dialog = new JDialog();
		dialog.setContentPane(panel);
		dialog.setSize(width, height);
		// ekrana ortalanması
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - dialog.getWidth()) / 2;
		final int y = (screenSize.height - dialog.getHeight()) / 2;
		dialog.setLocation(x, y);
		// dialog.pack();
		dialog.setModal(false);
		dialog.setVisible(true);

	}

	public void openFrame(OzPanel panel, Integer width, Integer height) {

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setContentPane(panel);
		frame.setSize(width, height);
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - frame.getWidth()) / 2;
		final int y = (screenSize.height - frame.getHeight()) / 2;
		frame.setLocation(x, y);
		//frame.setBounds(0, 0, width, height);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		frame.setAlwaysOnTop(false);
	}

	public static void createFrame(OzPanel panel, Integer width, Integer height) {

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setContentPane(panel);
		frame.setSize(width, height);
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - frame.getWidth()) / 2;
		final int y = (screenSize.height - frame.getHeight()) / 2;
		frame.setLocation(x, y);
		//frame.setBounds(0, 0, width, height);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		frame.setAlwaysOnTop(false);
	}

	public String getTxtid() {
		return txtid;
	}

	public void setTxtid(String txtid) {
		this.txtid = txtid;
	}

	public String getStrpanelbilgi() {
		if (strpanelbilgi == null) {
			strpanelbilgi = "";
		}
		return strpanelbilgi;
	}

	public void setStrpanelbilgi(String strpanelbilgi) {
		this.strpanelbilgi = strpanelbilgi;
	}

	public void setStrpanelbilgi(Class objclass) {
		setStrpanelbilgi(objclass.getSimpleName());
	}

}
