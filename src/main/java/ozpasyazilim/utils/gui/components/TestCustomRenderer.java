package ozpasyazilim.utils.gui.components;

import java.awt.*;
import javax.swing.*;

class TestCustomRenderer extends JPanel {
	static Color oldColor;// <--------------
	JComboBox comboBox;

	public TestCustomRenderer() {
		super(new BorderLayout());
		this.setPreferredSize(new Dimension(200, 100));
		this.add(get_comboBox(), BorderLayout.SOUTH);
		this.setBackground(new Color(153, 204, 255));
	}

	private JComboBox get_comboBox() {
		comboBox = new JComboBox();
		comboBox.addItem("Planning");
		comboBox.addItem("Testing");
		ComboBoxRenderer renderer = new ComboBoxRenderer();
		comboBox.setPreferredSize(new Dimension(80, 25));
		comboBox.setRenderer(renderer);
		comboBox.setBackground(new Color(153, 204, 255));// <---for the button
		return comboBox;
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("TestCustomRenderer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JComponent newContentPane = new TestCustomRenderer();
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				oldColor = UIManager.getColor("ComboBox.selectionBackground");// <---------------
				UIManager.put("ComboBox.selectionBackground", new javax.swing.plaf.ColorUIResource(new Color(153, 204,
					255)));// <-----
				createAndShowGUI();
			}
		});
	}

	class ComboBoxRenderer extends javax.swing.plaf.basic.BasicComboBoxRenderer {
		public ComboBoxRenderer() {
			super();
			setOpaque(true);
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
			setText(value.toString());
			if (isSelected) setBackground(oldColor);
			else
				setBackground(new Color(153, 204, 255));
			return this;
		}
	}
}