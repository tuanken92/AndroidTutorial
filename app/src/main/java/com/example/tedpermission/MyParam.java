package com.example.tedpermission;

import java.util.ArrayList;

public class MyParam {
    public static final String TAG = "TuanNA";

    public static final String KEY_INTENT_PUT_PRODUCT = "prd";
    public static final String KEY_INTENT_PRODUCT_QUANTITY = "InventoryQuantity";
    public static final String KEY_INTENT_PRODUCT_VISIBLE = "Visible";
    public static final int RESPONSE_EDIT_PRODUCT = 10001;

    //database
    static String DB_NAME = "myproduct.db";

    public static final String file_inventory = "/storage/emulated/0/Download/LAYOUT_KK_29.10.2021.xlsx";
    public static final int ROW_DATA = 10;
    public  static ArrayList<Products> list_products = new ArrayList<Products>();


}
