package ozpasyazilim.utils.gui.fxcomponents;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;
import org.controlsfx.control.Notifications;
import org.tbee.javafx.scene.layout.MigPane;
import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.fidborm.QueryType;
import ozpasyazilim.utils.mvc.*;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.returntypes.FnResult;
import ozpasyazilim.utils.fxwindow.FxSimpleDialog;
import ozpasyazilim.utils.fxwindow.FxSimpleDialog2;
import ozpasyazilim.utils.fxwindow.FxSimpleDialogMetaType;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Utility JavaFx Dialog Class
 * <p>
 * Pop Notifications (static)
 */
public class FxDialogShow {

    public static String titleGeneral;

    public static FxDialogShow build() {
        return new FxDialogShow();
    }

    /**
     * Yes,No,Cancel Dialogu açar <br>
     * <p>
     * BoResult Değeri <br>
     * Yes : True <br>
     * No : False <br>
     * İptal : null <br>
     *
     * <br> 09-08-2019
     *
     * @param message
     * @return FnResult objesini döner
     */
    public static FnResult showPromptDialogYesNo(String message) {
        return showPromptDialogYesNo(message, null);
    }

    public static Fdr showPromptDialogYesNoFdr(String message, String title) {

        FxMigPane migPane = new FxMigPane();
        migPane.add(new Label(message), "span");

        MigPane migFooter = new MigPane();

        FxButton btnYes = new FxButton("Evet");
        FxButton btnNo = new FxButton("Hayır");
        FxButton btnCancel = new FxButton("İptal");

        migFooter.add(btnYes, "gapafter 10");
        migFooter.add(btnNo, "gapafter 10");
        migFooter.add(btnCancel, "gapafter 10");

        migPane.add(migFooter, "span");

        FxStage stage = new FxStage();
        FxScene scene = new FxScene(migPane);

        if (title == null && titleGeneral != null) stage.setTitle(titleGeneral);
        if (title != null) stage.setTitle(title);

        Fdr fdr = new Fdr();

        btnYes.setOnAction(event -> {
            fdr.setBoResult(true);
            stage.close();
        });

        btnNo.setOnAction(event -> {
            fdr.setBoResult(false);
            stage.close();
        });

        btnCancel.setOnAction(event -> {
            fdr.setBoResult(null);
            stage.close();
        });

//			if(parentNode!=null){
//				Bounds bounds = parentNode.localToScreen(parentNode.getBoundsInLocal());
//				stage.setX(bounds.getMaxX());
//				stage.setY(bounds.getMinY());
//			}

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.showAndWait();
        return fdr;
    }


    public static Fdr showPromptDialogYesNoFdrNonModal(String message, String title) {

        FxMigPane migPane = new FxMigPane();
        migPane.add(new Label(message), "span");

        MigPane migFooter = new MigPane();

        FxButton btnYes = new FxButton("Evet");
        FxButton btnNo = new FxButton("Hayır");
        FxButton btnCancel = new FxButton("İptal");

        migFooter.add(btnYes, "gapafter 10");
        migFooter.add(btnNo, "gapafter 10");
        migFooter.add(btnCancel, "gapafter 10");

        migPane.add(migFooter, "span");

        FxStage stage = new FxStage();
        FxScene scene = new FxScene(migPane);

        if (title == null && titleGeneral != null) stage.setTitle(titleGeneral);
        if (title != null) stage.setTitle(title);

        Fdr fdr = new Fdr();

        btnYes.setOnAction(event -> {
            fdr.setBoResult(true);
            stage.close();
        });

        btnNo.setOnAction(event -> {
            fdr.setBoResult(false);
            stage.close();
        });

        btnCancel.setOnAction(event -> {
            fdr.setBoResult(null);
            stage.close();
        });

//			if(parentNode!=null){
//				Bounds bounds = parentNode.localToScreen(parentNode.getBoundsInLocal());
//				stage.setX(bounds.getMaxX());
//				stage.setY(bounds.getMinY());
//			}

        stage.initModality(Modality.NONE);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.showAndWait();
        return fdr;
    }

    public static Fdr showPromptDialogDetailedYesNoFdrNonModal(String message, String title, String detail) {

        FxMigPane migPane = new FxMigPane();
        migPane.add(new Label(message), "span");

        FxMigPane migDetail = new FxMigPane();

        if (!FiString.isEmptyTrim(detail)) {
            FxTextArea fxTextArea = new FxTextArea(detail);
            fxTextArea.setPrefHeight(150d);
            migDetail.add(fxTextArea, "span");
        }

        MigPane migFooter = new MigPane();

        FxButton btnYes = new FxButton("Evet");
        FxButton btnNo = new FxButton("Hayır");
        FxButton btnCancel = new FxButton("İptal");

        migFooter.add(btnYes, "gapafter 10");
        migFooter.add(btnNo, "gapafter 10");
        migFooter.add(btnCancel, "gapafter 10");

        migPane.add(migDetail, "span");
        migPane.add(migFooter, "span");

        FxStage stage = new FxStage();
        FxScene scene = new FxScene(migPane);

        if (title == null && titleGeneral != null) stage.setTitle(titleGeneral);
        if (title != null) stage.setTitle(title);

        Fdr fdr = new Fdr();

        btnYes.setOnAction(event -> {
            fdr.setBoResult(true);
            stage.close();
        });

        btnNo.setOnAction(event -> {
            fdr.setBoResult(false);
            stage.close();
        });

        btnCancel.setOnAction(event -> {
            fdr.setBoResult(null);
            stage.close();
        });

//			if(parentNode!=null){
//				Bounds bounds = parentNode.localToScreen(parentNode.getBoundsInLocal());
//				stage.setX(bounds.getMaxX());
//				stage.setY(bounds.getMinY());
//			}

        stage.initModality(Modality.NONE);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.showAndWait();
        return fdr;
    }

