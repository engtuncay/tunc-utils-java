package ozpasyazilim.utils.gui.components;

import ozpasyazilim.utils.core.FiException;
import ozpasyazilim.utils.log.Loghelper;

import javax.swing.*;
import java.awt.*;

/**
 * JFrame lerin kalıtım alacağı sınıf
 *
 * @author TUNC
 */

public class OzFrame extends JFrame {

	private String txtid;

	public String getTxtid() {
		return txtid;
	}

	public void setTxtid(String txtid) {
		this.txtid = txtid;
	}

	// quick frame controller için oluşturuldu.
	static int intx = 170;
	static int inty = 160;

	public static void startQuickFrameByController(OzControllerPro ozcontrollerpro) {

		SwingUtilities.invokeLater(() -> {

			//setupLookAndFeel();
			OzFrame ozframe = new OzFrame();

			//ozcontrollerpro.setupView();
			ozframe.setContentPane(ozcontrollerpro.getPanel());
			ozframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			//ozframe.setSize(300, 200);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Double dimx = screenSize.width * (0.90);
			Double dimy = screenSize.height * (0.60);
			ozframe.setBounds(intx, inty, dimx.intValue(), dimy.intValue());
			OzFrame.intx += 20;
			//inty += 20;
			if (intx > 180) {
				OzFrame.intx = 120;
				OzFrame.inty = 120;
			}

			//ozframe.pack();
			ozframe.setVisible(true);
			//ozframe.setAlwaysOnTop(true);
			ozframe.toFront();
			ozframe.repaint();
			//ozframe.setAlwaysOnTop(false);
		});


	}

	// Javafx de açılan unmodal Dialoglar
	public static OzJDialog openDialogUnmodal(OzController controller, Double percentage) {

		OzJDialog dialog = new OzJDialog();
		//JWindow ddd = new JWindow();


		SwingUtilities.invokeLater(() -> {

			dialog.setContentPane(controller.getOzPanel());
			dialog.setTitle(controller.getModuleLabel());

//  dilog ekran ortalanmak istenirseekrana ortalanması

//			final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//			final int x = (screenSize.width - dialog.getWidth()) / 2;
//			final int y = (screenSize.height - dialog.getHeight()) / 2;

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Double dimx = screenSize.width - Double.valueOf(intx);  //* percentage / 100;
			Double dimy = screenSize.height - (Double.valueOf(inty+40)); //70d; //* percentage / 100 - 20;

			dialog.setBounds(intx, inty, dimx.intValue(), dimy.intValue());
			//dialog.setSize(width, height);
			//dialog.setLocation(x, y);
			//dialog.pack();
			dialog.setModal(false);
			dialog.setVisible(true);

			dialog.toFront();
			dialog.repaint();
			//dialog.toBack();

//			intx += 20;
//			//inty += 20;
//			if (intx > 180) {intx = 120;inty = 120;}

		});

		return dialog;


	}

	//static JFrame hiddenFrmae = new JFrame();

	public static OzInternalFrame openIframe(OzController controller, Double percentage) {

		OzInternalFrame dialog = new OzInternalFrame();

		//JWindow ddd = new JWindow();


		SwingUtilities.invokeLater(() -> {

			dialog.setContentPane(controller.getOzPanel());
			dialog.setTitle(controller.getModuleLabel());

// dilog ekran ortalanmak istenirseekrana ortalanması
//			final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//			final int x = (screenSize.width - dialog.getWidth()) / 2;
//			final int y = (screenSize.height - dialog.getHeight()) / 2;

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Double dimx = screenSize.width - Double.valueOf(intx);  //* percentage / 100;
			Double dimy = screenSize.height - (Double.valueOf(inty+40)); //70d; //* percentage / 100 - 20;

			dialog.setBounds(intx, inty, dimx.intValue(), dimy.intValue());
			//dialog.setSize(width, height);
			//dialog.setLocation(x, y);
			//dialog.pack();
			//dialog.setModal(false);
			dialog.setVisible(true);

			dialog.toFront();
			dialog.repaint();
			//dialog.toBack();

//			intx += 20;
//			//inty += 20;
//			if (intx > 180) {intx = 120;inty = 120;}

		});

		return dialog;


	}

	public static void setupLookAndFeel() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			System.out.println("Exception:" + ex.getStackTrace());
			Loghelper.get(OzFrame.class).debug(FiException.exToLog(ex));
		}

		//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (ClassNotFoundException ex) {
//            // Logger.getLogger(ViewfrmMain2.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            // Logger.getLogger(ViewfrmMain2.class.getName()).log(Level.SEVERE,null, ex);
//        } catch (IllegalAccessException ex) {
//            // Logger.getLogger(ViewfrmMain2.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (UnsupportedLookAndFeelException ex) {
//            // Logger.getLogger(ViewfrmMain2.class.getName()).log(Level.SEVERE,null, ex);
//        } catch (Exception e) {
//            Loghelper.logexceptionOnlyMail(FiException.exceptionStackTraceStringFull(e));
//        }


	}

	public static OzFrame quickFrameByPanel(OzPanel ozPanel) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			System.out.println("Exception:" + ex.getStackTrace());
		}

		//SwingUtilities.invokeLater(() -> {

		OzFrame ozframe = new OzFrame();
		ozframe.getContentPane().add(ozPanel);
		ozframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//frame.setSize(300, 200);
		//ozframe.pack();
		//ozframe.setVisible(true);

		return ozframe;

		//});

	}

	public void startFrame() {

		SwingUtilities.invokeLater(() -> {

			this.pack();
			this.setVisible(true);

		});

		//return this;

	}

	public void runRunnable(Runnable runnable) {
		runnable.run();
	}


}
