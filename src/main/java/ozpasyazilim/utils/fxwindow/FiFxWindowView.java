package ozpasyazilim.utils.fxwindow;

import javafx.scene.layout.Pane;
import ozpasyazilim.utils.gui.fxcomponents.FxMigHp;
import ozpasyazilim.utils.gui.fxcomponents.FxMigPane;
import ozpasyazilim.utils.mvc.IFiModView;

/**
 * Toolbar-Content-Footer üç alandan oluşur.
 */
public class FiFxWindowView implements IFiModView {

  // Containers
  private FxMigPane migRoot;
  private FxMigPane migToolbar;
  private FxMigPane migContent;
  private FxMigPane migFooter;

  public FiFxWindowView() {

  }

  public FiFxWindowView(Boolean withInit) {
    initGui();
  }

  @Override
  public Pane getRootPane() {
    return migRoot;
  }

  @Override
  public void initGui() {

    // Container Initial.
    migRoot = new FxMigPane(FxMigHp.bui().lcgInset5Gap55().getLcg()); // old:Gap05
    migToolbar = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().lcgNoGrid().getLcg());
    migContent = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().getLcg());
    migFooter = new FxMigPane(FxMigHp.bui().lcgInset0Gap55().lcgNoGrid().getLcg());

    // Container Setup
    getMigRoot().addGrowXPushXSpan(migToolbar); //, FxMigHp.bui().ccGapAfter("5").getCc()
    getMigRoot().addGrowPushSpan(migContent);
    getMigRoot().addGrowXPushXSpan(migFooter);

  }

  public FxMigPane getMigRoot() {
    return migRoot;
  }

  public FxMigPane getMigToolbar() {
    return migToolbar;
  }

  public FxMigPane getMigContent() {
    return migContent;
  }

  public FxMigPane getMigFooter() {
    return migFooter;
  }

}
