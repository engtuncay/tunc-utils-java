package ozpasyazilim.utils.gui.fxcomponents;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.RadioButton;
import ozpasyazilim.utils.datatypes.FiMeta;

public class FxRadio extends RadioButton {

  private ObjectProperty objValue;

  /**
   * Label olarak görünen değerin key değeri , ya da kodu olarak düşünülebilir
   */
  private StringProperty txKey;
  //private IntegerProperty intValue;

  public FxRadio() {
    super();
  }

  public FxRadio(String text) {
    super(text);
  }

  public FxRadio(String text,String value) {
    super(text);
    this.txKey = new SimpleStringProperty(value);
  }

  public FxRadio(FiMeta fiMeta) {
    super(fiMeta.getFtTxValue());
    this.txKey = new SimpleStringProperty(fiMeta.getTxKey());
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

  public String getTxKey() {
    return txKey.get();
  }

  public StringProperty txKeyProperty() {
    if (txKey == null) {
      txKey = new SimpleStringProperty();
    }
    return txKey;
  }

  public void setTxKey(String txKey) {
    this.txKey.set(txKey);
  }
}
