package ozpasyazilim.utils.table;

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
                if (fiCol.getFieldName().equals(fieldName)) {
                    return fiCol;
                }
            }
        }
        return null;
    }

    public Date getColValueAsDate(Object objectFieldName) {
        FiCol fiCol = findColumnByFieldName(objectFieldName.toString());

        if (fiCol.getColValue() instanceof Date){
            return (Date)fiCol.getColValue();
        }

        return null;
    }


}
