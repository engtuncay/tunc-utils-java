package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.Node;
import ozpasyazilim.utils.table.OzColType;

public class NodeConfig {

  OzColType ozColType;

  String txColEditorClass;

  Node nodeComp;

  Object objCompValue;

  // Getter and Setter

  public OzColType getOzColType() {
    return ozColType;
  }

  public void setOzColType(OzColType ozColType) {
    this.ozColType = ozColType;
  }

  public String getTxColEditorClass() {
    return txColEditorClass;
  }

  public void setTxColEditorClass(String txColEditorClass) {
    this.txColEditorClass = txColEditorClass;
  }

  public Node getNodeComp() {
    return nodeComp;
  }

  public void setNodeComp(Node nodeComp) {
    this.nodeComp = nodeComp;
  }

  public Object getObjCompValue() {
    return objCompValue;
  }

  public void setObjCompValue(Object objCompValue) {
    this.objCompValue = objCompValue;
  }
}
