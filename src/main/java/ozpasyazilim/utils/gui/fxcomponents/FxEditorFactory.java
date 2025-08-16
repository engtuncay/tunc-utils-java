package ozpasyazilim.utils.gui.fxcomponents;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.apache.commons.beanutils.PropertyUtils;
import org.reactfx.EventStreams;
import ozpasyazilim.utils.core.*;

import ozpasyazilim.utils.datatypes.FiKeyBean;
import ozpasyazilim.utils.fidborm.FiReflectClass;
import ozpasyazilim.utils.fidborm.FiField;
import ozpasyazilim.utils.gui.components.ComboItemText;
import ozpasyazilim.utils.gui.components.ComboItemObj;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.core.FiReflection;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.IFiColHelper;
import ozpasyazilim.utils.table.OzColType;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Ana Metodlar : Generate Node Comp, Get Value , Set Value Node Comp
 * <p>
 * Generate Node Comp : Class ve vs. göre Node componenti oluşturulur.
 * <p>
 * Set Value Node Comp ile değer atar, Get Value Node Comp ile değerini alır.
 *
 */
public class FxEditorFactory {

    Map<String, String> mapSimpleClassToEditorClass;

    /**
     * UBOM Component Node'u oluşturmak için kullanılır (Örneğin FxFormMig'de kullanılır)
     * <p>
     * FiCol'dan extra özellikler alması için eklendi (length vs)
     * <p>
     * txClassName verilmemişse, OzColType'a göre component class belirlenir
     *
     * @param ozColType
     * @param txClassName
     * @param fiCol
     * @return
     */
    public static Node genNodeCompByClassNameMain(OzColType ozColType, String txClassName, FiCol fiCol) {
        //, Object compValue çıkarıldı

        //Loghelper.getInstance(FxEditorFactory.class).debug(" Ozcoltype:"+ozColType.toString());
        //Loghelper.getInstance(FxEditorFactory.class).debug(" Prm Comp Class:"+txClassName);

        if (fiCol == null) fiCol = new FiCol();

        // txClassName verilmemişse, OzColType'a göre comp class belirlenir
        if (txClassName == null) {
            //iFiCol.setColFxNodeClass(FxTextField.class.getName());
            txClassName = FxTextField.class.getName();

            // 14-12 eklendi
            if (ozColType == OzColType.Boolean) {
                txClassName = FxCheckBox.class.getName();
            }

            if (ozColType == OzColType.CommaSeperatedStr) {
                txClassName = FxTextFieldBtnCsv.class.getName();
            }

        }

        //Loghelper.getInstance(FxEditorFactory.class).debug(" Comp Class:"+txClassName.toString());

        if (txClassName.equals(FxTextField.class.getSimpleName())
                || txClassName.equals(FxTextField.class.getName())) {

            FxTextField comp = new FxTextField();

            if (ozColType == OzColType.Double) {
                comp.setupNumberDoubleTextField1();
                // Loghelper.get(FxEditorFactory.class).debug("Double Text Field Üretildi:"+ iFiCol.getHeaderName());

                //comp.setAlignment(Pos.BASELINE_RIGHT);
            }

            if (ozColType == OzColType.Integer) {
                comp.setupNumberDoubleTextField1();
                //comp.setAlignment(Pos.BASELINE_RIGHT);
            }

            if (FiBool.isTrue(fiCol.getBoEditorOnlyNumber())) {
                comp.setupNumberDoubleTextField1();
            }

            if (ozColType == OzColType.String) {
                if (fiCol.getOfcLnLength() != null) {
                    Loghelper.get(FxEditorFactory.class).debug("Text Limiti Tanımlandı.");
                    comp.setupTextFieldAsLimitLength(fiCol);
                }
                //comp.setAlignment(Pos.BASELINE_RIGHT);
            }

            //if (iFiCol.getColFilterKeyEvent() != null) comp.setOnKeyReleased(iFiCol.getColFilterKeyEvent());
            //iFiCol.setColFxNode(comp);
            return comp;
        }

        if (txClassName.equals(FxTextFieldBtn.class.getName())) {

            FxTextFieldBtn comp = new FxTextFieldBtn();

            if (ozColType == OzColType.Double) {
                comp.getFxTextField().setupNumberDoubleTextField1();
                comp.getFxTextField().setAlignment(Pos.BASELINE_RIGHT);
            }
            //iFiCol.setColFxNode(comp);
            return comp;
        }

        if (txClassName.equals(FxTextFieldBtnCsv.class.getName())) {

            FxTextFieldBtnCsv comp = new FxTextFieldBtnCsv();

            if (ozColType == OzColType.Double) {
                comp.getFxTextField().setupNumberDoubleTextField1();
                comp.getFxTextField().setAlignment(Pos.BASELINE_RIGHT);
            }
            //iFiCol.setColFxNode(comp);
            return comp;
        }

        if (txClassName.equals(FxTextFieldBtnWitLbl3.class.getName())) {

            FxTextFieldBtnWitLbl3 comp = new FxTextFieldBtnWitLbl3();

            if (ozColType == OzColType.Double) {
                comp.getFxTextField().setupNumberDoubleTextField1();
                comp.getFxTextField().setAlignment(Pos.BASELINE_RIGHT);
            }
            //iFiCol.setColFxNode(comp);
            return comp;
        }

        if (txClassName.equals(FxTextFieldBtnWitLbl2.class.getName())) {

            FxTextFieldBtnWitLbl2 comp = new FxTextFieldBtnWitLbl2();

            if (ozColType == OzColType.Double) {
                comp.getFxTextField().setupNumberDoubleTextField1();
                comp.getFxTextField().setAlignment(Pos.BASELINE_RIGHT);
            }
            //iFiCol.setColFxNode(comp);
            return comp;
        }

        if (txClassName.equals(DatePicker.class.getName())
                || txClassName.equals(FxDatePicker.class.getName())) {
            FxDatePicker comp = new FxDatePicker();
            //if (iFiCol.getColFilterKeyEvent() != null) comp.setOnKeyReleased(iFiCol.getColFilterKeyEvent());
            return comp;
        }

        if (txClassName.equals(FxLabel.class.getName())) {
            FxLabel comp = new FxLabel("");
            return comp;
        }

        if (txClassName.equals(FxLabelData.class.getName())) {
            FxLabelData comp = new FxLabelData("");
            return comp;
        }

        if (txClassName.equals(TextField.class.getName())) {
            FxTextField comp = new FxTextField();
            //if (iFiCol.getColFilterKeyEvent() != null) comp.setOnKeyReleased(iFiCol.getColFilterKeyEvent());
            return comp;
        }

        if (txClassName.equals(FxComboBox.class.getName())) {
            FxComboBox<ComboItemText> comp = new FxComboBox<>();
            return comp;
        }

        if (txClassName.equals(FxComboBoxSimple.class.getName())) {
            FxComboBoxSimple comp = new FxComboBoxSimple();
            return comp;
        }

        if (txClassName.equals(FxComboBoxObj.class.getName())) {
            FxComboBoxObj comp = new FxComboBoxObj();
            return comp;
        }

        if (txClassName.equals(FxCheckBox.class.getName())) {
            FxCheckBox comp = new FxCheckBox();
            return comp;
        }

        if (txClassName.equals(FxChoiceBoxSimple.class.getName())) {
            FxChoiceBoxSimple comp = new FxChoiceBoxSimple();
            comp.activateBackSpaceClearSelection();
            return comp;
        }

        return null;
    }

