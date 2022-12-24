package ozpasyazilim.utils.gui.fxcomponents;

import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.table.OzColType;

/**
 * layConst : new MigPane(String layoutConstraints, String colConstraints, String rowConstraints)
 * <p>
 * lgc,lcc,lrc parametreleri migPane oluşturulurken constructor'a parametre olarak verilir.
 * <p>
 * cc : cell contraint
 * <p>
 * cc ise migpane addNode metodu ile node eklenirken parametre olarak verilir.
 */
public class FxMigHp {

	/**
	 * Layout General Constraits (constructor based)
	 */
	private String layConstGen; // lgc

	/**
	 * Layout Column Constraints (constructor based)
	 */
	private String layColConst; // lcc

	/**
	 * Layout Row Constraints (constructor based)
	 */
	private String layRowConst; // lrc

	private String cellConst; //cc

	public static Boolean debugMode = false;

	// gap x y : x yatatya boşluk : y dikeyde boşluk
	public static String lgcStandard1InsetZeroGap50 = "insets 0,gap 5 0";
	// Statik degil get ile yapalım.
//	public static String lcStandard1InsetZeroGap55 = "insets 0,gap 5 5";
	public static String lgcStandard1InsetZeroGap00 = "insets 0,gap 0 0";
//	public static String lcStandard2WithInset3 = "insets 3,gap 3 3";
//	public static String lcStandard2WithInset5Gap55 = "insets 5,gap 5 5";
//	public static String lcStandard2WithInset5Gap00 = "insets 5,gap 0 0";
//	public static String lcStandard1InsetZeroGap5555 = "insets 0,gap 5 5 5 5";

	public static FxMigHp bui() {
		return new FxMigHp();
	}

	public static FxMigHp bcc(String cc) {
		FxMigHp fxMigHp = new FxMigHp();
		fxMigHp.setCellConst(cc);
		return fxMigHp;
	}

	public static FxMigHp blc(String lc) {
		FxMigHp fxMigHp = new FxMigHp();
		fxMigHp.setLayConstGen(lc);
		return fxMigHp;
	}

	public static FxMigHp blc(String lgc, String rc, String lcc) {
		FxMigHp fxMigHp = new FxMigHp();
		fxMigHp.setLayConstGen(lgc);
		fxMigHp.setLayRowConst(rc);
		fxMigHp.setLayColConst(lcc);
		return fxMigHp;
	}

	public FxMigHp ccWidth(String txWidth) {
		addCommaToCc();
		cellConst += "w " + txWidth;
		return this;
	}

	public FxMigHp lgcInsetAndGap(Integer inset, Integer gapx, Integer gapy) {
		addCommaLgcPrev();
		this.layConstGen += String.format("insets %s,gap %s %s", inset, gapx, gapy);
		return this;
	}

	public FxMigHp lgcNoGrid() {
		addCommaLgcPrev();
		this.layConstGen += "nogrid";
		return this;
	}

	public FxMigHp lgcAlignx(Integer percent) {
		addCommaLgcPrev();
		this.layConstGen += "ax " + percent + "%";
		return this;
	}

	public FxMigHp lgcStInset0Gap55() {
		lgcInsetAndGap(0, 5, 5);
		return this;
	}

	public FxMigHp lgcStInset0Gap50() {
		lgcInsetAndGap(0, 5,0 );
		return this;
	}

	public FxMigHp lgcStInset0Gap00() {
		lgcInsetAndGap(0, 0,0 );
		return this;
	}

	public FxMigHp lgcStInset3() {
		lgcInsetAndGap(3, 3, 3);
		return this;
	}

	public FxMigHp lgcStInset3Gap0() {
		lgcInsetAndGap(3, 0, 0);
		return this;
	}

	public FxMigHp lgcStInset3Gap33() {
		lgcInsetAndGap(3, 3, 3);
		return this;
	}

	private void addCommaLgcPrev() {
		if (FiString.isEmptyTrim(getLayConstGen())) return;
		this.layConstGen += ",";
	}

	private void addCommaToCc() {
		if (FiString.isEmptyTrim(getCcInit())) return;
		this.cellConst += ",";
	}

	public String getLayConstGen() {
		if (layConstGen == null) {
			layConstGen = "";
		}
		return layConstGen;
	}

	public String getLayRowConst() {
		if (layRowConst == null) {
			layRowConst = "";
		}
		return layRowConst;
	}

	public String getLayColConst() {
		if (layColConst == null) {
			layColConst = "";
		}
		return layColConst;
	}

	/**
	 * Layout Constraint
	 * <p>
	 * MigPane constructor na yazılır  (Lc,Lrc,Lcc)
	 *
	 * @return
	 */
	public String genLayConst() {

		if (layConstGen == null) {
			layConstGen = "";
		}

		if (debugMode) {
			if (!layConstGen.contains("debug")) {
				addCommaLgcPrev();
				layConstGen += "debug";
			}
		}

		return layConstGen;
	}

	/**
	 * Layout Row Constraints
	 *
	 * @return
	 */
	public String genLrc() {
		return layRowConst;
	}

	/**
	 * Layout Column Constraint
	 *
	 * @return
	 */
	public String genLcc() {
		return layColConst;
	}

	/**
	 * Component constraint
	 *
	 * @return
	 */
	public String genCc() {
		return cellConst;
	}

	public String getCcInit() {
		if (cellConst == null) {
			cellConst = "";
		}
		return cellConst;
	}

	public String getCc() {
		return cellConst;
	}

	public FxMigHp lgcDebug() {
		addCommaLgcPrev();
		this.layConstGen += "debug";
		return this;
	}

	public void setLayConstGen(String layConstGen) {
		this.layConstGen = layConstGen;
	}

	public void setLayRowConst(String layRowConst) {
		this.layRowConst = layRowConst;
	}

	public void setLayColConst(String layColConst) {
		this.layColConst = layColConst;
	}

	public void setCellConst(String cellConst) {
		this.cellConst = cellConst;
	}

	public FxMigHp addCcCompMaxWidthSizeByColType(IFiCol iFiCol) {

		if (iFiCol.getColType() == OzColType.Date || iFiCol.getColEditorClass().equals(FxDatePicker.class.getName())) {
			appendCc("wmax 150");
		}

		if (iFiCol.getColType() == OzColType.Double) {
			appendCc("wmax 200");
		}

		if (iFiCol.getColType() == OzColType.Integer) {
			appendCc("wmax 150");
		}

		return this;
	}

	private void appendCc(String value) {
		addCommaToCc();
		setCellConst(getCcInit() + value);
	}

	public FxMigHp ccAlignYTop() {
		appendCc("aligny top");
		return this;
	}

	public FxMigHp lgcStInset5Gap00() {
		lgcInsetAndGap(5, 0,0 );
		return this;
	}
}
