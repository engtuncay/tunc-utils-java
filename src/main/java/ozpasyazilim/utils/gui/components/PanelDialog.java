package ozpasyazilim.utils.gui.components;

import javax.swing.JLabel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Color;

public class PanelDialog extends OzPanel {
	private JLabel lblNewLabel;
	private JButton btnYes;
	private JPanel panel;
	private JButton btnNo;
	private JButton btnCancel;

	public PanelDialog() {
		createContents();
	}

	private void createContents() {
		setLayout(new FormLayout(new ColumnSpec[] {
			FormSpecs.RELATED_GAP_COLSPEC,
			ColumnSpec.decode("max(31dlu;default):grow"), }, new RowSpec[] {
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("14px:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC, }));
		setSize(400, 200);
		add(getLblNewLabel(), "2, 2, left, top");
		add(getPanel(), "2, 4, fill, fill");
	}



	public JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel(".");
			lblNewLabel.setBackground(new Color(204, 255, 255));
		}
		return lblNewLabel;
	}

	public JButton getBtnYes() {
		if (btnYes == null) {
			btnYes = new JButton("Evet");
			btnYes.setBackground(new Color(153, 204, 204));
		}
		return btnYes;
	}

	public JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panel.add(getBtnYes());
			panel.add(getBtnNo());
			panel.add(getBtnCancel());
		}
		return panel;
	}

	public JButton getBtnNo() {
		if (btnNo == null) {
			btnNo = new JButton("Hayır");
			btnNo.setBackground(new Color(255, 102, 102));
		}
		return btnNo;
	}

	public JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton("İptal");
		}
		return btnCancel;
	}
}