    /**
     * Fx Node Component'inden değeri alır ve ozcoltype'a ilgili türe çevirir.
     * <p>
     * Not Önemli !!! : Editorden gelen boş string ler null olarak yorumlandı.
     * <p>
     * IFxNode interface kullanan bir component ise degeri kendi metodundan alınır. {@link IFiNode}
     * <p>
     * {@code ifxNode1.getCompValueByColType(ozColType) }
     * <p>
     * txNodeClassName : paketle beraber uzun sınıf ismi
     * <p>
     * text Componentlerden (TextField,ComboBox,ChoiceBox) değer alındıktan sonra
     * <p>
     * {@code convertStringValueToObjectByOzColType(ozColType, textValue);} ile tipine çevrilir.
     *
     * @param
     * @return
     */
    public static Object getValueNodeCompMain(OzColType ozColType, String txNodeClassName, Node nodeComp, Object nodeValueDefault) {
        // Parametre olarak çıkarılanlar : IFiCol iFiCol , IFxNode ifxNode

        // Loghelper.getInstance(FxEditorFactory.class).debug("Col Node Class:"+ txNodeClassName);
        // Loghelper.getInstance(FxEditorFactory.class).debug("Col Node Component Id:"+ FiType.orEmpty(nodeComp));

        Object objCellValue = null;

        // String txNodeClassName = txNodeClassName; //infTableCol.getColFxNodeClass();

        if (txNodeClassName == null) txNodeClassName = "";

        // editor tanımlanmamışsa nodeValueDefault parametresi döndürülür
        if (nodeComp == null || txNodeClassName.isEmpty()) {
            return nodeValueDefault;
        }

        // IFxNode interface kullanan bir component ise degeri kendi metodundan alınır.
        if (nodeComp instanceof IFiNode) {
            // Loghelper.get(FxEditorFactory.class).debug("value node component alındı");
            IFiNode IFiNode1 = (IFiNode) nodeComp;
            return IFiNode1.getCompValueByColType(ozColType);
        }

        //*** Text Value (String) değer olan componentlerin değeri burada belirlenir.

        if (txNodeClassName.equals(FxTextField.class.getName())
                || txNodeClassName.equals(FxTextFieldBtn.class.getName())
                || txNodeClassName.equals(FxLabel.class.getName())
                || txNodeClassName.equals(FxLabelData.class.getName())
                || txNodeClassName.equals(FxTextFieldBtnWitLbl3.class.getName())) {

            String textValue = null;

            if (txNodeClassName.equals(FxTextField.class.getName())) {
                FxTextField comp = (FxTextField) nodeComp;
                textValue = comp.getText();
            }

            if (txNodeClassName.equals(FxTextFieldBtn.class.getName())) {
                FxTextFieldBtn comp = (FxTextFieldBtn) nodeComp;
                //textValue = comp.getFxTextField().getText();
                textValue = comp.getTxValue();
                //Loghelperr.debug(FxEditorFactory.class,"txVal:"+comp.getTxValue());
            }

            if (txNodeClassName.equals(FxTextFieldBtnWitLbl3.class.getName())) {
                FxTextFieldBtnWitLbl3 comp = (FxTextFieldBtnWitLbl3) nodeComp;
                //textValue = comp.getFxTextField().getText();
                textValue = comp.getTxValue();
                //Loghelperr.debug(FxEditorFactory.class,"txVal:"+comp.getTxValue());
            }

            if (txNodeClassName.equals(FxTextFieldBtnWitLbl2.class.getName())) {
                FxTextFieldWithButtonLabelCustom comp = (FxTextFieldWithButtonLabelCustom) nodeComp;
                //textValue = comp.getFxTextField().getText();
                textValue = comp.getTxValue();
                //Loghelperr.debug(FxEditorFactory.class,"txVal:"+comp.getTxValue());
            }

            if (txNodeClassName.equals(FxLabel.class.getName()) || txNodeClassName.equals(FxLabelData.class.getName())) {
                FxLabel comp = (FxLabel) nodeComp;
                textValue = comp.getTxValue();
            }

            // textvalue null veya "" boş string ise null döndürülür !!!!
            if (textValue == null || textValue.isEmpty()) {
                return null;
            }

            // ********** String Value ilgili tipe dönüştürülür
            return toTypeByOzColType(ozColType, textValue);

        }

        //*** Tarih Componentleri

        if (FiString.equalsOne(txNodeClassName, DatePicker.class.getName(), FxDatePicker.class.getName())) {

            LocalDate localDate = null;
            String txDate = null;

            DatePicker comp = null;

            if (txNodeClassName.equals(DatePicker.class.getName())) {
                comp = (DatePicker) nodeComp;
                txDate = comp.getEditor().getText();
                //localDate = comp.getValue();
                localDate = FxDatePicker.getStrConverter().fromString(txDate);

            }

            if (txNodeClassName.equals(FxDatePicker.class.getName())) {
                comp = (FxDatePicker) nodeComp;
                //localDate = comp.getValue();
                txDate = comp.getEditor().getText();
                localDate = FxDatePicker.getStrConverter().fromString(txDate);
            }

            if (localDate != null) {
                objCellValue = FiDate.convertLocalDateToSimpleDate(localDate);
                return objCellValue;
            } else if (txDate != null) {
                Date dtConvert = FiDate.strToDateGeneric(txDate);
//				if(dtConvert==null && comp.getValue()!=null){
//					comp.setValue(null);
//				}

//				if(dtConvert!=null && comp!=null) {
//					comp.setValue(FiDate.convertLocalDate(dtConvert));
//					//comp.requestFocus();
//				}
                return dtConvert;
            }

            return objCellValue;
        }

        //*** Diger Componentler

        if (txNodeClassName.equals(FxComboBox.class.getName())
                || txNodeClassName.equals(ComboBox.class.getName())) {

            if (txNodeClassName.equals(FxComboBox.class.getName())) {
                FxComboBox<ComboItemText> comp = (FxComboBox<ComboItemText>) nodeComp;
                String textValue = comp.getSelectedItemFi().getValue();
                return toTypeByOzColType(ozColType, textValue);
            }

        }

        if (txNodeClassName.equals(FxComboBoxSimple.class.getName())) {
            FxComboBoxSimple comp = (FxComboBoxSimple) nodeComp;
            ComboItemText selectedItemFi = comp.getSelectedItemFi();
            String textValue = null;
            if (selectedItemFi != null) textValue = selectedItemFi.getValue();
            return toTypeByOzColType(ozColType, textValue);
        }

        if (txNodeClassName.equals(FxComboBoxObj.class.getName())) {
            FxComboBoxObj comp = (FxComboBoxObj) nodeComp;
            ComboItemObj selectedItemFi = comp.getSelectedItemFi();
            // selectedItem yoksa null döner
            return (selectedItemFi != null ? selectedItemFi.getValue() : null);
        }

        if (txNodeClassName.equals(FxChoiceBoxSimple.class.getName())) {
            FxChoiceBoxSimple comp = (FxChoiceBoxSimple) nodeComp;
            if (comp.getFiSelectedItem() != null) {
                String textValue = comp.getFiSelectedItem().getValue();
                return toTypeByOzColType(ozColType, textValue);
            }
            return null;
        }

        if (txNodeClassName.equals(FxCheckBox.class.getName())) {
            FxCheckBox comp = (FxCheckBox) nodeComp;
            return comp.isSelected();
        }

        //*** Diger Durumlar

        //if (objCellValue != null) return objCellValue;

        // infTableCol.getColFxNodeClass() == null && //if (infTableCol.getFiValue() != null) {

        return nodeValueDefault; //getColFilterValueByColType(iFiTableCol);

    }

