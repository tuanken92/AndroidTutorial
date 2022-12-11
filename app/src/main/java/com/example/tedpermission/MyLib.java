package com.example.tedpermission;

import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Random;


public class MyLib {
    public static final String TAG = "TuanNA";
    public static final int id_min = 0;
    public static final int id_ = 1000;

    public static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    public static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public static void Log(String mes)
    {
        Log.d(TAG, mes);
    }
    public static void Log(String mes, IOException e)
    {
        Log.d(TAG, mes + e.getMessage());
    }

    public static void Log(String mes, Exception e)
    {
        Log.d(TAG, mes + e.getMessage());
    }

    public static Student RandomStudent()
    {
        Student student = new Student(new Random().nextInt((id_ - id_min) + 1) + id_min,
                getRandomString(5),
                getRandomString(20),
                getRandomString(9));

        return student;
    }
    public static boolean Is_File_Exist(String filePath)
    {
        try{
            File file = new File(filePath);
            return  file.exists();
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
            return false;
        }

    }
    public static void readExcelFileFromAssets()
    {
        InputStream myInput;
        File file = new File(MyParam.file_inventory);

        try {

            //Read file excel
            myInput = new FileInputStream(MyParam.file_inventory);
            XSSFWorkbook workbook = new XSSFWorkbook(myInput);

            //Read from sheet 0
            XSSFSheet sheet = workbook.getSheetAt(0);

            int rowsCount = sheet.getPhysicalNumberOfRows();
            Log.i(MyParam.TAG,"number row = " + rowsCount);

            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            MyParam.list_products.clear();
            for (int r = 0; r<rowsCount; r++) {

                //Select row
                Row row = sheet.getRow(r);

                //Get number column
                int cellsCount = row.getPhysicalNumberOfCells();
                Log.i(MyParam.TAG,"number column = " + cellsCount);

                if(r <= MyParam.ROW_DATA)
                {


                    for (int c = 0; c<cellsCount; c++) {
                        String value = getCellAsString(row, c, formulaEvaluator);
                        String cellInfo = "r:"+r+"; c:"+c+"; v:"+value;
                        printlnToUser(cellInfo);
                    }
                }
                else
                {
                    Log.i(MyParam.TAG,"process row = " + r + ", column = " + cellsCount);
                    Products product_temp = new Products();
                    if (cellsCount >= 7)
                    {
                        //order
                        for(int k = 0;  k < cellsCount; k++)
                            Log.i(MyParam.TAG, "k = " + k + ": " +row.getCell(k).toString());

                        product_temp.Order = (int)Double.parseDouble(row.getCell(0).toString());

                        //group
                        product_temp.Group = (int)Double.parseDouble(row.getCell(1).toString());

                        //code
                        product_temp.Code = row.getCell(2).toString();

                        //name
                        product_temp.Name = row.getCell(3).toString();

                        //description
                        product_temp.Description = row.getCell(4).toString();

                        //System Quantity
                        product_temp.SystemQuantity = (int)Double.parseDouble(row.getCell(5).toString());

                        //RealQuantity
                        product_temp.InventoryQuantity = 0;
                        product_temp.PartNumber = "";
                        product_temp.Serial = "";
                    }
                    MyParam.list_products.add(product_temp);
                }




            }
        } catch (Exception e) {
            /* proper exception handling to be here */
            //printlnToUser(e.toString());
            Log.e(MyParam.TAG, e.getMessage());
        }

    }

    public static String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = ""+cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("dd/MM/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = ""+numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = ""+cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {
            /* proper error handling should be here */
            printlnToUser(e.toString());
        }
        return value;
    }


    public static void printlnToUser(String str) {
        Log(str);
    }
}
