package com.example.meangirl.dictionary.Activities;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.meangirl.dictionary.DictionaryItem;
import com.example.meangirl.dictionary.ExpandableListAdapter;
import com.example.meangirl.dictionary.R;
import com.example.meangirl.dictionary.ReadingJson;
import com.example.meangirl.dictionary.SQLIte.Contract;
import com.example.meangirl.dictionary.SQLIte.DatabaseHelper;
import com.example.meangirl.dictionary.WritingJson;

import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;

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

                Writer output = null;

               // File file = new File("android.resource://" + getPackageName() + );
                try {
                    output = new BufferedWriter(new FileWriter(file));
                    WritingJson.writejsonStream(output, getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        });
        /**
         * imports new data and writes it into a database
         */
        importBtn.setOnClickListener(new View.OnClickListener() {
            ArrayList<DictionaryItem> itemsList = new ArrayList<>();
            @Override
            public void onClick(View v) {
                try {
                    itemsList = ReadingJson.readDictJsonFile(getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //adding the items to db
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
