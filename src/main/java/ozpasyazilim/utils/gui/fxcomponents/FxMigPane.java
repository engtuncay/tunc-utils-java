package ozpasyazilim.utils.gui.fxcomponents;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;
import ozpasyazilim.utils.core.FiString;

/**
 * Constraint Shortcuts
 * <p>
 * cc component constraint
 * <p>
 * lcg layout constraint general
 * <p>
 * lrc layout row constraint
 * <p>
 * lcc layout col constraint
 */
public class FxMigPane extends MigPane {

	//public static String lcStandardInset0Gap55 = "insets 0,gap 5 5"; // gap x y : x yatatya boşluk : y dikeyde boşluk
	//public static String lcDebug = ",debug";

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

	public FxMigPane(FxMigHp fxMigHp) {
		super(fxMigHp.getLcgPrep2(), fxMigHp.getLayColConst(), fxMigHp.getLayRowConst());
	}

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

	public static FxMigPane buiStandard() {
		return new FxMigPane(FxMigHp.lgcStandard1InsetZeroGap00);
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
		add(node, appendExtra("growx,pushx,span", extra));
	}

	public void addGrowXPushXSpan(Node node, String extra) {
		add(node, appendExtra("growx,pushx,span", extra));
	}

	private String appendExtra(String constraint, String extra) {

		if (FiString.isEmptyTrim(extra)) {
			return constraint;
		}
		// extra is full
		String seperator = FiString.getCommaIfNotEmpty(constraint);
		return FiString.orEmpty(constraint) + seperator + extra;
	}

	public void addGrowPushSpan(Node node) {
		add(node, "grow,push,span");
	}

	public void addWrap(Node node) {
		add(node, "wrap");
	}

	public void addGrowPushSpan(Node node, String extra) {
		add(node, appendExtra("grow,push,span", extra));
	}

	public void addGrowSpan(Node node, String extra) {
		add(node, appendExtra("grow,span", extra));
	}

	public void addSpan(Node node) {
		add(node, "span");
	}

	public void addSpan(Node node,String extra) {
		add(node, appendExtra("span",extra));
	}

	public void addGrowXPushXSpan(Node node) {
		addGrowXPushXSpan(node, null);
	}

	/**
	 * Yatay çizgi ekler
	 *
	 * @return seperator node
	 */
	public Separator addSeparatorLineHor() {
		Separator separator = new Separator(Orientation.HORIZONTAL);
		addGrowXSpan(separator);
		return separator;
	}

	public FxMigPane addFi(Node node) {
		add(node);
		return this;
	}

	public void addAlignxRight(Node node) {
		add(node, "alignx right,span");
	}


}
