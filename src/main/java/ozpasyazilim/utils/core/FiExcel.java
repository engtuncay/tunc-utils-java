package ozpasyazilim.utils.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.javatuples.Pair;
import ozpasyazilim.utils.datatypes.*;
import ozpasyazilim.utils.fidborm.FiReflectClass;
import ozpasyazilim.utils.fidborm.FiField;
import ozpasyazilim.utils.log.LogListener;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.mvc.IFiModCont;
import ozpasyazilim.utils.table.FiCol;
import ozpasyazilim.utils.table.IFiColHelper;
import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.windows.FiWinUtils;

/**
 * URREV metodları statik yapalım 17-09-2024
 */
public class FiExcel {

  // Başlık sütunlarını, kaçıncı satıra kadar arayacak
  private final Integer lastRowIndexForHeaderSearch = 101;

  FileInputStream fileInputStream;

  public static void main(String[] args) {

//		File fileExcel = new File("Y:\\ornekexcelxlsx.xlsx");
//		ModelExcelHelper modelExcelHelper = new ModelExcelHelper(fileExcel);
//		ModelExcelHelper.printExcelHelper(modelExcelHelper);
//
//		File fileExcel2 = new File("Y:\\ornekexcelxls.xls");
//		ModelExcelHelper modelExcelHelper2 = new ModelExcelHelper(fileExcel2);
//		ModelExcelHelper.printExcelHelper(modelExcelHelper2);
//
//		List<InfTableCol> listTableCol = new ArrayList<>();
//		listTableCol.add(new OzTableCol("Üst Limiti", "ustlimiti", OzColType.Double));
//		List<IskProSablon> iskProSablons = modelExcelHelper2.bindEntity(modelExcelHelper2, listTableCol, IskProSablon.class);
//
//		iskProSablons.forEach(iskProSablon -> System.out.println("entity:" + iskProSablon.getUstlimiti()));


  }

  public static FiExcel build() {
    return new FiExcel();
  }

  public <E> List<E> readExcelFile(File excelfile, List<? extends IFiCol> listColumns, Class<E> entityclass) {
    return readExcelFileMain(excelfile, listColumns, entityclass, null);
  }

  public FkbList readExcelFileAsFkbList(File excelfile, List<FiCol> listColumns) {
    return readExcelFileMainAsFkbList(excelfile, listColumns, null);
  }

  public <E> List<E> readExcelFileMain(File excelfile, List<? extends IFiCol> listColumns, Class<E> entityclass, String txSheetName) {

    if (excelfile == null) {
      Loghelper.get(getClass()).debug("Excel File Null");
      return null;
    }

    String extension = FilenameUtils.getExtension(excelfile.getName());

    if (extension.equalsIgnoreCase("xlsx"))
      return readExcelXLSX(excelfile, listColumns, entityclass, txSheetName);

    if (extension.equalsIgnoreCase("xls"))
      return readExcelXlsV3(excelfile, listColumns, entityclass);

    return null;
  }

  public FkbList readExcelFileMainAsFkbList(File excelfile, List<FiCol> fiCols, String txSheetName) {

    if (excelfile == null) {
      Loghelper.get(getClass()).debug("Excel File Null");
      return null;
    }

    String extension = FilenameUtils.getExtension(excelfile.getName());

    if (extension.equalsIgnoreCase("xlsx"))
      return readExcelXLSXAsFkbList(excelfile, fiCols, txSheetName);

    if (extension.equalsIgnoreCase("xls"))
      return readExcelXlsV3AsFkbList(excelfile, fiCols);

    return null;
  }

  public <E> List<E> readExcelXLS(File excelfile, List<? extends IFiCol> listColumns, Class<E> entityclass) {

    // 16-08-2019 çevirme yapılıyor
    // excelfile = new FiExcel().convertXlsToXlsxByVbs(excelfile,"entegre");
    //return readExcelXLSX(excelfile,listColumns,entityclass);

    // 17-08-2019
    return readExcelXlsV3(excelfile, listColumns, entityclass);

  }

  public FiListKeyString readExcelFileAsMap(File excelfile, List<? extends IFiCol> listColumns) {

    if (excelfile == null) {
      Loghelper.get(getClass()).debug("Excel File Null");
      return null;
    }

    String extension = FilenameUtils.getExtension(excelfile.getName());

    if (extension.equalsIgnoreCase("xlsx"))
      return readExcelXLSXAsMapString(excelfile, listColumns);

//		if (extension.toLowerCase().equals("xls"))
//			return readExcelXLS(excelfile, listColumns, entityclass);

    return null;
  }

