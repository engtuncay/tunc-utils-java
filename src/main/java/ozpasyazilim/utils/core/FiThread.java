package ozpasyazilim.utils.core;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import ozpasyazilim.utils.gui.fxcomponents.*;
import ozpasyazilim.utils.listener.CompoundRunnable;
import ozpasyazilim.utils.log.Loghelper;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class FiThread {

    public static Thread startThread(Runnable runnable) {

        Thread thread = new Thread(runnable);
        thread.start();
        return thread;

    }

    public static Thread startThread(Runnable runnable, FxButton... btnArray) {
        return startThread(runnable, btnArray, null);
    }

    public static Thread startThread(Runnable runnable, FiModObserver fiModObserver, FxButton... btnArray) {
        return startThread(runnable, btnArray, null, fiModObserver);
    }


    /**
     * With Tc -> With Try Catch for Runnable
     *
     * @param runnable
     * @param btnArray
     * @return
     */
    public static Thread startThreadWitTc(Runnable runnable, FxButton... btnArray) {
        return startThreadWithTc(runnable, btnArray, null);
    }

    public static Thread startThreadWithTc(Runnable runnable, FxButton[] btnListee, Runnable runnableEnd) {

        FxButton btnListe = btnListee[0];

        final String textOld = btnListe.getText();

        Platform.runLater(() -> {
            btnListe.setText("İşlem Yapılıyor..");
            for (FxButton fxButton : btnListee) {
                fxButton.setDisable(true);
            }
        });

        Runnable runnable1 = () -> {
            try {
                runnable.run();
            } catch (Exception ex) {
                FxDialogShow.showModalErrorAsyn("Hata oluştu", FiException.excToStrSummary(ex));
            }
        };

        Runnable runnable2 = () -> {
            //fxToastPopup2.end();
            Platform.runLater(() -> {
                btnListe.setText(textOld);

                for (FxButton fxButton : btnListee) {
                    fxButton.setDisable(false);
                }
            });

        };

        if (runnableEnd == null) runnableEnd = () -> {
        };

        CompoundRunnable compoundRunnable = new CompoundRunnable(runnable1, runnable2, runnableEnd);

        Thread thread = new Thread(compoundRunnable);
        thread.start();

        return thread;
    }

    public static Thread startThread(Runnable runnable, FxButton[] arrButtons, Runnable runnableEnd) {

        StringBuilder sbTxOld = new StringBuilder();

        if (arrButtons.length != 0 && arrButtons[0] != null) {
            sbTxOld.append(arrButtons[0].getText());
        }

        Supplier<Boolean> supTest = () -> arrButtons.length != 0 && arrButtons[0] != null;

        //FxButton btnListeFirst = arrButtons[0];
        //final String textOld = btnListeFirst.getText();

        Platform.runLater(() -> {

            if (supTest.get()) {
                arrButtons[0].setText("İşlem Yapılıyor..");

                for (FxButton fxButton : arrButtons) {
                    fxButton.setDisable(true);
                }
            }

        });

        Runnable runnable2 = () -> {
            //fxToastPopup2.end();
            Platform.runLater(() -> {

                if (supTest.get()) {
                    arrButtons[0].setText(sbTxOld.toString());

                    for (FxButton fxButton : arrButtons) {
                        fxButton.setDisable(false);
                    }
                }

            });

        };

        if (runnableEnd == null) runnableEnd = () -> {
        };

        CompoundRunnable compoundRunnable = new CompoundRunnable(runnable, runnable2, runnableEnd);

        Thread thread = new Thread(compoundRunnable);
        thread.start();

        return thread;
    }

    public static Thread startThread(Runnable custRunnableMain, FxButton[] arrButtons, Runnable custRunnableEnd, FiModObserver fiModObserver) {

        StringBuilder sbTxOld = new StringBuilder();

        if (arrButtons.length != 0 && arrButtons[0] != null) {
            sbTxOld.append(arrButtons[0].getText());
        }

        Supplier<Boolean> supTest = () -> arrButtons.length != 0 && arrButtons[0] != null;

        //FxButton btnListeFirst = arrButtons[0];
        //final String textOld = btnListeFirst.getText();

        Platform.runLater(() -> {

            if (supTest.get()) {
                arrButtons[0].setText("İşlem Yapılıyor..");

                for (FxButton fxButton : arrButtons) {
                    fxButton.setDisable(true);
                }
            }

        });

        Runnable runnable2 = () -> {
            //fxToastPopup2.end();
            Loghelper.get(FiThread.class).debug("Runnable2-Text düzeltme çalıştı");

            Platform.runLater(() -> {

                if (supTest.get()) {
                    arrButtons[0].setText(sbTxOld.toString());

                    for (FxButton fxButton : arrButtons) {
                        fxButton.setDisable(false);
                    }
                }

            });

        };

        fiModObserver.getObsMethodFinished().add(runnable2);

        if (custRunnableEnd == null) custRunnableEnd = () -> {
        };

        CompoundRunnable compoundRunnable = new CompoundRunnable(custRunnableMain, custRunnableEnd);

        Thread thread = new Thread(compoundRunnable);
        thread.start();

        return thread;
    }


    public static Thread startThreadMb(Runnable runnable, FxMenuButton mbComp, Runnable runnableEnd) {

//		FxMenuButton mbComp = mbComp;

        final String textOld = mbComp.getText();

        Platform.runLater(() -> {
            mbComp.setText("İşlem Yapılıyor..");
            mbComp.setDisable(true);
//			for (FxButton fxButton : mbComp) {
//				fxButton.setDisable(true);
//			}
        });

        Runnable runnable2 = () -> {
            //fxToastPopup2.end();
            Platform.runLater(() -> {
                mbComp.setText(textOld);
                mbComp.setDisable(false);
//				for (FxButton fxButton : mbComp) {
//					fxButton.setDisable(false);
//				}
            });

        };

        if (runnableEnd == null) runnableEnd = () -> {
        };

        CompoundRunnable compoundRunnable = new CompoundRunnable(runnable, runnable2, runnableEnd);

        Thread thread = new Thread(compoundRunnable); // compoundRunnable
        thread.start();
        return thread;

    }


    public static Thread startThread(Runnable runnable, FxButton btnListe, Runnable runnableEnd) {

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

        if (runnableEnd == null) runnableEnd = () -> {
        };

        CompoundRunnable compoundRunnable = new CompoundRunnable(runnable, runnable2, runnableEnd);

        Thread thread = new Thread(compoundRunnable);
        thread.start();
        return thread;
    }

    /**
     * uzun işlemlerde arayüzü kastırıyor, işlem yaptırmıyor
     *
     * @param runnable
     * @param btnComp
     */
    @Deprecated
    public static void startGuiThread(Runnable runnable, FxButton btnComp) {

        final String textOld = btnComp.getText();

        Platform.runLater(() -> {
            //fxToastPopup2.show(finalMessage+ " Hazırlanıyor...",atomicReference.get());
            btnComp.setText("İşlem Yapılıyor..");
            btnComp.setDisable(true);
        });

        Runnable runnable2 = () -> {
            btnComp.setText(textOld);
            btnComp.setDisable(false);
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

    public static Thread startThreadTable(Runnable runnable, FxTableView2 fxTableView2, FxButton btnReport) {

        FxLabel lblNodes = fxTableView2.getFiLblFooterMessage();
        final String textOld = lblNodes.getText();

        Platform.runLater(() -> {
            lblNodes.setText("Rapor çalışıyor...");
            lblNodes.setFxTextColor(Color.RED);

            if (btnReport != null) {
                btnReport.setDisable(true);
            }
            //lblNodes.setFxStyleColor
            //lblNodes.setDisable(true);
        });

        Runnable runnable2 = () -> {
            //fxToastPopup2.end();
            Platform.runLater(() -> {
                lblNodes.setText(textOld);

                if (btnReport != null) {
                    btnReport.setDisable(false);
                }
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
                //lblNodes.setText(textOld);
                lblNodes.setText("");
                //lblNodes.setDisable(false);
                fxTableView2.updatePageToolbarComps();
            });
        };

        CompoundRunnable compoundRunnable = new CompoundRunnable(runnable, runnable2);

        Thread thread = new Thread(compoundRunnable);
        thread.start();
        return thread;
    }

    public static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            //System.err.format("InterruptedException : %s%n", e);
            Loghelper.get(FiThread.class).error(FiException.exTosMain(e));
        }
    }


}
