package ozpasyazilim.utils.core;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import ozpasyazilim.utils.gui.fxcomponents.FxButton;
import ozpasyazilim.utils.gui.fxcomponents.FxLabel;
import ozpasyazilim.utils.gui.fxcomponents.FxTableView2;
import ozpasyazilim.utils.listener.CompoundRunnable;

public class FiThread {


	public static Thread startThread(Runnable runnable) {

		Thread thread = new Thread(runnable);
		thread.start();
		return thread;

	}

	public static Thread startThread(Runnable runnable, FxButton ...btnArray) {
		return startThread(runnable, btnArray, null);
	}

	public static Thread startThread(Runnable runnable, FxButton[] btnListee,Runnable runnableEnd) {

		FxButton btnListe = btnListee[0];

		final String textOld = btnListe.getText();

		Platform.runLater(() -> {
			btnListe.setText("İşlem Yapılıyor..");
			for (FxButton fxButton : btnListee) {
				fxButton.setDisable(true);
			}
		});

		Runnable runnable2 = () -> {
			//fxToastPopup2.end();
			Platform.runLater(() -> {
				btnListe.setText(textOld);

				for (FxButton fxButton : btnListee) {
					fxButton.setDisable(false);
				}
			});

		};

		if(runnableEnd==null) runnableEnd = ()->{};

		CompoundRunnable compoundRunnable = new CompoundRunnable(runnable, runnable2,runnableEnd);

		Thread thread = new Thread(compoundRunnable);
		thread.start();
		return thread;

	}



	public static Thread startThread(Runnable runnable, FxButton btnListe,Runnable runnableEnd) {

		final String textOld = btnListe.getText();

		Platform.runLater(() -> {
			btnListe.setText("İşlem Yapılıyor..");
			btnListe.setDisable(true);
		});

		Runnable runnable2 = () -> {
			//fxToastPopup2.end();
			Platform.runLater(() -> {
				btnListe.setText(textOld);
				btnListe.setDisable(false);
			});

		};

		if(runnableEnd==null) runnableEnd = ()->{};

		CompoundRunnable compoundRunnable = new CompoundRunnable(runnable, runnable2,runnableEnd);

		Thread thread = new Thread(compoundRunnable);
		thread.start();
		return thread;
	}

	public static void startGuiThread(Runnable runnable, FxButton btnListe) {

		final String textOld = btnListe.getText();

		Platform.runLater(() -> {
			//fxToastPopup2.show(finalMessage+ " Hazırlanıyor...",atomicReference.get());
			btnListe.setText("İşlem Yapılıyor..");
			btnListe.setDisable(true);
		});

		Runnable runnable2 = () -> {
			btnListe.setText(textOld);
			btnListe.setDisable(false);
		};

		CompoundRunnable compoundRunnable = new CompoundRunnable(runnable, runnable2);

		Platform.runLater(compoundRunnable);

	}

	public static void gui(Runnable runnable) {
		Platform.runLater(runnable);
	}


	public static Thread startThread2(Runnable runnable, FxLabel lblNode) {
		return startThread2(runnable, lblNode, null);
	}

	public static Thread startThread2Filtre(Runnable runnable, FxLabel lblNode) {
		return startThread2(runnable, lblNode, "Filtre Çalışıyor...");
	}

	public static Thread startThread2(Runnable runnable, FxLabel lblNode, String message) {

		//FxToastPopup2 fxToastPopup2 = new FxToastPopup2();
		//		if(message==null) message = "";
		//		String finalMessage = message;

		final String textOld = lblNode.getText();

		Platform.runLater(() -> {
			//fxToastPopup2.show(finalMessage+ " Hazırlanıyor...",atomicReference.get());
			String lblText = message;
			if (lblText == null) lblText = "Veriler Bekleniyor...";
			lblNode.setText(lblText);
			lblNode.setFxTextColor(Color.RED);

			//lblNode.setFxStyleColor
			//lblNode.setDisable(true);
		});

		Runnable runnable2 = () -> {
			//fxToastPopup2.end();
			Platform.runLater(() -> {
				lblNode.setText(textOld);
				//lblNode.setDisable(false);
			});

		};

		CompoundRunnable compoundRunnable = new CompoundRunnable(runnable, runnable2);

		Thread thread = new Thread(compoundRunnable);
		thread.start();
		return thread;
	}

	public static void startGuiThreadByLabelInfo(Runnable runnable, FxLabel lblNode, String textForLabel) {

		Platform.runLater(() -> {
			final String textOld = lblNode.getText();
			//fxToastPopup2.show(finalMessage+ " Hazırlanıyor...",atomicReference.get());
			String lblText = textForLabel;
			if (FiString.isEmpty(lblText)) lblText = "Veriler Bekleniyor...";
			lblNode.setText(lblText);
			lblNode.setFxTextColor(Color.RED);

			runnable.run();

			lblNode.setText(textOld);
			//lblNode.setFxStyleColor
			//lblNode.setDisable(true);
		});

	}

	public static void startGuiThread(Runnable runnable) {
		Platform.runLater(runnable);
	}

	public static Thread startThreadTable(Runnable runnable, FxTableView2 fxTableView2) {

		FxLabel lblNodes = fxTableView2.getFiLblFooterMessage();
		final String textOld = lblNodes.getText();

		Platform.runLater(() -> {
			lblNodes.setText("Filtre Çalışıyor...");
			lblNodes.setFxTextColor(Color.RED);
			//lblNodes.setFxStyleColor
			//lblNodes.setDisable(true);
		});

		Runnable runnable2 = () -> {
			//fxToastPopup2.end();
			Platform.runLater(() -> {
				lblNodes.setText(textOld);
				//lblNodes.setDisable(false);
			});

		};

		CompoundRunnable compoundRunnable = new CompoundRunnable(runnable, runnable2);

		Thread thread = new Thread(compoundRunnable);
		thread.start();
		return thread;
	}

	public static Thread startThreadForPaging(Runnable runnable, FxTableView2 fxTableView2) {

		//FxToastPopup2 fxToastPopup2 = new FxToastPopup2();
		//		if(message==null) message = "";
		//		String finalMessage = message;

		FxLabel lblNodes = fxTableView2.getFiLblFooterMessage();

		final String textOld = lblNodes.getText();

		Platform.runLater(() -> {
			//fxToastPopup2.show(finalMessage+ " Hazırlanıyor...",atomicReference.get());
			lblNodes.setText("Veriler alınıyor.");
			lblNodes.setFxTextColor(Color.RED);

			fxTableView2.setPagingButtonsDisable(true);
			//lblNodes.setFxStyleColor
			//lblNodes.setDisable(true);
		});

		Runnable runnable2 = () -> {
			//fxToastPopup2.end();
			Platform.runLater(() -> {
				lblNodes.setText(textOld);
				//lblNodes.setDisable(false);
				fxTableView2.updatePageToolbar();
			});

		};

		CompoundRunnable compoundRunnable = new CompoundRunnable(runnable, runnable2);

		Thread thread = new Thread(compoundRunnable);
		thread.start();
		return thread;
	}


}
