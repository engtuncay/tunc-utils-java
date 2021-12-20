package ozpasyazilim.utils.javafx;

import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.reactfx.EventStreams;
import ozpasyazilim.utils.gui.fxcomponents.FxDatePicker;
import ozpasyazilim.utils.gui.fxcomponents.FxTextField;
import ozpasyazilim.utils.gui.fxcomponents.FxTextFieldBtn;

import java.time.Duration;
import java.util.function.Consumer;

public class FiNodeFx {

	public static void registerTextPropertyWithDurationForNode(Consumer<String> consumerStrProp, long durationMilis, String colNodeClass, Node node) {

		if (colNodeClass.equals(FxTextField.class.getName())) {
			FxTextField comp = (FxTextField) node;

			EventStreams.valuesOf(comp.textProperty())
					.successionEnds(Duration.ofMillis(durationMilis))
					.subscribe(s -> consumerStrProp.accept(s));

			//comp.textProperty().addListener(consumerStrProp);
		}

		if (colNodeClass.equals(FxTextFieldBtn.class.getName())) {
			FxTextFieldBtn comp = (FxTextFieldBtn) node;
			//comp.getFxTextField().textProperty().addListener(consumerStrProp);

			EventStreams.valuesOf(comp.getFxTextField().textProperty())
					.successionEnds(Duration.ofMillis(durationMilis))
					.subscribe(s -> consumerStrProp.accept(s));
		}

		if (colNodeClass.equals(FxDatePicker.class.getName())) {
			FxDatePicker comp = (FxDatePicker) node;
			//comp.valueProperty().addListener(consumerStrProp);
			//comp.getEditor().textProperty().addListener(consumerStrProp);

			EventStreams.valuesOf(comp.getEditor().textProperty())
					.successionEnds(Duration.ofMillis(durationMilis))
					.subscribe(s -> consumerStrProp.accept(s));

		}

		if (colNodeClass.equals(DatePicker.class.getName())) {
			DatePicker comp = (DatePicker) node;
			//comp.promptTextProperty().addListener(consumerStrProp);
			//comp.getEditor().textProperty().addListener(consumerStrProp);

			EventStreams.valuesOf(comp.getEditor().textProperty())
					.successionEnds(Duration.ofMillis(durationMilis))
					.subscribe(s -> consumerStrProp.accept(s));
		}

		if (colNodeClass.equals(TextField.class.getName())) {
			TextField comp = (TextField) node;
			//comp.textProperty().addListener(consumerStrProp);

			EventStreams.valuesOf(comp.textProperty())
					.successionEnds(Duration.ofMillis(durationMilis))
					.subscribe(s -> consumerStrProp.accept(s));

		}
	}

	public static void registerTextPropertyWithDurationForFxTextfield(Consumer<String> consumerStrProp, long durationMilis, FxTextField node) {
		EventStreams.valuesOf(node.textProperty())
				.successionEnds(Duration.ofMillis(durationMilis))
				.subscribe(s -> consumerStrProp.accept(s));
	}
}
