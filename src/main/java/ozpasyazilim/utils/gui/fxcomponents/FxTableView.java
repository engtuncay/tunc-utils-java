package ozpasyazilim.utils.gui.fxcomponents;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.text.TextAlignment;

import javafx.util.Callback;
import org.apache.commons.beanutils.PropertyUtils;
import org.tbee.javafx.scene.layout.MigPane;
import ozpasyazilim.utils.gui.fxTableViewExtra.EnumColNodeType;
import ozpasyazilim.utils.gui.fxTableViewExtra.NestedPropertyValueFactory;
import ozpasyazilim.utils.annotations.FiDraft;
import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.core.FiReflection;
import ozpasyazilim.utils.mvc.IFxTableSelectionCont;
import ozpasyazilim.utils.returntypes.FnResult;
import ozpasyazilim.utils.table.OzColSummaryType;
import ozpasyazilim.utils.table.OzColType;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

// Notlar
// CellFactory : hücre üretim fabrikası TableColumn input alır, output olarak TableCell verir.
// (Callback fonksiyonunu icra eder) Callback<TableColumn<S, T>, TableCell<S, T>>

/**
 *
 * FxTableView2 kullanın.
 *
 * @param <EntClazz>>
 */
@Deprecated
public class FxTableView<EntClazz> extends TableView<EntClazz> implements IFxComp {

	private Class<EntClazz> entityClazz;

	private String fxId;
	private Map<String, Object> styleMap;
	private List<FxTableCol> fxTableColList;
	private FilteredList filteredList;
	// gereksiz çıkartılabilir
	private Boolean filteredListActive;
	// Filter Editor Activate
	private Boolean enabledLocalFilterEditor;
	private Boolean enabledRemoteFilterEditor;
	// Sayfalama için yapıldı.
	private Integer pageViewRowCount;
	private Integer pageTotalRowCount;
	private Boolean enableSummaryRowHeader;

	// Özel Filtreler buraya eklenir
	private List<Predicate> listPredFilterExtra;
	// Lokal içeriden yapılan filtrelemedir(ör.tablonun filter editorden gelen)
	private Predicate predFilterLocal;
	// Remote ise belli server üzerinde kayıt çekilirken yapılır.
	private Predicate predFilterRemote;

	// activateSelectAndClose aktive edilince, bu runnable çalıştır
	// , closeReason done yapıp, kapatma eylemi gerçekleştirmeli
	// ifxmodcont ile çözülünce
	// Runnable runnableSelectAndClose;

	// Filter Node enter basılınca yapılacak işlem
	private EventHandler<KeyEvent> colFilterNodeEnterFnGlobal;

	private String headerFilterNodeStyleClass = "tblHeaderFilter";
	private String headerSummaryClass = "tblHeaderSummary";

	// FxTable comp i , fxtable mig içerisinde ise buraya set edilir.
	FxTableMig fxTableMig;
	private EventHandler<KeyEvent> colFilterKeyDownEvent;

	public static void setFxColsFilterableNullToTrue(List<FxTableCol> colTblMain) {
		colTblMain.forEach(fxTableCol -> {
			if (fxTableCol.getBoFilterable() == null) fxTableCol.setBoFilterable(true);
		});
	}

