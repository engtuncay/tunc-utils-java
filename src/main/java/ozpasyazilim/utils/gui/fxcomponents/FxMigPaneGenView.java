package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.layout.Pane;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;
import ozpasyazilim.utils.mvc.IFxEntSimpleView;

/**
 * Bir alt sınıfta auto class kullanabilmek için EntClazz eklendi
 *
 * @param <EntClazz>
 */
public class FxMigPaneGenView<EntClazz> extends FxMigPane implements IFxEntSimpleView {

	public FxMigPaneGenView() {
		super();
	}

	public FxMigPaneGenView(LC layoutConstraints) {
		super(layoutConstraints);
	}

	public FxMigPaneGenView(LC layoutConstraints, AC colConstraints) {
		super(layoutConstraints, colConstraints);
	}

	public FxMigPaneGenView(LC layoutConstraints, AC colConstraints, AC rowConstraints) {
		super(layoutConstraints, colConstraints, rowConstraints);
	}

	public FxMigPaneGenView(String layoutConstraints) {
		super(layoutConstraints);
	}

	public FxMigPaneGenView(String layoutConstraints, String colConstraints) {
		super(layoutConstraints, colConstraints);
	}

	public FxMigPaneGenView(String layoutConstraints, String colConstraints, String rowConstraints) {
		super(layoutConstraints, colConstraints, rowConstraints);
	}

	public FxMigPaneGenView(FxMigHp fxMigHp) {
		super(fxMigHp.getLcgPrep2(), fxMigHp.getLayColConst(), fxMigHp.getLayRowConst());
	}

	public static FxMigPaneGenView buiStandard() {
		return new FxMigPaneGenView(FxMigHp.lgcStandard1InsetZeroGap00);
	}

	@Override
	public Pane getRootPane() {
		return this;
	}

	@Override
	public void initGui() {

	}
}
