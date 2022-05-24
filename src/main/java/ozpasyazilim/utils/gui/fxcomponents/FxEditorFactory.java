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

import ozpasyazilim.utils.datatypes.FiMapParams;
import ozpasyazilim.utils.fidborm.FiEntity;
import ozpasyazilim.utils.fidborm.FiField;
import ozpasyazilim.utils.gui.components.ComboItem;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.core.FiReflection;
import ozpasyazilim.utils.returntypes.Fdr;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.FiColInfHelper;
import ozpasyazilim.utils.table.OzColType;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Node Comp'lerin getValue ve setValue değerleri ayarlanır, generateNodeByClassName ile node component oluşturulur
 */
public class FxEditorFactory {

	Map<String, String> mapSimpleClassToEditorClass;
	private static Loghelper logger = Loghelper.getLogHelper(FxEditorFactory.class);

	public static Boolean setTextValueEditorNodeByString(List<? extends IFiCol> listFormElements, Object fieldName, String value, Object entity) {

		IFiCol columnByFieldName = FiColInfHelper.build(listFormElements).findColumnByFieldName(fieldName.toString());

		if (columnByFieldName == null) return false;

		setNodeValueForCompsMain(columnByFieldName, value, columnByFieldName.getColEditorClass(), columnByFieldName.getColEditorNode(), entity);
		return true;

	}

	public static Node getEditorNodeByFieldName(List<? extends IFiCol> listFormElements, Object fieldName) {

		IFiCol columnByFieldName = FiColInfHelper.build(listFormElements).findColumnByFieldName(fieldName.toString());

		if (columnByFieldName == null) return null;

		return columnByFieldName.getColEditorNode();
	}

