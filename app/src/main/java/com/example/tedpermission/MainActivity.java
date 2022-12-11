package com.example.tedpermission;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codekidlabs.storagechooser.StorageChooser;
import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //UI
    Button btnSelectFile, btnProgressbar, btnChooseLocation, btnSQL;
    TextView tvFilePath;
    ProgressBar prbDemo;

    //Variables
    DialogProperties properties;
    FilePickerDialog dialog;
    StorageChooser chooser;

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
        btnSelectFile = (Button) findViewById(R.id.btnSelectFile);
        btnSelectFile.setOnClickListener(this);

        btnProgressbar = (Button) findViewById(R.id.btnProgressbar);
        btnProgressbar.setOnClickListener(this);

        btnChooseLocation = (Button) findViewById(R.id.btnChooseDir);
        btnChooseLocation.setOnClickListener(this);

        btnSQL = (Button) findViewById(R.id.btnSQLActivity);
        btnSQL.setOnClickListener(this);

        tvFilePath = (TextView) findViewById(R.id.tvFilePath);

        prbDemo = (ProgressBar)findViewById(R.id.prbDemo);
        prbDemo.setIndeterminateDrawable(new FadingCircle());

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
                    SetText(tvFilePath, "null");
                }
                else
                {
                    SetText(tvFilePath, TextUtils.join("\r\n", files));
                }

            }
        });


        //Chose folder to save
        // Initialize Builder
        chooser = new StorageChooser.Builder()
                .withActivity(MainActivity.this)
                .withFragmentManager(getFragmentManager())
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
                Toast.makeText(MainActivity.this, path, Toast.LENGTH_SHORT).show();
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
    boolean bToggle = false;
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnSelectFile:
                //select file
                dialog.show();

                break;

            case R.id.btnProgressbar:
                if (bToggle) {
                    prbDemo.setVisibility(View.VISIBLE);
                } else {
                    prbDemo.setVisibility(View.INVISIBLE);
                }
                bToggle = !bToggle;
                break;

            case R.id.btnChooseDir:
                // Show dialog whenever you want by
                chooser.show();
                break;

            case R.id.btnSQLActivity:
                Intent myIntent = new Intent(this, SQLActivity.class);
                this.startActivity(myIntent);
                break;
        }
    }
}