    // 09-08-2019
    public static FnResult showPromptDialogYesNo(String message, String title) {

        //Platform.runLater(() -> {

        // stage:pencere çerçevesi
//		FxStage stage = new FxStage();
//
//		FxStackMigPane viewContainer = new FxStackMigPane();
//
//		FxScene scene = new FxScene(viewContainer.getRootPane());  //,width,height

        FxMigPane migPane = new FxMigPane();
        migPane.add(new Label(message), "span");

        MigPane migFooter = new MigPane();

        FxButton btnYes = new FxButton("Evet");
        FxButton btnNo = new FxButton("Hayır");
        FxButton btnCancel = new FxButton("İptal");


        migFooter.add(btnYes, "gapafter 10");
        migFooter.add(btnNo, "gapafter 10");
        migFooter.add(btnCancel, "gapafter 10");

        migPane.add(migFooter, "span");

        FxStage stage = new FxStage();
        FxScene scene = new FxScene(migPane);

        if (title == null && titleGeneral != null) stage.setTitle(titleGeneral);
        if (title != null) stage.setTitle(title);

        FnResult fnResult = new FnResult();

        btnYes.setOnAction(event -> {
            fnResult.setBoResult(true);
            stage.close();

        });

        btnNo.setOnAction(event -> {
            fnResult.setBoResult(false);
            stage.close();
        });

        btnCancel.setOnAction(event -> {
            fnResult.setBoResult(null);
            stage.close();
        });

//			if(parentNode!=null){
//				Bounds bounds = parentNode.localToScreen(parentNode.getBoundsInLocal());
//				stage.setX(bounds.getMaxX());
//				stage.setY(bounds.getMinY());
//			}

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.showAndWait();
        return fnResult;

    }

    // 22-12-2020
    public static Fdr showPromptDialogTwoChoice(String message, String title, String choice1, String choice2) {

        FxMigPane migPane = new FxMigPane();
        migPane.add(new Label(message), "span");

        MigPane migFooter = new MigPane();

        FxButton btnYes = new FxButton(FiString.orEmpty(choice1));
        FxButton btnNo = new FxButton(FiString.orEmpty(choice2));
        FxButton btnCancel = new FxButton("İptal");

        migFooter.add(btnYes, "gapafter 10");
        migFooter.add(btnNo, "gapafter 10");
        migFooter.add(btnCancel, "gapafter 10");

        migPane.add(migFooter, "span");

        FxStage stage = new FxStage();
        FxScene scene = new FxScene(migPane);

        if (title == null && titleGeneral != null) stage.setTitle(titleGeneral);
        if (title != null) stage.setTitle(title);

        Fdr fnResult = new Fdr();

        btnYes.setOnAction(event -> {
            fnResult.setBoResult(true);
            stage.close();
        });

        btnNo.setOnAction(event -> {
            fnResult.setBoResult(false);
            stage.close();
        });

        btnCancel.setOnAction(event -> {
            fnResult.setBoResult(null);
            stage.close();
        });

//			if(parentNode!=null){
//				Bounds bounds = parentNode.localToScreen(parentNode.getBoundsInLocal());
//				stage.setX(bounds.getMaxX());
//				stage.setY(bounds.getMinY());
//			}

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.showAndWait();
        return fnResult;
    }


    public static int showErrorAndGetDbResultForOptionalInt1(Fdr<Optional<Integer>> fdr) {

        if (!fdr.getValue().isPresent()) {
            FxDialogShow.showPopError("Kontrol Yapılırken Hata oluştu.Sistem Yöneticisine başvurun");
            return -1;
        }

        return fdr.getValue().get();
    }

    public static void showDbResultAsErrorIfFail(Fdr fdr) {

        if (!fdr.isTrueBoResult()) {
            if (fdr.isFalseBoResult()) FxDialogShow.showModalErrorAsyn("Hata oluştu", fdr.getMessage());

            if (fdr.getBoResult() == null) FxDialogShow.showModalErrorAsyn("İşlem sonucu alınamadı.", fdr.getMessage());
        }

    }

    public static Boolean showDbResultIfFailAsPopWarn(Fdr fdr) {
        if (!fdr.isTrueBoResult()) {
            FxDialogShow.showPopWarn(fdr.getMessage());
            return false;
        }
        return true;
    }

    public static void showModalForLogIfNotTrue(Fdr fdr) {

        if (fdr.isTrueBoResult()) {
            FxDialogShow.showModalInfoAsyn(fdr.getTxName(), fdr.getLogAsStringWitErrorInfo());
        } else {
            FxDialogShow.showModalErrorAsyn(fdr.getTxName(), fdr.getLogAsStringWitErrorInfo());
        }

    }

