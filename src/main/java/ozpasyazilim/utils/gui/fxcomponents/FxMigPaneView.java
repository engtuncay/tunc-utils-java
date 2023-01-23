package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;
import ozpasyazilim.utils.mvc.IFxModView;
import ozpasyazilim.utils.mvc.IFxSimpleView;

/**
 * View görevini üstlenen component (IfxModView interface tanımlı)
 */
public class FxMigPaneView extends FxMigPane implements IFxModView {

	public FxMigPaneView() {
		super();
	}

	public FxMigPaneView(LC layoutConstraints) {
		super(layoutConstraints);
	}

	public FxMigPaneView(LC layoutConstraints, AC colConstraints) {
		super(layoutConstraints, colConstraints);
	}

	public FxMigPaneView(LC layoutConstraints, AC colConstraints, AC rowConstraints) {
		super(layoutConstraints, colConstraints, rowConstraints);
	}

	public FxMigPaneView(String layoutConstraints) {
		super(layoutConstraints);
	}

	public FxMigPaneView(String layoutConstraints, String colConstraints) {
		super(layoutConstraints, colConstraints);
	}

	public FxMigPaneView(String layoutConstraints, String colConstraints, String rowConstraints) {
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
