package com.example.tedpermission;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.FadingCircle;

import java.util.List;


public class SQLActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHandler databaseHandler;
    Button btnCreateObject, btnAddStudent, btnDelStudent, btnGetAllStudent, btnGetStudent, btnEditStudent, btnExportToFile;
    EditText edtIdStudent;
    ProgressBar prbExportFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlactivity);

        //Init Variable
        InitVariables();

        //Init UI
        InitUI();
    }

    //Init UI
    void InitUI()
    {
        btnCreateObject = findViewById(R.id.btnCreateObject);
        btnCreateObject.setOnClickListener(this);

        btnAddStudent = findViewById(R.id.btnAddStudent);
        btnAddStudent.setOnClickListener(this);

        btnDelStudent = findViewById(R.id.btnDelStudent);
        btnDelStudent.setOnClickListener(this);

        btnGetAllStudent = findViewById(R.id.btnGetAllStudent);
        btnGetAllStudent.setOnClickListener(this);

        btnGetStudent = findViewById(R.id.btnGetStudent);
        btnGetStudent.setOnClickListener(this);

        btnEditStudent = findViewById(R.id.btnEditStudent);
        btnEditStudent.setOnClickListener(this);

        btnExportToFile = findViewById(R.id.btnExportToFile);
        btnExportToFile.setOnClickListener(this);

        edtIdStudent = findViewById(R.id.edtIdStudent);

    }

    void InitVariables()
    {
        prbExportFile = (ProgressBar)findViewById(R.id.prbExport);
        prbExportFile.setIndeterminateDrawable(new FadingCircle());
    }

    public void onClick(View view) {
        int idStudent = Integer.parseInt(edtIdStudent.getText()+"");
        switch (view.getId())
        {
            case R.id.btnCreateObject:
                databaseHandler = new DatabaseHandler(this);
                break;

            case R.id.btnAddStudent:
                databaseHandler.addStudent(MyLib.RandomStudent());
                break;

            case R.id.btnEditStudent:
                Student std = databaseHandler.getStudent(idStudent);
                std.setName(std.getName()+"_Updated");
                databaseHandler.updateStudent(std);
                break;

            case R.id.btnDelStudent:
                databaseHandler.deleteStudent(idStudent);
                break;

            case R.id.btnGetStudent:
                databaseHandler.getStudent(idStudent);
                break;

            case R.id.btnGetAllStudent:
                databaseHandler.getAllStudents();
                break;

            case R.id.btnExportToFile:
                prbExportFile.setVisibility(View.VISIBLE);
                prbExportFile.requestFocus();
                List<Student> listStudent =  databaseHandler.getAllStudents();
                //ChooseDirUtils.Init(this);
                //ChooseDirUtils.ShowDlg();
//                String dir = ChooseDirUtils.dir;
//                if(dir == null)
//                {
//                    MyLib.Log("dir = null!");
//                    return;
//                }
                String fileName = "/storage/emulated/0/Download/myStudents.xls";
                ExcelUtils.exportDataIntoWorkbook(this, fileName, listStudent);
                MyLib.Log("Done!" + fileName);
                prbExportFile.setVisibility(View.INVISIBLE);
                break;


        }
    }
}