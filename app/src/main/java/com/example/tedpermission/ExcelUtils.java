package com.example.tedpermission;

import android.content.Context;
import android.os.Environment;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtils {
    public static final String TAG = "ExcelUtil";
    private static Cell cell;
    private static Sheet sheet;
    private static Workbook workbook;
    private static CellStyle headerCellStyle;

    private static List<Student> importedExcelData;

    // PERMISSIONS
    public static final int REQUEST_PERMISSION_SETTING = 20;
    public static final int REQUEST_PERMISSION_ALL = 25;

    // EXCEL
    public static final String EXCEL_FILE_NAME = "students.xls";
    public static final String EXCEL_SHEET_NAME = "My Students";

    /**
     * Import data from Excel Workbook
     *
     * @param context - Application Context
     * @param fileName - Name of the excel file
     * @return importedExcelData
     */
    public static List<Student> readFromExcelWorkbook(Context context, String fileName) {
        return retrieveExcelFromStorage(context, fileName);
    }


    /**
     * Export Data into Excel Workbook
     *
     * @param context  - Pass the application context
     * @param fileName - Pass the desired fileName for the output excel Workbook
     * @param dataList - Contains the actual data to be displayed in excel
     */
    public static boolean exportDataIntoWorkbook(Context context, String fileName,
                                                 List<Student> dataList) {
        boolean isWorkbookWrittenIntoStorage;

        // Check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            MyLib.Log("Storage not available or read only");
            return false;
        }

        // Creating a New HSSF Workbook (.xls format)
        workbook = new HSSFWorkbook();

        setHeaderCellStyle();

        // Creating a New Sheet and Setting width for each column
        sheet = workbook.createSheet(EXCEL_SHEET_NAME);
        sheet.setColumnWidth(0, (15 * 400));
        sheet.setColumnWidth(1, (15 * 400));

        setHeaderRow();
        fillDataIntoExcel(dataList);
        isWorkbookWrittenIntoStorage = storeExcelInStorage(context, fileName);

        return isWorkbookWrittenIntoStorage;
    }

    /**
     * Checks if Storage is READ-ONLY
     *
     * @return boolean
     */
    private static boolean isExternalStorageReadOnly() {
        String externalStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(externalStorageState);
    }

    /**
     * Checks if Storage is Available
     *
     * @return boolean
     */
    private static boolean isExternalStorageAvailable() {
        String externalStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(externalStorageState);
    }

    /**
     * Setup header cell style
     */
    private static void setHeaderCellStyle() {
        headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(HSSFColor.AQUA.index);
        headerCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
    }

    /**
     * Setup Header Row
     */
    private static void setHeaderRow() {
        Row headerRow = sheet.createRow(0);

        cell = headerRow.createCell(0);
        cell.setCellValue("ID");
        cell.setCellStyle(headerCellStyle);

        cell = headerRow.createCell(1);
        cell.setCellValue("Name");
        cell.setCellStyle(headerCellStyle);

        cell = headerRow.createCell(2);
        cell.setCellValue("Address");
        cell.setCellStyle(headerCellStyle);


        cell = headerRow.createCell(3);
        cell.setCellValue("Phone Number");
        cell.setCellStyle(headerCellStyle);
    }

    /**
     * Fills Data into Excel Sheet
     * <p>
     * NOTE: Set row index as i+1 since 0th index belongs to header row
     *
     * @param dataList - List containing data to be filled into excel
     */
    private static void fillDataIntoExcel(List<Student> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            // Create a New Row for every new entry in list
            Row rowData = sheet.createRow(i + 1);

            // Create Cells for each row
            cell = rowData.createCell(0);
            cell.setCellValue(dataList.get(i).getId());

            cell = rowData.createCell(1);
            cell.setCellValue(dataList.get(i).getName());

            cell = rowData.createCell(2);
            cell.setCellValue(dataList.get(i).getAddress());

            cell = rowData.createCell(3);
            cell.setCellValue(dataList.get(i).getPhone_number());

        }
    }

    /**
     * Store Excel Workbook in external storage
     *
     * @param context  - application context
     * @param fileName - name of workbook which will be stored in device
     * @return boolean - returns state whether workbook is written into storage or not
     */
    private static boolean storeExcelInStorage(Context context, String fileName) {
        boolean isSuccess;
        File file = new File(/*context.getExternalFilesDir(null),*/ fileName);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            MyLib.Log("Writing file" + file);
            isSuccess = true;
        } catch (IOException e) {
            MyLib.Log( "Error writing Exception: ", e);
            isSuccess = false;
        } catch (Exception e) {
            MyLib.Log( "Failed to save file due to Exception: ", e);
            isSuccess = false;
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return isSuccess;
    }

    /**
     * Retrieve excel from External Storage
     *
     * @param context  - application context
     * @param fileName - name of workbook to be read
     * @return importedExcelData
     */
    private static List<Student> retrieveExcelFromStorage(Context context, String fileName) {
        importedExcelData = new ArrayList<>();

        File file = new File(context.getExternalFilesDir(null), fileName);
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
            MyLib.Log( "Reading from Excel" + file);

            // Create instance having reference to .xls file
            workbook = new HSSFWorkbook(fileInputStream);

            // Fetch sheet at position 'i' from the workbook
            sheet = workbook.getSheetAt(0);

            // Iterate through each row
            for (Row row : sheet) {
                int index = 0;
                List<String> rowDataList = new ArrayList<>();

                if (row.getRowNum() > 0) {
                    // Iterate through all the columns in a row (Excluding header row)
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        // Check cell type and format accordingly
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:

                                break;
                            case Cell.CELL_TYPE_STRING:
                                rowDataList.add(index, cell.getStringCellValue());
                                index++;
                                break;
                        }
                    }

                    importedExcelData.add(new Student(Integer.parseInt(rowDataList.get(0)),
                                                    rowDataList.get(1),
                                                    rowDataList.get(2),
                                                    rowDataList.get(3)));
                }

            }

        } catch (IOException e) {
            MyLib.Log( "Error Reading Exception: ", e);

        } catch (Exception e) {
            MyLib.Log( "Failed to read file due to Exception: ", e);

        } finally {
            try {
                if (null != fileInputStream) {
                    fileInputStream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return importedExcelData;
    }

}
