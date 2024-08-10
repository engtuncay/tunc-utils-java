package ozpasyazilim.utils.table;

import ozpasyazilim.utils.gui.fxcomponents.FxDatePicker;

import java.util.Date;
import java.util.List;

public class FiColsUtil {

    List<FiCol> fiColList;

    public static FiColsUtil bui(List<FiCol> fiColList) {
        return new FiColsUtil(fiColList);
    }

    public FiColsUtil(List<FiCol> fiColList) {
        this.fiColList = fiColList;
    }

    // Getter and Setter

    public List<FiCol> getFiColList() {
        return fiColList;
    }

    public void setFiColList(List<FiCol> fiColList) {
        this.fiColList = fiColList;
    }

    // end - getter and setter

    public FiCol findColumnByFieldName(Object objFieldName) {
        return findColumnByFieldName(objFieldName.toString());
    }

    public FiCol findColumnByFieldName(String fieldName) {

        if (!getFiColList().isEmpty()) {

            for (FiCol fiCol : getFiColList()) {
                if (fiCol.getOfcTxFieldName().equals(fieldName)) {
                    return fiCol;
                }
            }
        }
        return null;
    }

    public Date getColValueAsDate(Object objectFieldName) {
        if (objectFieldName == null) return null;

        FiCol fiCol = findColumnByFieldName(objectFieldName.toString());

        if (fiCol.getColValue() instanceof Date) {
            return (Date) fiCol.getColValue();
        }

        return null;
    }

    public String getColValueAsString(Object objectFieldName) {
        if (objectFieldName == null) return null;

        FiCol fiCol = findColumnByFieldName(objectFieldName.toString());

        if (fiCol.getColValue() instanceof String) {
            return (String) fiCol.getColValue();
        }

        return null;
    }

    public FxDatePicker getEditorCompAsFxDatePicker(String fieldName) {

        FiCol fiCol = findColumnByFieldName(fieldName); //IFiColHelper.build(getListFormElements()).getIFiColByID(fieldName);

        if (fiCol.getColEditorClass().equals(FxDatePicker.class.getName())) {
            FxDatePicker comp = (FxDatePicker) fiCol.getColEditorNode();
            return comp;
        }

        return null;
    }

    public static FiCol changeFieldNameWithNew(FiCol fiCol){
        fiCol.setOfcTxFieldName(fiCol.getOfcTxFieldName()+ "_new");
        return fiCol;
    }


}
