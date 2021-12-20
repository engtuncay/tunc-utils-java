package ozpasyazilim.utils.gui.components;

import java.awt.GridLayout;
import javax.swing.JPanel;
import java.awt.FlowLayout;

public class PanelMultiPurpose extends OzPanel {
	private JPanel panelHeader;
	private JPanel panelMain;
	private JPanel panelFooter;

	public PanelMultiPurpose() {
		createContents();
	}

	private void createContents() {

		setLayout(new GridLayout(3, 1, 0, 0));
		add(getPanelHeader());
		add(getPanelMain());
		add(getPanelFooter());

	}




	public JPanel getPanelHeader() {
		if (panelHeader == null) {
			panelHeader = new JPanel();
			panelHeader.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		}
		return panelHeader;
	}

	public JPanel getPanelMain() {
		if (panelMain == null) {
			panelMain = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelMain.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
		}
		return panelMain;
	}

	public JPanel getPanelFooter() {
		if (panelFooter == null) {
			panelFooter = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelFooter.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
		}
		return panelFooter;
	}
}
