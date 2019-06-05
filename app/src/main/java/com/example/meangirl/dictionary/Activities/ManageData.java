package com.example.meangirl.dictionary.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Output;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.meangirl.dictionary.DictionaryItem;
import com.example.meangirl.dictionary.ExpandableListAdapter;
import com.example.meangirl.dictionary.FileManager;
import com.example.meangirl.dictionary.R;
import com.example.meangirl.dictionary.ReadingJson;
import com.example.meangirl.dictionary.SQLIte.Contract;
import com.example.meangirl.dictionary.SQLIte.DatabaseHelper;
import com.example.meangirl.dictionary.WritingJson;

import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;

public class ManageData extends AppCompatActivity {
    Button exportBtn, importBtn, deleteAllBtn;
    int FILE_CREATE_REQUEST, FILE_OPEN_REQUEST;
    ArrayList<DictionaryItem> itemsList;
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
              //  open files
                FILE_CREATE_REQUEST = 1;
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/json");
               startActivityForResult(intent,FILE_CREATE_REQUEST);
            }
        });
        /**
         * imports new data and writes it into a database
         */
        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsList = new ArrayList<>();
                FILE_OPEN_REQUEST = 2;
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/json");
                startActivityForResult(intent,FILE_OPEN_REQUEST);

//                try {
//                    itemsList = ReadingJson.readDictJsonFile(getApplicationContext());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                //adding the items to db

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
     * To be implemented - this will handle response for intents for create file and select file
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_CANCELED) {
            // code to handle cancelled state
        } else if (requestCode == FILE_CREATE_REQUEST) {
            Uri uri = data.getData();
            try {
              OutputStream  os = getApplicationContext().getContentResolver().openOutputStream(uri);
              WritingJson.writejsonStream(os,getApplicationContext());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == FILE_OPEN_REQUEST){
            Uri uri = data.getData();
            try {
                InputStream is = getApplicationContext().getContentResolver().openInputStream(uri);
                String json = ReadingJson.readJsonFile(is);
               itemsList =  ReadingJson.readDictJsonFile(getApplicationContext(),json);
               Toast.makeText(getApplicationContext(), "Uspesno procitao json", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!itemsList.isEmpty()){
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                for (DictionaryItem item: itemsList) {
                    contentValues.put(Contract.TermsSchema.FIELD_TERM, item.getTerm());
                    contentValues.put(Contract.TermsSchema.FIELD_DEFINITION, item.getDefinition() );
                    //inserting into the db
                    db.insert(Contract.TermsSchema.TABLE_NAME, null, contentValues);
                }

            }
        }
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
