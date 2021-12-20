package ozpasyazilim.utils.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import ozpasyazilim.utils.gui.utils.UIUtil;

public class DInputDialog extends JDialog {
	private JScrollPane jScrollPane1 = new JScrollPane();
	private JTextPane textAreaMessage = new JTextPane();
	private JLabel labelTitle = new JLabel();
	private JLabel jLabel2 = new JLabel();
	private JButton buttonCancel = new JButton();
	private JButton buttonOk = new JButton();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private int minWidth = 0;
	private String result = null;
	private JLabel labelError = new JLabel();

	public DInputDialog() {
		this(null, "", false);
	}

	public DInputDialog(Frame parent, String title, boolean modal) {
		super(parent, title, modal);
		try {
			jbInit();
		} catch (Exception e) {
			// Loghelper.logexceptionOnlyMail(UtilModel.exceptionStackTraceStringFull(e));
			e.printStackTrace();
		}
	}

	public DInputDialog(Frame parent, String title) {
		super(parent, title, true);
		try {
			jbInit();
		} catch (Exception e) {
			// Loghelper.logexceptionOnlyMail(UtilModel.exceptionStackTraceStringFull(e));
			e.printStackTrace();
		}
	}

	public static String showInputPane(Frame parent, String title, int minWidth) {
		DInputDialog dialog = new DInputDialog(parent, "A�iklama");
		dialog.minWidth = minWidth;
		dialog.labelTitle.setText("<html>" + title + "</html>");
		UIUtil.showAtCenter(dialog);
		dialog.setVisible(true);
		return dialog.result;
	}

	public static String showInputPane(Frame parent, String title) {
		DInputDialog dialog = new DInputDialog(parent, "A�iklama");
		dialog.labelTitle.setText("<html>" + title + "</html>");
		UIUtil.showAtCenter(dialog);
		dialog.setVisible(true);
		return dialog.result;
	}

	private void jbInit() throws Exception {
		this.setSize(new Dimension(414, 288));
		this.getContentPane().setLayout(gridBagLayout1);
		this.setTitle("A��klama");
		textAreaMessage.setFont(new Font("Times New Roman", 1, 14));
		labelTitle.setText("  ");
		labelTitle.setFont(new Font("Tahoma", 1, 12));
		labelTitle.setForeground(new Color(189, 0, 0));
		jLabel2.setText("   ");
		buttonCancel.setText("�ptal");
		buttonCancel.setPreferredSize(new Dimension(80, 22));
		buttonCancel.setMinimumSize(new Dimension(80, 22));
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonCancel_actionPerformed(e);
			}
		});
		buttonOk.setText("Tamam");
		buttonOk.setMinimumSize(new Dimension(80, 22));
		buttonOk.setPreferredSize(new Dimension(80, 22));
		buttonOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonOk_actionPerformed(e);
			}
		});
		labelError.setVisible(false);
		labelError.setText("UYARI!!!        Girdi�iniz a��klama �ok k�sa.");
		labelError.setFont(new Font("Tahoma", 1, 14));
		labelError.setForeground(Color.red);
		jScrollPane1.getViewport().add(textAreaMessage, null);
		this.getContentPane().add(buttonOk, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
			GridBagConstraints.NONE, new Insets(5, 10, 10, 15), 0, 0));
		this.getContentPane().add(buttonCancel, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
			GridBagConstraints.NONE, new Insets(5, 5, 10, 10), 0, 0));
		this.getContentPane().add(jLabel2, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
			GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(labelTitle, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
			GridBagConstraints.BOTH, new Insets(10, 12, 5, 12), 0, 0));
		this.getContentPane().add(jScrollPane1, new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER,
			GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
		this.getContentPane().add(labelError, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
			GridBagConstraints.BOTH, new Insets(5, 15, 5, 15), 0, 0));
	}

	private void buttonCancel_actionPerformed(ActionEvent e) {
		result = null;
		this.dispose();
	}

	private void buttonOk_actionPerformed(ActionEvent e) {
		if (textAreaMessage.getText().length() > minWidth) {
			result = textAreaMessage.getText();
			this.dispose();
		} else {
			labelError.setVisible(true);
		}
	}
}
