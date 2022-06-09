package ozpasyazilim.utils.gui.fxcomponents;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;

public class FxMigPaneEnt<EntClazz> extends FxMigPane {

	public FxMigPaneEnt() {
		super();
	}

	public FxMigPaneEnt(LC layoutConstraints) {
		super(layoutConstraints);
	}

	public FxMigPaneEnt(LC layoutConstraints, AC colConstraints) {
		super(layoutConstraints, colConstraints);
	}

	public FxMigPaneEnt(LC layoutConstraints, AC colConstraints, AC rowConstraints) {
		super(layoutConstraints, colConstraints, rowConstraints);
	}

	public FxMigPaneEnt(String layoutConstraints) {
		super(layoutConstraints);
	}

	public FxMigPaneEnt(String layoutConstraints, String colConstraints) {
		super(layoutConstraints, colConstraints);
	}

	public FxMigPaneEnt(String layoutConstraints, String colConstraints, String rowConstraints) {
		super(layoutConstraints, colConstraints, rowConstraints);
	}

	public FxMigPaneEnt(FxMigHelper fxMigHelper) {
		super(fxMigHelper.getLayConst(), fxMigHelper.getColConst(), fxMigHelper.getRowConst());
	}

	public static FxMigPaneEnt buiStandard() {
		return new FxMigPaneEnt(FxMigHelper.lcStandard1InsetZeroGap00);
	}

}
