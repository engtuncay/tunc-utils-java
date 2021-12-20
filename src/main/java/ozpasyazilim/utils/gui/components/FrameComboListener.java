package ozpasyazilim.utils.gui.components;

import java.awt.EventQueue;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Color;

@SuppressWarnings("serial")
public class FrameComboListener extends JFrame {

	private JPanel contentPane;
	@SuppressWarnings("rawtypes")
	private JComboBox cbPesawat;
	private JLabel lblNamaPesawat;
	String pesawat[] = { "Garuda", "Lion Air", "Lufthansa Air", "Batavia Air", "Bali Air" };
	String dataPesawat[][] = {
		{ "Jakarta - Bali", "15.00", "17.30", "Rp. 250.000" },
		{ "Jakarta - Yogyakarta", "08.00", "09.00", "Rp. 150.000" },
		{ "Jakarta - Singapura", "13.00", "15.45", "Rp. 500.000" } };
	@SuppressWarnings("rawtypes")
	Vector vectorPesawat = new Vector();
	private JLabel lblRute;
	private JLabel lblJamBerangkat;
	private JLabel lblJamTiba;
	private JLabel lblHarga;
	private JButton btnRefresh;
	private JButton btnTampilkan;
	private JLabel lblIsiRute;
	private JLabel lblIsiBerangkat;
	private JLabel lblIsiTiba;
	private JLabel lblIsiHarga;
	private JLabel lblBg;
	private JLabel lblIco;

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FrameComboListener() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 621, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblNamaPesawat = new JLabel("Nama Pesawat : ");
		lblNamaPesawat.setForeground(new Color(255, 10, 255));
		lblNamaPesawat.setBounds(12, 60, 127, 15);
		contentPane.add(lblNamaPesawat);

		cbPesawat = new JComboBox();
		cbPesawat.setModel(new DefaultComboBoxModel(vectorPesawat));
		cbPesawat.setSelectedIndex(-1);
		cbPesawat.setEditable(true);

		JTextField text = (JTextField) cbPesawat.getEditor().getEditorComponent();
		text.setFocusable(true);
		text.setText("");
		text.addKeyListener(new ComboListener(cbPesawat, vectorPesawat));
		cbPesawat.setBounds(144, 56, 165, 24);
		contentPane.add(cbPesawat);

		// btnTampilkan.setIcon(new ImageIcon("/home/resa/Aplikasi Java/SwingComponents/src/Gambar/tampilData.png"));
		// btnTampilkan.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent act) {
		// if (cbPesawat.getSelectedItem().equals("Batavia Air")) {
		// lblIsiRute.setText(dataPesawat[0][0]);
		// lblIsiBerangkat.setText(dataPesawat[0][1]);
		// lblIsiTiba.setText(dataPesawat[0][2]);
		// lblIsiHarga.setText(dataPesawat[0][3]);
		// } else if (cbPesawat.getSelectedItem().equals("Bali Air")) {
		// lblIsiRute.setText(dataPesawat[0][0]);
		// lblIsiBerangkat.setText(dataPesawat[0][1]);
		// lblIsiTiba.setText(dataPesawat[0][2]);
		// lblIsiHarga.setText(dataPesawat[2][3]);
		// } else if (cbPesawat.getSelectedItem().equals("Lufthansa Air")) {
		// cbPesawat.requestFocus();
		// }
		// });
		// btnRefresh.setIcon(new ImageIcon("/home/resa/Aplikasi Java/SwingComponents/src/Gambar/Refresh.png"));
		// btnRefresh.setBounds(222, 150, 127, 43);
		// contentPane.add(btnRefresh);


		setLocationRelativeTo(null);
		setVectorPesawat();
	}

	@SuppressWarnings("unchecked")
	public void setVectorPesawat() {
		int a;
		for (a = 0; a < pesawat.length; a++) {
			vectorPesawat.add(pesawat[a]);
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameComboListener frame = new FrameComboListener();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				/*
				 * try
				 * {
				 * com.jtattoo.plaf.mcwin.McWinLookAndFeel.setTheme("Large-Font", "Java Swing", "");
				 * UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
				 * FrameComboListener frame = new FrameComboListener();
				 * frame.setVisible(true);
				 * }
				 * catch (Exception e)
				 * {
				 * e.printStackTrace();
				 * }
				 */
			}
		});
	}
}
