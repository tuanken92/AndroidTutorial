package com.example.tedpermission;

public class Products {

    public int Group;
    public String Code;
    public String Name;
    public String PartNumber;
    public String Serial;
    public String Description;
    public int SystemQuantity;
    public int InventoryQuantity;
    public int Order;
    public boolean Visible;

    public Products() {
        Group = 0;
        Code = "";
        Name = "";
        PartNumber = "";
        Serial = "";
        Description = "";
        SystemQuantity = 0;
        InventoryQuantity = 0;
        Order = 0;
        Visible = true;
    }

    public void PrintInfor()
    {
        String strInfo = String.format("%d| %d |%s %s (%s; %s) %s - %d %d - %b",
                Order, Group, Code, Name, PartNumber, Serial, Description, SystemQuantity, InventoryQuantity, Visible);
        MyLib.Log(strInfo);
    }
}
