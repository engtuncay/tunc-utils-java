package ozpasyazilim.utils.gui.components;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import ozpasyazilim.utils.gui.utils.UIUtil;




public class DProgressDialog extends JDialog {
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JLabel jLabel1 = new JLabel();
	private JProgressBar jProgressBar1 = new JProgressBar();
	private JLabel jLabel2 = new JLabel();
	private JLabel labelElapsedTime = new JLabel();
	private JLabel jLabel4 = new JLabel();
	private boolean progressRunning = true;
	private JButton buttonOK = new JButton();
	private JLabel labelStatusMessage = new JLabel();
	private boolean closeEndOfProgress = true;
	private JPanel jPanel1 = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	public DProgressDialog(Dialog parent) {
		super(parent, "Lütfen bekleyin", true);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DProgressDialog(Frame parent) {
		super(parent, "Lütfen bekleyin", true);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void runProgress(Dialog parent, DProgress progress) {
		runProgressMain(new DProgressDialog(parent), progress);
	}

	public static void runProgress(Frame parent, DProgress progress) {
		runProgressMain(new DProgressDialog(parent), progress);
	}

	private static void runProgressMain(final DProgressDialog dialog, final DProgress progress) {
		final long startTime = System.currentTimeMillis();
		Thread repainterThread = new Thread(new Runnable() {
			public void run() {
				while (dialog.progressRunning) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					long elapsedTime = System.currentTimeMillis() - startTime;
					long second = (elapsedTime / 1000) % 60;
					long minute = (elapsedTime / 60000);
					String minuteText = minute < 10 ? "0" + minute : String.valueOf(minute);
					String secondText = second < 10 ? "0" + second : String.valueOf(second);
					synchronized (dialog) {
						dialog.labelElapsedTime.setText(minuteText + ":" + secondText);
					}
				}
			}
		});
		Thread progressThread = new Thread(new Runnable() {
			public void run() {
				try {
					progress.progress();
				} catch (Throwable e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(dialog, "Hata olu?tu\n" + e.getMessage(), "Hata",
						JOptionPane.ERROR_MESSAGE);
				} finally {
					synchronized (dialog) {
						if (dialog.closeEndOfProgress) {
							dialog.setVisible(false);
							dialog.progressRunning = false;
							dialog.jProgressBar1.setIndeterminate(false);
							dialog.jProgressBar1.setValue(dialog.jProgressBar1.getMaximum());
							dialog.jLabel1.setText("��lem sonland?.");
						} else {
							dialog.buttonOK.setVisible(true);
							dialog.buttonOK.requestFocus();
							dialog.progressRunning = false;
							dialog.jProgressBar1.setIndeterminate(false);
							dialog.jProgressBar1.setValue(100);
							dialog.jLabel1.setText("��lem sonland?.");
						}
					}
				}
			}
		});
		progress.setProgressDialog(dialog);
		progress.preProgress();
		repainterThread.start();
		progressThread.start();
		UIUtil.showAtCenter(dialog);
		dialog.setVisible(true);
		progress.postProgress();
		dialog.progressRunning = false;
	}

	public DProgressDialog() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(gridBagLayout1);
		this.setSize(new Dimension(466, 199));
		jLabel1.setText("İşleminiz yapılıyor...");
		jLabel1.setBackground(Color.white);
		jLabel1.setOpaque(true);
		jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel1.setFont(new Font("Tahoma", 0, 15));
		jLabel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		jLabel1.setSize(new Dimension(553, 23));
		jProgressBar1.setIndeterminate(true);
		jProgressBar1.setToolTipText("null");
		jLabel2.setText("L�tfen Bekleyin...");
		labelElapsedTime.setText("00:00");
		jLabel4.setText("Ge�en S�re");
		buttonOK.setText("Tamam");
		// buttonOK.setIcon(ImageHandler.getImageIcon("kaydet.gif"));
		buttonOK.setVisible(false);
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonOK_actionPerformed(e);
			}
		});
		labelStatusMessage.setText("Status message goes here...");
		labelStatusMessage.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		labelStatusMessage.setOpaque(true);
		labelStatusMessage.setHorizontalTextPosition(SwingConstants.CENTER);
		labelStatusMessage.setHorizontalAlignment(SwingConstants.CENTER);
		jPanel1.setLayout(gridBagLayout2);
		jPanel1.setMinimumSize(new Dimension(0, 35));
		jPanel1.setPreferredSize(new Dimension(0, 35));
		this.getContentPane().add(jLabel1, new GridBagConstraints(0, 0, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
			GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 20));
		this.getContentPane().add(jProgressBar1, new GridBagConstraints(0, 3, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
			GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		this.getContentPane().add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
			GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
		this.getContentPane().add(labelElapsedTime, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
		this.getContentPane().add(jLabel4, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
			GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
		this.getContentPane().add(labelStatusMessage, new GridBagConstraints(0, 2, 3, 1, 1.0, 1.0,
			GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0));
		jPanel1.add(buttonOK, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
			GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.getContentPane().add(jPanel1, new GridBagConstraints(0, 4, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
			GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		setMessageMode(DProgress.MESSAGE_MODE_NORMAL);
	}

	public void setMessage(String message) {
		labelStatusMessage.setText(message);
	}

	public void setMessageMode(int mode) {
		switch (mode) {
		case DProgress.MESSAGE_MODE_NORMAL:
			labelStatusMessage.setForeground(DProgress.NORMAL_FOREGROUND);
			labelStatusMessage.setBackground(DProgress.NORMAL_BACKROUND);
			break;
		case DProgress.MESSAGE_MODE_ERROR:
			labelStatusMessage.setForeground(DProgress.ERROR_FOREGROUND);
			labelStatusMessage.setBackground(DProgress.ERROR_BACKROUND);
			break;
		case DProgress.MESSAGE_MODE_SUCCES:
			labelStatusMessage.setForeground(DProgress.SUCCES_FOREGROUND);
			labelStatusMessage.setBackground(DProgress.SUCCES_BACKROUND);
			break;
		}
	}

	public void setErrorMessage(String message) {
		setCloseOnEndOfProgress(false);
		setMessage(message);
		setMessageMode(DProgress.MESSAGE_MODE_ERROR);
	}

	public void setSuccessMessage(String message) {
		setCloseOnEndOfProgress(false);
		setMessage(message);
		setMessageMode(DProgress.MESSAGE_MODE_SUCCES);
	}

	public void setCloseOnEndOfProgress(boolean b) {
		closeEndOfProgress = b;
	}

	private void buttonOK_actionPerformed(ActionEvent e) {
		setVisible(false);
	}

	public void setProgreesBarIndeterminate(boolean bool) {
		jProgressBar1.setIndeterminate(bool);
	}

	public void setProgressBarValue(int value) {
		jProgressBar1.setValue(value);
	}

	public void setMaxProgressBarValue(int value) {
		jProgressBar1.setMaximum(value);
	}

	public void setMinProgressBarValue(int value) {
		jProgressBar1.setMinimum(value);
	}
}
