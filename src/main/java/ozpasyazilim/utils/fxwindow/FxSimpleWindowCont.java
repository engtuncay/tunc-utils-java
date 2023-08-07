package ozpasyazilim.utils.fxwindow;

import ozpasyazilim.utils.gui.fxcomponents.*;
import ozpasyazilim.utils.mvc.AbsFxSimpleBaseCont;
import ozpasyazilim.utils.mvc.IFxSimpleCont;

/**
 * Genel Pencere Yapısı (Toolbar-Content-Footer)
 *
 */
public class FxSimpleWindowCont extends AbsFxSimpleBaseCont implements IFxSimpleCont {

	FxSimpleWindowView modView;
	String txSelected;

	public FxSimpleWindowCont() {
		setModuleLabel("simple-table-window");
		setModuleCode("100");
	}

	@Override
	public FxSimpleWindowView getModView() {
		return modView;
	}

	@Override
	public void initCont() {
		modView = new FxSimpleWindowView(true);
	}

	public FxSimpleWindowCont buildInit() {
		initCont();
		return this;
	}

	public void setPrefWidthWindows() {

//		ObservableList<TableColumn<EntClazz, ?>> columns = getFxTableView().getColumns();
//
//		Double prefWidth = 30d;
//
//		for (TableColumn column : columns) {
//			//Loghelper.debug(getClass(), "Col Pref Width:"+column.widthProperty().getValue());
//
//			if(column.getPrefSize()!=null){
//				prefWidth+=column.getPrefSize();
//			}else{
//				prefWidth+=80;
//			}
//			//prefWidth += column.getPrefWidth();
//		}


		//Loghelper.debug(getClass(), "Pref Width Calc:"+prefWidth);
		//Loghelper.debug(getClass(), "Migpane Width:"+ getModView().getRootMigPane().getWidth());

		//getModView().getRootMigPane().setMinWidth(prefWidth+200);

		//getModView().getFxTableMig().getFxTableView().setMinWidth(prefWidth);
		//getFxStage().setMinWidth(500d);
		//getFxStage().sizeToScene();

	}

	public String getTxSelected() {
		return txSelected;
	}

	public void setTxSelected(String txSelected) {
		this.txSelected = txSelected;
	}

	public FxMigPane getMigToolbar() {
		return getModView().getMigToolbar();
	}

	public void addNoteLine(String txMessage) {
		getMigToolbar().wrapFi();
		getMigToolbar().addSpan(new FxLabel(txMessage));
	}

}

