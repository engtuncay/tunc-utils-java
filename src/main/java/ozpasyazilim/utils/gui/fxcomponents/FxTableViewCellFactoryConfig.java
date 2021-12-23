package ozpasyazilim.utils.gui.fxcomponents;

import de.jensd.fx.glyphs.icons525.Icons525;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;
import ozpasyazilim.utils.core.FiBoolean;
import ozpasyazilim.utils.core.FiString;
import ozpasyazilim.utils.gui.fxTableViewExtra.EnumColNodeType;
import ozpasyazilim.utils.core.FiReflection;
import ozpasyazilim.utils.table.OzColType;

/**
 * Cell Factory, TableView hücrelerinin içerisine yerleşecek componentlerin ayarlanmasını sağlar
 *
 */
public class FxTableViewCellFactoryConfig {

	/**
	 * Eğer cell editor class tanımlanmamışsa, default cell factory ayarları buradan yapılır.
	 *
	 * @param fxTableCol
	 */
	public static void setupCellFactoryByDefault(FxTableCol2 fxTableCol) {

// Loghelper.get(FxTableViewCellEditorFactoryConfig.class).debug("Cell Editor Setup Col Name:" + fxTableCol.getFiCol().getFieldName());

		// Editor Class belirlenmişse , auto Editor Tanımlanmaz
		if (!FiString.isEmpty(fxTableCol.getFiCol().getColEditorClass())) return; // if içinde başında ! vardı

		if (fxTableCol.getFiCol().getColType() == OzColType.Boolean) {

			fxTableCol.styleAlignCenterFi();

//			fxTableCol.setCellValueFactory(param -> {
//				TableColumn.CellDataFeatures paramm = (TableColumn.CellDataFeatures) param;
//				paramm.
//			} );

			// callback <P,R> parameter(argumant) type and return type
			Callback<TableColumn, TableCell> cellFactoryCheckBox = getCellFactoryForCheckBox(fxTableCol);

			fxTableCol.setCellFactory(cellFactoryCheckBox);

		}

//		if (fxTableCol.getFiCol().getColEditorClassInit().equals(EnumColNodeType.FxLabelCell.toString())) {
//
//			// fxTableCol.styleAlignCenterFi();
//
//			// callback <P,R> parameter(argumant) type and return type
//			Callback<TableColumn, TableCell> cellFactoryCheckBox = getCellFactoryFxLabelCell(fxTableCol);
//			fxTableCol.setCellFactory(cellFactoryCheckBox);
//
//		}

	}