    public static void showModalForLog(Fdr fdr) {
        Platform.runLater(() -> {
            FxSimpleDialog2.creInitWithMessageContent(fdr.getLogAsString())
                    .initDialogError().openAsNonModal();
        });
        //FxDialogShow.showModalError2(fdr.getTxName(), fdr.getLogAsStringWitErrorInfo());
    }

    public static void showFdr1PopOrFailModal(Fdr fdr) {

        if (fdr.getBoResult() == null) {
            FxDialogShow.showPopInfo("İşlem sonucu alınamadı." + "\n" + FiString.orEmpty(fdr.getMessage()));
            return;
        }

        if (fdr.isTrueBoResult()) {
            FxDialogShow.showPopInfo("İşlem Başarı ile Gerçekleşti" + "\n" + FiString.orEmpty(fdr.getMessage()));
            return;
        }

        FxDialogShow.showModalErrorAsyn("Hata Oluştu", fdr.getMessage());
    }

    public static void showFdr1PopOrFailModal2(Fdr fdr) {

        if (fdr.getBoResult() == null) {
            FxDialogShow.showPopInfo("İşlem sonucu alınamadı." + "\n" + FiString.orEmpty(fdr.getMessage()));
            return;
        }

        if (fdr.isTrueBoResult()) {
            FxDialogShow.showPopInfo("İşlem Başarı ile Gerçekleşti" + "\n" + FiString.orEmpty(fdr.getMessage()));
            return;
        }

        FxDialogShow.showSimModalError2("", fdr);
    }

    /**
     * TextArea da Log , Mesaj ve Özet Exception bilgilerini verir
     *
     * @param fdr
     */
    public static void showFdr1PopOrFailModalWitLogAndMessageAndExc(Fdr fdr) {

        if (fdr.getBoResult() == null) {
            //FxDialogShow.showPopInfo("İşlem sonucu alınamadı." + "\n" + FiString.orEmpty(fdr.getMessage()));
            FxDialogShow.showModalErrorWitLogAndMessageAndExc("İşlem Sonucu Alınamadı.", fdr);
            return;
        }

        if (fdr.isTrueBoResult()) {
            FxDialogShow.showPopInfo("İşlem Başarı ile Gerçekleşti" + "\n" + FiString.orEmpty(fdr.getMessage()));
            return;
        }

        FxDialogShow.showModalErrorWitLogAndMessageAndExc("Hata !!!", fdr);
    }


    public void showOkCancelDialog(String title, String headerText, Consumer<FxDialog> dialogOkMethod) {

        FxDialog fxDialog = new FxDialog();

        fxDialog.setTitle(title);
        fxDialog.setHeaderText(headerText);
        fxDialog.AddOkAndCancelButton(null, null);

		/*fxDialog.setFilterToOkButton(()->{
			//if(!result) lblmessage.setText("Hata oluştu");
			//return result;
		});*/

        fxDialog.setContentPane(null);

        fxDialog.setResultConverter(dialogButton -> {

            if (dialogButton == fxDialog.getOkButtonType()) {
                dialogOkMethod.accept(fxDialog);
            }
            return null;
        });

		/*Optional<ComboItem> result = fxDialog.showAndWait();

		result.ifPresent(strSirket -> {

			AppPropertiesImpl.setStrSirket(strSirket.getValue());
			Loghelper.getInstance(getClass()).debug(" Str Sirket Güncellendi:"+ strSirket.getValue());
			//Notifications.create().title("Özpaş Entegre").text("Şirket Değiştirildi:"+ strSirket.getLabel()).showInformation();
			//changeWorkspace.setText("Şirket: "+ AppPropertiesImpl.getComboSirket().getLabel());
			//buildButtonMenuRefresh();
			new UserProp().saveUpdateProp("sirket", strSirket.getValue());
			Loghelperr.getInstance(getClass()).debug("sirket prop Güncellendi");
			//tabMain.getTabs().clear();

		});*/

        fxDialog.showAndWait();

    }

    public void showOkDialog(Node nodeContent, String title, String headerText) {

        FxDialog fxDialog = new FxDialog();

        fxDialog.setTitle(title);
        fxDialog.setHeaderText(headerText);
        fxDialog.AddOkButton(null);


        //MigPane rolePane = getChangeWsPane(fxDialog);

        //Label lblmessage = (Label) rolePane.lookup("#mesajchws");

		/*fxDialog.setFilterToOkButton(()->{



			//if(!result) lblmessage.setText("Hata oluştu");
			//return result;
		});*/

        fxDialog.setContentPane(nodeContent);

        fxDialog.showAndWait();

		/*Optional<ComboItem> result = fxDialog.showAndWait();


		result.ifPresent(strSirket -> {

			AppPropertiesImpl.setStrSirket(strSirket.getValue());
			Loghelper.getInstance(getClass()).debug(" Str Sirket Güncellendi:"+ strSirket.getValue());
			//Notifications.create().title("Özpaş Entegre").text("Şirket Değiştirildi:"+ strSirket.getLabel()).showInformation();
			//changeWorkspace.setText("Şirket: "+ AppPropertiesImpl.getComboSirket().getLabel());
			//buildButtonMenuRefresh();
			new UserProp().saveUpdateProp("sirket", strSirket.getValue());
			Loghelperr.getInstance(getClass()).debug("sirket prop Güncellendi");
			//tabMain.getTabs().clear();

		});*/

        //return fxDialog;

    }

