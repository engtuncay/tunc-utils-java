package ozpasyazilim.utils.gui.components;

import java.awt.Dimension;

import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;

import javax.swing.JToolBar;

import ozpasyazilim.utils.gui.utils.DViewable;

public class DInternalFrame extends JInternalFrame implements DViewable {
	public DInternalFrame() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void jbInit() throws Exception {
		this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.setSize(new Dimension(462, 355));
		((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).setNorthPane(null);
		this.setTitle("Hurda ��letmesi M�d�rl��� Uygulamas�");
	}

	public void view(Properties params) {
	}

	public JToolBar getContextToolbar() {
		return null;
	}

	public String getScreenName() {
		return "Hurda-000";
	}

	public void setStatusMessage(String pMessage) {

	}

	public String getStatusMessage() {
		return null;
	}

}
