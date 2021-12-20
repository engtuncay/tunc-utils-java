package ozpasyazilim.utils.core;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class ExcelExporter {

    public ExcelExporter() {
    }

    public void exportTable(JTable table, File file) throws IOException {
        TableModel model = table.getModel();
        FileWriter out = new FileWriter(file);

        for (int i = 0; i < model.getColumnCount(); i++) {
            out.write(model.getColumnName(i) + ";");
        }
        out.write("\n");

        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                // I added this check for the ISBN conversion 
                if (j == 0) {
                    // the book Title 
                    if (model.getValueAt(i, j)==null)
                        out.write(";");
                    else
                        out.write("\""+model.getValueAt(i, j).toString() + "\";");
                } else {
                    if (model.getValueAt(i, j)==null)
                        out.write(";");
                    else
                        out.write("\"" + model.getValueAt(i, j).toString()  + 
                              "\";");
                }
            }
            out.write("\n");
        }
        out.close();
    }
}