    /**
     * Component değer ataması yapar, örneğin textfield ise textfield.setText değerine atama yaparak içeriğini ayarlar.
     * <p>
     * Datepicker ise tarih değerini atar.
     *
     * @param iFiCol       ilgili field/column tanımı
     * @param compValue    component verilecek değer
     * @param colNodeClass node sınıf ismi Örnegin FxTextField gibi
     * @param colNode      component'in Node objesi
     * @param entity       değerin alındığı Entity. İşlevi ???
     */
    private static void setValueNodeCompMain(IFiCol iFiCol, Object compValue, String colNodeClass, Node colNode, Object entity) {

        //String colNodeClass = iFiCol.getColFilterNodeClass();

        // IfxNode türünde ise CompValue metoduyla kendisine değer atamasını kendisi yapar
        if (colNode instanceof IFiNode) {
            //Loghelper.get(FxEditorFactory.class).debug("IfxNode Set CompValue :" + iFiCol.getHeaderName());
            IFiNode IFiNode = (IFiNode) colNode;
            IFiNode.setCompValue(compValue);
            return;
        }

        if (colNodeClass == null) {
            return;
        }

        // Componentlere göre değer ataması

        if (colNodeClass.equals(FxTextField.class.getName())) {
            //FiConsole.debug(iFiCol);
            FxTextField comp = (FxTextField) colNode;
            comp.setText(compValue.toString());
        }

        if (colNodeClass.equals(FxTextFieldBtn.class.getName())) {

            FxTextFieldBtn comp = (FxTextFieldBtn) colNode;
            comp.setTxValue(compValue.toString());

            if (iFiCol.getFnEditorNodeValueFormmatter() != null) {
                comp.getFxTextField().setText(iFiCol.getFnEditorNodeValueFormmatter().apply(entity).toString());
            } else {
                comp.getFxTextField().setText(compValue.toString());
            }

        }

        if (colNodeClass.equals(FxTextFieldBtnWitLbl3.class.getName())) {

            FxTextFieldBtnWitLbl3 comp = (FxTextFieldBtnWitLbl3) colNode;
            // Gerçek değeri txvalue da tutulur.
            comp.setTxValue(compValue.toString());
            if (iFiCol.getFnEditorNodeValueFormmatter() != null) {
                comp.getFxTextField().setText(iFiCol.getFnEditorNodeValueFormmatter().apply(entity).toString());
            } else {
                comp.getFxTextField().setText(compValue.toString());
            }

        }

        if (colNodeClass.equals(FxDatePicker.class.getName())) {
            FxDatePicker comp = (FxDatePicker) colNode;
            comp.setValue(FiDate.dateToLocalDate((Date) compValue));
            comp.getEditor().setText(FiDate.dateToStrAsddmmyyyyWitDot((Date) compValue));
        }

        if (colNodeClass.equals(DatePicker.class.getName())) {
            DatePicker comp = (DatePicker) colNode;
            comp.setValue(FiDate.dateToLocalDate((Date) compValue));
            comp.getEditor().setText(FiDate.dateToStrAsddmmyyyyWitDot((Date) compValue));
        }

        if (colNodeClass.equals(FxLabel.class.getName())) {
            FxLabel comp = (FxLabel) colNode;
            comp.setText(compValue.toString());
            comp.setTxValue(compValue.toString());
        }

        // fxcombobox comboitem ise

        if (colNodeClass.equals(FxComboBox.class.getName())) {
            FxComboBox comp = (FxComboBox) colNode;
            comp.setTxValue(compValue.toString());
        }

        if (colNodeClass.equals(FxComboBoxSimple.class.getName())) {
            FxComboBoxSimple comp = (FxComboBoxSimple) colNode;
            comp.setTxValue(compValue.toString());
            comp.setSelectedItemByTxValueFi(); // 5-11-21 eklendi
        }

        // 04-11-22 added.
        if (colNodeClass.equals(FxComboBoxObj.class.getName())) {
            Loghelper.get(FxEditorFactory.class).debug("FxComboBoxObj Set Value :" + iFiCol.getOfcTxHeader());
            FxComboBoxObj comp = (FxComboBoxObj) colNode;
            comp.setObjValue(compValue);
            comp.setSelectedItemByObjValueFi();
        }

        if (colNodeClass.equals(FxChoiceBox.class.getName())) {
            FxChoiceBox comp = (FxChoiceBox) colNode;
            comp.setTxValue(compValue.toString());
        }

        if (colNodeClass.equals(FxCheckBox.class.getName())) {
            FxCheckBox comp = (FxCheckBox) colNode;
            if (compValue != null) {
                Boolean boValue = (Boolean) compValue;
                comp.setSelected(boValue);
            }
        }

    }


    public static Boolean setTextValueEditorNodeByString(List<? extends IFiCol> listFormElements, Object fieldName, String value, Object entity) {

        IFiCol colByFieldName = IFiColHelper.build(listFormElements).findColumnByFieldName(fieldName.toString());

        if (colByFieldName == null) return false;

        setValueNodeCompMain(colByFieldName, value, colByFieldName.getColEditorClass(), colByFieldName.getColEditorNode(), entity);

        return true;
    }

    public static Node getEditorNodeByFieldName(List<? extends IFiCol> listFormElements, Object fieldName) {

        IFiCol columnByFieldName = IFiColHelper.build(listFormElements).findColumnByFieldName(fieldName.toString());

        if (columnByFieldName == null) return null;

        return columnByFieldName.getColEditorNode();
    }

    public static <PrmEntClazz> Fdr checkColumnsByClazzAnno(PrmEntClazz entity, Boolean boTrimIfNeed) {

        Class<?> aClass = entity.getClass();

        List<FiField> fiFieldList = FiReflectClass.getListFieldsShortWithId(aClass);
        Fdr fdr = new Fdr();
        fdr.setFdrBoResult(true);

        for (FiField fiField : fiFieldList) {

            Object property = null;

            if (FiBool.isFalse(fiField.getOfcBoNullable())) {

                if (property == null) property = FiReflection.getProperty(entity, fiField.getOfcTxFieldName());

                if (property == null) {
                    fdr.setFdrBoResult(false);
                    fdr.appendMsg("alan null olamaz:" + fiField.getOfcTxFieldName());
                }

            }

            if (fiField.getOfcLnLength() != null && fiField.getOfcLnLength() > 0 && fiField.getClassNameSimple().equals("String")) {

                if (property == null) property = FiReflection.getProperty(entity, fiField.getOfcTxFieldName());

                if (property != null) {
                    String txProperty = (String) property;
                    if (txProperty.length() > fiField.getOfcLnLength()) {

                        if (FiBool.isTrue(boTrimIfNeed)) {
                            //Loghelper.debug(FxEditorFactory.class,"FiField Length:"+fiField.getLength());
                            txProperty = txProperty.substring(0, fiField.getOfcLnLength() - 1);
                            FiReflection.setter(entity, fiField.getOfcTxFieldName(), txProperty);
                        } else {
                            fdr.setFdrBoResult(false);
                            fdr.appendMsg(String.format("Alan %s karakter fazla olamaz %s : %s"
                                    , fiField.getOfcLnLength(), txProperty.length(), fiField.getOfcTxFieldName()));
                        }

                    }
                }
            }

        }

        return fdr;
    }

    public static <PrmEntClazz> Fdr checkAndTrimTxtColumnsByClazzAnno(PrmEntClazz entity) {

        Class<?> aClass = entity.getClass();

        Boolean boTrimIfNeed = true;

        List<FiField> fiFieldList = FiReflectClass.getListFieldsShortWithId(aClass);
        Fdr fdr = new Fdr();
        fdr.setFdrBoResult(true);

        for (FiField fiField : fiFieldList) {

            Object property = null;

            if (fiField.getOfcLnLength() != null && fiField.getOfcLnLength() > 0 && fiField.getClassNameSimple().equals("String")) {

                property = FiReflection.getProperty(entity, fiField.getOfcTxFieldName());

                if (property != null) {
                    String txProperty = (String) property;
                    if (txProperty.length() > fiField.getOfcLnLength()) {

                        if (FiBool.isTrue(boTrimIfNeed)) {
                            //Loghelper.debug(FxEditorFactory.class,"FiField Length:"+fiField.getLength());
                            txProperty = txProperty.substring(0, fiField.getOfcLnLength() - 1);
                            FiReflection.setter(entity, fiField.getOfcTxFieldName(), txProperty);
                            fdr.setFdrBoResult(true);
                        } else {
                            fdr.setFdrBoResult(false);
                            fdr.appendMsg(String.format("Alan %s karakter fazla olamaz %s : %s"
                                    , fiField.getOfcLnLength(), txProperty.length(), fiField.getOfcTxFieldName()));
                        }

                    }
                }
            }

        }

        return fdr;
    }

