package ozpasyazilim.utils.fxwindow;

public enum FxSimpleDialogMetaType {
	TextFieldDouble, TextFieldInteger, InfoTextFlowDialog, TextField, InfoLabelDialog,

	/**
	 * Class ın Cand Id field larından bir form oluşturur otomatik.
	 */
	FormAutoByCandIdFields,
	DialogError, DialogInfo, TextFieldWithValidation,
	ErrorDialogDetailed,

	/**
	 * FxForm üzerinden dialog oluşturur
	 */
	FormDialog,
	TextAreaString,

	LogTable,

	Undefined
}
