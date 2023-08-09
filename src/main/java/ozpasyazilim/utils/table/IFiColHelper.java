package ozpasyazilim.utils.table;

import ozpasyazilim.utils.core.FiBoolean;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.fidborm.FiEntity;
import ozpasyazilim.utils.fidborm.FiField;
import ozpasyazilim.utils.gui.fxcomponents.FxTreeTableCol;
import ozpasyazilim.utils.mvc.IFiCol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IFiColHelper {

	List<? extends IFiCol> listFiColInf;
	List<FiCol> listFiCol;

	List<FxTreeTableCol> fxTreeTableColumnList;

	public IFiColHelper() {
	}

	public IFiColHelper(List<IFiCol> listFiColInf) {
		setListFiColInf(listFiColInf);
	}

	public static IFiColHelper build(List<? extends IFiCol> list) {
		IFiColHelper fiColInfHelper = new IFiColHelper();
		fiColInfHelper.setListFiColInf(list);
		return fiColInfHelper;
	}

	public static IFiColHelper buildFi(List<FiCol> list) {
		IFiColHelper fiColInfHelper = new IFiColHelper();
		fiColInfHelper.setListFiColInf(list);
		return fiColInfHelper;
	}

	public static List<IFiCol> build() {
		List<IFiCol> list = new ArrayList<>();
		return list;
	}

	public static FiCol buildTableCol(Object fieldName) {
		return new FiCol(fieldName.toString(), fieldName.toString());
	}

	public static <PrmClazz> void autoComment(List<FiCol> listFiCol, Class<PrmClazz> tblurunClass) {

		Map<String, FiField> mapFiFieldsShort = FiEntity.getMapFieldsExtra(tblurunClass);

		for (FiCol fiTableCol : listFiCol) {

			if (mapFiFieldsShort.containsKey(fiTableCol.getFieldName())) {

				FiField fiField = mapFiFieldsShort.get(fiTableCol.getFieldName());

				if (FiBoolean.isFalse(fiField.getNullable())) {
					fiTableCol.setColComment(FiString.addNewLineToEndIfNotEmpty(fiTableCol.getColComment()) + "Zorunlu Alan");
				}

				if (!FiString.isEmpty(fiField.getTxComment())) {
					fiTableCol.setColComment(FiString.addNewLineToEndIfNotEmpty(fiTableCol.getColComment()) + FiString.orEmpty(fiField.getTxComment()));
				}

			}

		}


	}


	public IFiCol getFiTableColByID(String colID) {

		for (IFiCol fiTableCol : getListFiColInf()) {
			if (fiTableCol.getFieldName().equalsIgnoreCase(colID)) {
				return fiTableCol;
			}
		}
		return null;
	}

	public List<? extends IFiCol> getListFiColInf() {
		if (listFiColInf == null) {
			listFiColInf = new ArrayList<>();
		}
		return listFiColInf;
	}

	public void setListFiColInf(List<? extends IFiCol> listFiColInf) {
		this.listFiColInf = listFiColInf;
	}

	public IFiColHelper buildFxTreeTableCol() {

		if (!getListFiColInf().isEmpty()) {

			List<FxTreeTableCol> fxTreeTableColumnList1 = new ArrayList<>();

			for (int colIndex = 0; colIndex < getListFiColInf().size(); colIndex++) {

				IFiCol ozTableCol = getListFiColInf().get(colIndex);

				// Column geçerli değilse eklenmez
				if (ozTableCol.getBoEnabled() != null && !ozTableCol.getBoEnabled()) continue;

				FxTreeTableCol fxTableColumn = new FxTreeTableCol();
				fxTableColumn.setHeader(ozTableCol.getHeaderName());
				fxTableColumn.setFieldName(ozTableCol.getFieldName());
				fxTableColumn.setId(ozTableCol.getFieldName());
				if (ozTableCol.getColType() == null) ozTableCol.setColType(OzColType.String);
				fxTableColumn.setColType(ozTableCol.getColType().toString());
				if (ozTableCol.getPrefSize() != null) {
					fxTableColumn.setPrefWidth(ozTableCol.getPrefSize());
				}
				if (ozTableCol.getBoEditable() != null && ozTableCol.getBoEditable()) {
					fxTableColumn.setEditable(true);
					fxTableColumn.setAutoEditor();
				}

				fxTableColumn.setAutoColumnDefault();

				//Loghelperr.getInstance(getClass()).debug(" Fx TableView col id:"+fxTableColumn.getId());
				fxTreeTableColumnList1.add(fxTableColumn);

			}

			setFxTreeTableColumnList(fxTreeTableColumnList1);

		}

		return this;
	}

	public List<FxTreeTableCol> getFxTreeTableColumnList() {
		return fxTreeTableColumnList;
	}

	public void setFxTreeTableColumnList(List<FxTreeTableCol> fxTreeTableColumnList) {
		if (fxTreeTableColumnList == null) {
			fxTreeTableColumnList = new ArrayList<>();
		}
		this.fxTreeTableColumnList = fxTreeTableColumnList;
	}

	public IFiCol findColumnByFieldName(List<IFiCol> listColumns, String fieldName) {
		setListFiColInf(listColumns);
		return findColumnByFieldName(fieldName);
	}

	public IFiCol findColumnByFieldName(Object objFieldName) {
		return findColumnByFieldName(objFieldName.toString());
	}

	public IFiCol findColumnByFieldName(String fieldName) {

		if (getListFiColInf().size() > 0) {

			for (IFiCol ozTableCol : getListFiColInf()) {
				if (ozTableCol.getFieldName().equals(fieldName)) {
					return ozTableCol;
				}
			}
		}
		return null;
	}

	public List<FiCol> getListFiCol() {
		return listFiCol;
	}

	public void setListFiCol(List<FiCol> listFiCol) {
		this.listFiCol = listFiCol;
	}

}
