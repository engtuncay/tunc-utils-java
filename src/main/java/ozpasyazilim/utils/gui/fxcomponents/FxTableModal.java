package ozpasyazilim.utils.gui.fxcomponents;

import javafx.geometry.Pos;
import ozpasyazilim.utils.core.*;
import ozpasyazilim.utils.datatypes.FiKeyBean;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.OzColSummaryType;
import ozpasyazilim.utils.table.OzColType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class FxTableModal {

    Integer decimalScale = 2;

    public String calculateSummary() {
        return null;
    }

    public <T> T prepSummaryHelper(List<T> listdata, List<IFiCol> listColumns, Class<T> clazz) {

        T summaryEntity = FiReflection.generateObject(clazz);

        for (IFiCol ozTableCol : listColumns) {

            if (ozTableCol.getSummaryType() != null && ozTableCol.getSummaryType() == OzColSummaryType.HEADINGFORTOTAL) {
                FiReflection.setter(ozTableCol, summaryEntity, "TOPLAM");
            }


            if (ozTableCol != null && ozTableCol.getColType() == OzColType.Double) {

                Class clazzType = FiReflection.getPropertyType(summaryEntity, ozTableCol.getOfcTxFieldName());

                //Loghelperr.getInstance(getClass()).debug(" Type"+ clazzType.getSimpleName());

                //if(clazzType.equals(Double.class)) Loghelperr.getInstance(getClass()).debug(" Double class"+ ozTableCol.getFieldName());

                if (ozTableCol.getSummaryType() == OzColSummaryType.SUM && clazzType.equals(Double.class)) {

                    //Loghelperr.getInstance(getClass()).debug(" Double type Field: "+ ozTableCol.getFieldName());

                    Double sumDouble = FiNumberToText.sumValuesDouble(listdata, ent -> {
                        Object objectByField = FiReflection.getPropertyy(ent, ozTableCol.getOfcTxFieldName());
                        if (objectByField == null) return 0d;
                        return (Double) objectByField;
                    });

                    //Loghelperr.getInstance(getClass()).debug(" Toplam:"+ sumDouble.toString());

                    FiReflection.setter(summaryEntity, ozTableCol, sumDouble);

                }

                if (ozTableCol.getSummaryType() == OzColSummaryType.AVG && clazzType.equals(Double.class)) {

                    //Loghelperr.getInstance(getClass()).debug(" Double type Field: "+ ozTableCol.getFieldName());

                    Double sumDouble = FiNumberToText.avgDoubleValues(listdata, ent -> {
                        Object objectByField = FiReflection.getPropertyy(ent, ozTableCol.getOfcTxFieldName());
                        if (objectByField == null) return 0d;
                        return (Double) objectByField;
                    });

                    //Loghelperr.getInstance(getClass()).debug(" Toplam:"+ sumDouble.toString());

                    FiReflection.setter(summaryEntity, ozTableCol, sumDouble);

                }


            }

            if (ozTableCol.getSummaryType() != null && ozTableCol.getSummaryType() == OzColSummaryType.CUSTOM) {

                //Loghelperr.getInstance(getClass()).debug(" Double type Field: "+ ozTableCol.getFieldName());

                if (ozTableCol.getSummaryCalculateFn() != null) {

                    Object summaryvalue = ozTableCol.getSummaryCalculateFn().apply(listdata);

                    //Loghelperr.getInstance(getClass()).debug(" Toplam:"+ sumDouble.toString());

                    FiReflection.setter(summaryEntity, ozTableCol, summaryvalue);


                }

            }
        }

        return summaryEntity;
    }

    public static <T> Object calcSummaryValue(List<T> listdata, IFiCol fiCol, FiReportConfig fiReportConfig, Boolean boFkbEnabled) {

        if (FiCollection.isEmpty(listdata)) {
            if (fiCol != null && fiCol.getColType() == OzColType.Double) {
                if (fiCol.getSummaryType() == OzColSummaryType.SUM) {
                    return "0.00";
                }
            }
            return "";
        }

        T exampleEntity = listdata.get(0); //new FiProperty().generateObject(clazz);

        if (fiCol.getSummaryType() != null && fiCol.getSummaryType() == OzColSummaryType.HEADINGFORTOTAL) {
            //new FiProperty().setter(fiCol, exampleEntity, "TOPLAM");
        }

        if (!FiBool.isTrue(boFkbEnabled)) {

            if (fiCol != null && fiCol.getColType() == OzColType.Double) {

                // 06.08.2019 Not: yanlış field name verilirse null dönüş yapılıyor
                //Class clazzType = new FiProperty().getPropertyType(exampleEntity, fiCol.getFieldName());
                // 07.08.2019
                Class clazzType = FiReflection.getFieldClassType(exampleEntity.getClass(), fiCol.getOfcTxFieldName());

//			if(clazzType==null) Loghelper.getInstance(FxTableModal.class).debug(String.format("Col %s : ClazzType Null : %s",fiCol.getFiHeader(),fiCol.getFieldName()));
//			Loghelperr.getInstance(getClass()).debug(String.format("Col Summary For: %s -- Sum Type %s -- Class Type %s ", fiCol.getFiHeader(),fiCol.getSummaryType(),clazzType.getSimpleName()));

                if (fiCol.getSummaryType() == OzColSummaryType.SUM && clazzType != null && clazzType.equals(Double.class)) {

                    Double sumDouble = FiNumberToText.sumValuesDouble(listdata, ent -> {
                        Object objectByField = FiReflection.getPropertyNested(ent, fiCol.getOfcTxFieldName());
                        if (objectByField == null) return 0d;
                        return (Double) objectByField;
                    });

                    sumDouble = FiNumber.truncateByHalfUp(sumDouble, fiReportConfig.getLnDecimalScale());

                    //Loghelperr.getInstance(getClass()).debug(" Toplam:"+ sumDouble.toString());
                    //new FiProperty().setter(exampleEntity, fiCol, sumDouble);

                    return sumDouble;

                }

                if (fiCol.getSummaryType() == OzColSummaryType.SUM && clazzType != null && clazzType.equals(BigDecimal.class)) {

                    BigDecimal sumDouble = FiNumberToText.sumValuesBigDecimal(listdata, ent -> {
                        Object objectByField = FiReflection.getPropertyNested(ent, fiCol.getOfcTxFieldName());
                        if (objectByField == null) return BigDecimal.ZERO;
                        return (BigDecimal) objectByField;
                    });

                    sumDouble = sumDouble.setScale(fiReportConfig.getLnDecimalScale(), RoundingMode.HALF_UP);  //new FiNumber().truncateByHalfUp(sumDouble,getDecimalScale());

                    //Loghelperr.getInstance(getClass()).debug(" Toplam:"+ sumDouble.toString());
                    //new FiProperty().setter(exampleEntity, fiCol, sumDouble);

                    return sumDouble;

                }

//			Loghelper.get(FxTableModal.class).debug("FitableCol:"+fiCol.getFiHeader());
//			Loghelper.get(FxTableModal.class).debug("FitableCol Null:"+fiCol==null);
//			Loghelper.get(FxTableModal.class).debug("FitableCol.getSumType Null:"+fiCol.getSummaryType()==null);

                if (fiCol.getSummaryType() == OzColSummaryType.AVG && clazzType != null && clazzType.equals(Double.class)) {

                    //Loghelperr.getInstance(getClass()).debug(" Double type Field: "+ fiCol.getFieldName());

                    Double sumDouble = FiNumberToText.avgDoubleValues(listdata, ent -> {
                        Object objectByField = new FiReflection().getPropertyNested(ent, fiCol.getOfcTxFieldName());
                        if (objectByField == null) return 0d;
                        return (Double) objectByField;
                    });

                    sumDouble = new FiNumber().truncateByHalfUp(sumDouble, fiReportConfig.getLnDecimalScale());

                    //Loghelperr.getInstance(getClass()).debug(" Toplam:"+ sumDouble.toString());

                    //new FiProperty().setter(exampleEntity, fiCol, sumDouble);
                    return sumDouble;

                }


            }

            if (fiCol.getSummaryType() != null && fiCol.getSummaryType() == OzColSummaryType.CUSTOM) {

                //Loghelperr.getInstance(getClass()).debug(" Double type Field: "+ fiCol.getFieldName());

                if (fiCol.getSummaryCalculateFn() != null) {

                    Object summaryvalue = fiCol.getSummaryCalculateFn().apply(listdata);

                    //Loghelperr.getInstance(getClass()).debug(" Toplam:"+ sumDouble.toString());

                    //new FiProperty().setter(exampleEntity, fiCol, summaryvalue);
                    return summaryvalue;

                }

            }

        } else { // fkbEnabled

            if (fiCol.getColType() == OzColType.Double) {

                if (fiCol.getSummaryType() == OzColSummaryType.SUM) {

                    Double sumDouble = FiNumberToText.sumValuesDouble(listdata, ent -> {
                        if (ent instanceof FiKeyBean) {
                            return ((FiKeyBean) ent).getAsDoubleOrZero(fiCol);
                        }
                        return 0d;
                    });

                    sumDouble = FiNumber.truncateByHalfUp(sumDouble, fiReportConfig.getLnDecimalScale());

                    //Loghelperr.getInstance(getClass()).debug(" Toplam:"+ sumDouble.toString());
                    //new FiProperty().setter(exampleEntity, fiCol, sumDouble);

                    return sumDouble;

                }

//				if (fiCol.getSummaryType() == OzColSummaryType.AVG && clazzType!=null && clazzType.equals(Double.class)) {
//
//					//Loghelperr.getInstance(getClass()).debug(" Double type Field: "+ fiCol.getFieldName());
//
//					Double sumDouble = FiNumberToText.avgDoubleValues(listdata, ent -> {
//						Object objectByField = new FiReflection().getPropertyNested(ent, fiCol.getOfcTxFieldName());
//						if (objectByField == null) return 0d;
//						return (Double) objectByField;
//					});
//
//					sumDouble = new FiNumber().truncateByHalfUp(sumDouble, fiReportConfig.getLnDecimalScale());
//
//					//Loghelperr.getInstance(getClass()).debug(" Toplam:"+ sumDouble.toString());
//
//					//new FiProperty().setter(exampleEntity, fiCol, sumDouble);
//					return sumDouble;
//
//				}


            }

            if (fiCol.getSummaryType() != null && fiCol.getSummaryType() == OzColSummaryType.CUSTOM) {

                //Loghelperr.getInstance(getClass()).debug(" Double type Field: "+ fiCol.getFieldName());

                if (fiCol.getSummaryCalculateFn() != null) {

                    Object summaryvalue = fiCol.getSummaryCalculateFn().apply(listdata);

                    //Loghelperr.getInstance(getClass()).debug(" Toplam:"+ sumDouble.toString());

                    //new FiProperty().setter(exampleEntity, fiCol, summaryvalue);
                    return summaryvalue;

                }

            }
        }

        return null;
    }

    public static <T> Object calcSummaryValueFi(List<T> listdata, FiCol fiTableCol, FiReportConfig fiReportConfig) {

        if (listdata == null || listdata.size() == 0) {

            if (fiTableCol != null && fiTableCol.getColType() == OzColType.Double) {

                if (fiTableCol.getSummaryType() == OzColSummaryType.SUM) {
                    return "0.00";
                }

            }
            return "";
        }

        T exampleEntity = listdata.get(0); //new FiProperty().generateObject(clazz);

        if (fiTableCol.getSummaryType() != null && fiTableCol.getSummaryType() == OzColSummaryType.HEADINGFORTOTAL) {
            //new FiProperty().setter(fiTableCol, exampleEntity, "TOPLAM");
        }


        if (fiTableCol != null && (fiTableCol.getColType() == OzColType.Double || fiTableCol.getColType() == OzColType.BigDecimal)) {

            // 06.08.2019 Not: yanlış field name verilirse null dönüş yapılıyor
            //Class clazzType = new FiProperty().getPropertyType(exampleEntity, fiTableCol.getFieldName());
            // 07.08.2019
            Class clazzType = FiReflection.getFieldClassType(exampleEntity.getClass(), fiTableCol.getOfcTxFieldName());

            //if(clazzType==null) Loghelperr.getInstance(getClass()).debug("ClazzType Null");

//			Loghelperr.getInstance(getClass()).debug(String.format("Col Summary For: %s -- Sum Type %s -- Class Type %s "
//					, fiTableCol.getFiHeader(),fiTableCol.getSummaryType(),clazzType.getSimpleName()));


            if (fiTableCol.getSummaryType() == OzColSummaryType.SUM && clazzType.equals(Double.class)) { //.equals(Double.class

                Double sumDouble = FiNumberToText.sumValuesDouble(listdata, ent -> {
                    Object objectByField = new FiReflection().getPropertyNested(ent, fiTableCol.getOfcTxFieldName());
                    if (objectByField == null) return 0d;
                    return (Double) objectByField;
                });

                sumDouble = new FiNumber().truncateByHalfUp(sumDouble, fiReportConfig.getLnDecimalScale());

                //Loghelperr.getInstance(getClass()).debug(" Toplam:"+ sumDouble.toString());
                //new FiProperty().setter(exampleEntity, fiTableCol, sumDouble);

                return sumDouble;

            }

            if (fiTableCol.getSummaryType() == OzColSummaryType.SumOutOfNegatives && clazzType.equals(Double.class)) { //.equals(Double.class

                Double sumDouble = FiNumberToText.sumValuesDouble(listdata, ent -> {
                    Object objectByField = new FiReflection().getPropertyNested(ent, fiTableCol.getOfcTxFieldName());
                    if (objectByField == null) return 0d;
                    if ((Double) objectByField < 0d) return 0d;
                    return (Double) objectByField;
                });

                sumDouble = new FiNumber().truncateByHalfUp(sumDouble, fiReportConfig.getLnDecimalScale());

                //Loghelperr.getInstance(getClass()).debug(" Toplam:"+ sumDouble.toString());
                //new FiProperty().setter(exampleEntity, fiTableCol, sumDouble);

                return sumDouble;

            }

            if (fiTableCol.getSummaryType() == OzColSummaryType.SUM && clazzType.equals(BigDecimal.class)) { //.equals(Double.class

                BigDecimal sumDouble = FiNumberToText.sumValuesBigDecimal(listdata, ent -> {
                    Object objectByField = new FiReflection().getPropertyNested(ent, fiTableCol.getOfcTxFieldName());
                    if (objectByField == null) return BigDecimal.ZERO;
                    return (BigDecimal) objectByField;
                });

                sumDouble = sumDouble.setScale(fiReportConfig.getLnDecimalScale(), RoundingMode.HALF_UP);
                //new FiNumber().truncateByHalfUp(sumDouble,getDecimalScale());

                //Loghelperr.getInstance(getClass()).debug(" Toplam:"+ sumDouble.toString());
                //new FiProperty().setter(exampleEntity, fiTableCol, sumDouble);

                return sumDouble;

            }

            if (fiTableCol.getSummaryType() == OzColSummaryType.AVG && clazzType.equals(Double.class)) {

                //Loghelperr.getInstance(getClass()).debug(" Double type Field: "+ fiTableCol.getFieldName());

                Double sumDouble = FiNumberToText.avgDoubleValues(listdata, ent -> {
                    Object objectByField = new FiReflection().getPropertyNested(ent, fiTableCol.getOfcTxFieldName());
                    if (objectByField == null) return 0d;
                    return (Double) objectByField;
                });

                sumDouble = new FiNumber().truncateByHalfUp(sumDouble, fiReportConfig.getLnDecimalScale());

                //Loghelperr.getInstance(getClass()).debug(" Toplam:"+ sumDouble.toString());

                //new FiProperty().setter(exampleEntity, fiTableCol, sumDouble);
                return sumDouble;

            }


        }

        if (fiTableCol.getSummaryType() != null && fiTableCol.getSummaryType() == OzColSummaryType.CUSTOM) {

            //Loghelperr.getInstance(getClass()).debug(" Double type Field: "+ fiTableCol.getFieldName());

            if (fiTableCol.getSummaryCalculateFn() != null) {

                Object summaryvalue = fiTableCol.getSummaryCalculateFn().apply(listdata);

                //Loghelperr.getInstance(getClass()).debug(" Toplam:"+ sumDouble.toString());

                //new FiProperty().setter(exampleEntity, fiTableCol, summaryvalue);
                return summaryvalue;

            }

        }

        return null;
    }


    public Integer getDecimalScale() {
        return decimalScale;
    }

    public void setDecimalScale(Integer decimalScale) {
        this.decimalScale = decimalScale;
    }

    public FxTableModal buildDecimalScale(Integer decimalScale) {
        this.decimalScale = decimalScale;
        return this;
    }

    public void styleSummaryLabel(FxLabel lblSummary, FxTableColDep fxTableColDep) {

        if (fxTableColDep.getColType() == OzColType.Double || fxTableColDep.getColType() == OzColType.Integer) {
            lblSummary.setAlignment(Pos.CENTER_RIGHT);
            lblSummary.getStyleClass().add("summaryDouble");

            Double width = lblSummary.getText().length() * 7.3d;
            //Loghelperr.getInstance(getClass()).debug("lbl l:"+ lblSummary.getText().length());
            //Loghelperr.getInstance(getClass()).debug("col w:"+ fxTableCol.getWidth());
            //Loghelperr.getInstance(getClass()).debug("oran:"+ fxTableCol.getWidth()/lblSummary.getText().length());
            if (fxTableColDep.getWidth() < width) {
                fxTableColDep.setPrefWidth(width);
            }

        }


    }

    public void styleSummaryLabel(FxLabel lblSummary, FxTableCol2 fxTableCol) {

        if (fxTableCol.getRefFiCol().getColType() == OzColType.Double || fxTableCol.getRefFiCol().getColType() == OzColType.Integer) {
            lblSummary.setAlignment(Pos.CENTER_RIGHT);
            lblSummary.getStyleClass().add("summaryDouble");

            Double width = lblSummary.getText().length() * 7.3d;
            //Loghelperr.getInstance(getClass()).debug("lbl l:"+ lblSummary.getText().length());
            //Loghelperr.getInstance(getClass()).debug("col w:"+ fxTableCol.getWidth());
            //Loghelperr.getInstance(getClass()).debug("oran:"+ fxTableCol.getWidth()/lblSummary.getText().length());
            if (fxTableCol.getWidth() < width) {
                fxTableCol.setPrefWidth(width);
            }

        }


    }
}

