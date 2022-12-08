package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.layout.Pane;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;
import ozpasyazilim.utils.mvc.IFxModView;
import ozpasyazilim.utils.mvc.IFxSimpleView;

/**
 * View görevini üstlenen component
 * <p>
 * IFxSimpleView arayüzü tanımlandı sadece
 */
public class FxMigPaneView2 extends FxMigPane implements IFxSimpleView {

	public FxMigPaneView2() {
		super();
	}

	public FxMigPaneView2(LC layoutConstraints) {
		super(layoutConstraints);
	}

	public FxMigPaneView2(LC layoutConstraints, AC colConstraints) {
		super(layoutConstraints, colConstraints);
	}

	public FxMigPaneView2(LC layoutConstraints, AC colConstraints, AC rowConstraints) {
		super(layoutConstraints, colConstraints, rowConstraints);
	}

	public FxMigPaneView2(String layoutConstraints) {
		super(layoutConstraints);
	}

	public FxMigPaneView2(String layoutConstraints, String colConstraints) {
		super(layoutConstraints, colConstraints);
	}

	public FxMigPaneView2(String layoutConstraints, String colConstraints, String rowConstraints) {
		super(layoutConstraints, colConstraints, rowConstraints);
	}


	@Override
	public Pane getRootPane() {
		return this;
	}

	@Override
	public void initGui() {

	}


}