	public static <PrmEntClazz> Fdr checkColumnsByClazzAnno(PrmEntClazz entity, Boolean boTrimIfNeed) {

		Class<?> aClass = entity.getClass();

		List<FiField> fiFieldList = FiEntity.getListFieldsShortWithId(aClass);
		Fdr fdr = new Fdr();
		fdr.setBoResult(true);

		for (FiField fiField : fiFieldList) {

			Object property = null;

			if (FiBoolean.isFalse(fiField.getNullable())) {

				if (property == null) property = FiReflection.getProperty(entity, fiField.getName());

				if (property == null) {
					fdr.setBoResult(false);
					fdr.append("alan null olamaz:" + fiField.getName());
				}

			}

			if (fiField.getLength() != null && fiField.getLength() > 0 && fiField.getClassNameSimple().equals("String")) {

				if (property == null) property = FiReflection.getProperty(entity, fiField.getName());

				if (property != null) {
					String txProperty = (String) property;
					if (txProperty.length() > fiField.getLength()) {

						if (FiBoolean.isTrue(boTrimIfNeed)) {
							//Loghelper.debug(FxEditorFactory.class,"FiField Length:"+fiField.getLength());
							txProperty = txProperty.substring(0, fiField.getLength() - 1);
							FiReflection.setter(entity, fiField.getName(), txProperty);
						} else {
							fdr.setBoResult(false);
							fdr.append(String.format("Alan %s karakter fazla olamaz %s : %s"
									, fiField.getLength(), txProperty.length(), fiField.getName()));
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

		List<FiField> fiFieldList = FiEntity.getListFieldsShortWithId(aClass);
		Fdr fdr = new Fdr();
		fdr.setBoResult(true);

		for (FiField fiField : fiFieldList) {

			Object property = null;

			if (fiField.getLength() != null && fiField.getLength() > 0 && fiField.getClassNameSimple().equals("String")) {

				if (property == null) property = FiReflection.getProperty(entity, fiField.getName());

				if (property != null) {
					String txProperty = (String) property;
					if (txProperty.length() > fiField.getLength()) {

						if (FiBoolean.isTrue(boTrimIfNeed)) {
							//Loghelper.debug(FxEditorFactory.class,"FiField Length:"+fiField.getLength());
							txProperty = txProperty.substring(0, fiField.getLength() - 1);
							FiReflection.setter(entity, fiField.getName(), txProperty);
							fdr.setBoResult(true);
						} else {
							fdr.setBoResult(false);
							fdr.append(String.format("Alan %s karakter fazla olamaz %s : %s"
									, fiField.getLength(), txProperty.length(), fiField.getName()));
						}

					}
				}
			}

		}

		return fdr;
	}

	public static void clearFormFields(List<FiCol> listFormElements) {

		for (FiCol listFormElement : listFormElements) {
			clearValueForNodeCompMain(listFormElement, listFormElement.getColEditorClass(), listFormElement.getColEditorNode());
		}

	}

	public static void setNodeValue(FiCol fiTableCol, Object objValue) {
		if (fiTableCol == null || fiTableCol.getColEditorNode() == null) {
			return;
		}

		setNodeValueByCompClass(fiTableCol.getColEditorNode(), fiTableCol.getColEditorClass(), objValue);

	}

	public static FiMapParams bindFiColToMapByFilterNode(List<FiCol> fiColList) {

		FiMapParams fiMapParams = new FiMapParams();

		for (FiCol fiCol : fiColList) {

			Object nodeObjValue = getNodeObjValueByFilterNode(fiCol, fiCol.getFilterNodeClass());

			//FIXME buraya alan string kontrolü eklenmeli
			if (FiBoolean.isTrue(fiCol.getBoFilterLike()) && nodeObjValue != null && nodeObjValue instanceof String) {
				fiMapParams.add(fiCol.getFieldName(), "%" + nodeObjValue + "%");
				continue;
			}
			fiMapParams.add(fiCol.getFieldName(), nodeObjValue);
		}

		return fiMapParams;
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
	 * @param ozTableCol
	 * @return
	 */
	public static Node generateAndSetFilterNode(IFiCol ozTableCol) {

		Node comp = generateNodeByClassNameMain(ozTableCol.getColType(), ozTableCol.getFilterNodeClass(), ozTableCol);

		// generator içinden çıkarılıp buraya eklendi, esas amaç anlaşılmadı
		setNodeValueByCompClass(comp, ozTableCol.getFilterNodeClass(), ozTableCol.getFilterValue());

		if (ozTableCol.getFilterNodeClass() == null) ozTableCol.setFilterNodeClass(comp.getClass().getName());
		if (comp != null) ozTableCol.setColFilterNode(comp);
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
	 * FitableCol editor node'una set ve render eder
	 * <p>
	 * Kullanan Compler
	 * FxFormMig
	 *
	 * @param fiTableCol
	 * @return
	 */
	public static Node generateAndRenderNodeBeforeLoadByEditorClassEntry(IFiCol fiTableCol, Object entity) {

		Node comp = generateNodeByClassNameMain(fiTableCol.getColType(), fiTableCol.getColEditorClass(), fiTableCol);

		if (fiTableCol.getFnEditorNodeRendererBeforeSettingValue() != null) {
			fiTableCol.getFnEditorNodeRendererBeforeSettingValue().accept(entity, comp);
		}

		if (fiTableCol.getColEditorClass() == null) fiTableCol.setColEditorClass(comp.getClass().getName());
		if (comp != null) fiTableCol.setColEditorNode(comp);

		if (comp instanceof IfxNode) {
			fiTableCol.setIfxNodeEditor((IfxNode) comp);
			//logger.getLogger().debug("ifxnode ayarlandı");
		}

		// generator içinden çıkarılıp buraya eklendi, esas amaç anlaşılmadı
		setNodeValueByCompClass(fiTableCol.getColEditorNode(), fiTableCol.getColEditorClass(), fiTableCol.getColEditorValue());

		return comp;
	}

	/**
	 * Kullanan Comp lar
	 * <p>
	 * FxFormMig
	 *
	 * @param fiCol
	 * @param entity
	 * @return
	 */
	public static Node  generateEditorNodeFullLifeCycle(FiCol fiCol, Object entity) {

		Node comp = generateNodeByClassNameMain(fiCol.getColType(), fiCol.getColEditorClass(), fiCol);

		if (fiCol.getFnNodeFocusTrigger()!=null) {
			comp.focusedProperty().addListener((observable, oldValue, newValue) -> fiCol.getFnNodeFocusTrigger().accept(comp));
		}

		if (fiCol.getColEditorClass() == null) fiCol.setColEditorClass(comp.getClass().getName());
		if (comp != null) fiCol.setColEditorNode(comp);

		if (comp instanceof IfxNode) {
			fiCol.setIfxNodeEditor((IfxNode) comp);
			//logger.getLogger().debug("ifxnode ayarlandı");
		}

		fiCol.doNodeOperationsBeforeSettingValue(entity, comp);

		// comp'a değer atanır
		setNodeValueByCompClass(fiCol.getColEditorNode(), fiCol.getColEditorClass(), fiCol.getColEditorValue());

		fiCol.doNodeOperationsAfterInitialValue1(entity, comp);
		fiCol.doNodeOperationsAfterInitialValue2(entity, comp);

		return comp;
	}

	public static void setNodeValueByCompClass(Node component, String compClass, Object compValue) {

		if (compClass.equals(DatePicker.class.getName())
				|| compClass.equals(FxDatePicker.class.getName())) {

			FxDatePicker comp = (FxDatePicker) component;
			//if (iFiTableCol.getColFilterKeyEvent() != null) comp.setOnKeyReleased(iFiTableCol.getColFilterKeyEvent());
			if (compValue != null && compValue instanceof Date) {
				comp.setValue(FiDate.convertLocalDate((Date) compValue));
			}
			//return comp;
		}

		if (component instanceof FxCheckBox) {

			if (compValue != null && compValue instanceof Boolean) {
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
	 * @param IFiTableCol
	 */
	public static void setAutoColEditorClassByColType(IFiCol IFiTableCol) {

		if (IFiTableCol.getColEditorClass() == null) {
			String autoColCellFactoryClassByType = FxEditorFactory.getAutoEditorClassMainByOzColType(IFiTableCol);
			IFiTableCol.setColEditorClass(autoColCellFactoryClassByType);
		}

	}

	/**
	 * Ozcoltype göre editor class belirler (formlarda uygulanıyor)
	 *
	 * @param IFiTableCol
	 * @return
	 */
	public static String getAutoEditorClassMainByOzColType(IFiCol IFiTableCol) {

		if (IFiTableCol.getColType() == OzColType.Date) {
			return FxDatePicker.class.getName();
		}

		if (IFiTableCol.getColType() == OzColType.Boolean) {
			return FxCheckBox.class.getName();
		}

		return null;

	}

	// Component Node oluşturmak için
	public static Node generateNodeByClassNameMain(OzColType ozColType, String txClassName, IFiCol iFiCol) { //, Object compValue

		String compClassName = txClassName; //iFiCol.getColFxNodeClass();
		//Loghelper.getInstance(FxEditorFactory.class).debug(" Ozcoltype:"+ozColType.toString());
		//Loghelper.getInstance(FxEditorFactory.class).debug(" Prm Comp Class:"+txClassName);

		if (compClassName == null) {
			//iFiCol.setColFxNodeClass(FxTextField.class.getName());
			compClassName = FxTextField.class.getName();

			// 14-12 eklendi
			if (ozColType == OzColType.Boolean) {
				compClassName = FxCheckBox.class.getName();
			}

		}

		//Loghelper.getInstance(FxEditorFactory.class).debug(" Comp Class:"+compClassName.toString());

		if (compClassName.equals(FxTextField.class.getName())
				|| compClassName.equals(FxTextField.class.getSimpleName())) {

			FxTextField comp = new FxTextField();

			if (ozColType == OzColType.Double) {
				comp.convertNumberDoubleTextField1();
				// Loghelper.get(FxEditorFactory.class).debug("Double Text Field Üretildi:"+ iFiCol.getHeaderName());

				//comp.setAlignment(Pos.BASELINE_RIGHT);
			}

			if (ozColType == OzColType.Integer) {
				comp.convertNumberDoubleTextField1();
				//comp.setAlignment(Pos.BASELINE_RIGHT);
			}

			//if (iFiCol.getColFilterKeyEvent() != null) comp.setOnKeyReleased(iFiCol.getColFilterKeyEvent());
			//iFiCol.setColFxNode(comp);
			return comp;
		}

		if (compClassName.equals(FxTextFieldBtn.class.getName())) {

			FxTextFieldBtn comp = new FxTextFieldBtn();

			if (ozColType == OzColType.Double) {
				comp.getFxTextField().convertNumberDoubleTextField1();
				comp.getFxTextField().setAlignment(Pos.BASELINE_RIGHT);
			}
			//iFiCol.setColFxNode(comp);
			return comp;
		}

		if (compClassName.equals(FxTextFieldWitBtnLbl3.class.getName())) {

			FxTextFieldWitBtnLbl3 comp = new FxTextFieldWitBtnLbl3();

			if (ozColType == OzColType.Double) {
				comp.getFxTextField().convertNumberDoubleTextField1();
				comp.getFxTextField().setAlignment(Pos.BASELINE_RIGHT);
			}
			//iFiCol.setColFxNode(comp);
			return comp;
		}

		if (compClassName.equals(FxTextFieldBtnLabel2.class.getName())) {

			FxTextFieldBtnLabel2 comp = new FxTextFieldBtnLabel2();

			if (ozColType == OzColType.Double) {
				comp.getFxTextField().convertNumberDoubleTextField1();
				comp.getFxTextField().setAlignment(Pos.BASELINE_RIGHT);
			}
			//iFiCol.setColFxNode(comp);
			return comp;
		}

		if (compClassName.equals(DatePicker.class.getName())
				|| compClassName.equals(FxDatePicker.class.getName())) {
			FxDatePicker comp = new FxDatePicker();
			//if (iFiCol.getColFilterKeyEvent() != null) comp.setOnKeyReleased(iFiCol.getColFilterKeyEvent());
			return comp;
		}

		if (compClassName.equals(FxLabel.class.getName())) {
			FxLabel comp = new FxLabel("");
			return comp;
		}

		if (compClassName.equals(FxLabelData.class.getName())) {
			FxLabelData comp = new FxLabelData("");
			return comp;
		}

		if (compClassName.equals(TextField.class.getName())) {
			FxTextField comp = new FxTextField();
			//if (iFiCol.getColFilterKeyEvent() != null) comp.setOnKeyReleased(iFiCol.getColFilterKeyEvent());
			return comp;
		}

		if (compClassName.equals(FxComboBox.class.getName())) {
			FxComboBox<ComboItem> comp = new FxComboBox<>();
			return comp;
		}

		if (compClassName.equals(FxComboBoxSimple.class.getName())) {
			FxComboBoxSimple comp = new FxComboBoxSimple();
			return comp;
		}

		if (compClassName.equals(FxCheckBox.class.getName())) {
			FxCheckBox comp = new FxCheckBox();
			return comp;
		}

		if (compClassName.equals(FxChoiceBoxSimple.class.getName())) {
			FxChoiceBoxSimple comp = new FxChoiceBoxSimple();
			comp.activateBackSpaceClearSelection();
			return comp;
		}

		return null;
	}

	public void registerEnterFnForFilterNode(IFiCol ozTableCol, EventHandler<KeyEvent> customKeyEvent) {

		if (ozTableCol.getFilterNodeClass().equals(FxTextField.class.getName())) {
			FxTextField comp = (FxTextField) ozTableCol.getColFilterNode();
			if (customKeyEvent != null) {
				comp.setOnKeyReleased(customKeyEvent);
			} else {
				comp.setOnKeyReleased(ozTableCol.getColFilterKeyEvent());
			}

		}

		if (ozTableCol.getFilterNodeClass().equals(FxTextFieldBtn.class.getName())) {
			FxTextFieldBtn comp = (FxTextFieldBtn) ozTableCol.getColFilterNode();
			if (customKeyEvent != null) {
				comp.setOnKeyReleased(customKeyEvent);
			} else {
				comp.setOnKeyReleased(ozTableCol.getColFilterKeyEvent());
			}

		}

		if (ozTableCol.getFilterNodeClass().equals(FxDatePicker.class.getName())) {
			FxDatePicker comp = (FxDatePicker) ozTableCol.getColFilterNode();
			comp.setOnKeyReleased(ozTableCol.getColFilterKeyEvent());
			if (customKeyEvent != null) {
				comp.setOnKeyReleased(customKeyEvent);
			} else {
				comp.setOnKeyReleased(ozTableCol.getColFilterKeyEvent());
			}
		}

		if (ozTableCol.getFilterNodeClass().equals(DatePicker.class.getName())) {
			DatePicker comp = (DatePicker) ozTableCol.getColFilterNode();
			if (customKeyEvent != null) {
				comp.setOnKeyReleased(customKeyEvent);
			} else {
				comp.setOnKeyReleased(ozTableCol.getColFilterKeyEvent());
			}
		}

		if (ozTableCol.getFilterNodeClass().equals(TextField.class.getName())) {
			TextField comp = (TextField) ozTableCol.getColFilterNode();
			if (customKeyEvent != null) {
				comp.setOnKeyReleased(customKeyEvent);
			} else {
				comp.setOnKeyReleased(ozTableCol.getColFilterKeyEvent());
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
			return getNodeObjValueByEditorNode(iFiTableCol, iFiTableCol.getColEditorClass());
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

			if (iFiTableCol.getFieldName() == null) {
				continue;
			}

			// Object cellvalue = getEditorObjValueByEditorNode(iFiTableCol, iFiTableCol.getColEditorClass());
			// map.get(iFiTableCol.getHeader());
			Object cellvalue = fnGetCellObjectValue.apply(iFiTableCol);

//			Loghelper.get(FxEditorFactory.class).debug("FiCol Value:"+ FiString.toStringOrNull(cellvalue));

			try {
				PropertyUtils.setProperty(entity, iFiTableCol.getFieldName().trim(), cellvalue);

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

	public static <E> void bindEntityToFormByFilterValue(List<? extends IFiCol> listColumns, E entity) {

		if (entity == null) return;

		for (int i = 0; i < listColumns.size(); i++) {

			IFiCol ozTableCol = listColumns.get(i);

			if (ozTableCol.getFieldName() == null) continue;

			Object cellvalue = FiReflection.getProperty(entity, ozTableCol.getFieldName());

			//Loghelperr.getInstance(getClass()).debug("Entity to Editor: Col:"+ ozTableCol.getFieldName());

			if (cellvalue == null) continue;

			// Bütün alanlar için de atanabilir , eski değer tutulmuş olur.
			ozTableCol.setFilterValue(cellvalue);

			// Hidden ise component e değer ataması yapılmaz
			if (FiBoolean.isTrue(ozTableCol.getBoHidden())) {
				continue;
			}

			setNodeValueForCompsMain(ozTableCol, cellvalue, ozTableCol.getFilterNodeClass(), ozTableCol.getColFilterNode(), entity);

			// Form Node özellikleri buradan atanır
			if (ozTableCol.getColFilterNode() != null) {
				if (FiBoolean.isTrue(ozTableCol.getBoNonEditableForForm())) {
					ozTableCol.getColFilterNode().setDisable(true);
				}
			}


		}

	}

	public static <E> void bindEntityToFormByEditorValue(List<? extends IFiCol> listColumns, E entity) {

		if (entity == null) return;

		for (int i = 0; i < listColumns.size(); i++) {

			IFiCol fiCol = listColumns.get(i);

			if (fiCol.getFieldName() == null) continue;

			Object cellvalue = FiReflection.getProperty(entity, fiCol.getFieldName());

			//Loghelperr.getInstance(getClass()).debug("Entity to Editor: Col:"+ fiCol.getFieldName());

			if (cellvalue == null) continue;

			// Bütün alanlar için de atanabilir , eski değer tutulmuş olur.
			fiCol.setColEditorValue(cellvalue);

			// Hidden ise, node comp üretilmediği için değer ataması yapılmaz.
			if (FiBoolean.isTrue(fiCol.getBoHidden())) {
				continue;
			}

			//FiConsole.printFieldsNotNull(fiCol);
			setNodeValueForCompsMain(fiCol, cellvalue, fiCol.getColEditorClass(), fiCol.getColEditorNode(), entity);

			// Form Node özellikleri buradan atanır
			if (fiCol.getColEditorNode() != null) {
				if (FiBoolean.isTrue(fiCol.getBoNonEditableForForm())) {
					fiCol.getColEditorNode().setDisable(true);
				}
			}


		}

	}

	public FiMapParams bindFormEditorToMapByFilterNode(List<? extends IFiCol> listColumns) {

		FiMapParams fiMapParams = new FiMapParams();

		for (int i = 0; i < listColumns.size(); i++) {

			IFiCol ozTableCol = listColumns.get(i);
			Object cellvalue = getNodeObjValueByFilterNode(ozTableCol);    // map.get(ozTableCol.getHeader());
			//Loghelperr.getInstance(getClass()).debug(" map param cell value :" + cellvalue.toString() + " class:" + cellvalue.getClass().getSimpleName());
			fiMapParams.put(ozTableCol.getFieldName().trim(), cellvalue);
		}

		return fiMapParams;

	}

	public static FiMapParams bindFormToKeyBeanByEditorNode(List<? extends IFiCol> listColumns) {
		FiMapParams fiMapParams = new FiMapParams();
		for (int i = 0; i < listColumns.size(); i++) {
			IFiCol iFiCol = listColumns.get(i);
			Object cellvalue = getNodeObjValueByEditorNode(iFiCol, iFiCol.getColEditorClass());    // map.get(iFiCol.getHeader());
			//Loghelperr.getInstance(getClass()).debug(" map param cell value :" + cellvalue.toString() + " class:" + cellvalue.getClass().getSimpleName());
			fiMapParams.put(iFiCol.getFieldName().trim(), cellvalue);
		}
		return fiMapParams;
	}

	public static void bindFormValueToFiColListByEditor(List<FiCol> listColumns) {

		for (int i = 0; i < listColumns.size(); i++) {
			FiCol fiCol = listColumns.get(i);
			Object cellvalue = getNodeObjValueByEditorNode(fiCol, fiCol.getColEditorClass());    // map.get(fiCol.getHeader());
			fiCol.setColEditorValue(cellvalue);
		}

	}

	/**
	 *
	 * Component değer ataması yapar, örneğin textfield ise textfield.setText değerine atama yaparak içeriğini ayarlar. Datepicker ise tarih değerini atar.
	 *
	 * @param iFiCol ilgili field/column tanımı
	 * @param cellvalue component verilecek değer
	 * @param colNodeClass node sınıf ismi Örnegin FxTextField gibi
	 * @param colNode component'in Node objesi
	 * @param entity değerin alındığı Entity. İşlevi ???
	 */
	private static void setNodeValueForCompsMain(IFiCol iFiCol, Object cellvalue, String colNodeClass, Node colNode, Object entity) {

		//String colNodeClass = iFiCol.getColFilterNodeClass();

		if (colNode instanceof IfxNode) {
			IfxNode ifxNode = (IfxNode) colNode;
			ifxNode.setCompValue(cellvalue);
			return;
		}

		if (colNodeClass == null) {
			return;
		}

		// Componentlere göre değer ataması

		if (colNodeClass.equals(FxTextField.class.getName())) {
			//FiConsole.debug(iFiCol);
			FxTextField comp = (FxTextField) colNode;
			comp.setText(cellvalue.toString());
		}

		if (colNodeClass.equals(FxTextFieldBtn.class.getName())) {

			FxTextFieldBtn comp = (FxTextFieldBtn) colNode;
			comp.setTxValue(cellvalue.toString());
			if (iFiCol.getFnEditorNodeValueFormmatter() != null) {
				comp.getFxTextField().setText(iFiCol.getFnEditorNodeValueFormmatter().apply(entity).toString());
			} else {
				comp.getFxTextField().setText(cellvalue.toString());
			}

		}

		if (colNodeClass.equals(FxTextFieldWitBtnLbl3.class.getName())) {

			FxTextFieldWitBtnLbl3 comp = (FxTextFieldWitBtnLbl3) colNode;
			// Gerçek değeri txvalue da tutulur.
			comp.setTxValue(cellvalue.toString());
			if (iFiCol.getFnEditorNodeValueFormmatter() != null) {
				comp.getFxTextField().setText(iFiCol.getFnEditorNodeValueFormmatter().apply(entity).toString());
			} else {
				comp.getFxTextField().setText(cellvalue.toString());
			}

		}

		if (colNodeClass.equals(FxDatePicker.class.getName())) {

			FxDatePicker comp = (FxDatePicker) colNode;
			comp.setValue(FiDate.convertLocalDate((Date) cellvalue));

		}

		if (colNodeClass.equals(DatePicker.class.getName())) {

			DatePicker comp = (DatePicker) colNode;
			comp.setValue(FiDate.convertLocalDate((Date) cellvalue));

		}

		if (colNodeClass.equals(FxLabel.class.getName())) {
			FxLabel comp = (FxLabel) colNode;
			comp.setText(cellvalue.toString());
			comp.setTxValue(cellvalue.toString());
		}

		// fxcombobox comboitem ise

		if (colNodeClass.equals(FxComboBox.class.getName())) {
			FxComboBox comp = (FxComboBox) colNode;
			comp.setTxValue(cellvalue.toString());
		}

		if (colNodeClass.equals(FxComboBoxSimple.class.getName())) {
			FxComboBoxSimple comp = (FxComboBoxSimple) colNode;
			comp.setTxValue(cellvalue.toString());
			comp.setSelectedItemByTxValueFi(); // 5-11-21 eklendi
		}

		if (colNodeClass.equals(FxChoiceBox.class.getName())) {
			FxChoiceBox comp = (FxChoiceBox) colNode;
			comp.setTxValue(cellvalue.toString());
		}

		if (colNodeClass.equals(FxCheckBox.class.getName())) {
			FxCheckBox comp = (FxCheckBox) colNode;
			if (cellvalue != null) {
				Boolean boValue = (Boolean) cellvalue;
				comp.setSelected(boValue);
			}
		}

	}

	/**
	 * Önemli !!! <br>
	 * Editorden gelen boş string ler null olarak yorumlandı.
	 *
	 * @param iFiTableCol
	 * @return
	 */
	public static Object getNodeObjValueByFilterNode(IFiCol iFiTableCol) {
		//return getEditorObjValueByFilterNode(IFiTableCol,iFiTableCol.getColFilterNodeClass());
		return getValueFromNodeCompMain(iFiTableCol.getColType(), iFiTableCol.getFilterNodeClass(), iFiTableCol.getColFilterNode(), iFiTableCol.getFilterValue(), null);
	}


	public static Object getNodeObjValueByFilterNode(IFiCol iFiTableCol, String prmEditorClass) {
		return getValueFromNodeCompMain(iFiTableCol.getColType(), prmEditorClass, iFiTableCol.getColFilterNode(), iFiTableCol.getFilterValue(), null);
	}

	public static Object getNodeObjValueByEditorNode(IFiCol iFiCol, String txEditorClass) {
		return getValueFromNodeCompMain(iFiCol.getColType(), txEditorClass, iFiCol.getColEditorNode(), iFiCol.getColEditorValue(), iFiCol.getIfxNodeEditor());
	}

	/**
	 * Önemli !!!
	 * <p>
	 * Editorden gelen boş string ler null olarak yorumlandı.
	 *
	 * @param
	 * @return
	 */
	public static Object getValueFromNodeCompMain(OzColType ozColType, String txNodeClass, Node nodeComp, Object nodeValue, IfxNode ifxNode) {
		// Parametre olarak IFiTableCol iFiTableCol çıkarıldı

		// Loghelper.getInstance(FxEditorFactory.class).debug("Col Node Class:"+ txNodeClass);
		// Loghelper.getInstance(FxEditorFactory.class).debug("Col Node Component Id:"+ FiType.orEmpty(nodeComp));

		Object cellvalue = null;    // map.get(infTableCol.getHeader());

		// String txNodeClass = txNodeClass; //infTableCol.getColFxNodeClass();

		if (txNodeClass == null) txNodeClass = "";

		// Node nodeComp = nodeComp; //infTableCol.getColFxNode();

		// editor tanımlanmamış ise nodeValue parametresi döndürülür
		if (nodeComp == null || txNodeClass.equals("")) {
			return nodeValue; //getColFilterValueByColType(iFiTableCol);
		}

		// IFxNode interface kullanan bir component ise degeri kendi metodundan alınır.
		if (nodeComp != null && nodeComp instanceof IfxNode) {
			// Loghelper.get(FxEditorFactory.class).debug("value node component alındı");
			IfxNode ifxNode1 = (IfxNode) nodeComp;
			return ifxNode1.getCompValueByColType(ozColType);
		}

		// Text Value (String) değer olan componentlerin değeri burada belirlenir.
		if (txNodeClass.equals(FxTextField.class.getName())
				|| txNodeClass.equals(FxTextFieldBtn.class.getName())
				|| txNodeClass.equals(FxLabel.class.getName())
				|| txNodeClass.equals(FxLabelData.class.getName())
				|| txNodeClass.equals(FxTextFieldWitBtnLbl3.class.getName())) {

			String textValue = null;

			if (txNodeClass.equals(FxTextField.class.getName())) {
				FxTextField comp = (FxTextField) nodeComp;
				textValue = comp.getText();
			}

			if (txNodeClass.equals(FxTextFieldBtn.class.getName())) {
				FxTextFieldBtn comp = (FxTextFieldBtn) nodeComp;
				//textValue = comp.getFxTextField().getText();
				textValue = comp.getTxValue();
				//Loghelperr.debug(FxEditorFactory.class,"txVal:"+comp.getTxValue());
			}

			if (txNodeClass.equals(FxTextFieldWitBtnLbl3.class.getName())) {
				FxTextFieldWitBtnLbl3 comp = (FxTextFieldWitBtnLbl3) nodeComp;
				//textValue = comp.getFxTextField().getText();
				textValue = comp.getTxValue();
				//Loghelperr.debug(FxEditorFactory.class,"txVal:"+comp.getTxValue());
			}

			if (txNodeClass.equals(FxTextFieldBtnLabel2.class.getName())) {
				FxTextFieldWithButtonLabelCustom comp = (FxTextFieldWithButtonLabelCustom) nodeComp;
				//textValue = comp.getFxTextField().getText();
				textValue = comp.getTxValue();
				//Loghelperr.debug(FxEditorFactory.class,"txVal:"+comp.getTxValue());
			}

			if (txNodeClass.equals(FxLabel.class.getName()) || txNodeClass.equals(FxLabelData.class.getName())) {
				FxLabel comp = (FxLabel) nodeComp;
				textValue = comp.getTxValue();
			}

			// textvalue null veya "" boş string ise null döndürülür !!!!
			if (textValue == null || textValue.equals("")) {
				return null;
			}

			// ********** String Value ilgili tipe dönüştürülür
			return convertStringValueToObjectByOzColType(ozColType, textValue);

		}

		// ***** Tarih Componentleri

		if (txNodeClass.equals(DatePicker.class.getName()) || txNodeClass.equals(FxDatePicker.class.getName())) {

			LocalDate localDate = null;
			String txDate = null;

			DatePicker comp = null;

			if (txNodeClass.equals(DatePicker.class.getName())) {
				comp = (DatePicker) nodeComp;
				txDate = comp.getEditor().getText();
				//localDate = comp.getValue();
				localDate = FxDatePicker.getStrConverter().fromString(txDate);

			}

			if (txNodeClass.equals(FxDatePicker.class.getName())) {
				comp = (FxDatePicker) nodeComp;
				//localDate = comp.getValue();
				txDate = comp.getEditor().getText();
				localDate = FxDatePicker.getStrConverter().fromString(txDate);
			}

			if (localDate != null) {
				cellvalue = FiDate.convertLocalDateToSimpleDate(localDate);
				return cellvalue;
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

			return cellvalue;
		}

		if (txNodeClass.equals(FxComboBox.class.getName())
				|| txNodeClass.equals(ComboBox.class.getName())) {

			if (txNodeClass.equals(FxComboBox.class.getName())) {
				FxComboBox<ComboItem> comp = (FxComboBox<ComboItem>) nodeComp;
				String textValue = comp.getSelectedItemFi().getValue();
				return convertStringValueToObjectByOzColType(ozColType, textValue);
			}

		}

		if (txNodeClass.equals(FxComboBoxSimple.class.getName())) {
			FxComboBoxSimple comp = (FxComboBoxSimple) nodeComp;
			String textValue = comp.getSelectedItemFi().getValue();
			return convertStringValueToObjectByOzColType(ozColType, textValue);
		}

		if (txNodeClass.equals(FxChoiceBoxSimple.class.getName())) {
			FxChoiceBoxSimple comp = (FxChoiceBoxSimple) nodeComp;
			if (comp.getFiSelectedItem() != null) {
				String textValue = comp.getFiSelectedItem().getValue();
				return convertStringValueToObjectByOzColType(ozColType, textValue);
			}
			return null;
		}

		if (txNodeClass.equals(FxCheckBox.class.getName())) {
			FxCheckBox comp = (FxCheckBox) nodeComp;
			return comp.isSelected();
		}


		// *****

		//if (cellvalue != null) return cellvalue;

		// infTableCol.getColFxNodeClass() == null && //if (infTableCol.getFiValue() != null) {

		return nodeValue; //getColFilterValueByColType(iFiTableCol);

	}

	/**
	 * textValue , ozColType göre tipine çevirerek dönüş yapar. <br>
	 * mesala ozColType integer ise integer tipine çevirerek object olarak dönüş yapar.
	 *
	 * @param ozColType
	 * @param textValue
	 * @return
	 */
	public static Object convertStringValueToObjectByOzColType(OzColType ozColType, String textValue) {

		// gelen veride özel işaretler temizlenir
		if (ozColType == OzColType.Double || ozColType == OzColType.Integer) {
			textValue = textValue.replaceAll("(<|>|!)", "");
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

		if (ozColType == OzColType.CommaSeperatedStr) {
			List<String> listData = FiCollection.commaSeperatedParseToStrList(textValue);
			return listData;
		}

		if (ozColType == OzColType.BoolEvetHayir) {
			if(FiString.isEmpty(textValue)) return null;

			if(textValue.equalsIgnoreCase("E") || textValue.equalsIgnoreCase("EVET")) {
				return true;
			}
			if(textValue.equalsIgnoreCase("H") || textValue.equalsIgnoreCase("HAYIR")) {
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
				cellvalue = FiDate.toString_globalFormat(FiDate.convertLocalDateToSimpleDate(comp.getValue()));
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
				cellvalue = FiDate.toString_globalFormat(FiDate.convertLocalDateToSimpleDate(comp.getValue()));
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

			if (FiBoolean.isTrue(ozTableCol.getBoHidden())) {
				continue;
			}

			if (FiBoolean.isTrue(ozTableCol.getBoEditable())) continue;

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
	public <T> void registerChangeListenerToEditor(IFiCol ozTableCol, ChangeListener changeListener) {


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
	 * @param ozTableCol
	 * @param consumerStrProp
	 * @param durationMilis
	 */
	public static <T> void registerTextPropertyWithDurationForFilterNode(IFiCol ozTableCol, Consumer<String> consumerStrProp, long durationMilis) {

		registerTextPropertyWithDurationForNode(consumerStrProp, durationMilis, ozTableCol.getFilterNodeClass(), ozTableCol.getColFilterNode());

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

			if (FiBoolean.isTrue(iFiTableCol.getBoRequired()) || FiBoolean.isFalse(iFiTableCol.getBoNullable())) {

				Object propertyNested = FiReflection.getPropertyNested(entity, iFiTableCol.getFieldName());

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

	public static Fdr checkColumnsForNullAndOtherErrorsByEditorValue(List<FiCol> colsForm) {

		for (FiCol fiTableCol : colsForm) {

			// boRequired True, BoNullable False yapılmışsa boş geçilemez.
			if (FiBoolean.isTrue(fiTableCol.getBoRequired()) || FiBoolean.isFalse(fiTableCol.getBoNullable())) {

				Object cellValue = fiTableCol.getColEditorValue();

				// Null ve Boş Alan Kontrolü
				if (cellValue == null ||
						(cellValue instanceof String && FiString.isEmpty((String) cellValue))) {
//					return new FdrResult(false, "Lütfen Gerekli Alanları Doldurunuz");
					return new Fdr(false, String.format("%s Alanı Zorunludur.Boş Geçilemez.", fiTableCol.getHeaderName()));
				}

			}
		}

		return new Fdr(true);
	}

	public static Fdr checkColErrorsByEditor(List<FiCol> colsForm) {

		for (FiCol fiTableCol : colsForm) {

			// boRequired True, BoNullable False yapılmışsa boş geçilemez.
			if (FiBoolean.isTrue(fiTableCol.getBoRequired()) || FiBoolean.isFalse(fiTableCol.getBoNullable())) {

//				Object cellValue = fiTableCol.getColEditorValue();
				Object cellValue = FxEditorFactory.getNodeObjValueByEditorNode(fiTableCol, fiTableCol.getColEditorClass());

				// Null ve Boş Alan Kontrolü
				if (cellValue == null ||
						(cellValue instanceof String && FiString.isEmpty((String) cellValue))) {
//					return new FdrResult(false, "Lütfen Gerekli Alanları Doldurunuz");
					return new Fdr(false, String.format("%s Alanı Zorunludur. Geçerli bir değer giriniz.", fiTableCol.getHeaderName()));
				}

			}
		}

		return new Fdr(true);
	}

	private static void clearValueForNodeCompMain(IFiCol ozTableCol, String colNodeClass, Node colNode) {

		//String colNodeClass = ozTableCol.getColFilterNodeClass();

		if (colNode != null && colNode instanceof IfxNode) {
			IfxNode ifxNode = (IfxNode) colNode;
//			ifxNode.setCompValue(cellvalue);
			ifxNode.setCompValue("");
			return;
		}

		if (colNodeClass == null) {
			return;
		}

		//Node colNode = ozTableCol.getColFilterNode();

		if (colNodeClass.equals(FxTextField.class.getName())) {
			//FiConsole.debug(ozTableCol);
			FxTextField comp = (FxTextField) colNode;
			comp.setText("");
			comp.setTxValue("");
		}

		if (colNodeClass.equals(FxTextFieldBtn.class.getName())) {

			FxTextFieldBtn comp = (FxTextFieldBtn) colNode;
			comp.setTxValue("");
			comp.getFxTextField().setText("");
//			if (ozTableCol.getFnEditorNodeValueFormmatter() != null) {
//				comp.getFxTextField().setText(ozTableCol.getFnEditorNodeValueFormmatter().apply(entity).toString());
//			} else {
//				comp.getFxTextField().setText(cellvalue.toString());
//			}

		}

		if (colNodeClass.equals(FxTextFieldWitBtnLbl3.class.getName())) {

			FxTextFieldWitBtnLbl3 comp = (FxTextFieldWitBtnLbl3) colNode;
			// Gerçek değeri txvalue da tutulur.
			comp.setTxValue("");
			comp.getFxTextField().setText("");
//			if (ozTableCol.getFnEditorNodeValueFormmatter() != null) {
//				comp.getFxTextField().setText(ozTableCol.getFnEditorNodeValueFormmatter().apply(entity).toString());
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
