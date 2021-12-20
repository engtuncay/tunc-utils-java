package ozpasyazilim.utils.gui.fxcomponents;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.util.Callback;

import java.util.function.Supplier;

public class FxDialog<T> extends Dialog<T> {

	String title;
	private String ftextOk="Ok";
	private ButtonType okButtonType;
	private ButtonType cancelButtonType;
	private String fTextCancel="İptal";

	public FxDialog() {
	}

	public FxDialog<T> FxDialog(String title) {
		return this;
	}

	public static <T> FxDialog build() {
		return new FxDialog<T>();
	}

	public void closeDialog() {
		this.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
		// Dialog will now close
		this.close();
	}

	public void AddOkAndCancelButton(String textOk, String textCancel) {

		if (textOk != null) ftextOk = textOk;
		if (textCancel != null) fTextCancel = textCancel;

		// Dialog alt buttonları eklenir
		this.getDialogPane().getButtonTypes().addAll(getOkButtonType(), getCancelButtonType());


	}

	public void AddOkButton(String textOk) {

		if (textOk != null) ftextOk = textOk;
		//if (textCancel != null) fTextCancel = textCancel;

		// Dialog alt buttonları eklenir
		this.getDialogPane().getButtonTypes().addAll(getOkButtonType());


	}


	public void setContentPane(Node node) {
		getDialogPane().setContent(node);
	}

	public void setFilterToOkButton(Supplier<Boolean> isValidTest) {
		// Enable/Disable login button depending on whether a username was entered.
		//ButtonType loginButtonType = new ButtonType(ftextOk, ButtonBar.ButtonData.OK_DONE);
		Button okButton = (Button) this.getDialogPane().lookupButton(okButtonType);
		//okButton.setDisable(true);

		okButton.addEventFilter(ActionEvent.ACTION, ae -> {
			if (!isValidTest.get()) ae.consume(); //not valid
		});

	}

	public void setResultCallback(Callback<ButtonType, T> callBack) {

		this.setResultConverter(callBack);

		/*dialogButton -> {
			if (dialogButton == loginButtonType) {

				if(belgesira.getText().equalsIgnoreCase("")) return null;
				if(evraktipid.getText().equalsIgnoreCase("")) return null;

				Integer nmBelgeSira= Integer.parseInt(belgesira.getText());

				Integer nmEvrakTipNo = Integer.parseInt(evraktipid.getText());

				return new TblBelgeno(belgeSeri.getText(), nmBelgeSira,nmEvrakTipNo);
			}
			return null;
		}*/

	}

	public ButtonType getOkButtonType() {

		if(okButtonType==null) okButtonType = new ButtonType(ftextOk, ButtonBar.ButtonData.OK_DONE);

		return okButtonType;
	}

	public ButtonType getCancelButtonType() {
		if(cancelButtonType==null) cancelButtonType = new ButtonType(fTextCancel,ButtonBar.ButtonData.CANCEL_CLOSE);
		return cancelButtonType;
	}


}