    public static void clearValuesOfFormFields(List<FiCol> listFormElements) {

        for (FiCol listFormElement : listFormElements) {
            clearValueNodeCompMain(listFormElement, listFormElement.getColEditorClass(), listFormElement.getColEditorNode());
        }

    }

//    public static void setNodeValue(FiCol fiTableCol, Object objValue) {
//        if (fiTableCol == null || fiTableCol.getColEditorNode() == null) {
//            return;
//        }
//
//        setNodeValueByCompClass(fiTableCol.getColEditorNode(), fiTableCol.getColEditorClass(), objValue);
//
//    }

    /**
     * UBOM Filter node Fkb dönüştürülmesi (FxTableView de kullanılıyor)
     *
     * @param fiColList
     * @return
     */
    public static FiKeyBean bindFiColListToFkbByFilterNode(List<FiCol> fiColList) {

        FiKeyBean fiKeyBean = new FiKeyBean();

        for (FiCol fiCol : fiColList) {

            Object nodeObjValue = getNodeObjValueByFilterNode(fiCol, fiCol.getFilterNodeClass());

            if (FiBool.isTrue(fiCol.getBoFilterLike()) && nodeObjValue instanceof String) {
                String txNodeValue = (String) nodeObjValue;
                txNodeValue = txNodeValue.trim().replaceAll(" ", "%");
                fiKeyBean.add(fiCol.getOfcTxFieldName(), "%" + txNodeValue + "%");
                continue;
            }
            fiKeyBean.add(fiCol.getOfcTxFieldName(), nodeObjValue);
        }

        return fiKeyBean;
    }

    public static FxChoiceBox getNodeCompAsChoiceBox(FiCol fiCol) {

        Node colEditorNode = fiCol.getColEditorNode();

        if (colEditorNode == null) return null;

        if (colEditorNode instanceof FxChoiceBox) {
            return (FxChoiceBox) colEditorNode;
        }

        return null;
    }

    public static FxChoiceBoxSimple getNodeCompAsChoiceBoxSimple(FiCol fiCol) {

        Node colEditorNode = fiCol.getColEditorNode();

        if (colEditorNode == null) return null;

        if (colEditorNode instanceof FxChoiceBoxSimple) {
            return (FxChoiceBoxSimple) colEditorNode;
        }

        return null;
    }

    public String convertSimpleTypeToCompClass(String typeSimpleName) {
        return getMapSimpleClassToEditorClass().getOrDefault(typeSimpleName, null);
    }

    public Map<String, String> getMapSimpleClassToEditorClass() {

        if (mapSimpleClassToEditorClass == null) {
            mapSimpleClassToEditorClass = new HashMap<>();

            mapSimpleClassToEditorClass.put("Integer", "FxTextField");
            // string leri bazı label olabilir
            mapSimpleClassToEditorClass.put("String", "FxTextField");
            mapSimpleClassToEditorClass.put("Date", "FxDatePicker");

        }
        return mapSimpleClassToEditorClass;
    }

    /**
     * Tablodaki filtrelerdeki compler için kullanılıyor
     *
     * @param iFiCol
     * @return
     */
    public static Node generateAndSetFilterNode(IFiCol iFiCol) {

        Node comp = genNodeCompByClassNameMain(iFiCol.getColType(), iFiCol.getFilterNodeClass(), null);

        // generator içinden çıkarılıp buraya eklendi, esas amaç anlaşılmadı
        setNodeValueByCompClass(comp, iFiCol.getFilterNodeClass(), iFiCol.getFilterValue());

        if (iFiCol.getFilterNodeClass() == null) iFiCol.setFilterNodeClass(comp.getClass().getName());
        if (comp != null) iFiCol.setColFilterNode(comp);
        return comp;

    }

    //FiTableCol için mi yapılsa
//	public static Node generateAndSetFilterNodeByFitableCol(FiTableCol ozTableCol) {
//
//		Node comp = generateNodeByClassNameMain(ozTableCol.getColType(), ozTableCol.getColFilterNodeClass());
//
//		// generator içinden çıkarılıp buraya eklendi, esas amaç anlaşılmadı
//		setNodeValueForNodeGenerator(comp, ozTableCol.getColFilterNodeClass(), ozTableCol.getColFilterValue());
//
//		if (ozTableCol.getColFilterNodeClass() == null) ozTableCol.setColFilterNodeClass(comp.getClass().getName());
//		if (comp != null) ozTableCol.setColFilterNode(comp);
//		return comp;
//	}


    /**
     * FitableCol editor node'una set eder <br>
     *
     * @param fiTableCol
     * @return
     */
    public Node generateAndRenderNodeByEditorClassWoutEntityEntry(IFiCol fiTableCol) {

        return generateAndRenderNodeBeforeLoadByEditorClassEntry(fiTableCol, null);

    }

    /**
     * FiCol editor node'una set ve render eder
     * <p>
     * Kullanan Components : FxFormMig
     *
     * @param ifiCol
     * @return
     */
    public static Node generateAndRenderNodeBeforeLoadByEditorClassEntry(IFiCol ifiCol, Object entity) {

        Node comp = genNodeCompByClassNameMain(ifiCol.getColType(), ifiCol.getColEditorClass(), null);

        if (ifiCol.getFnEditorNodeRendererBeforeSettingValue() != null) {
            ifiCol.getFnEditorNodeRendererBeforeSettingValue().accept(entity, comp);
        }

        if (ifiCol.getColEditorClass() == null) ifiCol.setColEditorClass(comp.getClass().getName());

        if (comp != null) ifiCol.setColEditorNode(comp);

        if (comp instanceof IFiNode) {
            ifiCol.setIfxNodeEditor((IFiNode) comp);
            //logger.getLogger().debug("ifxnode ayarlandı");
        }

        // generator içinden çıkarılıp buraya eklendi, esas amaç anlaşılmadı
        setNodeValueByCompClass(ifiCol.getColEditorNode(), ifiCol.getColEditorClass(), ifiCol.getColValue());

        return comp;
    }

    /**
     * Kullanan Component'ler
     * <p>
     * FxFormMig
     * <p>
     * Lifecycle Methods
     * <p>
     * getFnEditorNodeRendererBeforeSettingValue , getFnEditorNodeRendererAfterInitialValue çalıştırır.
     *
     * @param fiCol
     * @param entity
     * @return
     */
    public static Node generateEditorNodeFullLifeCycle(FiCol fiCol, Object entity) {

        Node comp = genNodeCompByClassNameMain(fiCol.getColType(), fiCol.getColEditorClass(), fiCol);

        if (fiCol.getFnNodeFocusTrigger() != null && comp != null) {
            comp.focusedProperty().addListener((observable, oldValue, newValue) -> fiCol.getFnNodeFocusTrigger().accept(comp));
        }

        if (comp != null) {
            if (fiCol.getColEditorClass() == null) fiCol.setColEditorClass(comp.getClass().getName());
            fiCol.setColEditorNode(comp);
        }

        if (comp instanceof IFiNode) {
            fiCol.setIfxNodeEditor((IFiNode) comp);
            //logger.getLogger().debug("ifxnode ayarlandı");
        }

        // getFnEditorNodeRendererBeforeSettingValue çalıştırır
        fiCol.lifeCycleNodeOperationsBeforeSettingValue(entity, comp);

        // comp'a değer atanır
        setNodeValueByCompClass(fiCol.getColEditorNode(), fiCol.getColEditorClass(), fiCol.getColValue());

        fiCol.lifeCycleNodeOperationsAfterInitialValue(entity, comp);

        return comp;
    }