	private static Callback<TableColumn, TableCell> getCellFactoryForCheckBox(FxTableCol2 fxTableCol) {

		return new Callback<TableColumn, TableCell>() {

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

							if (FiBoolean.isTrue(fxTableCol.getFiCol().getBoEditable())) {

								Object entity = getTableView().getItems().get(getIndex());

								if (fxTableCol.getFiCol().getPredFiEditorDisable() != null) {
									node.setDisable(fxTableCol.getFiCol().getPredFiEditorDisable().test(entity));
								} else {
									node.setDisable(false);
								}

								node.setOnAction(actionEvent -> {
									Boolean result = new FiReflection().setter(entity, fxTableCol.getFiCol().getFieldName(), node.isSelected());
									if (!result) {
										FxDialogShow.build().showPopWarn("Yazılımsal Hata!!! Seçilemedi.");
									} else {
										if (fxTableCol.getFiCol().getFnColCellManualChanged() != null) {
											fxTableCol.getFiCol().getFnColCellManualChanged().accept(entity);
										}
										//updateFiltersLocalAndOut();
									}
								});

								if (fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue() != null) {
									fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue().accept(entity, node);
								}

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
	}

	/**
	 * Editor Class belirtilmişse , Cell Factory'si oluşturulur
	 *
	 * @param fxTableCol
	 */
	public static <EntClazz> void setupCellFactoryByEditorClass(FxTableView2<EntClazz> fxTableView2, FxTableCol2 fxTableCol) {

		if (fxTableCol.getFiCol().getColEditorClass() == null) return;

		if (fxTableCol.getFiCol().getColEditorClass().equals(CheckBox.class.getSimpleName())) {

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

			fxTableCol.setCellFactory(getCellFactoryForCheckBoxSimple());
			return;
		}


		if (fxTableCol.getFiCol().getColEditorClass().equals(Button.class.getSimpleName())
				|| fxTableCol.getFiCol().getColEditorClass().equals(Button.class.getName())) {
			Callback<TableColumn<EntClazz, String>, TableCell<EntClazz, String>> cellFactory = getCellFactoryForButtonSimple(fxTableCol);
			fxTableCol.setCellFactory(cellFactory);
			return;
		}

		// 26-09-2019 Genel İşlevli Factory Class , istenilen comp üretilip ona göre render edilir
		if (fxTableCol.getFiCol().getColEditorClass().equals(Node.class.getName())) {
			Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>> cellFactory = getCellFactoryForNodeGeneral(fxTableCol);
			fxTableCol.setCellFactory(cellFactory);
			return;
		}

		// 26-09-2019 FxButtonV2
		if (fxTableCol.getFiCol().getColEditorClass().equals(EnumColNodeType.FxButtonV2.toString())) {
			Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>> cellFactory = getCellFactoryForFxButtonV2(fxTableCol);
			fxTableCol.setCellFactory(cellFactory);
		}

		if (fxTableCol.getFiCol().getColEditorClass().equals(FxButton.class.getName())
				|| fxTableCol.getFiCol().getColEditorClass().equals(FxButton.class.getSimpleName())) {
			Callback<TableColumn<EntClazz, String>, TableCell<EntClazz, String>> cellFactory = getCellFactoryForFxButton(fxTableCol);
			fxTableCol.setCellFactory(cellFactory);
		}

		if (fxTableCol.getFiCol().getColEditorClass().equals(ToggleButton.class.getSimpleName())) {
			Callback<TableColumn<EntClazz, Boolean>, TableCell<EntClazz, Boolean>> cellFactory = getCellFactoryForToggleButton(fxTableCol);
			fxTableCol.setCellFactory(cellFactory);
		}

		if (fxTableCol.getFiCol().getColEditorClass().equals(FxStateButton.class.getSimpleName())) {
			Callback<TableColumn<EntClazz, Integer>, TableCell<EntClazz, Integer>> cellFactory = getCellFactoryForFxStateButton(fxTableCol);
			fxTableCol.setCellFactory(cellFactory);
		}

		if (fxTableCol.getFiCol().getColEditorClass().equals(FxStateButtonThree.class.getSimpleName())) {
			Callback<TableColumn<EntClazz, Integer>, TableCell<EntClazz, Integer>> cellFactory = getCellFactoryForFxStateButtonThree(fxTableCol);
			fxTableCol.setCellFactory(cellFactory);
			return;
		}

		if (fxTableCol.getFiCol().getColEditorClass().equals(EnumColNodeType.FxButtonCustom.toString())) {
			fxTableCol.styleAlignCenterFi();
			Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>> cellFactory = getCellFactoryForFxButtonCustom(fxTableCol);
			fxTableCol.setCellFactory(cellFactory);
			return;
		}

		if (fxTableCol.getFiCol().getColEditorClass().equals(EnumColNodeType.FxLabelStatus.toString())) {
			fxTableCol.styleAlignCenterFi();
			Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>> cellFactory = getCellFactoryForFxLabelStatus(fxTableCol);
			fxTableCol.setCellFactory(cellFactory);
			return;
		}

	}

	private static <EntClazz> Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>> getCellFactoryForFxButtonCustom(FxTableCol2 fxTableCol) {
		// callback S : entity , Field Tür : Object : Herhangi bir tipde hücre alanı olabilir
		Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>> cellFactory =
				new Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>>() {
					@Override
					public TableCell call(final TableColumn<EntClazz, Object> param) {

						final TableCell<EntClazz, Object> cell = new TableCell<EntClazz, Object>() {

							// table cell objesi

							final FxStateButton btn = new FxStateButton(fxTableCol.getFiCol().getColEditorNodeText());

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

									if (fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue() != null) {
										fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue().accept(entity, btn);
									}

									btn.setOnAction(event -> {

										//S entity = getTableView().getItems().get(getIndex()); // table cell objesinin gettableview methodu

										// Action gerçekleştirilir, daha sonra renderer işlemleri yapılır
										if (fxTableCol.getFiCol().getFnEditorSetOnAction() != null)
											fxTableCol.getFiCol().getFnEditorSetOnAction().accept(entity, btn);

										if (fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue() != null)
											fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue().accept(entity, btn);

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
		return cellFactory;
	}

	private static <EntClazz> Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>> getCellFactoryForFxLabelStatus(FxTableCol2 fxTableCol) {

		// callback P : entity , Field Tür : Object : Herhangi bir tipde hücre alanı olabilir
		Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>> cellFactory =
				new Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>>() {
					@Override
					public TableCell call(final TableColumn<EntClazz, Object> param) {

						final TableCell<EntClazz, Object> cell = new TableCell<EntClazz, Object>() {

							// table cell objesi

							final FxLabel nodeComp = new FxLabel(""); // fxTableCol.getFiCol().getColEditorNodeText()

							@Override
							public void updateItem(Object item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else { // empty false ise -- boş degilse

									String itemValue = "0";
									if (item != null) itemValue = item.toString();

									EntClazz entity = getTableView().getItems().get(getIndex());

									if (fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue() != null) {
										fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue().accept(entity, nodeComp);
									}

									//ikonların ayarlanması
									if (itemValue.equals("0")) {
										nodeComp.setFxIcon(Icons525.CIRCLE);
									} else if (itemValue.equals("1")) {
										nodeComp.setFxIcon(Icons525.OK);
									} else if (itemValue.equals("2")) {
										nodeComp.setFxIcon(Icons525.CANCEL);
									} else {
										nodeComp.setText("n/a :" + itemValue);
									}
//								nodeComp.setText("s:"+item.toString());
									setGraphic(nodeComp);
									setText(null);
								}
							}
						};
						return cell;

					}
				};
		return cellFactory;
	}


	private static <EntClazz> Callback<TableColumn<EntClazz, Integer>, TableCell<EntClazz, Integer>> getCellFactoryForFxStateButtonThree(FxTableCol2 fxTableCol) {
		// callback < P,R> parameter and return types
		Callback<TableColumn<EntClazz, Integer>, TableCell<EntClazz, Integer>> cellFactory
				= //
				new Callback<TableColumn<EntClazz, Integer>, TableCell<EntClazz, Integer>>() {
					@Override
					public TableCell call(final TableColumn<EntClazz, Integer> param) {

						final TableCell<EntClazz, Integer> cell = new TableCell<EntClazz, Integer>() {

							// table cell objesi

							final FxStateButtonThree btn = new FxStateButtonThree(fxTableCol.getFiCol().getColEditorNodeText());

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

									if (fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue() != null) {
										fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue().accept(entity, btn);
									}

									btn.setOnAction(event -> {

										//S entity = getTableView().getItems().get(getIndex()); // table cell objesinin gettableview methodu

										//if(fxTableCol.getCellFactoryNodeBiAction()==null)return;

										fxTableCol.getFiCol().getFnEditorSetOnAction().accept(entity, btn);

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
		return cellFactory;
	}

	private static <EntClazz> Callback<TableColumn<EntClazz, Integer>, TableCell<EntClazz, Integer>> getCellFactoryForFxStateButton(FxTableCol2 fxTableCol) {
		// callback < P,R> parameter and return types
		Callback<TableColumn<EntClazz, Integer>, TableCell<EntClazz, Integer>> cellFactory
				= //
				new Callback<TableColumn<EntClazz, Integer>, TableCell<EntClazz, Integer>>() {
					@Override
					public TableCell call(final TableColumn<EntClazz, Integer> param) {

						final TableCell<EntClazz, Integer> cell = new TableCell<EntClazz, Integer>() {

							// table cell objesi

							final FxStateButton btn = new FxStateButton(fxTableCol.getFiCol().getColEditorNodeText());

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

									if (fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue() != null) {
										fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue().accept(entity, btn);
									}

									btn.setOnAction(event -> {

										//S entity = getTableView().getItems().get(getIndex()); // table cell objesinin gettableview methodu

										//if(fxTableCol.getCellFactoryNodeBiAction()==null)return;

										// Action gerçekleştirilir, daha sonra renderer işlemleri yapılır
										fxTableCol.getFiCol().getFnEditorSetOnAction().accept(entity, btn);

										if (fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue() != null)
											fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue().accept(entity, btn);

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
		return cellFactory;
	}

	private static <EntClazz> Callback<TableColumn<EntClazz, Boolean>, TableCell<EntClazz, Boolean>> getCellFactoryForToggleButton(FxTableCol2 fxTableCol) {
		// callback < P,R> parameter and return types
		Callback<TableColumn<EntClazz, Boolean>, TableCell<EntClazz, Boolean>> cellFactory
				= //
				new Callback<TableColumn<EntClazz, Boolean>, TableCell<EntClazz, Boolean>>() {
					@Override
					public TableCell call(final TableColumn<EntClazz, Boolean> param) {

						final TableCell<EntClazz, Boolean> cell = new TableCell<EntClazz, Boolean>() {

							// table cell objesi

							final ToggleButton btn = new ToggleButton(fxTableCol.getFiCol().getColEditorNodeText());

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

										fxTableCol.getFiCol().getFnEditorSetOnAction().accept(entity, btn);

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
		return cellFactory;
	}

	private static <EntClazz> Callback<TableColumn<EntClazz, String>, TableCell<EntClazz, String>> getCellFactoryForFxButton(FxTableCol2 fxTableCol) {
		// callback < P,R> parameter and return types
		Callback<TableColumn<EntClazz, String>, TableCell<EntClazz, String>> cellFactory
				= //
				new Callback<TableColumn<EntClazz, String>, TableCell<EntClazz, String>>() {
					@Override
					public TableCell call(final TableColumn<EntClazz, String> param) {

						final TableCell<EntClazz, String> cell = new TableCell<EntClazz, String>() {

							// table cell objesi

							final FxButton btn = new FxButton(fxTableCol.getFiCol().getColEditorNodeText());

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else { // empty false ise
									btn.setOnAction(event -> {
										EntClazz entity = getTableView().getItems().get(getIndex()); // table cell objesinin gettableview methodu
										if (fxTableCol.getFiCol().getFnEditorSetOnActionWithEntity() != null) {
											fxTableCol.getFiCol().getFnEditorSetOnActionWithEntity().accept(entity);
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
		return cellFactory;
	}

	private static <EntClazz> Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>> getCellFactoryForFxButtonV2(FxTableCol2 fxTableCol) {
		// callback < P,R> parameter and return types
		Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>> cellFactory
				= //
				new Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>>() {

					@Override
					public TableCell call(final TableColumn<EntClazz, Object> param) {

						// table cell objesi
						final TableCell<EntClazz, Object> cell = new TableCell<EntClazz, Object>() {

							final FxButton nodeComp = new FxButton(FiString.orEmpty(fxTableCol.getFiCol().getColEditorNodeText()));

							// Item hücrenin içine gelen değer (string,boolean,double vs)
							@Override
							public void updateItem(Object item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else { // empty false ise

									if (fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue() != null) {
										EntClazz entity = getTableView().getItems().get(getIndex());
										fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue().accept(entity, nodeComp);
									}

									if (fxTableCol.getFiCol().getFnEditorNodeRendererWithCol() != null) {
										EntClazz entity = getTableView().getItems().get(getIndex());
										fxTableCol.getFiCol().getFnEditorNodeRendererWithCol().accept(entity, nodeComp, fxTableCol);
									}

									if (fxTableCol.getFiCol().getFnEditorNodeRendererWitValue() != null) {
										EntClazz entity = getTableView().getItems().get(getIndex());
										Object propertyNested = FiReflection.getPropertyNested(entity, fxTableCol.getFiCol().getFieldName());
										fxTableCol.getFiCol().getFnEditorNodeRendererWitValue().accept(entity, nodeComp, propertyNested);
									}


									if (fxTableCol.getFiCol().getFnEditorSetOnAction() != null) {

										nodeComp.setOnAction(event -> {
											EntClazz entity = getTableView().getItems().get(getIndex());

											// Önce action yürütülür , daha sonra renderer işlemi yapılır
											fxTableCol.getFiCol().getFnEditorSetOnAction().accept(entity, nodeComp);


											if (fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue() != null) {
												fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue().accept(entity, nodeComp);
											}


										});
									}

									if (fxTableCol.getFiCol().getFnEditorNodeRendererWithCol() != null) {

										nodeComp.setOnAction(event -> {
											EntClazz entity = getTableView().getItems().get(getIndex());

											// Önce action yürütülür , daha sonra renderer işlemi yapılır
											fxTableCol.getFiCol().getFnEditorSetOnActionWitCol().accept(entity, nodeComp, fxTableCol);


											if (fxTableCol.getFiCol().getFnEditorNodeRendererWithCol() != null) {
												fxTableCol.getFiCol().getFnEditorNodeRendererWithCol().accept(entity, nodeComp, fxTableCol);
											}

										});
									}

									if (fxTableCol.getFiCol().getFnEditorSetOnActionWitValue() != null) {

										nodeComp.setOnAction(event -> {
											EntClazz entity = getTableView().getItems().get(getIndex());

											// Önce action yürütülür , daha sonra renderer işlemi yapılır
											if (true) {
												Object propertyNested = FiReflection.getPropertyNested(entity, fxTableCol.getFiCol().getFieldName());
												fxTableCol.getFiCol().getFnEditorSetOnActionWitValue().accept(entity, nodeComp, propertyNested);
											}

											if (fxTableCol.getFiCol().getFnEditorNodeRendererWitValue() != null) {
												Object propertyNested = FiReflection.getPropertyNested(entity, fxTableCol.getFiCol().getFieldName());
												fxTableCol.getFiCol().getFnEditorNodeRendererWitValue().accept(entity, nodeComp, propertyNested);
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
		return cellFactory;
	}

	private static <EntClazz> Callback<TableColumn<EntClazz, Object>, TableCell<EntClazz, Object>> getCellFactoryForNodeGeneral(FxTableCol2 fxTableCol) {
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
									if (fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue() != null) {
										fxTableCol.getFiCol().getFnEditorNodeRendererBeforeSettingValue().accept(entity, nodeComp);
									}

									setGraphic(nodeComp);
									setText(null);
								}
							}
						};
						return cell;

					}
				};
		return cellFactory;
	}

	private static Callback<TableColumn, TableCell> getCellFactoryForCheckBoxSimple() {
		return new Callback<TableColumn, TableCell>() {
			@Override
			public TableCell call(TableColumn param) {
				CheckBoxTableCell cell = new CheckBoxTableCell();
				cell.setAlignment(Pos.CENTER);
				return cell;
			}
		};
	}

	private static <EntClazz> Callback<TableColumn<EntClazz, String>, TableCell<EntClazz, String>> getCellFactoryForButtonSimple(FxTableCol2 fxTableCol) {
		// callback < P,R> parameter and return types
		Callback<TableColumn<EntClazz, String>, TableCell<EntClazz, String>> cellFactory
				= //
				new Callback<TableColumn<EntClazz, String>, TableCell<EntClazz, String>>() {
					@Override
					public TableCell call(final TableColumn<EntClazz, String> param) {

						final TableCell<EntClazz, String> cell = new TableCell<EntClazz, String>() {

							// table cell objesi

							final Button btn = new Button(fxTableCol.getFiCol().getColEditorNodeText());

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else { // empty false ise
									btn.setOnAction(event -> {
										EntClazz entity = getTableView().getItems().get(getIndex()); // table cell objesinin gettableview methodu
										if (fxTableCol.getFiCol().getFnEditorSetOnActionWithEntity() != null) {
											fxTableCol.getFiCol().getFnEditorSetOnActionWithEntity().accept(entity);
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
		return cellFactory;
	}

}
