package com.example.tedpermission;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "schoolManager";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "students";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PHONE_NUMBER = "phone_number";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        MyLib.Log("DatabaseHandler constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_students_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT)", TABLE_NAME, KEY_ID, KEY_NAME, KEY_ADDRESS, KEY_PHONE_NUMBER);
        db.execSQL(create_students_table);
        MyLib.Log("onCreate db");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String drop_students_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(drop_students_table);
        onCreate(db);
        MyLib.Log("onUpgrade db");
    }

    public void addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, student.getName());
        values.put(KEY_ADDRESS, student.getAddress());
        values.put(KEY_PHONE_NUMBER, student.getPhone_number());

        db.insert(TABLE_NAME, null, values);
        db.close();

        MyLib.Log("addStudent: "+student.PrintInfo());
    }

    public Student getStudent(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, KEY_ID + " = ?", new String[] { String.valueOf(studentId) },null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        Student student = new Student(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        MyLib.Log("getStudent: " + student.PrintInfo());
        return student;
    }

    public List<Student> getAllStudents() {
        List<Student>  studentList = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", TABLE_NAME);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            Student student = new Student(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            studentList.add(student);
            MyLib.Log("getAllStudents: "+ student.PrintInfo());
            cursor.moveToNext();
        }
        return studentList;
    }


    public void updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, student.getName());
        values.put(KEY_ADDRESS, student.getAddress());
        values.put(KEY_PHONE_NUMBER, student.getPhone_number());

        db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] { String.valueOf(student.getId()) });
        db.close();
        MyLib.Log("updateStudent: "+ student.PrintInfo());
    }


    public void deleteStudent(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(studentId) });
        db.close();
        MyLib.Log("deleteStudent: "+ studentId);
    }

}
