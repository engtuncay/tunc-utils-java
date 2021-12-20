package ozpasyazilim.utils.gui.components;

import ozpasyazilim.utils.gui.icons.IconManager;
import ozpasyazilim.utils.gui.utils.UIUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class DMessagePane extends JDialog {
	public static final int YES_ANSWER = 0;
	public static final int NO_ANSWER = 1;
	public static final int CANCEL_ANSWER = 2;
	private JLabel imageLabel = new JLabel();
	private JPanel jPanel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JButton buttonYes = new JButton();
	private JButton buttonNo = new JButton();
	private JButton buttonCancel = new JButton();
	private JButton buttonOk = new JButton();
	private int returnVal = 1;
	private static ImageIcon infoIcon = IOUtil.createImage("");
	private static ImageIcon confirmIcon = IOUtil.createImage("");
	private static ImageIcon errorIcon = IOUtil.createImage("");
	private JTextPane msgField = new JTextPane();
	private JButton buttonDetail = new JButton();
	private JScrollPane jScrollPane1 = new JScrollPane();
	private JTextPane labelDetail = new JTextPane();
	private Exception ex;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	public DMessagePane(Frame parent) {
		super(parent, true);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.setSize(new Dimension(385, 314));
		this.getContentPane().setLayout(gridBagLayout2);
		imageLabel.setMaximumSize(new Dimension(55, 55));
		imageLabel.setMinimumSize(new Dimension(55, 55));
		imageLabel.setPreferredSize(new Dimension(55, 55));
		jPanel1.setLayout(gridBagLayout1);
		buttonYes.setText("Evet");
		buttonYes.setIcon(IconManager.getSubmitImage());
		buttonYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				yesButton_actionPerformed(e);
			}
		});
		buttonNo.setText("Hayır");
		buttonNo.setIcon(IconManager.getRefuseImage());
		buttonNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				noButton_actionPerformed(e);
			}
		});
		buttonCancel.setText("İptal");
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonCancel_actionPerformed(e);
			}
		});
		buttonCancel.setIcon(IconManager.getRefuseImage());
		buttonOk.setText("Tamam");
		buttonOk.setIcon(IconManager.getSubmitImage());
		buttonOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton3_actionPerformed(e);
			}
		});
		msgField.setEditable(false);
		// msgField.setLineWrap(true);
		// msgField.setWrapStyleWord(true);
		msgField.setFont(new Font("Dialog", 1, 14));
		msgField.setMinimumSize(new Dimension(250, 100));
		msgField.setPreferredSize(new Dimension(250, 100));
		msgField.setMaximumSize(new Dimension(250, 500));

		labelDetail.setEditable(false);
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_LEFT);
		StyledDocument doc = (StyledDocument) msgField.getDocument();
		doc.setParagraphAttributes(0, doc.getLength() - 1, attrs, false);

		buttonDetail.setText("Detay");
		buttonDetail.setIcon(UIUtil.getIcon("16sagsag.png"));
		buttonDetail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonDetail_actionPerformed(e);
			}
		});
		jPanel1.add(buttonYes, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
			GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		jPanel1.add(buttonNo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
			GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		jPanel1.add(buttonCancel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
			GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		jPanel1.add(buttonOk, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
			GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		jPanel1.add(buttonDetail, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
			GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		jScrollPane1.getViewport().add(labelDetail, null);

		this.getContentPane().add(msgField, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
			GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		this.getContentPane().add(jPanel1, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
			GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

		this.getContentPane().add(imageLabel, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, GridBagConstraints.EAST,
			GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.setAlwaysOnTop(true);
		KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		kfm.addKeyEventPostProcessor(new KeyEventPostProcessor() {
			public boolean postProcessKeyEvent(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					enterKey_ActionPerformed();
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					enterKey_ActionPerformed();
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					enterKey_ActionPerformed();
				}
				return false;
			}
		});

	}

	private void enterKey_ActionPerformed() {
		this.setVisible(false);
	}

	private void yesButton_actionPerformed(ActionEvent e) {
		returnVal = YES_ANSWER;
		this.setVisible(false);
	}

	private void noButton_actionPerformed(ActionEvent e) {
		returnVal = NO_ANSWER;
		this.setVisible(false);
	}

	private void jButton3_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	private void jTextField1_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}


	public void showInfoPane(String msg) {
		this.setSize(new Dimension(350, 180));
		this.setTitle("Bilgi");
		msgField.setText("\n" + msg);

		buttonNo.setVisible(false);
		buttonYes.setVisible(false);
		buttonCancel.setVisible(false);
		buttonDetail.setVisible(false);
		jScrollPane1.setVisible(false);
		labelDetail.setVisible(false);
		buttonOk.setVisible(true);
		imageLabel.setIcon(UIUtil.getIcon("information.png"));
		this.setModal(true);
		this.setLocationRelativeTo(getRootPane());
		this.setVisible(true);
	}


	public int showConfirmPane(String msg) {
		this.setSize(new Dimension(400, 240));
		this.setTitle("Onay");
		this.setAlwaysOnTop(false);
		msgField.setText("\n" + msg);

		buttonNo.setVisible(true);
		buttonYes.setVisible(true);
		buttonCancel.setVisible(true);
		buttonDetail.setVisible(false);
		jScrollPane1.setVisible(false);
		labelDetail.setVisible(false);

		buttonOk.setVisible(false);
		imageLabel.setIcon(UIUtil.getIcon("question.png"));
		this.setModal(true);
		this.setLocationRelativeTo(getRootPane());

		this.setVisible(true);
		return returnVal;
	}

	public void showErrorPane(String msg, int width, int height) {
		this.setSize(new Dimension(width, height));
		this.setTitle("Hata");

		Color col2 = this.getContentPane().getBackground();
		Color col1 = msgField.getBackground();
		this.getContentPane().setBackground(Color.RED);
		msgField.setBackground(Color.red);
		msgField.setText("\n" + msg);
		buttonNo.setVisible(false);
		buttonYes.setVisible(false);
		buttonCancel.setVisible(false);
		buttonDetail.setVisible(false);
		jScrollPane1.setVisible(false);
		labelDetail.setVisible(false);

		buttonOk.setVisible(true);
		// File file = new File("C:\\project\\DataselComponents\\DslComponents\\src\\com\\datasel\\icon\\Error.png");
		// imageLabel.setIcon(fileToByteArray(file));
		imageLabel.setIcon(UIUtil.getIcon("Error.png"));
		this.setModal(true);
		this.setLocationRelativeTo(getRootPane());
		this.setVisible(true);
		this.getContentPane().setBackground(col2);
		msgField.setBackground(col1);
	}

	public void showErrorPane(String msg) {
		this.setSize(new Dimension(400, 250));
		this.setTitle("Hata");

		Color col2 = this.getContentPane().getBackground();
		Color col1 = msgField.getBackground();
		this.getContentPane().setBackground(Color.RED);
		msgField.setBackground(Color.red);
		msgField.setText("\n" + msg);
		buttonNo.setVisible(false);
		buttonYes.setVisible(false);
		buttonCancel.setVisible(false);
		buttonDetail.setVisible(false);
		jScrollPane1.setVisible(false);
		labelDetail.setVisible(false);

		buttonOk.setVisible(true);
		// File file = new File("C:\\project\\DataselComponents\\DslComponents\\src\\com\\datasel\\icon\\Error.png");
		// imageLabel.setIcon(fileToByteArray(file));
		imageLabel.setIcon(UIUtil.getIcon("Error.png"));
		this.setModal(true);
		this.setLocationRelativeTo(getRootPane());
		this.setVisible(true);
		this.getContentPane().setBackground(col2);
		msgField.setBackground(col1);
	}


	public int showYesNoConfirmPane(String msg) {
		this.setSize(new Dimension(350, 180));
		this.setTitle("Onay");
		msgField.setText("\n" + msg);

		buttonNo.setVisible(true);
		buttonYes.setVisible(true);
		buttonCancel.setVisible(false);
		buttonDetail.setVisible(false);
		jScrollPane1.setVisible(false);
		labelDetail.setVisible(false);

		buttonOk.setVisible(false);

		imageLabel.setIcon(UIUtil.getIcon("question.png"));
		this.setModal(true);
		this.setLocationRelativeTo(getRootPane());

		this.setVisible(true);
		return returnVal;
	}

	private ImageIcon fileToByteArray(File file) {
		byte[] data;
		try {
			FileInputStream fis = new FileInputStream(file);
			FileChannel fc = fis.getChannel();
			data = new byte[(int) fc.size()]; // fc.size returns the size of the file which backs the channel
			ByteBuffer bb = ByteBuffer.wrap(data);
			fc.read(bb);
			BufferedImage im;
			im = ImageIO.read(new ByteArrayInputStream(data));
			int w = im.getWidth();
			int h = im.getHeight();
			w = 120;
			h = 130;
			ImageIcon icon = new ImageIcon(im.getScaledInstance(w, h, Image.SCALE_SMOOTH));
			return icon;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void showErrorPaneWithDetail(String msg, Exception ex) {
		this.setSize(new Dimension(350, 210));
		this.setTitle("Hata");
		msgField.setText("\n" + msg);
		this.ex = ex;

		buttonNo.setVisible(false);
		buttonYes.setVisible(false);
		buttonCancel.setVisible(false);
		jScrollPane1.setVisible(true);
		labelDetail.setVisible(true);
		buttonDetail.setVisible(true);

		buttonOk.setVisible(true);
		// File file = new File("C:\\project\\DataselComponents\\DslComponents\\src\\com\\datasel\\icon\\Error.png");
		// imageLabel.setIcon(fileToByteArray(file));
		imageLabel.setIcon(UIUtil.getIcon("Error.png"));
		this.setModal(true);
		this.setLocationRelativeTo(getRootPane());

		this.setVisible(true);
		ex.printStackTrace();
	}

	private void buttonDetail_actionPerformed(ActionEvent e) {
		this.setSize(new Dimension(350, 350));
		this.getContentPane().add(jScrollPane1, new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
			GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		this.repaint();
		this.validate();
		jScrollPane1.setVisible(true);
		labelDetail.setVisible(true);
		StackTraceElement[] ste = ex.getStackTrace();
		String error = "";

		for (int i = 0; i < ste.length; i++) {
			error += ste[i].toString() + "\n";
		}

		labelDetail.setText(error);
	}

	private void buttonCancel_actionPerformed(ActionEvent e) {
		returnVal = NO_ANSWER;
		this.setVisible(false);
	}
}
