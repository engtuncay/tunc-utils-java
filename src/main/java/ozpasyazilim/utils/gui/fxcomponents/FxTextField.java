package ozpasyazilim.utils.gui.fxcomponents;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.DateTimeStringConverter;
import javafx.util.converter.IntegerStringConverter;
import ozpasyazilim.utils.core.FiNumber;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.function.UnaryOperator;

public class FxTextField<EntClazz> extends TextField {

	// Görünen Text değeri ile birlikte arka planda farklı bir değer tutmak için
	private StringProperty txValue;  //= new SimpleStringProperty();
	// Belli obje değeri tutmak için
	private ObjectProperty<EntClazz> objValue;  //= new SimpleObjectProperty<>();
	// Text veya value değerinin geçersiz olduğunu göstermek için kullanılır
	private Boolean boInvalidData;
	// Value dışında alternatif bir değer de tutmak için kullanılır.
	private StringProperty txAltValue;


	public FxTextField(String text) {
		super(text);
	}

	public FxTextField() {
	}

	public FxTextField format1() {

		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

		//DecimalFormat formatter = new DecimalFormat("#,###");
		//String newValueStr = formatter.format(Double.parseDouble(newValue));

		try {
			setTextFormatter(new TextFormatter<>(new DateTimeStringConverter(format), format.parse("00:00:00")));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return this;
	}

	public FxTextField convertNumberDoubleTextField1() {

		UnaryOperator<TextFormatter.Change> filter = change -> {

			//String enteredText = change.getText();
			String wholeText = change.getControlNewText();

			//System.out.println("Text:"+ change.getText());
			//System.out.println("Control New Text:"+ change.getControlNewText());
			//System.out.println("Control text:"+ change.getControlText());

			String regex = "^(!?|<?|-?|>?|[0-9]*)[0-9]*[\\.,]{0,1}[0-9]*";
			if (wholeText.matches(regex)) {
				return change;
			}

			return null;
		};

		TextFormatter<String> textFormatter = new TextFormatter<>(filter);
		setTextFormatter(textFormatter);
		setAlignment(Pos.BASELINE_RIGHT);
		return this;
	}

	public FxTextField convertNumberTextField2() {

		UnaryOperator<TextFormatter.Change> integerFilter = change -> {
			String newText = change.getControlNewText();
			// if proposed change results in a valid value, return change as-is:
			if (newText.matches("-?([1-9][0-9]*)?")) {
				return change;
			} else if ("-".equals(change.getText())) {

				// if user types or pastes a "-" in middle of current text,
				// toggle sign of value:

				if (change.getControlText().startsWith("-")) {
					// if we currently start with a "-", remove first character:
					change.setText("");
					change.setRange(0, 1);
					// since we're deleting a character instead of adding one,
					// the caret position needs to move back one, instead of
					// moving forward one, so we modify the proposed change to
					// move the caret two places earlier than the proposed change:
					change.setCaretPosition(change.getCaretPosition() - 2);
					change.setAnchor(change.getAnchor() - 2);
				} else {
					// otherwise just insert at the beginning of the text:
					change.setRange(0, 0);
				}
				return change;
			}
			// invalid change, veto it by returning null:
			return null;
		};

		setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));

		return this;

	}

	public FxTextField activateUpperCaseConverter() {

		UnaryOperator<TextFormatter.Change> filter = change -> {

			String enteredText = change.getText();
			//String wholeText = change.getControlNewText();

			if (enteredText != null) {
				change.setText(change.getText().toUpperCase());
				return change;
			}

			return null;
		};

		TextFormatter<String> textFormatter = new TextFormatter<>(filter);
		setTextFormatter(textFormatter);
		return this;
	}

	public String getTxValue() {
		return txValueProperty().get();
	}

	public StringProperty txValueProperty() {
		if (txValue == null) {
			txValue = new SimpleStringProperty();
		}
		return txValue;
	}

	public void setTxValue(String txValue)  {
		txValueProperty().set(txValue);
	}

	public Double getTextAsDouble() {
		String textValue = getText();
		Double cellvalue = FiNumber.strToDouble(textValue);
		return cellvalue;
	}

	public void onEnterActionFi(Runnable runnableEnter) {

		setOnAction(event -> {
			runnableEnter.run();
		});

//		filterTextField.setOnKeyReleased(event -> {
//			if (event.getCode() == KeyCode.ENTER){
//				// do what is to do
//			}
//		});

	}

	public EntClazz getObjValue() {
		return objValueProperty().get();
	}

	public ObjectProperty<EntClazz> objValueProperty() {
		if (objValue == null) {
			objValue = new SimpleObjectProperty<>();
		}
		return objValue;
	}

	public void setObjValue(EntClazz prmPropObjValue) {
		objValueProperty().set(prmPropObjValue);
	}


	public void setTextAndTxValue(String txPropTxfValue) {
		setTxValue(txPropTxfValue);
		setText(txPropTxfValue);
	}

	public void setEntValue(EntClazz entValue) {
		setObjValue(entValue);
	}

	public Boolean getBoInvalidData() {
		return boInvalidData;
	}

	public void setBoInvalidData(Boolean boInvalidData) {
		this.boInvalidData = boInvalidData;
	}

	public String getTxAltValue() {
		return txAltValueProperty().get();
	}

	public StringProperty txAltValueProperty() {
		if (txAltValue == null) {
			txAltValue = new SimpleStringProperty();
		}
		return txAltValue;
	}

	public void setTxAltValue(String txAltValue) {
		txAltValueProperty().set(txAltValue);
	}


}
