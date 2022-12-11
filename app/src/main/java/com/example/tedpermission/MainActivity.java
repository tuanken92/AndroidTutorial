package com.example.tedpermission;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //UI
    Button selectFileBtn;
    TextView filePathTv;


    //Variables
    DialogProperties properties;
    FilePickerDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Require permission
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

        //Init Variable
        InitVariables();

        //Init UI
        InitUI();
    }

    //Init UI
    void InitUI()
    {
        selectFileBtn = (Button) findViewById(R.id.selectFile_Btn);
        selectFileBtn.setOnClickListener(this);

        filePathTv = (TextView) findViewById(R.id.filePath_TextView);
    }


    //Init Variable
    void InitVariables()
    {
        //Dialog properties
        properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;
        properties.show_hidden_files = false;


        //File picker dlg
        dialog = new FilePickerDialog(MainActivity.this,properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
                for (String file_name: files
                     ) {
                    Toast.makeText(MainActivity.this, file_name, Toast.LENGTH_SHORT).show();
                }

                if(files.length == 0)
                {
                    SetText(filePathTv, "null");
                }
                else
                {
                    SetText(filePathTv, TextUtils.join("\r\n", files));
                }

            }
        });
    }

    //Set text to textview
    void SetText(TextView tv, String text)
    {
        if(tv == null)
            return;

        tv.setText(text);
    }
    //Permission listener
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };


    //Click action
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.selectFile_Btn:
                //select file
                dialog.show();
                break;

        }
    }
}