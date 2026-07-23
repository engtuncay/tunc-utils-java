package ozpasyazilim.utils.gui.fxcomponents;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.RadioButton;
import ozpasyazilim.utils.datatypes.FiMeta;

public class FxRadio extends RadioButton {

  private ObjectProperty objValue;
  private StringProperty txValue;
  //private IntegerProperty intValue;

  public FxRadio() {
    super();
  }

  public FxRadio(String text) {
    super(text);
  }

  public FxRadio(String text,String value) {
    super(text);
    this.txValue = new SimpleStringProperty(value);
  }

  public FxRadio(FiMeta fiMeta) {
    super(fiMeta.getFtTxValue());
    this.txValue = new SimpleStringProperty(fiMeta.getTxKey());
  }

  public Object getObjValue() {
    return objValue.get();
  }

  public ObjectProperty objValueProperty() {
    return objValue;
  }

  public void setObjValue(Object objValue) {
    this.objValue.set(objValue);
  }

  public String getTxValue() {
    return txValue.get();
  }

  public StringProperty txValueProperty() {
    if (txValue == null) {
      txValue = new SimpleStringProperty();
    }
    return txValue;
  }

  public void setTxValue(String txValue) {
    this.txValue.set(txValue);
  }
}
