package com.example.tedpermission;

import android.util.Log;

import java.io.File;
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

}