    public static void setNodeValueByCompClass(Node component, String compClass, Object compValue) {

        if (compClass.equals(DatePicker.class.getName())
                || compClass.equals(FxDatePicker.class.getName())) {

            FxDatePicker comp = (FxDatePicker) component;
            //if (iFiTableCol.getColFilterKeyEvent() != null) comp.setOnKeyReleased(iFiTableCol.getColFilterKeyEvent());
            if (compValue instanceof Date) {
                comp.setValue(FiDate.dateToLocalDate((Date) compValue));
            }
            //return comp;
        }

        if (component instanceof FxCheckBox) {

            if (compValue instanceof Boolean) {
                FxCheckBox comp = (FxCheckBox) component;
                comp.setSelected((Boolean) compValue);
            }

        }

        if (component instanceof FxTextField) {
            if (compValue != null) {
                FxTextField comp = (FxTextField) component;
                comp.setText(compValue.toString());
                comp.setTxValue(compValue.toString());
            }
        }

        if (component instanceof FxTextFieldBtn) {
            if (compValue != null) {
                FxTextFieldBtn comp = (FxTextFieldBtn) component;
                comp.getFxTextField().setText(compValue.toString());
                comp.getFxTextField().setTxValue(compValue.toString());
            }
        }
    }

    /**
     * Editor Class tanımlanmamışsa , otomatik olarak colType alanına göre editor class tanımlar.
     *
     * @param iFiCol
     */
    public static void setAutoColEditorClassByColType(IFiCol iFiCol) {

        if (iFiCol.getColEditorClass() == null) {
            String autoColCellFactoryClassByType = FxEditorFactory.getAutoEditorClassMainByOzColType(iFiCol);
            iFiCol.setColEditorClass(autoColCellFactoryClassByType);
        }

    }

    /**
     * Ozcoltype göre editor class belirler (formlarda uygulanıyor)
     *
     * @param iFiCol
     * @return
     */
    public static String getAutoEditorClassMainByOzColType(IFiCol iFiCol) {

        if (iFiCol.getColType() == OzColType.Date) {
            return FxDatePicker.class.getName();
        }

        if (iFiCol.getColType() == OzColType.Boolean) {
            return FxCheckBox.class.getName();
        }

        return null;
    }

    //


    public static void registerEnterFnForFilterNode(IFiCol iFiCol, EventHandler<KeyEvent> customKeyEvent) {

        if (iFiCol.getFilterNodeClass().equals(FxTextField.class.getName())) {
            FxTextField comp = (FxTextField) iFiCol.getColFilterNode();
            if (customKeyEvent != null) {
                comp.setOnKeyReleased(customKeyEvent);
            } else {
                comp.setOnKeyReleased(iFiCol.getColFilterKeyEvent());
            }

        }

        if (iFiCol.getFilterNodeClass().equals(FxTextFieldBtn.class.getName())) {
            FxTextFieldBtn comp = (FxTextFieldBtn) iFiCol.getColFilterNode();
            if (customKeyEvent != null) {
                comp.setOnKeyReleased(customKeyEvent);
            } else {
                comp.setOnKeyReleased(iFiCol.getColFilterKeyEvent());
            }

        }

        if (iFiCol.getFilterNodeClass().equals(FxTextFieldBtnCsv.class.getName())) {
            FxTextFieldBtnCsv comp = (FxTextFieldBtnCsv) iFiCol.getColFilterNode();
            if (customKeyEvent != null) {
                comp.setOnKeyReleased(customKeyEvent);
            } else {
                comp.setOnKeyReleased(iFiCol.getColFilterKeyEvent());
            }

        }

        if (iFiCol.getFilterNodeClass().equals(FxDatePicker.class.getName())) {
            FxDatePicker comp = (FxDatePicker) iFiCol.getColFilterNode();
            comp.setOnKeyReleased(iFiCol.getColFilterKeyEvent());
            if (customKeyEvent != null) {
                comp.setOnKeyReleased(customKeyEvent);
            } else {
                comp.setOnKeyReleased(iFiCol.getColFilterKeyEvent());
            }
        }

        if (iFiCol.getFilterNodeClass().equals(DatePicker.class.getName())) {
            DatePicker comp = (DatePicker) iFiCol.getColFilterNode();
            if (customKeyEvent != null) {
                comp.setOnKeyReleased(customKeyEvent);
            } else {
                comp.setOnKeyReleased(iFiCol.getColFilterKeyEvent());
            }
        }

        if (iFiCol.getFilterNodeClass().equals(TextField.class.getName())) {
            TextField comp = (TextField) iFiCol.getColFilterNode();
            if (customKeyEvent != null) {
                comp.setOnKeyReleased(customKeyEvent);
            } else {
                comp.setOnKeyReleased(iFiCol.getColFilterKeyEvent());
            }
        }


    }

    public static void registerKeyEventForNode(Node prmNode, String prmNodeClass, EventHandler<KeyEvent> customKeyEvent) {

        prmNode.addEventHandler(KeyEvent.KEY_PRESSED, customKeyEvent);

    }

    public static <E> E bindFormToEntityByFilterNode(List<? extends IFiCol> listColumns, Class<E> clazz) {

        return bindFormToEntityMainByNode(listColumns, clazz, iFiTableCol -> {
            return getNodeObjValueByFilterNode(iFiTableCol, iFiTableCol.getFilterNodeClass());
        });

    }

    public static <E> E bindFormToEntityByEditorNode(List<? extends IFiCol> listColumns, Class<E> clazz) {

        return bindFormToEntityMainByNode(listColumns, clazz, iFiTableCol -> {
            return getNodeObjValue(iFiTableCol, iFiTableCol.getColEditorClass());
        });

    }

