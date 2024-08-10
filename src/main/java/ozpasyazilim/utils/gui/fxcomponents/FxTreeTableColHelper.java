package ozpasyazilim.utils.gui.fxcomponents;

import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.table.FiCol;

import java.util.ArrayList;
import java.util.List;

public class FxTreeTableColHelper {

	List<FxTreeTableCol> fxTableColumnList;

	public void parseOzTableColList(List<FiCol> listFiCol){

		for (int colIndex = 0; colIndex < listFiCol.size(); colIndex++) {

			FiCol fiCol = listFiCol.get(colIndex);

			// Column geçerli değilse eklenmez
			if (fiCol.getBoEnabled()!=null && !fiCol.getBoEnabled()) continue;

			FxTreeTableCol fxTableColumn = new FxTreeTableCol();
			fxTableColumn.setRefFiCol(fiCol);
			//fxTableColumn.setHeader(fiCol.getHeaderName());
			//fxTableColumn.setFieldName(fiCol.getFieldName());
			fxTableColumn.setId(fiCol.getOfcTxFieldName());
			if(fiCol.getColType()==null) fiCol.setColType(OzColType.String);
			//fxTableColumn.setColType(fiCol.getColType().toString());
			if(fiCol.getPrefSize()!=null){
				fxTableColumn.setPrefWidth(fiCol.getPrefSize());
			}
			if(fiCol.getBoEditable()!=null && fiCol.getBoEditable()){
				fxTableColumn.setEditable(true);
				fxTableColumn.setAutoEditor();
			}

			fxTableColumn.setAutoColumnDefaultByFiCol();

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
