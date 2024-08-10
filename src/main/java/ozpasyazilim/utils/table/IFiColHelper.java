package ozpasyazilim.utils.table;

import ozpasyazilim.utils.core.FiBool;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.fidborm.FiFieldUtil;
import ozpasyazilim.utils.fidborm.FiField;
import ozpasyazilim.utils.gui.fxcomponents.FxTreeTableCol;
import ozpasyazilim.utils.mvc.IFiCol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IFiColHelper {

	/**
	 * IFiCol Türevi Genel kullanım olursa
	 */
	List<? extends IFiCol> listIFiCol;

	/**
	 * FiCol kullanılırsa
	 */
	List<FiCol> listFiCol;

	List<FxTreeTableCol> listFxTreeTableCol;

	public IFiColHelper() {
	}

	public IFiColHelper(List<IFiCol> listIFiCol) {
		setListIFiCol(listIFiCol);
	}

	public static IFiColHelper build(List<? extends IFiCol> list) {
		IFiColHelper iFiColHelper = new IFiColHelper();
		iFiColHelper.setListIFiCol(list);
		return iFiColHelper;
	}

	public static IFiColHelper buildFi(List<FiCol> list) {
		IFiColHelper iFiColHelper = new IFiColHelper();
		iFiColHelper.setListIFiCol(list);
		return iFiColHelper;
	}

	public static List<IFiCol> build() {
		List<IFiCol> list = new ArrayList<>();
		return list;
	}

	public static FiCol buildTableCol(Object fieldName) {
		return new FiCol(fieldName.toString(), fieldName.toString());
	}

	public static <PrmClazz> void autoComment(List<FiCol> listFiCol, Class<PrmClazz> tblurunClass) {

		Map<String, FiField> mapFiFieldsShort = FiFieldUtil.getMapFieldsExtra(tblurunClass);

		for (FiCol fiTableCol : listFiCol) {

			if (mapFiFieldsShort.containsKey(fiTableCol.getOfcTxFieldName())) {

				FiField fiField = mapFiFieldsShort.get(fiTableCol.getOfcTxFieldName());

				if (FiBool.isFalse(fiField.getOfcBoNullable())) {
					fiTableCol.setColComment(FiString.addNewLineToEndIfNotEmpty(fiTableCol.getColComment()) + "Zorunlu Alan");
				}

				if (!FiString.isEmpty(fiField.getTxComment())) {
					fiTableCol.setColComment(FiString.addNewLineToEndIfNotEmpty(fiTableCol.getColComment()) + FiString.orEmpty(fiField.getTxComment()));
				}

			}

		}


	}


	public IFiCol getIFiColByID(String colID) {

		for (IFiCol fiTableCol : getListIFiColInit()) {
			if (fiTableCol.getOfcTxFieldName().equalsIgnoreCase(colID)) {
				return fiTableCol;
			}
		}
		return null;
	}

	public List<? extends IFiCol> getListIFiColInit() {
		if (listIFiCol == null) {
			listIFiCol = new ArrayList<>();
		}
		return listIFiCol;
	}

	public void setListIFiCol(List<? extends IFiCol> listIFiCol) {
		this.listIFiCol = listIFiCol;
	}

	public IFiColHelper buildFxTreeTableCol() {

		if (!getListIFiColInit().isEmpty()) {

			List<FxTreeTableCol> fxTreeTableColumnList1 = new ArrayList<>();

			for (int colIndex = 0; colIndex < getListIFiColInit().size(); colIndex++) {

				IFiCol iFiCol = getListIFiColInit().get(colIndex);

				FiCol fiCol = FiCol.buildFromIFiCol(iFiCol);

				// Column geçerli değilse eklenmez
				if (iFiCol.getBoEnabled() != null && !iFiCol.getBoEnabled()) continue;

				FxTreeTableCol fxTableColumn = new FxTreeTableCol();
				fxTableColumn.setRefFiCol(fiCol);
//				fxTableColumn.setHeader(iFiCol.getHeaderName());
//				fxTableColumn.setFieldName(iFiCol.getFieldName());
				fxTableColumn.setId(iFiCol.getOfcTxFieldName());
				if (fiCol.getColType() == null) fiCol.setColType(OzColType.String);
//				fxTableColumn.setColType(iFiCol.getColType().toString());
				if (fiCol.getPrefSize() != null) {
					fxTableColumn.setPrefWidth(iFiCol.getPrefSize());
				}

				if (FiBool.isTrue(fiCol.getBoEditable())) {
					fxTableColumn.setEditable(true);
					fxTableColumn.setAutoEditor();
				}

				fxTableColumn.setAutoColumnDefaultByFiCol();

				//Loghelperr.getInstance(getClass()).debug(" Fx TableView col id:"+fxTableColumn.getId());
				fxTreeTableColumnList1.add(fxTableColumn);

			}

			setListFxTreeTableCol(fxTreeTableColumnList1);

		}

		return this;
	}

	public List<FxTreeTableCol> getListFxTreeTableCol() {
		return listFxTreeTableCol;
	}

	public void setListFxTreeTableCol(List<FxTreeTableCol> listFxTreeTableCol) {
		if (listFxTreeTableCol == null) {
			listFxTreeTableCol = new ArrayList<>();
		}
		this.listFxTreeTableCol = listFxTreeTableCol;
	}

	public IFiCol findColumnByFieldName(List<IFiCol> listColumns, String fieldName) {
		setListIFiCol(listColumns);
		return findColumnByFieldName(fieldName);
	}

	public IFiCol findColumnByFieldName(Object objFieldName) {
		return findColumnByFieldName(objFieldName.toString());
	}

	public IFiCol findColumnByFieldName(String fieldName) {

		if (!getListIFiColInit().isEmpty()) {

			for (IFiCol ozTableCol : getListIFiColInit()) {
				if (ozTableCol.getOfcTxFieldName().equals(fieldName)) {
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
