package ozpasyazilim.utils.gui.fxcomponents;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;

/**
 * Bir alt sınıfta auto class kullanabilmek için EntClazz eklendi
 *
 * @param <EntClazz>
 */
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

	public FxMigPaneEnt(FxMigHp fxMigHp) {
		super(fxMigHp.getLayConstGen(), fxMigHp.getLayColConst(), fxMigHp.getLayRowConst());
	}

	public static FxMigPaneEnt buiStandard() {
		return new FxMigPaneEnt(FxMigHp.lgcStandard1InsetZeroGap00);
	}

}
