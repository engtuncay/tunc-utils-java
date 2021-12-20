package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;
import ozpasyazilim.utils.core.FiDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FxDatePickerCore extends DatePicker {

	public FxDatePickerCore() {
		super();
		setDefaultConverter();
	}

	public FxDatePickerCore(Double prefWidth) {
		super();
		setDefaultConverter();
		setPrefWidth(prefWidth);
	}

	public FxDatePickerCore(Date dtValue) {
		super();
		setDefaultConverter();
		setValue(FiDate.convertLocalDate(dtValue));
	}

	public void setTodayDate() {
		this.setValue(FiDate.convertLocalDate(FiDate.getDatewithTimeZero()));
	}

	public void setDefaultConverter() {
		setConverter(getStrConverter());
	}


	public Date getSelectedDate() {
		if (getValue() == null) return null;
		return FiDate.convertLocalDateToSimpleDate(getValue());

	}

	public static StringConverter<LocalDate> getStrConverter() {

		return new StringConverter<LocalDate>() {

			final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
//			final DateTimeFormatter dateFormatter2 = DateTimeFormatter.ofPattern("dd.MM.yyyy");

			@Override
			public String toString(LocalDate date) {
				return (date != null) ? dateFormatter.format(date) : "";
			}

			@Override
			public LocalDate fromString(String value) {

				if (value == null || value.isEmpty()) return null;

				value.trim();

				String txDate = FiDate.convertDateSeparator(value,".");
				String txPattern = FiDate.generateDatePattern(txDate,".");

				//FiConsole.printObjectDefiniton(txDate,"txDate");
				//FiConsole.printObjectDefiniton(txPattern,"txPattern");

				if(txDate==null || txPattern==null) return null;

				final DateTimeFormatter dateFormatterGen = DateTimeFormatter.ofPattern(txPattern);

				//if(txDate.matches(txPattern)){
				try {
					return LocalDate.parse(txDate,dateFormatterGen);
				}catch (Exception ex){
					return null;
				}

				//}

//				if (value.matches("\\d{1,2}\\.\\d{1,2}\\.\\d{2}")) {
//					return LocalDate.parse(value, dateFormatter);
//				}
//
//				if (value.matches("\\d{1,2}\\.\\d{1,2}\\.\\d{4}")) {
//					return LocalDate.parse(value, dateFormatter2);
//				}
//
//				if (value.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
//					return LocalDate.parse(value, dateFormatter3);
//				}
//
//				if (value.matches("\\d{1,2}/\\d{1,2}/\\d{2}")) {
//					return LocalDate.parse(value, dateFormatter4);
//				}
//
//				if (value.matches("\\d{1,2},\\d{1,2},\\d{4}")) {
//					return LocalDate.parse(value, dateFormatter5);
//				}
//
//				if (value.matches("\\d{1,2},\\d{1,2},\\d{2}")) {
//					return LocalDate.parse(value, dateFormatter6);
//				}
//
//				if (value.matches("\\d{1,2}\\-\\d{1,2}\\-\\d{4}")) {
//					return LocalDate.parse(value, dateFormatter7);
//				}
//
//				if (value.matches("\\d{1,2}\\-\\d{1,2}\\-\\d{2}")) {
//					return LocalDate.parse(value, dateFormatter8);
//				}

				//return null; //LocalDate.parse(value, dateFormatter);
			}
		};
	}

	public void setIfNull(LocalDate value) {
		if(getValue()==null){
			setValue(value);
		}
	}
}
