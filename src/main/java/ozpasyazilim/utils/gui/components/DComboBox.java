package ozpasyazilim.utils.gui.components;

import javax.swing.JComboBox;

public class DComboBox extends JComboBox{
    public DComboBox() {
    }
    public void addItems(Object items[])
    {
        for(int index = 0; index < items.length; index++)
            addItem(items[index]);

    }

}
