package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.layout.Pane;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;
import ozpasyazilim.utils.mvc.IFiModView;

/**
 * View görevini üstlenen component
 * <p>
 * IFxSimpleView arayüzü tanımlandı sadece
 */
public class FxMigPaneSimpleView extends FxMigPane implements IFiModView {

	public FxMigPaneSimpleView() {
		super();
	}

	public FxMigPaneSimpleView(LC layoutConstraints) {
		super(layoutConstraints);
	}

	public FxMigPaneSimpleView(LC layoutConstraints, AC colConstraints) {
		super(layoutConstraints, colConstraints);
	}

	public FxMigPaneSimpleView(LC layoutConstraints, AC colConstraints, AC rowConstraints) {
		super(layoutConstraints, colConstraints, rowConstraints);
	}

	public FxMigPaneSimpleView(String layoutConstraints) {
		super(layoutConstraints);
	}

	public FxMigPaneSimpleView(String layoutConstraints, String colConstraints) {
		super(layoutConstraints, colConstraints);
	}

	public FxMigPaneSimpleView(String layoutConstraints, String colConstraints, String rowConstraints) {
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
