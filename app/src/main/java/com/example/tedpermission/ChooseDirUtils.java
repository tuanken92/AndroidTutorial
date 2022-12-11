package com.example.tedpermission;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.codekidlabs.storagechooser.StorageChooser;

public class ChooseDirUtils {

    static StorageChooser chooser;
    public static String dir = null;

    public static void Init(Activity activity)
    {
        //Chose folder to save
        // Initialize Builder
        chooser = new StorageChooser.Builder()
                .withActivity(activity)
                .withFragmentManager(activity.getFragmentManager())
                .withMemoryBar(true)
                .allowCustomPath(true)
                //Choose directory to save file
                .setType(StorageChooser.DIRECTORY_CHOOSER)

                //File picker
                //.setType(StorageChooser.FILE_PICKER)
                .build();



        // get path that the user has chosen
        chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
            @Override
            public void onSelect(String path) {
                Log.e("SELECTED_PATH", path);
                dir = path;
                Toast.makeText(activity, path, Toast.LENGTH_SHORT).show();
            }


        });


    }

    public  static void ShowDlg()
    {
        chooser.show();

    }

}
