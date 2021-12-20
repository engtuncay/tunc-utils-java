package ozpasyazilim.utils.gui.fxcomponents;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;
import ozpasyazilim.utils.core.FiString;

public class FxMigPane<EntClazz> extends MigPane {

	// inset 0
	public static String lcStandardInset0Gap50 = "insets 0,gap 5 0"; // gap x y : x yatatya boşluk : y dikeyde boşluk
	public static String lcStandardInset0Gap55 = "insets 0,gap 5 5"; // gap x y : x yatatya boşluk : y dikeyde boşluk
	public static String lcStandardInset0Gap00 = "insets 0,gap 0 0";
	public static String lcStandardInset0Gap5555 = "insets 0,gap 5 5 5 5"; // gap x y : x yatatya boşluk : y dikeyde boşluk

	// inset 5
	public static String lcStandardInset3Gap33 = "insets 3,gap 3 3";

	// inset 5
	public static String lcStandardInset5Gap55 = "insets 5,gap 5 5";
	public static String lcStandardInset5Gap00 = "insets 5,gap 0 0";

	public static String lcDebug = ",debug";

	public FxMigPane() {
		super();
	}

	public FxMigPane(LC layoutConstraints) {
		super(layoutConstraints);
	}

	public FxMigPane(LC layoutConstraints, AC colConstraints) {
		super(layoutConstraints, colConstraints);
	}

	public FxMigPane(LC layoutConstraints, AC colConstraints, AC rowConstraints) {
		super(layoutConstraints, colConstraints, rowConstraints);
	}

	public FxMigPane(String layoutConstraints) {
		super(layoutConstraints);
	}

	public FxMigPane(String layoutConstraints, String colConstraints) {
		super(layoutConstraints, colConstraints);
	}

	public FxMigPane(String layoutConstraints, String colConstraints, String rowConstraints) {
		super(layoutConstraints, colConstraints, rowConstraints);
	}

	public FxMigPane(FxMigHelper fxMigHelper) {
		super(fxMigHelper.getLayConst(), fxMigHelper.getColConst(), fxMigHelper.getRowConst());
	}

	// Constraint Shortcuts

	// cc component constraint
	// lc layout constraint
	// rc row constraint
	// clc col constraint

	public static String ccHeight(int height) {
		return ",h " + height;
	}

	public static String ccHeightPerc(int heightPercentage) {
		return ",h " + heightPercentage + "%";
	}

	public static String ccWidth(int width) {
		return ",w " + width;
	}

	public static String ccPrefWidth(int i) {
		return ",w :" + i + ":" + i;
	}

	public static void addDebugLc() {
		FxMigPane.lcStandardInset0Gap50 += FxMigPane.lcDebug;
		FxMigPane.lcStandardInset0Gap5555 += FxMigPane.lcDebug;
		FxMigPane.lcStandardInset3Gap33 += FxMigPane.lcDebug;
		FxMigPane.lcStandardInset5Gap55 += FxMigPane.lcDebug;
	}

	public static FxMigPane buiStandard() {
		return new FxMigPane(FxMigHelper.lcStandard1InsetZeroGap00);
	}

	public void wrapFi() {
		add(new FxLabel(""), "span,wrap");
	}

	public void addGrowXSpan(Node node) {
		add(node, "growx,pushx,span");
	}

	public void addSpanWrap(Node node) {
		add(node, "span,wrap");
	}

	public void addGrowXSpan(Node node, String extra) {
		add(node, "growx,pushx,span " + FiString.orEmpty(extra));
	}

	public void addGrowXPushXSpan(Node node, String extra) {
		add(node, "growx,pushx,span " + FiString.orEmpty(extra));
	}

	public void addGrowPushSpan(Node node, String extra) {
		add(node, "grow,push,span " + FiString.orEmpty(extra));
	}

	public void addGrowXPushXSpan(Node node) {
		addGrowXPushXSpan(node, null);
	}

	public Separator addSeparatorHr() {
		Separator separator = new Separator(Orientation.HORIZONTAL);
		addGrowXSpan(separator);
		return separator;
	}

	public FxMigPane addFi(Node node) {
		add(node);
		return this;
	}
}
