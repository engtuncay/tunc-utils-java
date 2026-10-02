package ozpasyazilim.utils.fxwindow;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ozpasyazilim.utils.gui.fxcomponents.FxButton;
import ozpasyazilim.utils.mvc.AbsFiModBaseCont;

/**
 * Common Window
 */
public class FxWindowCont extends AbsFiModBaseCont {

  FxWindowView modView;

  public FxWindowCont() {
    //super.moduleAciklama = "Mos-Shared Form Window";
    //super.moduleCode = EntegreModules.ModContGen.getModuleCode();
  }

  public FxWindowCont(String connProfile) {
    super(connProfile);
  }

  @Override
  public void initCont() {
    modView = new FxWindowView();
    modView.initGui();
  }

  public FxWindowCont buiInit() {
    initCont();
    return this;
  }

  public FxWindowView getModView() {
    return modView;
  }

  public void registerDeleteKeyOnNode(Node node, FxButton btn) {
    node.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
      if (event.getCode() == KeyCode.DELETE) {
        btn.fire();
      }
    });
  }

  public void registerBtnWitCtrlS(FxButton btn) {
    getModView().getRootPane().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
      if (event.isControlDown() && event.getCode() == KeyCode.S) {
        btn.fire();
      }
    });
  }

  public void registerRunWitCtrlAndKey(KeyCode keyCode, Runnable runnable) {
    getModView().getRootPane().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
      if (event.isControlDown() && event.getCode() == keyCode) {
        runnable.run();
      }
    });
  }

  public void registerBtnWitInsert(FxButton btn) {
    registerKeyWithButtonFire(btn, KeyCode.INSERT);
  }

  public void registerBtnWitF10(FxButton btn) {
    registerKeyWithButtonFire(btn, KeyCode.F10);
  }

  public void registerBtnWitDelete(FxButton btn) {
    registerKeyWithButtonFire(btn, KeyCode.DELETE);
  }

  public void registerEscWithCloseWindow() {
    getModView().getRootPane().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
      if (event.getCode() == KeyCode.ESCAPE) {
        //closeFxStage();
      }
    });
  }

  public void registerKeyWithButtonFire(FxButton btn, KeyCode keyCode) {
    getModView().getRootPane().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
      if (event.getCode() == keyCode) {
        btn.fire();
      }
    });
  }

}