    public static <E> E bindFormToEntityMainByNode(List<? extends IFiCol> listColumns, Class<E> clazz, Function<IFiCol, Object> fnGetCellObjectValue) {

        E entity = null;

        try {
            entity = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < listColumns.size(); i++) {

            IFiCol iFiTableCol = listColumns.get(i);

//			Loghelper.get(FxEditorFactory.class).debug("FiCol:"+ iFiTableCol.getFieldName());

            if (iFiTableCol.getOfcTxFieldName() == null) {
                continue;
            }

            // Object cellvalue = getEditorObjValueByEditorNode(iFiTableCol, iFiTableCol.getColEditorClass());
            // map.get(iFiTableCol.getHeader());
            Object cellvalue = fnGetCellObjectValue.apply(iFiTableCol);

//			Loghelper.get(FxEditorFactory.class).debug("FiCol Value:"+ FiString.toStringOrNull(cellvalue));

            try {
                PropertyUtils.setProperty(entity, iFiTableCol.getOfcTxFieldName().trim(), cellvalue);

                // if (cellvalue != null && ((String) cellvalue).isEmpty()) cellvalue = null;

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return entity;
    }

    public static <E> void bindEntityToFormCompsByFilterValue(List<? extends IFiCol> listColumns, E entity) {

        if (entity == null) return;

        for (int i = 0; i < listColumns.size(); i++) {

            IFiCol fiCol = listColumns.get(i);

            if (fiCol.getOfcTxFieldName() == null) continue;

            Object cellvalue = FiReflection.getProperty(entity, fiCol.getOfcTxFieldName());

            //Loghelperr.getInstance(getClass()).debug("Entity to Editor: Col:"+ fiCol.getFieldName());

            if (cellvalue == null) continue;

            // Bütün alanlar için de atanabilir , eski değer tutulmuş olur.
            fiCol.setFilterValue(cellvalue);

            // Hidden ise component e değer ataması yapılmaz
            if (FiBool.isTrue(fiCol.getBoHidden())) {
                continue;
            }

            setValueNodeCompMain(fiCol, cellvalue, fiCol.getFilterNodeClass(), fiCol.getColFilterNode(), entity);

            // Form Node özellikleri buradan atanır
            if (fiCol.getColFilterNode() != null) {
                if (FiBool.isTrue(fiCol.getBoNonEditableForForm())) {
                    fiCol.getColFilterNode().setDisable(true);
                }
            }


        }

    }

    /**
     * Kullanım Alanları
     * <p>
     * Entitydeki alanların değerini FiCol Editor Componetlere verir. Örnergin FxForm'da
     *
     * @param listColumns
     * @param entity
     * @param <E>
     */
    public static <E> void updateFiColsCompsWitFormEntityByEditorValue(List<? extends IFiCol> listColumns, E entity) {

        if (entity == null) return;

        for (int i = 0; i < listColumns.size(); i++) {

            IFiCol fiCol = listColumns.get(i);

            if (fiCol.getOfcTxFieldName() == null) continue;

            Object cellvalue = FiReflection.getProperty(entity, fiCol.getOfcTxFieldName());
            //Loghelperr.getInstance(getClass()).debug("Entity to Editor: Col:"+ fiCol.getFieldName());

            if (cellvalue == null) continue;

            // Bütün alanlar için de atanabilir , eski değer tutulmuş olur.
            fiCol.setColValue(cellvalue);

            // Hidden ise, node comp üretilmediği için değer ataması yapılmaz.
            if (FiBool.isTrue(fiCol.getBoHidden())) {
                continue;
            }

            //FiConsole.printFieldsNotNull(fiCol);
            setValueNodeCompMain(fiCol, cellvalue, fiCol.getColEditorClass(), fiCol.getColEditorNode(), entity);

            // Form Node özellikleri buradan atanır
            if (fiCol.getColEditorNode() != null) {
                if (FiBool.isTrue(fiCol.getBoNonEditableForForm())) {
                    fiCol.getColEditorNode().setDisable(true);
                }
            }


        }

    }

    public static void updateFiColsCompsWitFkbEntityByEditorValue(List<FiCol> listColumns, FiKeyBean fkbEntity) {

        if (fkbEntity == null) return;

        for (int i = 0; i < listColumns.size(); i++) {

            FiCol fiCol = listColumns.get(i);

            if (fiCol.getOfcTxFieldName() == null) continue;

            Object cellvalue = fkbEntity.get(fiCol.getOfcTxFieldName());
            //Loghelperr.getInstance(getClass()).debug("Entity to Editor: Col:"+ fiCol.getFieldName());

            if (cellvalue == null) continue;

            // Bütün alanlar için de atanabilir , eski değer tutulmuş olur.
            fiCol.setColValue(cellvalue);

            // Hidden ise, node comp üretilmediği için değer ataması yapılmaz.
            if (FiBool.isTrue(fiCol.getBoHidden())) {
                continue;
            }

            //FiConsole.printFieldsNotNull(fiCol);
            setValueNodeCompMain(fiCol, cellvalue, fiCol.getColEditorClass(), fiCol.getColEditorNode(), null);

            // Form Node özellikleri buradan atanır
            if (fiCol.getColEditorNode() != null) {
                if (FiBool.isTrue(fiCol.getBoNonEditableForForm())) {
                    fiCol.getColEditorNode().setDisable(true);
                }
            }


        }

    }

    public static FiKeyBean bindFormToFkbByFilterNodeDraft(List<? extends IFiCol> listColumns) {

        FiKeyBean fiKeyBean = new FiKeyBean();

        for (int i = 0; i < listColumns.size(); i++) {

            IFiCol ozTableCol = listColumns.get(i);
            Object cellvalue = getNodeObjValueByFilterNode(ozTableCol);    // map.get(ozTableCol.getHeader());
            //Loghelperr.getInstance(getClass()).debug(" map param cell value :" + cellvalue.toString() + " class:" + cellvalue.getClass().getSimpleName());
            fiKeyBean.put(ozTableCol.getOfcTxFieldName().trim(), cellvalue);
        }

        return fiKeyBean;

    }

    public static FiKeyBean bindFormToFkbByEditorNode(List<? extends IFiCol> listColumns) {
        FiKeyBean fiKeyBean = new FiKeyBean();
        for (int i = 0; i < listColumns.size(); i++) {
            IFiCol iFiCol = listColumns.get(i);
            Object cellvalue = getNodeObjValue(iFiCol, iFiCol.getColEditorClass());    // map.get(iFiCol.getHeader());
            //Loghelperr.getInstance(getClass()).debug(" map param cell value :" + cellvalue.toString() + " class:" + cellvalue.getClass().getSimpleName());
            fiKeyBean.put(iFiCol.getOfcTxFieldName().trim(), cellvalue);
        }
        return fiKeyBean;
    }

    /**
     * putFiCol metodu kullanıldı, FiCol eklendi FiKeybean'e
     *
     * @param listColumns
     * @return
     */
    public static FiKeyBean getFkbColsByEditorNodeForFiCols(List<FiCol> listColumns) {

        FiKeyBean fiKeyBean = new FiKeyBean();

        if(listColumns == null) return fiKeyBean;

        for (int i = 0; i < listColumns.size(); i++) {
            FiCol fiCol = listColumns.get(i);
            Object cellvalue = getNodeObjValue(fiCol, fiCol.getColEditorClass());    // map.get(fiCol.getHeader());
            //Loghelperr.getInstance(getClass()).debug(" map param cell value :" + cellvalue.toString() + " class:" + cellvalue.getClass().getSimpleName());
            //fiKeyBean.put(fiCol.getFieldName().trim(), cellvalue);
            fiKeyBean.putFiCol(fiCol, cellvalue);
        }
        return fiKeyBean;
    }

    public static void bindFormValueToFiColListByEditor(List<FiCol> listColumns) {

        for (int i = 0; i < listColumns.size(); i++) {
            FiCol fiCol = listColumns.get(i);
            Object cellvalue = getNodeObjValue(fiCol, fiCol.getColEditorClass());
            fiCol.setColValue(cellvalue);
        }

    }


    /**
     * Önemli !!! <br>
     * Editorden gelen boş string ler null olarak yorumlandı.
     *
     * @param iFiCol
     * @return
     */
    public static Object getNodeObjValueByFilterNode(IFiCol iFiCol) {
        //return getEditorObjValueByFilterNode(IFiTableCol,iFiCol.getColFilterNodeClass());
        return getValueNodeCompMain(iFiCol.getColType(), iFiCol.getFilterNodeClass(), iFiCol.getColFilterNode(), iFiCol.getFilterValue());
    }


    public static Object getNodeObjValueByFilterNode(IFiCol iFiCol, String prmEditorClass) {
        return getValueNodeCompMain(iFiCol.getColType(), prmEditorClass, iFiCol.getColFilterNode(), iFiCol.getFilterValue());
    }

    public static Object getNodeObjValue(IFiCol iFiCol, String txEditorClass) {
        return getValueNodeCompMain(iFiCol.getColType(), txEditorClass, iFiCol.getColEditorNode(), iFiCol.getColValue());
    }

    public static Object getNodeObjValue(FiCol fiCol, String txEditorClass) {
        return getValueNodeCompMain(fiCol.getColType(), txEditorClass, fiCol.getColEditorNode(), fiCol.getColValue());
    }

    public static Object getNodeObjValue(FiCol fiCol) {
        return getValueNodeCompMain(fiCol.getColType(), fiCol.getColEditorClass(), fiCol.getColEditorNode(), fiCol.getColValue());
    }

    public static String getNodeObjValueAsString(FiCol fiCol) {
        Object valueFromNodeCompMain = getValueNodeCompMain(fiCol.getColType(), fiCol.getColEditorClass(), fiCol.getColEditorNode(), fiCol.getColValue());

        if (valueFromNodeCompMain instanceof String) {
            return valueFromNodeCompMain.toString();
        }

        return null;
    }

    /**
     * textValue , ozColType göre tipine çevirerek dönüş yapar.
     * <p>
     * mesala ozColType integer ise integer tipine çevirerek object olarak dönüş yapar.
     *
     * @param ozColType
     * @param textValue
     * @return
     */
    public static Object toTypeByOzColType(OzColType ozColType, String textValue) {

        // gelen veride özel işaretler temizlenir
        if (ozColType == OzColType.Double || ozColType == OzColType.Integer) {
            if (textValue != null) textValue = textValue.replaceAll("(<|>|!)", "");
        }

        if (ozColType == OzColType.String) {
            return textValue;
        }

        if (ozColType == OzColType.Integer) {
            Object cellvalue = FiNumber.strToInt(textValue); //Integer.parseInt(textValue);
            return cellvalue;
        }

        if (ozColType == OzColType.Double) {
            Object cellvalue = FiNumber.strToDouble(textValue);
            return cellvalue;
        }

        if (ozColType == OzColType.Date) {
            Object cellvalue = FiDate.strToDateGeneric(textValue);
            return cellvalue;
        }

//		if (ozColType == OzColType.DateTimeIso) {
//			Object cellvalue = FiDate.strToDateGeneric2()togetNowStringAsIso8601Date(textValue);
//			return cellvalue;
//		}

        if (ozColType == OzColType.CommaSeperatedStr) {
            List<String> listData = FiCollection.commaSeperatedParseToStrList(textValue);
            return listData;
        }

        if (ozColType == OzColType.BoolEvetHayir) {
            if (FiString.isEmpty(textValue)) return null;

            if (textValue.equalsIgnoreCase("E") || textValue.equalsIgnoreCase("EVET")) {
                return true;
            }
            if (textValue.equalsIgnoreCase("H") || textValue.equalsIgnoreCase("HAYIR")) {
                return false;
            }
            return null;
        }

        if (ozColType == OzColType.Boolean) {
            if (FiString.isEmpty(textValue)) return null;

            if (textValue.equalsIgnoreCase("+")) {
                return true;
            }
            if (textValue.equalsIgnoreCase("-")) {
                return false;
            }

            return null;
        }

        return textValue;
    }


    private static Object getColFilterValueByColType(IFiCol IFiTableCol) {
        return IFiTableCol.getFilterValue();
    }


    public static String getValueOfFilterNodeAsString(IFiCol IFiTableCol) {
        String cellvalue = null;
//	Boolean binded = false;
        String editorClass = IFiTableCol.getFilterNodeClass();

        if (editorClass == null) editorClass = "";

        if (editorClass.equals(FxTextField.class.getName()) || editorClass.equals(FxTextFieldBtn.class.getName())) {
            String textValue = null;

            if (editorClass.equals(FxTextField.class.getName())) {
                FxTextField comp = (FxTextField) IFiTableCol.getColFilterNode();
                textValue = comp.getText();
            }

            if (editorClass.equals(FxTextFieldBtn.class.getName())) {
                FxTextFieldBtn comp = (FxTextFieldBtn) IFiTableCol.getColFilterNode();
                textValue = comp.getFxTextField().getText();
            }
            cellvalue = textValue;
            if (cellvalue.equals("")) return null;
            return cellvalue;
        }

        if (editorClass.equals(FxLabel.class.getName())) {
            if (IFiTableCol.getColFilterNode() != null) {
                FxLabel comp = (FxLabel) IFiTableCol.getColFilterNode();
                cellvalue = comp.getText();
                if (cellvalue.equals("")) return null;
                return cellvalue;
            }
        }

        if (editorClass.equals(DatePicker.class.getName())) {

            DatePicker comp = (DatePicker) IFiTableCol.getColFilterNode();
            if (comp.getValue() != null) {
                cellvalue = FiDate.dateToStrGlobalFormatAsyyyyHmmHdd(FiDate.convertLocalDateToSimpleDate(comp.getValue()));
                return cellvalue;
            } else {
                String txValueEditor = comp.getEditor().getText();

                if (txValueEditor.equals("")) return null;
                return txValueEditor;

            }

        }

        if (editorClass.equals(FxDatePicker.class.getName())) {

            FxDatePicker comp = (FxDatePicker) IFiTableCol.getColFilterNode();
            if (comp.getValue() != null) {
                //Loghelperr.getInstance(getClass()).debug("comp getvalue"+ comp.getValue());
                cellvalue = FiDate.dateToStrGlobalFormatAsyyyyHmmHdd(FiDate.convertLocalDateToSimpleDate(comp.getValue()));
                return cellvalue;
            } else {
                String txValueEditor = comp.getEditor().getText();
                //Loghelperr.getInstance(getClass()).debug("editor getvalue"+ txValueEditor);
                if (txValueEditor.equals("")) return null;
                return txValueEditor;
            }
        }

        //if (cellvalue != null) return cellvalue;

        // infTableCol.getColFxNodeClass() == null &&
        if (IFiTableCol.getFilterValue() != null) {

            // comment off 20190719-1524
//			if (!binded && infTableCol.getFiValue().equals("")) {
//				cellvalue = null;
//				binded = true;
//			}
//
//			if (!binded && infTableCol.getColType() == OzColType.Integer) {
//				cellvalue = infTableCol.getFiValue().toString();
//				binded = true;
//			}
//
//			if (!binded && infTableCol.getColType() == OzColType.Double) {
//				cellvalue = infTableCol.getFiValue().toString();
//				binded = true;
//			}
//
//			if (!binded) {
//				cellvalue = infTableCol.getFiValue().toString();
//			}

            //return cellvalue;
            return IFiTableCol.getFilterValue().toString();
        }

        return cellvalue;
    }

    public static void changeToEditMode(List<? extends IFiCol> listColumns) {

        for (int i = 0; i < listColumns.size(); i++) {

            IFiCol ozTableCol = listColumns.get(i);

            if (FiBool.isTrue(ozTableCol.getBoHidden())) {
                continue;
            }

            if (FiBool.isTrue(ozTableCol.getBoEditable())) continue;

            // editable is Null or False , then

            if (ozTableCol.getFilterNodeClass().equals(FxTextField.class.getName())) {
                FxTextField comp = (FxTextField) ozTableCol.getColFilterNode();
                comp.setDisable(true);
            }

            if (ozTableCol.getFilterNodeClass().equals(DatePicker.class.getName())) {
                DatePicker comp = (DatePicker) ozTableCol.getColFilterNode();
                comp.setDisable(true);
            }

            if (ozTableCol.getFilterNodeClass().equals(FxLabel.class.getName())) {
                FxLabel comp = (FxLabel) ozTableCol.getColFilterNode();
            }


        }


    }

    /**
     * change listener generic olmalı
     *
     * @param ozTableCol
     * @param changeListener
     * @param <T>
     */
    public static <T> void registerChangeListenerToEditor(IFiCol ozTableCol, ChangeListener changeListener) {

        if (ozTableCol.getFilterNodeClass().equals(FxTextField.class.getName())) {
            FxTextField comp = (FxTextField) ozTableCol.getColFilterNode();
            comp.textProperty().addListener(changeListener);
        }

        if (ozTableCol.getFilterNodeClass().equals(FxTextFieldBtn.class.getName())) {
            FxTextFieldBtn comp = (FxTextFieldBtn) ozTableCol.getColFilterNode();
            comp.getFxTextField().textProperty().addListener(changeListener);
        }

        if (ozTableCol.getFilterNodeClass().equals(FxDatePicker.class.getName())) {
            FxDatePicker comp = (FxDatePicker) ozTableCol.getColFilterNode();
            //comp.valueProperty().addListener(changeListener);
            comp.getEditor().textProperty().addListener(changeListener);

        }

        if (ozTableCol.getFilterNodeClass().equals(DatePicker.class.getName())) {
            DatePicker comp = (DatePicker) ozTableCol.getColFilterNode();
            //comp.promptTextProperty().addListener(changeListener);
            comp.getEditor().textProperty().addListener(changeListener);
        }

        if (ozTableCol.getFilterNodeClass().equals(TextField.class.getName())) {
            TextField comp = (TextField) ozTableCol.getColFilterNode();
            comp.textProperty().addListener(changeListener);
        }

    }

    /**
     * change listener generic olmalı
     *
     * @param <T>
     * @param iFiCol
     * @param fncStrProp
     * @param durationMilis
     */
    public static <T> void registerTextPropertyWithDurationForFilterNode(IFiCol iFiCol, Consumer<String> fncStrProp, long durationMilis) {
        registerTextPropertyWithDurationForNode(fncStrProp, durationMilis, iFiCol.getFilterNodeClass(), iFiCol.getColFilterNode());
    }

    /**
     * change listener generic olmalı
     *
     * @param <T>
     * @param ozTableCol
     * @param consumerStrProp
     * @param durationMilis
     */
    public static <T> void registerTextPropertyWithDurationForEditorNode(IFiCol ozTableCol, Consumer<String> consumerStrProp, long durationMilis) {

        String colFilterNodeClass = ozTableCol.getColEditorClass();
        Node colFilterNode = ozTableCol.getColEditorNode();

        registerTextPropertyWithDurationForNode(consumerStrProp, durationMilis, colFilterNodeClass, colFilterNode);

    }

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


    private static <EntClazz> List<IFiCol> getEmptyColsWhichRequiredOrNotNullable(List<? extends IFiCol> listCol, EntClazz entity) {

        List<IFiCol> listEmptyCols = new ArrayList<>();

        for (IFiCol iFiTableCol : listCol) {

            if (FiBool.isTrue(iFiTableCol.getBoRequired()) || FiBool.isFalse(iFiTableCol.getBoNullable())) {

                Object propertyNested = FiReflection.getPropertyNested(entity, iFiTableCol.getOfcTxFieldName());

                if (propertyNested == null) {
                    listEmptyCols.add(iFiTableCol);
                }

                if (propertyNested instanceof String && FiString.isEmpty((String) propertyNested)) {
                    listEmptyCols.add(iFiTableCol);
                }

            }

        }

        return listEmptyCols;

    }

    /**
     * True means exist no error
     * False means exist error
     *
     * @param colsForm
     * @param formEntity
     * @return
     */
    public static boolean checkColumnsForNullAndOtherErrors(List<? extends IFiCol> colsForm, Object formEntity) {

        List<IFiCol> emptyColsWhichNotNullable = FxEditorFactory.getEmptyColsWhichRequiredOrNotNullable(colsForm, formEntity);

        if (emptyColsWhichNotNullable.size() > 0) {
            FxDialogShow.showPopWarn("Lütfen Gerekli Alanları Doldurunuz");
            return false;
        }
        return true;
    }

    /**
     * Null , Required Checks
     * <p>
     * Form verileri kontrol edilirken kullanılıyor
     *
     * @param colsForm
     * @return
     */
    public static Fdr validateFiCols(List<FiCol> colsForm) {

        for (FiCol fiTableCol : colsForm) {

            // boRequired True, BoNullable False yapılmışsa boş geçilemez.
            if (FiBool.isTrue(fiTableCol.getBoRequired()) || FiBool.isFalse(fiTableCol.getBoNullable())) {

                Object cellValue = fiTableCol.getColValue();

                // Null ve Boş Alan Kontrolü
                if (cellValue == null ||
                        (cellValue instanceof String && FiString.isEmpty((String) cellValue))) {
//					return new FdrResult(false, "Lütfen Gerekli Alanları Doldurunuz");
                    return new Fdr(false, String.format("%s Alanı Zorunludur.Boş Geçilemez.", fiTableCol.getOfcTxHeader()));
                }

            }
        }

        return new Fdr(true);
    }

    /**
     * getBoRequired,getBoNullable özellikleri kontrol ediliyor.
     *
     * @param colsForm
     * @return
     */
    public static Fdr checkColErrorsByEditor(List<FiCol> colsForm) {

        for (FiCol fiTableCol : colsForm) {

            // boRequired True, BoNullable False yapılmışsa boş geçilemez.
            if (FiBool.isTrue(fiTableCol.getBoRequired()) || FiBool.isFalse(fiTableCol.getBoNullable())) {

                //Object cellValue = fiTableCol.getColEditorValue();
                Object cellValue = FxEditorFactory.getNodeObjValue(fiTableCol, fiTableCol.getColEditorClass());

                // Null ve Boş Alan Kontrolü
                if (cellValue == null ||
                        (cellValue instanceof String && FiString.isEmpty((String) cellValue))) {
//					return new FdrResult(false, "Lütfen Gerekli Alanları Doldurunuz");
                    return new Fdr(false, String.format("%s Alanı Zorunludur. Geçerli bir değer giriniz.", fiTableCol.getOfcTxHeader()));
                }

            }
        }

        return new Fdr(true);
    }

    private static void clearValueNodeCompMain(FiCol fiCol, String colNodeClass, Node colNode) {

        //String colNodeClass = fiCol.getColFilterNodeClass();

        if (colNode != null && colNode instanceof IFiNode) {
            IFiNode IFiNode = (IFiNode) colNode;
//			ifxNode.setCompValue(cellvalue);
            IFiNode.setCompValue("");
            return;
        }

        if (colNodeClass == null) {
            return;
        }

        //Node colNode = fiCol.getColFilterNode();

        if (colNodeClass.equals(FxTextField.class.getName())) {
            //FiConsole.debug(fiCol);
            FxTextField comp = (FxTextField) colNode;
            comp.setText("");
            comp.setTxValue("");
        }

        if (colNodeClass.equals(FxTextFieldBtn.class.getName())) {

            FxTextFieldBtn comp = (FxTextFieldBtn) colNode;
            comp.setTxValue("");
            comp.getFxTextField().setText("");
//			if (fiCol.getFnEditorNodeValueFormmatter() != null) {
//				comp.getFxTextField().setText(fiCol.getFnEditorNodeValueFormmatter().apply(entity).toString());
//			} else {
//				comp.getFxTextField().setText(cellvalue.toString());
//			}

        }

        if (colNodeClass.equals(FxTextFieldBtnWitLbl3.class.getName())) {

            FxTextFieldBtnWitLbl3 comp = (FxTextFieldBtnWitLbl3) colNode;
            // Gerçek değeri txvalue da tutulur.
            comp.setTxValue("");
            comp.getFxTextField().setText("");
//			if (fiCol.getFnEditorNodeValueFormmatter() != null) {
//				comp.getFxTextField().setText(fiCol.getFnEditorNodeValueFormmatter().apply(entity).toString());
//			} else {
//				comp.getFxTextField().setText(cellvalue.toString());
//			}

        }

        if (colNodeClass.equals(FxDatePicker.class.getName())) {

            FxDatePicker comp = (FxDatePicker) colNode;
            comp.setValue(null);
//			comp.setValue(FiDate.convertLocalDate((Date) cellvalue));

        }

        if (colNodeClass.equals(DatePicker.class.getName())) {

            DatePicker comp = (DatePicker) colNode;
//			comp.setValue(FiDate.convertLocalDate((Date) cellvalue));
            comp.setValue(null);

        }

        if (colNodeClass.equals(FxLabel.class.getName())) {
            FxLabel comp = (FxLabel) colNode;
            comp.setText("");
            comp.setTxValue("");
        }

        // fxcombobox comboitem ise

        if (colNodeClass.equals(FxComboBox.class.getName())) {
            FxComboBox comp = (FxComboBox) colNode;
            comp.setTxValue("");
            comp.getSelectionModel().select(null);
        }

        if (colNodeClass.equals(FxComboBoxSimple.class.getName())) {
            FxComboBoxSimple comp = (FxComboBoxSimple) colNode;
            comp.setTxValue("");
            comp.getSelectionModel().select(null);
        }

        if (colNodeClass.equals(FxChoiceBox.class.getName())) {
            FxChoiceBox comp = (FxChoiceBox) colNode;
            comp.setTxValue("");
        }

        if (colNodeClass.equals(FxCheckBox.class.getName())) {
            FxCheckBox comp = (FxCheckBox) colNode;
            comp.setSelected(false);
//			if (cellvalue != null) {
//				Boolean boValue = (Boolean) cellvalue;
//				comp.setSelected(boValue);
//			}
        }

    }

}
