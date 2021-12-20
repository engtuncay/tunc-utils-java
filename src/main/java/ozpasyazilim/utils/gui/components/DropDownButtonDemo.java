package ozpasyazilim.utils.gui.components;

import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.openide.awt.DropDownButtonFactory;

/**
 * This Swing program demonstrates how to create a drop down button.
 * @author www.codejava.net
 *
 */
public class DropDownButtonDemo extends JFrame implements ActionListener {

	public DropDownButtonDemo() throws HeadlessException {
		super("CodeJava - Drop Down Button Demo");
		
		createMenuBar();
		
		createToolbar();
		
		setSize(360, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void createToolbar() {
		JToolBar toolbar = new JToolBar();
		JButton buttonOpen = new JButton(new ImageIcon(
				getClass().getResource("/images/open.png")));
		toolbar.add(buttonOpen);
		toolbar.add(new JSeparator());
		
		JButton buttonSave = new JButton(new ImageIcon(
				getClass().getResource("/images/save.png")));
		toolbar.add(buttonSave);
		toolbar.add(new JSeparator());
		
		JToggleButton dropDownButton = createDropDownButton();
		toolbar.add(dropDownButton);
		toolbar.add(new JSeparator());

		JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem menuItemCreateSpringProject = new JMenuItem("Spring Project");
		popupMenu.add(menuItemCreateSpringProject);

		JMenuItem menuItemCreateHibernateProject = new JMenuItem("Hibernate Project");
		popupMenu.add(menuItemCreateHibernateProject);

		JMenuItem menuItemCreateStrutsProject = new JMenuItem("Struts Project");
		popupMenu.add(menuItemCreateStrutsProject);

		//		JSplitButton splitButton = new JSplitButton("Split Button Demo");
		//		splitButton.setPopupMenu(popupMenu);
		//		toolbar.add(splitButton);

		JButton btnDemo = new JButton("Deneme");
		//btnDemo.add(popupMenu);
		toolbar.add(btnDemo);
		
		JButton buttonRun = new JButton(new ImageIcon(
				getClass().getResource("/images/run.png")));
		toolbar.add(buttonRun);
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(toolbar);
	}

	private JToggleButton createDropDownButton() {
		JPopupMenu popupMenu = createDropDownMenu();
		
		ImageIcon icon = new ImageIcon(getClass().getResource("/images/new.gif"));

		
		JToggleButton dropDownButton = DropDownButtonFactory.createDropDownToggleButton(icon, popupMenu);
		dropDownButton.setText("Excel İşlemler");
		//dropDownButton.setIcon(null);
		//dropDownButton.addActionListener(this);
		
		return dropDownButton;
	}

	private JPopupMenu createDropDownMenu() {

		JPopupMenu popupMenu = new JPopupMenu();
		
		JMenuItem menuItemCreateSpringProject = new JMenuItem("Spring Project");
		popupMenu.add(menuItemCreateSpringProject);
		
		JMenuItem menuItemCreateHibernateProject = new JMenuItem("Hibernate Project");
		popupMenu.add(menuItemCreateHibernateProject);
		
		JMenuItem menuItemCreateStrutsProject = new JMenuItem("Struts Project");
		popupMenu.add(menuItemCreateStrutsProject);
		
		menuItemCreateSpringProject.addActionListener(this);
		menuItemCreateHibernateProject.addActionListener(this);
		menuItemCreateStrutsProject.addActionListener(this);
		
		return popupMenu;
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menuFile = new JMenu("File");
		JMenuItem menuItemExit = new JMenuItem("Exit");
		
		menuFile.add(menuItemExit);
		
		menuBar.add(menuFile);
	}

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new DropDownButtonDemo().setVisible(true);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (source instanceof JMenuItem) {
			JMenuItem clickedMenuItem = (JMenuItem) source;
			String menuText = clickedMenuItem.getText();
			System.out.println("Create a " + menuText);
		} else if (source instanceof JButton) {
			System.out.println("Create a default project");
		}
	}

}
