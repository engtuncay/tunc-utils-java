package ozpasyazilim.utils.core;

import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import javafx.scene.control.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import ozpasyazilim.utils.gui.fxcomponents.*;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.table.TableStyleConst;
import ozpasyazilim.utils.windows.FiWinUtils;

public class FiExcel2 {

    //private static Class getClass = FiExcel2.class;

    public static void cellTasks1(Cell cella, Integer row, Integer col) {

		/*if(colsizes.containsKey(col)){

		}*/


    }

    public static void setMerge(Sheet sheet, int numRow, int untilRow, int numCol, int untilCol, boolean border, Workbook wb) {
        CellRangeAddress cellMerge = new CellRangeAddress(numRow, untilRow, numCol, untilCol);
        sheet.addMergedRegion(cellMerge);
        if (border) {
            setRegionBorderWithMedium(cellMerge, sheet);
        }

    }

    public static void setRegionBorderWithMedium(CellRangeAddress region, Sheet sheet) {
        Workbook wb = sheet.getWorkbook();
        RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, region, sheet, wb);
        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, region, sheet, wb);
        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, region, sheet, wb);
        RegionUtil.setBorderTop(CellStyle.BORDER_THIN, region, sheet, wb);

    }

    public static void setRegionBorderWithMedium2(CellStyle cellStyle) {
        cellStyle.setBorderBottom((short) 1);
        cellStyle.setBorderLeft((short) 1);
        cellStyle.setBorderRight((short) 1);
        cellStyle.setBorderTop((short) 1);
    }

    //When you want to add Border in Excel, then
    //String cellAddr="$A$11:$A$17";
    //setRegionBorderWithMedium(CellRangeAddress.valueOf(cellAddr1), sheet);

    public static char converIndexToLetter(int i) {
        return (char) (i + 64);
    }

    public static CellRangeAddress getCellRangeAdressByIndex(Integer row1, Integer col1, Integer row2, Integer col2) {

        String cellRef1 = "$" + String.valueOf(converIndexToLetter(col1)) + (row1);      //"$A$11:$A$17";
        String cellRef2 = "$" + String.valueOf(converIndexToLetter(col2)) + (row2);      //"$A$11:$A$17";

        String fullRange = cellRef1 + ":" + cellRef2;

        //System.out.println(fullRange);

        return CellRangeAddress.valueOf(fullRange);
    }

    public static CellRangeAddress getCellRangeAdressByIndex(Integer row1, Integer col1) {

        String cellRef1 = "$" + String.valueOf(converIndexToLetter(col1)) + (row1);      //"$A$11:$A$17";
        String fullRange = cellRef1 + ":" + cellRef1;

        //System.out.println(fullRange);

        return CellRangeAddress.valueOf(fullRange);
    }

    public static FiExcel2 genInstance() {
        return new FiExcel2();
    }

    public static FiExcel2 build() {
        return new FiExcel2();
    }

    public void writeFxTableViewToExcelWithHeader(FxTableView table, Path path, List<ExcelCell> listHeaders, Object footer) {

        //if (colsizes == null) colsizes = new HashMap<>();

        new WorkbookFactory();
        Workbook wb = new XSSFWorkbook(); // Excell workbook
        Sheet sheet = wb.createSheet(); // WorkSheet

        Font fontGlobal = wb.getFontAt((short) 0);
        Short punto = 12;
        fontGlobal.setFontHeightInPoints(punto);
        //font.setFontName("Courier New");
        //((XSSFFont)font).setFamily(3);
        //((XSSFFont)font).setScheme(FontScheme.NONE);
        //	font.setItalic(true);
        //font.setBold(true);

        sheet.setFitToPage(true);
        PrintSetup ps = sheet.getPrintSetup();
        ps.setFitWidth((short) 1);
        ps.setFitHeight((short) 0);

        Integer excelRowIndex = 0;

        //TableModel model = table.getModel(); // Table model

        Integer headerInfoRowIndexStart = -1;
        if (listHeaders != null && listHeaders.size() > 0) {

            headerInfoRowIndexStart = excelRowIndex;

            for (int i = 0; i < listHeaders.size(); i++, excelRowIndex++) {
                Row headerInfoRow = sheet.createRow(excelRowIndex); // Create row
                headerInfoRow.createCell(0).setCellValue(""); //
            }
        }

        Integer colSizeTable = table.getColumns().size();

        // Header Row

        //String cellAddr="$A$11:$A$17";
        //setRegionBorderWithMedium(CellRangeAddress.valueOf(cellAddr1), sheet);


        Row headerRow = sheet.createRow(excelRowIndex); // Create row at line 0
        for (int indexCol = 0; indexCol < colSizeTable; indexCol++) { // For each column

            String txHeader = "";

            FxTableCol tblcol = (FxTableCol) table.getColumns().get(indexCol);

            Object nodeHeader = tblcol.getGraphic();

            //Loghelperr.getInstance(FiExcel.class).debug("Class:"+ tblcol.getGraphic().getClass().getSimpleName());

            if (nodeHeader instanceof FxVBox) {
                FxVBox vBox = (FxVBox) nodeHeader;
                //Loghelperr.getInstance(FiExcel.class).debug("fxbox");
                if (vBox.getChildren().get(0) != null) {
                    FxLabel fxComp = (FxLabel) vBox.getChildren().get(0);
                    txHeader = fxComp.getText();
                }
            } else {
                txHeader = tblcol.getText();
            }

            Cell cell = headerRow.createCell(indexCol);
            cell.setCellValue(txHeader);
            setRegionBorderWithMedium(getCellRangeAdressByIndex(excelRowIndex + 1, indexCol + 1), sheet);

        }
        excelRowIndex++;

        // For each table row
        for (int tableRowIndex = 0; tableRowIndex < table.getItems().size(); tableRowIndex++, excelRowIndex++) {

            Object rowent = table.getItems().get(tableRowIndex);

            printTableRowInExcel(table, wb, sheet, excelRowIndex, colSizeTable, rowent, false);

            // Set the row to the next one in the sequence
            //row = sheet.createRow((tableRowIndex + 2));
        }

        if (footer != null) {
            printTableRowInExcel(table, wb, sheet, excelRowIndex, colSizeTable, footer, false);
            excelRowIndex++;
        }

        for (int indexCol = 0; indexCol < table.getColumns().size(); indexCol++) { // For each column

            FxTableCol tableColumn = (FxTableCol) table.getColumns().get(indexCol);

            String columnId = tableColumn.getId();

            //Loghelperr.getInstance(OzExcel.class).debug(" col id"+ columnId);

            // colsizes.put("musteriunvani", 20*256+5); //  20 karakter için size
            if (tableColumn.getPrintSize() != null) {
                //Loghelperr.getInstance(getClass).debug(" Col Width set edilir:" + (indexCol + 1));
                //Double widthpixelToExcelWidth = tableColumn.getPrintSize()*256+5;
                sheet.setColumnWidth(indexCol, tableColumn.getPrintSize() * 236 + 15); //12 punto için
            } else {
                sheet.autoSizeColumn(indexCol, false);
            }

        }

        //CellStyle styleBoldCenter = wb.createCellStyle();
        //Font font = wb.createFont();
        //font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        //styleBoldCenter.setFont(font);
        //styleBoldCenter.setAlignment( CellStyle.ALIGN_CENTER);

        //CellStyle styleBold = wb.createCellStyle();
        //styleBold.setFont(font);

        if (listHeaders != null && listHeaders.size() > 0) {
            for (int topHeaderIndex = 0; topHeaderIndex < listHeaders.size(); topHeaderIndex++) {

                CellStyle cellStyle = wb.createCellStyle();
                Font font = wb.createFont();

                Row headerInfoRow = sheet.getRow(headerInfoRowIndexStart + topHeaderIndex);
                sheet.addMergedRegion(new CellRangeAddress(headerInfoRowIndexStart + topHeaderIndex, headerInfoRowIndexStart + topHeaderIndex, 0, colSizeTable - 1));
                Cell cellAdded = headerInfoRow.createCell(0);
                ExcelCell excelCell = listHeaders.get(topHeaderIndex);
                cellAdded.setCellValue(excelCell.getValue().toString());

                Map<ExcelCell.CellStyles, String> styleMap = excelCell.getStyleMap();

                if (styleMap.containsKey(ExcelCell.CellStyles.alignment)) {
                    String value = styleMap.get(ExcelCell.CellStyles.alignment);
                    if (value.equals(ExcelCell.StyleAlignments.center.toString())) {
                        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                    }
                }

                if (styleMap.containsKey(ExcelCell.CellStyles.fontSize)) {
                    String value = styleMap.get(ExcelCell.CellStyles.fontSize);
                    if (value.equals(ExcelCell.StyleFontSize.big.toString())) {
                        font.setFontHeight((short) (12 * 20));  //12 puntoya ayarlar
                    }
                }

                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                cellStyle.setFont(font);

                cellAdded.setCellStyle(cellStyle);

                setRegionBorderWithMedium(getCellRangeAdressByIndex(headerInfoRowIndexStart + topHeaderIndex + 1, 1, headerInfoRowIndexStart + topHeaderIndex + 1, colSizeTable), sheet);


            }

        }


        //setRegionBorderWithMedium(getCellRangeAdressByIndex(1,1,excelRowIndex-1,colSizeTable), sheet);


        try {
            Loghelper.get(getClass()).debug(" Path Excel:" + path.toString());
            wb.write(new FileOutputStream(path.toString()));// Save the file
        } catch (IOException e) {
            Loghelper.debugException(FiExcel2.class, e);
            // FIXME burada uyarı gösterilebilr
        }

    }

    public void writeFxTableViewToExcelWithHeader2(FxTableView table, Path path, List<ExcelCell> listHeaders, Object footer, Boolean performanceEnabled) {

        //if (colsizes == null) colsizes = new HashMap<>();
        new WorkbookFactory();
        // excel workbbok
        XSSFWorkbook wb = new XSSFWorkbook();

        XSSFSheet sheet = wb.createSheet(); // WorkSheet

        Font fontGlobal = wb.getFontAt((short) 0);
        Short punto = 12;
        fontGlobal.setFontHeightInPoints(punto);
        //font.setFontName("Courier New");
        //((XSSFFont)font).setFamily(3);
        //((XSSFFont)font).setScheme(FontScheme.NONE);
        //	font.setItalic(true);
        //font.setBold(true);

        sheet.setFitToPage(true);
        PrintSetup ps = sheet.getPrintSetup();
        ps.setFitWidth((short) 1);
        ps.setFitHeight((short) 0);

        Integer excelRowIndex = 0;

        //TableModel model = table.getModel(); // Table model

        Integer headerInfoRowIndexStart = -1;
        if (listHeaders != null && listHeaders.size() > 0) {

            headerInfoRowIndexStart = excelRowIndex;

            for (int i = 0; i < listHeaders.size(); i++, excelRowIndex++) {
                Row headerInfoRow = sheet.createRow(excelRowIndex); // Create row
                headerInfoRow.createCell(0).setCellValue(""); //
            }
        }

        Integer colSizeTable = table.getColumns().size();

        // Header Row

        //String cellAddr="$A$11:$A$17";
        //setRegionBorderWithMedium(CellRangeAddress.valueOf(cellAddr1), sheet);

        int indexCol = -1;
        Row headerRow = sheet.createRow(excelRowIndex); // Create row at line 0
        for (int indexColExcel = 0; indexColExcel < colSizeTable; indexColExcel++) { // For each column

            String txHeader = "";

            FxTableCol tblcol = (FxTableCol) table.getColumns().get(indexColExcel);

            if (FiBool.isTrue(tblcol.getBoIsNotExportedExcel())) continue;
            indexCol++;

            Object nodeHeader = tblcol.getGraphic();

            //Loghelperr.getInstance(FiExcel.class).debug("Class:"+ tblcol.getGraphic().getClass().getSimpleName());

            if (nodeHeader instanceof FxVBox) {
                FxVBox vBox = (FxVBox) nodeHeader;
                //Loghelperr.getInstance(FiExcel.class).debug("fxbox");
                if (vBox.getChildren().get(0) != null) {
                    FxLabel fxComp = (FxLabel) vBox.getChildren().get(0);
                    txHeader = fxComp.getText();
                }
            } else {
                txHeader = tblcol.getText();
            }


            Cell cell = headerRow.createCell(indexCol);
            cell.setCellValue(txHeader);

            CellStyle cellStyle = wb.createCellStyle();
            setRegionBorderWithMedium2(cellStyle);
            cell.setCellStyle(cellStyle);

            //setRegionBorderWithMedium(getCellRangeAdressByIndex(excelRowIndex + 1, indexCol + 1), sheet);

        }
        excelRowIndex++;

        // For each table row
        for (int tableRowIndex = 0; tableRowIndex < table.getItems().size(); tableRowIndex++, excelRowIndex++) {

            Object rowent = table.getItems().get(tableRowIndex);

            printTableRowInExcel(table, wb, sheet, excelRowIndex, colSizeTable, rowent, performanceEnabled);

            // Set the row to the next one in the sequence
            //row = sheet.createRow((tableRowIndex + 2));
        }

        if (footer != null) {
            printTableRowInExcel(table, wb, sheet, excelRowIndex, colSizeTable, footer, performanceEnabled);
            excelRowIndex++;
        }

        indexCol = -1;
        for (int indexColExcel = 0; indexColExcel < table.getColumns().size(); indexColExcel++) { // For each column

            FxTableCol tableColumn = (FxTableCol) table.getColumns().get(indexColExcel);

            if (FiBool.isTrue(tableColumn.getBoIsNotExportedExcel())) continue;
            indexCol++;

            String columnId = tableColumn.getId();

            //Loghelperr.getInstance(OzExcel.class).debug(" col id"+ columnId);

            // colsizes.put("musteriunvani", 20*256+5); //  20 karakter için size
            if (tableColumn.getPrintSize() != null) {
                //Loghelperr.getInstance(getClass).debug(" Col Width set edilir:" + (indexCol + 1));
                //Double widthpixelToExcelWidth = tableColumn.getPrintSize()*256+5;
                sheet.setColumnWidth(indexCol, tableColumn.getPrintSize() * 236 + 15); //12 punto için
            } else {

                //sheet.autoSizeColumn(indexCol, false);
            }

        }

        //CellStyle styleBoldCenter = wb.createCellStyle();
        //Font font = wb.createFont();
        //font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        //styleBoldCenter.setFont(font);
        //styleBoldCenter.setAlignment( CellStyle.ALIGN_CENTER);

        //CellStyle styleBold = wb.createCellStyle();
        //styleBold.setFont(font);

        if (listHeaders != null && listHeaders.size() > 0) {
            for (int topHeaderIndex = 0; topHeaderIndex < listHeaders.size(); topHeaderIndex++) {

                CellStyle cellStyle = wb.createCellStyle();
                Font font = wb.createFont();

                Row headerInfoRow = sheet.getRow(headerInfoRowIndexStart + topHeaderIndex);
                sheet.addMergedRegion(new CellRangeAddress(headerInfoRowIndexStart + topHeaderIndex, headerInfoRowIndexStart + topHeaderIndex, 0, colSizeTable - 1));
                Cell cellAdded = headerInfoRow.createCell(0);
                ExcelCell excelCell = listHeaders.get(topHeaderIndex);
                cellAdded.setCellValue(excelCell.getValue().toString());

                Map<ExcelCell.CellStyles, String> styleMap = excelCell.getStyleMap();

                if (styleMap.containsKey(ExcelCell.CellStyles.alignment)) {
                    String value = styleMap.get(ExcelCell.CellStyles.alignment);
                    if (value.equals(ExcelCell.StyleAlignments.center.toString())) {
                        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                    }
                }

                if (styleMap.containsKey(ExcelCell.CellStyles.fontSize)) {
                    String value = styleMap.get(ExcelCell.CellStyles.fontSize);
                    if (value.equals(ExcelCell.StyleFontSize.big.toString())) {
                        font.setFontHeight((short) (12 * 20));  //12 puntoya ayarlar
                    }
                }

                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                cellStyle.setFont(font);
                //setRegionBorderWithMedium2(cellStyle);
                //cellAdded.setCellStyle(cellStyle);

                //setRegionBorderWithMedium(getCellRangeAdressByIndex(headerInfoRowIndexStart + topHeaderIndex + 1, 1, headerInfoRowIndexStart + topHeaderIndex + 1, colSizeTable), sheet);


            }

        }


        //setRegionBorderWithMedium(getCellRangeAdressByIndex(1,1,excelRowIndex-1,colSizeTable), sheet);


        try {
            Loghelper.get(getClass()).debug(" Path Excel:" + path.toString());
            wb.write(new FileOutputStream(path.toString()));// Save the file
        } catch (IOException e) {
            Loghelper.debugException(FiExcel2.class, e);
            // FIXME burada uyarı gösterilebilr
        }


        //wb.dispose();

    }

    public void writeFxTableViewToExcelWithHeader2(FxTableView2 table, Path path, List<ExcelCell> listHeaders, Object footer, Boolean performanceEnabled) {

        //if (colsizes == null) colsizes = new HashMap<>();
        new WorkbookFactory();
        // excel workbbok
        XSSFWorkbook wb = new XSSFWorkbook();

        XSSFSheet sheet = wb.createSheet(); // WorkSheet

        Font fontGlobal = wb.getFontAt((short) 0);
        Short punto = 12;
        fontGlobal.setFontHeightInPoints(punto);
        //font.setFontName("Courier New");
        //((XSSFFont)font).setFamily(3);
        //((XSSFFont)font).setScheme(FontScheme.NONE);
        //	font.setItalic(true);
        //font.setBold(true);

        sheet.setFitToPage(true);
        PrintSetup ps = sheet.getPrintSetup();
        ps.setFitWidth((short) 1);
        ps.setFitHeight((short) 0);

        Integer excelRowIndex = 0;

        //TableModel model = table.getModel(); // Table model

        Integer headerInfoRowIndexStart = -1;
        if (listHeaders != null && listHeaders.size() > 0) {

            headerInfoRowIndexStart = excelRowIndex;

            for (int i = 0; i < listHeaders.size(); i++, excelRowIndex++) {
                Row headerInfoRow = sheet.createRow(excelRowIndex); // Create row
                headerInfoRow.createCell(0).setCellValue(""); //
            }
        }

        Integer colSizeTable = table.getColumns().size();

        // Header Row

        //String cellAddr="$A$11:$A$17";
        //setRegionBorderWithMedium(CellRangeAddress.valueOf(cellAddr1), sheet);

        int indexCol = -1;
        Row headerRow = sheet.createRow(excelRowIndex); // Create row at line 0
        for (int indexColExcel = 0; indexColExcel < colSizeTable; indexColExcel++) { // For each column

            String txHeader = "";

            FxTableCol2 tblcol = (FxTableCol2) table.getColumns().get(indexColExcel);

            if (FiBool.isTrue(tblcol.getRefFiCol().getBoDontExportExcel())) continue;
            indexCol++;

            Object nodeHeader = tblcol.getGraphic();

            //Loghelperr.getInstance(FiExcel.class).debug("Class:"+ tblcol.getGraphic().getClass().getSimpleName());

            if (nodeHeader instanceof FxVBox) {
                FxVBox vBox = (FxVBox) nodeHeader;
                //Loghelperr.getInstance(FiExcel.class).debug("fxbox");
                if (vBox.getChildren().get(0) != null) {
                    FxLabel fxComp = (FxLabel) vBox.getChildren().get(0);
                    txHeader = fxComp.getText();
                }
            } else {
                txHeader = tblcol.getText();
            }


            Cell cell = headerRow.createCell(indexCol);
            cell.setCellValue(txHeader);

            CellStyle cellStyle = wb.createCellStyle();
            setRegionBorderWithMedium2(cellStyle);
            cell.setCellStyle(cellStyle);

            //setRegionBorderWithMedium(getCellRangeAdressByIndex(excelRowIndex + 1, indexCol + 1), sheet);

        }
        excelRowIndex++;

        // For each table row
        for (int tableRowIndex = 0; tableRowIndex < table.getItems().size(); tableRowIndex++, excelRowIndex++) {

            Object rowent = table.getItems().get(tableRowIndex);

            printTableRowInExcel2(table, wb, sheet, excelRowIndex, colSizeTable, rowent, performanceEnabled);

            // Set the row to the next one in the sequence
            //row = sheet.createRow((tableRowIndex + 2));
        }

        if (footer != null) {
            printTableRowInExcel2(table, wb, sheet, excelRowIndex, colSizeTable, footer, performanceEnabled);
            excelRowIndex++;
        }

        indexCol = -1;
        for (int indexColExcel = 0; indexColExcel < table.getColumns().size(); indexColExcel++) { // For each column

            FxTableCol2 tableColumn = (FxTableCol2) table.getColumns().get(indexColExcel);

            if (FiBool.isTrue(tableColumn.getRefFiCol().getBoDontExportExcel())) continue;
            indexCol++;

            String columnId = tableColumn.getId();

            //Loghelperr.getInstance(OzExcel.class).debug(" col id"+ columnId);

            // colsizes.put("musteriunvani", 20*256+5); //  20 karakter için size
            if (tableColumn.getRefFiCol().getPrintSize() != null) {
                //Loghelperr.getInstance(getClass).debug(" Col Width set edilir:" + (indexCol + 1));
                //Double widthpixelToExcelWidth = tableColumn.getPrintSize()*256+5;
                sheet.setColumnWidth(indexCol, tableColumn.getRefFiCol().getPrintSize() * 236 + 15); //12 punto için
            } else {

                //sheet.autoSizeColumn(indexCol, false);
            }

        }

        //CellStyle styleBoldCenter = wb.createCellStyle();
        //Font font = wb.createFont();
        //font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        //styleBoldCenter.setFont(font);
        //styleBoldCenter.setAlignment( CellStyle.ALIGN_CENTER);

        //CellStyle styleBold = wb.createCellStyle();
        //styleBold.setFont(font);

        if (listHeaders != null && listHeaders.size() > 0) {
            for (int topHeaderIndex = 0; topHeaderIndex < listHeaders.size(); topHeaderIndex++) {

                CellStyle cellStyle = wb.createCellStyle();
                Font font = wb.createFont();

                Row headerInfoRow = sheet.getRow(headerInfoRowIndexStart + topHeaderIndex);
                sheet.addMergedRegion(new CellRangeAddress(headerInfoRowIndexStart + topHeaderIndex, headerInfoRowIndexStart + topHeaderIndex, 0, colSizeTable - 1));
                Cell cellAdded = headerInfoRow.createCell(0);
                ExcelCell excelCell = listHeaders.get(topHeaderIndex);
                cellAdded.setCellValue(excelCell.getValue().toString());

                Map<ExcelCell.CellStyles, String> styleMap = excelCell.getStyleMap();

                if (styleMap.containsKey(ExcelCell.CellStyles.alignment)) {
                    String value = styleMap.get(ExcelCell.CellStyles.alignment);
                    if (value.equals(ExcelCell.StyleAlignments.center.toString())) {
                        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                    }
                }

                if (styleMap.containsKey(ExcelCell.CellStyles.fontSize)) {
                    String value = styleMap.get(ExcelCell.CellStyles.fontSize);
                    if (value.equals(ExcelCell.StyleFontSize.big.toString())) {
                        font.setFontHeight((short) (12 * 20));  //12 puntoya ayarlar
                    }
                }

                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                cellStyle.setFont(font);
                //setRegionBorderWithMedium2(cellStyle);
                //cellAdded.setCellStyle(cellStyle);

                //setRegionBorderWithMedium(getCellRangeAdressByIndex(headerInfoRowIndexStart + topHeaderIndex + 1, 1, headerInfoRowIndexStart + topHeaderIndex + 1, colSizeTable), sheet);


            }

        }


        //setRegionBorderWithMedium(getCellRangeAdressByIndex(1,1,excelRowIndex-1,colSizeTable), sheet);


        try {
            Loghelper.get(getClass()).debug(" Path Excel:" + path.toString());
            wb.write(new FileOutputStream(path.toString()));// Save the file
        } catch (IOException e) {
            Loghelper.debugException(FiExcel2.class, e);
            // FIXME burada uyarı gösterilebilr
        }


        //wb.dispose();

    }

    public void writeFxTreeTableToExcelWithHeader(FxTreeTableView table, Path path, List<ExcelCell> listHeaders, Object footer,
                                                  Map<String, Integer> colsizes) throws FileNotFoundException, IOException {

        //Loghelperr.getInstance(FiExcel.class).debug("TreeTable Excel Write");

        if (colsizes == null) colsizes = new HashMap<>();

        new WorkbookFactory();
        Workbook wb = new XSSFWorkbook(); // Excell workbook
        Sheet sheet = wb.createSheet(); // WorkSheet

        Font fontGlobal = wb.getFontAt((short) 0);
        fontGlobal.setFontHeightInPoints((short) 12);
        //font.setFontName("Courier New");
        //((XSSFFont)font).setFamily(3);
        //((XSSFFont)font).setScheme(FontScheme.NONE);
        //	font.setItalic(true);
        //font.setBold(true);

        Map tablestyleMap = table.getStyleMap();

        sheet.setFitToPage(true);
        PrintSetup ps = sheet.getPrintSetup();
        ps.setFitWidth((short) 1);
        ps.setFitHeight((short) 0);

        Optional optional = FiCollection.mapcheckGet(tablestyleMap, new TableStyleConst().isFitToSingle);

        //Loghelperr.getInstance().debug(" Optional ispresent:"+ optional.isPresent());

        if (optional.isPresent() && (Boolean) optional.get()) {
            ps.setFitHeight((short) 1);
        }

        Integer excelRowIndex = 0;

        //TableModel model = table.getModel(); // Table model

        Integer headerInfoRowIndexStart = -1;
        if (listHeaders != null && listHeaders.size() > 0) {

            headerInfoRowIndexStart = excelRowIndex;

            for (int i = 0; i < listHeaders.size(); i++, excelRowIndex++) {
                Row headerInfoRow = sheet.createRow(excelRowIndex); // Create row
                headerInfoRow.createCell(0).setCellValue(""); //
            }
        }

        Integer colSizeTable = table.getColumns().size();

        // Header Row

        //String cellAddr="$A$11:$A$17";
        //setRegionBorderWithMedium(CellRangeAddress.valueOf(cellAddr1), sheet);


        Row headerRow = sheet.createRow(excelRowIndex); // Create row at line 0
        for (int indexCol = 0; indexCol < colSizeTable; indexCol++) { // For each column
            TreeTableColumn tblcol = (TreeTableColumn) table.getColumns().get(indexCol);
            Cell cell = headerRow.createCell(indexCol);
            cell.setCellValue(tblcol.getText());
            setRegionBorderWithMedium(getCellRangeAdressByIndex(excelRowIndex + 1, indexCol + 1), sheet);
        }
        excelRowIndex++;


        List listItem = new ArrayList();

        //if(table.getRoot().getValue()!=null) listItem.add(table.getRoot().getValue());


        if (table.getRoot().getChildren().size() > 0) {

            addTreeItemRecursive(listItem, table.getRoot());

			/*table.getRoot().getChildren().forEach( itemtree -> {

				TreeItem treeItem = (TreeItem) itemtree;

				listItem.add(treeItem.getValue());

				if(treeItem.getChildren().size()>0){

					treeItem.getChildren().forEach(itemtree);

				}

			});	*/

        }

        //Loghelperr.getInstance(FiExcel.class).debug("ozexcel listsize"+ listItem.size());

        // For each table row
        for (int tableRowIndex = 0; tableRowIndex < listItem.size(); tableRowIndex++, excelRowIndex++) {

            Object rowent = listItem.get(tableRowIndex);

            printTreeTableRowInExcel(table, wb, sheet, excelRowIndex, colSizeTable, rowent);

            // Set the row to the next one in the sequence
            //row = sheet.createRow((tableRowIndex + 2));
        }

        if (footer != null) {
            printTreeTableRowInExcel(table, wb, sheet, excelRowIndex, colSizeTable, footer);
            excelRowIndex++;
        }

        for (int indexCol = 0; indexCol < table.getColumns().size(); indexCol++) { // For each column

            TreeTableColumn tableColumn = (TreeTableColumn) table.getColumns().get(indexCol);

            String columnId = tableColumn.getId();

            //Loghelperr.getInstance(OzExcel.class).debug(" col id"+ columnId);

            if (colsizes.containsKey(columnId)) {
                //Loghelperr.getInstance(getClass).debug(" Col Width set edilir:" + (indexCol + 1));
                sheet.setColumnWidth(indexCol, colsizes.get(columnId));
            } else {
                sheet.autoSizeColumn(indexCol, false);
            }

        }

        //CellStyle styleBoldCenter = wb.createCellStyle();
        //Font font = wb.createFont();
        //font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        //styleBoldCenter.setFont(font);
        //styleBoldCenter.setAlignment( CellStyle.ALIGN_CENTER);

        //CellStyle styleBold = wb.createCellStyle();
        //styleBold.setFont(font);

        if (listHeaders != null && listHeaders.size() > 0) {
            for (int topHeaderIndex = 0; topHeaderIndex < listHeaders.size(); topHeaderIndex++) {

                CellStyle cellStyle = wb.createCellStyle();
                Font font = wb.createFont();

                Row headerInfoRow = sheet.getRow(headerInfoRowIndexStart + topHeaderIndex);
                sheet.addMergedRegion(new CellRangeAddress(headerInfoRowIndexStart + topHeaderIndex, headerInfoRowIndexStart + topHeaderIndex, 0, colSizeTable - 1));
                Cell cellAdded = headerInfoRow.createCell(0);
                ExcelCell excelCell = listHeaders.get(topHeaderIndex);
                cellAdded.setCellValue(excelCell.getValue().toString());

                Map<ExcelCell.CellStyles, String> styleMap = excelCell.getStyleMap();

                if (styleMap.containsKey(ExcelCell.CellStyles.alignment)) {
                    String value = styleMap.get(ExcelCell.CellStyles.alignment);
                    if (value.equals(ExcelCell.StyleAlignments.center.toString())) {
                        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                    }
                }

                if (styleMap.containsKey(ExcelCell.CellStyles.fontSize)) {
                    String value = styleMap.get(ExcelCell.CellStyles.fontSize);
                    if (value.equals(ExcelCell.StyleFontSize.big.toString())) {
                        font.setFontHeight((short) (12 * 20));  //12 puntoya ayarlar
                    }
                }

                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                cellStyle.setFont(font);

                cellAdded.setCellStyle(cellStyle);

                setRegionBorderWithMedium(getCellRangeAdressByIndex(headerInfoRowIndexStart + topHeaderIndex + 1, 1, headerInfoRowIndexStart + topHeaderIndex + 1, colSizeTable), sheet);


            }

        }

        //Loghelperr.getInstance(FiExcel.class).debug("Excel Oluşturuldu.");

        //setRegionBorderWithMedium(getCellRangeAdressByIndex(1,1,excelRowIndex-1,colSizeTable), sheet);


        wb.write(new FileOutputStream(path.toString()));// Save the file
    }

    private static void addTreeItemRecursive(List listItem, TreeItem treeItem) {


        if (treeItem.getValue() != null) {
            //System.out.println("Item Type"+ treeItem.getValue().getClass().getSimpleName());
            listItem.add(treeItem.getValue());
        }

        if (treeItem.getChildren().size() > 0) {
            treeItem.getChildren().forEach(item -> addTreeItemRecursive(listItem, (TreeItem) item));
        }

    }


    private void printTableRowInExcel(TableView table, Workbook wb, Sheet sheet, Integer excelRowIndex, Integer colSizeTable, Object rowent, Boolean performanceEnabled) {

        Row row = sheet.createRow(excelRowIndex); // Row created

        //T rowent = (T) table.getItems().get(tableRowIndex);
        int colIndexContent = -1;
        for (int colIndexExcel = 0; colIndexExcel < colSizeTable; colIndexExcel++) { // For each table column

            FxTableCol tblcol = (FxTableCol) table.getColumns().get(colIndexExcel);
            //ObservableValue obsval = (ObservableValue) tblcol.getCellObservableValue(row).getValue();
            //Object obj = obsval.getValue();
            if (FiBool.isTrue(tblcol.getBoIsNotExportedExcel())) continue;
            colIndexContent++;

            String value = "";

            //Object obj = getValueFunction.apply(colIndexContent,rowent);

            Object obj = null;

            try {
                obj = PropertyUtils.getProperty(rowent, tblcol.getId());
            } catch (IllegalAccessException e) {
                //e.printStackTrace();
                FiException.exTosMain(e);
            } catch (InvocationTargetException e) {
                //e.printStackTrace();
                FiException.exTosMain(e);
            } catch (NoSuchMethodException e) {
                //e.printStackTrace();
                Loghelper.get(FiExcel2.class).debug(" Metod Bulunamdı:" + tblcol.getId());
                FiException.exTosMain(e);
            }

            if (obj != null) {

                if (obj instanceof Float) {

                    Cell cella = row.createCell(colIndexContent);
                    CellStyle cellStyle = getMapCellStylesXSSF(wb).get(OzColType.Double.toString());
                    cella.setCellStyle(cellStyle);
                    cella.setCellValue(((Float) obj).doubleValue());
                    continue;
                }

                if (obj instanceof Double) {

                    Cell cell = row.createCell(colIndexContent);
                    Double dblCellValue = (Double) obj;
                    //dblCellValue = new FiNumber().truncateByHalfUp(dblCellValue, 3);
                    CellStyle cellStyle = getMapCellStylesXSSF(wb).get(OzColType.Double.toString());
                    cell.setCellValue(dblCellValue);
                    cell.setCellStyle(cellStyle);
                    //setRegionBorderWithMedium(getCellRangeAdressByIndex(excelRowIndex + 1, colIndexContent + 1), sheet);
                    continue;
                }

                if (obj instanceof Date) {

                    Cell cella = row.createCell(colIndexContent);
                    CellStyle cellStyle = getMapCellStylesXSSF(wb).get(OzColType.Date.toString());
                    cella.setCellStyle(cellStyle);
                    cella.setCellValue((Date) obj);
                    cella.setCellStyle(cellStyle);
                    continue;
                }
                value = obj.toString();
                Cell cellGeneric = row.createCell(colIndexContent);
                // Write value
                CellStyle cellStyle = getMapCellStylesXSSF(wb).get(OzColType.String.toString());
                cellGeneric.setCellStyle(cellStyle);
                cellGeneric.setCellValue(value);

            } else {

                Cell cellGeneric = row.createCell(colIndexContent);
                // Write value
                CellStyle cellStyle = getMapCellStylesXSSF(wb).get(OzColType.String.toString());
                cellGeneric.setCellStyle(cellStyle);
                cellGeneric.setCellType(Cell.CELL_TYPE_BLANK);

            }

        }
    }

    private void printTableRowInExcel2(TableView table, Workbook wb, Sheet sheet, Integer excelRowIndex, Integer colSizeTable, Object rowent, Boolean performanceEnabled) {

        Row row = sheet.createRow(excelRowIndex); // Row created

        //T rowent = (T) table.getItems().get(tableRowIndex);
        int colIndexContent = -1;
        for (int colIndexExcel = 0; colIndexExcel < colSizeTable; colIndexExcel++) { // For each table column

            FxTableCol2 tblcol = (FxTableCol2) table.getColumns().get(colIndexExcel);
            //ObservableValue obsval = (ObservableValue) tblcol.getCellObservableValue(row).getValue();
            //Object obj = obsval.getValue();
            if (FiBool.isTrue(tblcol.getRefFiCol().getBoDontExportExcel())) continue;
            colIndexContent++;

            String value = "";

            //Object obj = getValueFunction.apply(colIndexContent,rowent);

            Object obj = null;

            try {
                obj = PropertyUtils.getProperty(rowent, tblcol.getId());
            } catch (IllegalAccessException e) {
                //e.printStackTrace();
                FiException.exTosMain(e);
            } catch (InvocationTargetException e) {
                //e.printStackTrace();
                FiException.exTosMain(e);
            } catch (NoSuchMethodException e) {
                //e.printStackTrace();
                Loghelper.get(FiExcel2.class).debug(" Metod Bulunamdı:" + tblcol.getId());
                FiException.exTosMain(e);
            }

            if (obj != null) {

                if (obj instanceof Float) {

                    Cell cella = row.createCell(colIndexContent);
                    CellStyle cellStyle = getMapCellStylesXSSF(wb).get(OzColType.Double.toString());
                    cella.setCellStyle(cellStyle);
                    cella.setCellValue(((Float) obj).doubleValue());
                    continue;
                }

                if (obj instanceof Double) {

                    Cell cell = row.createCell(colIndexContent);
                    Double dblCellValue = (Double) obj;
                    //dblCellValue = new FiNumber().truncateByHalfUp(dblCellValue, 3);
                    CellStyle cellStyle = getMapCellStylesXSSF(wb).get(OzColType.Double.toString());
                    cell.setCellValue(dblCellValue);
                    cell.setCellStyle(cellStyle);
                    //setRegionBorderWithMedium(getCellRangeAdressByIndex(excelRowIndex + 1, colIndexContent + 1), sheet);
                    continue;
                }

                if (obj instanceof Date) {

                    Cell cella = row.createCell(colIndexContent);
                    CellStyle cellStyle = getMapCellStylesXSSF(wb).get(OzColType.Date.toString());
                    cella.setCellStyle(cellStyle);
                    cella.setCellValue((Date) obj);
                    cella.setCellStyle(cellStyle);
                    continue;
                }
                value = obj.toString();
                Cell cellGeneric = row.createCell(colIndexContent);
                // Write value
                CellStyle cellStyle = getMapCellStylesXSSF(wb).get(OzColType.String.toString());
                cellGeneric.setCellStyle(cellStyle);
                cellGeneric.setCellValue(value);

            } else {

                Cell cellGeneric = row.createCell(colIndexContent);
                // Write value
                CellStyle cellStyle = getMapCellStylesXSSF(wb).get(OzColType.String.toString());
                cellGeneric.setCellStyle(cellStyle);
                cellGeneric.setCellType(Cell.CELL_TYPE_BLANK);

            }

        }
    }

    Map<String, CellStyle> mapCellStylesXSSF;

    public Map<String, CellStyle> getMapCellStylesXSSF(Workbook wb) {

        if (mapCellStylesXSSF == null) {

            Map<String, CellStyle> mapStyles = new HashMap<>();

            CellStyle cellDateStyle = wb.createCellStyle();
            CreationHelper createHelper = wb.getCreationHelper();
            cellDateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yy"));
            setRegionBorderWithMedium2(cellDateStyle);
            mapStyles.put(OzColType.Date.toString(), cellDateStyle);

            CellStyle cellDoubleStyle = wb.createCellStyle();
            DataFormat format = wb.createDataFormat();
            cellDoubleStyle.setDataFormat(format.getFormat("#,##0.00"));
            setRegionBorderWithMedium2(cellDoubleStyle);
            mapStyles.put(OzColType.Double.toString(), cellDoubleStyle);

            CellStyle cellStringStyle = wb.createCellStyle();
            setRegionBorderWithMedium2(cellStringStyle);
            mapStyles.put(OzColType.String.toString(), cellStringStyle);

            this.mapCellStylesXSSF = mapStyles;
        }

        return mapCellStylesXSSF;

    }

    private static void printTreeTableRowInExcel(TreeTableView table, Workbook wb, Sheet sheet, Integer excelRowIndex, Integer colSizeTable, Object rowent) {

        Row row = sheet.createRow(excelRowIndex); // Row created

        //T rowent = (T) table.getItems().get(tableRowIndex);

        for (int colIndexContent = 0; colIndexContent < colSizeTable; colIndexContent++) { // For each table column

            TreeTableColumn tblcol = (TreeTableColumn) table.getColumns().get(colIndexContent);
            //ObservableValue obsval = (ObservableValue) tblcol.getCellObservableValue(row).getValue();
            //Object obj = obsval.getValue();

            String value = "";

            //Object obj = getValueFunction.apply(colIndexContent,rowent);

            Object obj = null;

            try {
                obj = PropertyUtils.getProperty(rowent, tblcol.getId());
            } catch (IllegalAccessException e) {
                //e.printStackTrace();
                FiException.exTosMain(e);
            } catch (InvocationTargetException e) {
                //e.printStackTrace();
                FiException.exTosMain(e);
            } catch (NoSuchMethodException e) {
                //e.printStackTrace();
                Loghelper.get(FiExcel2.class).debug(" Metod Bulunamdı:" + tblcol.getId());
                FiException.exTosMain(e);
            }

            if (obj != null) {

                if (obj instanceof Float) {
                    CellStyle cellStyle = wb.createCellStyle();
                    DataFormat format = wb.createDataFormat();
                    Cell cella = row.createCell(colIndexContent);
                    cellStyle.setDataFormat(format.getFormat("#,##0.00"));
                    cella.setCellValue(((Float) obj).doubleValue());
                    cella.setCellStyle(cellStyle);
                    setRegionBorderWithMedium(getCellRangeAdressByIndex(excelRowIndex + 1, colIndexContent + 1), sheet);
                    continue;
                }

                if (obj instanceof Double) {
                    CellStyle cellStyle = wb.createCellStyle();
                    DataFormat format = wb.createDataFormat();
                    Cell cella = row.createCell(colIndexContent);
                    cellStyle.setDataFormat(format.getFormat("#,##0.00"));
                    cella.setCellValue(((Double) obj));
                    cella.setCellStyle(cellStyle);
                    setRegionBorderWithMedium(getCellRangeAdressByIndex(excelRowIndex + 1, colIndexContent + 1), sheet);
                    continue;
                }

                if (obj instanceof Date) {

                    CreationHelper createHelper = wb.getCreationHelper();
                    CellStyle cellStyle = wb.createCellStyle();
                    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yy"));
                    Cell cella = row.createCell(colIndexContent);
                    cella.setCellValue((Date) obj);
                    cella.setCellStyle(cellStyle);
                    setRegionBorderWithMedium(getCellRangeAdressByIndex(excelRowIndex + 1, colIndexContent + 1), sheet);

                    continue;
                }
                value = obj.toString();
                row.createCell(colIndexContent).setCellValue(value); // Write value
                setRegionBorderWithMedium(getCellRangeAdressByIndex(excelRowIndex + 1, colIndexContent + 1), sheet);

            } else {
                row.createCell(colIndexContent).setCellValue(""); // Write value
                setRegionBorderWithMedium(getCellRangeAdressByIndex(excelRowIndex + 1, colIndexContent + 1), sheet);

            }

        }
    }

    public static void writeListDataToExcel(List listdata, List<? extends IFiCol> columnList, Path path) throws FileNotFoundException, IOException {

        // E eklenebilir

        new WorkbookFactory();
        Workbook wb = new XSSFWorkbook(); // Excell workbook
        Sheet sheet = wb.createSheet(); // WorkSheet
        Row row = sheet.createRow(1); // Row created at line 3

        Row headerRow = sheet.createRow(0); // Create row at line 0

        for (int indexColHeader = 0; indexColHeader < columnList.size(); indexColHeader++) { // For each column
            headerRow.createCell(indexColHeader).setCellValue(columnList.get(indexColHeader).getOfcTxHeader()); // Write column name
        }


        for (int indexRow = 0; indexRow < listdata.size(); indexRow++) { // For each

            Object rowent = listdata.get(indexRow);

            for (int cols = 0; cols < columnList.size(); cols++) { // For each table column

                String fieldname = columnList.get(cols).getOfcTxFieldName();

                String value = "";
                Object obj = null;

                if (fieldname != null) {

                    try {
                        obj = PropertyUtils.getProperty(rowent, fieldname);
                    } catch (IllegalAccessException e) {
                        //e.printStackTrace();
                        FiException.exTosMain(e);
                    } catch (InvocationTargetException e) {
                        //e.printStackTrace();
                        FiException.exTosMain(e);
                    } catch (NoSuchMethodException e) {
                        //e.printStackTrace();
                        Loghelper.get(FiExcel2.class).debug(" Metod Bulunamdı:" + fieldname);
                        FiException.exTosMain(e);
                    }

                }

                if (obj != null) {

                    setCellValueAndStyleByValType(wb, row, cols, obj);

                } else {
                    row.createCell(cols).setCellValue(""); // Write value
                }

            }

            // Set the row to the next one in the sequence
            row = sheet.createRow((indexRow + 2));
        }
        wb.write(new FileOutputStream(path.toString()));// Save the file
    }

    public static void writeListDataToExcelWithComment(List listdata, List<? extends IFiCol> columnList, Path path) throws FileNotFoundException, IOException {

        //new WorkbookFactory();
        Workbook wb = new XSSFWorkbook(); // Excell workbook
        Sheet sheet = wb.createSheet(); // WorkSheet
        Row row = sheet.createRow(1); // Row created at line 3

        Row headerRow = sheet.createRow(0); // Create row at line 0

        // Başlık Satırı ayarlanır // colCounterIndex kontrol edilen sütun index, colIndex yazılan sütun indeksi
        for (int colIndex = 0, colCounterIndex = 0; colCounterIndex < columnList.size(); colCounterIndex++) { // For each column

            IFiCol fiCol = columnList.get(colCounterIndex);
            //Loghelper.debug(getClazz(),"fitable col field"+fiCol.getFieldName());

            if (FiBool.isTrue(fiCol.getBoDontExportExcelTemplate())) {
                continue;
            }

            Cell cell = headerRow.createCell(colIndex);
            cell.setCellValue(FiString.orEmpty(fiCol.getOfcTxHeader())); // Write column name
            String comment = fiCol.getColComment();

            if (comment != null) {
                setCellComment(wb, sheet, cell, comment);
            }
            colIndex++;
        }

        if (listdata == null) listdata = new ArrayList();

        // Tek tek satırlar okunup, excel satırına yazılır
        for (int rowIndex = 0; rowIndex < listdata.size(); rowIndex++) { // For each

            Object rowEntity = listdata.get(rowIndex);

            // Sütunlara göre hücreler okunur
            for (int colIndex = 0, colCounterIndex = 0; colCounterIndex < columnList.size(); colCounterIndex++) { // For each table column

                IFiCol fiTableCol = columnList.get(colCounterIndex);

                if (FiBool.isTrue(fiTableCol.getBoDontExportExcelTemplate())) {
                    continue;
                }

                String fieldname = columnList.get(colCounterIndex).getOfcTxFieldName();
                Object cellValue = null;

                if (fieldname != null) {

                    try {
                        cellValue = PropertyUtils.getProperty(rowEntity, fieldname);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        Loghelper.get(getClassi()).error(FiException.exTosMain(e));
                    } catch (NoSuchMethodException e) {
                        Loghelper.get(getClassi()).error("Excel Write : Metod Bulunamdı:" + fieldname);
                        Loghelper.get(getClassi()).error(FiException.exTosMain(e));
                    }

                }

                if (cellValue != null) {
                    setCellValueAndStyleByValType(wb, row, colIndex, cellValue);
                } else {
                    row.createCell(colIndex).setCellValue(""); // Write value
                }

                colIndex++;
            }

            // Set the row to the next one in the sequence
            row = sheet.createRow((rowIndex + 2));
        }


        for (int indexCol = 0; indexCol < columnList.size(); indexCol++) { // For each column
            sheet.autoSizeColumn(indexCol, false);
        }


        wb.write(Files.newOutputStream(Paths.get(path.toString())));// Save the file
    }

    private static Class<FiExcel2> getClassi() {
        return FiExcel2.class;
    }

    private static Class getClazz() {
        return FiExcel2.class;
    }

    public static boolean setCellValueAndStyleByValType(Workbook wb, Row row, int colIndex, Object obj) {

        if (obj instanceof Float) {
            CellStyle cellStyle = wb.createCellStyle();
            DataFormat format = wb.createDataFormat();
            Cell cella = row.createCell(colIndex);
            cellStyle.setDataFormat(format.getFormat("#,##0.00"));
            cella.setCellValue(((Float) obj).doubleValue());
            cella.setCellStyle(cellStyle);
            return true;
        }

        if (obj instanceof Double) {
            CellStyle cellStyle = wb.createCellStyle();
            DataFormat format = wb.createDataFormat();
            Cell cella = row.createCell(colIndex);
            cellStyle.setDataFormat(format.getFormat("#,##0.00"));
            cella.setCellValue(((Double) obj));
            cella.setCellStyle(cellStyle);
            return true;
        }

        if (obj instanceof Date) {

            CreationHelper createHelper = wb.getCreationHelper();
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yy"));
            Cell cella = row.createCell(colIndex);
            cella.setCellValue((Date) obj);
            cella.setCellStyle(cellStyle);
            return true;
        }

        row.createCell(colIndex).setCellValue(obj.toString()); // Write value
        return true;
    }

    public static Object setCellStyleByValType(Workbook wb, Row row, int cols, Object obj) {

        if (obj instanceof Float) {
            CellStyle cellStyle = wb.createCellStyle();
            DataFormat format = wb.createDataFormat();
            Cell cella = row.createCell(cols);
            cellStyle.setDataFormat(format.getFormat("#,##0.00"));
            cella.setCellStyle(cellStyle);
            return ((Float) obj).doubleValue();
        }

        if (obj instanceof Double) {
            CellStyle cellStyle = wb.createCellStyle();
            DataFormat format = wb.createDataFormat();
            Cell cella = row.createCell(cols);
            cellStyle.setDataFormat(format.getFormat("#,##0.00"));
            //cella.setCellValue(((Double) obj));
            cella.setCellStyle(cellStyle);
            return obj;
        }

        if (obj instanceof Date) {

            CreationHelper createHelper = wb.getCreationHelper();
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yy"));
            Cell cella = row.createCell(cols);
            //cella.setCellValue((Date) obj);
            cella.setCellStyle(cellStyle);
            return obj;
        }

        return obj.toString();
    }

    public static void setCellComment(Workbook wb, Sheet sheet, Cell cell, String comment) {
        /* Create Drawing Object to hold comment */
        Drawing drawing = sheet.createDrawingPatriarch();

        Integer countNewLine = StringUtils.countMatches(comment, "\n") + 2;

        Integer satirboyut = Math.max(comment.length() / 30 + 1, countNewLine);
        //Loghelperr.getInstance(getClass).debug(" sütun:"+ indexColHeader+"count new line"+ countNewLine);

        /* Let us draw a big comment box to hold lots  of comment data */   // dx konumları, c1r1 ,c2r2 kutunun köşe noktaları (exceldeki satır ve sütün no)
        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 5, 5, 10, 5 + satirboyut);
        /* Create a comment object */
        Comment comment1 = drawing.createCellComment(anchor);
        /* Create some comment text as Rich Text String */
        CreationHelper richTextFactory = wb.getCreationHelper();
        RichTextString rtf1 = richTextFactory.createRichTextString(comment);
        /* You can also insert newline character in comments using \n */
        comment1.setString(rtf1);
        /* Define the author for the comments,you can set each comment to have a different author */
        comment1.setAuthor("Entegre");
        /* Stamp the comment to the cell */
        cell.setCellComment(comment1);
    }


    public <E> File writeSablonExcelFile(List<? extends IFiCol> listExcelColums, List<E> listData, String filename) {

        String basepath = FiWinUtils.getUserDirOrDesktopDir();

        //String excelfilename = new SimpleDateFormat("'P"+ belgekod +"'-yyyyMMddhhmmss'.xlsx'").format(new Date());
        // üzerine yazıyor

        String excelfilename = filename + " - " + FiDate.datetoString_timestampt2(new Date()) + ".xlsx";

        Path path = Paths.get(basepath + "\\ozpasentegre\\");
        if (!Files.exists(path)) {
            new File(path.toString()).mkdirs();
        }
        //.getProperty("user.home")
        path = Paths.get(basepath + "\\ozpasentegre\\" + excelfilename);

        //Loghelperr.getInstance(getClass()).debug(" path:" + path.toString());

        try {
            FiExcel2.writeListDataToExcelWithComment(listData, listExcelColums, path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new File(path.toString());

    }

    public void openExcelFileWithApp(File file) {

        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
            new FxDialogShow().showModalWarningAlert("Dosya açılırken Sorunlar Karşılaşıldı.\n" + file.getPath());
        }

    }

    public void printExcelFileByVbs(File file) {

        //Loghelperr.getInstance(getClass()).debug("vb excel print");

        //Application.Dialogs(xlDialogPrinterSetup).Show
        //ActiveWindow.SelectedSheets.PrintOut
        try {

            //If Application.Dialogs(xlDialogPrinterSetup).Show = True Then
            //ActiveWindow.SelectedSheets.PrintOut
            //End If

            String vbs = "Dim AppExcel\r\n"
                    + "Set AppExcel = CreateObject(\"Excel.application\")\r\n"
                    + "AppExcel.Workbooks.Open(\"" + file.getPath() + "\")\r\n"
                    + "appExcel.ActiveWindow.SelectedSheets.PrintOut\r\n"
                    + "Appexcel.Quit\r\n"
                    + "Set appExcel = Nothing";

            String vbs2 = "filepath = \"" + file.getPath() + "\"\n" +
                    "Dim appExcel\n" +
                    "Set appExcel = CreateObject(\"Excel.Application\") \n" +
                    "appExcel.Workbooks.Open(filepath)\n" +
                    "appExcel.Visible = True\n" +
                    "'bTemp =appExcel.Dialogs(8).Show\n" +
                    "'if bTemp = true Then appExcel.ActiveWindow.SelectedSheets.PrintOut\n" +
                    "appExcel.ActiveWindow.SelectedSheets.PrintPreview\n" +
                    "'appExcel.Quit\n" +
                    "'Set appExcel = Nothing";

            File vbScript = File.createTempFile("vbScript", ".vbs");
            vbScript.deleteOnExit();
            FileWriter fw = new FileWriter(vbScript);
            fw.write(vbs2);
            fw.close();

            //ProcessBuilder pb = new ProcessBuilder("cscript //NoLogo " + vbScript.getPath());
            //pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            //pb.redirectError(ProcessBuilder.Redirect.INHERIT);
            //Process p = pb.start();

            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + vbScript.getPath());

            Loghelper.get(getClass()).debug(IOUtils.toString(p.getInputStream(), "UTF-8"));
            Loghelper.get(getClass()).debug(IOUtils.toString(p.getErrorStream(), "UTF-8"));

//			String line;
//			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			while ((line = input.readLine()) != null) {
//				System.out.println(line);
//			}
//			input.close();

        } catch (Exception e) {
            e.printStackTrace();
            new FxDialogShow().showModalWarningAlert("hata oluştu.\nDetay:" + FiException.exTosMain(e));
        }
    }

    public File convertXlsToXlsxByVbs(File source, String suffix) {

        //Loghelperr.getInstance(getClass()).debug("vb convert excel");

        if (suffix == null) suffix = "";

        String filenameSimple = FiFile.removeFileExtension(source.getAbsolutePath()) + suffix;

        File newFile = null;

        try {

            String convertVbs = String.format(
                    "Dim oExcel\n" +
                            "Set oExcel = CreateObject(\"Excel.Application\") \n" +
                            "Dim oBook \n" +
                            "Set oBook = oExcel.Workbooks.Open( \"%s\" ) \n" +
                            "oExcel.DisplayAlerts = False \n" +
                            "oBook.SaveAs \"%s\",51 \n" +   // 1 xls, 6 csv
                            "oBook.Close False \n" +  //false yapılmıştı
                            "oExcel.Quit \n" +
                            "Set oExcel= Nothing \n", source.getAbsolutePath(), filenameSimple);

            Loghelper.get(getClass()).debug(convertVbs);

            File vbScript = File.createTempFile("vbScript", ".vbs");
            vbScript.deleteOnExit();

            // Dosya oluşturuluyor
            FileWriter fw = new FileWriter(vbScript);
            fw.write(convertVbs);
            fw.close();

            //ProcessBuilder pb = new ProcessBuilder("cscript //NoLogo " + vbScript.getPath());
            //pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            //pb.redirectError(ProcessBuilder.Redirect.INHERIT);
            //Process p = pb.start();

            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + vbScript.getPath());

            Loghelper.get(getClass()).debug(IOUtils.toString(p.getInputStream(), "UTF-8"));
            Loghelper.get(getClass()).debug(IOUtils.toString(p.getErrorStream(), "UTF-8"));

//			String line;
//			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			while ((line = input.readLine()) != null) {
//				System.out.println(line);
//			}
//			input.close();
            newFile = new File(filenameSimple + ".xlsx");

        } catch (Exception e) {
            e.printStackTrace();
            new FxDialogShow().showModalWarningAlert("hata oluştu.\nDetay:" + FiException.exTosMain(e));
        }

        return newFile;
    }

    public static void writeJTableToExcel(JTable table, Path path) throws FileNotFoundException, IOException {

        new WorkbookFactory();
        Workbook wb = new XSSFWorkbook(); // Excell workbook
        Sheet sheet = wb.createSheet(); // WorkSheet
        Row row = sheet.createRow(1); // Row created at line 3
        TableModel model = table.getModel(); // Table model

        Row headerRow = sheet.createRow(0); // Create row at line 0
        for (int headings = 0; headings < model.getColumnCount(); headings++) { // For each column
            headerRow.createCell(headings).setCellValue(model.getColumnName(headings)); // Write column name
        }

        for (int rows = 0; rows < model.getRowCount(); rows++) { // For each table row

            for (int cols = 0; cols < table.getColumnCount(); cols++) { // For each table column

                //String value = "";
                Object cellValue = model.getValueAt(rows, cols);
                if (cellValue != null) {
                    setCellValueAndStyleByValType(wb, row, cols, cellValue);
                } else {
                    row.createCell(cols).setCellValue(""); // Write value
                }

            }

            // Set the row to the next one in the sequence
            row = sheet.createRow((rows + 2));
        }
        wb.write(new FileOutputStream(path.toString()));// Save the file
    }


}
