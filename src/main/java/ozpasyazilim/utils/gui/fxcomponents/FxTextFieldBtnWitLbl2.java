package ozpasyazilim.utils.gui.fxcomponents;

import ozpasyazilim.utils.table.OzColType;

public class FxTextFieldBtnWitLbl2<EntClazz> extends FxTextFieldBtn<EntClazz> implements IFiNode {

	OzColType compValueType;

	public FxTextFieldBtnWitLbl2() {
		setup(null);
	}

	public FxTextFieldBtnWitLbl2(Long lnDurationChange){
		setup(lnDurationChange);
	}

	public void setup(Long lnDurationChange) {
		addLabelBelowRow();
		activateChangeEvent(lnDurationChange);
	}

	@Override
	public void setCompValueType(OzColType compValueType) {
		this.compValueType = compValueType;
	}

	@Override
	public Object getCompValueByColType() {
		return null;
	}

//	public FxLabel getLblAciklama() {return lblAciklama;}
//
//	public Consumer<String> getFnLblDataChange() {
//		return fnLblDataChange;
//	}

//	public void setFnLblDataChange(Consumer<String> fnLblDataChange) {
//		this.fnLblDataChange = fnLblDataChange;
//	}

//	public Long getLnDurationChangeDefaultVal() {
//		if (lnDurationChange == null) {
//			return 500l;
//		}
//		return lnDurationChange;
//	}

//	public void setLnDurationChange(Long lnDurationChange) {
//		this.lnDurationChange = lnDurationChange;
//	}

//	public void setLblText(String txValue) {
//		getLblAciklama().setText(txValue);
//	}

}