	public FilteredList<EntClazz> getItemsCurrentByBoField(String fieldForSelection) {

		return getItemsCurrent(ent -> {

			try {
				return FiBool.convertBooleanElseFalse(PropertyUtils.getNestedProperty(ent, fieldForSelection));
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		});

	}

	public void addAllOzTableColsAuto(List<IFiCol> listExcelColumns) {
		addAllInfTableColumnList(listExcelColumns);
	}

	// TableRow factory içerisine eklenecek eventlar bu map in içerisine tanımlanır
	private Map<FxTableRowActions, Consumer<TableRow>> mapTableRowEvents;

	// Satır Actionları
	//EventHandler<MouseEvent>
	//BiConsumer<MouseEvent, TableRow> fiRowDoubleClickEvent;
	//TriConsumer<Object, Boolean, TableRow> fiRowFactoryUpdateFn;

	private Boolean confiAutoScrollToLast;

	private ObjectProperty<KeyEvent> propTblKeyEvent = new SimpleObjectProperty<>();

	public FxTableView() {
		super();
		setupFxTable();
	}

	private void setupFxTable() {

		super.setOnKeyPressed(event -> {
			propTblKeyEvent.setValue(event);
		});

		setupCopySelectionToClipboard();

	}


	public FxTableView(ObservableList items) {
		super(items);
	}

	public static Node defAutoEditorClass(List<IFiCol> listColumn) {

		Node nodeGenerated = null;

		for (int i = 0; i < listColumn.size(); i++) {

			IFiCol ozTableColumn = listColumn.get(i);
			//ozTableColumn.setColFilterable(true);
			boolean blDefined = false;

			if (!blDefined && ozTableColumn.getColType() == OzColType.String || ozTableColumn.getColType() == OzColType.Double
					|| ozTableColumn.getColType() == OzColType.Integer) {
				ozTableColumn.setFilterNodeClass(FxTextField.class.getName());
				nodeGenerated = new FxEditorFactory().generateAndSetFilterNode(ozTableColumn);
				blDefined = true;
			}

			if (!blDefined && ozTableColumn.getColType() == OzColType.Date) {
				ozTableColumn.setFilterNodeClass(FxDatePicker.class.getName());
				nodeGenerated = new FxEditorFactory().generateAndSetFilterNode(ozTableColumn);
				blDefined = true;
			}

			if (!blDefined) {
				ozTableColumn.setFilterNodeClass(FxTextField.class.getName());
				nodeGenerated = new FxEditorFactory().generateAndSetFilterNode(ozTableColumn);
				blDefined = true;
			}

		}

		if (listColumn.size() == 1) return nodeGenerated;
		return null;

	}

	public void addAllFxTableColsPlain(List<FxTableCol> fxTableColList) {

		if (fxTableColList == null) return;

		for (int i = 0; i < fxTableColList.size(); i++) {
			FxTableCol fxTableCol = fxTableColList.get(i);
			addFxTableCol(fxTableCol);
		}

	}

	public FxTableView addAllFxTableColsAuto(List<FxTableCol> fxTableColList) {

		for (int i = 0; i < fxTableColList.size(); i++) {

			FxTableCol fxTableCol = fxTableColList.get(i);

			//fxTableCol.setAutoColumnDefault();
			autoFiColumnCellValueAndEditorFactoryConfig(fxTableCol);

			//Loghelperr.getInstance(getClass()).debug(" Fx TableView col id:"+fxTableCol.getId());
			addFxTableCol(fxTableCol);
		}
		return this;
	}

	public void setAutoClass() {
		if (this.entityClazz == null) {
			this.entityClazz = (Class<EntClazz>) ((ParameterizedType) this.getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
		}
	}

	private void autoFiColumnCellValueAndEditorFactoryConfig(FxTableCol fxTableCol) {
		//Loghelperr.getInstance(getClass()).debug("added "+ fxTableCol.getFieldName());
		fxTableCol.setText(fxTableCol.getOfcTxHeader());

		if (fxTableCol.getOfcTxFieldName().contains(".")) {
			Loghelper.get(getClass()).debug("Nested:" + fxTableCol.getOfcTxFieldName());
			fxTableCol.setCellValueFactory(new NestedPropertyValueFactory(fxTableCol.getOfcTxFieldName()));
		} else {
			fxTableCol.setCellValueFactory(new PropertyValueFactory<>(fxTableCol.getOfcTxFieldName()));
		}

		autoFiCellEditorFactoryDefaultConfig(fxTableCol);
		fxTableCol.setId(fxTableCol.getOfcTxFieldName());
		fxTableCol.setAutoFormatter(fxTableCol.getColType());
	}

	/**
	 * Eğer cell editor factory class , default editor factory ayarlar burada yapılır
	 *
	 * @param fxTableCol
	 */
	private void autoFiCellEditorFactoryDefaultConfig(FxTableCol fxTableCol) {

		// cell Factory
		if (!FiString.isEmpty(fxTableCol.getColEditorClass())) return;

		if (fxTableCol.getColType() == OzColType.Boolean) {

			fxTableCol.fiStyleAlignCenter();

//			fxTableCol.setCellValueFactory(param -> {
//				TableColumn.CellDataFeatures paramm = (TableColumn.CellDataFeatures) param;
//				paramm.
//			} );

			// callback <P,R> parameter(argumant) type and return type
			Callback<TableColumn, TableCell> cellFactoryCheckBox = new Callback<TableColumn, TableCell>() {

				@Override
				public TableCell call(final TableColumn tableColumn) {  //neden final ??

					final TableCell cell = new TableCell() {

						// table cell objesi
						final FxCheckBox node = new FxCheckBox();

						// item cellValue oluyor , entity getTableView().getItems().get(getIndex()) ile elde edilir
						@Override
						public void updateItem(Object item, boolean empty) {
							super.updateItem(item, empty);
							if (empty) {
								setGraphic(null);
								setText(null);
							} else { // empty false ise, boş degilse

								//S entity = getTableView().getItems().get(getIndex());
								//FiConsole.printObjectDefiniton(item, fxTableCol.getFieldName());

								if (item != null) {
									node.setSelected((Boolean) item);
								} else {
									node.setSelected(false);
								}

								if (FiBool.isTrue(fxTableCol.getBoEditable())) {

									if (fxTableCol.getPredFiEditorDisable() != null) {
										node.setDisable(fxTableCol.getPredFiEditorDisable().test(getTableView().getItems().get(getIndex())));
									} else {
										node.setDisable(false);
									}

									node.setOnAction(actionEvent -> {
										Boolean result = FiReflection.setter(getTableView().getItems().get(getIndex()), fxTableCol.getOfcTxFieldName(), node.isSelected());
										if (!result) {
											FxDialogShow.showPopWarn("Yazılımsal Hata!!! Seçilemedi.");
										} else {
											if (fxTableCol.getFnColCellChanged() != null) {
												fxTableCol.getFnColCellChanged().accept(getTableView().getItems().get(getIndex()));
											}
											//updateFiltersLocalAndOut();
										}
									});

								} else {
									node.setDisable(true);
								}

								node.setAlignment(Pos.CENTER);
								setGraphic(node);
								setText(null);
							}
						}
					};
					return cell;

				}
			};

			fxTableCol.setCellFactory(cellFactoryCheckBox);

		}

	}

	//private void setFiCellEditorFactory(FxTableCol fxTableCol) {
	public void activateCellFactoryClass(FxTableCol fxTableCol) {

		if (fxTableCol.getColEditorClass() == null) return;

		if (fxTableCol.getColEditorClass().equals(CheckBox.class.getSimpleName())) {

//			fxTableCol.setCellFactory(new Callback<TableColumn<S, Boolean>, TableCell<S, Boolean>>() {
//
//				@Override
//				public TableCell<S, Boolean> call(
//						TableColumn<S, Boolean> param) {
//					//return new CheckBoxCell(selectedItems);
//					CheckBoxTableCell<S, Boolean> cell = new CheckBoxTableCell<S, Boolean>();
//					cell.setAlignment(Pos.CENTER);
//					return cell;
//				}
//			});

			fxTableCol.setCellFactory(new Callback<TableColumn, TableCell>() {
				@Override
				public TableCell call(TableColumn param) {
					CheckBoxTableCell cell = new CheckBoxTableCell();
					cell.setAlignment(Pos.CENTER);
					return cell;
				}
			});

		}


		if (fxTableCol.getColEditorClass().equals(Button.class.getSimpleName())
				|| fxTableCol.getColEditorClass().equals(Button.class.getName())) {

			// callback < P,R> parameter and return types
			Callback<TableColumn<EntClazz, String>, TableCell<EntClazz, String>> cellFactory
					= //
					new Callback<TableColumn<EntClazz, String>, TableCell<EntClazz, String>>() {
						@Override
						public TableCell call(final TableColumn<EntClazz, String> param) {

							final TableCell<EntClazz, String> cell = new TableCell<EntClazz, String>() {

								// table cell objesi

								final Button btn = new Button(fxTableCol.getColEditorNodeText());

								@Override
								public void updateItem(String item, boolean empty) {
									super.updateItem(item, empty);
									if (empty) {
										setGraphic(null);
										setText(null);
									} else { // empty false ise
										btn.setOnAction(event -> {
											EntClazz entity = getTableView().getItems().get(getIndex()); // table cell objesinin gettableview methodu
											if (fxTableCol.getFnEditorSetOnActionWithEntity() != null) {
												fxTableCol.getFnEditorSetOnActionWithEntity().accept(entity);
											}
											//System.out.println(person.getFirstName()+ "   " + person.getLastName());
										});
										setGraphic(btn);
										setText(null);
									}
								}
							};
							return cell;

						}
					};

			fxTableCol.setCellFactory(cellFactory);
		}

		// 26-09-2019 Genel İşlevli Factory Class , istenilen comp üretilip ona göre render edilir
		if (fxTableCol.getColEditorClass().equals(Node.class.getName())) {

			// callback < P,R> parameter and return types
			Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>> cellFactory
					= //
					new Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>>() {

						@Override
						public TableCell call(final TableColumn<EntClazz, Object> param) {

							// table cell objesi
							final TableCell<EntClazz, Object> cell = new TableCell<EntClazz, Object>() {

								Node nodeComp;

								// Item hücrenin içine gelen değer (string,boolean,double vs)
								@Override
								public void updateItem(Object item, boolean empty) {
									super.updateItem(item, empty);
									if (empty) {
										setGraphic(null);
										setText(null);
									} else { // empty false ise

										EntClazz entity = getTableView().getItems().get(getIndex());
										if (fxTableCol.getFnEditorNodeRendererBeforeSettingValue() != null) {
											fxTableCol.getFnEditorNodeRendererBeforeSettingValue().accept(entity, nodeComp);
										}

										setGraphic(nodeComp);
										setText(null);
									}
								}
							};
							return cell;

						}
					};

			fxTableCol.setCellFactory(cellFactory);
		}

		// 26-09-2019 Genel İşlevli FxButton
		if (fxTableCol.getColEditorClass().equals(EnumColNodeType.FxButtonV2.toString())) {

			// callback < P,R> parameter and return types
			Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>> cellFactory
					= //
					new Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>>() {

						@Override
						public TableCell call(final TableColumn<EntClazz, Object> param) {

							// table cell objesi
							final TableCell<EntClazz, Object> cell = new TableCell<EntClazz, Object>() {

								final FxButton nodeComp = new FxButton(FiString.orEmpty(fxTableCol.getColEditorNodeText()));

								// Item hücrenin içine gelen değer (string,boolean,double vs)
								@Override
								public void updateItem(Object item, boolean empty) {
									super.updateItem(item, empty);
									if (empty) {
										setGraphic(null);
										setText(null);
									} else { // empty false ise

										if (fxTableCol.getFnEditorNodeRendererBeforeSettingValue() != null) {
											EntClazz entity = getTableView().getItems().get(getIndex());
											fxTableCol.getFnEditorNodeRendererBeforeSettingValue().accept(entity, nodeComp);
										}

										if (fxTableCol.getFnEditorNodeRendererWithCol() != null) {
											EntClazz entity = getTableView().getItems().get(getIndex());
											fxTableCol.getFnEditorNodeRendererWithCol().accept(entity, nodeComp,fxTableCol);
										}

										if (fxTableCol.getFnEditorNodeRendererWitValue() != null) {
											EntClazz entity = getTableView().getItems().get(getIndex());
											Object propertyNested = FiReflection.getPropertyNested(entity, fxTableCol.getOfcTxFieldName());
											fxTableCol.getFnEditorNodeRendererWitValue().accept(entity, nodeComp,propertyNested);
										}


										if (fxTableCol.getFnEditorSetOnAction() != null) {

											nodeComp.setOnAction(event -> {
												EntClazz entity = getTableView().getItems().get(getIndex());

												// Önce action yürütülür , daha sonra renderer işlemi yapılır
												fxTableCol.getFnEditorSetOnAction().accept(entity, nodeComp);


												if (fxTableCol.getFnEditorNodeRendererBeforeSettingValue() != null) {
													fxTableCol.getFnEditorNodeRendererBeforeSettingValue().accept(entity, nodeComp);
												}


											});
										}

										if (fxTableCol.getFnEditorSetOnActionWitCol() != null) {

											nodeComp.setOnAction(event -> {
												EntClazz entity = getTableView().getItems().get(getIndex());

												// Önce action yürütülür , daha sonra renderer işlemi yapılır
												fxTableCol.getFnEditorSetOnActionWitCol().accept(entity, nodeComp,fxTableCol);


												if (fxTableCol.getFnEditorNodeRendererWithCol() != null) {
													fxTableCol.getFnEditorNodeRendererWithCol().accept(entity, nodeComp,fxTableCol);
												}

											});
										}

										if (fxTableCol.getFnEditorSetOnActionWitValue() != null) {

											nodeComp.setOnAction(event -> {
												EntClazz entity = getTableView().getItems().get(getIndex());

												// Önce action yürütülür , daha sonra renderer işlemi yapılır
												if(true){
													Object propertyNested = FiReflection.getPropertyNested(entity, fxTableCol.getOfcTxFieldName());
													fxTableCol.getFnEditorSetOnActionWitValue().accept(entity, nodeComp,propertyNested);
												}

												if (fxTableCol.getFnEditorNodeRendererWitValue() != null) {
													Object propertyNested = FiReflection.getPropertyNested(entity, fxTableCol.getOfcTxFieldName());
													fxTableCol.getFnEditorNodeRendererWitValue().accept(entity, nodeComp,propertyNested);
												}

											});
										}

										setGraphic(nodeComp);
										setText(null);
									}
								}
							};
							return cell;

						}
					};

			fxTableCol.setCellFactory(cellFactory);
		}

		if (fxTableCol.getColEditorClass().equals(FxButton.class.getName())
				|| fxTableCol.getColEditorClass().equals(FxButton.class.getSimpleName())) {

			// callback < P,R> parameter and return types
			Callback<TableColumn<EntClazz, String>, TableCell<EntClazz, String>> cellFactory
					= //
					new Callback<TableColumn<EntClazz, String>, TableCell<EntClazz, String>>() {
						@Override
						public TableCell call(final TableColumn<EntClazz, String> param) {

							final TableCell<EntClazz, String> cell = new TableCell<EntClazz, String>() {

								// table cell objesi

								final FxButton btn = new FxButton(fxTableCol.getColEditorNodeText());

								@Override
								public void updateItem(String item, boolean empty) {
									super.updateItem(item, empty);
									if (empty) {
										setGraphic(null);
										setText(null);
									} else { // empty false ise
										btn.setOnAction(event -> {
											EntClazz entity = getTableView().getItems().get(getIndex()); // table cell objesinin gettableview methodu
											if (fxTableCol.getFnEditorSetOnActionWithEntity() != null) {
												fxTableCol.getFnEditorSetOnActionWithEntity().accept(entity);
											}
											//System.out.println(person.getFirstName()+ "   " + person.getLastName());
										});
										setGraphic(btn);
										setText(null);
									}
								}
							};
							return cell;

						}
					};

			fxTableCol.setCellFactory(cellFactory);
		}

		if (fxTableCol.getColEditorClass().equals(ToggleButton.class.getSimpleName())) {

			// callback < P,R> parameter and return types
			Callback<TableColumn<EntClazz, Boolean>, TableCell<EntClazz, Boolean>> cellFactory
					= //
					new Callback<TableColumn<EntClazz, Boolean>, TableCell<EntClazz, Boolean>>() {
						@Override
						public TableCell call(final TableColumn<EntClazz, Boolean> param) {

							final TableCell<EntClazz, Boolean> cell = new TableCell<EntClazz, Boolean>() {

								// table cell objesi

								final ToggleButton btn = new ToggleButton(fxTableCol.getColEditorNodeText());

								@Override
								public void updateItem(Boolean item, boolean empty) {
									super.updateItem(item, empty);
									if (empty) {
										setGraphic(null);
										setText(null);
									} else { // empty false ise

										if (btn.isSelected()) {
											btn.setText("OK");
										} else {
											btn.setText("X");
										}

										btn.setOnAction(event -> {

											EntClazz entity = getTableView().getItems().get(getIndex()); // table cell objesinin gettableview methodu

											//if(fxTableCol.getCellFactoryNodeBiAction()==null)return;

											fxTableCol.getFnEditorSetOnAction().accept(entity, btn);

											if (btn.isSelected()) {
												//new FiProperty().setter(entity,fxTableCol.getFieldName(),true);
//												fxTableCol.getCellFactoryNodeBiAction().accept(entity,btn);
												//btn.setText("OK");
											} else {
												//new FiProperty().setter(entity,fxTableCol.getFieldName(),false);
												//btn.setText("X");
											}

//											if (fxTableCol.getCellFactoryAction() != null) {
//												fxTableCol.getCellFactoryAction().accept(entity);
//											}
											//System.out.println(person.getFirstName()+ "   " + person.getLastName());
										});
										setGraphic(btn);
										setText(null);
									}
								}
							};
							return cell;

						}
					};

			fxTableCol.setCellFactory(cellFactory);
		}

		if (fxTableCol.getColEditorClass().equals(FxStateButton.class.getSimpleName())) {

			// callback < P,R> parameter and return types
			Callback<TableColumn<EntClazz, Integer>, TableCell<EntClazz, Integer>> cellFactory
					= //
					new Callback<TableColumn<EntClazz, Integer>, TableCell<EntClazz, Integer>>() {
						@Override
						public TableCell call(final TableColumn<EntClazz, Integer> param) {

							final TableCell<EntClazz, Integer> cell = new TableCell<EntClazz, Integer>() {

								// table cell objesi

								final FxStateButton btn = new FxStateButton(fxTableCol.getColEditorNodeText());

								@Override
								public void updateItem(Integer item, boolean empty) {
									super.updateItem(item, empty);
									if (empty) {
										setGraphic(null);
										setText(null);
									} else { // empty false ise -- boş degilse

										//if(item==null) item= 0;
										//btn.setText(item.toString());

										EntClazz entity = getTableView().getItems().get(getIndex());

										if (fxTableCol.getFnEditorNodeRendererBeforeSettingValue() != null) {
											fxTableCol.getFnEditorNodeRendererBeforeSettingValue().accept(entity, btn);
										}

										btn.setOnAction(event -> {

											//S entity = getTableView().getItems().get(getIndex()); // table cell objesinin gettableview methodu

											//if(fxTableCol.getCellFactoryNodeBiAction()==null)return;

											// Action gerçekleştirilir, daha sonra renderer işlemleri yapılır
											fxTableCol.getFnEditorSetOnAction().accept(entity, btn);

											if (fxTableCol.getFnEditorNodeRendererBeforeSettingValue() != null)
												fxTableCol.getFnEditorNodeRendererBeforeSettingValue().accept(entity, btn);

											//new FiProperty().setter(entity,fxTableCol.getFieldName(),true);
											//fxTableCol.getCellFactoryNodeBiAction().accept(entity,btn);
										});
										setGraphic(btn);
										setText(null);
									}
								}
							};
							return cell;

						}
					};

			fxTableCol.setCellFactory(cellFactory);
		}

		if (fxTableCol.getColEditorClass().equals(FxStateButtonThree.class.getSimpleName())) {

			// callback < P,R> parameter and return types
			Callback<TableColumn<EntClazz, Integer>, TableCell<EntClazz, Integer>> cellFactory
					= //
					new Callback<TableColumn<EntClazz, Integer>, TableCell<EntClazz, Integer>>() {
						@Override
						public TableCell call(final TableColumn<EntClazz, Integer> param) {

							final TableCell<EntClazz, Integer> cell = new TableCell<EntClazz, Integer>() {

								// table cell objesi

								final FxStateButtonThree btn = new FxStateButtonThree(fxTableCol.getColEditorNodeText());

								@Override
								public void updateItem(Integer item, boolean empty) {
									super.updateItem(item, empty);
									if (empty) {
										setGraphic(null);
										setText(null);
									} else { // empty false ise

										//if(item==null) item= 0;
										//btn.setText(item.toString());

										EntClazz entity = getTableView().getItems().get(getIndex());

										if (fxTableCol.getFnEditorNodeRendererBeforeSettingValue() != null) {
											fxTableCol.getFnEditorNodeRendererBeforeSettingValue().accept(entity, btn);
										}

										btn.setOnAction(event -> {

											//S entity = getTableView().getItems().get(getIndex()); // table cell objesinin gettableview methodu

											//if(fxTableCol.getCellFactoryNodeBiAction()==null)return;

											fxTableCol.getFnEditorSetOnAction().accept(entity, btn);

											//new FiProperty().setter(entity,fxTableCol.getFieldName(),true);
											//fxTableCol.getCellFactoryNodeBiAction().accept(entity,btn);
										});
										setGraphic(btn);
										setText(null);
									}
								}
							};
							return cell;

						}
					};

			fxTableCol.setCellFactory(cellFactory);
			return;
		}

		if (fxTableCol.getColEditorClass().equals(EnumColNodeType.FxButtonCustom.toString())) {

			fxTableCol.fiStyleAlignCenter();

			// callback S : entity , Field Tür : Object : Herhangi bir tipde hücre alanı olabilir
			Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>> cellFactory =
					new Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>>() {
						@Override
						public TableCell call(final TableColumn<EntClazz, Object> param) {

							final TableCell<EntClazz, Object> cell = new TableCell<EntClazz, Object>() {

								// table cell objesi

								final FxStateButton btn = new FxStateButton(fxTableCol.getColEditorNodeText());

								@Override
								public void updateItem(Object item, boolean empty) {
									super.updateItem(item, empty);
									if (empty) {
										setGraphic(null);
										setText(null);
									} else { // empty false ise -- boş degilse

										//if(item==null) item= 0;
										//btn.setText(item.toString());

										EntClazz entity = getTableView().getItems().get(getIndex());

										if (fxTableCol.getFnEditorNodeRendererBeforeSettingValue() != null) {
											fxTableCol.getFnEditorNodeRendererBeforeSettingValue().accept(entity, btn);
										}

										btn.setOnAction(event -> {

											//S entity = getTableView().getItems().get(getIndex()); // table cell objesinin gettableview methodu

											// Action gerçekleştirilir, daha sonra renderer işlemleri yapılır
											if (fxTableCol.getFnEditorSetOnAction() != null)
												fxTableCol.getFnEditorSetOnAction().accept(entity, btn);

											if (fxTableCol.getFnEditorNodeRendererBeforeSettingValue() != null)
												fxTableCol.getFnEditorNodeRendererBeforeSettingValue().accept(entity, btn);

											//new FiProperty().setter(entity,fxTableCol.getFieldName(),true);

										});
										setGraphic(btn);
										setText(null);
									}
								}
							};
							return cell;

						}
					};

			fxTableCol.setCellFactory(cellFactory);
		}

	}

	public void addAllInfTableColumnList(List<? extends IFiCol> tableColumns) {

		List<FxTableCol> listFxTableCol = new ArrayList<>();

		for (int colIndex = 0; colIndex < tableColumns.size(); colIndex++) {

			IFiCol ifiTableCol = tableColumns.get(colIndex);
			// Column geçerli değilse eklenmez
			if (FiBool.isFalse(ifiTableCol.getBoEnabled())) continue;
			listFxTableCol.add(new FxTableCol(ifiTableCol));
		}

		addAllFxTableColsAuto(listFxTableCol);

	}

//	private FxTableCol convertOzTableColToFxTableCol(OzTableCol ozTableCol) {
//
//		FxTableCol fxTableCol = new FxTableCol(ozTableCol);
//		return fxTableCol;
//	}

	public void addAllInfTableColumnList(List<IFiCol> tableColumns, Boolean plain) {

		this.getColumns().clear();

		for (int colIndex = 0; colIndex < tableColumns.size(); colIndex++) {
			IFiCol ozTableCol = tableColumns.get(colIndex);

			// Column geçerli değilse eklenmez
			if (ozTableCol.getBoEnabled() != null && !ozTableCol.getBoEnabled()) continue;

			FxTableCol fxTableCol = new FxTableCol(ozTableCol);

			if (ozTableCol.getColType() == null) ozTableCol.setColType(OzColType.String);

			fxTableCol.setAutoColumnNoFormatter();
			addFxTableCol(fxTableCol);

			//Loghelperr.getInstance(getClass()).debug(" Fx TableView col id:"+fxTableCol.getId());

		}

	}

	public void addFxTableCol(FxTableCol fxTableCol) {
		this.getColumns().add(fxTableCol);
		getFxTableColList().add(fxTableCol);
		setupHeaderPaneAndTitle(fxTableCol);
		if (getEnabledLocalFilterEditor() || getEnabledRemoteFilterEditor()) setupHeaderFilterNode(fxTableCol);
		if (getEnableSummaryRowHeader() == true) setupHeaderSummaryNode(fxTableCol);
		activateFilter(fxTableCol);
		activateCellFactoryClass(fxTableCol);

		fxTableCol.setGraphic(fxTableCol.getPaneHeader());
	}

//	public void refreshHeader() {
//
//		getFxTableColList().forEach(fxTableCol -> {
//			setupHeaderEditorForFxTableCol(fxTableCol);
//		});
//
//	}

	public Object getFiSelectedObject() {
		return getSelectionModel().getSelectedItem();
	}

	public FxTableView setActivateFxColsFilterableNullToTrue() {
		getFxTableColList().forEach(fxTableCol -> {
			if (fxTableCol.getBoFilterable() == null) fxTableCol.setBoFilterable(true);
		});
		setEnabledLocalFilterEditor(true);
		activateFilters();
		return this;
	}


	public void setFxColsFilterable(Boolean boFilterable) {
		getFxTableColList().forEach(fxTableCol -> {
			fxTableCol.setBoFilterable(boFilterable);
		});
	}

	public FilteredList<EntClazz> getItemsCurrent() {
		return getFilteredList();
	}

	public FilteredList<EntClazz> getItemsCurrent(Predicate<EntClazz> predFilter) {
		return getFilteredList().filtered(predFilter);
	}

	public ObservableList<EntClazz> getItemsCurrent2() {
		return getItems();
	}

	/**
	 * Filtrelenmemiş orijinal listeyi verir
	 *
	 * @return
	 */
	public ObservableList<EntClazz> getItemsAll() {

		ObservableList<EntClazz> currentList = getFilteredList().getSource();
		return currentList;

	}

	/**
	 * Filtrelenmemiş orijinal liste üzerinden filitreleme yapar
	 *
	 * @param predFilter
	 * @return
	 */
	public FilteredList<EntClazz> getItemsAll(Predicate<EntClazz> predFilter) {

		FilteredList<EntClazz> filtered = getFilteredList().getSource().filtered(predFilter);

		return filtered;
	}

	public void fiRefreshTable() {
		Platform.runLater(() -> {
			refresh();
			updateSummary();
			updateStatusBar();
		});
	}


	public void onFiRowDoubleClickEvent(Consumer<TableRow> doubleClickEvent) {

		if (doubleClickEvent == null) return;
		//getMapTableRowEvents().remove(TableRowActions.DoubleClick);

		getMapTableRowEvents().put(FxTableRowActions.DoubleClick, doubleClickEvent);

	}

	public void removeRowDoubleClickEvent() {
		if (getMapTableRowEvents().containsKey(FxTableRowActions.DoubleClick))
			getMapTableRowEvents().remove(FxTableRowActions.DoubleClick);
	}

	public void setupRowFactory() {

		if (this.mapTableRowEvents == null) {
			this.mapTableRowEvents = new HashMap<>();
		}

		setRowFactory(tv -> {

			TableRow tableRow = new TableRow<>();  // TableRow<Entity>
			tableRow.setOnMouseClicked(event -> {

				if (event.getClickCount() == 2 && (!tableRow.isEmpty())) {
					if (this.mapTableRowEvents.containsKey(FxTableRowActions.DoubleClick)) {
						this.mapTableRowEvents.get(FxTableRowActions.DoubleClick).accept(tableRow);
					}
				}

			});
			return tableRow;

		});

	}

	private void activateFilters() {
		getFxTableColList().forEach(fxTableCol -> {
			activateFilter(fxTableCol);
		});
	}


	public void activateHeader(FxTableCol fxTableCol) {

	}


	private void activateFilter(FxTableCol fxTableCol) {

		Boolean headerAdded = false;

		if (FiBool.isFalse(fxTableCol.getBoFilterable())) {
			return;
		}

		if (FiBool.isTrue(fxTableCol.getBoFilterable())) {
			setupHeaderFilterNode(fxTableCol);
			headerAdded = true;
		}

		if (getEnabledLocalFilterEditor() || getEnabledRemoteFilterEditor()) {
			if (!headerAdded) {
				if (fxTableCol.getBoFilterable() == null) fxTableCol.setBoFilterable(true);
				setupHeaderFilterNode(fxTableCol);
				headerAdded = true;
			}
		}


		if (getEnabledLocalFilterEditor() || FiType.isTrue(fxTableCol.getBoFilterable())) {

			if (!headerAdded) {
				if (fxTableCol.getBoFilterable() == null) fxTableCol.setBoFilterable(true);
				setupHeaderFilterNode(fxTableCol);
				headerAdded = true;
			}

			if (fxTableCol.getColFilterNode() == null) defAutoEditorClass(Arrays.asList(fxTableCol));

			//ChangeListener changeListener = (observable, oldValue, newValue) -> filterLocal(newValue); // fxtablecol da eklenebilir
			Consumer<String> changeConsumer = textProp -> filterLocal(textProp);
			FxEditorFactory.registerTextPropertyWithDurationForFilterNode(fxTableCol, changeConsumer, 250);
			FxEditorFactory.registerEnterFnForFilterNode(fxTableCol, getColFilterNodeEnterFnGlobal());
			//fxTableCol.getTxfFilter().textProperty().addListener(changeListener);
		}

		if (getEnabledRemoteFilterEditor() && !FiBool.isFalse(fxTableCol.getBoFilterable())) {

			if (!headerAdded) {
				if (fxTableCol.getBoFilterable() == null) fxTableCol.setBoFilterable(true);
				setupHeaderFilterNode(fxTableCol);
				headerAdded = true;
			}

			FxEditorFactory.registerEnterFnForFilterNode(fxTableCol, getColFilterNodeEnterFnGlobal());
		}

	}

	/**
	 * Row Actionları,Güncellemeleri için yapılması gerekir
	 */
//	@Deprecated
//	public void updateRowFactory() {
//
//		setRowFactory(tblview -> {
//
//			TableRow row = new TableRow() {
//				@Override
//				// item param, entity karşılık geliyor.
//				protected void updateItem(Object item, boolean empty) {
//
//					super.updateItem(item, empty);
//					if (getFiRowFactoryUpdateFn() != null) {
//						getFiRowFactoryUpdateFn().accept(item, empty, this);
//					}
//				}
//			};
//
//			//Loghelperr.getInstance(getClass()).debug(" Row Generated" + row.getId());
//
//			if (getFiRowDoubleClickEvent() != null) {
//				row.setOnMouseClicked(event -> {
//					if (event.getClickCount() == 2 && !row.isEmpty()) {
//						getFiRowDoubleClickEvent().accept(event, row);
//						//row.getStyleClass().add("selectedRow");
//					}
//				});
//			}
//			return row;
//
//		});
//
//	}
	private void setupHeaderPaneAndTitle(FxTableCol fxcol) {

		if (fxcol.getPaneHeader() != null) return;

		FxLabel label = new FxLabel(fxcol.getOfcTxHeader());
		label.setStyle("-fx-padding: 2px;");
		label.setWrapText(true);
		label.setAlignment(Pos.CENTER);
		label.setTextAlignment(TextAlignment.CENTER);

		//fxButton.getStyleClass().add("dialogx" + indexbutton.toString());
		//hboxSwingBar.getChildren().remove(hboxSwingBar.lookup(".dialogx" + jDialog.getIndex()));

		//StackPane stack = new StackPane();
		FxMigPane vbox = new FxMigPane("insets 0,gap 0 0");
		//vbox.setAlignment(Pos.TOP_LEFT);

		//vbox.getChildren().add(label);
		vbox.add(label, "span");

		vbox.prefWidthProperty().bind(fxcol.widthProperty().subtract(5));
		label.prefWidthProperty().bind(vbox.prefWidthProperty());

		//fxcol.setFiHeaderAsVbox(vbox, fxcol);
		fxcol.setPaneHeader(vbox);
		fxcol.setText(fxcol.getOfcTxHeader());


	}


	private void setupHeaderFilterNode(FxTableCol fxcol) {

		if (fxcol.getColFilterNode() != null) return;

		//Loghelperr.getInstance(getClass()).debug(" Fi Header Setup:" + fxcol.getFiHeader());


		//TextField txfCol = new TextField();
		//new FxEditorFactory
		//fxcol.setTxfFilter(fxcol.getEditorFxNode());

		// Filter Editor Node Eklenir
		// Loghelperr.getInstance(getClass()).debug(" Fxcol:"+ fxcol.getFieldName());

		//if(FiBoolean.isNotTrue(fxcol.getColFilterable()) || fxcol.getSummaryType()==null) return;

		//if (fxcol.getColFxNode() == null) {
		FxMigPane vbox = fxcol.getPaneHeader();
		Node node = null;
		node = defAutoEditorClass(Arrays.asList(fxcol));
		node.setId("filterNode");

		if (FiBool.isFalse(fxcol.getBoFilterable())) {
			//Loghelperr.getInstance(getClass()).debug("Node Filter Pasif");
			node.setDisable(true);
		}
		// filter node aşağı tuşuna basınca elemanlar gider
		node.addEventHandler(KeyEvent.KEY_PRESSED,getColFilterKeyDownEvent());

		node.getStyleClass().add(headerFilterNodeStyleClass);


		//vbox.getChildren().add(fxcol.getColFxNode());
		//vbox.getChildren().add(node);
		vbox.add(node, "span");
		fxcol.setColFilterNode(node);
		//}


//      08-08-2019
//		if (getEnableSummaryRowHeader()) {
//			//Loghelperr.getInstance(getClass()).debug("Summary Row Header");
//			Node lblSummary = activateSummaryNode(fxcol, vbox);
//			vbox.getChildren().add(lblSummary);
//		}


		//fxcol.setFiHeaderAsVbox(vbox, fxcol);


	}

	private void setupHeaderSummaryNode(FxTableCol fxcol) {

		//09-08-2019
		if (fxcol.getColHeaderSummaryNode() != null) return;

		if (fxcol.getSummaryType() == null) {
			//fxcol.set
		}

		// Filtre editor eklenmemiş eklensin
		if (fxcol.getColFilterNode() == null) setupHeaderFilterNode(fxcol);

		if (fxcol.getSummaryType() == OzColSummaryType.CheckBox) {

			FxMigPane pane = new FxMigPane("insets 0");

			FxCheckBox fxCheckBox = new FxCheckBox();

			fxcol.setColHeaderSummaryNode(fxCheckBox);
			fxcol.setSummaryFxCheckBox(fxCheckBox);

			fxCheckBox.setOnAction(event -> {

				Boolean boStatus = fxCheckBox.isSelected();

				getItemsAll().forEach(ent -> {

					Boolean disabledSelection = false;

					if (fxcol.getPredFiEditorDisable() != null && fxcol.getPredFiEditorDisable().test(ent)) {
						disabledSelection = true;
					}

					if (!disabledSelection) {
						try {
							PropertyUtils.setNestedProperty(ent, fxcol.getOfcTxFieldName(), false);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						}
					}


				});

				getItemsCurrent().forEach(ent -> {

					Boolean disabledSelection = false;

					if (fxcol.getPredFiEditorDisable() != null && fxcol.getPredFiEditorDisable().test(ent)) {
						disabledSelection = true;
					}

					if (!disabledSelection) {
						try {
							PropertyUtils.setNestedProperty(ent, fxcol.getOfcTxFieldName(), boStatus);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						}
					}


				});

				fiRefreshTable();


			});

			fxCheckBox.setAlignment(Pos.CENTER);
			pane.add(fxCheckBox, "span,push,align 50%");

			//fxcol.getFiPaneHeader().getChildren().add(pane);
			fxcol.getPaneHeader().add(pane, "span,grow");
			return;
		}

		FxLabel lblSummary = new FxLabel("");

		lblSummary.setStyle("-fx-padding: 1px;");
		lblSummary.getStyleClass().add(headerSummaryClass);
		lblSummary.setWrapText(true);
		lblSummary.setAlignment(Pos.CENTER_LEFT);
		//lblSummary.setTextAlignment(TextAlignment.RIGHT);
		lblSummary.prefWidthProperty().bind(fxcol.getPaneHeader().prefWidthProperty());
		fxcol.setSummaryLabelNode(lblSummary);
		fxcol.setColHeaderSummaryNode(lblSummary);

		//fxcol.getFiPaneHeader().getChildren().add(lblSummary);
		fxcol.getPaneHeader().add(lblSummary, "span");
		return;
	}


	/*	private int countHeaderClick = -1 ;

	@Override
	public void sort() {
		final ObservableList<? extends TableColumn<S, ?>> sortOrder = getSortOrder();

		countHeaderClick++;
		super.sort();
		if (sortOrder.size() > 0) {
			//columnClicked = sortOrder.get(0).getId(); // name of column clicked

			//strSortTypeValue = sortOrder.get(0).getSortType().toString(); // ascending or descending

		} else {
           //<what ever you want to perform>
		}
	}*/

	/**
	 * sıralama daha düzgün oluyor , unsorted çalışıyor
	 * filtered kullanılır
	 *
	 * @param listTable
	 */
//	@Deprecated
//	public void setItemsAsSortedList(List listTable) {
//
//		if (FiObject.isTrue(filteredListActive)) {
//			setItemsAsFilteredList(listTable);
//			return;
//		}
//
//		SortedList sortedList = new SortedList(FXCollections.observableArrayList(listTable));
//
//		setItems(sortedList);//.addAll(listRapor);
//
//		sortedList.comparatorProperty().bind(comparatorProperty());
//
//	}
	public void setItemsAsFilteredList(List listTable) {

		if (listTable == null) listTable = new ArrayList();

		//System.out.println("Filter List Active");

		FilteredList filteredList = new FilteredList(FXCollections.observableArrayList(listTable), getAllFilterPredicates());
		setFilteredList(filteredList);
		if (getConfiAutoScrollToLast()) scrollToLastForFilteredList();

		SortedList sortableData = new SortedList<>(filteredList);
		setItems(sortableData);
		sortableData.comparatorProperty().bind(this.comparatorProperty());
		eventsAfterTableViewDataChange();

		//setItems(filteredList);//.addAll(listRapor);

	}

	/**
	 * Tablonun içinde bulunan satırları filtreler
	 *
	 * @param newValue
	 */
	private void filterLocal(Object newValue) {

		//ObservableValue<? extends String> observable, String oldValue, String newValue, String fieldName
		//System.out.println("text:"+ newValue);

		//Loghelperr.getInstance(getClass()).debug("Filter Lokal");

		Predicate predFilterLocal = ent -> {

			Boolean filterCheckResult = true;

			for (FxTableCol fxTableColumn : getFxTableColList()) {

				// sütun filtrelenebilir olması gerekir
				if (FiBool.isTrue(fxTableColumn.getBoFilterable())) {

					// filterCheckResult false olursa filtreden geçmez , sonuca girmez.
					// true olursa , filtreden geçerek sonuca dahil olur, eklenir.

					// !!!!!!!!!
					// true yakalarsa continue olacak , kontrole devam edecek
					// false yakalarsa return yapılacak , sonuca dahil edilmeyecek

					//Loghelperr.getInstance(getClass()).debug(" field"+ fxTableCol.getFieldName());
					Object objFilterValue = new FxEditorFactory().getNodeObjValueByFilterNode(fxTableColumn); // fxTableCol.getTxfFilter().getText();

					// Filter editor boşluk olunca onu integer çevirdiğinde null olarak dönüş yapar
					String txFilterValue = new FxEditorFactory().getValueOfFilterNodeAsString(fxTableColumn);

					//Loghelperr.getInstance(getClass()).debug(String.format("Filter Lokal %s : %s ",fxTableColumn.getFieldName(),objFilterValue));

					// !!! txFilterValue null veya boş gelemez , objCellValue null geçebilir (özel aramalar yüzünden)
					// Filter Değerleri Null , filtreleme yapmaz , direk sonuca ekler
					if (txFilterValue == null || txFilterValue.equals("")) {
						filterCheckResult = true;
						continue;
					}

					Object objCellValue = FiReflection.getProperty(ent, fxTableColumn.getOfcTxFieldName());

					// !!!!! CellValue için Null değerleri çevrim yapabiliriz
					if (objCellValue == null) {

						// Boolean için null değer false olarak yorumlandı
						if (fxTableColumn.getColType() == OzColType.Boolean) {
							objCellValue = false;
						}

					}

					// Özel Aramalar (! ,!! ) ve Boşluk Aramaları
					if (true) {

						//Loghelperr.getInstance(getClass()).debug("Instance Type" );
						//Loghelperr.getInstance(getClass()).debug("ozel kontrol:[" + txFilterValue + "]");

						if (txFilterValue.equals("!") || txFilterValue.matches("^\\s+")) {

							if (objCellValue == null || objCellValue.toString().trim().equals("")) {
								filterCheckResult = true;
								continue;
							} else {
								filterCheckResult = false;
								return filterCheckResult;
							}
						}

						if (txFilterValue.equals("!!")) {

							if (objCellValue == null || objCellValue.toString().trim().equals("") || objCellValue.toString().matches("^\\s+")) {
								filterCheckResult = false;
								return filterCheckResult;
							} else {
								filterCheckResult = true;
								continue;
							}

						}

					}


					if (objCellValue instanceof String) {

						Boolean filterCheckResultStr = filerLocalForString(objCellValue, objFilterValue);

						if (filterCheckResultStr) {
							continue;
						} else {
							return filterCheckResultStr;
						}

						//if(!txCellValue.contains(objFilterValue.toString()))filterCheckResult=false;

					}

					if (objCellValue instanceof Date) {

						// objCellValue dolu gelip, objFilterValue yoksa kontrol edilemez, sonuca dahil edilir
						if (objFilterValue == null) continue;

						//FiConsole.printObjectDefiniton(objFilterValue,"objFilterValue");

						// filterValue dolu olup, cell value boş olursa , sonuca dahil edilmez
						if (objCellValue == null) {
							//Loghelperr.getInstance(getClass()).debug("Null");
							filterCheckResult = false;
							return filterCheckResult;
						}

						Date dtCellValue = (Date) objCellValue;

						String strDateCell = FiDate.toStringYmd(dtCellValue);
						String strDateFilter = "";

						if (objFilterValue instanceof Date) {
							strDateFilter = FiDate.toStringYmd((Date) objFilterValue);
						} else {
							strDateFilter = objFilterValue.toString();
						}

						//Loghelperr.logSingle("DateCh",getClass(),"Date Cell Value"+ strDateCell + "   : Filter Value :" + strDateFilter );
						//if (!dtCellValue.equals(objFilterValue)) filterCheckResult = false;

						if (!strDateFilter.equals("") && !strDateCell.equals(strDateFilter)) filterCheckResult = false;

						if (filterCheckResult) {
							continue;
						} else {
							return filterCheckResult;
						}
					}


					if (objCellValue instanceof Double) {

						// objCellValue dolu gelip, objFilterValue yoksa kontrol edilemez, sonuca dahil edilir
						if (objFilterValue == null) continue;

						if (objCellValue == null) {
							filterCheckResult = false;
							return filterCheckResult;
						}

						Double valueCol = (Double) objCellValue;
						String txValueCol = new FiNumber().formatNumberPlain(valueCol);

						// Filter Value

						// W1 Double çevirmeli Yöntem
						//Double dblFilter = (Double) objFilterValue;
						//String txFilter = new FiNumber().formatNumberPlain(dblFilter);  //objFilterValue.toString().replaceAll("\\.0$","");
						// ///////////////////////

						//String valueFilter2 = objFilterValue.toString().replaceAll("(<|>)","");

						// Loghelperr.logSingle("filter1",getClass(),"value filter :"+ valueFilter2);
						// W2 Expo Number To String
						String txFilter = new FiNumber().formatStringExpoNumber(objFilterValue.toString());

						//Loghelperr.getInstance(getClass()).debug(String.format("Double Obj Value : %s , Filter Value: %s",txValueCol,txFilter));
						if (!txValueCol.toString().matches(".*" + txFilter + ".*")) {
							filterCheckResult = false;
						}


						if (filterCheckResult) {
							continue;
						} else {
							return filterCheckResult;
						}

					}

					if (objCellValue instanceof Integer) {

						// objCellValue dolu gelip, objFilterValue yoksa kontrol edilemez, sonuca dahil edilir
						if (objFilterValue == null) continue;

						if (objCellValue == null) {
							filterCheckResult = false;
							return filterCheckResult;
						}

						//Integer cellValue = (Integer) objCellValue;
						String txValueCol = objCellValue.toString();
						String txFilter = objFilterValue.toString(); //new FiNumber().formatStringExpoNumber(objFilterValue.toString());
						txFilter = txFilter.replace(" ", "\\s");
						//Loghelperr.getInstance(getClass()).debug(String.format("Double Obj Value : %s , Filter Value: %s",txValueCol,txFilter));
						if (!txValueCol.toString().matches(txFilter + ".*")) {
							filterCheckResult = false;
							return filterCheckResult;
						}

						if (filterCheckResult) {
							continue;
						} else {
							return filterCheckResult;
						}

					}

					if (objCellValue instanceof Boolean) {

						objFilterValue = FiBool.convertBooleanElseValue(objFilterValue, null);

						//FiConsole.printObjectDefinitonLimityByClass(objFilterValue, "Filter Value", getClass());

						// objCellValue dolu gelip, objFilterValue yoksa kontrol edilemez, sonuca dahil edilir
						if (objFilterValue == null) continue;

						//Loghelperr.getInstance(getClass()).debug(" Obj Filter :" + objFilterValue + " Obj Cell Value" + objCellValue.toString());

						//if (objCellValue == null) objCellValue = false;
						Boolean boCellValue = FiBool.convertBooleanElseFalse(objCellValue);

						//FiConsole.printObjectDefinitonLimityByClass(boCellValue, "Bo Cell Value", getClass());

						Boolean boFilterValue = (Boolean) objFilterValue;

						if (boFilterValue.equals(boCellValue)) {
							continue;
						} else {
							filterCheckResult = false;
							return filterCheckResult;
						}

					}


					// son kontrol
					if (objFilterValue != null && objCellValue == null) {
						filterCheckResult = false;
						return filterCheckResult;
					}

				}

			} // end of For
			//Loghelperr.getInstance(getClass()).debug("-----------");
			return filterCheckResult;
		};

		setPredFilterLocalThenUpdate(predFilterLocal);

		//eventsAfterTableViewDataChange();

	}

	private Boolean filerLocalForString(Object objCellValue, Object objFilterValue) {

		// objFilterValue yoksa kontrol edilemez, sonuca dahil edilir
		if (objFilterValue == null) return true;

		// filter value varken, cellvalue null ise sonuca dahil olamaz.
		if (objCellValue == null) {
			return false;
			//filterCheckResult = false;
			//return filterCheckResult;
		}

		//Loghelperr.getInstance(getClass()).debug("Obj Value"+ objCellValue.toString());

		String txCellValue = (String) objCellValue;
		String txFilterValueLower = objFilterValue.toString().toLowerCase();

		String regexPattern = null;
		if (txFilterValueLower.trim().equals("")) {
			regexPattern = "\\s*";
		} else {
			txFilterValueLower.replace(" ", "\\s");
			regexPattern = ".*" + txFilterValueLower + ".*";  // txFilterValue
		}

		if (!txCellValue.toLowerCase().matches(regexPattern)) {
			//filterCheckResult = false;
			return false;
		}

		return true;

	}

	public void updateStatusBar() {

		//Loghelperr.debugLog(getClass(), "update status barr");

		if (getFxTableMig() != null) {
			Platform.runLater(() -> {
				getFxTableMig().getLblFooter().setText(" Kayıt Sayısı : " + getFilteredList().size());
			});
		}

	}

	public void updateSummary() {

		if (getFilteredList() == null || getFilteredList().size() == 0) {

			if (getEnableSummaryRowHeader()) {

				getFxTableColList().forEach(fxTableCol -> {

					if (fxTableCol.getSummaryLabelNode() != null && fxTableCol.getSummaryType() != null) {
						Platform.runLater(() -> {
							fxTableCol.getSummaryLabelNode().setText("");
							new FxTableModal().styleSummaryLabel(fxTableCol.getSummaryLabelNode(), fxTableCol);
						});
					}

				});

			}

			return;
		}

		//Loghelperr.debugLog(getClass(), "update summary");

		if (getEnableSummaryRowHeader()) {

			getFxTableColList().forEach(fxTableCol -> {

				if (fxTableCol.getSummaryLabelNode() != null && fxTableCol.getSummaryType() != null) {
					Platform.runLater(() -> {
						String sumValue = new FiNumber().formatNumber(new FxTableModal().calcSummaryValue(getFilteredList(), fxTableCol,new FiReportConfig()));
						fxTableCol.getSummaryLabelNode().setText(sumValue);
						new FxTableModal().styleSummaryLabel(fxTableCol.getSummaryLabelNode(), fxTableCol);
					});
				}
			});

		}


	}


	public FxTableCol getColumnByID(String colID) {

		//ObservableList<TableColumn<S, ?>> columns = getColumns();

		for (FxTableCol fxTableCol : getFxTableColList()) {
			if (fxTableCol.getId().equals(colID)) {
				return fxTableCol;
			}

		}
		return null;
	}

	public FxTableCol getColumnByFieldName(String fieldName) {

		for (FxTableCol fxTableCol : getFxTableColList()) {
			if (fxTableCol.getOfcTxFieldName().equals(fieldName)) {
				return fxTableCol;
			}
		}
		return null;
	}

	public List<FxTableCol> getFxTableColList() {
		if (fxTableColList == null) {
			fxTableColList = new ArrayList<>();
		}
		return fxTableColList;
	}

	private void setFxTableColList(List<FxTableCol> fxTableColList) {
		this.fxTableColList = fxTableColList;
	}

	public Map<String, Object> getStyleMap() {
		if (this.styleMap == null) styleMap = new HashMap<>();
		return styleMap;
	}

	public void setStyleMap(Map<String, Object> styleMap) {
		this.styleMap = styleMap;
	}

	public Boolean getFilteredListActive() {
		return filteredListActive;
	}

	public void setFilteredListActive(Boolean filteredListActive) {
		this.filteredListActive = filteredListActive;
	}

	public FilteredList getFilteredList() {
		if (filteredList == null) {
			setItemsAsFilteredList(new ArrayList());
		}
		return filteredList;
	}

	public ObservableList getFiSourceList() {
		return getFilteredList().getSource();
	}

	private void setFilteredList(FilteredList filteredList) {
		this.filteredList = filteredList;
	}

	public Node getEditorComp(String fieldName) {

		FxTableCol ozTableCol = getColumnByFieldName(fieldName);
		return ozTableCol.getColFilterNode();

	}

	public FxTextField getEditorCompFxTexfield(String fieldName) {

		FxTableCol ozTableCol = getColumnByFieldName(fieldName);

		if (ozTableCol.getFilterNodeClass().equals(FxTextField.class.getName())) {
			FxTextField comp = (FxTextField) ozTableCol.getColFilterNode();
			return comp;
		}

		return null;
	}

	public FxTextFieldBtn getEditorCompFxTexfieldWithButt(String fieldName) {

		FxTableCol ozTableCol = getColumnByFieldName(fieldName);

		if (ozTableCol.getFilterNodeClass().equals(FxTextFieldBtn.class.getName())) {
			FxTextFieldBtn comp = (FxTextFieldBtn) ozTableCol.getColFilterNode();
			return comp;
		}

		return null;
	}

//
//
//		if (ozTableCol.getColFxNodeClass().equals(DatePicker.class.getName())) {
//			DatePicker comp = (DatePicker) ozTableCol.getEditorFxNode();
//			FxComponent<DatePicker> fxcomp = new FxComponent();
//			return fxcomp;
//		}
//
//		if (ozTableCol.getColFxNodeClass().equals(TextField.class.getName())) {
//			FxTextField comp = (FxTextField) ozTableCol.getEditorFxNode();
//			FxComponent<FxTextField> fxcomp = new FxComponent();
//			return fxcomp;
//		}


	public Boolean getEnabledLocalFilterEditor() {
		if (this.enabledLocalFilterEditor == null) return false;
		return enabledLocalFilterEditor;
	}

	public void setEnabledLocalFilterEditor(Boolean enabledLocalFilterEditor) {
		this.enabledLocalFilterEditor = enabledLocalFilterEditor;
	}

	public Boolean getEnabledRemoteFilterEditor() {
		if (this.enabledRemoteFilterEditor == null) return false;
		return enabledRemoteFilterEditor;
	}

	public void setEnabledRemoteFilterEditor(Boolean enabledRemoteFilterEditor) {
		this.enabledRemoteFilterEditor = enabledRemoteFilterEditor;
	}

	public static void autoFilterEventToAllCol(List<? extends IFiCol> listCol, EventHandler<KeyEvent> eventFilter) {

		for (int i = 0; i < listCol.size(); i++) {
			IFiCol tableCol = listCol.get(i);
			tableCol.setColFilterKeyEvent(eventFilter);
		}


	}

	public FnResult excelOpen(String appDir, String fileName) {
		return excelOpen(appDir, fileName, false);
	}

	public FnResult excelOpen(String appDir, String fileName, Boolean performanceEnabled) {

		String fullPath = appDir + "\\" + fileName;

		FnResult result = FnResult.genInstance();

		Path pathEntegre = Paths.get(appDir);

		if (!Files.exists(pathEntegre)) {
			new File(pathEntegre.toString()).mkdirs();
		}

		FiExcel2.genInstance().writeFxTableViewToExcelWithHeader2(this, Paths.get(fullPath), null, null, performanceEnabled);

		File fileResult = new File(fullPath);

		if (fileResult != null) {
			new FiExcel2().openExcelFileWithApp(fileResult);
			return result.buildResult(true);
		} else {
			new FxDialogShow().showModalWarningAlert("Dosya Oluşturulamadı.Sistem Yöneticinize Başvurun.");
			return result.buildResult(false);
		}

	}

	@FiDraft
	public MigPane pagingPane() {

		MigPane migPane = new MigPane();
		return null;

	}

	public void scrollToLastForFilteredList() {

		final int size = getFilteredList().getSource().size();
		scrollTo(size - 1);

//		getFilteredList().getSource().addListener((ListChangeListener<S>) (c -> {
//			c.next();
//			final int size = getFilteredList().getSource().size();
//			if (size > 0) {
//				scrollTo(size - 1);
//			}
//		}));

	}


	public void initFilteredList() {

		if (getFilteredList() == null) {
			setItemsAsFilteredList(new ArrayList());
		}

	}

	public void setPredFilterOut(Predicate predFilterOutSet) {
		//this.predFilterOut = predFilterOut;
		if (predFilterOutSet == null) {
			setListPredFilterExtra(null);
			return;
		}
		setListPredFilterExtra(Arrays.asList(predFilterOutSet));
		updateFiltersLocalAndOut();
	}

	public void appendPredFilter(Predicate predFilter) {

		if (getListPredFilterExtra() == null) {
			setListPredFilterExtra(Arrays.asList(predFilter));
		} else {
			getListPredFilterExtra().add(predFilter);
		}

		updateFiltersLocalAndOut();
	}

	public void updateFiltersLocalAndOut() {

		//System.out.println("getfiltered list :" + (getFilteredList()==null));
		getFilteredList().setPredicate(getAllFilterPredicates());
		eventsAfterTableViewDataChange();
	}

	private Predicate getAllFilterPredicates() {

		Predicate predAll = ent -> true;

		if (getPredFilterLocal() != null) predAll = predAll.and(getPredFilterLocal());

		if (getPredFilterRemote() != null) predAll = predAll.and(getPredFilterRemote());

		if (FiCollection.isNotEmpty(getListPredFilterExtra())) {
			//Loghelperr.getInstance(getClass()).debug("Size Filter Out : "+ getListPredFilterExtra().size());
			for (Predicate predItem : getListPredFilterExtra()) {
				predAll = predAll.and(predItem);
			}
		}

		return predAll;
	}

	private void eventsAfterTableViewDataChange() {
		updateSummary();
		updateStatusBar();
	}

	private void setPredFilterLocalThenUpdate(Predicate predFilterIn) {
		this.predFilterLocal = predFilterIn;
		//setListpredFilterIn(Arrays.asList(predFilterLocal));
		updateFiltersLocalAndOut();
	}

	public void removeFxTableCol(FxTableCol fxTableCol) {
		getColumns().remove(fxTableCol);
		getFxTableColList().remove(fxTableCol);
	}

	@Override
	public String getFxId() {
		return fxId;
	}

	@Override
	public void setFxId(String fxId) {
		this.fxId = fxId;
	}

	public EventHandler<KeyEvent> getColFilterNodeEnterFnGlobal() {
		return colFilterNodeEnterFnGlobal;
	}

	public void setColFilterNodeEnterFnGlobal(EventHandler<KeyEvent> colFilterNodeEnterFnGlobal) {
		this.colFilterNodeEnterFnGlobal = colFilterNodeEnterFnGlobal;
	}

	public Boolean getEnableSummaryRowHeader() {
		if (enableSummaryRowHeader == null) return false;
		return enableSummaryRowHeader;
	}

	public void setEnableSummaryRowHeader(Boolean value) {
		this.enableSummaryRowHeader = value;
		activateHeaderSummary();
	}

	private void activateHeaderSummary() {
		getFxTableColList().forEach(fxTableCol -> {
			if (getEnableSummaryRowHeader()) setupHeaderSummaryNode(fxTableCol);
		});
	}

//	public BiConsumer<MouseEvent, TableRow> getFiRowDoubleClickEvent() {
//		return fiRowDoubleClickEvent;
//	}

	/**
	 * Entity ulaşmak için
	 * <br>TableRow row = (TableRow) event.getSource();
	 * <br>Entity myEntity = (Entity) row.getItem();
	 * <p>
	 * //	 * @param fiRowDoubleClickEvent
	 */
//	public void setFiRowDoubleClickEvent(BiConsumer<MouseEvent, TableRow> fiRowDoubleClickEvent) {
//		this.fiRowDoubleClickEvent = fiRowDoubleClickEvent;
//		activeRowFactory();
//	}

//	public TriConsumer<Object, Boolean, TableRow> getFiRowFactoryUpdateFn() {
//		return fiRowFactoryUpdateFn;
//	}
//
//	public void setFiRowFactoryUpdateFn(TriConsumer<Object, Boolean, TableRow> fiRowFactoryUpdateFn) {
//		this.fiRowFactoryUpdateFn = fiRowFactoryUpdateFn;
//		activeRowFactory();
//	}

//	private void activeRowFactory() {
//		updateRowFactory();
//		this.refresh();
//	}

//	public void activateCellFactoryClass(FxTableCol fxTableCol) {
//		setFiCellEditorFactory(fxTableCol);
//	}
	public Boolean getConfiAutoScrollToLast() {
		if (confiAutoScrollToLast == null) return false;
		return confiAutoScrollToLast;
	}

	public void setConfiAutoScrollToLast(Boolean confiAutoScrollToLast) {
		this.confiAutoScrollToLast = confiAutoScrollToLast;
	}

	@SuppressWarnings("rawtypes")
	private void copySelectionToClipboard() {

		final Set<Integer> rows = new TreeSet<>();
		for (final TablePosition tablePosition : getSelectionModel().getSelectedCells()) {
			rows.add(tablePosition.getRow());
		}

		final StringBuilder strb = new StringBuilder();
		boolean firstRow = true;

		for (final Integer row : rows) {
			if (!firstRow) {
				strb.append('\n');
			}
			firstRow = false;
			boolean firstCol = true;
			for (final TableColumn<?, ?> column : getColumns()) {
				if (!firstCol) {
					strb.append('\t');
				}
				firstCol = false;
				final Object cellData = column.getCellData(row);
				strb.append(cellData == null ? "" : cellData.toString());
			}
		}

		final ClipboardContent clipboardContent = new ClipboardContent();
		clipboardContent.putString(strb.toString());
		Clipboard.getSystemClipboard().setContent(clipboardContent);

	}

	/**
	 * Get table selection and copy it to the clipboard.
	 */
	public void copySelectionToClipboard2() {

		StringBuilder clipboardString = new StringBuilder();

		ObservableList<TablePosition> positionList = getSelectionModel().getSelectedCells();

		int prevRow = -1;

		for (TablePosition position : positionList) {

			int row = position.getRow();
			int col = position.getColumn();

			// determine whether we advance in a row (tab) or a column
			// (newline).
			if (prevRow == row) {

				clipboardString.append('\t');

			} else if (prevRow != -1) {

				clipboardString.append('\n');

			}

			// create string from cell
			String text = "";

			Object observableValue = (Object) getColumns().get(col).getCellObservableValue(row);

			// null-check: provide empty string for nulls
			if (observableValue == null) {
				text = "";
			} else if (observableValue instanceof DoubleProperty) { // TODO: handle boolean etc

				text = NumberFormat.getNumberInstance().format(((DoubleProperty) observableValue).get());

			} else if (observableValue instanceof IntegerProperty) {

				text = NumberFormat.getNumberInstance().format(((IntegerProperty) observableValue).get());

			} else if (observableValue instanceof StringProperty) {
				text = ((StringProperty) observableValue).get();

			} else {
				//System.out.println("Unsupported observable value: " + observableValue);
				text = ((ObjectProperty) observableValue).getValue().toString();
			}

			// add new item to clipboard
			clipboardString.append(text);

			// remember previous
			prevRow = row;
		}

		// create clipboard content
		final ClipboardContent clipboardContent = new ClipboardContent();
		clipboardContent.putString(clipboardString.toString());

		// set clipboard content
		Clipboard.getSystemClipboard().setContent(clipboardContent);


	}

	public void setupCopySelectionToClipboard() {

		final KeyCodeCombination keyCodeCopy = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);

		propTblKeyEvent.addListener((observable, oldValue, newValue) -> {

			if (keyCodeCopy.match((KeyEvent) newValue)) {
				copySelectionToClipboard2();
			}

		});

	}


	public void activateFxTableSelectAndClose(IFxTableSelectionCont iFxMosCont) {

		setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.ENTER) {
				//getRunnableSelectAndClose().run();
				iFxMosCont.setEntitySelected(getSelectionModel().getSelectedItem());
				iFxMosCont.setCloseReason("done");
				iFxMosCont.getFxStageInit().close();
			}

			// default olarak eklendi
//			if (event.getCode() == KeyCode.DOWN) {
//				Platform.runLater(() -> {
//					requestFocus();
//				});
//			}

		});

		onFiRowDoubleClickEvent(tableRow -> {
			//getRunnableSelectAndClose().run();
			iFxMosCont.setEntitySelected(getSelectionModel().getSelectedItem());
			iFxMosCont.setCloseReason("done");
			iFxMosCont.getFxStageInit().close();
		});
	}


	public Event getPropTblKeyEvent() {
		return propTblKeyEvent.get();
	}

	public ObjectProperty<KeyEvent> propTblKeyEventProperty() {
		return propTblKeyEvent;
	}

	// Getter and Setters

	public Map<FxTableRowActions, Consumer<TableRow>> getMapTableRowEvents() {
		if (this.mapTableRowEvents == null) {
			setupRowFactory();
		}
		return mapTableRowEvents;
	}

	public FxTableMig getFxTableMig() {
		return fxTableMig;
	}

	public void setFxTableMig(FxTableMig fxTableMig) {
		this.fxTableMig = fxTableMig;
	}

	public Class<EntClazz> getEntityClazz() {
		return entityClazz;
	}

	public void setEntityClazz(Class<EntClazz> entityClazz) {
		this.entityClazz = entityClazz;
	}

	public List<Predicate> getListPredFilterExtra() {
		return listPredFilterExtra;
	}

	public void setListPredFilterExtra(List<Predicate> listPredFilterExtra) {
		this.listPredFilterExtra = listPredFilterExtra;
	}

	public Predicate getPredFilterLocal() {
		return predFilterLocal;
	}

	public Predicate getPredFilterRemote() {
		return predFilterRemote;
	}

	public void setPredFilterRemote(Predicate predFilterRemote) {
		this.predFilterRemote = predFilterRemote;
	}

	public EventHandler<KeyEvent> getColFilterKeyDownEvent() {
		if (colFilterKeyDownEvent == null) {
			EventHandler<KeyEvent> customKeyEvent = keyEvent -> {
				if(keyEvent.getCode() == KeyCode.DOWN){
					Platform.runLater(() -> {
						requestFocus();
					});
				}
			};
			colFilterKeyDownEvent = customKeyEvent;
		}
		return colFilterKeyDownEvent;
	}

	public void setColFilterKeyDownEvent(EventHandler<KeyEvent> colFilterKeyDownEvent) {this.colFilterKeyDownEvent = colFilterKeyDownEvent;}

//	public Runnable getRunnableSelectAndClose() {
//		if(runnableSelectAndClose ==null){
//			Runnable runnable = () -> {};
//			return runnable;
//		}
//		return runnableSelectAndClose;
//	}
//
//	public void setRunnableSelectAndClose(Runnable runnableSelectAndClose) {
//		this.runnableSelectAndClose = runnableSelectAndClose;
//	}
}