    public void showOkDialog(Node nodeContent, String title, String headerText, Integer prefWidt, Integer prefHeight, Boolean resizable) {

        FxDialog fxDialog = new FxDialog();

        fxDialog.setTitle(title);
        fxDialog.setHeaderText(headerText);
        fxDialog.AddOkButton(null);
        if (resizable != null) fxDialog.setResizable(resizable);
        if (prefHeight != null) fxDialog.setHeight(prefHeight);
        if (prefWidt != null) fxDialog.setWidth(prefWidt);

        //MigPane rolePane = getChangeWsPane(fxDialog);

        //Label lblmessage = (Label) rolePane.lookup("#mesajchws");

		/*fxDialog.setFilterToOkButton(()->{



			//if(!result) lblmessage.setText("Hata oluştu");
			//return result;
		});*/

        fxDialog.setContentPane(nodeContent);


        fxDialog.showAndWait();

		/*Optional<ComboItem> result = fxDialog.showAndWait();


		result.ifPresent(strSirket -> {

			AppPropertiesImpl.setStrSirket(strSirket.getValue());
			Loghelper.getInstance(getClass()).debug(" Str Sirket Güncellendi:"+ strSirket.getValue());
			//Notifications.create().title("Özpaş Entegre").text("Şirket Değiştirildi:"+ strSirket.getLabel()).showInformation();
			//changeWorkspace.setText("Şirket: "+ AppPropertiesImpl.getComboSirket().getLabel());
			//buildButtonMenuRefresh();
			new UserProp().saveUpdateProp("sirket", strSirket.getValue());
			Loghelperr.getInstance(getClass()).debug("sirket prop Güncellendi");
			//tabMain.getTabs().clear();

		});*/

        //return fxDialog;

    }






	/*MigPane migPane = new MigPane();
	Button btnExcelAc = new Button("Aç");
		migPane.add(new Label("Dosya Adı:"));
		migPane.add(new Label(path.toString()));
		migPane.add(btnExcelAc);

	File file = new File(path.toString());

		btnExcelAc.setOnAction(event -> {

		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
			Loghelper.debugException(getClass(),e);
		}

	});*/

    public void showOkInfoDialog(String title, String headerText, String content) {

        FxDialog fxDialog = new FxDialog();

        fxDialog.setTitle(title);
        fxDialog.setHeaderText(headerText);
        fxDialog.AddOkButton(null);
        fxDialog.setContentPane(null);

        if (!content.isEmpty()) {
            MigPane nodeContent = new MigPane();  // getChangeWsPane(fxDialog);
            TextArea textArea = new TextArea();
            textArea.setText(content);
            nodeContent.add(textArea);
            fxDialog.setContentPane(nodeContent);
        }

        fxDialog.showAndWait();

    }

    public void showOkInfoDialog(String content) {

        FxDialog fxDialog = new FxDialog();

        fxDialog.setTitle("Özpaş Entegre");
        fxDialog.setHeaderText(null);
        fxDialog.AddOkButton(null);
        fxDialog.setContentPane(null);

        if (!content.isEmpty()) {
            MigPane nodeContent = new MigPane();  // getChangeWsPane(fxDialog);
            TextArea textArea = new TextArea();
            textArea.setText(content);
            nodeContent.add(textArea);
            fxDialog.setContentPane(nodeContent);
        }

        fxDialog.showAndWait();

    }

    public void nodeDialogViewContainer(Parent parentNode, IFxViewContainer viewContainer) {
        nodeDialogViewContainer(parentNode, viewContainer, null, null);
    }

    public void nodeDialogViewContainer(Parent parentNode, IFxViewContainer viewContainer, Integer width, Integer height) {

        Platform.runLater(() -> {

            FxStage stage = new FxStage();
            FxScene scene = null;

            if (width != null && height != null) {
                scene = new FxScene(viewContainer.getView(), width, height);
            } else {
                scene = new FxScene(viewContainer.getView());
            }

            if (parentNode != null) {
                Bounds bounds = parentNode.localToScreen(parentNode.getBoundsInLocal());
                stage.setX(bounds.getMaxX());
                stage.setY(bounds.getMinY());
            }

            viewContainer.setFxStage(stage);

            stage.sizeToScene();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();

        });


    }

    public static void nodeWindow(Node nodeRelative, IFxSimpleCont ifxSimpleCont) {
        nodeWindow(nodeRelative, ifxSimpleCont, null, null, null);
    }