  public <E> List<E> readExcelXlsV1(File excelfile, List<IFiCol> listColumns, Class<E> entityclass) {

    FileInputStream file = null;
    HSSFWorkbook workbook = null;

    try {
      file = new FileInputStream(excelfile);
      // Get the workbook instance for XLS file
      workbook = new HSSFWorkbook(file);

    } catch (FileNotFoundException e1) {
      Loghelper.debugException(getClass(), e1);
      //FIXME
      LogListener.setLogMessage("Excel Dosya Bulunamadı.");
    } catch (IOException e2) {
      Loghelper.debugException(getClass(), e2);
      //LogListener.setLogMessageAndDetail("IO Hatası Oluştu.", e2);
    }

    List<Map<String, String>> listrows = new ArrayList<>();
    HSSFSheet sheet = null;

    // FIXME sheet boş gelebiliyor
    try {
      // Try to Get first sheet from the workbook
      sheet = workbook.getSheetAt(0);

    } catch (Exception e) {
      Loghelper.get(this.getClass()).error("Hata :" + FiException.exTosMain(e));
      return null;
    }

    //Iterator rows = sheet.rowIterator();

    Integer lastRowNumber = sheet.getLastRowNum();

    //Object stringe çevrildi.14/08/18
    String[] rowHeader = null;

    //HSSFRow row;
    //HSSFCell cell;

    // ** start header row okunur
    Integer rowIndexExcel = 0;

    HSSFRow rowHeaderExcel = null; // = sheet.getRow(rowIndexExcel);

    //Loghelper.getInstance(getClass()).debug("Başlık Satırı Aranacak");
    //Loghelper.getInstance(getClass()).debug("Last Row Number:" + lastRowNumber);

    // Eğer sütun isimleri daha alt satırlardaysa bulmaya yarar
    Boolean colFound = false;

    //Loghelper.getInstance(getClass()).debug(" aranan col:" + listColumns.get(0).getHeader());

    List<String> headers = null;

    // Öncelikle Başlıkların olduğu satır aranıyor
    for (; rowIndexExcel <= lastRowNumber; rowIndexExcel++) {

      //Loghelper.getInstance(getClass()).debug(" Row Index Cell:" + rowIndexExcel);

      rowHeaderExcel = sheet.getRow(rowIndexExcel);

      if (rowHeaderExcel == null) continue;

      Short lastColNumber = rowHeaderExcel.getLastCellNum(); // 1 başlayarak, son sütün hücre numarası

      rowHeader = new String[lastColNumber];

      for (int cn = 0; cn < lastColNumber; cn++) {

        HSSFCell cell = rowHeaderExcel.getCell(cn);
        if (cell == null) continue;
        rowHeader[cn] = getCellStringValueXLS(cell);   //cell.getStringCellValue();

      }

      if (rowHeader[0] == null) rowHeader[0] = "";

      headers = Arrays.asList(rowHeader);

      headers = headers.stream().map(b -> {
        if (b != null) return b.trim();
        return null;
      }).collect(Collectors.toList());

      //Loghelper.getInstance(getClass()).debug(" headar1 : " + rowHeader[0]);

      if (headers.contains(listColumns.get(0).getOfcTxHeader().trim())) {
        //Loghelper.getInstance(getClass()).debug(" Başlık bulundu:" + rowIndexExcel);
        colFound = true;
        break;
      }

      if (rowIndexExcel.equals(lastRowNumber)) {
        Loghelper.get(getClass()).debug("Son satır erişti başlık yok");
      }

    }

    if (!colFound) return new ArrayList<>();

    List<String> finalHeaders = headers;

    listColumns.forEach(entity -> {
      if (finalHeaders.contains(entity.getOfcTxHeader().trim())) {
        entity.setBoEnabled(true);
      }
    });

    //Loghelper.getInstance(getClass()).debug(" Row Headers Length:"+rowHeader.length));
    // not : alternatif yol : for (Row row : sheet)


    // Exceldeki Data Satırları Okunuyor
    Integer lastColNumber = rowHeader.length;              //rowHeaderExcel.getLastCellNum(); // 1 başlayarak, son sütün hücre numarası

    rowIndexExcel++;
    for (; rowIndexExcel <= lastRowNumber; rowIndexExcel++) {

      //Object[] rowobject = new Object[rowContent.getLastCellNum()];
      //rowContent = (XSSFRow) rows.next();
      //Loghelper.getInstance(getClass()).debug("Read Row:"+ rowIndex);

      HSSFRow rowContent = sheet.getRow(rowIndexExcel);

      if (rowContent == null) continue;

      //Iterator<Cell> iterRowCells = rowContent.cellIterator();

      Map<String, String> maprow = new HashMap<>();

      //while (iterRowCells.hasNext())
      Boolean empty = true;
      for (int cn = 0; cn < lastColNumber; cn++) {   //rowContent.getLastCellNum()

        //Loghelper.getInstance(getClass()).debug(" col:"+cn);

        String header = "";

        if (rowHeader[cn] == null) {
          //Loghelper.getInstance(getClass()).debug(" header cn null"+ cn);
          continue;
        }

        header = rowHeader[cn].toString();

        //if (rowHeader.length > cn) arsiv
        //if(!iterRowCells.hasNext()) arsiv

        HSSFCell cell = rowContent.getCell(cn);

        if (cell == null) {
          //Loghelper.getInstance(getClass()).debug(" header cell null"+ header);
          maprow.put(header, "");
          continue;
        }

        String strCellValue = getCellStringValueXLS(cell);
        maprow.put(header, strCellValue);

        if (!strCellValue.equals("")) empty = false;

      }

      if (!empty) {
        //Loghelper.getInstance(getClass()).debug("eklendi");
        listrows.add(maprow);
      }

    }


    try {
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return bindEntityExcel(listrows, listColumns, entityclass);

  }

  public <E> List<E> readExcelXLSX(File fileExcelXlsx, List<? extends IFiCol> listColumns, Class<E> entityclass, String txSheetName) {

    XSSFSheet sheet;

    XSSFWorkbook workbookXlsx = getWorkbookFromExcelXlsxFile(fileExcelXlsx);

    if (!FiString.isEmptyTrim(txSheetName)) {
      sheet = workbookXlsx.getSheet(txSheetName);
      if (sheet == null) {
        sheet = workbookXlsx.getSheetAt(0);
      }
    } else {
      sheet = workbookXlsx.getSheetAt(0);
    }

    // Get first sheet from the workbook
    //XSSFSheet sheet = getWorkbookFromExcelXlsxFile(fileExcelXlsx).getSheetAt(0);

    List<Map<String, String>> listrows = new ArrayList<>();
    //Not old usage //Iterator rows = sheet.rowIterator();

    // exceldeki son satır nosu (satır no 1 den başlar)
    Integer lastRowNumber = sheet.getLastRowNum();

    //Excel başlık satırlarını arar , kaçıncı satırda bulduğunu ve satır listesini verir
    //Excelde listColumns daki sütunları arar required ve bir tane sütun bulursa eğer bulundu yapar
    Pair<Integer, List<String>> excelHeadersXLSX = findHeadersInExcel(sheet, listColumns);

    Boolean colFound = false;

    if (excelHeadersXLSX != null) colFound = true;

    if (!colFound) return new ArrayList<>();

    List<String> finalHeaders = excelHeadersXLSX.getValue1();

    // ?? finalheaders null olarak dönmüşse , başlık satırlarını bulamamış
    if (finalHeaders == null) {
      Loghelper.get(getClass()).debug("Başlık Satırları bulunamadı...");
      return null;
    }

    listColumns.forEach(fiTableCol -> {
      if (fiTableCol.getOfcTxHeader() == null) return;

      if (finalHeaders.contains(fiTableCol.getOfcTxHeader().trim())) {
        fiTableCol.setBoEnabled(true);
      }
    });

    // not : alternatif yol : for (Row row : sheet)
    Integer rowIndexExcel = excelHeadersXLSX.getValue0();

    // Exceldeki Data Satırları Okunuyor
    Integer lastColNumber = finalHeaders.size();  //rowHeader.length;              //rowHeaderExcel.getLastCellNum(); // 1 başlayarak, son sütün hücre numarası

    // index = Excel Satır - 1 dir. ( Index 0 dan başlar ) (Excel 1 den başlar)
    // Loghelper.getInstance(getClass()).debug(" Row Index (Header Index)(Satır Sıralaması 0 dan başlar):"+ rowIndexExcel);
    rowIndexExcel++;
    for (; rowIndexExcel <= lastRowNumber; rowIndexExcel++) {
      XSSFRow rowContent = sheet.getRow(rowIndexExcel);
      if (rowContent == null) continue;
      Map<String, String> maprow = new HashMap<>();
      Boolean empty = true;

      for (int cn = 0; cn < lastColNumber; cn++) {   //rowContent.getLastCellNum()
        //Loghelper.getInstance(getClass()).debug(" col:"+cn);
        String header = "";
        if (finalHeaders.get(cn) == null) {
          //Loghelper.getInstance(getClass()).debug(" header cn null"+ cn);
          continue;
        }

        header = finalHeaders.get(cn).toString();

        //if (rowHeader.length > cn) arsiv
        //if(!iterRowCells.hasNext()) arsiv

        XSSFCell cell = rowContent.getCell(cn);

        if (cell == null) {
          //Loghelper.getInstance(getClass()).debug(" header cell null"+ header);
          maprow.put(header, "");
          continue;
        }

        //cell = (XSSFCell) iterRowCells.next();

        String strCellValue = getCellStringValueXLSX(cell);
        maprow.put(header, strCellValue);

        if (!strCellValue.equals("")) empty = false;

      }

      if (!empty) {
        //Loghelper.getInstance(getClass()).debug("eklendi");
        listrows.add(maprow);
      }

    }

    if (getFileInputStream() != null) {

      try {
        getFileInputStream().close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return bindEntityExcel(listrows, listColumns, entityclass);

  }

  public FkbList readExcelXLSXAsFkbList(File fileExcelXlsx, List<FiCol> listColumns, String txSheetName) {

    XSSFSheet sheet;

    XSSFWorkbook workbookXlsx = getWorkbookFromExcelXlsxFile(fileExcelXlsx);

    if (!FiString.isEmptyTrim(txSheetName)) {
      sheet = workbookXlsx.getSheet(txSheetName);
      if (sheet == null) {
        sheet = workbookXlsx.getSheetAt(0);
      }
    } else {
      sheet = workbookXlsx.getSheetAt(0);
    }

    // Get first sheet from the workbook
    //XSSFSheet sheet = getWorkbookFromExcelXlsxFile(fileExcelXlsx).getSheetAt(0);

    List<Map<String, String>> listrows = new ArrayList<>();
    //Not old usage //Iterator rows = sheet.rowIterator();

    // exceldeki son satır nosu (satır no 1 den başlar)
    Integer lastRowNumber = sheet.getLastRowNum();

    //Excel başlık satırlarını arar , kaçıncı satırda bulduğunu ve satır listesini verir
    //Excelde listColumns daki sütunları arar required ve bir tane sütun bulursa eğer bulundu yapar
    Pair<Integer, List<String>> excelHeadersXLSX = findHeadersInExcel(sheet, listColumns);

    Boolean colFound = false;

    if (excelHeadersXLSX != null) colFound = true;

    if (!colFound) return new FkbList();

    List<String> finalHeaders = excelHeadersXLSX.getValue1();

    // ?? finalheaders null olarak dönmüşse , başlık satırlarını bulamamış
    if (finalHeaders == null) {
      Loghelper.get(getClass()).debug("Başlık Satırları bulunamadı...");
      return null;
    }

    listColumns.forEach(fiTableCol -> {
      if (fiTableCol.getOfcTxHeader() == null) return;

      if (finalHeaders.contains(fiTableCol.getOfcTxHeader().trim())) {
        fiTableCol.setBoEnabled(true);
      }
    });

    // not : alternatif yol : for (Row row : sheet)
    Integer rowIndexExcel = excelHeadersXLSX.getValue0();

    // Exceldeki Data Satırları Okunuyor
    Integer lastColNumber = finalHeaders.size();  //rowHeader.length;              //rowHeaderExcel.getLastCellNum(); // 1 başlayarak, son sütün hücre numarası

    // index = Excel Satır - 1 dir. ( Index 0 dan başlar ) (Excel 1 den başlar)
    // Loghelper.getInstance(getClass()).debug(" Row Index (Header Index)(Satır Sıralaması 0 dan başlar):"+ rowIndexExcel);
    rowIndexExcel++;
    for (; rowIndexExcel <= lastRowNumber; rowIndexExcel++) {
      XSSFRow rowContent = sheet.getRow(rowIndexExcel);
      if (rowContent == null) continue;
      Map<String, String> maprow = new HashMap<>();
      Boolean empty = true;

      for (int cn = 0; cn < lastColNumber; cn++) {   //rowContent.getLastCellNum()
        //Loghelper.getInstance(getClass()).debug(" col:"+cn);
        String header = "";
        if (finalHeaders.get(cn) == null) {
          //Loghelper.getInstance(getClass()).debug(" header cn null"+ cn);
          continue;
        }

        header = finalHeaders.get(cn).toString();

        //if (rowHeader.length > cn) arsiv
        //if(!iterRowCells.hasNext()) arsiv

        XSSFCell cell = rowContent.getCell(cn);

        if (cell == null) {
          //Loghelper.getInstance(getClass()).debug(" header cell null"+ header);
          maprow.put(header, "");
          continue;
        }

        //cell = (XSSFCell) iterRowCells.next();

        String strCellValue = getCellStringValueXLSX(cell);
        maprow.put(header, strCellValue);

        if (!strCellValue.equals("")) empty = false;

      }

      if (!empty) {
        //Loghelper.getInstance(getClass()).debug("eklendi");
        listrows.add(maprow);
      }

    }

    if (getFileInputStream() != null) {

      try {
        getFileInputStream().close();
      } catch (IOException e) {
        //e.printStackTrace();
        Loghelper.get(getClass()).debug(FiException.exToErrorLog(e));

      }
    }

    return bindEntityExcelToFkb(listrows, listColumns);
  }

  public FiListKeyString readExcelXLSXAsMapString(File fileExcelXlsx, List<? extends IFiCol> listColumns) {

    // Get first sheet from the workbook
    XSSFSheet sheet = getWorkbookFromExcelXlsxFile(fileExcelXlsx).getSheetAt(0);

    FiListKeyString listrows = new FiListKeyString();
    //Not old usage //Iterator rows = sheet.rowIterator();

    // exceldeki son satır nosu (satır no 1 den başlar)
    Integer lastRowNumber = sheet.getLastRowNum();

    //Excel başlık satırlarını arar , kaçıncı satırda bulduğunu ve satır listesini verir
    //Excelde listColumns daki sütunları arar required ve bir tane sütun bulursa eğer bulundu yapar
    Pair<Integer, List<String>> pairHeaderExcel = findHeadersInExcel(sheet, listColumns);

    Boolean colFound = false;
    if (pairHeaderExcel != null) colFound = true;
    if (!colFound) return new FiListKeyString();

    List<String> finalHeaders = pairHeaderExcel.getValue1();

    // ?? finalheaders null olarak dönmüşse , başlık satırlarını bulamamış
    if (FiCollection.isEmpty(finalHeaders)) {
      Loghelper.get(getClass()).debug("Başlık Satırları bulunamadı...");
      return null;
    }

    listColumns.forEach(fiTableCol -> {
      if (fiTableCol.getOfcTxHeader() == null) return;

      if (finalHeaders.contains(fiTableCol.getOfcTxHeader().trim())) {
        fiTableCol.setBoEnabled(true);
      }
    });

    // not : alternatif yol : for (Row row : sheet)
    Integer rowIndexExcel = pairHeaderExcel.getValue0();

    // Exceldeki Data Satırları Okunuyor
    Integer lastColNumber = finalHeaders.size();  //rowHeader.length;              //rowHeaderExcel.getLastCellNum(); // 1 başlayarak, son sütün hücre numarası

    // index = Excel Satır - 1 dir. ( Index 0 dan başlar ) (Excel 1 den başlar)
    // Loghelper.getInstance(getClass()).debug(" Row Index (Header Index)(Satır Sıralaması 0 dan başlar):"+ rowIndexExcel);
    rowIndexExcel++;
    for (; rowIndexExcel <= lastRowNumber; rowIndexExcel++) {
      XSSFRow rowContent = sheet.getRow(rowIndexExcel);
      if (rowContent == null) continue;
      FiKeyString maprow = new FiKeyString();
      Boolean empty = true;

      for (int cn = 0; cn < lastColNumber; cn++) {   //rowContent.getLastCellNum()
        //Loghelper.getInstance(getClass()).debug(" col:"+cn);
        String header = "";
        if (finalHeaders.get(cn) == null) {
          //Loghelper.getInstance(getClass()).debug(" header cn null"+ cn);
          continue;
        }
        header = finalHeaders.get(cn).toString();
        XSSFCell cell = rowContent.getCell(cn);

        if (cell == null) {
          //Loghelper.getInstance(getClass()).debug(" header cell null"+ header);
          maprow.put(header, "");
          continue;
        }
        //cell = (XSSFCell) iterRowCells.next();
        String strCellValue = getCellStringValueXLSX(cell);
        maprow.put(header, strCellValue);
        if (!strCellValue.equals("")) empty = false;
      }

      if (!empty) {
        //Loghelper.getInstance(getClass()).debug("eklendi");
        listrows.add(maprow);
      }

    }

    if (getFileInputStream() != null) {
      try {
        getFileInputStream().close();
      } catch (IOException e) {
        Loghelper.get(getClass()).debug(FiException.exToErrorLog(e));
      }
    }

    return bindExcelToListMapStr(listrows, listColumns);
  }

  public FileInputStream getFileInputStream() {
    return fileInputStream;
  }

  public void setFileInputStream(FileInputStream fileInputStream) {
    this.fileInputStream = fileInputStream;
  }

  public XSSFWorkbook getWorkbookFromExcelXlsxFile(File excelXlsxFile) {

    FileInputStream file = null;
    XSSFWorkbook workbook = null;

    try {
      file = new FileInputStream(excelXlsxFile);
      // Get the workbook instance for XLS file
      workbook = new XSSFWorkbook(file);
      setFileInputStream(file);

    } catch (IOException ex) {
      Loghelper.get(getClass()).error(FiException.exTosMain(ex));
    }

    // Get first sheet from the workbook
    //XSSFSheet sheet = workbook.getSheetAt(sheetIndex);

    return workbook;

  }

  public HSSFWorkbook getWorkbookFromExcelXlsFile(File excelFile) {

    FileInputStream file = null;
    HSSFWorkbook workbook = null;

    try {
      file = new FileInputStream(excelFile);
      // Get the workbook instance for XLS file
      workbook = new HSSFWorkbook(file);
      setFileInputStream(file);

    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    } catch (IOException e2) {
      e2.printStackTrace();
    }

    // Get first sheet from the workbook
    //XSSFSheet sheet = workbook.getSheetAt(sheetIndex);

    return workbook;

  }

  public List<String> readExcelHeadersXLSX(File excelXlsxFile, List<IFiCol> listColumns) {

    XSSFSheet sheet = getWorkbookFromExcelXlsxFile(excelXlsxFile).getSheetAt(0);
    List<String> excelHeadersXLSX = findHeadersInExcel(sheet, listColumns).getValue1();

    if (getFileInputStream() != null) {
      try {
        getFileInputStream().close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return excelHeadersXLSX;

  }

  /**
   * @param sheet
   * @param listColumns
   * @return Pair Prm1: Başlıkların bulunduğu satır index i , Prm2: Başlıklar???
   */
  public Pair<Integer, List<String>> findHeadersInExcel(XSSFSheet sheet, List<? extends IFiCol> listColumns) {

    //Not Iterator rows = sheet.rowIterator();

    // 1 başlayarak, exceldeki son satır nosu
    Integer lastRowNumber = sheet.getLastRowNum();

    // Başlıklar bu arrayde tutulacak
    String[] rowHeader = null;  // map<int,string> de tanımlabilir

    XSSFRow rowItemExcel = null; // = sheet.getRow(rowIndexExcel);

    //Loghelper.getInstance(getClass()).debug("Başlık Satırı Aranacak");
    //Loghelper.getInstance(getClass()).debug("Last Row Number:" + lastRowNumber);

    // Eğer sütun isimleri daha alt satırlardaysa bulmaya yarar
    Boolean colFound = false;

    //Loghelper.getInstance(getClass()).debug(" aranan col:" + listColumns.get(0).getHeader());
    List<String> headers = null;

    Map<String, ? extends IFiCol> mapHeaderToFiCol = FiCollection.listToMapSingle(listColumns, ozTableCol -> ozTableCol.getOfcTxHeader().trim());

    // ** rowIndexExcel start header row okunur
    Integer rowIndexExcel = 0;
    // Öncelikle Başlıkların olduğu satır aranıyor
    IntRef intRef = IntRef.bui();
    for (; rowIndexExcel <= lastRowNumber; rowIndexExcel++) {
      //Loghelper.getInstance(getClass()).debug(" Searching Header Row Number (index+1): " + (rowIndexExcel + 1));
      rowItemExcel = sheet.getRow(rowIndexExcel);
      if (rowItemExcel == null) continue;

      Short lastColNumber = rowItemExcel.getLastCellNum(); // 1 başlayarak, son sütün(col) hücre numarası
      //Loghelper.getInstance(getClass()).debug("Last Col Number:" + lastColNumber);
      if (lastColNumber == -1 || lastColNumber == 0) continue;
      rowHeader = new String[lastColNumber];

      for (int colIndex = 0; colIndex < lastColNumber; colIndex++) {
        XSSFCell cell = rowItemExcel.getCell(colIndex);

        if (cell == null) {
          //Loghelperr.getInstance(getClass()).debug(String.format("(%s,%s)=%s",rowIndexExcel,colIndex,"null"));
          continue;
        }

        String cellValue = getCellStringValueXLSX(cell);
        //Loghelperr.debugLog(getClass(),String.format("(%s,%s)=%s",rowIndexExcel,colIndex,cellValue),intRef,10);
        rowHeader[colIndex] = cellValue.trim();

        // Loghelper.get(getClass()).debug("Header Value:" + cellValue.trim());

        if (mapHeaderToFiCol.containsKey(cellValue.trim())) {
          IFiCol fiCol = mapHeaderToFiCol.get(cellValue.trim());
          colFound = true;
          fiCol.setColIndex(colIndex);
          fiCol.setBoExist(true);
        }

        // 100 sütundan sonra sütuna bakmasın
        if (lastColNumber == 100) {
          break;
        }
      }

      //required olan column bulunmamışsa colfund false yapılır
      for (IFiCol iTableCol : listColumns) {

        if (colFound && FiBool.isTrue(iTableCol.getBoRequired()) && FiBool.isFalseOrNull(iTableCol.getBoExist())) {
          colFound = false;
          Loghelper.debugLog(getClass(), "Required Col olmadığı için colfound false a çevrildi");
        }

      }

      //if (rowHeader[0] == null) rowHeader[0] = "";

      if (rowHeader.length > 0) {
        headers = Arrays.asList(rowHeader);
      }

      if (colFound) break;

      // 100 satırdan sonra başlık aramasın
      if (rowIndexExcel == this.lastRowIndexForHeaderSearch) break;

      if (rowIndexExcel.equals(lastRowNumber)) {
        Loghelper.get(getClass()).debug("Son satıra erişti, başlıklar bulunamadı.");
      }


    }

    if (!colFound) {
      return new Pair<>(null, null);
    }

    List<String> finalHeaders = headers;

    // ??????? bulunan satırları colEnabled prop si true yapılmış , nedeni anlaşıldı , bunun yerine boIsExist var olduğu belirtilmişti
    listColumns.forEach(entity -> {
      if (finalHeaders.contains(entity.getOfcTxHeader().trim())) {
        entity.setBoEnabled(true);
      }
    });

    //Loghelper.getInstance(getClass()).debug(" Row Headers Length:"+rowHeader.length));
    //not : alternatif yol : for (Row row : sheet)

    Integer lastColNumber = rowHeader.length;  //rowItemExcel.getLastCellNum(); // 1 başlayarak, son sütün hücre numarası

    Pair<Integer, List<String>> result = new Pair<>(null, null);

    if (colFound) {
      result = new Pair<>(rowIndexExcel, finalHeaders);
    }

    return result;

  }

  /**
   * XLS Excel dosyaları için
   *
   * @param sheet
   * @param listColumns
   * @return Pair Prm1: Başlıkların bulunduğu satır index i , Prm2: Başlıklar???
   */
  public Pair<Integer, List<String>> findHeadersInExcel(HSSFSheet sheet, List<? extends IFiCol> listColumns) {

    //Not Iterator rows = sheet.rowIterator();

    // 1 başlayarak, exceldeki son satır nosu
    Integer lastRowNumber = sheet.getLastRowNum();

    // Başlıklar bu arrayde tutulacak
    String[] rowHeader = null;  // map<int,string> de tanımlabilir

    // ** start header row okunur
    Integer rowIndexExcel = 0;

    HSSFRow rowHeaderExcel = null; // = sheet.getRow(rowIndexExcel);

    //Loghelper.getInstance(getClass()).debug("Başlık Satırı Aranacak");
    //Loghelper.getInstance(getClass()).debug("Last Row Number:" + lastRowNumber);

    // Eğer sütun isimleri daha alt satırlardaysa bulmaya yarar
    Boolean colFound = false;

    //Loghelper.getInstance(getClass()).debug(" aranan col:" + listColumns.get(0).getHeader());
    List<String> headers = null;

    Map<String, ? extends IFiCol> mapOztableCol = FiCollection.listToMapSingle(listColumns, ozTableCol -> ozTableCol.getOfcTxHeader().trim());

    // Öncelikle Başlıkların olduğu satır aranıyor
    for (; rowIndexExcel <= lastRowNumber; rowIndexExcel++) {

      //Loghelper.getInstance(getClass()).debug(" Searching Header Row Number (index+1): " + (rowIndexExcel + 1));

      rowHeaderExcel = sheet.getRow(rowIndexExcel);

      if (rowHeaderExcel == null) continue;

      Short lastColNumber = rowHeaderExcel.getLastCellNum(); // 1 başlayarak, son sütün hücre numarası

      //Loghelper.get(getClass()).debug("Last Col Number:" + lastColNumber);

      if (lastColNumber == -1 || lastColNumber == 0) continue;

      rowHeader = new String[lastColNumber];

      for (int cn = 0; cn < lastColNumber; cn++) {

        HSSFCell cell = rowHeaderExcel.getCell(cn);
        if (cell == null) continue;
        String cellValue = getCellStringValueXls(cell);
        rowHeader[cn] = cellValue.trim();   //cell.getStringCellValue();

        if (mapOztableCol.containsKey(cellValue.trim())) {

          IFiCol ozTableCol = mapOztableCol.get(cellValue.trim());
          colFound = true;
          ozTableCol.setColIndex(cn);
          ozTableCol.setBoExist(true);
        }

        // IMPO 300 sütundan sonrasına bakmadık , uzun bir döngüye girmesin
        // 300 sütundan sonra sütun aramalarını bırakır
        Short lastColIndexForHeaders = getLastColIndexForHeaders();
        if (lastColNumber.equals(lastColIndexForHeaders)) {
          break;
        }
      }

      // Required olan column bulunmamışsa colFound false yapılır
      for (IFiCol iTableCol : listColumns) {

        if (colFound && FiBool.isTrue(iTableCol.getBoRequired()) && FiBool.isFalseOrNull(iTableCol.getBoExist())) {
          colFound = false;
          Loghelper.debugLog(getClass(), "Required Col olmadığı için colfound false a çevrildi");
        }

      }

      //if (rowHeader[0] == null) rowHeader[0] = "";

      if (rowHeader.length > 0) {
        headers = Arrays.asList(rowHeader);
      }

//			headers = headers.stream().map(b -> {
//				if (b != null) return b.trim();
//				return null;
//			}).collect(Collectors.toList());

      // Loghelper.getInstance(getClass()).debug(" headar1 : " + rowHeader[0]);

//			for (OzTableCol ozTableCol :listColumns) {
//
//				if (headers.contains(ozTableCol.getFiHeader().trim())) {
//					//Loghelper.getInstance(getClass()).debug(" Başlık bulundu:" + rowIndexExcel);
//					colFound = true;
//					ozTableCol.setExist(true);
//
//					break;
//				}
//			}

      if (colFound) break;

      // 100 satırdan sonra başlık aramasın
      if (rowIndexExcel == this.lastRowIndexForHeaderSearch) break;

      if (rowIndexExcel.equals(lastRowNumber)) {
        Loghelper.get(getClass()).debug("Son satır erişti başlık yok");
      }


    }

    if (!colFound) return new Pair<>(null, null);

    List<String> finalHeaders = headers;

    listColumns.forEach(entity -> {
      if (finalHeaders.contains(entity.getOfcTxHeader().trim())) {
        entity.setBoEnabled(true);
      }
    });

    //Loghelper.getInstance(getClass()).debug(" Row Headers Length:"+rowHeader.length));
    // not : alternatif yol : for (Row row : sheet)


    // Exceldeki Data Satırları Okunuyor

    Integer lastColNumber = rowHeader.length;              //rowHeaderExcel.getLastCellNum(); // 1 başlayarak, son sütün hücre numarası

    Pair<Integer, List<String>> result = new Pair<>(null, null);

    if (colFound) {
      result = new Pair<>(rowIndexExcel, finalHeaders);
    }

    return result;

  }

  private static Short getLastColIndexForHeaders() {
    Short lastColIndexForHeaders = 300;
    return lastColIndexForHeaders;
  }

  public String getCellStringValueXLS(HSSFCell cell) {

    switch (cell.getCellType()) {
      case XSSFCell.CELL_TYPE_BOOLEAN:
        return String.valueOf(cell.getBooleanCellValue());

      case XSSFCell.CELL_TYPE_NUMERIC:
				/*int i = (int)cell.getNumericCellValue();
				strCellValue = String.valueOf(i);*/
        // XIM araştır format
        double num = cell.getNumericCellValue();

        DecimalFormat pattern = new DecimalFormat("#,#,#,#,#,#,#,#,#,#");
        NumberFormat testNumberFormat = NumberFormat.getNumberInstance();
        String mob = testNumberFormat.format(num);
        Number n = null;
        try {
          n = pattern.parse(mob);
          return String.valueOf(n);
        } catch (ParseException e) {
          Loghelper.debugException(getClass(), e);
          return String.valueOf(cell.getNumericCellValue());
        }

      case XSSFCell.CELL_TYPE_STRING:
        return cell.getStringCellValue();

      case XSSFCell.CELL_TYPE_FORMULA:
        return cell.getRichStringCellValue().getString();

      case XSSFCell.CELL_TYPE_BLANK:
      case XSSFCell.CELL_TYPE_ERROR:
      default:
        return "";

    }

    //return cell.getStringCellValue();
  }

  public String getCellStringValueXLSX(XSSFCell cell) {
    switch (cell.getCellType()) {
      case XSSFCell.CELL_TYPE_BOOLEAN:
        //maprow.put(header, cell.getBooleanCellValue());
        return cell.getRawValue();
      case XSSFCell.CELL_TYPE_NUMERIC:
        return cell.getRawValue();
      case XSSFCell.CELL_TYPE_STRING:
        return cell.getStringCellValue();
      case XSSFCell.CELL_TYPE_BLANK:
      case XSSFCell.CELL_TYPE_FORMULA:
      case XSSFCell.CELL_TYPE_ERROR:
      default:
        return "";
    }
  }

  public String getCellStringValueXls(HSSFCell cell) {

    switch (cell.getCellType()) {
      case HSSFCell.CELL_TYPE_BOOLEAN:
        //maprow.put(header, cell.getBooleanCellValue());
        return String.valueOf(cell.getBooleanCellValue());  // RawValue idi (xlsx)
      case HSSFCell.CELL_TYPE_NUMERIC:
        return String.valueOf(cell.getNumericCellValue()); // rawValue idi
      case HSSFCell.CELL_TYPE_STRING:
        return cell.getStringCellValue();
      case HSSFCell.CELL_TYPE_BLANK:
      case HSSFCell.CELL_TYPE_FORMULA:
      case HSSFCell.CELL_TYPE_ERROR:
      default:
        return "";
    }

  }

  public <E> List<E> bindEntityExcel(List<Map<String, String>> listmapData, List<? extends IFiCol> listColumns, Class<E> entityclass) {

    List<E> list = new ArrayList<>();

    for (Iterator iterator = listmapData.iterator(); iterator.hasNext(); ) {

      Map<String, String> map = (Map<String, String>) iterator.next();

      E entity = null;
      try {
        entity = entityclass.newInstance();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }

      Map<String, FiField> fieldsAsMap = FiReflection.getFieldsAsMap(entityclass);

      for (int i = 0; i < listColumns.size(); i++) {

        IFiCol fiTableCol = listColumns.get(i);

        // Excelden gelen veride bu sütun yoksa atlanır
        if (!map.containsKey(fiTableCol.getOfcTxHeader())) {
          // FIXME bir defa gösterilmeli
          //Loghelper.getInstance(getClass()).debug(" Sütun bulunamadı:" + fiTableCol.getHeader());
          continue;
        }

        try {
          Object cellvalue = map.get(fiTableCol.getOfcTxHeader());

          if (cellvalue != null && ((String) cellvalue).isEmpty()) cellvalue = null;

          String colTypeName = fieldsAsMap.getOrDefault(fiTableCol.getOfcTxFieldName(), new FiField()).getClassNameSimple();
          String colTypeName2 = colTypeName == null ? "" : colTypeName;

          if (fiTableCol.equalsColType(OzColType.Date) || (fiTableCol.getColType() == null && colTypeName2.equals("Date"))) {
            if (cellvalue != null) {
              String strDate = cellvalue.toString();
              cellvalue = FiDate.strToDateGeneric2(strDate);
            }
          }

          if (fiTableCol.equalsColType(OzColType.Integer) || (fiTableCol.getColType() == null && colTypeName2.equals("Integer"))) {
            cellvalue = FiNumber.strToInt(cellvalue);
          }

          if (fiTableCol.equalsColType(OzColType.Double) || (fiTableCol.getColType() == null && colTypeName2.equals("Double"))) {
            cellvalue = FiNumber.strToDouble(cellvalue);
          }

          PropertyUtils.setProperty(entity, fiTableCol.getOfcTxFieldName().trim(), cellvalue);

        } catch (IllegalAccessException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
        }
      }

      if (entity != null) list.add(entity);


    }

    return list;
  }

  public FkbList bindEntityExcelToFkb(List<Map<String, String>> listmapData, List<? extends IFiCol> listColumns) {

    FkbList list = new FkbList();

    for (Iterator iterator = listmapData.iterator(); iterator.hasNext(); ) {

      Map<String, String> map = (Map<String, String>) iterator.next();

      FiKeyBean entity = new FiKeyBean();

      //Map<String, FiField> fieldsAsMap = FiReflection.getFieldsAsMap(entityclass);

      for (int i = 0; i < listColumns.size(); i++) {

        IFiCol fiTableCol = listColumns.get(i);

        // Excelden gelen veride bu sütun yoksa atlanır
//        if (!map.containsKey(fiTableCol.getOfcTxHeader())) {
//          // FIXME bir defa gösterilmeli
//          //Loghelper.getInstance(getClass()).debug(" Sütun bulunamadı:" + fiTableCol.getHeader());
//          continue;
//        }

        try {
          Object cellvalue = map.get(fiTableCol.getOfcTxHeader());

          if (cellvalue != null && ((String) cellvalue).isEmpty()) cellvalue = null;

          //String colTypeName = fieldsAsMap.getOrDefault(fiTableCol.getOfcTxFieldName(), new FiField()).getClassNameSimple();
          //String colTypeName2 = colTypeName == null ? "" : colTypeName;

          if (fiTableCol.equalsColType(OzColType.Date)
            //|| (fiTableCol.getColType() == null && colTypeName2.equals("Date"))
          ) {
            if (cellvalue != null) {
              String strDate = cellvalue.toString();
              cellvalue = FiDate.strToDateGeneric2(strDate);
            }
          }

          if (fiTableCol.equalsColType(OzColType.Integer)
            //|| (fiTableCol.getColType() == null && colTypeName2.equals("Integer"))
          ) {
            cellvalue = FiNumber.strToInt(cellvalue);
          }

          if (fiTableCol.equalsColType(OzColType.Double)
              //|| (fiTableCol.getColType() == null && colTypeName2.equals("Double"))
          ) {
            cellvalue = FiNumber.strToDouble(cellvalue);
          }

          //PropertyUtils.setProperty(entity, fiTableCol.getOfcTxFieldName().trim(), cellvalue);
          entity.add(fiTableCol.getOfcTxFieldName().trim(), cellvalue);

        } catch (Exception e) {
          Loghelper.get(getClass()).debug(FiException.exToErrorLog(e));
        }
      }

      //if (entity != null) list.add(entity);
      list.add(entity);

    }

    return list;
  }


  public FiListKeyString bindExcelToListMapStr(FiListKeyString listmapData, List<? extends IFiCol> listColumns) {

    FiListKeyString fiListMapEntity = new FiListKeyString();

    for (Iterator iterator = listmapData.iterator(); iterator.hasNext(); ) {
      Map<String, String> mapExcelRow = (Map<String, String>) iterator.next();

      FiKeyString fiMapEntity = new FiKeyString();
      for (int i = 0; i < listColumns.size(); i++) {
        IFiCol fiTableCol = listColumns.get(i);
        // Excelden gelen veride bu sütun yoksa atlanır
        if (!mapExcelRow.containsKey(fiTableCol.getOfcTxHeader())) continue;

        String cellvalue = mapExcelRow.get(fiTableCol.getOfcTxHeader());
        if (FiString.isEmpty(cellvalue)) cellvalue = null;
        // Tip Çevrilmesi yapılmadan String olarak Eklendi
        fiMapEntity.put(fiTableCol.getOfcTxFieldName().trim(), cellvalue);
      }
      if (fiMapEntity.size() > 0) fiListMapEntity.add(fiMapEntity);
    }

    return fiListMapEntity;
  }

  public <E> void printExcelList(List<E> listdata, List<IFiCol> columnList, Integer size) {

    System.out.println("");
    System.out.print("Sütünlar :");
    //listmapexcel.get(0).keySet().forEach(s -> System.out.print(s + " : "));
    columnList.forEach(entity -> {
      if (entity.getBoEnabled() != null && entity.getBoEnabled())
        System.out.print(entity.getOfcTxHeader() + " :: ");
    });
    System.out.println("");
    System.out.println("Satır Sayısı : " + listdata.size() + "\n");

    if (size == null) size = listdata.size();

    for (int indexRow = 0; indexRow < size; indexRow++) { // For each

      Object rowent = listdata.get(indexRow);

      System.out.print("Satir:" + (indexRow + 1) + " :");

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
          System.out.print(fieldname + ":" + obj.toString() + " :: ");
        } else {

        }

      }
      System.out.println(" }");

    }

    System.out.println("");

    //AtomicReference<Boolean> first= new AtomicReference<>(true);

//			stringStringMap.keySet().forEach(s -> {
//				if(!first.get()) System.out.print(",");
//				System.out.print( s + ":" + stringStringMap.get(s));
//				first.set(false);
//			});

    //System.out.println("");

    //});


  }

  // old methods

  public List<Map<String, String>> readExcelXLSXtoListMapString(File excel_xlsx_file) {

    FileInputStream file = null;
    XSSFWorkbook workbook = null;

    try {
      file = new FileInputStream(excel_xlsx_file);
      // Get the workbook instance for XLS file
      workbook = new XSSFWorkbook(file);

    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    } catch (IOException e2) {
      e2.printStackTrace();
    }

    // Get first sheet from the workbook
    XSSFSheet sheet = workbook.getSheetAt(0);

    List<Map<String, String>> listrows = new ArrayList<>();

    //Iterator rows = sheet.rowIterator();
    Integer lastRowNumber = sheet.getLastRowNum(); // 1 başlayarak, exceldeki son satır nosu
    String[] rowHeader = null;  // map<int,string> de tanımlabilir

    // ** start header row okunur

    XSSFRow rowHeaderExcel = sheet.getRow(0);
    //rows.hasNext()
    // ilk satır başlık olacak

    if (rowHeaderExcel == null) return listrows;

    //rowHeaderExcel = (XSSFRow) rows.next();
    //Iterator cells = rowHeaderExcel.cellIterator();

    Short lastColNumber = rowHeaderExcel.getLastCellNum(); // 1 başlayarak, son sütün hücre numarası
    //rowHeaderExcel.getLastCellNum()

    rowHeader = new String[lastColNumber];

    //while (cells.hasNext())
    for (int cn = 0; cn < lastColNumber; cn++) {

      XSSFCell cell = rowHeaderExcel.getCell(cn);

      if (cell == null) continue;
      //cell = (XSSFCell) cells.next();

      rowHeader[cn] = cell.getStringCellValue();

      //Loghelper.getInstance(getClass()).debug("header eklendi:" + cell.getStringCellValue());

    }

    //Loghelper.getInstance(getClass()).debug("Row Header Okundu");


    // ** end header row okuma

    //Loghelper.getInstance(getClass()).debug(" Row Headers Length:"+rowHeader.length));

    // alternatif yol
    // for (Row row : sheet) {
    //while (rows.hasNext())

    for (int rowIndex = 1; rowIndex <= lastRowNumber; rowIndex++) {

      //Object[] rowobject = new Object[rowContent.getLastCellNum()];
      //rowContent = (XSSFRow) rows.next();
      //Loghelper.getInstance(getClass()).debug("Read Row:"+ rowIndex);

      XSSFRow rowContent = sheet.getRow(rowIndex);

      if (rowContent == null) continue;

      //Iterator<Cell> iterRowCells = rowContent.cellIterator();

      Map<String, String> maprow = new HashMap<>();

      //while (iterRowCells.hasNext())
      Boolean empty = true;
      for (int cn = 0; cn < lastColNumber; cn++) {   //rowContent.getLastCellNum()

        //Loghelper.getInstance(getClass()).debug(" col:"+cn);

        String header = "";

        if (rowHeader[cn] == null) {
          //Loghelper.getInstance(getClass()).debug(" header cn null"+ cn);
          continue;
        }

        header = rowHeader[cn]; //.toString();

        //if (rowHeader.length > cn) arsiv
        //if(!iterRowCells.hasNext()) arsiv

        XSSFCell cell = rowContent.getCell(cn);

        if (cell == null) {
          //Loghelper.getInstance(getClass()).debug(" header cell null"+ header);
          maprow.put(header, "");
          continue;
        }

        //cell = (XSSFCell) iterRowCells.next();


        switch (cell.getCellType()) {
          case XSSFCell.CELL_TYPE_BOOLEAN:
            //maprow.put(header, cell.getBooleanCellValue());
            maprow.put(header, cell.getRawValue());
            empty = false;
            break;
          case XSSFCell.CELL_TYPE_NUMERIC:
            maprow.put(header, cell.getRawValue());
            empty = false;
            //rowobject[cn] = cell.getNumericCellValue();
            break;
          case XSSFCell.CELL_TYPE_STRING:
            maprow.put(header, cell.getStringCellValue());
            empty = false;
            //rowobject[cn] = cell.getStringCellValue();
            break;
          case XSSFCell.CELL_TYPE_BLANK:
          case XSSFCell.CELL_TYPE_FORMULA:
          case XSSFCell.CELL_TYPE_ERROR:
            //rowobject[cn] = "";
            //break;
          default:
            maprow.put(header, "");
            //rowHeader[cn] = "";

        }

      }

      if (!empty) {
        //Loghelper.getInstance(getClass()).debug("eklendi");
        listrows.add(maprow);
      }

    }


    try {
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return listrows;

  }

  public List<String> readExcelRowIndex(File excelXlsxFile, Integer rowIndexToRead) {

    FileInputStream file = null;
    XSSFWorkbook workbook = null;

    try {
      file = new FileInputStream(excelXlsxFile);
      // Get the workbook instance for XLS file
      workbook = new XSSFWorkbook(file);

    } catch (IOException e2) {
      e2.printStackTrace();
    }
    List<String> listHeaders = new ArrayList<>();

    // Get first sheet from the workbook
    XSSFSheet sheet = workbook.getSheetAt(0);

    XSSFRow rowContent = sheet.getRow(rowIndexToRead); // index nodaki satır okunur (row 0 dan başlıyor)

    Short lastColNumber = rowContent.getLastCellNum(); // 1 başlayarak, son sütün hücre numarası

    //if (rowContent == null) return listHeaders;

    for (int colIndexTemp = 0; colIndexTemp < lastColNumber; colIndexTemp++) {

      XSSFCell cell = rowContent.getCell(colIndexTemp);
      if (cell == null) {
        listHeaders.add("");
      } else {
        listHeaders.add(getCellStringValueXLSX(cell));
      }


    }

    try {
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return listHeaders;

  }

  public Map<String, Integer> convertObjectcol_to_mapcol_lowercase(Object[] arrColrow) {

    Map<String, Integer> mapcol = new HashMap<>();

    for (int j = 0; j < arrColrow.length; j++) {

      Object colobject = arrColrow[j];

      //Loghelper.getInstance(this.getClass()).info("debug:okunan sutun" + colobject.toString());

      if (colobject != null) {
        mapcol.put(colobject.toString().toLowerCase(), j);
      }


    }

    // > sutunların belirlenmesi


    return mapcol;

  }

  // xlsx metodu xls ye çevrildi , numeric için rawValue desteği yoktu. 17-08-2019
  public <E> List<E> readExcelXlsV3(File excelXlsFile, List<? extends IFiCol> listColumns, Class<E> entityclass) {

    // Get first sheet from the workbook
    HSSFSheet sheet = getWorkbookFromExcelXlsFile(excelXlsFile).getSheetAt(0);   //  workbook.getSheetAt(0);

    List<Map<String, String>> listrows = new ArrayList<>();

    //Not Iterator rows = sheet.rowIterator();

    // 1 başlayarak, exceldeki son satır nosu
    Integer lastRowNumber = sheet.getLastRowNum();

    // Pair prm1: headerların bulunduğu satır indexi
    Pair<Integer, List<String>> excelHeadersXLSX = findHeadersInExcel(sheet, listColumns);

    Boolean colHeadersFound = false;

    if (excelHeadersXLSX != null) colHeadersFound = true;

    if (!colHeadersFound) return new ArrayList<>();

    List<String> finalHeaders = excelHeadersXLSX.getValue1();

    listColumns.forEach(entity -> {
      if (finalHeaders.contains(entity.getOfcTxHeader().trim())) {
        entity.setBoEnabled(true);
      }
    });


    // not : alternatif yol : for (Row row : sheet)
    Integer rowIndexExcel = excelHeadersXLSX.getValue0();

    // Exceldeki Data Satırları Okunuyor
    Integer lastColNumber = finalHeaders.size();  //rowHeader.length;              //rowHeaderExcel.getLastCellNum(); // 1 başlayarak, son sütün hücre numarası

    // index = Excel Satır - 1 dir. ( Index 0 dan başlar ) (Excel 1 den başlar)
    // Loghelper.getInstance(getClass()).debug(" Row Index (Header Index)(Satır Sıralaması 0 dan başlar):"+ rowIndexExcel);
    // Loghelper.getInstance(getClass()).debug(" Row Headers Col Length:"+ lastColNumber);

    rowIndexExcel++;

    for (; rowIndexExcel <= lastRowNumber; rowIndexExcel++) {

      HSSFRow rowContent = sheet.getRow(rowIndexExcel);

      if (rowContent == null) continue;

      Map<String, String> maprow = new HashMap<>();

      Boolean empty = true;

      for (int cn = 0; cn < lastColNumber; cn++) {   //rowContent.getLastCellNum()

        //Loghelper.getInstance(getClass()).debug(" col:"+cn);

        String header = "";

        if (finalHeaders.get(cn) == null) {
          //Loghelper.getInstance(getClass()).debug(" header cn null"+ cn);
          continue;
        }

        header = finalHeaders.get(cn).toString();

        //if (rowHeader.length > cn) arsiv
        //if(!iterRowCells.hasNext()) arsiv

        HSSFCell cell = rowContent.getCell(cn);

        if (cell == null) {
          //Loghelper.getInstance(getClass()).debug(" header cell null"+ header);
          maprow.put(header, "");
          continue;
        }

        //cell = (XSSFCell) iterRowCells.next();

        String strCellValue = getCellStringValueXls(cell);
        maprow.put(header, strCellValue);

        if (!strCellValue.equals("")) empty = false;

      }

      if (!empty) {
        //Loghelper.getInstance(getClass()).debug("eklendi");
        listrows.add(maprow);
      }

    }

    if (getFileInputStream() != null) {

      try {
        getFileInputStream().close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return bindEntityExcel(listrows, listColumns, entityclass);

  }

  public FkbList readExcelXlsV3AsFkbList(File excelXlsFile, List<? extends IFiCol> listColumns) {

    // Get first sheet from the workbook
    HSSFSheet sheet = getWorkbookFromExcelXlsFile(excelXlsFile).getSheetAt(0);   //  workbook.getSheetAt(0);

    List<Map<String, String>> listrows = new ArrayList<>();

    //Not Iterator rows = sheet.rowIterator();

    // 1 başlayarak, exceldeki son satır nosu
    Integer lastRowNumber = sheet.getLastRowNum();

    // Pair prm1: headerların bulunduğu satır indexi
    Pair<Integer, List<String>> excelHeadersXLSX = findHeadersInExcel(sheet, listColumns);

    Boolean colHeadersFound = false;

    if (excelHeadersXLSX != null) colHeadersFound = true;

    if (!colHeadersFound) return new FkbList();

    List<String> finalHeaders = excelHeadersXLSX.getValue1();

    listColumns.forEach(entity -> {
      if (finalHeaders.contains(entity.getOfcTxHeader().trim())) {
        entity.setBoEnabled(true);
      }
    });


    // not : alternatif yol : for (Row row : sheet)
    Integer rowIndexExcel = excelHeadersXLSX.getValue0();

    // Exceldeki Data Satırları Okunuyor
    Integer lastColNumber = finalHeaders.size();  //rowHeader.length;              //rowHeaderExcel.getLastCellNum(); // 1 başlayarak, son sütün hücre numarası

    // index = Excel Satır - 1 dir. ( Index 0 dan başlar ) (Excel 1 den başlar)
    // Loghelper.getInstance(getClass()).debug(" Row Index (Header Index)(Satır Sıralaması 0 dan başlar):"+ rowIndexExcel);
    // Loghelper.getInstance(getClass()).debug(" Row Headers Col Length:"+ lastColNumber);

    rowIndexExcel++;

    for (; rowIndexExcel <= lastRowNumber; rowIndexExcel++) {

      HSSFRow rowContent = sheet.getRow(rowIndexExcel);

      if (rowContent == null) continue;

      Map<String, String> maprow = new HashMap<>();

      Boolean empty = true;

      for (int cn = 0; cn < lastColNumber; cn++) {   //rowContent.getLastCellNum()

        //Loghelper.getInstance(getClass()).debug(" col:"+cn);

        String header = "";

        if (finalHeaders.get(cn) == null) {
          //Loghelper.getInstance(getClass()).debug(" header cn null"+ cn);
          continue;
        }

        header = finalHeaders.get(cn).toString();

        //if (rowHeader.length > cn) arsiv
        //if(!iterRowCells.hasNext()) arsiv

        HSSFCell cell = rowContent.getCell(cn);

        if (cell == null) {
          //Loghelper.getInstance(getClass()).debug(" header cell null"+ header);
          maprow.put(header, "");
          continue;
        }

        //cell = (XSSFCell) iterRowCells.next();

        String strCellValue = getCellStringValueXls(cell);
        maprow.put(header, strCellValue);

        if (!strCellValue.equals("")) empty = false;

      }

      if (!empty) {
        //Loghelper.getInstance(getClass()).debug("eklendi");
        listrows.add(maprow);
      }

    }

    if (getFileInputStream() != null) {

      try {
        getFileInputStream().close();
      } catch (IOException e) {
        Loghelper.get(getClass()).debug(FiException.exToErrorLog(e));
      }
    }

    return bindEntityExcelToFkb(listrows, listColumns);
  }

  public List<Object[]> readExcelXlstoListObject(File excelfile) {

    FileInputStream file = null;
    HSSFWorkbook workbook = null;

    try {
      file = new FileInputStream(excelfile);
      // Get the workbook instance for XLS file
      workbook = new HSSFWorkbook(file);

    } catch (IOException ex) {
      Loghelper.get(getClass()).error(FiException.exTosMain(ex));
    }

    // Get first sheet from the workbook
    HSSFSheet sheet = workbook.getSheetAt(0);

    List<Object[]> listrows = new ArrayList<>();

    for (Row row : sheet) {

      Object[] rowobject = new Object[row.getLastCellNum()];

      for (int cn = 0; cn < row.getLastCellNum(); cn++) {

        // If the cell is missing from the file, generate a blank one
        // (Works by specifying a MissingCellPolicy)

        Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK); //Row.CREATE_NULL_AS_BLANK

        switch (cell.getCellType()) {
          case Cell.CELL_TYPE_BOOLEAN:
            rowobject[cn] = cell.getBooleanCellValue();
            break;
          case Cell.CELL_TYPE_NUMERIC:
            rowobject[cn] = cell.getNumericCellValue();
            break;
          case Cell.CELL_TYPE_STRING:
            rowobject[cn] = cell.getStringCellValue();
            break;
          case Cell.CELL_TYPE_BLANK:
          case Cell.CELL_TYPE_FORMULA:
          case Cell.CELL_TYPE_ERROR:
            rowobject[cn] = "";
            break;
        }

        // Print the cell for debugging ( 0:numeric,1:String,2:Formula,3:Blank,4:Boolean,5:Error
        // System.out.print("CELL: "
        // + cn
        // + " --> "
        // + cell.toString()
        // + " --> "
        // + cell.getCellType());
        // Logmain.info("");

      }

      // FIXME : row tümü boşsa ekleme

      listrows.add(rowobject);
    }

    try {
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return listrows;

  }

  public Map<String, Integer> convertObjectcol_to_mapcol_lowercasePANEK(Object[] arrColrow) {

    Map<String, Integer> mapcol = new HashMap<>();

    for (int j = 0; j < arrColrow.length; j++) {

      Object colobject = arrColrow[j];

      Loghelper.get(this.getClass()).info("debug:okunan sutun" + colobject.toString());

      if (colobject != null) {
        mapcol.put(colobject.toString().toLowerCase(), j);
      }


    }

    // > sutunların belirlenmesi
    return mapcol;
  }

  // header lar array çevirilip index ile yapılabilir.
  public List<Map<String, String>> readExcelXLStoListMap(File excelfile) {

    FileInputStream file = null;
    HSSFWorkbook workbook = null;

    try {
      file = new FileInputStream(excelfile);
      // Get the workbook instance for XLS file
      workbook = new HSSFWorkbook(file);

    } catch (FileNotFoundException e1) {
      Loghelper.debugException(getClass(), e1);
      LogListener.setLogMessage("Excel Dosya Bulunamadı.");
    } catch (IOException e2) {
      Loghelper.debugException(getClass(), e2);
      LogListener.setLogMessageAndDetail("IO Hatası Oluştu.", e2);
    }


    List<Map<String, String>> listrows = new ArrayList<>();
    HSSFSheet sheet = null;

    // FIXME sheet boş gelebiliyor
    try {
      // Try to Get first sheet from the workbook
      sheet = workbook.getSheetAt(0);

    } catch (Exception e) {
      Loghelper.get(this.getClass()).error("Hata :" + FiException.exTosMain(e));
      return null;
    }

    Iterator rows = sheet.rowIterator();

    Object[] rowHeader = null;

    HSSFRow row;
    HSSFCell cell;

    // ilk satır başlık olacak
    if (rows.hasNext()) {

      row = (HSSFRow) rows.next();
      Iterator cells = row.cellIterator();

      rowHeader = new Object[row.getLastCellNum()];

      //while (cells.hasNext())
      for (int cn = 0; cn < row.getLastCellNum(); cn++) {
        cell = (HSSFCell) cells.next();

        rowHeader[cn] = cell.getStringCellValue();

				/*switch (cell.getCellType()) {
					case HSSFCell.CELL_TYPE_BOOLEAN:
						rowHeader[cn] = String.valueOf(cell.getBooleanCellValue());
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						rowHeader[cn] = String.valueOf(cell.getNumericCellValue());
						break;
					case XSSFCell.CELL_TYPE_STRING:
						rowHeader[cn] = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						rowHeader[cn] = "";
					case XSSFCell.CELL_TYPE_FORMULA:
					case XSSFCell.CELL_TYPE_ERROR:
						rowHeader[cn] = cell.getRichStringCellValue();
						break;
					default:
						rowHeader[cn] = cell.getRichStringCellValue();
				}*/

      }
    }

    // alternatif yol
    // for (Row row : sheet) {
    while (rows.hasNext()) {

      //Object[] rowobject = new Object[row.getLastCellNum()];

      row = (HSSFRow) rows.next();

      Iterator cells = row.cellIterator();

      Map<String, String> maprow = new HashMap<>();

      Boolean empty = true;

      //while (cells.hasNext())
      for (int cn = 0; cn < row.getLastCellNum(); cn++) {
        cell = (HSSFCell) cells.next();

        String header = "";
        if (rowHeader.length > cn) {
          header = rowHeader[cn].toString();
        } else {
          continue;
        }

        switch (cell.getCellType()) {
          case XSSFCell.CELL_TYPE_BOOLEAN:
            maprow.put(header, String.valueOf(cell.getBooleanCellValue()));
            empty = false;
            break;
          case XSSFCell.CELL_TYPE_NUMERIC:
						/*int i = (int)cell.getNumericCellValue();
						strCellValue = String.valueOf(i);*/
            // XIM araştır format
            double num = cell.getNumericCellValue();
            DecimalFormat pattern = new DecimalFormat("#,#,#,#,#,#,#,#,#,#");
            NumberFormat testNumberFormat = NumberFormat.getNumberInstance();
            String mob = testNumberFormat.format(num);
            Number n = null;
            try {
              n = pattern.parse(mob);
              maprow.put(header, String.valueOf(n));
            } catch (ParseException e) {
              maprow.put(header, String.valueOf(cell.getNumericCellValue()));
              e.printStackTrace();
            }
            //maprow.put(header, String.valueOf(cell.getNumericCellValue()));
            empty = false;
            break;
          case XSSFCell.CELL_TYPE_STRING:
            maprow.put(header, cell.getStringCellValue());
            empty = false;
            break;

          case XSSFCell.CELL_TYPE_FORMULA:
            maprow.put(header, cell.getRichStringCellValue().getString());
            empty = false;

          case XSSFCell.CELL_TYPE_BLANK:
          case XSSFCell.CELL_TYPE_ERROR:
          default:
            maprow.put(header, "");

        }

      }

      if (!empty) listrows.add(maprow);

    }


    try {
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return listrows;

  }

  public static void writeXLSXFile() throws IOException {

    String excelFileName = "C:/Test.xlsx";//name of excel file

    String sheetName = "Sheet1";//name of sheet

    XSSFWorkbook wb = new XSSFWorkbook();
    XSSFSheet sheet = wb.createSheet(sheetName);

    //iterating r number of rows
    for (int r = 0; r < 5; r++) {
      XSSFRow row = sheet.createRow(r);

      //iterating c number of columns
      for (int c = 0; c < 5; c++) {
        XSSFCell cell = row.createCell(c);

        cell.setCellValue("Cell " + r + " " + c);
      }
    }

    FileOutputStream fileOut = new FileOutputStream(excelFileName);

    //write this workbook to an Outputstream.
    wb.write(fileOut);
    fileOut.flush();
    fileOut.close();
  }

  public List<Object[]> readExcelXLSXtoListArray(File excelfile) {

    FileInputStream file = null;
    XSSFWorkbook workbook = null;

    try {
      file = new FileInputStream(excelfile);
      // Get the workbook instance for XLS file
      workbook = new XSSFWorkbook(file);

    } catch (FileNotFoundException e1) {

      e1.printStackTrace();
    } catch (IOException e2) {
      e2.printStackTrace();
    }

    // Get first sheet from the workbook
    XSSFSheet sheet = workbook.getSheetAt(0);

    List<Object[]> listrows = new ArrayList<>();

    XSSFRow row;
    XSSFCell cell;

    Iterator rows = sheet.rowIterator();

    // alternatif yol
    // for (Row row : sheet) {
    while (rows.hasNext()) {

      row = (XSSFRow) rows.next();
      Iterator cells = row.cellIterator();

      Object[] rowobject = new Object[row.getLastCellNum()];

      for (int cn = 0; cn < row.getLastCellNum(); cn++) {
        cell = (XSSFCell) cells.next();

        switch (cell.getCellType()) {
          case XSSFCell.CELL_TYPE_BOOLEAN:
            rowobject[cn] = cell.getBooleanCellValue();
            break;
          case XSSFCell.CELL_TYPE_NUMERIC:
            rowobject[cn] = cell.getNumericCellValue();
            break;
          case XSSFCell.CELL_TYPE_STRING:
            rowobject[cn] = cell.getStringCellValue();
            break;
          case XSSFCell.CELL_TYPE_BLANK:
          case XSSFCell.CELL_TYPE_FORMULA:
          case XSSFCell.CELL_TYPE_ERROR:
            rowobject[cn] = "";
            break;

        }

      }

      //System.out.println();
      listrows.add(rowobject);

    }


    try {
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return listrows;

  }

  public <E> List<E> readExcelXLSX_V1(File excelXlsxFile, List<IFiCol> listColumns, Class<E> entityclass) {

    // Get first sheet from the workbook
    XSSFSheet sheet = getWorkbookFromExcelXlsxFile(excelXlsxFile).getSheetAt(0);   //  workbook.getSheetAt(0);

    List<Map<String, String>> listrows = new ArrayList<>();

    //Not Iterator rows = sheet.rowIterator();

    // 1 başlayarak, exceldeki son satır nosu
    Integer lastRowNumber = sheet.getLastRowNum();

    // Başlıklar bu arrayde tutulacak
    String[] rowHeader = null;  // map<int,string> de tanımlabilir

    // ** start header row okunur
    Integer rowIndexExcel = 0;

    XSSFRow rowHeaderExcel = null; // = sheet.getRow(rowIndexExcel);

    //Loghelper.getInstance(getClass()).debug("Başlık Satırı Aranacak");
    //Loghelper.getInstance(getClass()).debug("Last Row Number:" + lastRowNumber);

    // Eğer sütun isimleri daha alt satırlardaysa bulmaya yarar
    Boolean colFound = false;

    //Loghelper.getInstance(getClass()).debug(" aranan col:" + listColumns.get(0).getHeader());
    List<String> headers = null;

    // Öncelikle Başlıkların olduğu satır aranıyor
    for (; rowIndexExcel <= lastRowNumber; rowIndexExcel++) {

      Loghelper.get(getClass()).debug(" Searching Header Row Number (index+1): " + (rowIndexExcel + 1));

      rowHeaderExcel = sheet.getRow(rowIndexExcel);

      if (rowHeaderExcel == null) continue;

      Short lastColNumber = rowHeaderExcel.getLastCellNum(); // 1 başlayarak, son sütün hücre numarası

      if (lastColNumber == -1 || lastColNumber == 0) continue;

      rowHeader = new String[lastColNumber];

      for (int cn = 0; cn < lastColNumber; cn++) {

        XSSFCell cell = rowHeaderExcel.getCell(cn);
        if (cell == null) continue;
        rowHeader[cn] = getCellStringValueXLSX(cell);   //cell.getStringCellValue();

      }

      if (rowHeader[0] == null) rowHeader[0] = "";

      headers = Arrays.asList(rowHeader);

      headers = headers.stream().map(b -> {
        if (b != null) return b.trim();
        return null;
      }).collect(Collectors.toList());

      // Loghelper.getInstance(getClass()).debug(" headar1 : " + rowHeader[0]);

      // FIXME bütün zorunlu sütunlar aratılabilir
      if (headers.contains(listColumns.get(0).getOfcTxHeader().trim())) {
        //Loghelper.getInstance(getClass()).debug(" Başlık bulundu:" + rowIndexExcel);
        colFound = true;
        break;
      }

      if (listColumns.size() > 1 && headers.contains(listColumns.get(1).getOfcTxHeader().trim())) {
        //Loghelper.getInstance(getClass()).debug(" Başlık bulundu:" + rowIndexExcel);
        colFound = true;
        break;
      }

      if (listColumns.size() > 2 && headers.contains(listColumns.get(2).getOfcTxHeader().trim())) {
        //Loghelper.getInstance(getClass()).debug(" Başlık bulundu:" + rowIndexExcel);
        colFound = true;
        break;
      }

      if (rowIndexExcel.equals(lastRowNumber)) {
        Loghelper.get(getClass()).debug("Son satır erişti başlık yok");
      }

    }

    if (!colFound) return new ArrayList<>();

    List<String> finalHeaders = headers;

    listColumns.forEach(entity -> {
      if (finalHeaders.contains(entity.getOfcTxHeader().trim())) {
        entity.setBoEnabled(true);
      }
    });

    //Loghelper.getInstance(getClass()).debug(" Row Headers Length:"+rowHeader.length));
    // not : alternatif yol : for (Row row : sheet)


    // Exceldeki Data Satırları Okunuyor
    Integer lastColNumber = rowHeader.length;              //rowHeaderExcel.getLastCellNum(); // 1 başlayarak, son sütün hücre numarası

    rowIndexExcel++;
    for (; rowIndexExcel <= lastRowNumber; rowIndexExcel++) {

      //Object[] rowobject = new Object[rowContent.getLastCellNum()];
      //rowContent = (XSSFRow) rows.next();
      //Loghelper.getInstance(getClass()).debug("Read Row:"+ rowIndex);

      XSSFRow rowContent = sheet.getRow(rowIndexExcel);

      if (rowContent == null) continue;

      //Iterator<Cell> iterRowCells = rowContent.cellIterator();

      Map<String, String> maprow = new HashMap<>();

      //while (iterRowCells.hasNext())
      Boolean empty = true;
      for (int cn = 0; cn < lastColNumber; cn++) {   //rowContent.getLastCellNum()

        //Loghelper.getInstance(getClass()).debug(" col:"+cn);

        String header = "";

        if (rowHeader[cn] == null) {
          //Loghelper.getInstance(getClass()).debug(" header cn null"+ cn);
          continue;
        }

        header = rowHeader[cn].toString();

        //if (rowHeader.length > cn) arsiv
        //if(!iterRowCells.hasNext()) arsiv

        XSSFCell cell = rowContent.getCell(cn);

        if (cell == null) {
          //Loghelper.getInstance(getClass()).debug(" header cell null"+ header);
          maprow.put(header, "");
          continue;
        }

        //cell = (XSSFCell) iterRowCells.next();

        String strCellValue = getCellStringValueXLSX(cell);
        maprow.put(header, strCellValue);

        if (!strCellValue.equals("")) empty = false;


//				switch (cell.getCellType()) {
//					case XSSFCell.CELL_TYPE_BOOLEAN:
//						//maprow.put(header, cell.getBooleanCellValue());
//						maprow.put(header, cell.getRawValue());
//						empty = false;
//						break;
//					case XSSFCell.CELL_TYPE_NUMERIC:
//						maprow.put(header, cell.getRawValue());
//						empty = false;
//						//rowobject[cn] = cell.getNumericCellValue();
//						break;
//					case XSSFCell.CELL_TYPE_STRING:
//						maprow.put(header, cell.getStringCellValue());
//						empty = false;
//						//rowobject[cn] = cell.getStringCellValue();
//						break;
//					case XSSFCell.CELL_TYPE_BLANK:
//					case XSSFCell.CELL_TYPE_FORMULA:
//					case XSSFCell.CELL_TYPE_ERROR:
//						//rowobject[cn] = "";
//						//break;
//					default:
//						maprow.put(header, "");
//						//rowHeader[cn] = "";
//				}

      }

      if (!empty) {
        //Loghelper.getInstance(getClass()).debug("eklendi");
        listrows.add(maprow);
      }

    }

    if (getFileInputStream() != null) {

      try {
        getFileInputStream().close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return bindEntityExcel(listrows, listColumns, entityclass);

  }

  public static <PrmEntClazz> void saveSablonExcelByClass(IFiModCont iFxModCont, List<PrmEntClazz> listSampleData
      , Class<PrmEntClazz> clazzForAutoComment, String appDir) {

    List<FiField> listFiFieldsSummary = FiReflectClass.getListFieldsWoutStatic(clazzForAutoComment);

    List<FiCol> fiTableColList = FiCol.convertListFiField(listFiFieldsSummary);

    saveAndOpenSablonExcel(iFxModCont, fiTableColList, listSampleData, clazzForAutoComment, appDir);

  }

  public static <PrmEntClazz> void saveAndOpenSablonExcel(IFiModCont iFiModCont, List<FiCol> listExcelColums, List<PrmEntClazz> listSampleData, Class<PrmEntClazz> clazzForAutoComment, String appDir) {

    if (clazzForAutoComment != null) {
      IFiColHelper.autoComment(listExcelColums, clazzForAutoComment);
    }

    String basepath = FiWinUtils.getUserDirOrDesktopDir();

    String excelfilename = "Sablon-" + iFiModCont.getModuleLabel() + "-" + FiDate.datetoString_timestampt2(new Date()) + ".xlsx";

    Path path = Paths.get(String.format("%s\\%s\\", basepath, appDir));

    if (!Files.exists(path)) {
      new File(path.toString()).mkdirs();
    }

    path = Paths.get(String.format("%s\\%s\\%s", basepath, appDir, excelfilename));

    //Loghelper2.getInstance(getClass()).debug(" path:" + path.toString());

    if (listSampleData == null) listSampleData = new ArrayList();

    try {
      FiExcel2.writeListDataToExcelWithComment(listSampleData, listExcelColums, path);
    } catch (IOException e) {
      Loghelper.get(getClassi()).error(FiException.exTosMain(e));
    }

    File file = new File(path.toString());
    new FiExcel2().openExcelFileWithApp(file);
  }

  private static Class<FiExcel> getClassi() {
    return FiExcel.class;
  }


}


//		// ilk satır başlık olacak
//		if (rows.hasNext()) {
//
//			row = (HSSFRow) rows.next();
//			Iterator cells = row.cellIterator();
//
//			rowHeader = new Object[row.getLastCellNum()];
//
//			//while (cells.hasNext())
//			for (int cn = 0; cn < row.getLastCellNum(); cn++) {
//				cell = (HSSFCell) cells.next();
//
//				rowHeader[cn] = cell.getStringCellValue();
//
//				/*switch (cell.getCellType()) {
//					case HSSFCell.CELL_TYPE_BOOLEAN:
//						rowHeader[cn] = String.valueOf(cell.getBooleanCellValue());
//						break;
//					case XSSFCell.CELL_TYPE_NUMERIC:
//						rowHeader[cn] = String.valueOf(cell.getNumericCellValue());
//						break;
//					case XSSFCell.CELL_TYPE_STRING:
//						rowHeader[cn] = cell.getStringCellValue();
//						break;
//					case XSSFCell.CELL_TYPE_BLANK:
//						rowHeader[cn] = "";
//					case XSSFCell.CELL_TYPE_FORMULA:
//					case XSSFCell.CELL_TYPE_ERROR:
//						rowHeader[cn] = cell.getRichStringCellValue();
//						break;
//					default:
//						rowHeader[cn] = cell.getRichStringCellValue();
//				}*/
//
//			}
//		}


// alternatif yol
// for (Row row : sheet) {
//		while (rows.hasNext()) {
//
//			//Object[] rowobject = new Object[row.getLastCellNum()];

//			row = (HSSFRow) rows.next();
//
//			Iterator cells = row.cellIterator();
//
//			Map<String, String> maprow = new HashMap<>();
//
//			Boolean empty = true;
//
//			//while (cells.hasNext())
//			for (int cn = 0; cn < row.getLastCellNum(); cn++) {
//				cell = (HSSFCell) cells.next();
//
//				String header = "";
//				if (rowHeader.length > cn) {
//					header = rowHeader[cn].toString();
//				} else {
//					continue;
//				}
//
//				switch (cell.getCellType()) {
//					case XSSFCell.CELL_TYPE_BOOLEAN:
//						maprow.put(header, String.valueOf(cell.getBooleanCellValue()));
//						empty = false;
//						break;
//					case XSSFCell.CELL_TYPE_NUMERIC:
//						/*int i = (int)cell.getNumericCellValue();
//						strCellValue = String.valueOf(i);*/
//						// XIM araştır format
//						double num = cell.getNumericCellValue();
//						DecimalFormat pattern = new DecimalFormat("#,#,#,#,#,#,#,#,#,#");
//						NumberFormat testNumberFormat = NumberFormat.getNumberInstance();
//						String mob = testNumberFormat.format(num);
//						Number n = null;
//						try {
//							n = pattern.parse(mob);
//							maprow.put(header, String.valueOf(n));
//						} catch (ParseException e) {
//							maprow.put(header, String.valueOf(cell.getNumericCellValue()));
//							e.printStackTrace();
//						}
//						//maprow.put(header, String.valueOf(cell.getNumericCellValue()));
//						empty = false;
//						break;
//					case XSSFCell.CELL_TYPE_STRING:
//						maprow.put(header, cell.getStringCellValue());
//						empty = false;
//						break;
//
//					case XSSFCell.CELL_TYPE_FORMULA:
//						maprow.put(header, cell.getRichStringCellValue().getString());
//						empty = false;
//
//					case XSSFCell.CELL_TYPE_BLANK:
//					case XSSFCell.CELL_TYPE_ERROR:
//					default:
//						maprow.put(header, "");
//
//				}
//
//			}
//
//			if (!empty) listrows.add(maprow);
//
//		}