package com.ruoyi.rva.framework.util;

import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The summery of the class.
 *
 * @author cailei
 * @version 1.0
 */
@Slf4j
public class RvaExcelUtil {
    /**
     * 在excel中，有可能你写入的整数变成了1.23e10的格式，需要将其转化为整数
     *
     * @param cellVal 不允许为空
     * @return
     */
    public static Integer getInt(Object cellVal) {
        return Double.valueOf(cellVal.toString()).intValue();
    }

    /**
     * 在excel中，有可能你写入的整数变成了1.23e10的格式，需要将其转化为整数
     *
     * @param cellVal
     * @return
     */
    public static Long getLong(Object cellVal) {
        return Double.valueOf(cellVal.toString()).longValue();
    }

    /**
     * @param cellVal
     * @return
     */
    public static Double getDouble(Object cellVal) {
        return Double.valueOf(cellVal.toString());
    }

    /**
     * @param fileContent
     * @return
     */
    @SneakyThrows
    public static Workbook getBookByMultipartFile(MultipartFile fileContent) {
        return getBookByInputStream(fileContent.getInputStream(), fileContent.getOriginalFilename().endsWith(".xlsx"));
    }

    /**
     * @param inputStream
     * @param xlsx
     * @return
     */
    public static Workbook getBookByInputStream(InputStream inputStream, Boolean xlsx) {
        Workbook book = null;
        try {
            if (xlsx) {
                book = new XSSFWorkbook(inputStream);
            } else {
                book = new HSSFWorkbook(inputStream);
            }
        } catch (Exception e) {
            RvaUtils.throwRuntimeException("excel解析异常！", xlsx ? "xlsx" : "xls", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {

            }
        }
        return book;
    }

    /**
     * IFileTransportService根据url获取excel文件.
     *
     * @param url 文件相对路径.
     * @return public static Workbook getBookByUrl(String url) { if (url == null ||
     *         url.equals("")) { throw new VPCException("comm-no-upload-file"); }
     *         IFileTransportService fts = WoKit.getFileTransportService(); Workbook
     *         book = null; InputStream is = null; try { is =
     *         fts.getInputStream(url); if (url.endsWith(".xlsx")) { book = new
     *         XSSFWorkbook(is); } else { book = new HSSFWorkbook(is); } } catch
     *         (Exception e) { throw new VPCException(e, "comm-excel-parse", url); }
     *         finally { try { is.close(); } catch (Exception e) { } } return book;
     *         }
     */

    /**
     * @param book
     * @param sheetName
     */
    public static void deleteSheet(Workbook book, String sheetName) {
        for (int i = 0; i < book.getNumberOfSheets(); i++) {
            if (book.getSheetName(i).equals(sheetName)) {
                book.removeSheetAt(i);
                return;
            }
        }
    }

    /**
     * 将sheet中第0行的值作为key值，其他行作为数据行，读出所有数据。
     *
     * @param sh
     * @return
     */
    public static List<Map<String, Object>> getData(Sheet sh) {
        return getData(sh, 0, -1, false);
    }

    /**
     * 将sheet中第0行的值作为key值，其他行作为数据行，读出所有数据。
     *
     * @param sh
     * @return
     */
    public static List<Map<String, Object>> getDataWithColumnIndex(Sheet sh) {
        return getData(sh, 0, -1, true);
    }

    /**
     * 将excel第一个sheet中第0行的值作为key值，其他行作为数据行，读出所有数据。
     *
     * @param sh
     * @return public static List<Map<String, Object>> getData(String excelName) {
     *         Workbook b = ExcelUtil.getBookByName(excelName); return
     *         getData(b.getSheetAt(0)); }
     */

    /**
     * 将excel第一个sheet中第0行的值作为key值，其他行作为数据行，读出所有数据。
     *
     * @param sh
     * @return public static List<Map<String, Object>> getDataWithColumnIndex(
     * String excelName) { Workbook b = ExcelUtil.getBookByName(excelName);
     * return getDataWithColumnIndex(b.getSheetAt(0)); }
     */
    private static final String COLUMN_INDEX = "MOS_COLUMN_INDEX";

    /**
     * @param m
     * @param columnName
     * @return
     */
    public static Integer getColumnIndex(Map<String, Object> m, String columnName) {
        Map<String, Object> colIndex = (Map<String, Object>) m.get(COLUMN_INDEX);
        return (Integer) colIndex.get(columnName);
    }

    public static void each (Map<String, Object> m, EachColumn eachColumn) {
        m.keySet().forEach(key -> {
            if (COLUMN_INDEX.equals(key)) {
                return;
            }
            Integer index = getColumnIndex(m, key);
            eachColumn.each(index, key, m.get(key));
        });
    }

    /**
     * @param name
     * @return
     */
    public static Boolean isColumnName(String name) {
        if (name.equals(COLUMN_INDEX)) {
            return false;
        }
        return true;
    }

    /**
     * @param sh
     * @param startRow
     * @param endRow
     * @return
     */
    public static List<Map<String, Object>> getData(Sheet sh, int startRow, int endRow) {
        return getData(sh, startRow, endRow, false);
    }

    /**
     * 将sheet中startRow行的值作为key值，其他行作为数据行，读出所有数据。
     *
     * @param sh
     * @param startRow
     * @param endRow
     * @param hasColumnIndex
     * @return
     */
    public static List<Map<String, Object>> getData(Sheet sh, int startRow, int endRow, final Boolean hasColumnIndex) {
        if (endRow == -1) {
            endRow = sh.getLastRowNum();
        }
        if (endRow - startRow < 0) {
            RvaUtils.throwRuntimeException("sheet页没有数据！", sh.getSheetName());
        }
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Row header = sh.getRow(startRow);
        for (int i = startRow + 1; i <= endRow; i++) {
            Row r = sh.getRow(i);
            if (r == null) {
                log.info("Read the end of the sheet " + sh.getSheetName() + ", row:" + i);
                break;
            }
            Map<String, Object> m = new HashMap<String, Object>();
            Map<String, Object> colIndex = new HashMap<String, Object>();
            if (hasColumnIndex) {
                m.put(COLUMN_INDEX, colIndex);
            }
            for (int j = 0; j < header.getLastCellNum(); j++) {
                Cell headerCell = header.getCell(j);
                if (headerCell == null) {
                    continue;
                }
                m.put(header.getCell(j).getStringCellValue(), getCellValue(r.getCell(j), null));
                colIndex.put(header.getCell(j).getStringCellValue(), j);
            }
            data.add(m);
        }
        return data;
    }

    /**
     * @param key
     * @return public static Workbook getBookByName(String name) { log.info("Excel
     *         name:" + name); File base = null; if
     *         (!WoKit.getBasePath().equals("")) { base = new
     *         File(WoKit.getBasePath()); } else { base = new File("."); } File f =
     *         WoKit.findFileByName(base, name); if (f == null) { log.warn("File " +
     *         name + " doesn't exist."); return null; } log.info("file absolute
     *         path:" + f.getAbsolutePath()); if (!f.isFile() || !f.exists()) {
     *         return null; } return getBook(f); }
     */

    /**
     * @param f
     * @return
     */
    public static Workbook getBook(File f) {
        Workbook book = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            if (f.getName().endsWith(".xlsx")) {
                book = new XSSFWorkbook(fis);
            } else {
                book = new HSSFWorkbook(fis);
            }
        } catch (Exception e) {
            RvaUtils.throwRuntimeException("excel解析异常！", f.getAbsolutePath());
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return book;
    }

    /**
     * @param cell
     */
    public static void validateEmpty(Cell cell) {
        if (cell == null || isEmpty(cell)) {
            RvaUtils.throwRuntimeException("单元格式异常！", new Object[]{cell.getRowIndex() + 1, cell.getColumnIndex() + 1});
        }
    }

    /**
     * @param cell
     * @return
     */
    public static Boolean isEmpty(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BOOLEAN
                || (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equals(""))) {
            return true;
        }
        return false;
    }

