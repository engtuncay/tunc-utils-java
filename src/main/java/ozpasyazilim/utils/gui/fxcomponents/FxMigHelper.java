package ozpasyazilim.utils.gui.fxcomponents;

import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.table.OzColType;

public class FxMigHelper {

	private String layConst; // lc
	private String rowConst; // lrc
	private String colConst; // lcc
	private String cellConst; //cc

	public static Boolean debugMode = false;

	// gap x y : x yatatya boşluk : y dikeyde boşluk
	public static String lcStandard1InsetZeroGap50 = "insets 0,gap 5 0";
	public static String lcStandard1InsetZeroGap55 = "insets 0,gap 5 5";
	public static String lcStandard1InsetZeroGap00 = "insets 0,gap 0 0";
	public static String lcStandard2WithInset3 = "insets 3,gap 3 3";
	public static String lcStandard2WithInset5Gap55 = "insets 5,gap 5 5";
	public static String lcStandard2WithInset5Gap00 = "insets 5,gap 0 0";
	public static String lcStandard1InsetZeroGap5555 = "insets 0,gap 5 5 5 5";

	public static FxMigHelper bui() {
		return new FxMigHelper();
	}

	public static FxMigHelper bcc(String cc) {
		FxMigHelper fxMigHelper = new FxMigHelper();
		fxMigHelper.setCellConst(cc);
		return fxMigHelper;
	}

	public static FxMigHelper blc(String lc) {
		FxMigHelper fxMigHelper = new FxMigHelper();
		fxMigHelper.setLayConst(lc);
		return fxMigHelper;
	}

	public static FxMigHelper blc(String lc, String lrc, String lcc) {
		FxMigHelper fxMigHelper = new FxMigHelper();
		fxMigHelper.setLayConst(lc);
		fxMigHelper.setRowConst(lrc);
		fxMigHelper.setColConst(lcc);
		return fxMigHelper;
	}

	public FxMigHelper ccWidth(String txWidth) {
		addSeperatorCc();
		cellConst += "w " + txWidth;
		return this;
	}

	public FxMigHelper lcInsetAndGap(Integer inset, Integer gapx, Integer gapy) {
		addCommaLcPrev();
		this.layConst += String.format("insets %s,gap %s %s", inset, gapx, gapy);
		return this;
	}

	public FxMigHelper lcNoGrid() {
		addCommaLcPrev();
		this.layConst += "nogrid";
		return this;
	}

	public FxMigHelper lcAlignx(Integer percent) {
		addCommaLcPrev();
		this.layConst += "ax " + percent + "%";
		return this;
	}

	public FxMigHelper lcStInset0Gap55() {
		lcInsetAndGap(0, 5, 5);
		return this;
	}

	public FxMigHelper lcStInset3() {
		lcInsetAndGap(3, 3, 3);
		return this;
	}

	public FxMigHelper lcStInset3Gap0() {
		lcInsetAndGap(3, 0, 0);
		return this;
	}

	private void addCommaLcPrev() {
		if (FiString.isEmpty(getLayConst())) return;
		this.layConst += ",";
	}

	private void addSeperatorCc() {
		if (FiString.isEmpty(getCellConst())) return;
		this.cellConst += ",";
	}

	public String getLayConst() {
		if (layConst == null) {
			layConst = "";
		}
		return layConst;
	}

	public String getRowConst() {
		if (rowConst == null) {
			rowConst = "";
		}
		return rowConst;
	}

	public String getColConst() {
		if (colConst == null) {
			colConst = "";
		}
		return colConst;
	}

	/**
	 * Layout Constraint
	 * <p>
	 * MigPane constructor na yazılır  (Lc,Lrc,Lcc)
	 *
	 * @return
	 */
	public String genLc() {

		if (layConst == null) {
			layConst = "";
		}

		if (debugMode) {
			if (!layConst.contains("debug")) {
				addCommaLcPrev();
				layConst += "debug";
			}
		}

		return layConst;
	}

	/**
	 * Layout Row Constraints
	 *
	 * @return
	 */
	public String genLrc() {
		return rowConst;
	}

	/**
	 * Layout Column Constraint
	 *
	 * @return
	 */
	public String genLcc() {
		return colConst;
	}

	/**
	 * Component constraint
	 *
	 * @return
	 */
	public String genCc() {
		return cellConst;
	}

	public String getCellConst() {
		if (cellConst == null) {
			cellConst = "";
		}
		return cellConst;
	}

	public FxMigHelper lcDebug() {
		addCommaLcPrev();
		this.layConst += "debug";
		return this;
	}

	public void setLayConst(String layConst) {
		this.layConst = layConst;
	}

	public void setRowConst(String rowConst) {
		this.rowConst = rowConst;
	}

	public void setColConst(String colConst) {
		this.colConst = colConst;
	}

	public void setCellConst(String cellConst) {
		this.cellConst = cellConst;
	}

	public FxMigHelper addCcCompMaxWidthSizeByColType(IFiCol iFiTableCol) {

		if (iFiTableCol.getColType() == OzColType.Date || iFiTableCol.getColEditorClass().equals(FxDatePicker.class.getName())) {
			appendCc("wmax 150");
		}

		if (iFiTableCol.getColType() == OzColType.Double) {
			appendCc("wmax 200");
		}

		if (iFiTableCol.getColType() == OzColType.Integer) {
			appendCc("wmax 150");
		}

		return this;
	}

	private void appendCc(String value) {
		addSeperatorCc();
		this.cellConst += value;
	}





}
