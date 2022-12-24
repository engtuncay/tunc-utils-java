package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.layout.Pane;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;
import ozpasyazilim.utils.mvc.IFxSimpleView;

/**
 * View görevini üstlenen component
 * <p>
 * IFxSimpleView arayüzü tanımlandı sadece
 */
public class FxMigPaneViewSimp extends FxMigPane implements IFxSimpleView {

	public FxMigPaneViewSimp() {
		super();
	}

	public FxMigPaneViewSimp(LC layoutConstraints) {
		super(layoutConstraints);
	}

	public FxMigPaneViewSimp(LC layoutConstraints, AC colConstraints) {
		super(layoutConstraints, colConstraints);
	}

	public FxMigPaneViewSimp(LC layoutConstraints, AC colConstraints, AC rowConstraints) {
		super(layoutConstraints, colConstraints, rowConstraints);
	}

	public FxMigPaneViewSimp(String layoutConstraints) {
		super(layoutConstraints);
	}

	public FxMigPaneViewSimp(String layoutConstraints, String colConstraints) {
		super(layoutConstraints, colConstraints);
	}

	public FxMigPaneViewSimp(String layoutConstraints, String colConstraints, String rowConstraints) {
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
