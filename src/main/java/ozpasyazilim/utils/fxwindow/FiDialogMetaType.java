package ozpasyazilim.utils.fxwindow;

public enum FiDialogMetaType {
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
	/**
	 * SimpleDialog value olarak içeriği kullanmış
	 * Deprecated
	 */
	TextAreaString,
	/**
	 * Textarea içeriğini messegaContent olarak kullanılır
	 */
	TextAreaString2,

	LogTable,

	/**
	 * Content alanı dinamil doldurulur
	 */
	CustomContent1,

	Undefined

}
