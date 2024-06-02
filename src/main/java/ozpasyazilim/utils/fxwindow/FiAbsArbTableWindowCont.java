package ozpasyazilim.utils.fxwindow;

import javafx.collections.transformation.FilteredList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ozpasyazilim.utils.core.FiCollection;
import ozpasyazilim.utils.core.FiReflection;
import ozpasyazilim.utils.gui.fxcomponents.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class FiAbsArbTableWindowCont<EntClazz> extends FiArbWindowCont {

    String txSelected;
    List<EntClazz> listEntitySelected;
    List<EntClazz> listAll;
    EntClazz entitySelected;
    EntClazz entityLastSaved;

    /**
     * Seçim Düğmesi
     */
    protected FxButton btnCrudSelect;

    /**
     * Rapor oluştur Düğmesi
     */
    protected FxButton btnCrudReport;
    protected FxButton btnCrudRefresh;

    private FxTableMig2<EntClazz> fxTableMig;

    public FiAbsArbTableWindowCont() {
        super();
        super.moduleLabel = "FiArbTableWindow";
        //super.moduleCode =
    }

    @Override
    public void initCont() {
        super.initCont();

        fxTableMig = new FxTableMig2<>();
        getModView().getMigContent().addGrowPushSpan(fxTableMig);

        //Loghelper.getInstance(getClass()).debug("MotTableWindowCont Init - View will init");
        //modView = new AbsMotTableWindowView(true);
    }

    abstract protected void pullTableData();

    public EntClazz getSelectedItemInTable() {
        return (EntClazz) getFxTableView().getSelectedItemFi();
    }

    public <EntClazz, B> void assignBoSecim(FxTextFieldBtn fxTextFieldBtn, Function<EntClazz, B> fnKeyValue) {

        if (fxTextFieldBtn.getEntValue() == null) return;

        List<EntClazz> listData = (List<EntClazz>) fxTextFieldBtn.getEntValue();

        FilteredList<EntClazz> itemsCurrent = (FilteredList<EntClazz>) getFxTableView().getItemsCurrentFi();
        Map<B, EntClazz> mapKeyToObject = FiCollection.listToMapSingle(listData, fnKeyValue);

        itemsCurrent.forEach(entity -> {
            B keyValue = fnKeyValue.apply(entity);
            if (mapKeyToObject.containsKey(keyValue)) {
                FiReflection.setter(entity, "boSecim", true);
                //entity.setBoSecim(true);
            }
        });
    }

    // Getters Setters

    public List<EntClazz> getListAll() {
        return listAll;
    }

    public void setListAll(List<EntClazz> listAll) {
        this.listAll = listAll;
    }

    public EntClazz getEntityLastSaved() {
        return entityLastSaved;
    }

    public void setEntityLastSaved(EntClazz entityLastSaved) {
        this.entityLastSaved = entityLastSaved;
    }

    public FxTableView2<EntClazz> getFxTableView() {
        //return getModView().getFxTableMig().getFxTableView();
        return getFxTableMig().getFxTableView();
    }

    public String getTxSelected() {
        return txSelected;
    }

    public void setTxSelected(String txSelected) {
        this.txSelected = txSelected;
    }

    public List<EntClazz> getListEntitySelected() {
        return listEntitySelected;
    }

    public void setListEntitySelected(List<EntClazz> listEntitySelected) {
        this.listEntitySelected = listEntitySelected;
    }

    public FxMigPane getMigToolbar() {
        return getModView().getMigToolbar();
    }

    protected FxButton addBtnMotReport() {
        btnCrudReport = FiArbButtonsIcons.genBtnReport();
        getModView().getMigToolbar().add(btnCrudReport);
        return btnCrudReport;
    }

    protected FxButton addBtnReportWithActionThread() {
        btnCrudReport = FiArbButtonsIcons.genBtnReport();
        getModView().getMigToolbar().add(btnCrudReport);
        btnCrudReport.setOnActionWithThread(this::pullTableData);
        return btnCrudReport;
    }

    public void registerDeleteOnTable(FxButton btn) {
        getFxTableView().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.DELETE) {
                btn.fire();
            }
        });
    }

    public FxButton getBtnMotReport() {
        return btnCrudReport;
    }

//    protected void pullTableDataAsyn() {
//        Platform.runLater(this::pullTableData);
//    }

    public FxTableMig2<EntClazz> getFxTableMig() {
        return fxTableMig;
    }

    public void setFxTableMig(FxTableMig2<EntClazz> fxTableMig) {
        this.fxTableMig = fxTableMig;
    }

    public EntClazz getEntitySelected() {
        return entitySelected;
    }

    public void setEntitySelected(EntClazz entitySelected) {
        this.entitySelected = entitySelected;
    }

    public void actCrudRefresh() {
        pullTableData();
    }

    protected void addSelectAndRefreshButton() {
        btnCrudSelect = FiArbButtonsIcons.genBtnSecim();
        btnCrudRefresh = FiArbButtonsIcons.genBtnRefresh();
        // Add Layout
        getModView().getMigToolbar().add(btnCrudSelect);
        getModView().getMigToolbar().add(btnCrudRefresh);
    }

    protected void addRefreshButton() {
        btnCrudRefresh = FiArbButtonsIcons.genBtnRefresh();
        getModView().getMigToolbar().add(btnCrudRefresh);
    }

    protected void actBtnRefreshFire() {
        getBtnCrudRefresh().fire();
    }

    public FxButton getBtnCrudRefresh() {
        return btnCrudRefresh;
    }

}