    public static void nodeWindow(IFxSimpleCont iFxSimpleCont, DialogConfig dialogConfig) {

        if (dialogConfig == null) dialogConfig = new DialogConfig();

        Pane rootPane = iFxSimpleCont.getModView().getRootPane();

        FxWindowHelper.setMaxHeightAndWidthForWindows(rootPane);

        if (!FiString.isEmpty(dialogConfig.getCssFileName())) {
            rootPane.getStylesheets().add(dialogConfig.getCssFileName());
        }

        Stage stage = iFxSimpleCont.getFxStageInit();

        if (stage == null) {
            stage = new FxStage();
            iFxSimpleCont.setFxStage(stage);
        }

        if (!FiString.isEmpty(dialogConfig.getTitle())) {
            stage.setTitle(dialogConfig.getTitle());
        }

        FxScene scene = null;

        if (dialogConfig.getWidth() != null && dialogConfig.getHeight() != null) {
            scene = new FxScene(rootPane, dialogConfig.getWidth(), dialogConfig.getHeight());  //,width,height
        } else {
            scene = new FxScene(rootPane);  //,width,height
        }

        if (dialogConfig.getNodeRelative() != null) {
            Bounds bounds = dialogConfig.getNodeRelative().localToScreen(dialogConfig.getNodeRelative().getBoundsInLocal());
            stage.setX(bounds.getMaxX());
            stage.setY(bounds.getMinY());
        }

        stage.setScene(scene);
        //stage.sizeToScene();

        if (dialogConfig.getWidth() != null) {
            //Loghelper.debug(getClass(), "width ayarlanıyor" + dialogContext.getWidth());
            //stage.setWidth(dialogConfig.getWidth());
            rootPane.setPrefWidth(dialogConfig.getWidth());
        }

        if (dialogConfig.getWidth() == null && dialogConfig.getHeight() == null) {
            stage.sizeToScene();
        }

        if (FiBoolean.isTrue(dialogConfig.getBoNonModal())) {
            stage.initModality(Modality.WINDOW_MODAL);
        } else {
            stage.initModality(Modality.APPLICATION_MODAL);
        }
        stage.showAndWait();

    }

    public static void nodeWindow(Node nodeRelative, IFxSimpleCont iFxSimpleCont, Integer width, Integer height, Boolean isNonModal) {
        //Loghelper.get(getClass()).debug("nodeModalByIFxSimpleCont Giriş");

        Stage stage = iFxSimpleCont.getFxStageInit();
        //Loghelper.get(getClass()).debug("nodeModalByIFxSimpleCont");

        if (stage == null) {
            //Loghelper.get(getClass()).debug("nodeModalByIFxSimpleCont-A1-Yeni stage-");
            stage = new FxStage();
            iFxSimpleCont.setFxStage(stage);
        }

        FxScene scene = null;

        if (width != null && height != null) {
            scene = new FxScene(iFxSimpleCont.getModView().getRootPane(), width, height);  //,width,height
        } else {
            scene = new FxScene(iFxSimpleCont.getModView().getRootPane());  //,width,height
        }

        if (nodeRelative != null) {
            Bounds bounds = nodeRelative.localToScreen(nodeRelative.getBoundsInLocal());
            stage.setX(bounds.getMaxX());
            stage.setY(bounds.getMinY());
        }

        stage.setScene(scene);
        stage.sizeToScene();
        if (FiBoolean.isTrue(isNonModal)) {
            stage.initModality(Modality.APPLICATION_MODAL);
        } else {
            stage.initModality(Modality.WINDOW_MODAL);
        }

        stage.showAndWait();
    }

    public void nodeDialogGenView(Parent nodeRelative, IFxViewContainer viewContainer) {
        nodeDialogGenView(nodeRelative, viewContainer, null, null);
    }

    public void nodeDialogGenView(Parent nodeRelative, IFxViewContainer viewContainer, Integer width, Integer height) {

        Platform.runLater(() -> {

            Stage stage = viewContainer.getFxStage();

            if (stage == null) {
                stage = new FxStage();
                viewContainer.setFxStage(stage);
            }

            FxScene scene = null;

            if (width != null && height != null) {
                scene = new FxScene(viewContainer.getRootPane(), width, height);  //,width,height
            } else {
                scene = new FxScene(viewContainer.getRootPane());  //,width,height
            }

            if (nodeRelative != null) {
                Bounds bounds = nodeRelative.localToScreen(nodeRelative.getBoundsInLocal());
                stage.setX(bounds.getMaxX());
                stage.setY(bounds.getMinY());
            }

            stage.sizeToScene();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        });
    }

    public static void showModalWarningAlert(String message) {
        showModalWarningAlert(message, null);
    }

