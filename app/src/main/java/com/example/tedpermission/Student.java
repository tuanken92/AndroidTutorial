package com.example.tedpermission;

public class Student {
    int id;
    String name;
    String address;
    String phone_number;

    public Student(int id, String name, String address, String phone_number) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
    }

    public Student(String name, String address, String phone_number) {
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
    }

    public String PrintInfo()
    {
        return String.format("id = %d, name = %s, address = %s, phone = %s", id, name, address, phone_number);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
