package ozpasyazilim.utils.gui.components;


import java.awt.Color;
import java.awt.FlowLayout;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.util.ArrayList;

import javax.swing.BorderFactory;

public class AddDeleteTable extends OzPanel
{
  private AddDeleteTableModel tableModel;
  private DTable table;
  
  private OzPanel buttonPanel;
  
  private DButton addButton;
  private DButton deleteButton;
  
  
  public AddDeleteTable(String title)
  {
    this.setBorder(BorderFactory.createTitledBorder(title));
    
    buttonPanel=new OzPanel();
    buttonPanel.setLayout(new FlowLayout());
    
    addButton=new DButton();
    addButton.setText("+");
    deleteButton=new DButton();
    deleteButton.setText("X");
    deleteButton.setBackground(Color.RED);
    
    
    
    buttonPanel.add(addButton);
    buttonPanel.add(deleteButton);
    
    this.setLayout(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints();
    c.anchor=GridBagConstraints.FIRST_LINE_START;
    this.add(buttonPanel,c);
    
    table=new DTable();
    DScrollPane tablePane=new DScrollPane(table);
    tablePane.setHorizontalScrollBarPolicy(DScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    tablePane.setVerticalScrollBarPolicy(DScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    
    c.gridy=1;
    c.anchor=GridBagConstraints.CENTER;
    this.add(tablePane,c);
    
    
    Object[] cols={"A","B","C"};
    tableModel=new AddDeleteTableModel(cols,new ArrayList());
    table.setModel(tableModel);
  }
  
  
  public static void createGUI()
  {
    DFrame frame=new DFrame();
    
    AddDeleteTable dene= new AddDeleteTable("deneme");
    
    frame.setContentPane(dene);
    frame.pack();
    frame.setVisible(true);
  }
  
  public static void main(String[] args)
  {
  
    javax.swing.SwingUtilities.invokeLater(new Runnable() 
    {
                public void run() {
                    createGUI();
                }
            });
    System.out.println("hello"); 
  }
}
