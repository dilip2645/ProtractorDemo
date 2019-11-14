package com.mycompany.myapp.common;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class XLSGeneretorLocal {

	private FileUtility fileUtil;
	
	@Autowired
    public XLSGeneretorLocal(FileUtility fileUtilityLocal) {
		super();
		this.fileUtil = fileUtilityLocal;
	}
	
	/**
     * This method generates XLS file with given report data and report headers
     * 
     * @param data
     *            JSON array of Report data
     * @param headers
     *            report header name
     * @param propertyFileName
     *            property file name
     * @param workbookName
     *            workbook name
     * @return xlsFile returns generated XLS file
     * @throws FileNotFoundException
     * @throws IOException
     * @throws JSONException
     * @throws CustomException
     * 
     */

    public File createExcel(
            JSONArray data,
            Map<String, String> headers,
            String fileName,
            String woorkbookName) throws CustomException, IOException {

        int rowCount = 0;
        Row row;
        int cellNum = 0;
        File outputFile = fileUtil.createFile(fileName,Constants.XLSX_EXTENTION);
        try (XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            XSSFSheet sheet = workbook.createSheet(woorkbookName);
            CellStyle style = workbook.createCellStyle();
            CellStyle boldStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            boldStyle.setFont(font);
            row = sheet.createRow(rowCount);
            DataFormat dataformater = workbook.createDataFormat();

            if (data.length() <= 0) {
                Cell cell = row.createCell(cellNum);
                cell.setCellStyle(boldStyle);
                cell.setCellValue("No records found");
            } else {

                for (String header : headers.values()) {
                    Cell cell = row.createCell(cellNum);
                    cell.setCellStyle(boldStyle);
                    sheet.autoSizeColumn(cellNum);
                    cell.setCellValue(header);
                    cellNum++;
                }
                for (int i = 0; i < data.length(); i++) {
                    rowCount++;
                    row = sheet.createRow(rowCount);
                    JSONObject column = data.getJSONObject(i);
                    cellNum = 0;
                    for (String header : headers.keySet()) {
                        Cell cell = row.createCell(cellNum);
                        cell.setCellValue(column.get(header).toString());
                        cellNum++;
                    }
                }
                for (int i = 0; i < data.length(); i++) {
                	sheet.autoSizeColumn(i);
                }
                
            }
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new CustomException("Error creating xls file " + e);
        }
        return outputFile;
    }

    /**
     * This method generates XLS file with given report data and report headers
     * 
     * @param data
     *            JSON object of Report data
     * @param reportHeaders
     *            report header name
     * @param transactionInfo
     *            Transaction information
     * @param transactionDetails
     *            TransactionDetails
     * @param propertyFileName
     *            property file name
     * @param workbookName
     *            WorkbookName
     * @return xlsFile returns generated XLS file
     * @throws FileNotFoundException
     * @throws IOException
     * @throws JSONException
     * @throws CustomException
     * 
     */
    public File createExcel1(
            JSONObject data,
            Map<String, String> reportHeaders,
            Map<String, String> transactionInfo,
            Map<String, String> transactionDetails,
            String fileName,
            String workbookName)
            throws IOException, CustomException {

        int rowCount = 0;
        Row row;
        int cellNum = 0;
        JSONArray reportData = null;
        JSONObject details = null;
        for (Object keys : data.keySet()) {

            if (data.get(keys.toString()) instanceof JSONObject) {
                details = data.getJSONObject(keys.toString());
            }
            if (data.get(keys.toString()) instanceof JSONArray) {
                reportData = data.getJSONArray((String) data.keys().next());
            }
        }

        File outputFile = fileUtil.createFile(fileName,Constants.XLSX_EXTENTION);

        try (XSSFWorkbook workbook = new XSSFWorkbook();
                FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            XSSFSheet sheet = workbook.createSheet(workbookName);
            Cell cell;
            row = sheet.createRow(rowCount);
            if (reportData.length() <= 0) {
                rowCount++;
                row = sheet.createRow(rowCount);
                cell = row.createCell(cellNum);
                cell.setCellValue("No records found");
            } else {
                for (Entry<String, String> entry : transactionDetails.entrySet()) {
                    rowCount++;
                    row = sheet.createRow(rowCount);
                    cell = row.createCell(cellNum);
                    cell.setCellStyle(style);
                    cell.setCellValue(entry.getValue());
                    cellNum++;
                    cell = row.createCell(cellNum);
                    cell.setCellValue(details.getString(entry.getKey()));
                    cellNum = 0;

                }
                rowCount++;
                row = sheet.createRow(rowCount);
                for (int i = 0; i < reportData.length(); i++) {
                    JSONObject column = reportData.getJSONObject(i);
                    for (Entry<String, String> entry : transactionInfo.entrySet()) {
                        rowCount++;
                        row = sheet.createRow(rowCount);
                        cell = row.createCell(cellNum);
                        cell.setCellStyle(style);
                        cell.setCellValue(entry.getValue());
                        cellNum++;
                        cell = row.createCell(cellNum);
                        cell.setCellValue(column.getString(entry.getKey()));
                        cellNum = 0;
                    }
                    rowCount++;
                    row = sheet.createRow(rowCount);
                    for (Entry<String, String> entry : reportHeaders.entrySet()) {
                        cell = row.createCell(cellNum);
                        cell.setCellStyle(style);
                        cell.setCellValue(entry.getValue());
                        cellNum++;
                    }
                    cellNum = 0;
                    rowCount++;
                    row = sheet.createRow(rowCount);
                    for (Object key : reportHeaders.keySet()) {
                        cell = row.createCell(cellNum);
                        cell.setCellValue(column.getString((String) key));
                        cellNum++;
                    }
                    rowCount++;
                    row = sheet.createRow(rowCount);
                    sheet.autoSizeColumn(cellNum);
                    cellNum = 0;
                }
            }
            workbook.write(outputStream);

        } catch (IOException e) {
            throw new CustomException("Error creating xls file " + e);
        }
        return outputFile;
    }

    /**
     * This method is used to create excel file with two sheet with different
     * format
     * 
     * @param data
     *            First sheet records
     * @param headers
     *            Record title of first sheet
     * @param propertyFileName
     *            Property file name to get directory location
     * @param fileName
     *            Output file name
     * @param secondSheetData
     *            Second sheet record data
     * @param secondSheetRowTitle
     *            Record title of second sheet
     * @param secondSheetDataKey
     *            key name of second sheet data
     * @param secondSheetDataSubKey
     *            sub key name of second sheet
     * @param woorkbookNames
     *            workbook name of sheet
     * @return output file
     * @throws IOException
     * @throws CustomException
     */
    @SuppressWarnings({ "unchecked" })
    public File createExcel(
            JSONArray data,
            Map<String, String> headers,
            String fileName,
            JSONArray secondSheetData,
            List<String> secondSheetRowTitle,
            Map<String, Object> secondSheetDataKey,
            List<String> secondSheetDataSubKey,
            List<String> woorkbookNames) throws IOException, CustomException {

        int rowCount = 0;
        Row row;
        int cellNum = 0;
        File outputFile = fileUtil.createFile(fileName,Constants.XLSX_EXTENTION);
     
        try (XSSFWorkbook workbook = new XSSFWorkbook();
                FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            XSSFSheet sheet = workbook.createSheet(woorkbookNames.get(0));
            CellStyle style = workbook.createCellStyle();
            CellStyle boldStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            boldStyle.setFont(font);
            row = sheet.createRow(rowCount);
            DataFormat dataformater = workbook.createDataFormat();
            if (data != null) {
                if (data.length() <= 0) {
                    Cell cell = row.createCell(cellNum);
                    cell.setCellStyle(boldStyle);
                    cell.setCellValue("No records found");
                } else {

                    for (String header : headers.values()) {
                        Cell cell = row.createCell(cellNum);
                        cell.setCellStyle(boldStyle);
                        sheet.autoSizeColumn(cellNum);
                        cell.setCellValue(header);
                        cellNum++;
                    }
                    for (int i = 0; i < data.length(); i++) {
                        rowCount++;
                        row = sheet.createRow(rowCount);
                        JSONObject column = data.getJSONObject(i);
                        cellNum = 0;
                        for (String header : headers.keySet()) {
                            Cell cell = row.createCell(cellNum);

                            if (column.get(header) instanceof Long) {
                                cell.setCellValue(column.getLong(header));
                                style.setDataFormat(dataformater.getFormat("#,##0"));
                                cell.setCellStyle(style);
                            } else if (column.get(header) instanceof Integer) {
                                cell.setCellValue(column.getInt(header));

                            } else if (column.get(header) instanceof Double) {
                                cell.setCellValue(column.getDouble(header));
                                style.setDataFormat(dataformater.getFormat("#,##0.00"));
                                cell.setCellStyle(style);
                            } else {
                                cell.setCellValue(column.getString(header));
                            }
                            sheet.autoSizeColumn(cellNum);
                            cellNum++;
                        }
                    }
                }
            }
            rowCount = 0;
            cellNum = 0;
            XSSFSheet secondSheet = workbook.createSheet(woorkbookNames.get(1));
            CellStyle cellStyle = workbook.createCellStyle();
            secondSheet.setColumnWidth(0, 6000);
            secondSheet.setColumnWidth(1, 4000);
            secondSheet.setColumnWidth(2, 2000);
            secondSheet.setColumnWidth(3, 5000);
            row = secondSheet.createRow(rowCount);
            for (String gltitle : secondSheetRowTitle) {
                Cell cell = row.createCell(cellNum + 1);
                cell.setCellStyle(style);
                cell.setCellValue(gltitle);
                cellNum++;

            }
            cellNum = 0;
            Object[] keys = secondSheetDataKey.keySet().toArray();
            Map<String, String> subTitle = (Map<String, String>) secondSheetDataKey.get(keys[0]);

            for (int i = 0; i < secondSheetData.length(); i++) {

                JSONObject column = secondSheetData.getJSONObject(i);
                rowCount = rowCount + 1;
                row = secondSheet.createRow(rowCount);
                cellNum = 0;
                Cell cell = row.createCell(cellNum);
                cell.setCellValue(subTitle.get(column.getString((String) keys[0])));
                JSONArray record = column.getJSONArray((String) keys[1]);
                cellNum = 0;
                for (int j = 0; j < record.length(); j++) {
                    JSONObject rowData = record.getJSONObject(j);
                    rowCount = rowCount + 1;
                    row = secondSheet.createRow(rowCount);
                    for (String glDetail : secondSheetDataSubKey) {
                        cell = row.createCell(cellNum);
                        cellNum++;
                        if (rowData.get(glDetail) instanceof String) {
                            cell.setCellValue(rowData.getString(glDetail));
                            if (cellNum == 1 || cellNum == 2) {
                                cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                                cell.setCellStyle(cellStyle);
                            } else if (cellNum == 3) {
                                CellStyle centerAlign = workbook.createCellStyle();
                                centerAlign.setAlignment(HorizontalAlignment.CENTER);
                                cell.setCellStyle(centerAlign);
                            }
                        } else {
                            cell.setCellValue(rowData.getDouble(glDetail));
                            style.setDataFormat(dataformater.getFormat("#,##0.00"));
                            cell.setCellStyle(style);
                        }
                    }
                    cellNum = 0;
                }
                rowCount = rowCount + 1;
                row = secondSheet.createRow(rowCount);
                cellNum = 0;
                cell = row.createCell(cellNum);
                cell.setCellValue((String) secondSheetDataKey.get(keys[2]));
                cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                cell.setCellStyle(cellStyle);
                cell = row.createCell(cellNum + 3);
                cell.setCellValue(column.getDouble((String) keys[2]));
                style.setDataFormat(dataformater.getFormat("#,##0.00"));
                cell.setCellStyle(style);
            }

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new CustomException("Error creating xls file " + e);
        }
        return outputFile;
    }
	
}
