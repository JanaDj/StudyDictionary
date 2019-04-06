package com.example.meangirl.dictionary.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.meangirl.dictionary.ExpandableListAdapter;
import com.example.meangirl.dictionary.R;
import com.example.meangirl.dictionary.SQLIte.Contract;
import com.example.meangirl.dictionary.SQLIte.DatabaseHelper;

public class ManageData extends AppCompatActivity {
    Button exportBtn, importBtn, deleteAllBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_data);
        connectWithXML();

        /**
         * exports all data from the database
         */
        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        /**
         * imports new data and writes it into a database
         */
        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        /**
         * clears all data from the database
         */
        deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ManageData.this);
                builder1.setMessage("Are you sure you want to delete all data from the disctionary?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "DELETE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DatabaseHelper dbHelper = new DatabaseHelper(ManageData.this);
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                //delete all data
                                dbHelper.onUpgrade(db,DatabaseHelper.DB_VERSION, DatabaseHelper.DB_VERSION+1);
                                Toast.makeText(getApplicationContext(), "Data has been successfully cleared", Toast.LENGTH_LONG).show();
                            }
                        });
                builder1.setNegativeButton(
                        "CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }
    /**
     * Method to connect xml components with java code
     */
    void connectWithXML(){
        exportBtn = findViewById(R.id.exportBtn);
        importBtn = findViewById(R.id.importBtn);
        deleteAllBtn = findViewById(R.id.deleteAllBtn);
    }
}
