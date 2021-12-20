package ozpasyazilim.utils.gui.fxcomponents;

public class FxTextFieldBtnLabel2<EntClazz> extends FxTextFieldBtn<EntClazz> implements IfxNode {

//	FxLabel lblAciklama;
//	Consumer<String> fnLblDataChange;
//	Long lnDurationChange;

	public FxTextFieldBtnLabel2() {
		setup(null);
	}

	public FxTextFieldBtnLabel2(Long lnDurationChange){
		setup(lnDurationChange);
	}

	public void setup(Long lnDurationChange) {
		addLabelBelowRow();
		activateChangeEvent(lnDurationChange);
//		setLnDurationChange(lnDurationChange);
//		lblAciklama = new FxLabel("");
//		fiWrap();
//		add(lblAciklama, "growx,pushx");
//		FiNodeFx.registerTextPropertyWithDurationForFxTextfield((s) -> {
//			if (getFnLblDataChange()!=null) {
//				getFnLblDataChange().accept(s);
//			}
//		},getLnDurationChange(),getFxTextField());
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