    public static void showModalWarningAlert(String message, String title) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            if (title == null) {
                alert.setTitle("UYARI");
            } else {
                alert.setTitle(title);
            }

            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.show();
        });
    }

    public static void showModalErrorAlert(String message) {
        showModalErrorAlert(null, message);
    }

    public static void showModalErrorAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            if (title == null) {
                alert.setTitle("UYARI");
            } else {
                alert.setTitle(title);
            }

            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.show();
        });
    }

    public static void showModalInfoAlertAsyn(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("BİLGİ");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.show();
        });
    }

    /**
     *
     *
     * @param messageHeader
     * @param message
     */
    public static void showModalInfoAsyn(String messageHeader, String message) {
        Platform.runLater(() -> {
            FxSimpleDialog fxSimpleDialog = new FxSimpleDialog(FxSimpleDialogMetaType.DialogInfo, message);
            fxSimpleDialog.setMessageHeader(messageHeader);
            fxSimpleDialog.openAsDialogSync();
        });
    }

    public static void showModalInfoAsyn(String messageHeader, String message,Runnable runAfterOkButton) {
        Platform.runLater(() -> {
            FxSimpleDialog fxSimpleDialog = new FxSimpleDialog(FxSimpleDialogMetaType.DialogInfo, message);
            fxSimpleDialog.setRunAfterOkEvent(runAfterOkButton);
            fxSimpleDialog.setMessageHeader(messageHeader);
            fxSimpleDialog.openAsDialogSync();
        });
    }

    public static FxSimpleDialog showModalInfoNgt(String messageHeader, String message) {
        FxSimpleDialog fxSimpleDialog = new FxSimpleDialog(FxSimpleDialogMetaType.DialogInfo, message);
        fxSimpleDialog.setMessageHeader(messageHeader);
        fxSimpleDialog.openAsDialogSync();
        return fxSimpleDialog;
    }

    /**
     * ngt : not gui thread , nmd: non-modal window
     * @param messageHeader
     * @param message
     */
    public static void showModalInfoNgtNmd(String messageHeader, String message) {
        FxSimpleDialog fxSimpleDialog = new FxSimpleDialog(FxSimpleDialogMetaType.DialogInfo, message);
        fxSimpleDialog.setMessageHeader(messageHeader);
        fxSimpleDialog.openAsNonModal();
    }

    public static void showModalErrorAsyn(String messageHeader, String message) {
        Platform.runLater(() -> {
            FxSimpleDialog fxSimpleDialog = new FxSimpleDialog(FxSimpleDialogMetaType.DialogError, message);
            fxSimpleDialog.setMessageHeader(messageHeader);
            fxSimpleDialog.openAsDialogSync();
        });
    }

    public static void showModalError2Ngt(String messageHeader, String message) {
        FxSimpleDialog fxSimpleDialog = new FxSimpleDialog(FxSimpleDialogMetaType.DialogError, message);
        fxSimpleDialog.setMessageHeader(messageHeader);
        fxSimpleDialog.openAsDialogSync();
    }


    public static void showSimModalError2(String messageHeader, Fdr fdr) {
        Platform.runLater(() -> {
            String message = fdr.getMessage();

            if (fdr.getException() != null) {
                message += "\n Exception Tanımı \n\n" + FiException.exceptionIfToString(fdr.getException());
            }

            FxSimpleDialog fxSimpleDialog = new FxSimpleDialog(FxSimpleDialogMetaType.DialogError, message, messageHeader);
            fxSimpleDialog.openAsDialogSync();
        });
    }

    public static void showModalErrorWitLogAndMessageAndExc(String messageHeader, Fdr fdr) {
        Platform.runLater(() -> {
            String message = "";

            String logAsString = fdr.getLogAsStringWitErrorInfo();
            if (!FiString.isEmptyTrim(logAsString)) {
                message += "\n" + logAsString;
            }
            if (!FiString.isEmptyTrim(fdr.getMessage())) {
                message += "\n" + fdr.getMessage();
            }

            if (fdr.getException() != null) {
                message += "\nException Tanımı : " + FiException.exceptionToStrSummary(fdr.getException());
            }

            FxSimpleDialog fxSimpleDialog = new FxSimpleDialog(FxSimpleDialogMetaType.DialogError, message, messageHeader);
            fxSimpleDialog.openAsDialogSync();
        });
    }

    /**
     * FnResult yerine Fdr li kullan
     *
     * @param content
     * @return
     */
    @Deprecated
    public FnResult showYesNoCancelDialog(String content) {
        return showYesNoCancelDialog(content, null);
    }

    /**
     *
     * FnResult yerine Fdr li kullan
     *
     * @param content
     * @param title
     * @return
     */
    @Deprecated
    public FnResult showYesNoCancelDialog(String content, String title) {

        MigPane migPane = new MigPane();
        migPane.add(new Label(content), "span");

        MigPane migFooter = new MigPane();

        FxButton btnYes = new FxButton("Evet");
        FxButton btnNo = new FxButton("Hayır");
        FxButton btnCancel = new FxButton("İptal");

        migFooter.add(btnYes, "gapafter 10");
        migFooter.add(btnNo, "gapafter 10");
        migFooter.add(btnCancel, "gapafter 10");

        migPane.add(migFooter, "span");

        FxStage stage = new FxStage();
        FxScene scene = new FxScene(migPane);

        if (title == null && titleGeneral != null) stage.setTitle(titleGeneral);
        if (title != null) stage.setTitle(title);

        FnResult fnResult = new FnResult();

        btnYes.setOnAction(event -> {
            fnResult.setBoResult(true);
            stage.close();

        });

        btnNo.setOnAction(event -> {
            fnResult.setBoResult(false);
            stage.close();
        });

        btnCancel.setOnAction(event -> {
            fnResult.setBoResult(null);
            stage.close();
        });

//			if(parentNode!=null){
//				Bounds bounds = parentNode.localToScreen(parentNode.getBoundsInLocal());
//				stage.setX(bounds.getMaxX());
//				stage.setY(bounds.getMinY());
//			}

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.showAndWait();
        return fnResult;

    }

    public Fdr showYesNoCancelDialog(DialogConfig dialogConfig) {

        if (dialogConfig == null) return new Fdr(false);

        FxMigPane migPane = new FxMigPane();
        migPane.add(new Label(dialogConfig.getTxContent()), "span,grow,push");

        FxMigPane migFooter = new FxMigPane();

        FxButton btnYes = new FxButton("Evet");
        FxButton btnNo = new FxButton("Hayır");
        FxButton btnCancel = new FxButton("İptal");

        migFooter.add(btnYes, "gapafter 10");
        migFooter.add(btnNo, "gapafter 10");
        migFooter.add(btnCancel, "gapafter 10");

        migPane.add(migFooter, "span,growx");

        FxStage stage = new FxStage();
        FxScene scene = new FxScene(migPane);

        String title = dialogConfig.getTitle();

        if (title == null && titleGeneral != null) stage.setTitle(titleGeneral);
        if (title != null) stage.setTitle(title);

        Fdr fdrResult = new Fdr();

        btnYes.setOnAction(event -> {
            fdrResult.setBoResult(true);
            stage.close();
        });

        btnNo.setOnAction(event -> {
            fdrResult.setBoResult(false);
            stage.close();
        });

        btnCancel.setOnAction(event -> {
            fdrResult.setBoResult(null);
            stage.close();
        });

//			if(parentNode!=null){
//				Bounds bounds = parentNode.localToScreen(parentNode.getBoundsInLocal());
//				stage.setX(bounds.getMaxX());
//				stage.setY(bounds.getMinY());
//			}

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.showAndWait();
        return fdrResult;
    }

    public void showPopInfo(String title, String message) {
        Platform.runLater(() -> {
            Notifications.create().title(title).text(message).showInformation();
        });
    }

    public static void showPopInfo(String message) {
        Platform.runLater(() -> {
            Notifications.create().title(FiType.orEmpty(FxDialogShow.getTitleGeneral())).text(message).showInformation();
        });
    }

    public static void showPopWarn(String message) {
        Platform.runLater(() -> {
            Notifications.create().title(FiType.orEmpty(FxDialogShow.getTitleGeneral())).text(message).showWarning();
        });
    }

    public static void showPopError(String message) {
        Platform.runLater(() -> {
            Notifications.create().title(FiType.orEmpty(FxDialogShow.getTitleGeneral())).text(message).showError();
        });
    }


    public static void showPopOrModalLog(Fdr fdr, Boolean boPopInfo) {

        String grup = !FiString.isEmpty(fdr.getTxName()) ? fdr.getTxName() + "\n" : "";

        if (fdr.isTrueBoResult()) {

            Pair<String, Boolean> logAsStringAndErrorExist = fdr.getLogAsStringAndErrorExist();

            if (FiBoolean.isTrue(boPopInfo)) {
                if (logAsStringAndErrorExist.getValue()) {
                    showPopInfo(grup + "İşlem Başarılı, fakat bazı yerlerde hata oluştu.\n" + fdr.getLogAsStringWitErrorInfo());
                } else {
                    showPopInfo(grup + "İşlem Başarılı\n" + fdr.getLogAsStringWitErrorInfo());
                }

            } else {
                if (logAsStringAndErrorExist.getValue()) {
                    showModalInfoAsyn(grup + "İşlem Başarılı, fakat bazı yerlerde hata oluştu. Detayı inceleyiniz. !!\n", fdr.getLogAsStringWitErrorInfo());
                } else {
                    showModalInfoAsyn(grup + "İşlem Başarılı\n", fdr.getLogAsStringWitErrorInfo());
                }
            }
        } else {
            showModalErrorAlert(grup + "Hata Oluştu.", fdr.getLogAsStringWitErrorInfo());
        }
    }

    private static void showPopOrModalInfo(String messageHeader, String messageDetail) {

        if (messageDetail == null) messageDetail = "";

        if (messageDetail.length() < 301) {
            showPopInfo(messageHeader + messageDetail);
        } else {
            showModalInfoAsyn(messageHeader, messageDetail);
        }
    }

    public static void showPopInfoSuccess() {
        Platform.runLater(() -> {
            String message = "İşlem Başarılı";
            Notifications.create().title(FiType.orEmpty(FxDialogShow.titleGeneral)).text(message).showInformation();
        });
    }



    public static String getTitleGeneral() {
        if (titleGeneral == null) return "";
        return titleGeneral;
    }

    public static void setTitleGeneral(String titleGeneral) {
        FxDialogShow.titleGeneral = titleGeneral;
    }

    public void showDeleteMessage(FnResult fnResult1) {

        if (FiType.isTrue(fnResult1)) {
            showPopInfo("Kayıt Silindi...");
        } else {
            showPopInfo("Kayıt Silinirken hata oluştu !!!");
        }
    }

    public static void showDbResult(Fdr dbResult) {
        showDbResult(dbResult, null);
    }

    public static void showDbResult2(Fdr dbResult, Runnable runIfTrue) {
        showDbResult(dbResult, null);
        if (dbResult.isTrueBoResult()) runIfTrue.run();
    }

    public static void showDbResult(Fdr dbResult, String title) {

        if (dbResult == null) {
            FxDialogShow.showModalErrorAlert("İşlem sonucu dönmedi.Sistem Yöneticisini bilgilendirin.");
            return;
        }

        if (dbResult.isTrueBoResult()) {

            String messageDetail = dbResult.getMessageNotNull();

            if (dbResult.getRowsAffectedNotNull() > 0) {
                String messageHeader = FiString.addNewLineToEndIfNotEmpty(title) + "İşlem Başarı ile Gerçekleşti :::\n";

                if (FiNumber.orZero(dbResult.getLnInsertedRows()) > 0 || dbResult.getTxQueryType().equalsIgnoreCase(QueryType.bui().insert)) {
                    messageHeader += String.format("\nEklenen Yeni Kayıt Sayısı:%s", FiNumber.orZero(dbResult.getLnInsertedRows()));
                }

                if (dbResult.getTxQueryType().equalsIgnoreCase(QueryType.bui().updatePop)) {
                    messageHeader += String.format("\nGüncellenen Kayıt Sayısı:%s", FiNumber.orZero(dbResult.getLnUpdatedRows()));
                }

                FxDialogShow.showPopOrModalInfo(messageHeader, "");
                //Loghelper.debug(FxDialogShow.class, "Message Det0ail:"+ messageDetail);

            } else {
                String messageHeader = FiString.addNewLineToEndIfNotEmpty(title) + "İşlem Başarı ile Gerçekleşti.";
                if (FiNumber.orZero(dbResult.getLnInsertedRows()) > 0 || dbResult.getTxQueryType().equals(QueryType.bui().insert)) {
                    messageHeader += String.format("\nEklenen Yeni Kayıt Sayısı:%s", FiNumber.orZero(dbResult.getLnInsertedRows()));
                }
                FxDialogShow.showPopOrModalInfo(messageHeader, messageDetail);
            }


        } else {

            if (dbResult.isFalseBoResult()) {
                String messageHeader = FiString.addNewLineToEndIfNotEmpty(title) + "Hata Oluştu !!!\n";
                String messageDetail = FiString.addNewLineToEndIfNotEmpty(dbResult.getMessage()) + "\n Exception:\n" + FiException.exceptionIfToString(dbResult.getException());
                FxDialogShow.showModalErrorAsyn(messageHeader, messageDetail);
            }

            if (dbResult.isNullBoResult()) {
                String messageDetail = dbResult.getMessage();
                FxDialogShow.showModalInfoAsyn(FiString.addNewLineToEndIfNotEmpty(title) + "İşlem yapılacak kayıt bulunamadı.", messageDetail); //uygun kayıt bulunamadı.
            }


        }

    }

    public static void showDbResultUpdate(Fdr dbResult) {

        if (FiBoolean.isTrue(dbResult.getBoResult())) {

            if (dbResult.isEmptyMessage()) {
                dbResult.setMessage(String.format("İşlem Başarı ile Gerçekleşti.\n %s Adet Kayıt Güncellendi.", dbResult.getRowsAffectedNotNull()));
            }

            //showInfoDialog(dbResult.getMessage());
            FxDialogShow.showModalInfoAlertAsyn(dbResult.getMessage());

        } else {

            if (dbResult.isEmptyMessage()) {
                dbResult.setMessage("Hata Oluştu !!! : " + FiException.exceptionIfToString(dbResult.getException()));
            }

            showModalWarningAlert(dbResult.getMessage());

        }

    }

    public static void showFdrDbResultWithAllModal(Fdr dbResult) {

        if (FiBoolean.isTrue(dbResult.getBoResult())) {

            if (FiString.isEmpty(dbResult.getMessage())) {
                dbResult.setMessage(String.format("İşlem Başarı ile Gerçekleşti.\n %s Adet Kayıt Güncellendi.", dbResult.getRowsAffectedNotNull()));
            }

            //showInfoDialog(dbResult.getMessage());
            FxDialogShow.showModalInfoAlertAsyn(dbResult.getMessage());

        } else {

            if (dbResult.isFalseBoResult()) {
                if (dbResult.getMessage() == null) {
                    dbResult.setMessage("Hata Oluştu !!! : " + FiException.exceptionIfToString(dbResult.getException()));
                }
            }

            if (dbResult.isNullBoResult()) {
                if (dbResult.getMessage() == null) {
                    dbResult.setMessage("İşlem Yapılmadı.");
                }
            }

            showModalWarningAlert(dbResult.getMessage());
        }

    }

    public static void showDbResultUpdatePop(Fdr dbResult) {

        if (FiBoolean.isTrue(dbResult.getBoResult())) {

            if (dbResult.getMessage() == null) {
                dbResult.setMessage(String.format("İşlem Başarı ile Gerçekleşti.\n %s Adet Kayıt İşleme Alındı.", dbResult.getRowsAffectedNotNull()));
            }

            //showInfoDialog(dbResult.getMessage());
            FxDialogShow.showPopInfo(dbResult.getMessage());

        } else {

            if (dbResult.getMessage() == null) {
                dbResult.setMessage("Hata Oluştu !!! : " + FiException.exceptionIfToString(dbResult.getException()));
            }

            showModalWarningAlert(dbResult.getMessage());

        }

    }

    public void showDbResultWithOwner(Fdr dbResult, Window windowOwner) {

        if (FiBoolean.isTrue(dbResult.getBoResult())) {

            if (dbResult.getMessage() == null) {
                dbResult.setMessage("İşlem Başarı ile Gerçekleşti.");
            }
            //showInfoDialog(dbResult.getMessage());
            if (windowOwner != null) {
                Platform.runLater(() -> {
                    Notifications.create().owner(windowOwner).position(Pos.CENTER).text(dbResult.getMessage()).showInformation();
                });
            } else {
                showPopInfo(dbResult.getMessage());
            }


        } else {

            if (dbResult.getMessage() == null) {
                dbResult.setMessage("Hata Oluştu !!! : " + FiException.exceptionIfToString(dbResult.getException()));
            }
            showModalWarningAlert(dbResult.getMessage());

        }

    }



}
