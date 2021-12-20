package ozpasyazilim.utils.gui.fxcomponents;

import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.table.FiCol;

import java.util.ArrayList;
import java.util.List;

public class FxTreeTableColHelper {

	List<FxTreeTableCol> fxTableColumnList;

	public void parseOzTableColList(List<FiCol> listFiCol){

		for (int colIndex = 0; colIndex < listFiCol.size(); colIndex++) {

			FiCol fiTableCol = listFiCol.get(colIndex);

			// Column geçerli değilse eklenmez
			if (fiTableCol.getBoEnabled()!=null && !fiTableCol.getBoEnabled()) continue;

			FxTreeTableCol fxTableColumn = new FxTreeTableCol();
			fxTableColumn.setHeader(fiTableCol.getHeaderName());
			fxTableColumn.setFieldName(fiTableCol.getFieldName());
			fxTableColumn.setId(fiTableCol.getFieldName());
			if(fiTableCol.getColType()==null) fiTableCol.setColType(OzColType.String);
			fxTableColumn.setColType(fiTableCol.getColType().toString());
			if(fiTableCol.getPrefSize()!=null){
				fxTableColumn.setPrefWidth(fiTableCol.getPrefSize());
			}
			if(fiTableCol.getBoEditable()!=null && fiTableCol.getBoEditable()){
				fxTableColumn.setEditable(true);
				fxTableColumn.setAutoEditor();
			}

			fxTableColumn.setAutoColumnDefault();

			//Loghelperr.getInstance(getClass()).debug(" Fx TableView col id:"+fxTableColumn.getId());
			getFxTableColumnList().add(fxTableColumn);

		}



	}

	public void clearAndParseOzTableColList(List<FiCol> listFiCol){

		setFxTableColumnList(new ArrayList<>());
		parseOzTableColList(listFiCol);

	}

	public List<FxTreeTableCol> getFxTableColumnList() {
		if (fxTableColumnList == null) {
			fxTableColumnList=new ArrayList<>();
		}
		return fxTableColumnList;
	}

	public void setFxTableColumnList(List<FxTreeTableCol> fxTableColumnList) {
		this.fxTableColumnList = fxTableColumnList;
	}
}
