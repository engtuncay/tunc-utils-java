package ozpasyazilim.utils.fxwindow;

import javafx.collections.transformation.FilteredList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ozpasyazilim.utils.core.FiCollection;
import ozpasyazilim.utils.core.FiReflection;
import ozpasyazilim.utils.datatypes.Fkb;
import ozpasyazilim.utils.gui.fxcomponents.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Fi(Util sınıfında olduğunu gösterir)
 * <p>
 * pullTable metodları implemente edilmesi gerektiği için Abstract yapıldı
 *
 * @param
 */
public abstract class FiAbsTableFkbWindowCont extends FiFxWindowCont {

  private String txSelected;
  private List<Fkb> listEntitySelected;
  private List<Fkb> listAll;
  private Fkb entitySelected;
  private Fkb entityLastSaved;

  /**
   * Seçim Düğmesi
   */
  protected FxButton btnCrudSelect;

  /**
   * Rapor oluştur Düğmesi
   */
  protected FxButton btnCrudReport;
  protected FxButton btnCrudRefresh;

  private FxTableMig2<Fkb> fxTableMig;

  public FiAbsTableFkbWindowCont() {
    super();
    setModuleLabel("FiTableWindow");
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

  /**
   * eğer thread ile çekilecekse data, thread içinde pullTableDataThread metodu çalıştırılır
   */
  abstract protected void pullTableData();

  /**
   * Thread için çalıştırılacak metod
   */
  abstract protected void pullTableDataThread();

  public Fkb getSelectedItemInTable() {
    return (Fkb) getFxTableView().getSelectedItemFi();
  }

  public <EntClazz, B> void assignBoSecim(FxTextFieldBtn fxTextFieldBtn, Function<EntClazz, B> fnKeyValue) {

    if (fxTextFieldBtn.getObjValue() == null) return;

    List<EntClazz> listData = (List<EntClazz>) fxTextFieldBtn.getObjValue();

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

  public List<Fkb> getListAll() {
    return listAll;
  }

  public void setListAll(List<Fkb> listAll) {
    this.listAll = listAll;
  }

  public Fkb getEntityLastSaved() {
    return entityLastSaved;
  }

  public void setEntityLastSaved(Fkb entityLastSaved) {
    this.entityLastSaved = entityLastSaved;
  }

  public FxTableView2<Fkb> getFxTableView() {
    //return getModView().getFxTableMig().getFxTableView();
    return getFxTableMig().getFxTableView();
  }

  public String getTxSelected() {
    return txSelected;
  }

  public void setTxSelected(String txSelected) {
    this.txSelected = txSelected;
  }

  public List<Fkb> getListEntitySelected() {
    return listEntitySelected;
  }

  public void setListEntitySelected(List<Fkb> listEntitySelected) {
    this.listEntitySelected = listEntitySelected;
  }

  public FxMigPane getMigToolbar() {
    return getModView().getMigToolbar();
  }

  protected FxButton addBtnMotReport() {
    btnCrudReport = FiButtons.genBtnReport();
    getModView().getMigToolbar().add(btnCrudReport);
    return btnCrudReport;
  }

  protected void addBtnReportWithActionThread() {
    btnCrudReport = FiButtons.genBtnReport();
    getModView().getMigToolbar().add(btnCrudReport);
    btnCrudReport.setOnActionWithThread(this::pullTableData);
  }

  protected void addBtnReportWithAction() {
    btnCrudReport = FiButtons.genBtnReport();
    getModView().getMigToolbar().add(btnCrudReport);
    btnCrudReport.setOnAction((event) -> pullTableData());
  }

  public void registerDeleteOnTable(FxButton btn) {
    getFxTableView().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
      if (event.getCode() == KeyCode.DELETE) {
        btn.fire();
      }
    });
  }

  public FxButton getBtnCrudReport() {
    return btnCrudReport;
  }

  public FxTableMig2<Fkb> getFxTableMig() {
    return fxTableMig;
  }

  public void setFxTableMig(FxTableMig2<Fkb> fxTableMig) {
    this.fxTableMig = fxTableMig;
  }

  public Fkb getEntitySelected() {
    return entitySelected;
  }

  public void setEntitySelected(Fkb entitySelected) {
    this.entitySelected = entitySelected;
  }

  public void actCrudRefresh() {
    pullTableData();
  }

  protected void addSelectAndRefreshButton() {
    btnCrudSelect = FiButtons.genBtnSecim();
    btnCrudRefresh = FiButtons.genBtnRefresh();
    // Add Layout
    getModView().getMigToolbar().add(btnCrudSelect);
    getModView().getMigToolbar().add(btnCrudRefresh);
  }

  protected void addRefreshButton() {
    btnCrudRefresh = FiButtons.genBtnRefresh();
    getModView().getMigToolbar().add(btnCrudRefresh);
  }

  protected void actBtnRefreshFire() {
    getBtnCrudRefresh().fire();
  }

  public FxButton getBtnCrudRefresh() {
    return btnCrudRefresh;
  }

}