    /**
     * @param cell
     */
    public static void validateNumericCell(Cell cell) {
        if (cell.getCellType() != CellType.NUMERIC || RvaUtils.isEmpty(cell.getStringCellValue())) {
            RvaUtils.throwRuntimeException("单元格式异常！",
                    new Object[]{cell.getRowIndex() + 1, cell.getColumnIndex() + 1});
        }
    }

    /**
     * @param cell
     * @return
     */
    public static Object getCellValue(Cell cell, Object defaultVal) {
        if (isEmpty(cell)) {
            return defaultVal;
        }
        Object val = null;
        switch (cell.getCellType()) {

            case BOOLEAN:
                val = cell.getBooleanCellValue();
                break;
            case ERROR:
            case BLANK:
                val = defaultVal;
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    val = RvaDateUtils.getDatetime(cell.getDateCellValue());
                } else {
                    val = cell.getNumericCellValue();
                }
                break;
            case FORMULA:
                val = cell.getCellFormula();
                break;
            default:
                val = cell.getStringCellValue();
                break;
        }
        return val;
    }

    /**
     * @param cell
     * @param defaultVal
     * @return
     */
    public static Integer getIntValue(Cell cell, Integer defaultVal) {
        Object val = getCellValue(cell, defaultVal);
        return RvaUtils.getInt(val.toString(), defaultVal);
    }

    /**
     * @param cell
     * @param defaultVal
     * @return
     */
    public static Double getDoubleValue(Cell cell, Double defaultVal) {
        Object val = getCellValue(cell, defaultVal);
        return RvaUtils.getDouble(val.toString(), defaultVal);
    }

    /**
     * @param cell
     * @param defaultVal
     * @return
     */
    public static String getStringValue(Cell cell, String defaultVal) {
        Object val = getCellValue(cell, defaultVal);
        if (val == null) {
            return null;
        }
        return val.toString();
    }

    /**
     * 将sheet中第rowStartNum行至rowEndNum行的数据替换为listData中的数据.如果listData中有标题行，
     * 则将标题行数据替换第rowStartNum行，并且格式与其保持一致，其他数据行替换第rowStartNum+1行及其之后的行，并且格式与其保持一致
     */
    public static void insertSheetContent(Sheet sheet, int rowStartNum, List<Object[]> listData) {
        // create content by listData
        for (int i = 0; i < listData.size(); i++) {
            Object[] rowData = listData.get(i);
            // create grid content by listData.get(i)
            createRowByTmpl(sheet, rowStartNum, rowStartNum, listData.get(i));
        }
    }

    /**
     * 将sheet中第rowStartNum行至rowEndNum行的数据替换为listData中的数据.如果listData中有标题行，
     * 则将标题行数据替换第rowStartNum行，并且格式与其保持一致，其他数据行替换第rowStartNum+1行及其之后的行，并且格式与其保持一致
     */
    public static void replaceSheetContent(Sheet sheet, int rowStartNum, int rowEndNum, List<Object[]> listData,
                                           boolean hasHeader) {
        log.info("rowStartNum:" + rowStartNum + ",rowEndNum:" + rowEndNum);
        if (rowStartNum > rowEndNum) {
            log.warn("replaceSheetContent rowStartNum > rowEndNum!");
            rowEndNum = rowStartNum - 1;
            // return;
        }
        if (listData == null || listData.size() == 0) {
            if (hasHeader) {
                // delete rows between rowStartNum and rowEndNum
                if ((rowStartNum + 1) <= rowEndNum) {
                    delRows(sheet, rowStartNum + 1, rowEndNum);
                }
            } else {
                // delete rows between rowStartNum and rowEndNum
                delRows(sheet, rowStartNum, rowEndNum);
            }
            return;
        }
        int dataIndex = 0;

        // create header by listData.get(0) if hasHeader
        if (hasHeader) {
            createRowByTmpl(sheet, rowStartNum, rowEndNum + 1, listData.get(0));
            dataIndex = 1;
        }
        // remove content of row between rowStartNum + dataIndex and rowEndNum
        for (int i = rowStartNum + dataIndex + 1; i <= rowEndNum; i++) {
            sheet.removeRow(sheet.getRow(i));
        }
        // create content by listData
        for (int i = dataIndex; i < listData.size(); i++) {
            Object[] rowData = listData.get(i);
            // create grid content by listData.get(i)
            createRowByTmpl(sheet, rowStartNum + dataIndex, rowEndNum + i + 1, rowData);
        }
        // delete rows between rowStartNum and rowEndNum
        delRows(sheet, rowStartNum, rowEndNum);
    }

    /**
     * 将sheet中第rowStartNum行之后的数据替换为listData中的数据.如果listData中有标题行，
     * 则将标题行数据替换第rowStartNum行，并且格式与其保持一致，其他数据行替换第rowStartNum+1行及其之后的行，并且格式与其保持一致
     */
    public static void replaceSheetContent(Sheet sheet, int rowStartNum, List<Object[]> listData, boolean hasHeader) {
        replaceSheetContent(sheet, rowStartNum, sheet.getLastRowNum(), listData, hasHeader);
    }

    /**
     * @param sheet
     * @param rowStartNum 标题行,大于rowStartNum的行均为数据。
     * @param mapData
     */
    public static void replaceSheetContent(Sheet sheet, int rowStartNum, List<Map<String, Object>> mapData) {
        List<Object[]> listData = new ArrayList<Object[]>();
        Row header = sheet.getRow(rowStartNum);
        for (Map<String, Object> m : mapData) {
            Object[] r = new Object[header.getLastCellNum()];
            for (int i = 0; i < header.getLastCellNum(); i++) {
                String key = header.getCell(i).getStringCellValue();
                r[i] = m.get(key);
            }
            listData.add(r);
        }
        replaceSheetContent(sheet, rowStartNum + 1, sheet.getLastRowNum(), listData, false);
    }

    public static void delRows(Sheet sheet, int start, int end) {
        if (start > end) {
            log.warn("delRows,sheet:" + sheet.getSheetName() + ", start > end, start:" + start + ",end:" + end);
            return;
        }
        if (end < sheet.getLastRowNum()) {
            sheet.shiftRows(end + 1, sheet.getLastRowNum(), start - end - 1);
        } else if (end == sheet.getLastRowNum()) {
            for (int i = start; i <= end; i++) {
                sheet.removeRow(sheet.getRow(i));
            }
        } else {
            log.info("No rows delete for sheet " + sheet.getSheetName() + ",end:" + end + ",lastRow:"
                    + sheet.getLastRowNum());
        }
    }

    private static void createRowByTmpl(Sheet sheet, int rowTmpl, int newRowNum, Object[] rowData) {
        log.info("createRowByTmpl,sheet:" + sheet.getSheetName() + ",rowTmpl:" + rowTmpl + ",newRowNum:" + newRowNum);
        int lastRowNum = sheet.getLastRowNum();
        if (newRowNum <= lastRowNum) {
            if (rowTmpl >= newRowNum && rowTmpl <= lastRowNum) {
                rowTmpl++;
            }
            sheet.shiftRows(newRowNum, lastRowNum, 1);
            lastRowNum++;
        }
        Row rNew = sheet.createRow(newRowNum);
        if (rowTmpl > lastRowNum) {
            for (int i = 0; i < rowData.length; i++) {
                Cell c = rNew.createCell(i);
                setCellValue(c, c.getCellType(), rowData[i]);
            }
            log.info("rowTmpl > lastRowNum, rowTmpl:" + rowTmpl + "lastRowNum:" + lastRowNum);
            return;
        }
        Row rTmpl = sheet.getRow(rowTmpl);
        int lastCellNum = rTmpl.getLastCellNum();
        Cell cTmpl = null;
        for (int i = 0; i < rowData.length; i++) {
            Cell c = rNew.createCell(i);
            if (i < lastCellNum) {
                cTmpl = rTmpl.getCell(i);
            }
            log.info("data value:" + rowData[i] + ", tmpl cell value:" + getCellValue(cTmpl, null));
            c.setCellStyle(cTmpl.getCellStyle());
            c.setCellType(cTmpl.getCellType());
            if (cTmpl.getCellType() == CellType.FORMULA) {
                c.setCellFormula(getFormula(cTmpl.getCellFormula(), c.getRowIndex()));
            } else {
                setCellValue(c, cTmpl.getCellType(), rowData[i]);
            }
        }
    }

    public static void setCellValue(Cell cell, CellType cellType, Object val) {
        if (cell == null) {
            RvaUtils.throwRuntimeException("excel单元格为空！");
        }
        if (RvaUtils.isEmpty(val)) {
            return;
        }
        cell.setCellType(cellType);
        switch (cellType) {
            case BOOLEAN:
                cell.setCellValue("1".equals(val.toString()) || Boolean.valueOf(val.toString()));
                break;
            case NUMERIC:// excel内部，日期也是整个类型
                if (val instanceof Date) {
                    cell.setCellValue((Date) val);
                } else {
                    cell.setCellValue(Double.parseDouble(val.toString()));
                }
                break;
            default:
                cell.setCellValue(val.toString());
                break;
        }
    }

    private static Sheet cloneSheet(Sheet srcSheet, Workbook destBook) {
        Sheet destSheet = destBook.createSheet(srcSheet.getSheetName());
        for (int i = 0; i <= srcSheet.getLastRowNum(); i++) {
            log.info("i - " + i);
            Row srcRow = srcSheet.getRow(i);
            if (srcRow == null) {
                log.info("continue row");
                continue;
            }
            Row r = destSheet.createRow(i);
            r.setHeight(srcRow.getHeight());
            for (int j = 0; j <= srcRow.getLastCellNum(); j++) {
                log.info("j - " + j);
                Cell srcCell = srcRow.getCell(j);
                if (srcCell == null) {
                    log.info("continue cell");
                    continue;
                }
                Cell c = r.createCell(j);
                // c.getCellStyle().
                cloneCell(srcCell, c);
                if (i == 0) {
                    destSheet.setColumnWidth(j, srcSheet.getColumnWidth(j));
                }
            }
        }
        return destSheet;
    }

    private static void cloneCell(Cell src, Cell dest) {
        dest.setCellType(src.getCellType());
        dest.setCellComment(src.getCellComment());
        CellStyle destCS = dest.getCellStyle();
        destCS.setFillForegroundColor(src.getCellStyle().getFillForegroundColor());
        dest.setCellStyle(destCS);
        // dest.setHyperlink(src.getHyperlink());
        switch (src.getCellType()) {
            case BOOLEAN:
                dest.setCellValue(src.getBooleanCellValue());
                break;
            case NUMERIC:
                dest.setCellValue(src.getNumericCellValue());
                break;
            case FORMULA:
                dest.setCellFormula(src.getCellFormula());
                break;
            case ERROR:
                dest.setCellValue(src.getErrorCellValue());
                break;
            case BLANK:
                break;
            default:
                dest.setCellValue(src.getStringCellValue());
                break;
        }
    }

    /**
     * @param data
     * @param col
     * @param val
     * @return
     */
    public static Map<String, Object> getRowByColumnValue(List<Map<String, Object>> data, String col, Object val) {
        if (data == null || data.size() == 0) {
            return null;
        }
        for (Map<String, Object> r : data) {
            Object rowVal = r.get(col);
            if (rowVal == null) {
                if (val == null) {
                    return r;
                }
            } else {
                if (val != null) {
                    if (rowVal.toString().equals(val.toString())) {
                        return r;
                    }
                }

            }
        }
        return null;
    }

    /**
     * @param src
     * @param dest
     */
    public static void cloneBook(Workbook src, Workbook dest) {
        for (int i = 0; i < src.getNumberOfSheets(); i++) {
            cloneSheet(src.getSheetAt(i), dest);
        }
    }

    private static void test() throws FileNotFoundException, IOException {
        Workbook b = RvaExcelUtil.getBook(new File("d:/1.xlsx"));
        Sheet s = b.getSheetAt(0);
        System.out.println("numeric:" + CellType.NUMERIC);
        System.out.println("blank:" + CellType.BLANK);
        System.out.println("string:" + CellType.STRING);
        createRowByTmpl(s, 1, 2, new Object[]{"xxx", 1.2, 2.3});
        b.write(new FileOutputStream(new File("d:/2.xlsx")));
    }

    /**
     * 将excel写入文件.
     *
     * @param book
     * @param f
     */
    public static void write(Workbook book, File f) {
        if (f == null) {
            RvaUtils.throwRuntimeException("excel写入文件异常！");
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(f);
            book.write(out);
        } catch (Exception e) {
            RvaUtils.throwRuntimeException("excel写入失败！", f.getAbsolutePath(), e);
        } finally {
            try {
                out.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 根据srcRow行的公式srcFormula，生成targetRow行的公式
     *
     * @param srcFormula 源行里的公式，仅支持本行中单元格的公式
     * @param targetRow  0开始的目标行号
     * @return
     */
    public static String getFormula(String srcFormula, int targetRow) {
        String cellAddressRegex = "[A-Z]?[A-Z][0-9]";
        Pattern p = Pattern.compile(cellAddressRegex);
        Matcher matcher = p.matcher(srcFormula);
        String regex = "";
        while (matcher.find()) {
            regex += "(.*)(" + matcher.group(0) + ")";
        }
        p = Pattern.compile(regex + "(.*)");
        matcher = p.matcher(srcFormula);
        matcher.find();
        String resultString = "";
        for (int i = 0; i < matcher.groupCount(); i++) {
            String string = matcher.group(i + 1);
            if (checkRegex(cellAddressRegex, string)) {
                resultString += string.split("[0-9]+")[0] + (targetRow + 1);
            } else {
                resultString += string;
            }
        }
        return resultString;
    }

    /**
     * 获取Excel图片
     *
     * @param sheet    当前sheet对象
     * @param workbook 工作簿对象
     * @return Map key:图片单元格索引（1_1）String，value:图片流PictureData
     */
    public static Map<String, PictureData> getSheetPictures(Sheet sheet, Workbook workbook) {

        boolean isXSSFWorkbook = !(workbook instanceof HSSFWorkbook);

        Map<String, PictureData> pictures;
        if (isXSSFWorkbook) {
            pictures = ExcelUtil.getSheetPictures07((XSSFSheet) sheet, (XSSFWorkbook) workbook);
        } else {
            pictures = ExcelUtil.getSheetPictures03((HSSFSheet) sheet, (HSSFWorkbook) workbook);
        }
        return pictures;
    }


    public static boolean checkRegex(String regex, String value) {
        try {
            Pattern p = Pattern.compile(regex);
            Matcher matcher = p.matcher(value);
            return matcher.matches();
        } catch (Exception e) {
            log.error(String.format("regex:%s,value:%s", regex, value), e);
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        String path = "E:\\dashihaina\\draft\\role_1647415861776.xlsx";


        InputStream is = new FileInputStream(path);
        Workbook wb = WorkbookFactory.create(is);

        Sheet sheet = wb.getSheetAt(0);
        Map<String, PictureData> pictures = RvaExcelUtil.getSheetPictures(sheet, wb);
        Row row = sheet.getRow(1);
        Cell cell = row.getCell(8);
//        XSSFSheet xssfSheet = (XSSFSheet) sheet;
//        for (POIXMLDocumentPart dr : xssfSheet.getRelations()) {
//            if ("/xl/embeddings/oleObject1.bin".equals(dr.getPackagePart().getPartName().getURI().toString())) {//视频文件
//                try {
//                    PackagePart packagePart = dr.getPackagePart();
//                    InputStream inputStream = packagePart.getInputStream();
//                    FileOutputStream fileOutputStream = new FileOutputStream("E:/dashihaina/draft/movie.mp4");
//
//
//                    fileOutputStream.write(new byte[inputStream.available()]);
//                    fileOutputStream.flush();
//                    fileOutputStream.close();
//
//                    inputStream.close();
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }


        List<? extends PictureData> allPictures = wb.getAllPictures();
//        pictures.values().forEach(p -> {
//            byte[] data = p.getData();
//            try {
//                String   val = FileUtils.writeBytes(data, "E:\\dashihaina\\draft");
//                log.info("path==========",val);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        });
        log.info("cell", cell);

        log.info(getFormula("IF(L2<20,\\\"不合格\\\",IF(L2<30,\\\"合格\\\",\\\"优秀\\\"))", 5));
        // Matcher matcher = p.matcher("IF(L2<20,\"不合格\",IF(L2<30,\"合格\",\"优秀\"))");
        log.info("I22".split("[0-9]+")[0]);
    }
    public static Workbook getBookByUrl(String downloadPath) {
        Workbook book = null;
        InputStream fis = null;
        try {
            URL url = new URL(downloadPath);
            fis = url.openStream();
            String file = url.getFile();
            if (file.endsWith(".xlsx")) {
                book = new XSSFWorkbook(fis);
            } else {
                book = new HSSFWorkbook(fis);
            }
        } catch (Exception e) {
            RvaUtils.throwRuntimeException("excel解析异常！", downloadPath);
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return book;
    }
    public static interface EachColumn {

        void each (Integer colIndex, String key, Object val);
    }
}
